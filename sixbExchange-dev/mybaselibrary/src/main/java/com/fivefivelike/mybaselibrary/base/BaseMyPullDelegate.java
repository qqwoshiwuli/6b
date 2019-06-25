package com.fivefivelike.mybaselibrary.base;

import android.support.v7.widget.RecyclerView;

import com.fivefivelike.mybaselibrary.R;


/**
 * Created by 郭青枫 on 2017/9/26.
 */

public abstract class BaseMyPullDelegate extends BasePullDelegate {

    //设置 下拉刷新ui颜色
    public void initRecycleviewPull(RecyclerView.Adapter adapter, RecyclerView.LayoutManager manager, BasePullCallback callback, int headerCount, android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        super.initRecycleviewPull(adapter, manager, callback, headerCount, onRefreshListener);
        setColorSchemeResources(R.color.black);
    }
}