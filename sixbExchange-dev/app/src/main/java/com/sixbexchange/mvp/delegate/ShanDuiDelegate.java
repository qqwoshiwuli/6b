package com.sixbexchange.mvp.delegate;

import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.circledialog.view.NoSlideViewPager;
import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.sixbexchange.R;
import com.tablayout.SegmentTabLayout;

public class ShanDuiDelegate extends BaseDelegate {
    public TrParentsDelegate.ViewHolder viewHolder;

    @Override
    public void initView() {
        viewHolder = new TrParentsDelegate.ViewHolder(getRootView());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shandui;
    }


    public static class ViewHolder {
        public View rootView;


        public FrameLayout fl_right;

        public ViewHolder(View rootView) {
            this.rootView = rootView;

            this.fl_right = (FrameLayout) rootView.findViewById(R.id.fl_right);
        }

    }
}