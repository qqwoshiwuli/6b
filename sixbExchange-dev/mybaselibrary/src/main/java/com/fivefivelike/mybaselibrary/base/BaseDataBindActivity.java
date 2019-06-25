package com.fivefivelike.mybaselibrary.base;

import android.os.Bundle;
import android.view.View;

import com.fivefivelike.mybaselibrary.http.RequestCallback;
import com.fivefivelike.mybaselibrary.http.ServiceDataCallback;
import com.fivefivelike.mybaselibrary.mvp.databind.IDataBind;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;

import io.reactivex.disposables.Disposable;


/**
 * Created by 郭青枫 on 2017/7/3.
 */

public abstract class BaseDataBindActivity<T extends BaseDelegate, D extends IDataBind> extends BaseActivity<T> implements RequestCallback, ServiceDataCallback, DefaultClickLinsener {
    protected D binder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binder = getDataBinder(viewDelegate);
        super.onCreate(savedInstanceState);
    }

    public abstract D getDataBinder(T viewDelegate);

    @Override
    protected void onDestroy() {
        cancelpost();
        super.onDestroy();
    }

    /**
     * 添加订阅
     *
     * @param disposable
     */
    public void addRequest(Disposable disposable) {
        if (binder != null) {
            binder.addRequest(disposable);
        }
    }

    /**
     * 取消订阅
     */
    private void cancelpost() {
        if (binder != null) {
            binder.cancelpost();
        }
    }

    @Override
    public void success(int requestCode, String data,String errorData) {
        if (binder != null) {
            binder.success(this, this,
                    this, requestCode, data, errorData);
        }
    }

    @Override
    public void onDataSuccess(String data, String info, int status, int requestCode) {
        //接口调用成功
        onStopNet(requestCode, BaseDataBind.StopNetMode.NET_SUCCESS);
        onServiceSuccess(data, info, status, requestCode);
    }

    @Override
    public void onDataError(String data, String info, int status, int requestCode) {
        if (status == BaseDataBind.JsonErrorStatu) {
            //服务端传值错误  或者是 404 405
            onStopNet(requestCode, BaseDataBind.StopNetMode.JSON_ERROR);
        } else if (status == BaseDataBind.viewErrorStatu) {
            //返回数据使用错误
            onStopNet(requestCode, BaseDataBind.StopNetMode.VIEW_ERROR);
        } else {
            //服务端报告错误
            onStopNet(requestCode, BaseDataBind.StopNetMode.NET_ERROR);
        }
        onServiceError(data, info, status, requestCode);
    }

    @Override
    public void error(int requestCode, Throwable exThrowable, String errorData) {
        //接口调用异常
        error(requestCode, exThrowable);
        if (binder != null) {
            binder.showError(exThrowable, null, errorData);
        }
    }

    public void error(int requestCode, Throwable exThrowable) {
        onStopNet(requestCode, BaseDataBind.StopNetMode.ERROR);
    }

    //网络请求结束,不管成功或者失败
    public void onStopNet(int requestCode, BaseDataBind.StopNetMode type) {
        if (viewDelegate != null) {
            viewDelegate.showNodata(requestCode, type);
        }
    }

    protected void onServiceError(String data, String info, int status, int requestCode) {
        if (binder.isMissToken(status)) {
            binder.loginAgain(this);
        }
    }

    protected abstract void onServiceSuccess(String data, String info, int status, int requestCode);

    @Override
    public void onClick(View view, int position, Object item) {
        if (binder != null) {
            binder.onDialogBtnClick(this, view, position, item);
        }
    }

    public D getBinder() {
        return binder;
    }
}

