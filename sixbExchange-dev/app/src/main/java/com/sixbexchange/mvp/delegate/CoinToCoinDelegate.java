package com.sixbexchange.mvp.delegate;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.fivefivelike.mybaselibrary.http.HandlerHelper;
import com.fivefivelike.mybaselibrary.http.WebSocketRequest;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.ListUtils;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.fivefivelike.mybaselibrary.view.FontTextview;
import com.fivefivelike.mybaselibrary.view.IconFontTextview;
import com.fivefivelike.mybaselibrary.view.LineTextView;
import com.fivefivelike.mybaselibrary.view.RoundButton;
import com.fivefivelike.mybaselibrary.view.SingleLineZoomTextView;
import com.fivefivelike.mybaselibrary.view.SwipeRefreshLayout;
import com.sixbexchange.R;
import com.sixbexchange.adapter.DepthAdapter;
import com.sixbexchange.entity.bean.CoinToCoinBean;
import com.sixbexchange.entity.bean.CoinToCoinDetailBean;
import com.sixbexchange.entity.bean.DepthBean;
import com.sixbexchange.entity.bean.WebSocketDepthBean;
import com.sixbexchange.utils.BigUIUtil;
import com.sixbexchange.utils.UserSet;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoinToCoinDelegate extends BaseDelegate {
    public ViewHolder viewHolder;
    String oldPrice = "";
    String TAG = getClass().getSimpleName();
    boolean isVisibility = false;
    public List<CoinToCoinBean> mCoinToCoinBeanList;
    private String webso;
    private String baseCurrency="";
    private String quoteCurrency="";

    public void setVisibility(boolean visibility) {
        isVisibility = visibility;
    }

    public void setCoinDetailBean(List<CoinToCoinBean> mCoinToCoinBeanList, CoinToCoinDetailBean coinToCoinDetailBean, int leftPos, int rightPos) {
        this.mCoinToCoinBeanList = mCoinToCoinBeanList;

        viewHolder.tv_show_price_type.setText("价格(" + "USD" + ")");
        viewHolder.tv_coin_num.setText("数量(" + "张" + ")");
        baseCurrency = mCoinToCoinBeanList.get(leftPos).getData().get(rightPos).getBaseCurrency();
        quoteCurrency = mCoinToCoinBeanList.get
                (leftPos).getData().get(rightPos).getQuoteCurrency();
        String pricePrecision = mCoinToCoinBeanList.get(leftPos).getData().get(rightPos).getPricePrecision();
        String amountPrecision = mCoinToCoinBeanList.get(leftPos).getData().get(rightPos).getAmountPrecision();
        webso = "huobip" + baseCurrency + "." + quoteCurrency;
        viewHolder.tv_order_type.setText(baseCurrency.toUpperCase() + "/" + quoteCurrency.toUpperCase());

        initDepth(new ArrayList<DepthBean>(), new ArrayList<DepthBean>());
        asksAdapter.setPriceSize(UiHeplUtils.numPointAfterLength(pricePrecision));
        bidsAdapter.setPriceSize(UiHeplUtils.numPointAfterLength(pricePrecision));

        asksAdapter.setNumSize(UiHeplUtils.numPointAfterLength(amountPrecision));
        bidsAdapter.setNumSize(UiHeplUtils.numPointAfterLength(amountPrecision));

        if (coinToCoinDetailBean!=null){
            String avibuy="0";
            String aviuse="0";
            if (coinToCoinDetailBean.getBCH().equals(baseCurrency.toUpperCase())) {
                avibuy = coinToCoinDetailBean.getBCH().getAvi();
            } else if (coinToCoinDetailBean.getBCT().equals(baseCurrency.toUpperCase())) {
                avibuy = coinToCoinDetailBean.getBCT().getAvi();
            } else if (coinToCoinDetailBean.getBSV().equals(baseCurrency.toUpperCase())) {
                avibuy = coinToCoinDetailBean.getBSV().getAvi();
            } else if (coinToCoinDetailBean.getBTC().equals(baseCurrency.toUpperCase())) {
                avibuy = coinToCoinDetailBean.getBTC().getAvi();
            } else if (coinToCoinDetailBean.getEOS().equals(baseCurrency.toUpperCase())) {
                avibuy = coinToCoinDetailBean.getEOS().getAvi();
            } else if (coinToCoinDetailBean.getETH().equals(baseCurrency.toUpperCase())) {
                avibuy = coinToCoinDetailBean.getETH().getAvi();
            } else if (coinToCoinDetailBean.getLTC().equals(baseCurrency.toUpperCase())) {
                avibuy = coinToCoinDetailBean.getLTC().getAvi();
            } else {
                avibuy = coinToCoinDetailBean.getXRP().getAvi();
            }
            if (coinToCoinDetailBean.getBCH().equals(quoteCurrency.toUpperCase())) {
                aviuse = coinToCoinDetailBean.getBCH().getAvi();
            } else if (coinToCoinDetailBean.getBCT().equals(quoteCurrency.toUpperCase())) {
                aviuse = coinToCoinDetailBean.getBCT().getAvi();
            } else if (coinToCoinDetailBean.getBSV().equals(quoteCurrency.toUpperCase())) {
                aviuse = coinToCoinDetailBean.getBSV().getAvi();
            } else if (coinToCoinDetailBean.getBTC().equals(quoteCurrency.toUpperCase())) {
                aviuse = coinToCoinDetailBean.getBTC().getAvi();
            } else if (coinToCoinDetailBean.getEOS().equals(quoteCurrency.toUpperCase())) {
                aviuse = coinToCoinDetailBean.getEOS().getAvi();
            } else if (coinToCoinDetailBean.getETH().equals(quoteCurrency.toUpperCase())) {
                aviuse = coinToCoinDetailBean.getETH().getAvi();
            } else if (coinToCoinDetailBean.getLTC().equals(quoteCurrency.toUpperCase())) {
                aviuse = coinToCoinDetailBean.getLTC().getAvi();
            } else {
                aviuse = coinToCoinDetailBean.getXRP().getAvi();
            }
            BigDecimal bg1 = new BigDecimal(Double.parseDouble(avibuy));
            double f1 = bg1.setScale(Integer.parseInt(amountPrecision), BigDecimal.ROUND_HALF_UP).doubleValue();
            BigDecimal bg2 = new BigDecimal(Double.parseDouble(aviuse));
            double f2= bg2.setScale(Integer.parseInt(pricePrecision), BigDecimal.ROUND_HALF_UP).doubleValue();
            viewHolder.tv_transaction.setText(f1+baseCurrency);
            viewHolder.tv_use.setText(f2+quoteCurrency);
            viewHolder.tv_valuation.setText("估值 --");
            viewHolder.tv_latest_index.setText("--");
            viewHolder.tv_now_price.setText("--");
            viewHolder.tv_now_price.setTextSize(TypedValue.COMPLEX_UNIT_PX, CommonUtils.getDimensionPixelSize(R.dimen.text_trans_36px));
        }
        saleModeChange(tp);
        viewHolder.lin_to_kline.setEnabled(true);
        viewHolder.iv_to_kline.setVisibility(View.VISIBLE);
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

        }
    };

    private void showNowPrice(String price) {
        viewHolder.tv_now_price.setTextColor(CommonUtils.getColor(UserSet.getinstance().getRiseColor()));
        if (!TextUtils.isEmpty(oldPrice)) {
            if (new BigDecimal(price).doubleValue() < new BigDecimal(oldPrice).doubleValue()) {
                viewHolder.tv_now_price.setTextColor(CommonUtils.getColor(UserSet.getinstance().getDropColor()));
            }
        }
        oldPrice = price;
        viewHolder.tv_now_price.setText(BigUIUtil.getinstance().bigPrice(price));
    }

    private void depthPrice(String price) {
        viewHolder.et_num.setText(BigUIUtil.getinstance().bigPrice(price));
    }


    public void sendWs(boolean isSubscribe) {
        if (!isVisibility) {
            return;
        }
        if (WebSocketRequest.getInstance().getCallBack(TAG) != null&&webso!=null) {
            Map<String, String> map = new HashMap<>();
            map.put("uri", (isSubscribe ? "subscribe" : "unsubscribe") + "-single-tick-verbose");
            map.put("contract", webso);
            WebSocketRequest.getInstance().sendData(GsonUtil.getInstance().toJson(map));
        }
        HandlerHelper.getinstance().initHander(TAG,
                viewHolder.rv_buy,
                new HandlerHelper.OnUpdataLinsener() {
                    @Override
                    public void onUpdataLinsener(Object val) {
                        if (isVisibility) {
                            if (val instanceof WebSocketDepthBean && webso != null) {
                                if (ObjectUtils.equals(webso,((WebSocketDepthBean) val).getContract())) {
                                    showNowPrice(((WebSocketDepthBean) val).getLast());
                                    changeDepth((WebSocketDepthBean) val);
                                }
                            }
                        }
                    }
                });
        HandlerHelper.getinstance().setDelayMillis(400);
    }

    public void initWs() {
        saleModeChange(tp);
        WebSocketRequest.getInstance().addCallBack(TAG, new WebSocketRequest.WebSocketCallBack() {
            @Override
            public void onDataSuccess(String name, String data, String info, int status) {
                if (ObjectUtils.equals(TAG, name) &&!WebSocketRequest.getInstance().isSend() && isVisibility) {
                    WebSocketDepthBean webSocketDepthBean =GsonUtil.getInstance().toObj(data, "data", WebSocketDepthBean.class);
                    if (webSocketDepthBean != null &&
                            ObjectUtils.equals(webso,webSocketDepthBean.getContract()) &&
                            !ListUtils.isEmpty(webSocketDepthBean.getAsks()) &&
                            !ListUtils.isEmpty(webSocketDepthBean.getBids()) &&
                            !TextUtils.isEmpty(webSocketDepthBean.getLast())) {
                        HandlerHelper.getinstance().put(webso,webSocketDepthBean);
                    }
                }
            }

            @Override
            public void onDataError(String name, String data, String info, int status) {
                Log.i(TAG,"error==>"+name);
            }
        });
    }

    public int depthSize = 6;

    private void changeDepth(WebSocketDepthBean val) {
        if (ObjectUtils.equals(webso, ((WebSocketDepthBean) val).getContract())
                && val.getBids().size() >= depthSize &&
                val.getAsks().size() >= depthSize) {
            List<DepthBean> depthBeans = ((WebSocketDepthBean) val)
                    .getAsks().subList(0, depthSize);
            Collections.reverse(depthBeans);
            initDepth(depthBeans,((WebSocketDepthBean) val).getBids().subList(0, depthSize));
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
                    depthPrice(asksAdapter.getDatas().get(position).getPrice());
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
                    depthPrice(bidsAdapter.getDatas().get(position).getPrice());
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

    public void saleModeChange(int type) {
        if (type == 0) {
            viewHolder.tv_buy.setBackground(CommonUtils.getDrawable(R.color.decreasing_color));
            viewHolder.tv_sell.setBackground(CommonUtils.getDrawable(R.color.base_mask));
            viewHolder.tv_buy.setTextColor(CommonUtils.getColor(R.color.white));
            viewHolder.tv_sell.setTextColor(CommonUtils.getColor(R.color.increasing_color));
            viewHolder.tv_commit.setBackground(CommonUtils.getDrawable(R.color.decreasing_color));
            viewHolder.tv_commit.setText("买入"+baseCurrency.toUpperCase());
        } else {
            viewHolder.tv_buy.setBackground(CommonUtils.getDrawable(R.color.base_mask));
            viewHolder.tv_sell.setBackground(CommonUtils.getDrawable(R.color.increasing_color));
            viewHolder.tv_buy.setTextColor(CommonUtils.getColor(R.color.decreasing_color));
            viewHolder.tv_sell.setTextColor(CommonUtils.getColor(R.color.white));
            viewHolder.tv_commit.setBackground(CommonUtils.getDrawable(R.color.increasing_color));
            viewHolder.tv_commit.setText("卖出"+quoteCurrency.toUpperCase());
        }
    }

    private int tp=0;
    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
        //买入卖出
        viewHolder.tv_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tp=0;
                saleModeChange(tp);
            }
        });
        viewHolder.tv_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tp=1;
                saleModeChange(tp);
            }
        });

        viewHolder.et_num.addTextChangedListener(priceTextWatcher);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.layout_coin_to_coin;
    }


    public static class ViewHolder {
        public SwipeRefreshLayout swipeRefreshLayout;
        public View rootView;
        public IconFontTextview tv_order_type;
        public LinearLayout lin_order_type;
        public SingleLineZoomTextView tv_now_price;
        public ImageView iv_to_kline;
        public LinearLayout lin_to_kline;
        public TextView tv_buy;
        public TextView tv_sell;
        public FontTextview tv_price;
        public EditText et_num;
        public TextView tv_valuation;
        public RoundButton tv_commit;
        public TextView tv_use;
        public LinearLayout lin_use;
        public LineTextView tv_transaction_title;
        public TextView tv_transaction;
        public LinearLayout lin_transaction;
        public TextView tv_show_price_type;
        public TextView tv_coin_num;
        public RecyclerView rv_buy;
        public RecyclerView rv_sell;
        public SingleLineZoomTextView tv_latest_index;
        public SingleLineZoomTextView tv_fund_rate;
        public RecyclerView recycler_view;
        public LinearLayout lin_no_order;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tv_order_type = (IconFontTextview) rootView.findViewById(R.id.tv_order_type);
            this.lin_order_type = (LinearLayout) rootView.findViewById(R.id.lin_order_type);
            this.tv_now_price = (SingleLineZoomTextView) rootView.findViewById(R.id.tv_now_price);
            this.iv_to_kline = (ImageView) rootView.findViewById(R.id.iv_to_kline);
            this.lin_to_kline = (LinearLayout) rootView.findViewById(R.id.lin_to_kline);
            this.tv_buy = (TextView) rootView.findViewById(R.id.tv_buy);
            this.tv_sell = (TextView) rootView.findViewById(R.id.tv_sell);
            this.tv_price = (FontTextview) rootView.findViewById(R.id.tv_price);
            this.et_num = (EditText) rootView.findViewById(R.id.et_num);
            this.tv_valuation = (TextView) rootView.findViewById(R.id.tv_valuation);
            this.tv_commit = (RoundButton) rootView.findViewById(R.id.tv_commit);
            this.tv_use = (TextView) rootView.findViewById(R.id.tv_use);
            this.lin_use = (LinearLayout) rootView.findViewById(R.id.lin_use);
            this.tv_transaction_title = (LineTextView) rootView.findViewById(R.id.tv_transaction_title);
            this.tv_transaction = (TextView) rootView.findViewById(R.id.tv_transaction);
            this.lin_transaction = (LinearLayout) rootView.findViewById(R.id.lin_transaction);
            this.tv_show_price_type = (TextView) rootView.findViewById(R.id.tv_show_price_type);
            this.tv_coin_num = (TextView) rootView.findViewById(R.id.tv_coin_num);
            this.rv_buy = (RecyclerView) rootView.findViewById(R.id.rv_buy);
            this.rv_sell = (RecyclerView) rootView.findViewById(R.id.rv_sell);
            this.tv_latest_index = (SingleLineZoomTextView) rootView.findViewById(R.id.tv_latest_index);
            this.tv_fund_rate = (SingleLineZoomTextView) rootView.findViewById(R.id.tv_fund_rate);
            this.recycler_view = (RecyclerView) rootView.findViewById(R.id.recycler_view);
            this.lin_no_order = (LinearLayout) rootView.findViewById(R.id.lin_no_order);
            this.swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        }
    }
}