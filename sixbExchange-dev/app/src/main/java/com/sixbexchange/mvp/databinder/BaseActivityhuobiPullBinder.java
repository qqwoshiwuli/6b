package com.sixbexchange.mvp.databinder;

import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;
import com.sixbexchange.mvp.delegate.BaseActivityPullDelegate;
import com.sixbexchange.mvp.delegate.BaseActivityhuobiPullDelegate;
import com.sixbexchange.server.HttpUrl;

import io.reactivex.disposables.Disposable;

public class BaseActivityhuobiPullBinder<T extends BaseActivityhuobiPullDelegate> extends BaseDataBind<T> {
    public BaseActivityhuobiPullBinder(T viewDelegate) {
        super(viewDelegate);
    }
    public Disposable tradeall(
            String exchange,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("exchange", exchange);
        return new HttpRequest.Builder()
                .setRequestCode(0x123)
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

}