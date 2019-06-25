package com.sixbexchange.mvp.delegate;

import android.graphics.Rect;
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
import android.widget.TextView;

import com.blankj.utilcode.util.CacheUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.fivefivelike.mybaselibrary.entity.ResultDialogEntity;
import com.fivefivelike.mybaselibrary.http.HandlerHelper;
import com.fivefivelike.mybaselibrary.http.OkWebsocket;
import com.fivefivelike.mybaselibrary.http.WebSocket4Request;
import com.fivefivelike.mybaselibrary.http.WebSocketRequest;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.ListUtils;
import com.fivefivelike.mybaselibrary.utils.StringUtil;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.view.IconFontTextview;
import com.fivefivelike.mybaselibrary.view.RoundButton;
import com.fivefivelike.mybaselibrary.view.SingleLineZoomTextView;
import com.fivefivelike.mybaselibrary.view.SwipeRefreshLayout;
import com.fivefivelike.mybaselibrary.view.dialog.ResultDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sixbexchange.R;
import com.sixbexchange.adapter.DepthAdapter;
import com.sixbexchange.adapter.TrOkexOrdersAdapter;
import com.sixbexchange.base.CacheName;
import com.sixbexchange.entity.bean.DepthBean;
import com.sixbexchange.entity.bean.OrderBean;
import com.sixbexchange.entity.bean.TradeDetailBean;
import com.sixbexchange.entity.bean.TransactionBean;
import com.sixbexchange.entity.bean.WebSocketDepthBean;
import com.sixbexchange.entity.bean.WebSocketOKDepthBean;
import com.sixbexchange.utils.BigUIUtil;
import com.sixbexchange.utils.UserSet;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.apache.commons.compress.compressors.deflate64.Deflate64CompressorInputStream;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;

public class OkexTrDelegate extends BaseDelegate {
    public ViewHolder viewHolder;
    String oldPrice = "";
    String TAG = getClass().getSimpleName();
    public TradeDetailBean tradeDetailBean;
    boolean isVisibility = false;
    public TransactionBean transactionBean = new TransactionBean();


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

