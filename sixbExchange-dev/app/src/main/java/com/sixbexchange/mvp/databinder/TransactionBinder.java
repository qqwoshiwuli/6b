package com.sixbexchange.mvp.databinder;

import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;
import com.sixbexchange.mvp.delegate.TransactionDelegate;
import com.sixbexchange.server.HttpUrl;

import io.reactivex.disposables.Disposable;

public class TransactionBinder extends BaseDataBind<TransactionDelegate> {

    public TransactionBinder(TransactionDelegate viewDelegate) {
        super(viewDelegate);
    }

    public Disposable exchangeList(
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        return new HttpRequest.Builder()
                .setRequestCode(0x123)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().exchangeList)
                .setShowDialog(false)
                .setRequestName("交易所列表")
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
                .setRequestCode(0x129)
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
                .setRequestCode(0x127)
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