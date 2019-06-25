package com.sixbexchange.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.blankj.utilcode.util.CacheUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.http.HandlerHelper;
import com.fivefivelike.mybaselibrary.http.WebSocketRequest;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.ListUtils;
import com.fivefivelike.mybaselibrary.utils.ToastUtil;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.sixbexchange.R;
import com.sixbexchange.adapter.DepthAdapter;
import com.sixbexchange.adapter.TrOkexOrdersAdapter;
import com.sixbexchange.base.CacheName;
import com.sixbexchange.entity.bean.DepthBean;
import com.sixbexchange.entity.bean.OrderBean;
import com.sixbexchange.entity.bean.TradeDetailBean;
import com.sixbexchange.entity.bean.TransactionBean;
import com.sixbexchange.entity.bean.WebSocketDepthBean;
import com.sixbexchange.mvp.databinder.TrTransactionBinder;
import com.sixbexchange.mvp.delegate.TrTransactionDelegate;
import com.sixbexchange.utils.BigUIUtil;
import com.sixbexchange.utils.UserSet;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrTransactionFragment extends BaseDataBindFragment<TrTransactionDelegate, TrTransactionBinder> {

    boolean isVisibility = false;
    String TAG = getClass().getSimpleName();
    int depthSize = 5;

    @Override
    protected Class<TrTransactionDelegate> getDelegateClass() {
        return TrTransactionDelegate.class;
    }

    @Override
    public TrTransactionBinder getDataBinder(TrTransactionDelegate viewDelegate) {
        return new TrTransactionBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.viewHolder.fl_opponent_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tradeTypeChange(1);
            }
        });
        viewDelegate.viewHolder.fl_limit_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tradeTypeChange(0);
            }
        });
        viewDelegate.viewHolder.tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tradeModeChange(1);
            }
        });
        viewDelegate.viewHolder.tv_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tradeModeChange(0);
            }
        });
        tradeTypeChange(0);
        tradeModeChange(0);
        viewDelegate.viewHolder.tv_order_price.addTextChangedListener(priceTextWatcher);
        viewDelegate.viewHolder.tv_order_num.addTextChangedListener(numTextWatcher);
        viewDelegate.viewHolder.tv_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOrder(tradeMode == 0 ? 1 : 4);
            }
        });
        viewDelegate.viewHolder.tv_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOrder(tradeMode == 0 ? 2 : 3);
            }
        });
    }

    private void setOrder(int type) {
        if (TextUtils.isEmpty(viewDelegate.viewHolder.tv_order_price.getText().toString())) {
            ToastUtil.show("请输入价格");
            return;
        }
        if (TextUtils.isEmpty(viewDelegate.viewHolder.tv_order_num.getText().toString())) {
            ToastUtil.show("请输入数量");
            return;
        }
        if (!UiHeplUtils.isDouble(viewDelegate.viewHolder.tv_order_num.getText().toString())) {
            ToastUtil.show("请输入正确的数量");
            return;
        }
        if (!UiHeplUtils.isDouble(viewDelegate.viewHolder.tv_order_price.getText().toString()) &&
                !ObjectUtils.equals("市价", viewDelegate.viewHolder.tv_order_price.getText().toString())) {
            ToastUtil.show("请输入正确的价格");
            return;
        }
        boolean isMarketPrice = ObjectUtils.equals("市价",
                viewDelegate.viewHolder.tv_order_price.getText().toString());
        boolean isBuy = type == 1 || type == 4;
        addRequest(binder.placeOrder(
                tradeDetailBean.getExchange(),
                isMarketPrice ? "1" : "0",
                isMarketPrice ? (isBuy ? asksAdapter.getDatas().get(depthSize - 1).getPrice() : bidsAdapter.getDatas().get(0).getPrice()) : viewDelegate.viewHolder.tv_order_price.getText().toString(),
                type,
                tradeDetailBean.getOnlykey(),
                tradeDetailBean.getCurrencyPair(),
                viewDelegate.viewHolder.tv_order_num.getText().toString(),
                isBuy ? "b" : "s",
                this
        ));

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initWs();
        initDepth(new ArrayList<DepthBean>(), new ArrayList<DepthBean>());
        viewDelegate.viewHolder.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshData();
            }
        });
        initList(new ArrayList<OrderBean>());
    }


    private void onRefreshData() {
        if (tradeDetailBean != null) {
            viewDelegate.viewHolder.swipeRefreshLayout.setRefreshing(true);
            addRequest(binder.accountopen(tradeDetailBean.getExchange(),
                    tradeDetailBean.getOnlykey(), this));
            addRequest(binder.accountclose(tradeDetailBean.getExchange(),
                    tradeDetailBean.getOnlykey(), this));
            addRequest(binder.accountgetOrders(
                    tradeDetailBean.getExchange(),
                    tradeDetailBean.getCurrencyPair(),
                    this));
        }
    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        sendWs(true);
        isVisibility = true;
    }

    //订阅深度和指数
    private void sendWs(boolean isSubscribe) {
        if (WebSocketRequest.getInstance().getCallBack(TAG) != null &&
                tradeDetailBean != null) {
            Map<String, String> map = new HashMap<>();
            map.put("uri", (isSubscribe ? "subscribe" : "unsubscribe") + "-single-tick-verbose");
            map.put("contract", tradeDetailBean.getDelivery());
            WebSocketRequest.getInstance().sendData(GsonUtil.getInstance().toJson(map));
            map.put("uri", (isSubscribe ? "subscribe" : "unsubscribe") + "-single-tick-verbose");
            map.put("contract", tradeDetailBean.getIndex());
            WebSocketRequest.getInstance().sendData(GsonUtil.getInstance().toJson(map));
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        isVisibility = false;
    }

    TrOkexOrdersAdapter adapter;

    private void initList(List<OrderBean> data) {
        if (adapter == null) {
            adapter = new TrOkexOrdersAdapter(getActivity(), data);
            adapter.setDefaultClickLinsener(new DefaultClickLinsener() {
                @Override
                public void onClick(View view, int position, Object item) {
                    //撤单
                    addRequest(binder.cancelOrder(
                            tradeDetailBean.getExchange(),
                            adapter.getDatas().get(position).getExchange_oid(),
                            TrTransactionFragment.this
                    ));
                }
            });
            viewDelegate.viewHolder.recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            viewDelegate.viewHolder.recycler_view.setAdapter(adapter);
        } else {
            adapter.setTradeDetailBean(tradeDetailBean);
            adapter.setData(data);
            viewDelegate.viewHolder.lin_no_order.setVisibility(
                    ListUtils.isEmpty(data) ? View.VISIBLE : View.GONE
            );
            viewDelegate.viewHolder.lin_close_all.setVisibility(
                    !ListUtils.isEmpty(data) ? View.VISIBLE : View.GONE
            );
            viewDelegate.viewHolder.lin_close_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tradeDetailBean != null) {
                        addRequest(binder.cancelAllOrder(
                                tradeDetailBean.getExchange(),
                                tradeDetailBean.getCurrencyPair(),
                                TrTransactionFragment.this
                        ));
                    }
                }
            });
        }
    }

    TradeDetailBean tradeDetailBean;

    @Override
    public void onStopNet(int requestCode, BaseDataBind.StopNetMode type) {
        super.onStopNet(requestCode, type);
        viewDelegate.viewHolder.swipeRefreshLayout.setRefreshing(false);
    }

    TransactionBean transactionBean = new TransactionBean();

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                //订单列表
                initList(GsonUtil.getInstance().toList(data, OrderBean.class));
                //订阅ws
                sendWs(true);
                break;
            case 0x124:
                transactionBean.setAvailableOpenMore(
                        GsonUtil.getInstance().getValue(data, "availableOpenMore", String.class));
                transactionBean.setUsableOpenMore(
                        GsonUtil.getInstance().getValue(data, "usableOpenMore", String.class));
                transactionBean.setAvailableOpenSpace(
                        GsonUtil.getInstance().getValue(data, "availableOpenSpace", String.class));
                transactionBean.setUsableOpenSpace(
                        GsonUtil.getInstance().getValue(data, "usableOpenSpace", String.class));
                tradeModeChange(tradeMode);
                //订阅ws
                sendWs(true);
                break;
            case 0x127:
                transactionBean.setAvailableflatSpace(
                        GsonUtil.getInstance().getValue(data, "availableflatSpace", String.class));
                transactionBean.setAvailableflatMore(
                        GsonUtil.getInstance().getValue(data, "availableflatMore", String.class));
                tradeModeChange(tradeMode);
                //订阅ws
                sendWs(true);
                break;
            case 0x125:
                //下单
                onRefreshData();
                break;
            case 0x126:
                //撤单
                onRefreshData();
                break;
        }
    }


    public void initTradeDetail(TradeDetailBean s) {
        //取消订阅
        sendWs(false);
        tradeDetailBean = s;
        CacheUtils.getInstance().put(CacheName.TradeExchangeCache, tradeDetailBean.getExchange());
        CacheUtils.getInstance().put(CacheName.TradeIdCache, tradeDetailBean.getId());
        CacheUtils.getInstance().put(CacheName.TradeCoinCache, tradeDetailBean.getCurrency());

        viewDelegate.viewHolder.tv_order_price_unit.setText(tradeDetailBean.getPriceUnit());
        viewDelegate.viewHolder.tv_show_price_type.setText("价格(" + tradeDetailBean.getPriceUnit() + ")");
        viewDelegate.viewHolder.tv_order_num_unit.setText(tradeDetailBean.getAmountUnit());
        viewDelegate.viewHolder.tv_coin_num.setText("数量(" + tradeDetailBean.getAmountUnit() + ")");
        viewDelegate.viewHolder.tv_order_type.setText(tradeDetailBean.getCurrencyPairName());

        viewDelegate.viewHolder.tv_fund_rate.setText(tradeDetailBean.getRate() + "%");
        ((View) viewDelegate.viewHolder.tv_fund_rate.getParent()).setVisibility(
                TextUtils.isEmpty(tradeDetailBean.getRate()) ? View.GONE : View.VISIBLE
        );
        ((View) viewDelegate.viewHolder.tv_latest_index.getParent()).setVisibility(View.GONE);

        //设置深度 价格 数量 小数点长度
        asksAdapter.setPriceSize(UiHeplUtils.numPointAfterLength(tradeDetailBean.getMinPriceUnit()));
        bidsAdapter.setPriceSize(UiHeplUtils.numPointAfterLength(tradeDetailBean.getMinPriceUnit()));

        asksAdapter.setNumSize(UiHeplUtils.numPointAfterLength(tradeDetailBean.getMinAmountUnit()));
        bidsAdapter.setNumSize(UiHeplUtils.numPointAfterLength(tradeDetailBean.getMinAmountUnit()));

        //注册ws
        //        Map<String, String> map = new HashMap<>();
        //        map.put("uri", "subscribe-single-tick-verbose");
        //        map.put("contract", tradeDetailBean.getCurrencyPair());
        //        WebSocketRequest.getInstance().sendData(GsonUtil.getInstance().toJson(map));


        //清除可用可开 指数 价格 深度 订单，重新获取
        viewDelegate.viewHolder.tv_buy_type_num.setText("--");
        viewDelegate.viewHolder.tv_buy_available.setText("--");
        viewDelegate.viewHolder.tv_sell_available.setText("--");
        viewDelegate.viewHolder.tv_sell_type_num.setText("--");
        viewDelegate.viewHolder.tv_latest_index.setText("--");
        viewDelegate.viewHolder.tv_now_price.setText("--");
        transactionBean.setAvailableflatMore("");
        transactionBean.setAvailableflatSpace("");
        transactionBean.setAvailableOpenMore("");
        transactionBean.setAvailableOpenSpace("");
        transactionBean.setUsableOpenMore("");
        transactionBean.setUsableOpenSpace("");
        initDepth(new ArrayList<DepthBean>(), new ArrayList<DepthBean>());
        initList(new ArrayList<OrderBean>());
        onRefreshData();


        //选择交易对
        viewDelegate.viewHolder.tv_order_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TransactionFragment) getParentFragment()).showSelectCurrencyPair(v);
            }
        });

        if (tradeDetailBean.getKline() == 1) {
            viewDelegate.viewHolder.lin_to_kline.setEnabled(true);
            viewDelegate.viewHolder.iv_to_kline.setVisibility(View.VISIBLE);
        } else {
            viewDelegate.viewHolder.lin_to_kline.setEnabled(false);
            viewDelegate.viewHolder.iv_to_kline.setVisibility(View.GONE);
        }

        //跳转k线
        viewDelegate.viewHolder.lin_to_kline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getParentFragment().getParentFragment() instanceof MainFragment) {
                    ((MainFragment) getParentFragment().getParentFragment()).startBrotherFragment(
                            CoinInfoFragment.newInstance(
                                    tradeDetailBean.getDelivery(),
                                    tradeDetailBean.getCurrencyPairName()
                            )
                    );
                }
            }
        });

    }

    TextWatcher priceTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (UiHeplUtils.isDouble(s.toString()) && tradeDetailBean != null) {
                if (UiHeplUtils.numPointAfterLength(tradeDetailBean.getMinPriceUnit()) <
                        UiHeplUtils.numPointAfterLength(s.toString()) ||
                        (UiHeplUtils.numPointAfterLength(tradeDetailBean.getMinPriceUnit()) == 0 &&
                                s.toString().contains(".")
                        )) {
                    viewDelegate.viewHolder.tv_order_price.removeTextChangedListener(priceTextWatcher);
                    String substring = s.toString().substring(0, s.toString().length() - 1);
                    viewDelegate.viewHolder.tv_order_price.setText(substring);
                    viewDelegate.viewHolder.tv_order_price.setSelection(substring.length());
                    viewDelegate.viewHolder.tv_order_price.addTextChangedListener(priceTextWatcher);
                }
            }
        }
    };
    TextWatcher numTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (UiHeplUtils.isDouble(s.toString()) && tradeDetailBean != null) {
                if (UiHeplUtils.numPointAfterLength(tradeDetailBean.getMinAmountUnit()) <
                        UiHeplUtils.numPointAfterLength(s.toString())) {
                    viewDelegate.viewHolder.tv_order_num.removeTextChangedListener(numTextWatcher);
                    String substring = s.toString().substring(0, s.toString().length() - 1);
                    viewDelegate.viewHolder.tv_order_num.setText(substring);
                    viewDelegate.viewHolder.tv_order_num.setSelection(substring.length());
                    viewDelegate.viewHolder.tv_order_num.addTextChangedListener(numTextWatcher);
                }
            }
        }
    };
    DepthAdapter asksAdapter;
    DepthAdapter bidsAdapter;
    int tradeMode = 0;

    //开仓0  平仓1
    private void tradeModeChange(int type) {
        tradeMode = type;
        if (type == 0) {
            viewDelegate.viewHolder.tv_close.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewDelegate.viewHolder.tv_open.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
            viewDelegate.viewHolder.tv_close.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewDelegate.viewHolder.tv_open.setTextColor(CommonUtils.getColor(R.color.mark_color));

            viewDelegate.viewHolder.tv_buy.setText("买入开多(看涨)");
            viewDelegate.viewHolder.tv_sell.setText("卖出开空(看跌)");


            viewDelegate.viewHolder.tv_buy_left.setText("可用");
            viewDelegate.viewHolder.tv_buy_type.setText("可开多");
            viewDelegate.viewHolder.tv_sell_left.setText("可用");
            viewDelegate.viewHolder.tv_sell_type.setText("可开空");

            viewDelegate.viewHolder.tv_buy_type_num.setVisibility(View.VISIBLE);
            viewDelegate.viewHolder.tv_sell_type_num.setVisibility(View.VISIBLE);

        } else {
            viewDelegate.viewHolder.tv_close.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
            viewDelegate.viewHolder.tv_open.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewDelegate.viewHolder.tv_close.setTextColor(CommonUtils.getColor(R.color.mark_color));
            viewDelegate.viewHolder.tv_open.setTextColor(CommonUtils.getColor(R.color.color_font4));

            viewDelegate.viewHolder.tv_buy.setText("买入平空");
            viewDelegate.viewHolder.tv_sell.setText("卖出平多");

            viewDelegate.viewHolder.tv_buy_left.setText("可平空");
            viewDelegate.viewHolder.tv_buy_type.setText("");
            viewDelegate.viewHolder.tv_sell_left.setText("可平多");
            viewDelegate.viewHolder.tv_sell_type.setText("");

            viewDelegate.viewHolder.tv_buy_type_num.setVisibility(View.GONE);
            viewDelegate.viewHolder.tv_sell_type_num.setVisibility(View.GONE);
        }

        if (transactionBean != null && tradeDetailBean != null) {

            if (type == 0) {
                viewDelegate.viewHolder.tv_buy_type_num.setText(transactionBean.getAvailableOpenMore() + tradeDetailBean.getAmountUnit());
                viewDelegate.viewHolder.tv_buy_available.setText(transactionBean.getUsableOpenMore() + tradeDetailBean.getMarginUnit());

                viewDelegate.viewHolder.tv_sell_type_num.setText(transactionBean.getAvailableOpenSpace() + tradeDetailBean.getAmountUnit());
                viewDelegate.viewHolder.tv_sell_available.setText(transactionBean.getUsableOpenSpace() + tradeDetailBean.getMarginUnit());
            } else {
                viewDelegate.viewHolder.tv_buy_available.setText(transactionBean.getAvailableflatSpace() + tradeDetailBean.getAmountUnit());
                viewDelegate.viewHolder.tv_sell_available.setText(transactionBean.getAvailableflatMore() + tradeDetailBean.getAmountUnit());
            }
        }
    }

    private void depthPrice(String price, String amount) {
        tradeTypeChange(0);
        viewDelegate.viewHolder.tv_order_price.setText(
                BigUIUtil.getinstance().bigPrice(price));
        viewDelegate.viewHolder.tv_order_num.setText(amount);
    }

    //市价1 限价0切换
    private void tradeTypeChange(int type) {
        if (type == 0) {
            viewDelegate.viewHolder.tv_opponent_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewDelegate.viewHolder.tv_limit_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
            viewDelegate.viewHolder.tv_opponent_price.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewDelegate.viewHolder.tv_limit_price.setTextColor(CommonUtils.getColor(R.color.mark_color));
            viewDelegate.viewHolder.tv_order_price.setText("");
            viewDelegate.viewHolder.tv_order_price.setEnabled(true);
        } else {
            viewDelegate.viewHolder.tv_opponent_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
            viewDelegate.viewHolder.tv_limit_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewDelegate.viewHolder.tv_opponent_price.setTextColor(CommonUtils.getColor(R.color.mark_color));
            viewDelegate.viewHolder.tv_limit_price.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewDelegate.viewHolder.tv_order_price.setText("市价");
            viewDelegate.viewHolder.tv_order_price.setEnabled(false);
        }
    }

    //显示深度
    private void initDepth(List<DepthBean> asks, List<DepthBean> bids) {
        if (asksAdapter == null) {
            asksAdapter = new DepthAdapter(getActivity(), asks, false);
            asksAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    depthPrice(asksAdapter.getDatas().get(position).getPrice(),
                            asksAdapter.getDatas().get(position).getVolume());
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
            viewDelegate.viewHolder.rv_buy.setLayoutManager(new LinearLayoutManager(getActivity()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            viewDelegate.viewHolder.rv_buy.setAdapter(asksAdapter);
        } else {
            asksAdapter.setData(asks);
        }
        if (bidsAdapter == null) {
            bidsAdapter = new DepthAdapter(getActivity(), bids, true);
            bidsAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    depthPrice(bidsAdapter.getDatas().get(position).getPrice(),
                            bidsAdapter.getDatas().get(position).getVolume());
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
            viewDelegate.viewHolder.rv_sell.setLayoutManager(new LinearLayoutManager(getActivity()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            viewDelegate.viewHolder.rv_sell.setAdapter(bidsAdapter);
        } else {
            bidsAdapter.setData(bids);
        }
    }

    private void initWs() {
        WebSocketRequest.getInstance().addCallBack(TAG, new WebSocketRequest.WebSocketCallBack() {
            @Override
            public void onDataSuccess(String name, String data, String info, int status) {
                if (ObjectUtils.equals(TAG, name) &&
                        !WebSocketRequest.getInstance().isSend() && isVisibility
                        ) {
                    WebSocketDepthBean webSocketDepthBean =
                            GsonUtil.getInstance().toObj(data, "data", WebSocketDepthBean.class);
                    if (webSocketDepthBean != null &&
                            ObjectUtils.equals(tradeDetailBean.getDelivery(),
                                    webSocketDepthBean.getContract()) &&
                            !ListUtils.isEmpty(webSocketDepthBean.getAsks()) &&
                            !ListUtils.isEmpty(webSocketDepthBean.getBids()) &&
                            !TextUtils.isEmpty(webSocketDepthBean.getLast())) {
                        //深度
                        HandlerHelper.getinstance().put(tradeDetailBean.getDelivery(),
                                webSocketDepthBean);
                    } else if (webSocketDepthBean != null &&
                            ObjectUtils.equals(tradeDetailBean.getIndex(),
                                    webSocketDepthBean.getContract())) {
                        //指数
                        HandlerHelper.getinstance().put(tradeDetailBean.getIndex(),
                                webSocketDepthBean);
                    }
                }
            }

            @Override
            public void onDataError(String name, String data, String info, int status) {

            }
        });
        HandlerHelper.getinstance().initHander(TAG,
                viewDelegate.viewHolder.rv_buy,
                new HandlerHelper.OnUpdataLinsener() {
                    @Override
                    public void onUpdataLinsener(Object val) {
                        if (isVisibility) {
                            if (val instanceof WebSocketDepthBean && tradeDetailBean != null) {
                                if (ObjectUtils.equals(tradeDetailBean.getDelivery(),
                                        ((WebSocketDepthBean) val).getContract())) {
                                    showNowPrice(((WebSocketDepthBean) val).getLast());
                                    changeDepth((WebSocketDepthBean) val);
                                } else if (ObjectUtils.equals(tradeDetailBean.getIndex(),
                                        ((WebSocketDepthBean) val).getContract())) {
                                    ((View) viewDelegate.viewHolder.tv_latest_index.getParent()).setVisibility(View.VISIBLE);
                                    viewDelegate.viewHolder.tv_latest_index.setText(
                                            BigUIUtil.getinstance().getSymbol(tradeDetailBean.getPriceUnit()) +
                                                    BigUIUtil.getinstance().bigPrice(((WebSocketDepthBean) val).getLast())
                                    );
                                }
                            }
                        }
                    }
                });
        HandlerHelper.getinstance().setDelayMillis(400);
    }

    String oldPrice = "";

    private void showNowPrice(String price) {
        viewDelegate.viewHolder.tv_now_price.setTextColor(CommonUtils.getColor(UserSet.getinstance().getRiseColor()));
        if (!TextUtils.isEmpty(oldPrice)) {
            if (new BigDecimal(price).doubleValue() < new BigDecimal(oldPrice).doubleValue()) {
                viewDelegate.viewHolder.tv_now_price.setTextColor(CommonUtils.getColor(UserSet.getinstance().getDropColor()));
            }
        }
        oldPrice = price;
        viewDelegate.viewHolder.tv_now_price.setText(
                BigUIUtil.getinstance().getSymbol(tradeDetailBean.getPriceUnit()) +
                        BigUIUtil.getinstance().bigPrice(price));
    }

    private void changeDepth(WebSocketDepthBean val) {
        if (ObjectUtils.equals(tradeDetailBean.getDelivery(), ((WebSocketDepthBean) val).getContract())
                && val.getBids().size() >= depthSize &&
                val.getAsks().size() >= depthSize) {
            List<DepthBean> depthBeans = ((WebSocketDepthBean) val)
                    .getAsks().subList(0, depthSize);
            Collections.reverse(depthBeans);
            initDepth(
                    depthBeans,
                    ((WebSocketDepthBean) val).getBids().subList(0, depthSize));
        }
    }
}
