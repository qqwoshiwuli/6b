package com.fivefivelike.mybaselibrary.http;

import android.os.Message;
import android.support.v7.widget.RecyclerView;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 郭青枫 on 2018/2/1 0001.
 */

public class HandlerHelper {

    String nowWhat;
    ConcurrentHashMap<String, Object> nowExchangeDataMap;
    RecyclerView nowRecyclerView;
    Message message;
    int delayMillis = 1000;

    public void setDelayMillis(int delayMillis) {
        this.delayMillis = delayMillis;
    }

    private static class helper {
        private static HandlerHelper helper = new HandlerHelper();
    }

    public static HandlerHelper getinstance() {
        return HandlerHelper.helper.helper;
    }

    private HandlerHelper() {
    }


    public interface OnUpdataLinsener {
        void onUpdataLinsener(Object val);
    }

    OnUpdataLinsener mOnUpdataLinsener;
    Disposable disposable;
    public Disposable initHander(String what,
                           RecyclerView recyclerView, OnUpdataLinsener onUpdataLinsener) {
        nowWhat = what;
        nowRecyclerView = recyclerView;
        mOnUpdataLinsener = onUpdataLinsener;
        message = new Message();
        message.what = 0;
        message.obj = nowWhat;
        if (nowExchangeDataMap == null) {
            nowExchangeDataMap = new ConcurrentHashMap<>();
        } else {
            nowExchangeDataMap.clear();
        }
        if (disposable != null) {
            disposable.dispose();
        }
        disposable = Observable.interval(delayMillis, delayMillis, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onTerminateDetach()
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (nowExchangeDataMap == null) {
                            return;
                        }
                        if (nowRecyclerView == null) {
                            return;
                        }
                        message = new Message();
                        message.what = 0;
                        message.obj = nowWhat;
                        if (nowRecyclerView.getScrollState() != 0) {
                            //recycleView正在滑动
                            if (nowExchangeDataMap.size() > nowRecyclerView.getChildCount() / 4) {
                                nowExchangeDataMap.clear();
                            }
                        } else {
                            //更新数据
                            Iterator iter = nowExchangeDataMap.entrySet().iterator();
                            while (iter.hasNext()) {
                                if (nowRecyclerView.getScrollState() != 0) {
                                    return;
                                }
                                Map.Entry entry = (Map.Entry) iter.next();
                                Object val = entry.getValue();
                                String key = (String) entry.getKey();
                                if (val != null && mOnUpdataLinsener != null && nowExchangeDataMap != null) {
                                    mOnUpdataLinsener.onUpdataLinsener(val);
                                    nowExchangeDataMap.remove(key);
                                } else {
                                }
                            }
                        }
                    }
                });
        return disposable;
    }

    public void put(String onlyKey, Object exchangeData) {
        if (nowExchangeDataMap != null) {
            nowExchangeDataMap.put(onlyKey, exchangeData);
        }
    }

    public void clear() {
        if (nowExchangeDataMap != null) {
            nowExchangeDataMap.clear();
        }
    }

    public void onDestory() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

}
