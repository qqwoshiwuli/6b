package com.fivefivelike.mybaselibrary.mvp.databind;

import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.fivefivelike.mybaselibrary.http.ServiceDataCallback;
import com.fivefivelike.mybaselibrary.mvp.view.IDelegate;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;

import io.reactivex.disposables.Disposable;

/**
 * Created by 郭青枫 on 2017/7/7.
 */

public interface IDataBind<T extends IDelegate> {

    //添加网络请求
    void addRequest(Disposable disposable);

    //网络请求取消绑定
    void cancelpost();

    //判断token丢失
    boolean isMissToken(int status);

    //重新登录
    void loginAgain(FragmentActivity activity);

    //展示报错
    void showError(Throwable exThrowable,String returnJson,String errorData);

    //网络 返回弹窗 点击事件
    void onDialogBtnClick(FragmentActivity activity, View view, int position, Object item);

    //网络请求成功
    void success(FragmentActivity activity,
                 ServiceDataCallback serviceDataCallback,
                 DefaultClickLinsener defaultClickLinsener,
                 int requestCode, String data, String errorData);


}
