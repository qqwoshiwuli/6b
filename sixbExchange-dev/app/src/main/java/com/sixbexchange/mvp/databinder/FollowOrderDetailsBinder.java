package com.sixbexchange.mvp.databinder;

import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;
import com.sixbexchange.mvp.delegate.FollowOrderDetailsDelegate;
import com.sixbexchange.server.HttpUrl;

import io.reactivex.disposables.Disposable;

public class FollowOrderDetailsBinder extends BaseDataBind<FollowOrderDetailsDelegate> {

    public FollowOrderDetailsBinder(FollowOrderDetailsDelegate viewDelegate) {
        super(viewDelegate);
    }
    public Disposable followdetail(
            String id,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("id",id);
        return new HttpRequest.Builder()
                .setRequestCode(0x123)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().followdetail)
                .setShowDialog(true)
                .setRequestName("跟单详情")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }

}