package com.sixbexchange.mvp.databinder;

import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;
import com.sixbexchange.mvp.delegate.ShanDuiDelegate;
import com.sixbexchange.mvp.delegate.TrParentsDelegate;
import com.sixbexchange.server.HttpUrl;

import io.reactivex.disposables.Disposable;

public class ShanDuiBinder extends BaseDataBind<ShanDuiDelegate> {

    public ShanDuiBinder(ShanDuiDelegate viewDelegate) {
        super(viewDelegate);
    }

    public Disposable bitmexbindStatus(
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        return new HttpRequest.Builder()
                .setRequestCode(0x123)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().bitmexbindStatus)
                .setShowDialog(false)
                .setRequestName("bitmex开启状态")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }

    public Disposable bitmexbind(
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        return new HttpRequest.Builder()
                .setRequestCode(0x124)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().bitmexbind)
                .setShowDialog(true)
                .setRequestName("开启bitmex")
                .setRequestMode(HttpRequest.RequestMode.POST)
                .setParameterMode(HttpRequest.ParameterMode.Json)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }

}