package com.sixbexchange.mvp.fragment.transaction.bitmex;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.fivefivelike.mybaselibrary.base.BasePullFragment;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.sixbexchange.R;
import com.sixbexchange.adapter.SelectCoinsAdapter;
import com.sixbexchange.entity.bean.SelectExchCoinBean;
import com.sixbexchange.mvp.databinder.BaseActivityPullBinder;
import com.sixbexchange.mvp.delegate.BaseActivityPullDelegate;

import java.util.ArrayList;
import java.util.List;

public class BMTrSelectTypeFragment extends BasePullFragment<BaseActivityPullDelegate, BaseActivityPullBinder> {

    @Override
    protected Class<BaseActivityPullDelegate> getDelegateClass() {
        return BaseActivityPullDelegate.class;
    }

    @Override
    public BaseActivityPullBinder getDataBinder(BaseActivityPullDelegate viewDelegate) {
        return new BaseActivityPullBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initToolbar(new ToolbarBuilder().setTitle("选择交易币种").setShowBack(false)
                .setmRightImg1(CommonUtils.getString(R.string.ic_seting)));
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) viewDelegate.getmToolbarTitle().getLayoutParams();
        layoutParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        layoutParams.leftMargin = (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_20px);
        viewDelegate.getmToolbarTitle().setLayoutParams(layoutParams);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initList(new ArrayList<SelectExchCoinBean>());
        onRefresh();
    }


    String exchName = "bitmex";
    SelectCoinsAdapter adapter;

    private void initList(List<SelectExchCoinBean> data) {
        if (adapter == null) {
            adapter = new SelectCoinsAdapter(getActivity(), data);
            adapter.setDefaultClickLinsener(new DefaultClickLinsener() {
                @Override
                public void onClick(View view, int position, Object item) {
                    if (getParentFragment() instanceof BMTrFragment) {
                        if ((int) item < adapter.getDatas().get(position).getList().size()) {
                            ((BMTrFragment) getParentFragment()).closeDrawer();
                            if (getParentFragment().getParentFragment() instanceof BMTrParentsFragment) {
                                ((BMTrParentsFragment) getParentFragment().getParentFragment()).selectCurrencyPair(
                                        adapter.getDatas().get(position).getList().get((int) item));
                            }
                        }
                    }
                }
            });
            initRecycleViewPull(adapter, new LinearLayoutManager(getActivity()));
            viewDelegate.setIsPullDown(false);
            viewDelegate.setCanToTop(false);
            viewDelegate.setIsLoadMore(false);
        } else {
            getDataBack(adapter.getDatas(), data, adapter);
        }
    }

    @Override
    protected void clickRightIv() {
        super.clickRightIv();
        if (getParentFragment() instanceof BMTrFragment) {
            ((BMTrFragment) getParentFragment()).closeDrawer();
        }
    }

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                initList(GsonUtil.getInstance().toList(data, SelectExchCoinBean.class));
                break;
        }
    }

    @Override
    protected void refreshData() {
        if (adapter != null) {
            addRequest(binder.tradeall(exchName, this));
        }
    }
}
