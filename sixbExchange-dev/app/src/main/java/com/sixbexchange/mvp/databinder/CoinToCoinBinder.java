package com.sixbexchange.mvp.databinder;

import android.text.TextUtils;

import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;
import com.fivefivelike.mybaselibrary.utils.ToastUtil;
import com.sixbexchange.mvp.delegate.CoinToCoinDelegate;
import com.sixbexchange.server.HttpUrl;

import io.reactivex.disposables.Disposable;

public class CoinToCoinBinder extends BaseDataBind<CoinToCoinDelegate> {

    public CoinToCoinBinder(CoinToCoinDelegate viewDelegate) {
        super(viewDelegate);
    }

    public Disposable orderremarkCoin(RequestCallback requestCallback) {
        getBaseMapWithUid();
        return new HttpRequest.Builder()
                .setRequestCode(0x123)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().coinpop)
                .setShowDialog(false)
                .setRequestName("获取闪兑交易对列表")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }

    public Disposable ourdetailCoin(RequestCallback requestCallback) {
        getBaseMapWithUid();
        return new HttpRequest.Builder()
                .setRequestCode(0x124)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().coindetail)
                .setShowDialog(false)
                .setRequestName("获取6b资产明细")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.Json)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }

    public Disposable coinhistory(
            String symbol,
            String pageNum,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("symbol", symbol);
        put("pageNum", pageNum);
        return new HttpRequest.Builder()
                .setRequestCode(0x125)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().coinhistory)
                .setShowDialog(false)
                .setRequestName("交易历史")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }

    public void checkOrderCoin(int type, RequestCallback requestCallback) {
        if (TextUtils.isEmpty(viewDelegate.viewHolder.et_num.getText().toString())) {
            ToastUtil.show("请输入价格");
            return;
        }

        addRequest(ordercoin(
                viewDelegate.viewHolder.et_num.getText().toString(),
                "",
                "",
                type + "",
                requestCallback
        ));
    }

    /**
     * amount:数量
     *
     * precision:精度
     *
     * symbol:交易对 (baseCurrency + "_" + quoteCurrency 例：btc_usd)
     *
     * type:1 买 ，2卖
     */
    public Disposable ordercoin(
            String amount,
            String precision,
            String symbol,
            String type,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("amount", amount);
        put("precision", precision);
        put("symbol", symbol);
        put("type", type);
        return new HttpRequest.Builder()
                .setRequestCode(0x126)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().coinorder)
                .setShowDialog(true)
                .setRequestName("下单相关")
                .setRequestMode(HttpRequest.RequestMode.POST)
                .setParameterMode(HttpRequest.ParameterMode.Json)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
}