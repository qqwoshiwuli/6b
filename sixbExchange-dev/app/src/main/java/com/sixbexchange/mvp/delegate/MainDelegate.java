package com.sixbexchange.mvp.delegate;

import android.view.View;
import android.widget.FrameLayout;

import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.fivefivelike.mybaselibrary.view.BottomBar;
import com.sixbexchange.R;
import com.tablayout.listener.CustomTabEntity;

import java.util.ArrayList;

public class MainDelegate extends BaseDelegate {
    public ViewHolder viewHolder;
    private ArrayList<CustomTabEntity> mTabEntities;

    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }


    public static class ViewHolder {
        public View rootView;
        public FrameLayout fl_tab_container;
        public BottomBar bottomBar;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.fl_tab_container = (FrameLayout) rootView.findViewById(R.id.fl_tab_container);
            this.bottomBar = (BottomBar) rootView.findViewById(R.id.bottomBar);
        }

    }
}