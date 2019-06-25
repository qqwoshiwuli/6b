package com.sixbexchange.mvp.delegate;

import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.FrameLayout;

import com.circledialog.view.NoSlideViewPager;
import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.sixbexchange.R;
import com.tablayout.CommonTabLayout;

public class ExchTrParentsDelegate extends BaseDelegate {
    public ViewHolder viewHolder;

    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_exch_tr_parents;
    }


    public static class ViewHolder {
        public View rootView;
        public CommonTabLayout tl_2;
        public NoSlideViewPager vp_root;
        public FrameLayout fl_left;
        public DrawerLayout main_drawer_layout;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tl_2 = (CommonTabLayout) rootView.findViewById(R.id.tl_2);
            this.vp_root = (NoSlideViewPager) rootView.findViewById(R.id.vp_root);
            this.fl_left = (FrameLayout) rootView.findViewById(R.id.fl_left);
            this.main_drawer_layout = (DrawerLayout) rootView.findViewById(R.id.main_drawer_layout);
        }

    }
}