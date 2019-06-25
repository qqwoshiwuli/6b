package com.sixbexchange.mvp.databinder;

import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;
import com.sixbexchange.mvp.delegate.RechargeAddressDelegate;
import com.sixbexchange.server.HttpUrl;

import io.reactivex.disposables.Disposable;

public class RechargeAddressBinder extends BaseDataBind<RechargeAddressDelegate> {

    public RechargeAddressBinder(RechargeAddressDelegate viewDelegate) {
        super(viewDelegate);
    }

    public Disposable addrinfo(
            String coin,
            int position,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("coin", coin.toLowerCase());
        return new HttpRequest.Builder()
                .setRequestCode(0x123)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(position == 1 ? HttpUrl.getIntance().depositAddress : HttpUrl.getIntance().addrinfo)
                .setShowDialog(false)
                .setRequestName("充币地址")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }

    public Disposable getAccountDetail(
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        return new HttpRequest.Builder()
                .setRequestCode(0x124)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().getAccountDetail)
                .setShowDialog(false)
                .setRequestName("钱包资金明细")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
}