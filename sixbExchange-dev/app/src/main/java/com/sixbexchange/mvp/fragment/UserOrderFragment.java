package com.sixbexchange.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.view.InnerPagerAdapter;
import com.sixbexchange.R;
import com.sixbexchange.mvp.databinder.UserOrderBinder;
import com.sixbexchange.mvp.delegate.UserOrderDelegate;
import com.tablayout.TabEntity;
import com.tablayout.listener.CustomTabEntity;

import java.util.ArrayList;

/*
*跟单主页
* @author gqf
* @Description
* @Date  2019/4/3 0003 11:18
* @Param
* @return
**/

public class UserOrderFragment extends BaseDataBindFragment<UserOrderDelegate, UserOrderBinder> {

    @Override
    protected Class<UserOrderDelegate> getDelegateClass() {
        return UserOrderDelegate.class;
    }

    @Override
    public UserOrderBinder getDataBinder(UserOrderDelegate viewDelegate) {
        return new UserOrderBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initToolbar(new ToolbarBuilder().setTitle("").setShowBack(false));
        viewDelegate.getmToolbarTitle().setVisibility(View.GONE);
    }

    ArrayList fragments;
    InnerPagerAdapter innerPagerAdapter;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initTablelayout();
    }


    private void initTablelayout() {
        fragments = new ArrayList<>();
        fragments.add(new FollowPeopleFragment());
        fragments.add(new MyFollowFragment());
        if (innerPagerAdapter == null) {
            String[] stringArray = CommonUtils.getStringArray(R.array.sa_select_user_oredr);
            mTabEntities = new ArrayList<>();
            for (int i = 0; i < stringArray.length; i++) {
                mTabEntities.add(new TabEntity(stringArray[i], 0, 0));
            }
            viewDelegate.viewHolder.tl_2.setTabData(mTabEntities);
            viewDelegate.viewHolder.tl_2.setTextSelectColor(CommonUtils.getColor(R.color.mark_color));
            viewDelegate.viewHolder.tl_2.setTextUnselectColor(CommonUtils.getColor(R.color.color_font2));
            viewDelegate.viewHolder.tl_2.setIndicatorColor(CommonUtils.getColor(R.color.mark_color));
            viewDelegate.viewHolder.vp_root.setOffscreenPageLimit(3);
            innerPagerAdapter = new InnerPagerAdapter(getChildFragmentManager(), fragments, stringArray);
            viewDelegate.viewHolder.tl_2.setViewPager(innerPagerAdapter, viewDelegate.viewHolder.vp_root);
        } else {
            innerPagerAdapter.setDatas(fragments);
        }
        viewDelegate.viewHolder.tl_2.setCurrentTab(0);
        viewDelegate.viewHolder.vp_root.setCurrentItem(0);
    }

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
        }
    }

}
