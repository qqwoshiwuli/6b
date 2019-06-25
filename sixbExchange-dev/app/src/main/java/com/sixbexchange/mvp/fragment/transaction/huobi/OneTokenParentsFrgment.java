package com.sixbexchange.mvp.fragment.transaction.huobi;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.blankj.utilcode.util.CacheUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.http.WebSocketRequest;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.ListUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.view.DropDownPopu;
import com.fivefivelike.mybaselibrary.view.InnerPagerAdapter;
import com.google.gson.Gson;
import com.sixbexchange.R;
import com.sixbexchange.adapter.SelectHuobi2Adapter;
import com.sixbexchange.adapter.SelectHuobiAdapter;
import com.sixbexchange.base.CacheName;
import com.sixbexchange.entity.bean.ExchSelectPositionBean;
import com.sixbexchange.entity.bean.OrderBean;
import com.sixbexchange.entity.bean.SelectExchCoinBean;
import com.sixbexchange.entity.bean.TradeDetailBean;
import com.sixbexchange.entity.bean.selectExchhuobiBeans;
import com.sixbexchange.mvp.databinder.ExchTrParentsBinder;
import com.sixbexchange.mvp.delegate.ExchTrParentsDelegate;
import com.sixbexchange.mvp.fragment.transaction.bitmex.BMTrFragment;
import com.sixbexchange.mvp.fragment.transaction.bitmex.BMTrOrderFragment;
import com.sixbexchange.mvp.fragment.transaction.bitmex.BMTrPositionFragment;
import com.sixbexchange.mvp.fragment.transaction.okex.OkexTrCloseFragment;
import com.sixbexchange.mvp.fragment.transaction.okex.OkexTrOpenFragment;
import com.sixbexchange.mvp.fragment.transaction.okex.OkexTrOrderFragment;
import com.sixbexchange.mvp.fragment.transaction.okex.OkexTrParentsFragment;
import com.sixbexchange.mvp.fragment.transaction.okex.OkexTrPositionFragment;
import com.sixbexchange.mvp.popu.SelectExchCoinPopu;
import com.tablayout.TabEntity;
import com.tablayout.listener.CustomTabEntity;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class OneTokenParentsFrgment extends BaseDataBindFragment<ExchTrParentsDelegate, ExchTrParentsBinder> {

    @Override
    protected Class<ExchTrParentsDelegate> getDelegateClass() {
        return ExchTrParentsDelegate.class;
    }

    @Override
    public ExchTrParentsBinder getDataBinder(ExchTrParentsDelegate viewDelegate) {
        return new ExchTrParentsBinder(viewDelegate);
    }

    String TradeIdCache;
    String TradeExchangeCache = "huobip";

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initTablelayout();
        TradeIdCache = CacheUtils.getInstance().getString(CacheName.TradeIdhuobiCache);
        addRequest(binder.tradeall2(TradeExchangeCache, this));
    }

    ArrayList fragments;
    InnerPagerAdapter innerPagerAdapter;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    int showFragmentPosition = 0;
    OneTokenFragment oneTokenFragment;

    private void initTablelayout() {
        fragments = new ArrayList<>();
        fragments.add(oneTokenFragment = new OneTokenFragment());
        if (innerPagerAdapter == null) {
            String[] stringArray = CommonUtils.getStringArray(R.array.sa_select_okex_transaction_title);
            mTabEntities = new ArrayList<>();
            for (int i = 0; i < 1; i++) {
                mTabEntities.add(new TabEntity(stringArray[i], 0, 0));
            }
            viewDelegate.viewHolder.tl_2.setTabData(mTabEntities);
            viewDelegate.viewHolder.tl_2.setTextSelectColor(CommonUtils.getColor(R.color.mark_color));
            viewDelegate.viewHolder.tl_2.setTextUnselectColor(CommonUtils.getColor(R.color.color_font2));
            viewDelegate.viewHolder.tl_2.setIndicatorColor(CommonUtils.getColor(R.color.mark_color));
            viewDelegate.viewHolder.vp_root.setOffscreenPageLimit(5);
            innerPagerAdapter = new InnerPagerAdapter(getChildFragmentManager(), fragments, stringArray);
            viewDelegate.viewHolder.tl_2.setViewPager(innerPagerAdapter, viewDelegate.viewHolder.vp_root);
        } else {
            innerPagerAdapter.setDatas(fragments);
        }
        viewDelegate.viewHolder.tl_2.setVisibility(View.GONE);
        viewDelegate.viewHolder.tl_2.setCurrentTab(showFragmentPosition);
        viewDelegate.viewHolder.vp_root.setCurrentItem(showFragmentPosition);
    }

    public void setChildTradeDetail(selectExchhuobiBeans.HuobiDataBean huobiDataBean) {
        oneTokenFragment.initTradeDetail(huobiDataBean);
//        okexTrCloseFragment.initTradeDetail(tradeDetailBean);
//        okexTrOrderFragment.initTradeDetail(tradeDetailBean);
//        addRequest(binder.accountgetOrders(
//                tradeDetailBean.getExchange(),
//                tradeDetailBean.getCurrencyPair(),
//                this));
    }


    //交易页面统一刷新
    public void tradeOnRefreshData(boolean isNow) {
        Observable.intervalRange(0, isNow ? 1 : 2, isNow ? 0 : 500, 1500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onTerminateDetach()
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                            addRequest(binder.tradeall2(TradeExchangeCache, OneTokenParentsFrgment.this));
//                            addRequest(binder.tradedetail(TradeExchangeCache, TradeIdCache, OneTokenParentsFrgment.this));
                    }
                });
    }


    TradeDetailBean tradeDetailBean;
    List<ExchSelectPositionBean> exchCoins;

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x127:
                selectExchCoinBeans = GsonUtil.getInstance().toList(data, selectExchhuobiBeans.class);
                oneTokenFragment.initTradeDetail(selectExchCoinBeans.get(bizhongint).getData().get(transactionbizhongint));
                oneTokenFragment.viewDelegate.viewHolder.swipeRefreshLayout.setRefreshing(false);
                break;
        }
    }

    List<selectExchhuobiBeans> selectExchCoinBeans;


    List<String> coins = new ArrayList<>();


    SelectExchCoinPopu selectExchCoinPopu;
    PopupWindow window=null;
    View contentView;
    ListView bizhong;
    ListView transactionbizhong;
    SelectHuobiAdapter selectHuobiAdapter;
    SelectHuobi2Adapter selectHuobi2Adapter;
    int bizhongint=0;
    int transactionbizhongint=0;
    //选择交易对下拉框
    public void showpopwindows(View view) {
        oneTokenFragment.viewDelegate.viewHolder.touming.getBackground().setAlpha(120);
        if (window==null)
        {
         contentView=LayoutInflater.from(getContext()).inflate(R.layout.activity_base_huobipull, null, false);
         window=new PopupWindow(contentView,ViewGroup.LayoutParams.MATCH_PARENT,1000);
        bizhong=contentView.findViewById(R.id.bizhong);
        transactionbizhong=contentView.findViewById(R.id.transactionbizhong);
        selectHuobiAdapter=new SelectHuobiAdapter(getContext(),selectExchCoinBeans);
        bizhong.setAdapter(selectHuobiAdapter);
        selectHuobi2Adapter=new SelectHuobi2Adapter(getContext(),selectExchCoinBeans);
        transactionbizhong.setAdapter(selectHuobi2Adapter);
            bizhong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //改变选中状态
                    selectHuobiAdapter.setCurrentItem(position);
                    //通知ListView改变状态
                    selectHuobiAdapter.notifyDataSetChanged();
                    selectHuobi2Adapter.setlist(position);
                    selectHuobi2Adapter.notifyDataSetChanged();
                    bizhongint=position;
                }
            });
            transactionbizhong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //改变选中状态
                    selectHuobi2Adapter.setCurrentItem(position);
                    //通知ListView改变状态
                    selectHuobi2Adapter.notifyDataSetChanged();
                    transactionbizhongint=position;
                    setChildTradeDetail(selectExchCoinBeans.get(bizhongint).getData().get(position));
                    window.dismiss();
                }
            });
        // 设置PopupWindow的背景
        window.setBackgroundDrawable(CommonUtils.getDrawable(R.color.colorPrimary));
        // 设置PopupWindow是否能响应外部点击事件
        window.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        window.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
        window.showAsDropDown(view);
        oneTokenFragment.viewDelegate.viewHolder.touming.setVisibility(View.VISIBLE);
            window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    oneTokenFragment.viewDelegate.viewHolder.touming.setVisibility(View.GONE);
                }
            });
    }else
        {
            window.setFocusable(true);
            if (window.isShowing())
            {
                window.dismiss();
            }else
            {
                window.showAsDropDown(view);
                oneTokenFragment.viewDelegate.viewHolder.touming.setVisibility(View.VISIBLE);
            }
        }
    }
}