    public void setTradeDetailBean(TradeDetailBean tradeDetailBean) {
        this.tradeDetailBean = tradeDetailBean;
        CacheUtils.getInstance().put(CacheName.TradeIdOkexCache, tradeDetailBean.getId());
        CacheUtils.getInstance().put(CacheName.TradeCoinOkexCache, tradeDetailBean.getCurrency());

        viewHolder.tv_order_price_unit.setText(tradeDetailBean.getPriceUnit());
        viewHolder.tv_show_price_type.setText("价格(" + tradeDetailBean.getPriceUnit() + ")");
        viewHolder.tv_order_num_unit.setText(BigUIUtil.getinstance().showLeafOrCoin(tradeDetailBean.getAmountUnit(), tradeDetailBean.getCurrency()));
        viewHolder.tv_coin_num.setText("数量(" +
                BigUIUtil.getinstance().showLeafOrCoin(tradeDetailBean.getAmountUnit(), tradeDetailBean.getCurrency()) + ")");
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
        asksAdapter.setSymbol(tradeDetailBean.getCurrency());
        bidsAdapter.setSymbol(tradeDetailBean.getCurrency());

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
                if ((ObjectUtils.equals("1", UserSet.getinstance().getLeafOrCoin()) ?
                        UiHeplUtils.numPointAfterLength(tradeDetailBean.getMinAmountUnit()) : 4) <
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

    private void showNowPrice(String price) {
        viewHolder.tv_now_price.setTextColor(CommonUtils.getColor(UserSet.getinstance().getRiseColor()));
        if (!TextUtils.isEmpty(oldPrice)) {
            if (new BigDecimal(price).doubleValue() < new BigDecimal(oldPrice).doubleValue()) {
                viewHolder.tv_now_price.setTextColor(CommonUtils.getColor(UserSet.getinstance().getDropColor()));
            }
        }
        oldPrice = price;
        showTransactionInfo(tradeMode);
        viewHolder.tv_now_price.setText(
                BigUIUtil.getinstance().getSymbol(tradeDetailBean.getPriceUnit()) +
                        BigUIUtil.getinstance().bigPrice(price));
    }

    private void depthPrice(String price) {
        if (tradeMode == 0) {
            tradeTypeChangeOpen(3);
        } else {
            tradeTypeChangeClose(0);
        }
        viewHolder.tv_order_price.setText(
                BigUIUtil.getinstance().bigPrice(price));
        //viewHolder.tv_order_num.setText(amount);
    }

    //市价1 限价0切换
    public void tradeTypeChangeClose(int type) {
        if (type == 0) {
            viewHolder.tv_opponent_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_limit_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
            viewHolder.tv_opponent_price.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_limit_price.setTextColor(CommonUtils.getColor(R.color.mark_color));
            viewHolder.tv_order_price.setText("");
            viewHolder.tv_order_price.setEnabled(true);
        } else {
            viewHolder.tv_opponent_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
            viewHolder.tv_limit_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_opponent_price.setTextColor(CommonUtils.getColor(R.color.mark_color));
            viewHolder.tv_limit_price.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_order_price.setText("市价");
            viewHolder.tv_order_price.setEnabled(false);
        }
    }
    //百分比
    public int percentint=0;
    public void Percent(int type)
    {
        percentint=type;
        if (type==1)
        {
            if (tradeMode==0)
            {
                if (UserSet.getinstance().getLeafOrCoin().equals("0"))
                {

                    double temp=Double.parseDouble(BigUIUtil.getinstance().bigLeafToCoin(transactionBean.getAvailableOpenMore(),
                            oldPrice, tradeDetailBean.getCurrency()))*0.1;
                    viewHolder.tv_order_num.setText(StringUtil.getDecimal(UiHeplUtils.numPointAfterLength(tradeDetailBean.getMinPriceUnit()),temp)+"");
                }else
                {
                    double temp=Double.parseDouble(transactionBean.getAvailableOpenMore())*0.1;
                    viewHolder.tv_order_num.setText(Math.round(temp)+"");
                }
            }else
            {
                viewHolder.tv_order_num.setText("10%");
            }
            viewHolder.tv_10.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
            viewHolder.tv_10.setTextColor(CommonUtils.getColor(R.color.mark_color));
            viewHolder.tv_20.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_20.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_50.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_50.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_100.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_100.setTextColor(CommonUtils.getColor(R.color.color_font4));
        }else if(type==2)
        {
            if (tradeMode==0)
            {
                if (UserSet.getinstance().getLeafOrCoin().equals("0"))
                {

                    double temp=Double.parseDouble(BigUIUtil.getinstance().bigLeafToCoin(transactionBean.getAvailableOpenMore(),
                            oldPrice, tradeDetailBean.getCurrency()))*0.2;
                    viewHolder.tv_order_num.setText(StringUtil.getDecimal(UiHeplUtils.numPointAfterLength(tradeDetailBean.getMinPriceUnit()),temp)+"");
                }else
                {
                    double temp=Double.parseDouble(transactionBean.getAvailableOpenMore())*0.2;
                    viewHolder.tv_order_num.setText(Math.round(temp)+"");
                }
            }else
            {
                viewHolder.tv_order_num.setText("20%");
            }
            viewHolder.tv_20.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
            viewHolder.tv_20.setTextColor(CommonUtils.getColor(R.color.mark_color));
            viewHolder.tv_10.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_10.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_50.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_50.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_100.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_100.setTextColor(CommonUtils.getColor(R.color.color_font4));
        }else if(type==3)
        {
            if (tradeMode==0)
            {
                if (UserSet.getinstance().getLeafOrCoin().equals("0"))
                {

                    double temp=Double.parseDouble(BigUIUtil.getinstance().bigLeafToCoin(transactionBean.getAvailableOpenMore(),
                            oldPrice, tradeDetailBean.getCurrency()))*0.5;
                    viewHolder.tv_order_num.setText(StringUtil.getDecimal(UiHeplUtils.numPointAfterLength(tradeDetailBean.getMinPriceUnit()),temp)+"");
                }else
                {
                    double temp=Double.parseDouble(transactionBean.getAvailableOpenMore())*0.5;
                    viewHolder.tv_order_num.setText(Math.round(temp)+"");
                }
            }else
            {
                viewHolder.tv_order_num.setText("50%");
            }
            viewHolder.tv_50.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
            viewHolder.tv_50.setTextColor(CommonUtils.getColor(R.color.mark_color));
            viewHolder.tv_10.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_10.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_20.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_20.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_100.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_100.setTextColor(CommonUtils.getColor(R.color.color_font4));
        }else if(type==4)
        {
            if (tradeMode==0)
            {
                if (UserSet.getinstance().getLeafOrCoin().equals("0"))
                {

                    double temp=Double.parseDouble(BigUIUtil.getinstance().bigLeafToCoin(transactionBean.getAvailableOpenMore(),
                            oldPrice, tradeDetailBean.getCurrency()));
                    viewHolder.tv_order_num.setText(StringUtil.getDecimal(UiHeplUtils.numPointAfterLength(tradeDetailBean.getMinPriceUnit()),temp)+"");
                }else
                {
                    viewHolder.tv_order_num.setText(transactionBean.getAvailableOpenMore()+"");
                }
            }else
            {
                viewHolder.tv_order_num.setText("100%");
            }
            viewHolder.tv_100.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
            viewHolder.tv_100.setTextColor(CommonUtils.getColor(R.color.mark_color));
            viewHolder.tv_10.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_10.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_20.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_20.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_50.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_50.setTextColor(CommonUtils.getColor(R.color.color_font4));
        }else
        {
            percentint=0;
            viewHolder.tv_order_num.setText("");
            viewHolder.tv_100.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_100.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_10.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_10.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_20.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_20.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_50.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_50.setTextColor(CommonUtils.getColor(R.color.color_font4));
        }
    }
    //对手价0 买1 1 卖1 2
    int openTradeType = 3;
    public void tradeTypeChangeOpen(int type) {
        if (openTradeType == type) {
            openTradeType = 3;
        } else {
            openTradeType = type;
        }
        if (openTradeType == 0) {
            viewHolder.tv_opponent_price_open.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
            viewHolder.tv_buy_one_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_sell_one_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_opponent_price_open.setTextColor(CommonUtils.getColor(R.color.mark_color));
            viewHolder.tv_buy_one_price.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_sell_one_price.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_order_price.setText("对手价");
            viewHolder.tv_order_price.setEnabled(false);
        } else if (openTradeType == 1) {
            viewHolder.tv_opponent_price_open.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_buy_one_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
            viewHolder.tv_sell_one_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_opponent_price_open.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_buy_one_price.setTextColor(CommonUtils.getColor(R.color.mark_color));
            viewHolder.tv_sell_one_price.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_order_price.setText("买一价");
            viewHolder.tv_order_price.setEnabled(false);
        } else if (openTradeType == 2) {
            viewHolder.tv_opponent_price_open.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_buy_one_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_sell_one_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
            viewHolder.tv_opponent_price_open.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_buy_one_price.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_sell_one_price.setTextColor(CommonUtils.getColor(R.color.mark_color));
            viewHolder.tv_order_price.setText("卖一价");
            viewHolder.tv_order_price.setEnabled(false);
        } else {
            viewHolder.tv_opponent_price_open.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_buy_one_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_sell_one_price.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_opponent_price_open.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_buy_one_price.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_sell_one_price.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_order_price.setText("");
            viewHolder.tv_order_price.setEnabled(true);
        }
    }

    //订阅深度和指数
    public void sendWs(boolean isSubscribe) {
        if (!isVisibility) {
            return;
        }
        if (WebSocket4Request.getInstance().getCallBack(TAG) != null &&
                tradeDetailBean != null) {
            try {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("op",(isSubscribe ? "subscribe" : "unsubscribe").toString());
                JSONArray array1 =new JSONArray();
                array1.put(tradeDetailBean.getDelivery());
//                array1.put(tradeDetailBean.getIndex());
                jsonObject.put("args",array1);
                WebSocket4Request.getInstance().sendData(jsonObject.toString());

                JSONObject jsonObject2=new JSONObject();
                jsonObject2.put("op",(isSubscribe ? "subscribe" : "unsubscribe").toString());
                JSONArray array2 =new JSONArray();
                array2.put(tradeDetailBean.getIndex());
                jsonObject2.put("args",array2);
                WebSocket4Request.getInstance().sendData(jsonObject2.toString());

                JSONObject jsonObject3=new JSONObject();
                jsonObject3.put("op",(isSubscribe ? "subscribe" : "unsubscribe").toString());
                JSONArray array3 =new JSONArray();
                array3.put(tradeDetailBean.getCurrency());
                jsonObject3.put("args",array3);
                WebSocket4Request.getInstance().sendData(jsonObject3.toString());

//                Log.i("wuli","jsonObject="+jsonObject.toString());
//                Log.i("wuli","jsonObject="+jsonObject2.toString());
//                Log.i("wuli","jsonObject="+jsonObject3.toString());
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        HandlerHelper.getinstance().initHander(TAG,
                viewHolder.rv_buy,
                new HandlerHelper.OnUpdataLinsener() {
                    @Override
                    public void onUpdataLinsener(Object val) {
                        if (isVisibility) {
                            if (val instanceof WebSocketOKDepthBean && tradeDetailBean != null) {
                                if (ObjectUtils.equals("futures/depth5",
                                        ((WebSocketOKDepthBean) val).getTable())) {
                                    changeDepth((WebSocketOKDepthBean) val);
                                }
                                else if (ObjectUtils.equals("index/ticker",
                                        ((WebSocketOKDepthBean) val).getTable())) {
                                    ((View) viewHolder.tv_latest_index.getParent()).setVisibility(View.VISIBLE);
                                    viewHolder.tv_latest_index.setText(
                                            BigUIUtil.getinstance().getSymbol(tradeDetailBean.getPriceUnit()) +
                                                    BigUIUtil.getinstance().bigPrice(((WebSocketOKDepthBean) val).getData().get(0).getLast())
                                    );
                                }
                                else if (ObjectUtils.equals("futures/ticker",
                                        ((WebSocketOKDepthBean) val).getTable())) {
                                showNowPrice(((WebSocketOKDepthBean) val).getData().get(0).getLast());
                                }
                            }
                        }
                    }
                });
        HandlerHelper.getinstance().setDelayMillis(400);
    }
    private static String uncompress(byte[] bytes) {
        try (final ByteArrayOutputStream out = new ByteArrayOutputStream();
             final ByteArrayInputStream in = new ByteArrayInputStream(bytes);
             final Deflate64CompressorInputStream zin = new Deflate64CompressorInputStream(in)) {
            final byte[] buffer = new byte[1024];
            int offset;
            while (-1 != (offset = zin.read(buffer))) {
                out.write(buffer, 0, offset);
            }
            return out.toString();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void initWs(int type) {
        tradeModeChange(type);
        TAG = TAG + tradeMode;
        WebSocket4Request.getInstance().getcallback(new WebSocket4Request.WebSocketCallBack() {
            @Override
            public void onDataSuccess(String name, ByteString data, String info, int status) {
            }
            @Override
            public void onData(String data) {
//                Log.i("wuli","22"+isVisibility);
                    Gson gson=new Gson();
                    WebSocketOKDepthBean WebSocketOKDepthBean =gson.fromJson(data,WebSocketOKDepthBean.class);
                            if (null!=WebSocketOKDepthBean.getTable())
                            {
                                    HandlerHelper.getinstance().put(tradeDetailBean.getDelivery(),
                                            WebSocketOKDepthBean);
                            }
            }

            @Override
            public void onDataError(String name, ByteString data, String info, int status) {

            }
        });
        WebSocket4Request.getInstance().addCallBack(TAG, new WebSocket4Request.WebSocketCallBack() {
            @Override
            public void onDataSuccess(String name, ByteString data, String info, int status) {
//                String temp=uncompress(data.toByteArray());
//                Log.i("wuli","data="+temp);
                if (isVisibility) {
//                    Gson gson=new Gson();
//                    WebSocketOKDepthBean WebSocketOKDepthBean =gson.fromJson(temp,WebSocketOKDepthBean.class);
//                    if (WebSocketOKDepthBean != null  &&
//                            !ListUtils.isEmpty(WebSocketOKDepthBean.getData().get(0).getAsks()) &&
//                            !ListUtils.isEmpty(WebSocketOKDepthBean.getData().get(0).getBids())
//                            ) {
//                        //深度
//                        HandlerHelper.getinstance().put(tradeDetailBean.getDelivery(),
//                                WebSocketOKDepthBean);
//                    }
//                    else if (WebSocketOKDepthBean != null &&
//                            ObjectUtils.equals(tradeDetailBean.getIndex(),
//                                    WebSocketOKDepthBean.getContract())) {
//                        //指数
//                        HandlerHelper.getinstance().put(tradeDetailBean.getIndex(),
//                                WebSocketOKDepthBean);
//                    }
                }
            }

            @Override
            public void onData(String data) {

            }

            @Override
            public void onDataError(String name, ByteString data, String info, int status) {

            }
        });
    }

    public int depthSize = 5;

    private void changeDepth(WebSocketOKDepthBean val) {
        if (ObjectUtils.equals("futures/depth5", ((WebSocketOKDepthBean) val).getTable())
                && val.getData().get(0).getBids().size() >= depthSize &&
                val.getData().get(0).getAsks().size() >= depthSize) {
            List<DepthBean> asks = new ArrayList<>();
            for (int i = 0; i < val.getData().get(0).getAsks().size(); i++) {
                DepthBean depthBean=new DepthBean();
                depthBean.setPrice(val.getData().get(0).getAsks().get(i).get(0)+"");
                depthBean.setVolume(val.getData().get(0).getAsks().get(i).get(1)+"");
                asks.add(depthBean);
            }
            asks.subList(0, depthSize);
            Collections.reverse(asks);
            List<DepthBean> bids = new ArrayList<>();
            for (int i = 0; i < val.getData().get(0).getBids().size(); i++) {
                DepthBean depthBean=new DepthBean();
                depthBean.setPrice(val.getData().get(0).getBids().get(i).get(0)+"");
                depthBean.setVolume(val.getData().get(0).getBids().get(i).get(1)+"");
                bids.add(depthBean);
            }
            initDepth(
                    asks,
                    bids.subList(0, depthSize));
        }
    }

    public DepthAdapter asksAdapter;
    public DepthAdapter bidsAdapter;

    //显示深度
    public void initDepth(List<DepthBean> asks, List<DepthBean> bids) {
        if (asksAdapter == null) {
            asksAdapter = new DepthAdapter(getActivity(), asks, false);
            asksAdapter.setOkex(true);
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
            viewHolder.rv_buy.addItemDecoration(new SpacesItemDecoration(12));
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
            viewHolder.rv_sell.addItemDecoration(new SpacesItemDecoration(12));
            viewHolder.rv_sell.setAdapter(bidsAdapter);
        } else {
            bidsAdapter.setData(bids);
        }
    }
    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildPosition(view) == 0)
                outRect.top = space;
        }
    }
    //开仓0  平仓1
    public int tradeMode = 0;
    public boolean isLimit = true;

    public void tradeTypeChange(boolean isLimit) {
        this.isLimit = isLimit;
        if (isLimit) {
            viewHolder.tv_trigger.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_limit.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r1));
            viewHolder.tv_trigger.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_limit.setTextColor(CommonUtils.getColor(R.color.color_Primary));
            if (tradeMode == 0) {
                viewHolder.tv_order_price_title.setText("价格");
                viewHolder.lin_trigger_price.setVisibility(View.GONE);
                viewHolder.lin_order_select_price_open.setVisibility(View.VISIBLE);
                ((LinearLayout.LayoutParams) viewHolder.lin_order_num.getLayoutParams()).topMargin = 0;
                viewHolder.lin_order_num.requestLayout();
            } else {
                viewHolder.tv_order_price_title.setText("价格");
                viewHolder.lin_trigger_price.setVisibility(View.GONE);
                viewHolder.lin_order_select_price_close.setVisibility(View.VISIBLE);
                tradeTypeChangeOpen(3);
            }
        } else {
            viewHolder.tv_limit.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_trigger.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r1));
            viewHolder.tv_limit.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_trigger.setTextColor(CommonUtils.getColor(R.color.color_Primary));

            if (tradeMode == 0) {
                ((LinearLayout.LayoutParams) viewHolder.lin_order_num.getLayoutParams()).topMargin = (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_20px);
                viewHolder.lin_order_num.requestLayout();
                viewHolder.tv_order_price_title.setText("委托价格");
                viewHolder.lin_trigger_price.setVisibility(View.VISIBLE);
                viewHolder.lin_order_select_price_open.setVisibility(View.GONE);
            } else {
                viewHolder.tv_order_price_title.setText("委托价格");
                tradeTypeChangeClose(0);
                viewHolder.lin_trigger_price.setVisibility(View.VISIBLE);
                viewHolder.lin_order_select_price_close.setVisibility(View.GONE);
            }
        }
    }

    public void tradeModeChange(int type) {
        tradeMode = type;
        if (type == 0) {
            viewHolder.tv_close.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_open.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
            viewHolder.tv_close.setTextColor(CommonUtils.getColor(R.color.color_font4));
            viewHolder.tv_open.setTextColor(CommonUtils.getColor(R.color.mark_color));

            viewHolder.tv_buy.setText("买入开多(看涨)");
            viewHolder.tv_sell.setText("卖出开空(看跌)");


            viewHolder.tv_buy_left.setText("可用");
            viewHolder.tv_buy_type.setText("可开多");
            viewHolder.tv_sell_left.setText("可用");
            viewHolder.tv_sell_type.setText("可开空");

            viewHolder.tv_buy_type_num.setVisibility(View.VISIBLE);
            viewHolder.tv_sell_type_num.setVisibility(View.VISIBLE);

            viewHolder.lin_order_select_price_open.setVisibility(View.VISIBLE);
            viewHolder.lin_order_select_price_close.setVisibility(View.GONE);

            ((LinearLayout.LayoutParams) viewHolder.lin_order_price.getLayoutParams())
                    .bottomMargin = 0;
            ((LinearLayout.LayoutParams) viewHolder.lin_order_price.getLayoutParams())
                    .topMargin = (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_10px);
            viewHolder.lin_order_price.requestLayout();
        } else {
            viewHolder.tv_close.setBackground(CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
            viewHolder.tv_open.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
            viewHolder.tv_close.setTextColor(CommonUtils.getColor(R.color.mark_color));
            viewHolder.tv_open.setTextColor(CommonUtils.getColor(R.color.color_font4));

            viewHolder.tv_buy.setText("买入平空");
            viewHolder.tv_sell.setText("卖出平多");


            viewHolder.tv_buy_left.setText("可平");
            viewHolder.tv_buy_type.setText("持仓");
            viewHolder.tv_sell_left.setText("可平");
            viewHolder.tv_sell_type.setText("持仓");

            viewHolder.tv_buy_type_num.setVisibility(View.VISIBLE);
            viewHolder.tv_sell_type_num.setVisibility(View.VISIBLE);

            viewHolder.lin_order_select_price_open.setVisibility(View.GONE);
            viewHolder.lin_order_select_price_close.setVisibility(View.VISIBLE);
            viewHolder.lin_sell_left.setBackgroundResource(R.drawable.shape_gray_border_r5);
            viewHolder.lin_buy_left.setBackgroundResource(R.drawable.shape_gray_border_r5);
            viewHolder.lin_sell_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Percent(4);
                }
            });
            viewHolder.lin_buy_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Percent(4);
                }
            });
            viewHolder.lin_sell_left2.setBackgroundResource(R.drawable.shape_gray_border_r5);
            viewHolder.lin_buy_left2.setBackgroundResource(R.drawable.shape_gray_border_r5);
            viewHolder.lin_sell_left2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Percent(4);
                }
            });
            viewHolder.lin_buy_left2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Percent(4);
                }
            });
        }
        tradeTypeChange(isLimit);
        showTransactionInfo(type);
    }

    private void  showTransactionInfo(int type){
        if (transactionBean != null && tradeDetailBean != null) {
            if (type == 0) {
                viewHolder.tv_buy_type_num.setText(
                        BigUIUtil.getinstance().bigLeafToCoin(transactionBean.getAvailableOpenMore(),
                                oldPrice, tradeDetailBean.getCurrency()) +
                                BigUIUtil.getinstance().showLeafOrCoin(tradeDetailBean.getAmountUnit(), tradeDetailBean.getCurrency()));
                viewHolder.tv_buy_available.setText(transactionBean.getUsableOpenMore() + tradeDetailBean.getMarginUnit());
                viewHolder.tv_sell_type_num.setText(
                        BigUIUtil.getinstance().bigLeafToCoin(transactionBean.getAvailableOpenSpace(),
                                oldPrice, tradeDetailBean.getCurrency()) + BigUIUtil.getinstance().showLeafOrCoin(tradeDetailBean.getAmountUnit(), tradeDetailBean.getCurrency()));
                viewHolder.tv_sell_available.setText(transactionBean.getUsableOpenSpace() + tradeDetailBean.getMarginUnit());
            } else {
                viewHolder.tv_buy_available.setText(
                        BigUIUtil.getinstance().bigLeafToCoin(transactionBean.getAvailableflatSpace(),
                                oldPrice, tradeDetailBean.getCurrency()) + BigUIUtil.getinstance().showLeafOrCoin(tradeDetailBean.getAmountUnit(), tradeDetailBean.getCurrency()));
               viewHolder.tv_sell_available.setText(
                        BigUIUtil.getinstance().bigLeafToCoin(transactionBean.getAvailableflatMore(),
                                oldPrice, tradeDetailBean.getCurrency()) + BigUIUtil.getinstance().showLeafOrCoin(tradeDetailBean.getAmountUnit(), tradeDetailBean.getCurrency()));
                viewHolder.tv_buy_type_num.setText(
                        BigUIUtil.getinstance().bigLeafToCoin(transactionBean.getAlreadyOpenSpace(),
                                oldPrice, tradeDetailBean.getCurrency()) +
                                BigUIUtil.getinstance().showLeafOrCoin(tradeDetailBean.getAmountUnit(), tradeDetailBean.getCurrency()));
                 viewHolder.tv_sell_type_num.setText(
                        BigUIUtil.getinstance().bigLeafToCoin(transactionBean.getAlreadyOpenMore(),
                                oldPrice, tradeDetailBean.getCurrency()) + BigUIUtil.getinstance().showLeafOrCoin(tradeDetailBean.getAmountUnit(), tradeDetailBean.getCurrency()));

            }
        }
    }

    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
        //平仓 交易模式
        viewHolder.fl_opponent_price_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tradeTypeChangeClose(1);
            }
        });
        viewHolder.fl_limit_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tradeTypeChangeClose(0);
            }
        });

        //开仓交易模式
        viewHolder.fl_opponent_price_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tradeTypeChangeOpen(0);
            }
        });
        viewHolder.fl_buy_one_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tradeTypeChangeOpen(1);
            }
        });
        viewHolder.fl_sell_one_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tradeTypeChangeOpen(2);
            }
        });
        viewHolder.tv_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Percent(1);
            }
        });
        viewHolder.tv_20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Percent(2);
            }
        });
        viewHolder.tv_50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Percent(3);
            }
        });
        viewHolder.tv_100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Percent(4);
            }
        });
        viewHolder.tv_order_price.addTextChangedListener(priceTextWatcher);
        viewHolder.tv_order_num.addTextChangedListener(numTextWatcher);
    }

    public TrOkexOrdersAdapter adapter;

    public void initList(List<OrderBean> data) {
        if (adapter == null) {
            adapter = new TrOkexOrdersAdapter(getActivity(), data);
            viewHolder.recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            viewHolder.recycler_view.setAdapter(adapter);
        } else {
            adapter.setTradeDetailBean(tradeDetailBean);
            adapter.setData(data);
            viewHolder.lin_no_order.setVisibility(
                    ListUtils.isEmpty(data) ? View.VISIBLE : View.GONE
            );
            viewHolder.lin_close_all.setVisibility(
                    !ListUtils.isEmpty(data) ? View.VISIBLE : View.GONE
            );
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_okex_tr;
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
        public LinearLayout lin_buy_left2;
        public TextView tv_buy_type;
        public TextView tv_buy_type_num;
        public TextView tv_sell;
        public TextView tv_sell_left;
        public TextView tv_sell_available;
        public LinearLayout lin_sell_left;
        public LinearLayout lin_sell_left2;
        public TextView tv_sell_type;
        public TextView tv_sell_type_num;
        public TextView tv_show_price_type;
        public TextView tv_coin_num;
        public RecyclerView rv_buy;
        public RecyclerView rv_sell;
        public SingleLineZoomTextView tv_latest_index;
        public SingleLineZoomTextView tv_fund_rate;
        public RecyclerView recycler_view;
        public LinearLayout lin_no_order;
        public LinearLayout lin_close_all;
        public SwipeRefreshLayout swipeRefreshLayout;
        public FrameLayout fl_left;
        public DrawerLayout main_drawer_layout;
        public TextView tv_10,tv_20,tv_50,tv_100;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.lin_buy_left2 = (LinearLayout) rootView.findViewById(R.id.lin_buy_left2);
            this.lin_sell_left2 = (LinearLayout) rootView.findViewById(R.id.lin_sell_left2);
            this.tv_10= (TextView) rootView.findViewById(R.id.tv_10);
            this.tv_20= (TextView) rootView.findViewById(R.id.tv_20);
            this.tv_50= (TextView) rootView.findViewById(R.id.tv_50);
            this.tv_100= (TextView) rootView.findViewById(R.id.tv_100);
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
            this.fl_left = (FrameLayout) rootView.findViewById(R.id.fl_left);
            this.main_drawer_layout = (DrawerLayout) rootView.findViewById(R.id.main_drawer_layout);
        }

    }
}