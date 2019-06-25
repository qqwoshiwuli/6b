package com.sixbexchange.mvp.delegate;

import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.circledialog.view.NoSlideViewPager;
import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.sixbexchange.R;
import com.tablayout.SegmentTabLayout;

public class TrParentsDelegate extends BaseDelegate {
    public ViewHolder viewHolder;

    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tr_parents;
    }


    public static class ViewHolder {
        public View rootView;

        public SegmentTabLayout tl_1;
        public ImageView iv_description;
        public NoSlideViewPager vp_root;
        public FrameLayout fl_right;
        public DrawerLayout main_drawer_layout;

        public ViewHolder(View rootView) {
            this.rootView = rootView;

            this.tl_1 = (SegmentTabLayout) rootView.findViewById(R.id.tl_1);
            this.iv_description = (ImageView) rootView.findViewById(R.id.iv_description);
            this.vp_root = (NoSlideViewPager) rootView.findViewById(R.id.vp_root);
            this.fl_right = (FrameLayout) rootView.findViewById(R.id.fl_right);
            this.main_drawer_layout = (DrawerLayout) rootView.findViewById(R.id.main_drawer_layout);
        }

    }
}