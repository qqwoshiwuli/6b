package com.sixbexchange.mvp.delegate;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.util.CacheUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.circledialog.res.drawable.RadiuBg;
import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.fivefivelike.mybaselibrary.entity.ResultDialogEntity;
import com.fivefivelike.mybaselibrary.http.HandlerHelper;
import com.fivefivelike.mybaselibrary.http.WebSocket3Request;
import com.fivefivelike.mybaselibrary.http.WebSocketRequest;
import com.fivefivelike.mybaselibrary.http.WebSocketRequest;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.ListUtils;
import com.fivefivelike.mybaselibrary.utils.StringUtil;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.view.IconFontTextview;
import com.fivefivelike.mybaselibrary.view.LineTextView;
import com.fivefivelike.mybaselibrary.view.MyListView;
import com.fivefivelike.mybaselibrary.view.RoundButton;
import com.fivefivelike.mybaselibrary.view.SingleLineZoomTextView;
import com.fivefivelike.mybaselibrary.view.SwipeRefreshLayout;
import com.fivefivelike.mybaselibrary.view.dialog.ResultDialog;
import com.google.gson.Gson;
import com.sixbexchange.R;
import com.sixbexchange.adapter.DepthAdapter;
import com.sixbexchange.adapter.TrOkexOrdersAdapter;
import com.sixbexchange.adapter.WsTrBMOrdersAdapter;
import com.sixbexchange.base.CacheName;
import com.sixbexchange.entity.bean.CoinToCoinDetailBean;
import com.sixbexchange.entity.bean.DepthBean;
import com.sixbexchange.entity.bean.OrderBean;
import com.sixbexchange.entity.bean.TradeDetailBean;
import com.sixbexchange.entity.bean.WebSocketDepthBean;
import com.sixbexchange.entity.bean.WsOrderBean;
import com.sixbexchange.entity.bean.selectExchhuobiBeans;
import com.sixbexchange.utils.BigUIUtil;
import com.sixbexchange.utils.UserSet;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OneTokenDelegate extends BaseDelegate {
    public ViewHolder viewHolder;
    String oldPrice = "0";
    String TAG = getClass().getSimpleName();
    boolean isVisibility = false;


    public void setVisibility(boolean visibility) {
        isVisibility = visibility;
    }

    ResultDialogEntity resultDialogEntity;

    public void showTriggerDialog(String data) {
        if (ObjectUtils.equals("1", GsonUtil.getInstance().getValue(data, "need"))) {
            if (resultDialogEntity == null) {
                resultDialogEntity = new ResultDialogEntity();
                resultDialogEntity.setType("1");
                resultDialogEntity.setConfirmBtn("知道了");
            }
            resultDialogEntity.setTitle(GsonUtil.getInstance().getValue(data, "remark"));
            resultDialogEntity.setConfirmColor(CommonUtils.getStringColor(R.color.mark_color));
            ResultDialog.getInstence()
                    .ShowResultDialog(this.getActivity(), GsonUtil.getInstance().toJson(resultDialogEntity), new DefaultClickLinsener() {
                        @Override
                        public void onClick(View view, int position, Object item) {

                        }
                    });
        }
    }

    Double aDoubleprice=0.0;
    private void showNowPrice(String price) {
        aDoubleprice=Double.parseDouble(price);
        showCoin(coinToCoinDetailBean);
        viewHolder.tv_now_price.setTextColor(CommonUtils.getColor(UserSet.getinstance().getRiseColor()));
        if (!TextUtils.isEmpty(oldPrice)) {
            if (new BigDecimal(price).doubleValue() < new BigDecimal(oldPrice).doubleValue()) {
                viewHolder.tv_now_price.setTextColor(CommonUtils.getColor(UserSet.getinstance().getDropColor()));
            }
        }
        oldPrice = price;
        viewHolder.tv_now_price.setText(
                        BigUIUtil.getinstance().bigPrice(price));
    }


    selectExchhuobiBeans.HuobiDataBean huobiDataBean=null;

    public void setTradeDetailBean(selectExchhuobiBeans.HuobiDataBean selectExchCoinBeans)
    {
        this.huobiDataBean=selectExchCoinBeans;
        initWs(0);
        viewHolder.tv_show_price_type.setText("价格("+huobiDataBean.getQuoteCurrency()+")");
        viewHolder.tv_coin_num.setText("数量("+huobiDataBean.getBaseCurrency()+")");
        viewHolder.tv_order_type.setText(huobiDataBean.getBaseCurrency()+"/"+huobiDataBean.getQuoteCurrency());
        viewHolder.tv_order_num.setHint("数量("+huobiDataBean.getBaseCurrency()+")");
        viewHolder.tv_now_price.setText("--");
        viewHolder.tv_num.setText("--");
        asksAdapter.setPriceSize(huobiDataBean.getPricePrecision());
        bidsAdapter.setPriceSize(huobiDataBean.getPricePrecision());

        asksAdapter.setNumSize(huobiDataBean.getAmountPrecision());
        bidsAdapter.setNumSize(huobiDataBean.getAmountPrecision());
        sendWs(true);
    }
    //订阅深度和指数
    public void sendWs(boolean isSubscribe) {
        aDoubleprice=0.0;
//        if (!isVisibility) {
//            return;
//        }
//        Log.i("wuli","huobip/"+huobiDataBean.getBaseCurrency()+"."+huobiDataBean.getQuoteCurrency());
        if (huobiDataBean!=null ) {
            Map<String, String> map = new HashMap<>();
            map.put("uri", (isSubscribe ? "subscribe" : "unsubscribe") + "-single-tick-verbose");
            map.put("contract","huobip/"+huobiDataBean.getBaseCurrency()+"."+huobiDataBean.getQuoteCurrency());
            WebSocketRequest.getInstance().sendData(GsonUtil.getInstance().toJson(map));
        }
        HandlerHelper.getinstance().initHander(TAG,
                viewHolder.rv_buy,
                new HandlerHelper.OnUpdataLinsener() {
                    @Override
                    public void onUpdataLinsener(Object val) {
//                        if (isVisibility) {
                            if (val instanceof WebSocketDepthBean) {
                                    showNowPrice(((WebSocketDepthBean) val).getLast());
                                    changeDepth((WebSocketDepthBean) val);
//                                else if (ObjectUtils.equals(tradeDetailBean.getIndex(),
//                                        ((WebSocketDepthBean) val).getContract())) {
//                                    ((View) viewHolder.tv_latest_index.getParent()).setVisibility(View.VISIBLE);
//                                    viewHolder.tv_latest_index.setText(
//                                            BigUIUtil.getinstance().getSymbol(tradeDetailBean.getPriceUnit()) +
//                                                    BigUIUtil.getinstance().bigPrice(((WebSocketDepthBean) val).getLast())
//                                    );
//                                }
                            }
//                        }
                    }
                });
        HandlerHelper.getinstance().setDelayMillis(400);
    }

    public void initWs(int type) {
        WebSocketRequest.getInstance().addCallBack(TAG, new WebSocketRequest.WebSocketCallBack() {
            @Override
            public void onDataSuccess(String name, String data, String info, int status) {
                if (ObjectUtils.equals(TAG, name) &&
                        !WebSocketRequest.getInstance().isSend() && isVisibility
                        &&huobiDataBean!=null) {
//                    Log.i("wuli","data="+data);
                    Gson gson=new Gson();
                    WebSocketDepthBean webSocketDepthBean =
                            GsonUtil.getInstance().toObj(data, "data", WebSocketDepthBean.class);
                    if (webSocketDepthBean != null  &&
                            !ListUtils.isEmpty(webSocketDepthBean.getAsks()) &&
                            !ListUtils.isEmpty(webSocketDepthBean.getBids()) &&
                            !TextUtils.isEmpty(webSocketDepthBean.getLast())&&webSocketDepthBean.getContract().equals("huobip/"+huobiDataBean.getBaseCurrency()+"."+huobiDataBean.getQuoteCurrency())) {
                        //深度
                        HandlerHelper.getinstance().put("huobip/"+huobiDataBean.getBaseCurrency()+"."+huobiDataBean.getQuoteCurrency(),
                                webSocketDepthBean) ;
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
        if ( val.getBids().size() >= depthSize &&
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
        if (asks.size()>5&&bids.size()>5)
        {
            if (isbuy)
            {
                viewHolder.tv_num.setText(asks.get(5).getPrice()+"");
            }else
            {
                viewHolder.tv_num.setText(bids.get(0).getPrice()+"");
            }
        }
        if (asksAdapter == null) {
            asksAdapter = new DepthAdapter(getActivity(), asks, false);
            asksAdapter.setOkex(true);
            asksAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    viewHolder.tv_num.setText(asksAdapter.getDatas().get(position).getPrice());
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
            bidsAdapter.setOkex(true);
            bidsAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    viewHolder.tv_num.setText(bidsAdapter.getDatas().get(position).getPrice());
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
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
        viewHolder.tv_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.tv_buy.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
                viewHolder.tv_sell.setTextColor(ContextCompat.getColor(getActivity(),R.color.color_E7625D));
                viewHolder.tv_buy.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.color_6BB578));
                viewHolder.tv_sell.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.color_F6F7F9));
                viewHolder.tv_transaction.setText("买入");
                viewHolder.tv_transaction.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.color_6BB578));
                isbuy=true;
                if (coinToCoinDetailBean!=null)
                {
                    viewHolder.tv_kemai.setText("可买");
                    if (huobiDataBean.getQuoteCurrency().equals("usdt"))
                    {
                        viewHolder.tv_buy_available.setText(StringUtil.getDecimal(6,Double.parseDouble(coinToCoinDetailBean.getUSDT().getAvi()))+" "+huobiDataBean.getQuoteCurrency());
                        String price=StringUtil.getDecimal(huobiDataBean.getAmountPrecision(),(Double.parseDouble(coinToCoinDetailBean.getUSDT().getAvi())/aDoubleprice));
                        viewHolder.tv_kemai_num.setText(price+" "+huobiDataBean.getBaseCurrency());
                    }else
                    {
                        viewHolder.tv_buy_available.setText(StringUtil.getDecimal(6,Double.parseDouble(coinToCoinDetailBean.getBTC().getAvi()))+" "+huobiDataBean.getQuoteCurrency());
                        String price=StringUtil.getDecimal(huobiDataBean.getAmountPrecision(),(Double.parseDouble(coinToCoinDetailBean.getBTC().getAvi())/aDoubleprice));
                        viewHolder.tv_kemai_num.setText(price+" "+huobiDataBean.getBaseCurrency());
                    }
                }
            }
        });
        viewHolder.tv_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.tv_sell.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
                viewHolder.tv_buy.setTextColor(ContextCompat.getColor(getActivity(),R.color.color_6BB578));
                viewHolder.tv_buy.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.color_F6F7F9));
                viewHolder.tv_sell.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.color_E7625D));
                viewHolder.tv_transaction.setText("卖出");
                viewHolder.tv_transaction.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.color_E7625D));
                isbuy=false;
                if (coinToCoinDetailBean!=null)
                {
                    viewHolder.tv_kemai.setText("可兑");
                    String avibuy="0";
                    if ("BCH".equals(huobiDataBean.getBaseCurrency().toUpperCase())) {
                        if (!TextUtils.isEmpty(coinToCoinDetailBean.getBCH().getAvi()))
                        avibuy = coinToCoinDetailBean.getBCH().getAvi();
                    } else if ("BCT".equals(huobiDataBean.getBaseCurrency().toUpperCase())) {
                        if (!TextUtils.isEmpty(coinToCoinDetailBean.getBCT().getAvi()))
                        avibuy = coinToCoinDetailBean.getBCT().getAvi();
                    } else if ("BSV".equals(huobiDataBean.getBaseCurrency().toUpperCase())) {
                        if (!TextUtils.isEmpty(coinToCoinDetailBean.getBSV().getAvi()))
                        avibuy = coinToCoinDetailBean.getBSV().getAvi();
                    } else if ("BTC".equals(huobiDataBean.getBaseCurrency().toUpperCase())) {
                        if (!TextUtils.isEmpty(coinToCoinDetailBean.getBCT().getAvi()))
                        avibuy = coinToCoinDetailBean.getBTC().getAvi();
                    } else if ("EOS".equals(huobiDataBean.getBaseCurrency().toUpperCase())) {
                        if (!TextUtils.isEmpty(coinToCoinDetailBean.getEOS().getAvi()))
                        avibuy = coinToCoinDetailBean.getEOS().getAvi();
                    } else if ("ETH".equals(huobiDataBean.getBaseCurrency().toUpperCase())) {
                        if (!TextUtils.isEmpty(coinToCoinDetailBean.getETH().getAvi()))
                        avibuy = coinToCoinDetailBean.getETH().getAvi();
                    } else if ("LTC".equals(huobiDataBean.getBaseCurrency().toUpperCase())) {
                        if (!TextUtils.isEmpty(coinToCoinDetailBean.getLTC().getAvi()))
                        avibuy = coinToCoinDetailBean.getLTC().getAvi();
                    } else {
                        if (!TextUtils.isEmpty(coinToCoinDetailBean.getXRP().getAvi()))
                        avibuy = coinToCoinDetailBean.getXRP().getAvi();
                    }
                    viewHolder.tv_buy_available.setText(avibuy+" "+huobiDataBean.getBaseCurrency());
                    String price=StringUtil.getDecimal(huobiDataBean.getPricePrecision(),(Double.parseDouble(avibuy)*aDoubleprice));
                    viewHolder.tv_kemai_num.setText(price+" "+huobiDataBean.getQuoteCurrency());               }
            }
        });
    }
    public boolean isbuy=true;
    CoinToCoinDetailBean coinToCoinDetailBean=null;
    public void showCoin(CoinToCoinDetailBean coinToCoinDetailBean)
    {
        this.coinToCoinDetailBean=coinToCoinDetailBean;
       if (isbuy&&aDoubleprice!=0&&coinToCoinDetailBean!=null)
        {
            viewHolder.tv_kemai.setText("可买");
            if (huobiDataBean.getQuoteCurrency().equals("usdt"))
            {
                viewHolder.tv_buy_available.setText(StringUtil.getDecimal(6,Double.parseDouble(coinToCoinDetailBean.getUSDT().getAvi()))+" "+huobiDataBean.getQuoteCurrency());
                String price=StringUtil.getDecimal(huobiDataBean.getAmountPrecision(),(Double.parseDouble(coinToCoinDetailBean.getUSDT().getAvi())/aDoubleprice));
                viewHolder.tv_kemai_num.setText(price+" "+huobiDataBean.getBaseCurrency());
            }else
            {
                viewHolder.tv_buy_available.setText(StringUtil.getDecimal(6,Double.parseDouble(coinToCoinDetailBean.getBTC().getAvi()))+" "+huobiDataBean.getQuoteCurrency());
                String price=StringUtil.getDecimal(huobiDataBean.getAmountPrecision(),(Double.parseDouble(coinToCoinDetailBean.getBTC().getAvi())/aDoubleprice));
                viewHolder.tv_kemai_num.setText(price+" "+huobiDataBean.getBaseCurrency());
            }
        }else
        {
            viewHolder.tv_kemai.setText("可兑");
            String avibuy="0";
            if ("BCH".equals(huobiDataBean.getBaseCurrency().toUpperCase())) {
                if (!TextUtils.isEmpty(coinToCoinDetailBean.getBCH().getAvi()))
                    avibuy = coinToCoinDetailBean.getBCH().getAvi();
            } else if ("BCT".equals(huobiDataBean.getBaseCurrency().toUpperCase())) {
                if (!TextUtils.isEmpty(coinToCoinDetailBean.getBCT().getAvi()))
                avibuy = coinToCoinDetailBean.getBCT().getAvi();
            } else if ("BSV".equals(huobiDataBean.getBaseCurrency().toUpperCase())) {
                if (!TextUtils.isEmpty(coinToCoinDetailBean.getBSV().getAvi()))
                avibuy = coinToCoinDetailBean.getBSV().getAvi();
            } else if ("BTC".equals(huobiDataBean.getBaseCurrency().toUpperCase())) {
                if (!TextUtils.isEmpty(coinToCoinDetailBean.getBTC().getAvi()))
                avibuy = coinToCoinDetailBean.getBTC().getAvi();
            } else if ("EOS".equals(huobiDataBean.getBaseCurrency().toUpperCase())) {
                if (!TextUtils.isEmpty(coinToCoinDetailBean.getEOS().getAvi()))
                avibuy = coinToCoinDetailBean.getEOS().getAvi();
            } else if ("ETH".equals(huobiDataBean.getBaseCurrency().toUpperCase())) {
                if (!TextUtils.isEmpty(coinToCoinDetailBean.getETH().getAvi()))
                avibuy = coinToCoinDetailBean.getETH().getAvi();
            } else if ("LTC".equals(huobiDataBean.getBaseCurrency().toUpperCase())) {
                if (!TextUtils.isEmpty(coinToCoinDetailBean.getLTC().getAvi()))
                avibuy = coinToCoinDetailBean.getLTC().getAvi();
            } else {
                if (!TextUtils.isEmpty(coinToCoinDetailBean.getXRP().getAvi()))
                avibuy = coinToCoinDetailBean.getXRP().getAvi();
            }
            viewHolder.tv_buy_available.setText(avibuy+" "+huobiDataBean.getBaseCurrency());
            String price=StringUtil.getDecimal(huobiDataBean.getPricePrecision(),(Double.parseDouble(avibuy)*aDoubleprice));
            viewHolder.tv_kemai_num.setText(price+" "+huobiDataBean.getQuoteCurrency());
    }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_onetoken;
    }


    public static class ViewHolder {
        public View rootView;
        public View touming;
        public MyListView listview;
        public IconFontTextview tv_order_type;
        public LinearLayout lin_order_type;
        public IconFontTextview tv_lever;
        public LinearLayout lin_lever;
        public SingleLineZoomTextView tv_now_price;
        public ImageView iv_to_kline;
        public LinearLayout lin_to_kline;
        public TextView tv_open;
        public TextView tv_close;
        public TextView tv_limit,tv_transaction,tv_kemai_num;
        public TextView tv_order_price_title;
        public TextView tv_trigger;
        public TextView tv_opponent_price;
        public FrameLayout fl_opponent_price_close;
        public TextView tv_limit_price;
        public FrameLayout fl_limit_price;
        public LinearLayout lin_order_select_price_close;
        public EditText tv_trigger_price;
        public TextView tv_trigger_price_unit;
        public LinearLayout lin_trigger_price;
        public EditText tv_order_price;
        public TextView tv_order_price_unit;
        public LinearLayout lin_order_price;
        public TextView tv_opponent_price_open;
        public FrameLayout fl_opponent_price_open;
        public TextView tv_buy_one_price;
        public FrameLayout fl_buy_one_price;
        public TextView tv_sell_one_price;
        public FrameLayout fl_sell_one_price;
        public LinearLayout lin_order_select_price_open;
        public EditText tv_order_num;
        public TextView tv_order_num_unit;
        public LinearLayout lin_order_num;
        public TextView tv_buy;
        public TextView tv_buy_left;
        public TextView tv_buy_available;
        public LinearLayout lin_buy_left;
        public TextView tv_buy_type;
        public TextView tv_buy_type_num;
        public TextView tv_sell,tv_num;
        public TextView tv_sell_left;
        public TextView tv_sell_available;
        public LinearLayout lin_sell_left;
        public TextView tv_sell_type;
        public TextView tv_sell_type_num;
        public TextView tv_show_price_type;
        public TextView tv_coin_num,tv_kemai;
        public RecyclerView rv_buy;
        public RecyclerView rv_sell;
        public SingleLineZoomTextView tv_latest_index;
        public SingleLineZoomTextView tv_fund_rate;
        public RecyclerView recycler_view;
        public LinearLayout lin_no_order;
        public LinearLayout lin_close_all;
        public SwipeRefreshLayout swipeRefreshLayout;
        public TextView reduce,increase;
//        public DrawerLayout main_drawer_layout;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            reduce=(TextView) rootView.findViewById(R.id.reduce);
            increase=(TextView) rootView.findViewById(R.id.increase);
            listview=(MyListView) rootView.findViewById(R.id.listview);
            tv_kemai_num=(TextView) rootView.findViewById(R.id.tv_kemai_num);
            tv_kemai=(TextView) rootView.findViewById(R.id.tv_kemai);
            this.touming=rootView.findViewById(R.id.touming);
            this.tv_transaction=(TextView) rootView.findViewById(R.id.tv_transaction);
            this.tv_num = (TextView) rootView.findViewById(R.id.tv_num);
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
            this.tv_order_price_title = (TextView) rootView.findViewById(R.id.tv_order_price_title);
            this.tv_trigger = (TextView) rootView.findViewById(R.id.tv_trigger);
            this.tv_opponent_price = (TextView) rootView.findViewById(R.id.tv_opponent_price);
            this.fl_opponent_price_close = (FrameLayout) rootView.findViewById(R.id.fl_opponent_price_close);
            this.tv_limit_price = (TextView) rootView.findViewById(R.id.tv_limit_price);
            this.fl_limit_price = (FrameLayout) rootView.findViewById(R.id.fl_limit_price);
            this.lin_order_select_price_close = (LinearLayout) rootView.findViewById(R.id.lin_order_select_price_close);
            this.tv_trigger_price = (EditText) rootView.findViewById(R.id.tv_trigger_price);
            this.tv_trigger_price_unit = (TextView) rootView.findViewById(R.id.tv_trigger_price_unit);
            this.lin_trigger_price = (LinearLayout) rootView.findViewById(R.id.lin_trigger_price);
            this.tv_order_price = (EditText) rootView.findViewById(R.id.tv_order_price);
            this.tv_order_price_unit = (TextView) rootView.findViewById(R.id.tv_order_price_unit);
            this.lin_order_price = (LinearLayout) rootView.findViewById(R.id.lin_order_price);
            this.tv_opponent_price_open = (TextView) rootView.findViewById(R.id.tv_opponent_price_open);
            this.fl_opponent_price_open = (FrameLayout) rootView.findViewById(R.id.fl_opponent_price_open);
            this.tv_buy_one_price = (TextView) rootView.findViewById(R.id.tv_buy_one_price);
            this.fl_buy_one_price = (FrameLayout) rootView.findViewById(R.id.fl_buy_one_price);
            this.tv_sell_one_price = (TextView) rootView.findViewById(R.id.tv_sell_one_price);
            this.fl_sell_one_price = (FrameLayout) rootView.findViewById(R.id.fl_sell_one_price);
            this.lin_order_select_price_open = (LinearLayout) rootView.findViewById(R.id.lin_order_select_price_open);
            this.tv_order_num = (EditText) rootView.findViewById(R.id.tv_order_num);
            this.tv_order_num_unit = (TextView) rootView.findViewById(R.id.tv_order_num_unit);
            this.lin_order_num = (LinearLayout) rootView.findViewById(R.id.lin_order_num);
            this.tv_buy = (TextView) rootView.findViewById(R.id.tv_buy);
            this.tv_buy_left = (TextView) rootView.findViewById(R.id.tv_buy_left);
            this.tv_buy_available = (TextView) rootView.findViewById(R.id.tv_buy_available);
            this.lin_buy_left = (LinearLayout) rootView.findViewById(R.id.lin_buy_left);
            this.tv_buy_type = (TextView) rootView.findViewById(R.id.tv_buy_type);
            this.tv_buy_type_num = (TextView) rootView.findViewById(R.id.tv_buy_type_num);
            this.tv_sell = (TextView) rootView.findViewById(R.id.tv_sell);
            this.tv_sell_left = (TextView) rootView.findViewById(R.id.tv_sell_left);
            this.tv_sell_available = (TextView) rootView.findViewById(R.id.tv_sell_available);
            this.lin_sell_left = (LinearLayout) rootView.findViewById(R.id.lin_sell_left);
            this.tv_sell_type = (TextView) rootView.findViewById(R.id.tv_sell_type);
            this.tv_sell_type_num = (TextView) rootView.findViewById(R.id.tv_sell_type_num);
            this.tv_show_price_type = (TextView) rootView.findViewById(R.id.tv_show_price_type);
            this.tv_coin_num = (TextView) rootView.findViewById(R.id.tv_coin_num);
            this.rv_buy = (RecyclerView) rootView.findViewById(R.id.rv_buy);
            this.rv_sell = (RecyclerView) rootView.findViewById(R.id.rv_sell);
            this.tv_latest_index = (SingleLineZoomTextView) rootView.findViewById(R.id.tv_latest_index);
            this.tv_fund_rate = (SingleLineZoomTextView) rootView.findViewById(R.id.tv_fund_rate);
            this.recycler_view = (RecyclerView) rootView.findViewById(R.id.recycler_view);
            this.lin_no_order = (LinearLayout) rootView.findViewById(R.id.lin_no_order);
            this.lin_close_all = (LinearLayout) rootView.findViewById(R.id.lin_close_all);
            this.swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);

//            this.main_drawer_layout = (DrawerLayout) rootView.findViewById(R.id.main_drawer_layout);
        }

    }
}