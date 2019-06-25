package com.sixbexchange.mvp.databinder;

import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.ObjectUtils;
import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;
import com.fivefivelike.mybaselibrary.utils.ListUtils;
import com.fivefivelike.mybaselibrary.utils.ToastUtil;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.sixbexchange.mvp.delegate.OkexTrDelegate;
import com.sixbexchange.server.HttpUrl;
import com.sixbexchange.utils.BigUIUtil;
import com.sixbexchange.utils.UserSet;

import io.reactivex.disposables.Disposable;

public class OkexTrBinder extends BaseDataBind<OkexTrDelegate> {

    public OkexTrBinder(OkexTrDelegate viewDelegate) {
        super(viewDelegate);
    }

    public Disposable orderremark(
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("exchange", "okef");
        return new HttpRequest.Builder()
                .setRequestCode(0x128)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().orderremark)
                .setShowDialog(false)
                .setRequestName("计划委托弹窗")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }

    public Disposable accountopen(
            String exchange,
            String contract,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("exchange", exchange);
        put("contract", contract);
        return new HttpRequest.Builder()
                .setRequestCode(0x124)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().accountopen)
                .setShowDialog(false)
                .setRequestName("获取开仓可用")
                .setRequestMode(HttpRequest.RequestMode.POST)
                .setParameterMode(HttpRequest.ParameterMode.Json)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }

    public Disposable accountclose(
            String exchange,
            String contract,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("exchange", exchange);
        put("contract", contract);
        return new HttpRequest.Builder()
                .setRequestCode(0x127)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().accountclose)
                .setShowDialog(false)
                .setRequestName("获取平单可用")
                .setRequestMode(HttpRequest.RequestMode.POST)
                .setParameterMode(HttpRequest.ParameterMode.Json)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }

    public void checkOrderStop(int type, RequestCallback requestCallback) {
        if (TextUtils.isEmpty(viewDelegate.viewHolder.tv_trigger_price.getText().toString())) {
            ToastUtil.show("请输入触发价格");
            return;
        }
        if (!UiHeplUtils.isDouble(viewDelegate.viewHolder.tv_trigger_price.getText().toString())) {
            ToastUtil.show("请输入正确的触发价格");
            return;
        }
        if (TextUtils.isEmpty(viewDelegate.viewHolder.tv_order_price.getText().toString())) {
            ToastUtil.show("请输入价格");
            return;
        }
        if (!UiHeplUtils.isDouble(viewDelegate.viewHolder.tv_order_price.getText().toString())) {
            ToastUtil.show("请输入正确的价格");
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
        addRequest(orderstop(
                viewDelegate.tradeDetailBean.getExchange(),
                viewDelegate.tradeDetailBean.getCurrencyPair(),
                viewDelegate.tradeDetailBean.getOnlykey(),
                type + "",
                viewDelegate.viewHolder.tv_trigger_price.getText().toString(),
                viewDelegate.viewHolder.tv_order_price.getText().toString(),
                viewDelegate.viewHolder.tv_order_num.getText().toString(),
                requestCallback
        ));
    }

    public Disposable orderstop(
            String exchange,
            String contract,
            String currencyPair,
            String type,
            String triggerPrice,
            String entrustPrice,
            String amount,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("exchange", exchange);
        put("contract", contract);
        put("currencyPair", currencyPair);
        put("type", type);
        put("triggerPrice", triggerPrice);
        put("entrustPrice", entrustPrice);
        put("amount", amount);
        return new HttpRequest.Builder()
                .setRequestCode(0x125)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().orderstop)
                .setShowDialog(true)
                .setRequestName("计划委托")
                .setRequestMode(HttpRequest.RequestMode.POST)
                .setParameterMode(HttpRequest.ParameterMode.Json)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }


    /*
    公共参数：

exchange : 交易所( bitmex,okef)


bitmex 交易所：

matchPrice : 是否对手价（0 否，1 是）

price: 价格（就算是对手价也要传个价格过来 如果方向是1、4，则传 卖1 ； 如果方向是2、3，则传 买1）

type: 方向 （1 开多，2 开空，3 平多，4 平空）

currencyPair:交易对


其他交易所：

contract: 币对

price：价格

bs：方向（b 买，s 卖）

amount：数量
    **/
    String num;
    public void checkOrder(int type, RequestCallback requestCallback,String zhang) {
        if (TextUtils.isEmpty(viewDelegate.viewHolder.tv_order_price.getText().toString())) {
            ToastUtil.show("请输入价格");
            return;
        }
        if (TextUtils.isEmpty(viewDelegate.viewHolder.tv_order_num.getText().toString())) {
            ToastUtil.show("请输入数量");
            return;
        }
        if (!UiHeplUtils.isDouble(viewDelegate.viewHolder.tv_order_num.getText().toString())&&viewDelegate.percentint==0) {
            ToastUtil.show("请输入正确的数量");
            return;
        }
        if (!UiHeplUtils.isDouble(viewDelegate.viewHolder.tv_order_price.getText().toString()) &&
                !ObjectUtils.equals("市价", viewDelegate.viewHolder.tv_order_price.getText().toString()) &&
                !ObjectUtils.equals("对手价", viewDelegate.viewHolder.tv_order_price.getText().toString()) &&
                !ObjectUtils.equals("买一价", viewDelegate.viewHolder.tv_order_price.getText().toString()) &&
                !ObjectUtils.equals("卖一价", viewDelegate.viewHolder.tv_order_price.getText().toString())
                ) {
            ToastUtil.show("请输入正确的价格");
            return;
        }
        boolean isMarketPrice = false;
        String price = "";
        boolean isBuy = type == 1 || type == 4;
        if (viewDelegate.tradeMode == 0) {
            isMarketPrice = ObjectUtils.equals("对手价",
                    viewDelegate.viewHolder.tv_order_price.getText().toString());
            if (isMarketPrice) {
                price = getDepthPrice(isBuy);
            } else {
                if (ObjectUtils.equals("买一价", viewDelegate.viewHolder.tv_order_price.getText().toString())) {
                    price = getDepthPrice(false);// viewDelegate.bidsAdapter.getDatas().get(0).getPrice();
                } else if (ObjectUtils.equals("卖一价", viewDelegate.viewHolder.tv_order_price.getText().toString())) {
                    price = getDepthPrice(true);// viewDelegate.asksAdapter.getDatas().get(viewDelegate.depthSize - 1).getPrice();
                } else {
                    price = viewDelegate.viewHolder.tv_order_price.getText().toString();
                }
            }
        } else {
            isMarketPrice = ObjectUtils.equals("市价",
                    viewDelegate.viewHolder.tv_order_price.getText().toString());
            price = isMarketPrice ? getDepthPrice(isBuy) :
                    viewDelegate.viewHolder.tv_order_price.getText().toString();
        }
        if (TextUtils.isEmpty(price)) {
            ToastUtil.show("数据不完整,请尝试刷新");
            return;
        }
        if (!UiHeplUtils.isDouble(price)) {
            ToastUtil.show("价格数据异常");
            return;
        }
          if (viewDelegate.percentint==1)
        {
            double numtype=Double.parseDouble(zhang)*0.1;
            num = Math.round(numtype)+"";
        }else if (viewDelegate.percentint==2)
        {
            double numtype=Double.parseDouble(zhang)*0.2;
            num = Math.round(numtype)+"";
        }
        else if (viewDelegate.percentint==3)
        {
            double numtype=Double.parseDouble(zhang)*0.5;
            num = Math.round(numtype)+"";
        }else if (viewDelegate.percentint==4)
        {
            double numtype=Double.parseDouble(zhang);
            num = Math.round(numtype)+"";
        }else {
              num = viewDelegate.viewHolder.tv_order_num.getText().toString();
              if (ObjectUtils.equals("0", UserSet.getinstance().getLeafOrCoin())) {
                  num = BigUIUtil.getinstance().bigCoinToLeaf(num, price, viewDelegate.tradeDetailBean.getCurrency());
              }
          }
        addRequest(placeOrder(
                viewDelegate.tradeDetailBean.getExchange(),
                isMarketPrice ? "1" : "0",
                price,
                type,
                viewDelegate.tradeDetailBean.getOnlykey(),
                viewDelegate.tradeDetailBean.getCurrencyPair(),
                num,
                isBuy ? "b" : "s",
                requestCallback
        ));
    }

    private String getDepthPrice(boolean isAsks) {
        if (!ListUtils.isEmpty(viewDelegate.asksAdapter.getDatas()) && !ListUtils.isEmpty(viewDelegate.bidsAdapter.getDatas())) {
            return isAsks ?
                    viewDelegate.asksAdapter.getDatas().get(viewDelegate.depthSize - 1).getPrice() :
                    viewDelegate.bidsAdapter.getDatas().get(0).getPrice();
        }
        return "";
    }


    public Disposable placeOrder(
            String exchange,
            String matchPrice,
            String price,
            int type,
            String currencyPair,
            String contract,
            String amount,
            String bs,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("exchange", exchange);
        put("matchPrice", matchPrice);
        put("price", price);
        put("type", type);
        put("currencyPair", currencyPair);
        put("contract", contract);
        put("bs", bs);
        put("amount", amount);
        return new HttpRequest.Builder()
                .setRequestCode(0x125)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().placeOrder)
                .setShowDialog(true)
                .setRequestName("下单")
                .setRequestMode(HttpRequest.RequestMode.POST)
                .setParameterMode(HttpRequest.ParameterMode.Json)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }

    public Disposable cancelOrder(
            String exchange,
            String exchangeOid,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("exchange", exchange);
        put("exchangeOid", exchangeOid);
        return new HttpRequest.Builder()
                .setRequestCode(0x126)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().cancelOrder)
                .setShowDialog(true)
                .setRequestName("撤单")
                .setRequestMode(HttpRequest.RequestMode.POST)
                .setParameterMode(HttpRequest.ParameterMode.Json)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }

    public Disposable cancelAllOrder(
            String exchange,
            String contract,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("exchange", exchange);
        put("contract", contract);
        return new HttpRequest.Builder()
                .setRequestCode(0x126)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().cancelAllOrder)
                .setShowDialog(true)
                .setRequestName("撤单")
                .setRequestMode(HttpRequest.RequestMode.POST)
                .setParameterMode(HttpRequest.ParameterMode.Json)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
}