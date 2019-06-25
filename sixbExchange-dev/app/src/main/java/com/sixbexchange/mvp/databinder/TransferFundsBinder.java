package com.sixbexchange.mvp.databinder;

import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;
import com.sixbexchange.mvp.delegate.TransferFundsDelegate;
import com.sixbexchange.server.HttpUrl;

import io.reactivex.disposables.Disposable;

public class TransferFundsBinder extends BaseDataBind<TransferFundsDelegate> {

    public TransferFundsBinder(TransferFundsDelegate viewDelegate) {
        super(viewDelegate);
    }

    public Disposable allCoins(
            String exchange,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("exchange",exchange);
        return new HttpRequest.Builder()
                .setRequestCode(0x123)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().allCoins)
                .setShowDialog(true)
                .setRequestName("交易所币种列表")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
    public Disposable accounttrans(
            String from,
            String dest,
            String srcCoin,
            String quantity,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("from",from);
        put("dest",dest);
        put("srcCoin",srcCoin);
        put("quantity",quantity);
        return new HttpRequest.Builder()
                .setRequestCode(0x124)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().accounttrans)
                .setShowDialog(true)
                .setRequestName("资金划转")
                .setRequestMode(HttpRequest.RequestMode.POST)
                .setParameterMode(HttpRequest.ParameterMode.Json)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
}