package com.sixbexchange.mvp.databinder;

import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.ObjectUtils;
import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;
import com.fivefivelike.mybaselibrary.utils.ListUtils;
import com.fivefivelike.mybaselibrary.utils.ToastUtil;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.sixbexchange.mvp.delegate.BMTrFragmentDelegate;
import com.sixbexchange.mvp.delegate.OkexTrDelegate;
import com.sixbexchange.mvp.delegate.OneTokenDelegate;
import com.sixbexchange.server.HttpUrl;
import com.sixbexchange.utils.BigUIUtil;
import com.sixbexchange.utils.UserSet;

import io.reactivex.disposables.Disposable;

public class OneTokenBinder extends BaseDataBind<OneTokenDelegate> {

    public OneTokenBinder(OneTokenDelegate viewDelegate) {
        super(viewDelegate);
    }
    public Disposable ourdetailCoin(RequestCallback requestCallback) {
        getBaseMapWithUid();
        return new HttpRequest.Builder()
                .setRequestCode(0x123)
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
    public Disposable buy_order(
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
                .setRequestCode(0x124)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().spotorder)
                .setShowDialog(true)
                .setRequestName("闪兑下单")
                .setRequestMode(HttpRequest.RequestMode.POST)
                .setParameterMode(HttpRequest.ParameterMode.Json)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
    public Disposable orderremark(
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("exchange", "okef");
        return new HttpRequest.Builder()
                .setRequestCode(0x128)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().orderremark)
                .setShowDialog(false)
                .setRequestName("计划委托弹窗")
                .setRequestMode(HttpRequest.RequestMode.GET)
                .setParameterMode(HttpRequest.ParameterMode.KeyValue)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }

//    public Disposable accountopen(
//            String exchange,
//            String contract,
//            RequestCallback requestCallback) {
//        getBaseMapWithUid();
//        put("exchange", exchange);
//        put("contract", contract);
//        return new HttpRequest.Builder()
//                .setRequestCode(0x124)
//                .setDialog(viewDelegate.getNetConnectDialog())
//                .setRequestUrl(HttpUrl.getIntance().accountopen)
//                .setShowDialog(false)
//                .setRequestName("获取开仓可用")
//                .setRequestMode(HttpRequest.RequestMode.POST)
//                .setParameterMode(HttpRequest.ParameterMode.Json)
//                .setRequestObj(baseMap)
//                .setRequestCallback(requestCallback)
//                .build()
//                .RxSendRequest();
//    }

    public Disposable accountclose(
            String exchange,
            String contract,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("exchange", exchange);
        put("contract", contract);
        return new HttpRequest.Builder()
                .setRequestCode(0x127)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().accountclose)
                .setShowDialog(false)
                .setRequestName("获取平单可用")
                .setRequestMode(HttpRequest.RequestMode.POST)
                .setParameterMode(HttpRequest.ParameterMode.Json)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }

    private String getDepthPrice(boolean isAsks) {
        if (!ListUtils.isEmpty(viewDelegate.asksAdapter.getDatas()) && !ListUtils.isEmpty(viewDelegate.bidsAdapter.getDatas())) {
            return isAsks ?
                    viewDelegate.asksAdapter.getDatas().get(viewDelegate.depthSize - 1).getPrice() :
                    viewDelegate.bidsAdapter.getDatas().get(0).getPrice();
        }
        return "";
    }


    public Disposable placeOrder(
            String exchange,
            String matchPrice,
            String price,
            int type,
            String currencyPair,
            String contract,
            String amount,
            String bs,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("exchange", exchange);
        put("matchPrice", matchPrice);
        put("price", price);
        put("type", type);
        put("currencyPair", currencyPair);
        put("contract", contract);
        put("bs", bs);
        put("amount", amount);
        return new HttpRequest.Builder()
                .setRequestCode(0x125)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().placeOrder)
                .setShowDialog(true)
                .setRequestName("下单")
                .setRequestMode(HttpRequest.RequestMode.POST)
                .setParameterMode(HttpRequest.ParameterMode.Json)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }

    public Disposable cancelOrder(
            String exchange,
            String exchangeOid,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("exchange", exchange);
        put("exchangeOid", exchangeOid);
        return new HttpRequest.Builder()
                .setRequestCode(0x126)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().cancelOrder)
                .setShowDialog(true)
                .setRequestName("撤单")
                .setRequestMode(HttpRequest.RequestMode.POST)
                .setParameterMode(HttpRequest.ParameterMode.Json)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }

    public Disposable cancelAllOrder(
            String exchange,
            String contract,
            RequestCallback requestCallback) {
        getBaseMapWithUid();
        put("exchange", exchange);
        put("contract", contract);
        return new HttpRequest.Builder()
                .setRequestCode(0x126)
                .setDialog(viewDelegate.getNetConnectDialog())
                .setRequestUrl(HttpUrl.getIntance().cancelAllOrder)
                .setShowDialog(true)
                .setRequestName("撤单")
                .setRequestMode(HttpRequest.RequestMode.POST)
                .setParameterMode(HttpRequest.ParameterMode.Json)
                .setRequestObj(baseMap)
                .setRequestCallback(requestCallback)
                .build()
                .RxSendRequest();
    }
}