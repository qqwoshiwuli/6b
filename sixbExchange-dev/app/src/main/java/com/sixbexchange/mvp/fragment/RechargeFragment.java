package com.sixbexchange.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.view.InnerPagerAdapter;
import com.sixbexchange.R;
import com.sixbexchange.mvp.databinder.RechargeBinder;
import com.sixbexchange.mvp.delegate.RechargeDelegate;
import com.tablayout.TabEntity;
import com.tablayout.listener.CustomTabEntity;

import java.util.ArrayList;

public class RechargeFragment extends BaseDataBindFragment<RechargeDelegate, RechargeBinder> {

    @Override
    protected Class<RechargeDelegate> getDelegateClass() {
        return RechargeDelegate.class;
    }

    @Override
    public RechargeBinder getDataBinder(RechargeDelegate viewDelegate) {
        return new RechargeBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initToolbar(new ToolbarBuilder().setTitle("充值"));

    }

    public static RechargeFragment newInstance(
            String typeStr,
            String exchName,
            int position,
            int exchPosition
    ) {
        RechargeFragment newFragment = new RechargeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("typeStr", typeStr);
        bundle.putString("exchName", exchName);
        bundle.putInt("position", position);
        bundle.putInt("exchPosition", exchPosition);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    String typeStr = "";
    String exchName = "";
    int position = 0;
    int exchPosition = 0;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("typeStr", typeStr);
        outState.putString("exchName", exchName);
        outState.putInt("position", position);
        outState.putInt("exchPosition", exchPosition);
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (savedInstanceState != null) {
            typeStr = savedInstanceState.getString("typeStr", "");
            exchName = savedInstanceState.getString("exchName", "");
            position = savedInstanceState.getInt("position");
            exchPosition = savedInstanceState.getInt("exchPosition");
        } else {
            typeStr = this.getArguments().getString("typeStr", "");
            exchName = this.getArguments().getString("exchName", "");
            position = this.getArguments().getInt("position");
            exchPosition = this.getArguments().getInt("exchPosition");
        }
        initTablelayout();
    }

    ArrayList fragments;
    InnerPagerAdapter innerPagerAdapter;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    RechargeAddressFragment rechargeAddressFragment;
    private void initTablelayout() {
        fragments = new ArrayList<>();
        fragments.add(rechargeAddressFragment=RechargeAddressFragment.newInstance(typeStr, exchPosition));
        fragments.add(TransferFundsFragment.newInstance(typeStr,exchName, exchPosition));
        if (innerPagerAdapter == null) {
            String[] stringArray = CommonUtils.getStringArray(R.array.sa_select_recharge);
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

        viewDelegate.viewHolder.vp_root.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    rechargeAddressFragment.showDialog();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewDelegate.viewHolder.tl_2.setCurrentTab(position);
        viewDelegate.viewHolder.vp_root.setCurrentItem(position);
        if(position==0){
            rechargeAddressFragment.showDialog();
        }
    }

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
        }
    }

}
