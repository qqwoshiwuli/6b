package com.sixbexchange.mvp.delegate;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.CacheUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.circledialog.res.drawable.RadiuBg;
import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.fivefivelike.mybaselibrary.http.HandlerHelper;
import com.fivefivelike.mybaselibrary.http.WebSocketRequest;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.ListUtils;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.fivefivelike.mybaselibrary.view.IconFontTextview;
import com.fivefivelike.mybaselibrary.view.LineTextView;
import com.fivefivelike.mybaselibrary.view.RoundButton;
import com.fivefivelike.mybaselibrary.view.SingleLineZoomTextView;
import com.fivefivelike.mybaselibrary.view.SwipeRefreshLayout;
import com.sixbexchange.R;
import com.sixbexchange.adapter.DepthAdapter;
import com.sixbexchange.adapter.WsTrBMOrdersAdapter;
import com.sixbexchange.base.CacheName;
import com.sixbexchange.entity.bean.DepthBean;
import com.sixbexchange.entity.bean.TradeDetailBean;
import com.sixbexchange.entity.bean.TransactionBean;
import com.sixbexchange.entity.bean.WebSocketDepthBean;
import com.sixbexchange.entity.bean.WsOrderBean;
import com.sixbexchange.utils.BigUIUtil;
import com.sixbexchange.utils.UserSet;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BMTrFragmentDelegate extends BaseDelegate {
    public ViewHolder viewHolder;
    public int stopLossType = 0;
    public int trType = 0;

    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());

        viewHolder.tv_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLossType = 0;
                changeStopLoss(stopLossType);
            }
        });
        viewHolder.tv_now_price_stop_loss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLossType = 1;
                changeStopLoss(stopLossType);
            }
        });
        viewHolder.tv_limit_stop_loss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLossType = 2;
                changeStopLoss(stopLossType);
            }
        });
        viewHolder.tv_order_price_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.tv_order_price.setFocusable(true);
                viewHolder.tv_order_price.setFocusableInTouchMode(true);
                viewHolder.tv_order_price.requestFocus();
            }
        });
        viewHolder.tv_order_num_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.tv_order_num.setFocusable(true);
                viewHolder.tv_order_num.setFocusableInTouchMode(true);
                viewHolder.tv_order_num.requestFocus();
            }
        });
        viewHolder.tv_order_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    viewHolder.tv_order_num.setCursorVisible(false);
                    viewHolder.tv_order_num.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            viewHolder.tv_order_num.setCursorVisible(true);
                            viewHolder.lin_order_num.setBackground(
                                    CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
                            viewHolder.tv_order_num.setSelection(
                                    viewHolder.tv_order_num.getText().toString().length());
                        }
                    }, 50);
                } else {
                    viewHolder.lin_order_num.setBackground(
                            CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
                }
            }
        });
        viewHolder.tv_order_price.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    viewHolder.tv_order_price.setCursorVisible(false);
                    viewHolder.tv_order_price.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            viewHolder.tv_order_price.setCursorVisible(true);
                            viewHolder.lin_order_price.setBackground(
                                    CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
                            viewHolder.tv_order_price.setSelection(
                                    viewHolder.tv_order_price.getText().toString().length());
                        }
                    }, 50);
                } else {
                    viewHolder.lin_order_price.setBackground(
                            CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
                }
            }
        });
        viewHolder.tv_trigger_price.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    viewHolder.tv_trigger_price.setCursorVisible(false);
                    viewHolder.tv_trigger_price.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            viewHolder.tv_trigger_price.setCursorVisible(true);
                            viewHolder.lin_trigger_price.setBackground(
                                    CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
                            viewHolder.tv_trigger_price.setSelection(
                                    viewHolder.tv_trigger_price.getText().toString().length());
                        }
                    }, 50);
                } else {
                    viewHolder.lin_trigger_price.setBackground(
                            CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
                }
            }
        });
        viewHolder.tv_order_price.addTextChangedListener(priceTextWatcher);
        viewHolder.tv_trigger_price.addTextChangedListener(triggerTextWatcher);
        viewHolder.tv_order_num.addTextChangedListener(numTextWatcher);
    }

    TextWatcher triggerTextWatcher = new TextWatcher() {
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
                    viewHolder.tv_trigger_price.removeTextChangedListener(triggerTextWatcher);
                    String substring = s.toString().substring(0, s.toString().length() - 1);
                    viewHolder.tv_trigger_price.setText(substring);
                    viewHolder.tv_trigger_price.setSelection(substring.length());
                    viewHolder.tv_trigger_price.addTextChangedListener(triggerTextWatcher);
                }
            }
        }
    };
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
                    viewHolder.tv_order_price.removeTextChangedListener(priceTextWatcher);
                    String substring = s.toString().substring(0, s.toString().length() - 1);
                    viewHolder.tv_order_price.setText(substring);
                    viewHolder.tv_order_price.setSelection(substring.length());
                    viewHolder.tv_order_price.addTextChangedListener(priceTextWatcher);
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
                    viewHolder.tv_order_num.removeTextChangedListener(numTextWatcher);
                    String substring = s.toString().substring(0, s.toString().length() - 1);
                    viewHolder.tv_order_num.setText(substring);
                    viewHolder.tv_order_num.setSelection(substring.length());
                    viewHolder.tv_order_num.addTextChangedListener(numTextWatcher);
                }
            }
        }
    };

    public void changeTrType(int type, String text) {
        viewHolder.tv_opponent_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
        viewHolder.tv_buy_one_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
        viewHolder.tv_sell_one_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
        viewHolder.tv_opponent_price.setTextColor(CommonUtils.getColor(R.color.color_font4));
        viewHolder.tv_buy_one_price.setTextColor(CommonUtils.getColor(R.color.color_font4));
        viewHolder.tv_sell_one_price.setTextColor(CommonUtils.getColor(R.color.color_font4));
        if (type == 0) {
            trType = 0;
            viewHolder.tv_order_price.setText("");
            if (TextUtils.isEmpty(text)) {
                viewHolder.tv_order_price.setText(
                        !UiHeplUtils.isDouble(viewHolder.tv_now_price.getText().toString()) ||
                                ObjectUtils.equals("--",
                                        viewHolder.tv_now_price.getText().toString()) ?
                                "" : viewHolder.tv_now_price.getText().toString());
            } else {
                if (UiHeplUtils.isDouble(text)) {
                    viewHolder.tv_order_price.setText(text);
                }
            }
            viewHolder.lin_order_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_order_price.setEnabled(true);
        } else {
            if (trType == type) {
                trType = 0;
                viewHolder.lin_order_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
                viewHolder.tv_order_price.setEnabled(true);
                viewHolder.tv_order_price.setText("");
            } else {
                viewHolder.lin_order_price.setBackground(new RadiuBg(
                        CommonUtils.getColor(R.color.base_mask),
                        5, 5, 5, 5
                ));
                viewHolder.tv_order_price.setEnabled(false);
                viewHolder.tv_order_price.setText(text);
                trType = type;
                if (trType == 1) {
                    viewHolder.tv_opponent_price.setTextColor(CommonUtils.getColor(R.color.mark_color));
                    viewHolder.tv_opponent_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
                } else if (trType == 2) {
                    viewHolder.tv_buy_one_price.setTextColor(CommonUtils.getColor(R.color.mark_color));
                    viewHolder.tv_buy_one_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
                } else if (trType == 3) {
                    viewHolder.tv_sell_one_price.setTextColor(CommonUtils.getColor(R.color.mark_color));
                    viewHolder.tv_sell_one_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
                }
            }
        }
    }

    private void changeStopLoss(int type) {
        viewHolder.tv_limit.setBackground(new RadiuBg(
                CommonUtils.getColor(type == 0 ? R.color.mark_color : R.color.base_mask_more),
                5, 5, 5, 5
        ));
        viewHolder.tv_now_price_stop_loss.setBackground(new RadiuBg(
                CommonUtils.getColor(type == 1 ? R.color.mark_color : R.color.base_mask_more),
                5, 5, 5, 5
        ));
        viewHolder.tv_limit_stop_loss.setBackground(new RadiuBg(
                CommonUtils.getColor(type == 2 ? R.color.mark_color : R.color.base_mask_more),
                5, 5, 5, 5
        ));
        viewHolder.tv_limit.setTextColor(CommonUtils.getColor(type == 0 ? R.color.color_Primary : R.color.color_font3));
        viewHolder.tv_now_price_stop_loss.setTextColor(CommonUtils.getColor(type == 1 ? R.color.color_Primary : R.color.color_font3));
        viewHolder.tv_limit_stop_loss.setTextColor(CommonUtils.getColor(type == 2 ? R.color.color_Primary : R.color.color_font3));
        if (type == 0) {
            viewHolder.lin_order_price.setVisibility(View.VISIBLE);
            viewHolder.lin_trigger_price.setVisibility(View.GONE);
            viewHolder.lin_order_select_price.setVisibility(View.VISIBLE);
        } else if (type == 1) {
            viewHolder.lin_order_price.setVisibility(View.GONE);
            viewHolder.lin_trigger_price.setVisibility(View.VISIBLE);
            viewHolder.lin_order_select_price.setVisibility(View.GONE);
            changeTrType(0, "");
        } else if (type == 2) {
            viewHolder.lin_order_price.setVisibility(View.VISIBLE);
            viewHolder.lin_trigger_price.setVisibility(View.VISIBLE);
            viewHolder.lin_order_select_price.setVisibility(View.GONE);
            changeTrType(0, "");
        }
        ((LinearLayout.LayoutParams) viewHolder.lin_order_num.getLayoutParams())
                .topMargin = (int) CommonUtils.getDimensionPixelSize(type != 0 ? R.dimen.trans_20px : R.dimen.trans_0px);
        viewHolder.lin_order_num.requestLayout();

    }

    public void changOpenOrClose(boolean isOpen) {
        if (isOpen) {
            viewHolder.tv_open.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
            viewHolder.tv_open.setTextColor(CommonUtils.getColor(R.color.mark_color));
            viewHolder.tv_close.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_close.setTextColor(CommonUtils.getColor(R.color.color_font4));

            viewHolder.tv_buy_type.setText("可开多");
            viewHolder.tv_sell_type.setText("可开空");

            viewHolder.tv_buy_left.setText("可用");
            viewHolder.tv_sell_left.setText("可用");

            viewHolder.lin_buy_left.setVisibility(View.VISIBLE);
            viewHolder.lin_sell_left.setVisibility(View.VISIBLE);

            viewHolder.tv_buy.setText(
                    "买入开多(看涨)"
            );
            viewHolder.tv_sell.setText(
                    "卖出开空(看跌)"
            );

            viewHolder.lin_stop_loss.setVisibility(View.GONE);
            viewHolder.tv_stop_loss_toast.setVisibility(View.GONE);
            changeStopLoss(0);
        } else {
            viewHolder.tv_close.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
            viewHolder.tv_close.setTextColor(CommonUtils.getColor(R.color.mark_color));
            viewHolder.tv_open.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_open.setTextColor(CommonUtils.getColor(R.color.color_font4));

            viewHolder.tv_sell_type.setText("可平多");
            viewHolder.tv_buy_type.setText("可平空");
            viewHolder.tv_buy_type.setText("可平空");

            viewHolder.tv_buy_left.setText("冻结");
            viewHolder.tv_sell_left.setText("冻结");

            viewHolder.lin_buy_left.setVisibility(View.GONE);
            viewHolder.lin_sell_left.setVisibility(View.GONE);

            if (!viewHolder.tv_order_price.isFocused() &&
                    !viewHolder.tv_order_num.isFocused()) {
                viewHolder.tv_order_price.requestFocus();
            }

            viewHolder.tv_buy.setText(
                    "买入平空"
            );
            viewHolder.tv_sell.setText(
                    "卖出平多"
            );
            viewHolder.lin_stop_loss.setVisibility(View.VISIBLE);
            viewHolder.tv_stop_loss_toast.setVisibility(View.VISIBLE);
            changeStopLoss(stopLossType);
        }

        if (transactionBean != null && tradeDetailBean != null) {
            if (isOpen) {
                viewHolder.tv_buy_type_num.setText(transactionBean.getAvailableOpenMore() + tradeDetailBean.getAmountUnit());
                viewHolder.tv_buy_available.setText(transactionBean.getUsableOpenMore() + tradeDetailBean.getMarginUnit());

                viewHolder.tv_sell_type_num.setText(transactionBean.getAvailableOpenSpace() + tradeDetailBean.getAmountUnit());
                viewHolder.tv_sell_available.setText(transactionBean.getUsableOpenSpace() + tradeDetailBean.getMarginUnit());
            } else {
                viewHolder.tv_buy_type_num.setText(transactionBean.getAvailableflatSpace().replace("-", "") + tradeDetailBean.getAmountUnit());
                viewHolder.tv_sell_type_num.setText(transactionBean.getAvailableflatMore().replace("-", "") + tradeDetailBean.getAmountUnit());
            }
        }

    }


    public String TAG = getClass().getSimpleName();
    public TradeDetailBean tradeDetailBean;
    boolean isVisibility = false;
    public TransactionBean transactionBean = new TransactionBean();

    public void setVisibility(boolean visibility) {
        isVisibility = visibility;
    }

    public void setTradeDetailBean(TradeDetailBean tradeDetailBean) {
        this.tradeDetailBean = tradeDetailBean;

        CacheUtils.getInstance().put(CacheName.TradeIdBMCache, tradeDetailBean.getId());
        CacheUtils.getInstance().put(CacheName.TradeCoinBMCache, tradeDetailBean.getCurrency());

        viewHolder.tv_order_price_unit.setText(tradeDetailBean.getPriceUnit());
        viewHolder.tv_trigger_price_unit.setText(tradeDetailBean.getPriceUnit());
        viewHolder.tv_show_price_type.setText("价格(" + tradeDetailBean.getPriceUnit() + ")");
        viewHolder.tv_order_num_unit.setText(tradeDetailBean.getAmountUnit());
        viewHolder.tv_coin_num.setText("数量(" + tradeDetailBean.getAmountUnit() + ")");
        viewHolder.tv_order_type.setText(tradeDetailBean.getCurrencyPairName());


        viewHolder.tv_fund_rate.setText(tradeDetailBean.getRate() + "%");
        ((View) viewHolder.tv_fund_rate.getParent()).setVisibility(
                TextUtils.isEmpty(tradeDetailBean.getRate()) ? View.GONE : View.VISIBLE
        );
        ((View) viewHolder.tv_latest_index.getParent()).setVisibility(View.GONE);

        //设置深度 价格 数量 小数点长度
        initDepth(new ArrayList<DepthBean>(), new ArrayList<DepthBean>());
        asksAdapter.setPriceSize(UiHeplUtils.numPointAfterLength(tradeDetailBean.getMinPriceUnit()));
        bidsAdapter.setPriceSize(UiHeplUtils.numPointAfterLength(tradeDetailBean.getMinPriceUnit()));

        asksAdapter.setNumSize(UiHeplUtils.numPointAfterLength(tradeDetailBean.getMinAmountUnit()));
        bidsAdapter.setNumSize(UiHeplUtils.numPointAfterLength(tradeDetailBean.getMinAmountUnit()));

        //清除可用可开 指数 价格 深度 订单，重新获取
        viewHolder.tv_buy_type_num.setText("--");
        viewHolder.tv_buy_available.setText("--");
        viewHolder.tv_sell_available.setText("--");
        viewHolder.tv_sell_type_num.setText("--");
        viewHolder.tv_latest_index.setText("--");
        viewHolder.tv_now_price.setText("--");
        viewHolder.tv_now_price.setTextSize(TypedValue.COMPLEX_UNIT_PX, CommonUtils.getDimensionPixelSize(R.dimen.text_trans_36px));

        transactionBean.setAvailableflatMore("");
        transactionBean.setAvailableflatSpace("");
        transactionBean.setAvailableOpenMore("");
        transactionBean.setAvailableOpenSpace("");
        transactionBean.setUsableOpenMore("");
        transactionBean.setUsableOpenSpace("");

        if (tradeDetailBean.getKline() == 1) {
            viewHolder.lin_to_kline.setEnabled(true);
            viewHolder.iv_to_kline.setVisibility(View.VISIBLE);
        } else {
            viewHolder.lin_to_kline.setEnabled(false);
            viewHolder.iv_to_kline.setVisibility(View.GONE);
        }

    }

    public WsTrBMOrdersAdapter wsTrBMOrdersAdapter;

    //订阅深度和指数
    public void sendWs(boolean isSubscribe) {
        if (!isVisibility) {
            return;
        }
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
        oldPrice = "";
        HandlerHelper.getinstance().initHander(TAG,
                viewHolder.rv_buy,
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
                                    ((View) viewHolder.tv_latest_index.getParent()).setVisibility(View.VISIBLE);
                                    viewHolder.tv_latest_index.setText(
                                            BigUIUtil.getinstance().getSymbol(tradeDetailBean.getPriceUnit()) +
                                                    BigUIUtil.getinstance().bigPrice(((WebSocketDepthBean) val).getLast())
                                    );
                                }
                            } else if (val instanceof WsOrderBean && wsTrBMOrdersAdapter != null) {
                                wsPushOrder((WsOrderBean) val);
                            }
                        }
                    }
                });
        HandlerHelper.getinstance().setDelayMillis(400);
    }

    private void wsPushOrder(WsOrderBean val) {
        if (ObjectUtils.equals("1", ((WsOrderBean) val).getState())) {
            wsTrBMOrdersAdapter.removeOrderDisposable((WsOrderBean) val);
        }
        wsTrBMOrdersAdapter.setWsData((WsOrderBean) val);
    }


    public String oldPrice = "";

    private void showNowPrice(String price) {
        viewHolder.tv_now_price.setTextColor(CommonUtils.getColor(UserSet.getinstance().getRiseColor()));
        if (!TextUtils.isEmpty(oldPrice)) {
            if (new BigDecimal(price).doubleValue() < new BigDecimal(oldPrice).doubleValue()) {
                viewHolder.tv_now_price.setTextColor(CommonUtils.getColor(UserSet.getinstance().getDropColor()));
            }
        }
        oldPrice = price;
        viewHolder.tv_now_price.setText(
                BigUIUtil.getinstance().getSymbol(tradeDetailBean.getPriceUnit()) +
                        BigUIUtil.getinstance().bigPrice(price));
    }

    public void initWs() {
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
    }

    public int depthSize = 6;

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

    public DepthAdapter asksAdapter;
    public DepthAdapter bidsAdapter;

    //显示深度
    public void initDepth(List<DepthBean> asks, List<DepthBean> bids) {
        if (asksAdapter == null) {
            asksAdapter = new DepthAdapter(getActivity(), asks, false);
            asksAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    changeTrType(0, asksAdapter.getDatas().get(position).getPrice());
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
            viewHolder.rv_buy.setLayoutManager(new LinearLayoutManager(getActivity()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            viewHolder.rv_buy.setAdapter(asksAdapter);
        } else {
            asksAdapter.setData(asks);
        }
        if (bidsAdapter == null) {
            bidsAdapter = new DepthAdapter(getActivity(), bids, true);
            bidsAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    changeTrType(0, bidsAdapter.getDatas().get(position).getPrice());
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
            viewHolder.rv_sell.setLayoutManager(new LinearLayoutManager(getActivity()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            viewHolder.rv_sell.setAdapter(bidsAdapter);
        } else {
            bidsAdapter.setData(bids);
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bmtr;
    }


    public static class ViewHolder {
        public View rootView;
        public IconFontTextview tv_order_type;
        public LinearLayout lin_order_type;
        public IconFontTextview tv_lever;
        public LinearLayout lin_lever;
        public SingleLineZoomTextView tv_now_price;
        public ImageView iv_to_kline;
        public LinearLayout lin_to_kline;
        public TextView tv_open;
        public TextView tv_close;
        public TextView tv_limit;
        public TextView tv_now_price_stop_loss;
        public TextView tv_limit_stop_loss;
        public LinearLayout lin_stop_loss;
        public EditText tv_trigger_price;
        public TextView tv_trigger_price_unit;
        public LinearLayout lin_trigger_price;
        public EditText tv_order_price;
        public TextView tv_order_price_unit;
        public LinearLayout lin_order_price;
        public TextView tv_opponent_price;
        public FrameLayout fl_opponent_price;
        public TextView tv_buy_one_price;
        public FrameLayout fl_buy_one_price;
        public TextView tv_sell_one_price;
        public FrameLayout fl_sell_one_price;
        public LinearLayout lin_order_select_price;
        public EditText tv_order_num;
        public TextView tv_order_num_unit;
        public LinearLayout lin_order_num;
        public TextView tv_stop_loss_toast;
        public RoundButton tv_buy;
        public LineTextView tv_buy_left;
        public TextView tv_buy_available;
        public LinearLayout lin_buy_left;
        public LineTextView tv_buy_type;
        public TextView tv_buy_type_num;
        public RoundButton tv_sell;
        public LineTextView tv_sell_left;
        public TextView tv_sell_available;
        public LinearLayout lin_sell_left;
        public LineTextView tv_sell_type;
        public TextView tv_sell_type_num;
        public TextView tv_show_price_type;
        public TextView tv_coin_num;
        public RecyclerView rv_buy;
        public RecyclerView rv_sell;
        public SingleLineZoomTextView tv_latest_index;
        public SingleLineZoomTextView tv_fund_rate;
        public RecyclerView recycler_view_bc;
        public RecyclerView recycler_view;
        public LinearLayout lin_no_order;
        public SwipeRefreshLayout swipeRefreshLayout;
        public FrameLayout fl_left;
        public DrawerLayout main_drawer_layout;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tv_order_type = (IconFontTextview) rootView.findViewById(R.id.tv_order_type);
            this.lin_order_type = (LinearLayout) rootView.findViewById(R.id.lin_order_type);
            this.tv_lever = (IconFontTextview) rootView.findViewById(R.id.tv_lever);
            this.lin_lever = (LinearLayout) rootView.findViewById(R.id.lin_lever);
            this.tv_now_price = (SingleLineZoomTextView) rootView.findViewById(R.id.tv_now_price);
            this.iv_to_kline = (ImageView) rootView.findViewById(R.id.iv_to_kline);
            this.lin_to_kline = (LinearLayout) rootView.findViewById(R.id.lin_to_kline);
            this.tv_open = (TextView) rootView.findViewById(R.id.tv_open);
            this.tv_close = (TextView) rootView.findViewById(R.id.tv_close);
            this.tv_limit = (TextView) rootView.findViewById(R.id.tv_limit);
            this.tv_now_price_stop_loss = (TextView) rootView.findViewById(R.id.tv_now_price_stop_loss);
            this.tv_limit_stop_loss = (TextView) rootView.findViewById(R.id.tv_limit_stop_loss);
            this.lin_stop_loss = (LinearLayout) rootView.findViewById(R.id.lin_stop_loss);
            this.tv_trigger_price = (EditText) rootView.findViewById(R.id.tv_trigger_price);
            this.tv_trigger_price_unit = (TextView) rootView.findViewById(R.id.tv_trigger_price_unit);
            this.lin_trigger_price = (LinearLayout) rootView.findViewById(R.id.lin_trigger_price);
            this.tv_order_price = (EditText) rootView.findViewById(R.id.tv_order_price);
            this.tv_order_price_unit = (TextView) rootView.findViewById(R.id.tv_order_price_unit);
            this.lin_order_price = (LinearLayout) rootView.findViewById(R.id.lin_order_price);
            this.tv_opponent_price = (TextView) rootView.findViewById(R.id.tv_opponent_price);
            this.fl_opponent_price = (FrameLayout) rootView.findViewById(R.id.fl_opponent_price);
            this.tv_buy_one_price = (TextView) rootView.findViewById(R.id.tv_buy_one_price);
            this.fl_buy_one_price = (FrameLayout) rootView.findViewById(R.id.fl_buy_one_price);
            this.tv_sell_one_price = (TextView) rootView.findViewById(R.id.tv_sell_one_price);
            this.fl_sell_one_price = (FrameLayout) rootView.findViewById(R.id.fl_sell_one_price);
            this.lin_order_select_price = (LinearLayout) rootView.findViewById(R.id.lin_order_select_price);
            this.tv_order_num = (EditText) rootView.findViewById(R.id.tv_order_num);
            this.tv_order_num_unit = (TextView) rootView.findViewById(R.id.tv_order_num_unit);
            this.lin_order_num = (LinearLayout) rootView.findViewById(R.id.lin_order_num);
            this.tv_stop_loss_toast = (TextView) rootView.findViewById(R.id.tv_stop_loss_toast);
            this.tv_buy = (RoundButton) rootView.findViewById(R.id.tv_buy);
            this.tv_buy_left = (LineTextView) rootView.findViewById(R.id.tv_buy_left);
            this.tv_buy_available = (TextView) rootView.findViewById(R.id.tv_buy_available);
            this.lin_buy_left = (LinearLayout) rootView.findViewById(R.id.lin_buy_left);
            this.tv_buy_type = (LineTextView) rootView.findViewById(R.id.tv_buy_type);
            this.tv_buy_type_num = (TextView) rootView.findViewById(R.id.tv_buy_type_num);
            this.tv_sell = (RoundButton) rootView.findViewById(R.id.tv_sell);
            this.tv_sell_left = (LineTextView) rootView.findViewById(R.id.tv_sell_left);
            this.tv_sell_available = (TextView) rootView.findViewById(R.id.tv_sell_available);
            this.lin_sell_left = (LinearLayout) rootView.findViewById(R.id.lin_sell_left);
            this.tv_sell_type = (LineTextView) rootView.findViewById(R.id.tv_sell_type);
            this.tv_sell_type_num = (TextView) rootView.findViewById(R.id.tv_sell_type_num);
            this.tv_show_price_type = (TextView) rootView.findViewById(R.id.tv_show_price_type);
            this.tv_coin_num = (TextView) rootView.findViewById(R.id.tv_coin_num);
            this.rv_buy = (RecyclerView) rootView.findViewById(R.id.rv_buy);
            this.rv_sell = (RecyclerView) rootView.findViewById(R.id.rv_sell);
            this.tv_latest_index = (SingleLineZoomTextView) rootView.findViewById(R.id.tv_latest_index);
            this.tv_fund_rate = (SingleLineZoomTextView) rootView.findViewById(R.id.tv_fund_rate);
            this.recycler_view_bc = (RecyclerView) rootView.findViewById(R.id.recycler_view_bc);
            this.recycler_view = (RecyclerView) rootView.findViewById(R.id.recycler_view);
            this.lin_no_order = (LinearLayout) rootView.findViewById(R.id.lin_no_order);
            this.swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
            this.fl_left = (FrameLayout) rootView.findViewById(R.id.fl_left);
            this.main_drawer_layout = (DrawerLayout) rootView.findViewById(R.id.main_drawer_layout);
        }

    }
}