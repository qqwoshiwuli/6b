package com.sixbexchange.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.CacheUtils;
import com.fivefivelike.mybaselibrary.base.BasePullFragment;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.ListUtils;
import com.fivefivelike.mybaselibrary.utils.SaveUtil;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.sixbexchange.R;
import com.sixbexchange.adapter.WalletCoinAdapter;
import com.sixbexchange.base.CacheName;
import com.sixbexchange.entity.bean.WalletCoinBean;
import com.sixbexchange.mvp.databinder.BaseFragmentPullBinder;
import com.sixbexchange.mvp.delegate.BaseFragentPullDelegate;
import com.sixbexchange.mvp.dialog.OpenBitmexDialog;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
* 钱包资产列表
* @author gqf
* @Description
* @Date  2019/4/3 0003 11:15
* @Param
* @return
**/

public class ExchWalletFragment extends BasePullFragment<BaseFragentPullDelegate, BaseFragmentPullBinder> {

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
    }

    public static ExchWalletFragment newInstance(
            String typeStr,
            String exchName,
            int position) {
        ExchWalletFragment newFragment = new ExchWalletFragment();
        Bundle bundle = new Bundle();
        bundle.putString("typeStr", typeStr);
        bundle.putString("exchName", exchName);
        bundle.putInt("position", position);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    String typeStr = "";
    String exchName = "";
    int position = 0;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("typeStr", typeStr);
        outState.putString("exchName", exchName);
        outState.putInt("position", position);
    }

    WalletCoinAdapter adapter;


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (savedInstanceState != null) {
            typeStr = savedInstanceState.getString("typeStr", "");
            exchName = savedInstanceState.getString("exchName", "");
            position = savedInstanceState.getInt("position");
        } else {
            typeStr = this.getArguments().getString("typeStr", "");
            exchName = this.getArguments().getString("exchName", "");
            position = this.getArguments().getInt("position");
        }
        String data=SaveUtil.getInstance().getString("qianbaodata1");
        if (TextUtils.isEmpty(data)&&position==0)
        {
            onRefresh();
            initList(new ArrayList<WalletCoinBean>());
        }else
        {
            List<String> list = GsonUtil.getInstance().toList(
                    data, String.class
            );
            Map<String, String> map = GsonUtil.getInstance().toMap(list.get(position),
                    new TypeReference<Map<String, String>>() {
                    });
            map.remove("name");
            map.remove("position");
            List<WalletCoinBean> walletCoinBeans = new ArrayList<>();
            for (String key : map.keySet()) {
                WalletCoinBean walletCoinBean = GsonUtil.getInstance().toObj(map.get(key), WalletCoinBean.class);
                walletCoinBean.setCoin(key);
                walletCoinBeans.add(walletCoinBean);
            }
            initList(walletCoinBeans);
            CacheUtils.getInstance().put(CacheName.ExchWalletCache, data);
            onRefresh();
        }


    }

    private void initList(List<WalletCoinBean> data) {
        if (adapter == null) {
            adapter = new WalletCoinAdapter(getActivity(), data,exchName);
            adapter.setDefaultClickLinsener(new DefaultClickLinsener() {
                @Override
                public void onClick(View view, int p, Object item) {
                    if (getParentFragment().getParentFragment() instanceof MainFragment) {
                        if (view.getId() == R.id.tv_withdraw) {
                            ((MainFragment) getParentFragment().getParentFragment()).startBrotherFragment(
                                    WithdrawCoinFragment.newInstance(
                                            adapter.getDatas().get(p).getCoin(),
                                            position));
                        } else if (view.getId() == R.id.tv_transfer) {
                            ((MainFragment) getParentFragment().getParentFragment()).startBrotherFragment(
                                    RechargeFragment.newInstance(adapter.getDatas().get(p).getCoin().replace("XBT", "BTC")
                                            , exchName, 1, position));
                        } else if (view.getId() == R.id.tv_recharge) {
                            ((MainFragment) getParentFragment().getParentFragment()).startBrotherFragment(
                                    RechargeFragment.newInstance(adapter.getDatas().get(p).getCoin()
                                            , exchName, 0, position));
                        }
                    }
                }
            });
            adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {


                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
            viewDelegate.setIsPullDown(false);
            viewDelegate.setCanToTop(false);
            viewDelegate.setIsLoadMore(false);
            initRecycleViewPull(adapter, new LinearLayoutManager(getActivity()));
        } else {
            getDataBack(adapter.getDatas(), data, adapter);
            if (exchName.toLowerCase().contains("bitmex")&& ListUtils.isEmpty(data)) {
                View rootView = getActivity().getLayoutInflater().inflate(R.layout.layout_open_bitmex, null);
                rootView.findViewById(R.id.tv_open).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OpenBitmexDialog openBitmexDialog=new OpenBitmexDialog(getActivity());
                        openBitmexDialog.showDialog(new DefaultClickLinsener() {
                            @Override
                            public void onClick(View view, int position, Object item) {
                                addRequest(binder.bitmexbind(ExchWalletFragment.this));
                            }
                        });
                    }
                });
                viewDelegate.viewHolder.fl_rcv.addView(rootView);
            }
        }
    }

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                if (position==0)
                {
                    SaveUtil.getInstance().saveString("qianbaodata1",data);
                }
                List<String> list = GsonUtil.getInstance().toList(
                        data, String.class
                );
                Map<String, String> map = GsonUtil.getInstance().toMap(list.get(position),
                        new TypeReference<Map<String, String>>() {
                        });
                map.remove("name");
                map.remove("position");
                List<WalletCoinBean> walletCoinBeans = new ArrayList<>();
                for (String key : map.keySet()) {
                    WalletCoinBean walletCoinBean = GsonUtil.getInstance().toObj(map.get(key), WalletCoinBean.class);
                    walletCoinBean.setCoin(key);
                    walletCoinBeans.add(walletCoinBean);
                }
                initList(walletCoinBeans);
                CacheUtils.getInstance().put(CacheName.ExchWalletCache, data);
                break;
            case 0x124:
                addRequest(binder.getAccountDetail(true, this));
                break;
        }
    }


    @Override
    protected void refreshData() {
        addRequest(binder.getAccountDetail(adapter == null, this));
    }
}
