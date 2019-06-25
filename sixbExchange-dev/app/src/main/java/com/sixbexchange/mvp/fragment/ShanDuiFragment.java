package com.sixbexchange.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;

import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.glide.GlideUtils;
import com.sixbexchange.R;
import com.sixbexchange.mvp.databinder.ShanDuiBinder;
import com.sixbexchange.mvp.delegate.ShanDuiDelegate;
import com.sixbexchange.mvp.fragment.transaction.huobi.OneTokenFragment;
import com.sixbexchange.mvp.fragment.transaction.huobi.OneTokenParentsFrgment;

public class ShanDuiFragment extends BaseDataBindFragment<ShanDuiDelegate, ShanDuiBinder> {

    @Override
    protected Class<ShanDuiDelegate> getDelegateClass() {
        return ShanDuiDelegate.class;
    }

    @Override
    public ShanDuiBinder getDataBinder(ShanDuiDelegate viewDelegate) {
        return new ShanDuiBinder(viewDelegate);
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
//        initToolbar(new ToolbarBuilder().setTitle("")
//                .setmRightImg1(CommonUtils.getString(R.string.ic_seting))
//                .setShowBack(false));
//        viewDelegate.getmToolbarTitle().setVisibility(View.GONE);
        addRequest(binder.bitmexbindStatus(this));
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        loadDrawerFragment();
        GlideUtils.loadImage("https://mapp.bicoin.info/wang/other/6b/help%403x.png",
                viewDelegate.viewHolder.iv_description,GlideUtils.getNoCacheRO());
    }

    @Override
    protected void clickRightIv() {
        super.clickRightIv();
        viewDelegate.viewHolder.main_drawer_layout.openDrawer(Gravity.RIGHT);
//        exchangeSetFragment.setExchPosition(viewDelegate.viewHolder.tl_1.getCurrentTab());
    }


    OneTokenParentsFrgment exchangeSetFragment;

    private void loadDrawerFragment() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            exchangeSetFragment = new OneTokenParentsFrgment();
            transaction.add(R.id.fl_right, exchangeSetFragment, "ExchangeSetFragment");

        transaction.commitAllowingStateLoss();
    }

    public void closeDrawer() {
        viewDelegate.viewHolder.main_drawer_layout.closeDrawers();
    }

    String statu = "1";

    public void refreshOkex() {
//        if (okexTrParentsFragment != null) {
//            okexTrParentsFragment.setChildTradeDetail();
//        }
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
