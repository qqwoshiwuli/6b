package com.sixbexchange.mvp.databinder;

import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;
import com.sixbexchange.mvp.delegate.RegisteredDelegate;
import com.sixbexchange.server.HttpUrl;

import io.reactivex.disposables.Disposable;

public class RegisteredBinder extends BaseDataBind<RegisteredDelegate> {

    public RegisteredBinder(RegisteredDelegate viewDelegate) {
        super(viewDelegate);
    }

    public Disposable signup(
            String mobile,
            String password,
            String vcode,
            String icode,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("mobile", mobile);
        put("password", password);
        put("vcode", vcode);
        put("icode", icode);
        return new HttpRequest.Builder()
                .setRequestCode(0x123)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().signup)
                .setShowDialog(true)
                .setRequestName("注册")
                .setRequestMode(HttpRequest.RequestMode.POST)
                .setParameterMode(HttpRequest.ParameterMode.Json)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }

    public Disposable vcode(
            String mobile,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("mobile", mobile);
        return new HttpRequest.Builder()
                .setRequestCode(0x124)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().vcode)
                .setShowDialog(true)
                .setRequestName("注册验证码")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
}