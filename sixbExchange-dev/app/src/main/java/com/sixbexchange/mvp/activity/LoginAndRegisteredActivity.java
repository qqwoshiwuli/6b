package com.sixbexchange.mvp.activity;

import android.support.v4.app.Fragment;

import com.blankj.utilcode.util.AppUtils;
import com.fivefivelike.mybaselibrary.base.BaseDataBindActivity;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.view.InnerPagerAdapter;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.UserLoginInfo;
import com.sixbexchange.mvp.databinder.LoginAndRegisteredBinder;
import com.sixbexchange.mvp.delegate.LoginAndRegisteredDelegate;
import com.sixbexchange.mvp.fragment.LoginFragment;
import com.sixbexchange.mvp.fragment.RegisteredFragment;
import com.tablayout.TabEntity;
import com.tablayout.listener.CustomTabEntity;

import java.util.ArrayList;

public class LoginAndRegisteredActivity extends BaseDataBindActivity<LoginAndRegisteredDelegate, LoginAndRegisteredBinder> {

    @Override
    protected Class<LoginAndRegisteredDelegate> getDelegateClass() {
        return LoginAndRegisteredDelegate.class;
    }

    @Override
    public LoginAndRegisteredBinder getDataBinder(LoginAndRegisteredDelegate viewDelegate) {
        return new LoginAndRegisteredBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initToolbar(new ToolbarBuilder().setTitle("注册或登录"));
        UserLoginInfo.logout();
        initTablelayout();
    }

    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
        if (!UserLoginInfo.isLoginNoToast()) {
            AppUtils.exitApp();
        }
    }

    ArrayList<Fragment> fragments;
    InnerPagerAdapter innerPagerAdapter;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private void initTablelayout() {
        fragments = new ArrayList<>();
        fragments.add(new LoginFragment());
        fragments.add(new RegisteredFragment());
        if (innerPagerAdapter == null) {
            String[] stringArray = CommonUtils.getStringArray(R.array.sa_select_login_title);
            mTabEntities = new ArrayList<>();
            for (int i = 0; i < stringArray.length; i++) {
                mTabEntities.add(new TabEntity(stringArray[i], 0, 0));
            }
            viewDelegate.viewHolder.tl_2.setTabData(mTabEntities);
            viewDelegate.viewHolder.tl_2.setTextSelectColor(CommonUtils.getColor(R.color.mark_color));
            viewDelegate.viewHolder.tl_2.setTextUnselectColor(CommonUtils.getColor(R.color.color_font2));
            viewDelegate.viewHolder.tl_2.setIndicatorColor(CommonUtils.getColor(R.color.mark_color));
            viewDelegate.viewHolder.vp_root.setOffscreenPageLimit(3);
            innerPagerAdapter = new InnerPagerAdapter(getSupportFragmentManager(), fragments, stringArray);
            viewDelegate.viewHolder.tl_2.setViewPager(innerPagerAdapter, viewDelegate.viewHolder.vp_root);
        } else {
            innerPagerAdapter.setDatas(fragments);
        }
        viewDelegate.viewHolder.tl_2.setCurrentTab(0);
        viewDelegate.viewHolder.vp_root.setCurrentItem(0);
    }

    public void setCurrentView(int position) {
        viewDelegate.viewHolder.tl_2.setCurrentTab(position);
        viewDelegate.viewHolder.vp_root.setCurrentItem(position);
    }

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
        }
    }

}
