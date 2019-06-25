package com.sixbexchange.mvp.databinder;

import android.util.Log;

import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;
import com.sixbexchange.base.AppConst;
import com.sixbexchange.mvp.delegate.HomeDelegate;
import com.sixbexchange.server.HttpUrl;

import io.reactivex.disposables.Disposable;

public class HomeBinder extends BaseDataBind<HomeDelegate> {

    public HomeBinder(HomeDelegate viewDelegate) {
        super(viewDelegate);
    }

    public Disposable getlatestversion(
            String version,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("type", 1);
        put("version", version);
        return new HttpRequest.Builder()
                .setRequestCode(0x123)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().getlatestversion)
                .setShowDialog(false)
                .setRequestName("版本更新")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }

    public Disposable gethost(
            String version,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("type", 1);
        put("version", version);
        return new HttpRequest.Builder()
                .setRequestCode(0x124)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(AppConst.app2BaseUrl + "/api/app/open/appversion/getlatestversion")
                .setShowDialog(false)
                .setRequestName("获取host")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
    public Disposable getAccount(
            boolean isShow,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        return new HttpRequest.Builder()
                .setRequestCode(0x125)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().getAccount)
                .setShowDialog(isShow)
                .setRequestName("获取钱包列表")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
    public Disposable getAccountDetail(
            boolean isShow,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        return new HttpRequest.Builder()
                .setRequestCode(0x126)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().getAccountDetail)
                .setShowDialog(isShow)
                .setRequestName("钱包资金明细")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
}