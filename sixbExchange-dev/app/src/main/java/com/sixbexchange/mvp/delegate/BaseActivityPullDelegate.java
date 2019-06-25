package com.sixbexchange.mvp.delegate;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.fivefivelike.mybaselibrary.base.BaseMyPullDelegate;
import com.fivefivelike.mybaselibrary.view.SwipeRefreshLayout;
import com.sixbexchange.R;

import skin.support.widget.SkinCompatLinearLayout;


/**
 * Created by 郭青枫 on 2017/9/27.
 * 同一的 activity列表页面 代理
 */

public class BaseActivityPullDelegate extends BaseMyPullDelegate {
    public ViewHolder viewHolder;


    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base_pull;
    }


    public static class ViewHolder {
        public View rootView;
        public RecyclerView pull_recycleview;
        public FrameLayout fl_rcv;
        public SwipeRefreshLayout swipeRefreshLayout;
        public SkinCompatLinearLayout fl_pull;
        public LinearLayout lin_pull;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.pull_recycleview = (RecyclerView) rootView.findViewById(R.id.pull_recycleview);
            this.fl_rcv = (FrameLayout) rootView.findViewById(R.id.fl_rcv);
            this.swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
            this.fl_pull = (SkinCompatLinearLayout) rootView.findViewById(R.id.fl_pull);
            this.lin_pull = (LinearLayout) rootView.findViewById(R.id.lin_pull);
        }

    }
}
