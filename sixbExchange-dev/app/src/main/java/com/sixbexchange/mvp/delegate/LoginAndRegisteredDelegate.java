package com.sixbexchange.mvp.delegate;

import android.view.View;

import com.circledialog.view.NoSlideViewPager;
import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.sixbexchange.R;
import com.tablayout.CommonTabLayout;

public class LoginAndRegisteredDelegate extends BaseDelegate {
    public ViewHolder viewHolder;

    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_and_registered;
    }


    public static class ViewHolder {
        public View rootView;

        public CommonTabLayout tl_2;
        public NoSlideViewPager vp_root;

        public ViewHolder(View rootView) {
            this.rootView = rootView;

            this.tl_2 = (CommonTabLayout) rootView.findViewById(R.id.tl_2);
            this.vp_root = (NoSlideViewPager) rootView.findViewById(R.id.vp_root);
        }

    }
}