package com.sixbexchange.mvp.databinder;

import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;
import com.sixbexchange.mvp.delegate.WithdrawCoinDelegate;
import com.sixbexchange.server.HttpUrl;

import io.reactivex.disposables.Disposable;

public class WithdrawCoinBinder extends BaseDataBind<WithdrawCoinDelegate> {

    public WithdrawCoinBinder(WithdrawCoinDelegate viewDelegate) {
        super(viewDelegate);
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

    public Disposable extract(
            String coin,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("coin", coin);
        return new HttpRequest.Builder()
                .setRequestCode(0x123)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().extract)
                .setShowDialog(true)
                .setRequestName("提币页面")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
    public Disposable getSingleBalance(
            String srcCoin,
            String exchange,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("srcCoin", srcCoin);
        put("exchange", exchange);
        return new HttpRequest.Builder()
                .setRequestCode(0x128)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().getSingleBalance)
                .setShowDialog(false)
                .setRequestName("提币页面")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
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
                .setRequestCode(0x125)
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

    public Disposable mobile(
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        return new HttpRequest.Builder()
                .setRequestCode(0x126)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().mobile)
                .setShowDialog(true)
                .setRequestName("前登录用户手机号")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }

    public Disposable sendExtract(
            String memo,
            String addr,
            String coin,
            String amount,
            String fee,
            String vCode,
            String exchange,
            String transKey,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("addr", addr);
        put("memo", memo);
        put("coin", coin);
        put("amount", amount);
        put("fee", fee);
        put("vCode", vCode);
        put("exchange", exchange);
        put("transKey", transKey);
        return new HttpRequest.Builder()
                .setRequestCode(0x127)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().sendExtract)
                .setShowDialog(true)
                .setRequestName("发送提币请求")
                .setRequestMode(HttpRequest.RequestMode.POST)
                .setParameterMode(HttpRequest.ParameterMode.Json)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
}