package com.sixbexchange.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.base.BasePullFragment;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.ListUtils;
import com.fivefivelike.mybaselibrary.utils.SaveUtil;
import com.sixbexchange.R;
import com.sixbexchange.adapter.ExchWalletAdapter;
import com.sixbexchange.entity.bean.ExchWalletBean;
import com.sixbexchange.mvp.databinder.AssetsBinder;
import com.sixbexchange.mvp.delegate.AssetsDelegate;
import com.sixbexchange.utils.BigUIUtil;
import com.tablayout.listener.OnTabSelectListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/*
 *资产主页
 * @author gqf
 * @Description
 * @Date  2019/4/3 0003 11:14
 * @Param
 * @return
 **/
public class AssetsFragment extends BaseDataBindFragment<AssetsDelegate, AssetsBinder> {

    @Override
    protected Class<AssetsDelegate> getDelegateClass() {
        return AssetsDelegate.class;
    }


    ExchWalletAdapter exchWalletAdapter;

    @Override
    public AssetsBinder getDataBinder(AssetsDelegate viewDelegate) {
        return new AssetsBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initToolbar(new ToolbarBuilder().setTitle("我的资产")
                .setmRightImg1(CommonUtils.getString(R.string.ic_file_search) + "账单")
                .setShowBack(false));
        viewDelegate.setToolColor(R.color.mark_color, false);
        viewDelegate.getmToolbarTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh(true);
            }
        });
    }

    @Override
    protected void clickRightIv() {
        super.clickRightIv();
        //历史账单

    }

    public void onRefresh(boolean isShow) {
        addRequest(binder.getAccount(isShow, AssetsFragment.this));
        if (!ListUtils.isEmpty(fragments)) {
            if (fragments.size() > viewDelegate.viewHolder.vp_sliding.getCurrentItem()) {
                if (fragments.get(viewDelegate.viewHolder.vp_sliding.getCurrentItem()) instanceof BasePullFragment) {
                    ((BasePullFragment) fragments
                            .get(viewDelegate.viewHolder.vp_sliding.getCurrentItem()))
                            .onRefresh();
                }
            }
        }
    }


    private void initList(List<ExchWalletBean> data) {
//        if (exchWalletAdapter == null) {
            exchWalletAdapter = new ExchWalletAdapter(getActivity(), data);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            viewDelegate.viewHolder.recycler_view.setLayoutManager(linearLayoutManager);
            viewDelegate.viewHolder.recycler_view.setAdapter(exchWalletAdapter);
//        } else {
//            exchWalletAdapter.setData(data);
//        }
        BigDecimal bigDecimal = new BigDecimal("0");
        for (int i = 0; i < data.size(); i++) {
            bigDecimal = bigDecimal.add(new BigDecimal(data.get(i).getUsdt()));
        }
        viewDelegate.viewHolder.tv_total_assets.setText(
                BigUIUtil.getinstance().bigEnglishNum(
                        bigDecimal.stripTrailingZeros().toPlainString(), 4));
//        exchWalletAdapter.setData(data);
//        exchWalletAdapter.notifyDataSetChanged();
        initFragments(exchWalletAdapter.getDatas());
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        String data=SaveUtil.getInstance().getString("qianbaodata");
        if (TextUtils.isEmpty(data))
        {
            initList(new ArrayList<ExchWalletBean>());
            fragments.clear();
            onRefresh(exchWalletAdapter == null);
        }
        else
        {
            viewDelegate.viewHolder.judgeNestedScrollView.setTabAndPager(
                    viewDelegate.viewHolder.tl_2,
                    viewDelegate.viewHolder.vp_sliding
            );
            initList(GsonUtil.getInstance().toList(data, ExchWalletBean.class));
            onRefresh(false);
        }
//        initList(new ArrayList<ExchWalletBean>());
//        fragments.clear();

    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

    }

    ArrayList<Fragment> fragments = new ArrayList<>();
    List<String> mTitles = new ArrayList<>();

    private void initFragments(List<ExchWalletBean> data) {
        if (!ListUtils.isEmpty(data) && ListUtils.isEmpty(fragments)) {
            fragments.clear();
            mTitles.clear();
            for (int i = 0; i < data.size(); i++) {
                mTitles.add(data.get(i).getExchangeCn());
                fragments.add(ExchWalletFragment.newInstance(
                        data.get(i).getExchange(),
                        data.get(i).getExchangeCn(),
                        i));
            }
            viewDelegate.viewHolder.vp_sliding.setOffscreenPageLimit(1);
            viewDelegate.viewHolder.tl_2.setViewPager(viewDelegate.viewHolder.vp_sliding,
                    mTitles.toArray(new String[mTitles.size()]), this, fragments);
            viewDelegate.viewHolder.tl_2.setCurrentTab(0);
            viewDelegate.viewHolder.tl_2.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelect(int position) {

                }

                @Override
                public void onTabReselect(int position) {
                    if (fragments.size() > position && fragments.get(position) != null && fragments.get(position) instanceof BasePullFragment) {
                        ((BasePullFragment) fragments.get(position)).onRefresh();
                    }
                }
            });
        }
    }


    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                SaveUtil.getInstance().saveString("qianbaodata",data);
                viewDelegate.viewHolder.judgeNestedScrollView.setTabAndPager(
                        viewDelegate.viewHolder.tl_2,
                        viewDelegate.viewHolder.vp_sliding
                );
                initList(GsonUtil.getInstance().toList(data, ExchWalletBean.class));
                break;
        }
    }

}
