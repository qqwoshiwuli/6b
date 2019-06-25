package com.sixbexchange.mvp.databinder;

import android.util.Log;

import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;
import com.sixbexchange.mvp.delegate.LoginDelegate;
import com.sixbexchange.server.HttpUrl;

import io.reactivex.disposables.Disposable;

public class LoginBinder extends BaseDataBind<LoginDelegate> {

    public LoginBinder(LoginDelegate viewDelegate) {
        super(viewDelegate);
    }

    public Disposable login(
            String username,
            String password,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("password",password);
        put("username",username);
        return new HttpRequest.Builder()
                .setRequestCode(0x123)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().login)
                .setShowDialog(true)
                .setRequestName("登录")
                .setRequestMode(HttpRequest.RequestMode.POST)
                .setParameterMode(HttpRequest.ParameterMode.Json)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
    public Disposable userinfo(
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        return new HttpRequest.Builder()
                .setRequestCode(0x124)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().userinfo)
                .setShowDialog(true)
                .setRequestName("用户信息")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
}