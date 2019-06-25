package com.sixbexchange.mvp.delegate;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.fivefivelike.mybaselibrary.base.BaseMyPullDelegate;
import com.fivefivelike.mybaselibrary.view.SwipeRefreshLayout;
import com.sixbexchange.R;

import skin.support.widget.SkinCompatLinearLayout;

public class BaseActivityhuobiPullDelegate extends BaseMyPullDelegate {
    public BaseActivityhuobiPullDelegate.ViewHolder viewHolder;


    @Override
    public void initView() {
        viewHolder = new BaseActivityhuobiPullDelegate.ViewHolder(getRootView());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base_huobipull;
    }


    public static class ViewHolder {
        public View rootView;
        public ListView bizhong;
        public ListView transactionbizhong;


        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.bizhong = (ListView) rootView.findViewById(R.id.bizhong);
            this.transactionbizhong = (ListView) rootView.findViewById(R.id.transactionbizhong);

        }

    }
}
