package com.sixbexchange.mvp.delegate;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.sixbexchange.R;
import com.tablayout.CommonTabLayout;

public class UserOrderDelegate extends BaseDelegate {
    public ViewHolder viewHolder;

    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_order;
    }


    public static class ViewHolder {
        public View rootView;

        public CommonTabLayout tl_2;
        public ViewPager vp_root;

        public ViewHolder(View rootView) {
            this.rootView = rootView;

            this.tl_2 = (CommonTabLayout) rootView.findViewById(R.id.tl_2);
            this.vp_root = (ViewPager) rootView.findViewById(R.id.vp_root);
        }

    }
}