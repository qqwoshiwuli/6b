package com.sixbexchange.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.fivefivelike.mybaselibrary.http.WebSocket2Request;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.sixbexchange.entity.bean.kline.DataParse;
import com.sixbexchange.entity.bean.kline.KLineBean;
import com.sixbexchange.mvp.databinder.CoinInfoBinder;
import com.sixbexchange.mvp.delegate.CoinInfoDelegate;
import com.sixbexchange.utils.DateUtils;
import com.sixbexchange.utils.UserSet;
import com.sixbexchange.widget.chart.BlzKlineDraw;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CoinInfoFragment extends BaseDataBindFragment<CoinInfoDelegate, CoinInfoBinder> {

    @Override
    protected Class<CoinInfoDelegate> getDelegateClass() {
        return CoinInfoDelegate.class;
    }

    @Override
    public CoinInfoBinder getDataBinder(CoinInfoDelegate viewDelegate) {
        return new CoinInfoBinder(viewDelegate);
    }

    public static CoinInfoFragment newInstance(
            String typeStr,
            String name) {
        CoinInfoFragment newFragment = new CoinInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("typeStr", typeStr);
        bundle.putString("name", name);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    String typeStr = "";
    String name = "";

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("typeStr", typeStr);
        outState.putString("name", name);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (savedInstanceState != null) {
            typeStr = savedInstanceState.getString("typeStr", "");
            name = savedInstanceState.getString("name", "");
        } else {
            typeStr = this.getArguments().getString("typeStr", "");
            name = this.getArguments().getString("name", "");
        }
        getIntentData();
    }


    String klineValue = "";
    BlzKlineDraw klineDraw;
    private DataParse mData;

    public void getIntentData() {
        klineValue = UserSet.getinstance().getKTime();
        initWs();
        initToolbar(new ToolbarBuilder()
                .setTitle(""));
        viewDelegate.getmToolbarTitle().setVisibility(View.GONE);
        viewDelegate.viewHolder.tv_title.setText(
                Html.fromHtml(
                        name
                ));
        viewDelegate.setDefaultClickLinsener(new DefaultClickLinsener() {
            @Override
            public void onClick(View view, int position, Object item) {
                //k线选择时间
                //时间 改变
                klineValue = (String) item;
                if (klineDraw != null && mData != null) {
                    UserSet.getinstance().setKTime(klineValue);
                    changeKline();
                }
            }
        });
        mData = new DataParse();
        klineDraw = new BlzKlineDraw();
        klineDraw.initView(getActivity(),
                viewDelegate.viewHolder.combinedchart, viewDelegate.viewHolder.barchart);
        viewDelegate.setShowLoading(true);
        klineDraw.isFullScreen(false);

        addRequest(binder.getKlineByOnlyKey(
                typeStr,
                klineValue,
                System.currentTimeMillis() / 1000,
                0x123,
                this
        ));

    }

    private void changeKline() {
        //请求新数据
        binder.cancelpost();
        //清空数据
        if (klineDraw != null) {
            klineDraw.cleanData();
        }
        sendWs(false);
        addRequest(binder.getKlineByOnlyKey(
                typeStr,
                klineValue,
                System.currentTimeMillis() / 1000,
                0x123,
                this
        ));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (klineDraw != null) {
            klineDraw.cleanData();
        }
    }

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                //初始化k线
                List<KLineBean> newkLineBeans =
                        GsonUtil.getInstance().toList(data, KLineBean.class);
                viewDelegate.setShowLoading(false);
                //数据 整理
                mData.parseKLine(newkLineBeans, klineValue);
                //绘制
                getOffLineData();
                //订阅ws
                sendWs(true);
                break;
            case 0x129:
                //k线翻页
                List<KLineBean> lineBeans = GsonUtil.getInstance().toList(data, KLineBean.class);
                break;

        }
    }


    private void getOffLineData() {
        viewDelegate.viewHolder.combinedchart.setOnMaxLeftLinsener(null);
        klineDraw.setData(mData);
        //klineDraw.setKlineShowType(viewDelegate.getSelectKline());
        klineDraw.setOnClick(new BlzKlineDraw.OnClick() {
            @Override
            public void click(int xPosition, int startPosition, int endPosition) {
                viewDelegate.initKline(xPosition, klineDraw.getmData());
            }
        });
        viewDelegate.initKline(klineDraw.getmData().getKLineDatas().size() - 1, klineDraw.getmData());
        viewDelegate.setGetRateLinsener(new DefaultClickLinsener() {
            @Override
            public void onClick(View view, int position, Object item) {
                changeKline();
            }
        });
    }


    String TAG = getClass().getSimpleName();

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        sendWs(true);
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        sendWs(false);
    }

    Disposable subscribe;
    boolean isSubscribe;

    //订阅深度和指数
    private void sendWs(boolean is) {
        this.isSubscribe = is;
        if (subscribe != null) {
            subscribe.dispose();
        }
        subscribe = Observable.interval(1000, 1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onTerminateDetach()
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (WebSocket2Request.getInstance().getCallBack(TAG) != null) {
                            Map<String, String> map = new HashMap<>();
                            map.put("uri", (isSubscribe ? "subscribe" : "unsubscribe") + "-single-candle");
                            map.put("contract", typeStr);
                            map.put("duration", klineValue);
                            WebSocket2Request.getInstance().sendData(GsonUtil.getInstance().toJson(map));
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (subscribe != null) {
            subscribe.dispose();
        }
    }


    long updataTime;
    String duration;

    private void initWs() {
        updataTime = System.currentTimeMillis();
        WebSocket2Request.getInstance().addCallBack(TAG, new WebSocket2Request.WebSocketCallBack() {
            @Override
            public void onDataSuccess(String name, String data, String info, int status) {
                if (ObjectUtils.equals(TAG, name) &&
                        updataTime + 500 < System.currentTimeMillis() &&
                        !WebSocket2Request.getInstance().isSend()) {
                    updataTime = System.currentTimeMillis();
                    KLineBean val =
                            GsonUtil.getInstance().toObj(data, KLineBean.class);
                    if (val != null &&
                            ObjectUtils.equals(val.getKey(), typeStr) &&
                            ObjectUtils.equals(klineValue, duration) &&
                            ObjectUtils.equals(klineValue, val.getDuration()) &&
                            val.getClose() != null) {
                        String timeString = GsonUtil.getInstance().getValue(data, "time", String.class);
                        if (!TextUtils.isEmpty(timeString)) {
                            Date time = DateUtils.getTime(timeString, DateUtils.TIME_STYLE_S12);
                            if (time != null) {
                                val.setTimestamp(TimeUtils.date2Millis(time) / 1000 +
                                        8 * 60 * 60);
                                klineDraw.updata(val, klineValue);
                            }
                        }
                    } else {
                        String timeString = GsonUtil.getInstance().getValue(data, "duration", String.class);
                        if (!TextUtils.isEmpty(timeString)) {
                            duration = timeString;
                        }
                    }
                }
            }

            @Override
            public void onDataError(String name, String data, String info, int status) {

            }
        });
    }

    @Override
    public void onStopNet(int requestCode, BaseDataBind.StopNetMode type) {
        if (requestCode == 0x129) {
            viewDelegate.setShowLoading(false);
        }
        super.onStopNet(requestCode, type);
    }

}
