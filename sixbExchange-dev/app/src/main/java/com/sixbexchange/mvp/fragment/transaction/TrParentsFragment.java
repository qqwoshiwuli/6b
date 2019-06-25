package com.sixbexchange.mvp.fragment.transaction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;

import com.blankj.utilcode.util.ObjectUtils;
import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.utils.glide.GlideUtils;
import com.fivefivelike.mybaselibrary.view.InnerPagerAdapter;
import com.sixbexchange.R;
import com.sixbexchange.mvp.activity.WebActivityActivity;
import com.sixbexchange.mvp.databinder.TrParentsBinder;
import com.sixbexchange.mvp.delegate.TrParentsDelegate;
import com.sixbexchange.mvp.dialog.OpenBitmexDialog;
import com.sixbexchange.mvp.fragment.transaction.bitmex.BMTrParentsFragment;
import com.sixbexchange.mvp.fragment.transaction.okex.OkexTrParentsFragment;
import com.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

public class TrParentsFragment extends BaseDataBindFragment<TrParentsDelegate, TrParentsBinder> {
    private String[] mTitles = {" OKEX合约 ", "BitMEX合约"};
    private ArrayList<Fragment> mFragments;

    @Override
    protected Class<TrParentsDelegate> getDelegateClass() {
        return TrParentsDelegate.class;
    }

    @Override
    public TrParentsBinder getDataBinder(TrParentsDelegate viewDelegate) {
        return new TrParentsBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initToolbar(new ToolbarBuilder().setTitle("")
                .setmRightImg1(CommonUtils.getString(R.string.ic_seting))
                .setShowBack(false));
        viewDelegate.getmToolbarTitle().setVisibility(View.GONE);
        addRequest(binder.bitmexbindStatus(this));
    }

    OkexTrParentsFragment okexTrParentsFragment;

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mFragments = new ArrayList<>();
        mFragments.add(okexTrParentsFragment = new OkexTrParentsFragment());
        mFragments.add(new BMTrParentsFragment());
        viewDelegate.viewHolder.tl_1.setTabData(mTitles);

        InnerPagerAdapter innerPagerAdapter = new InnerPagerAdapter(
                getChildFragmentManager(), mFragments, mTitles);
        viewDelegate.viewHolder.vp_root.setAdapter(innerPagerAdapter);
        viewDelegate.viewHolder.tl_1.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 1 && ObjectUtils.equals(statu, "0")) {
                    viewDelegate.viewHolder.tl_1.setCurrentTab(0);
                    OpenBitmexDialog openBitmexDialog = new OpenBitmexDialog(getActivity());
                    openBitmexDialog.showDialog(new DefaultClickLinsener() {
                        @Override
                        public void onClick(View view, int position, Object item) {
                            addRequest(binder.bitmexbind(TrParentsFragment.this));
                        }
                    });
                } else {
                    viewDelegate.viewHolder.vp_root.setCurrentItem(position);
                }
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        loadDrawerFragment();

        GlideUtils.loadImage("https://mapp.bicoin.info/wang/other/6b/help%403x.png",
                viewDelegate.viewHolder.iv_description,GlideUtils.getNoCacheRO());
        viewDelegate.viewHolder.iv_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewDelegate.viewHolder.tl_1.getCurrentTab()==0){
                    WebActivityActivity.startAct(getActivity(),"https://mapp.bicoin.info/wang/other/6b/help.html");
                }else {
                    WebActivityActivity.startAct(getActivity(),"https://bicoin.oss-cn-beijing.aliyuncs.com/wang/other/6b/help-bm.html");
                }
            }
        });
    }

    @Override
    protected void clickRightIv() {
        super.clickRightIv();
        viewDelegate.viewHolder.main_drawer_layout.openDrawer(Gravity.RIGHT);
        exchangeSetFragment.setExchPosition(viewDelegate.viewHolder.tl_1.getCurrentTab());
    }


    ExchangeSetFragment exchangeSetFragment;

    private void loadDrawerFragment() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (getChildFragmentManager()
                .findFragmentByTag("ExchangeSetFragment") == null) {
            exchangeSetFragment = new ExchangeSetFragment();
            transaction.add(R.id.fl_right, exchangeSetFragment, "ExchangeSetFragment");
        } else {
            exchangeSetFragment = (ExchangeSetFragment) getChildFragmentManager()
                    .findFragmentByTag("ExchangeSetFragment");
            transaction.show(exchangeSetFragment);
        }
        transaction.commitAllowingStateLoss();
    }

    public void closeDrawer() {
        viewDelegate.viewHolder.main_drawer_layout.closeDrawers();
    }

    String statu = "1";

    public void refreshOkex() {
        if (okexTrParentsFragment != null) {
            okexTrParentsFragment.setChildTradeDetail();
        }
    }

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                statu = GsonUtil.getInstance().getValue(data, "status");
                break;
            case 0x124:
                addRequest(binder.bitmexbindStatus(this));
                break;
        }
    }

}
