package com.sixbexchange.mvp.databinder;

import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;
import com.sixbexchange.mvp.delegate.FindPassWordDelegate;
import com.sixbexchange.server.HttpUrl;

import io.reactivex.disposables.Disposable;

public class FindPassWordBinder extends BaseDataBind<FindPassWordDelegate> {

    public FindPassWordBinder(FindPassWordDelegate viewDelegate) {
        super(viewDelegate);
    }

    public Disposable preReset(
            String mobile,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("mobile", mobile);
        return new HttpRequest.Builder()
                .setRequestCode(0x124)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().preReset)
                .setShowDialog(true)
                .setRequestName("获取钱包列表")
                .setRequestMode(HttpRequest.RequestMode.POST)
                .setParameterMode(HttpRequest.ParameterMode.Json)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
}