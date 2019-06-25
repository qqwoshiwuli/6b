package com.sixbexchange.mvp.databinder;

import com.blankj.utilcode.util.ObjectUtils;
import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;
import com.sixbexchange.mvp.delegate.CoinInfoDelegate;
import com.sixbexchange.server.HttpUrl;
import com.yanzhenjie.nohttp.rest.CacheMode;

import io.reactivex.disposables.Disposable;

public class CoinInfoBinder extends BaseDataBind<CoinInfoDelegate> {

    public CoinInfoBinder(CoinInfoDelegate viewDelegate) {
        super(viewDelegate);
    }

    public Disposable getKlineByOnlyKey(
            String contract,
            String duration,
            long nowTime,
            int code,
            RequestCallback requestCallback) {
        getBaseMapWithUid();

        baseMap.put("contract", contract);

        long time = 5 * 60;
        if (ObjectUtils.equals(duration, "1m")) {
            time = time / 5;
        } else if (ObjectUtils.equals(duration, "1h")) {
            time = time * 12;
        } else if (ObjectUtils.equals(duration, "1d")) {
            time = time * 12 * 24;
        }

        baseMap.put("since", (nowTime - time * 300));
        baseMap.put("duration", duration);
        viewDelegate.setShowLoading(true);

        return new HttpRequest.Builder()
                .setRequestCode(code)
                .setRequestUrl(HttpUrl.getIntance().candles)
                .setCacheMode(CacheMode.ONLY_REQUEST_NETWORK)
                .setShowDialog(false)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestName("获取K线")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }


}