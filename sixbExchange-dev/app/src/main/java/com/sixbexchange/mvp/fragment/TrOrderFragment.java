package com.sixbexchange.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ObjectUtils;
import com.fivefivelike.mybaselibrary.base.BasePullFragment;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.view.DropDownPopu;
import com.fivefivelike.mybaselibrary.view.IconFontTextview;
import com.sixbexchange.R;
import com.sixbexchange.adapter.OrdersAdapter;
import com.sixbexchange.entity.bean.OrderBean;
import com.sixbexchange.entity.bean.TradeDetailBean;
import com.sixbexchange.mvp.databinder.BaseFragmentPullBinder;
import com.sixbexchange.mvp.delegate.BaseFragentPullDelegate;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

public class TrOrderFragment extends BasePullFragment<BaseFragentPullDelegate, BaseFragmentPullBinder> {

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

    OrdersAdapter adapter;

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);


    }

    private void initList(List<OrderBean> data) {
        if (adapter == null) {
            adapter = new OrdersAdapter(getActivity(), data);
            viewDelegate.viewHolder.pull_recycleview.setBackgroundColor(CommonUtils.getColor(R.color.base_bg));
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
        tv_coin.setText(s.getCurrencyPairName() + " " + CommonUtils.getString(R.string.ic_Down));
        initList(new ArrayList<OrderBean>());
        onRefresh();
    }

    public IconFontTextview tv_coin;
    public IconFontTextview tv_statu;

    private void initHeader() {
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.layout_order_header, null);
        this.tv_coin = (IconFontTextview) rootView.findViewById(R.id.tv_coin);
        this.tv_statu = (IconFontTextview) rootView.findViewById(R.id.tv_statu);
        tv_coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TransactionFragment) getParentFragment()).showSelectCurrencyPair(v);
            }
        });

        tv_statu.setText("待成交 " + CommonUtils.getString(R.string.ic_Down));
        tv_statu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectState(v);
            }
        });

        viewDelegate.viewHolder.fl_pull.addView(rootView, 0);
    }

    DropDownPopu dropDownPopu;
    List<String> states;
    String state = "active";

    public void showSelectState(View view) {
        if (dropDownPopu == null) {
            dropDownPopu = new DropDownPopu();
            states = new ArrayList<>();
            states.add("待成交");
            states.add("已结束");
        }
        dropDownPopu.show(
                states, view, getActivity(),
                ObjectUtils.equals(state, "active") ? 0 :1,
                new MultiItemTypeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                       if (position == 0) {
                            state = "active";
                        } else {
                            state = "end";
                        }
                        tv_statu.setText(states.get(position) + " " + CommonUtils.getString(R.string.ic_Down));
                        dropDownPopu.dismiss();
                        initList(new ArrayList<OrderBean>());
                        onRefresh();
                    }

                    @Override
                    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                        return false;
                    }
                }
        );
    }


    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                initList(GsonUtil.getInstance().toList(data, OrderBean.class));
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
