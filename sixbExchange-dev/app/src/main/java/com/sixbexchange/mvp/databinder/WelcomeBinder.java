package com.sixbexchange.mvp.databinder;

import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;
import com.sixbexchange.mvp.delegate.WebActivityDelegate;
import com.sixbexchange.mvp.delegate.WelcomeDelegate;
import com.sixbexchange.server.HttpUrl;

import io.reactivex.disposables.Disposable;

public class WelcomeBinder extends BaseDataBind<WelcomeDelegate> {

    public WelcomeBinder(WelcomeDelegate viewDelegate) {
        super(viewDelegate);
    }

    public Disposable getopenchart(
            String imgType,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("imgType",imgType);
        return new HttpRequest.Builder()
                .setRequestCode(0x124)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl("https://www.6b.top/api/app/open/chart")
                .setShowDialog(false)
                .setRequestName("轮播")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
}