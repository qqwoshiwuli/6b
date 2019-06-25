package com.sixbexchange.mvp.databinder;

import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;
import com.sixbexchange.mvp.delegate.SetNewPasswordDelegate;
import com.sixbexchange.server.HttpUrl;

import io.reactivex.disposables.Disposable;

public class SetNewPasswordBinder extends BaseDataBind<SetNewPasswordDelegate> {

    public SetNewPasswordBinder(SetNewPasswordDelegate viewDelegate) {
        super(viewDelegate);
    }

    public Disposable setpassword(
            String mobile,
            String vcode,
            String newPassword,
            String oldPassword,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("mobile", mobile);
        put("oldPassword", oldPassword);
        put("newPassword", newPassword);
        put("vcode", vcode);
        return new HttpRequest.Builder()
                .setRequestCode(0x123)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().setpassword)
                .setShowDialog(true)
                .setRequestName("找回密码验证码")
                .setRequestMode(HttpRequest.RequestMode.POST)
                .setParameterMode(HttpRequest.ParameterMode.Json)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
}