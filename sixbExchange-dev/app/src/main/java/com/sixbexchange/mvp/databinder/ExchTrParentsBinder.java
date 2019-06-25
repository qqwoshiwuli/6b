package com.sixbexchange.mvp.databinder;

import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;
import com.sixbexchange.mvp.delegate.ExchTrParentsDelegate;
import com.sixbexchange.server.HttpUrl;

import io.reactivex.disposables.Disposable;

public class ExchTrParentsBinder extends BaseDataBind<ExchTrParentsDelegate> {

    public ExchTrParentsBinder(ExchTrParentsDelegate viewDelegate) {
        super(viewDelegate);
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

    public Disposable tradedetail(
            String exchange,
            String id,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("exchange", exchange);
        put("id", id);
        return new HttpRequest.Builder()
                .setRequestCode(0x124)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().tradedetail)
                .setShowDialog(false)
                .setRequestName("交易信息")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
    public Disposable tradelist(
            String exchange,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("exchange", exchange);
        return new HttpRequest.Builder()
                .setRequestCode(0x125)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().tradelist)
                .setShowDialog(false)
                .setRequestName("交易所交易对")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
    public Disposable tradelistok(
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        return new HttpRequest.Builder()
                .setRequestCode(0x128)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().openokefAll)
                .setShowDialog(false)
                .setRequestName("交易所交易对")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
    public Disposable tradeCoins(
            String exchange,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("exchange", exchange);
        return new HttpRequest.Builder()
                .setRequestCode(0x126)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().tradeCoins)
                .setShowDialog(false)
                .setRequestName("获取交易所下的币种列表")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
    public Disposable tradeall(
            String exchange,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("exchange", exchange);
        return new HttpRequest.Builder()
                .setRequestCode(0x127)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().tradeall)
                .setShowDialog(false)
                .setRequestName("交易所交易对")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
    public Disposable tradeall2(
            String exchange,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("exchange", exchange);
        return new HttpRequest.Builder()
                .setRequestCode(0x127)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().tradeall2)
                .setShowDialog(false)
                .setRequestName("交易所交易对")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
}