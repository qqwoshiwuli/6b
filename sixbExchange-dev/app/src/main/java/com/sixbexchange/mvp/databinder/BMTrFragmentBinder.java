package com.sixbexchange.mvp.databinder;

import android.text.TextUtils;

import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;
import com.fivefivelike.mybaselibrary.utils.ToastUtil;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.sixbexchange.mvp.delegate.BMTrFragmentDelegate;
import com.sixbexchange.server.HttpUrl;

import io.reactivex.disposables.Disposable;

public class BMTrFragmentBinder extends BaseDataBind<BMTrFragmentDelegate> {

    public BMTrFragmentBinder(BMTrFragmentDelegate viewDelegate) {
        super(viewDelegate);
    }
    public Disposable wsAuth(
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        return new HttpRequest.Builder()
                .setRequestCode(0x131)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().wsAuth)
                .setShowDialog(false)
                .setRequestName("获取ws权限")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
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
        String price = "";
        if (viewDelegate.stopLossType != 1) {
            if (TextUtils.isEmpty(viewDelegate.viewHolder.tv_order_price.getText().toString())) {
                ToastUtil.show("请输入价格");
                return;
            }
            price = viewDelegate.viewHolder.tv_order_price.getText().toString();
            if (!UiHeplUtils.isDouble(price)) {
                ToastUtil.show("请输入正确的价格");
                return;
            }
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
                price,
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
                .setRequestCode(0x130)
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

    public Disposable changeLeverage(
            String exchange,
            String contract,
            String rateOrAmount,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("exchange", exchange);
        put("contract", contract);
        put("rateOrAmount", rateOrAmount);
        return new HttpRequest.Builder()
                .setRequestCode(0x129)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().changeLeverage)
                .setShowDialog(true)
                .setRequestName("修改杠杆")
                .setRequestMode(HttpRequest.RequestMode.POST)
                .setParameterMode(HttpRequest.ParameterMode.Json)
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

    public Disposable accountgetOrders(
            String exchange,
            String contract,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("exchange", exchange);
        put("state", "active");
        put("contract", contract);
        return new HttpRequest.Builder()
                .setRequestCode(0x123)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().accountgetOrders)
                .setShowDialog(false)
                .setRequestName("获取单个合约的未完成订单")
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

    public Disposable getLeverage(
            String exchange,
            String contract,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("exchange", exchange);
        put("contract", contract);
        return new HttpRequest.Builder()
                .setRequestCode(0x128)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().getLeverage)
                .setShowDialog(false)
                .setRequestName("获取当前杠杆")
                .setRequestMode(HttpRequest.RequestMode.POST)
                .setParameterMode(HttpRequest.ParameterMode.Json)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
}