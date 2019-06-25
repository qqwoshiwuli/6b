package com.fivefivelike.mybaselibrary.base;

import android.support.v7.widget.RecyclerView;
import android.widget.BaseAdapter;

import com.fivefivelike.mybaselibrary.mvp.databind.IDataBind;

import java.util.List;

/**
 * Created by 郭青枫 on 2016/11/12.
 * 上啦下载下拉刷新的父页面
 */

public abstract class BasePullActivity<T extends BasePullDelegate, D extends IDataBind> extends BaseDataBindActivity<T, D>
        implements BasePullCallback, android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener {


    /**
     * 初始化使用RecycleView的上拉页面
     */
    protected void initRecycleViewPull(RecyclerView.Adapter adapter, RecyclerView.LayoutManager layoutManager) {
        viewDelegate.initRecycleviewPull(adapter, layoutManager, this, 0, this);
    }

    protected void initRecycleViewPull(RecyclerView.Adapter adapter, int headerCount, RecyclerView.LayoutManager layoutManager) {
        viewDelegate.initRecycleviewPull(adapter, layoutManager, this, headerCount, this);
    }

    /**
     * 请求数据
     *
     * @param loadMode 类型
     */
    public void requestData(BasePullDelegate.LoadMode loadMode) {
        viewDelegate.requestData(loadMode);
        refreshData();
    }

    /**
     * 请求数据返回更新
     *
     * @param srcList  原集合
     * @param backList 请求返回的集合
     * @param adapter  适配器  可以是BaseAdapter 或者RecyclerView.Adapter
     */
    public void getDataBack(List srcList, List backList, Object adapter) {
        viewDelegate.requestBack(srcList);
        if (backList != null && backList.size() > 0) {
            viewDelegate.hideNoData();
            srcList.addAll(backList);
        } else {
            viewDelegate.loadFinish();
        }
        if (backList != null) {
            if (srcList.size() + backList.size() == 0) {
                viewDelegate.showNoData();
            }
        } else {
            if (srcList.size() == 0) {
                viewDelegate.showNoData();
            }
        }
        if (adapter instanceof BaseAdapter) {
            ((BaseAdapter) adapter).notifyDataSetChanged();
        } else {
            ((RecyclerView.Adapter) adapter).notifyDataSetChanged();
        }
    }

    /**
     * 请求数据
     */
    protected abstract void refreshData();

    @Override
    public void loadData() {
        requestData(BasePullDelegate.LoadMode.DOWN);
    }

    @Override
    public void onRefresh() {
        requestData(BasePullDelegate.LoadMode.REFRESH);
    }

    @Override
    public void error(int requestCode, Throwable exThrowable) {
        super.error(requestCode, exThrowable);
        //上啦加载失败 页码减1
        pageReduction();
    }

    @Override
    protected void onServiceError(String data, String info, int status, int requestCode) {
        super.onServiceError(data, info, status, requestCode);
        pageReduction();
    }

    public void onStopNet(int requestCode, BaseDataBind.StopNetMode type) {
        super.onStopNet(requestCode,type);
        onStopLoading();
    }

    public void pageReduction() {
        viewDelegate.pageReduction();
    }

    public void onStopLoading() {
        viewDelegate.stopRefresh();
    }

}
