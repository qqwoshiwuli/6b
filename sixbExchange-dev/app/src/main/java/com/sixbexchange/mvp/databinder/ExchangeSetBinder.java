package com.sixbexchange.mvp.databinder;

import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;
import com.sixbexchange.mvp.delegate.ExchangeSetDelegate;
import com.sixbexchange.server.HttpUrl;

import io.reactivex.disposables.Disposable;

public class ExchangeSetBinder extends BaseDataBind<ExchangeSetDelegate> {

    public ExchangeSetBinder(ExchangeSetDelegate viewDelegate) {
        super(viewDelegate);
    }

    public Disposable getConfig(
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        return new HttpRequest.Builder()
                .setRequestCode(0x123)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().getConfig)
                .setShowDialog(false)
                .setRequestName("切换okex张/币显示")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }

    public Disposable setOkexSetting(
            String status,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("status", status);
        return new HttpRequest.Builder()
                .setRequestCode(0x124)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().setOkexSetting)
                .setShowDialog(false)
                .setRequestName("切换okex张/币显示")
                .setRequestMode(HttpRequest.RequestMode.POST)
                .setParameterMode(HttpRequest.ParameterMode.Json)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }

    public Disposable saveConfig(
            String dealFailType,
            String continueTime,
            String radio,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("dealFailType", dealFailType);
        put("continueTime", continueTime);
        put("radio", radio);
        return new HttpRequest.Builder()
                .setRequestCode(0x125)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().saveConfig)
                .setShowDialog(false)
                .setRequestName("bitmex设置")
                .setRequestMode(HttpRequest.RequestMode.POST)
                .setParameterMode(HttpRequest.ParameterMode.Json)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
}