package com.sixbexchange.mvp.fragment.transaction.bitmex;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.fivefivelike.mybaselibrary.base.BasePullFragment;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.sixbexchange.R;
import com.sixbexchange.adapter.BMOrdersAdapter;
import com.sixbexchange.entity.bean.OrderBean;
import com.sixbexchange.entity.bean.TradeDetailBean;
import com.sixbexchange.mvp.databinder.BaseFragmentPullBinder;
import com.sixbexchange.mvp.delegate.BaseFragentPullDelegate;
import com.tablayout.CommonTabLayout;
import com.tablayout.TabEntity;
import com.tablayout.listener.CustomTabEntity;
import com.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

public class BMTrOrderFragment extends BasePullFragment<BaseFragentPullDelegate, BaseFragmentPullBinder> {

    @Override
    protected Class<BaseFragentPullDelegate> getDelegateClass() {
        return BaseFragentPullDelegate.class;
    }

    @Override
    public BaseFragmentPullBinder getDataBinder(BaseFragentPullDelegate viewDelegate) {
        return new BaseFragmentPullBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initList(new ArrayList<OrderBean>());
    }

    BMOrdersAdapter adapter;

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);


    }
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (adapter != null && tradeDetailBean != null) {
            onRefresh();
        }
    }
    private void initList(List<OrderBean> data) {
        if (adapter == null) {
            adapter = new BMOrdersAdapter(getActivity(), data);
            adapter.setDefaultClickLinsener(new DefaultClickLinsener() {
                @Override
                public void onClick(View view, int position, Object item) {
                    addRequest(binder.cancelOrder(
                            tradeDetailBean.getExchange(),
                            adapter.getDatas().get(position).getExchange_oid(),
                            BMTrOrderFragment.this
                    ));
                }
            });
            initHeader();
            initRecycleViewPull(adapter, new LinearLayoutManager(getActivity()));
        } else {
            adapter.setTradeDetailBean(tradeDetailBean);
            getDataBack(adapter.getDatas(), data, adapter);
        }
    }

    TradeDetailBean tradeDetailBean;

    public void initTradeDetail(TradeDetailBean s) {
        tradeDetailBean = s;
        initList(new ArrayList<OrderBean>());
        onRefresh();
    }

    String[] title = {"未成交", "已成交","历史订单","失败订单"};
    private ArrayList<CustomTabEntity> mTabEntities;
    CommonTabLayout tl_2;
    String state = "active";

    private void initHeader() {
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.layout_order_header, null);
        this.tl_2 = (CommonTabLayout) rootView.findViewById(R.id.tl_2);
        mTabEntities = new ArrayList<>();
        for (int i = 0; i < title.length; i++) {
            mTabEntities.add(new TabEntity(title[i], 0, 0));
        }
        tl_2.setTabData(mTabEntities);
        tl_2.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 0) {
                    state = "active";
                } else if(position==1){
                    state = "end";
                }else if(position==2){
                    state = "";
                }else if(position==3){
                    state = "error-order";
                }
                onRefresh();
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        viewDelegate.viewHolder.fl_pull.addView(rootView, 0);
    }


    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                initList(GsonUtil.getInstance().toList(data, OrderBean.class));
                break;
            case 0x126:
                onRefresh();
                break;
        }
    }

    @Override
    protected void refreshData() {
        viewDelegate.viewHolder.swipeRefreshLayout.setRefreshing(true);
        addRequest(binder.accountgetOrders(
                tradeDetailBean.getExchange(),
                state,
                tradeDetailBean.getCurrencyPair(),
                this));
    }
}
