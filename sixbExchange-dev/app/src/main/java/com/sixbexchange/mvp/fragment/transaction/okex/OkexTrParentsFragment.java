package com.sixbexchange.mvp.fragment.transaction.okex;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.CacheUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.ListUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.utils.logger.KLog;
import com.fivefivelike.mybaselibrary.view.DropDownPopu;
import com.fivefivelike.mybaselibrary.view.InnerPagerAdapter;
import com.sixbexchange.R;
import com.sixbexchange.base.CacheName;
import com.sixbexchange.entity.bean.ExchSelectPositionBean;
import com.sixbexchange.entity.bean.OrderBean;
import com.sixbexchange.entity.bean.SelectExchCoinBean;
import com.sixbexchange.entity.bean.TradeDetailBean;
import com.sixbexchange.entity.bean.TradeDetailOKBean;
import com.sixbexchange.mvp.databinder.ExchTrParentsBinder;
import com.sixbexchange.mvp.delegate.ExchTrParentsDelegate;
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

public class OkexTrParentsFragment extends BaseDataBindFragment<ExchTrParentsDelegate, ExchTrParentsBinder> {

    @Override
    protected Class<ExchTrParentsDelegate> getDelegateClass() {
        return ExchTrParentsDelegate.class;
    }

    @Override
    public ExchTrParentsBinder getDataBinder(ExchTrParentsDelegate viewDelegate) {
        return new ExchTrParentsBinder(viewDelegate);
    }

    String TradeIdCache;
    String TradeExchangeCache = "okef";

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initTablelayout();
        TradeIdCache = CacheUtils.getInstance().getString(CacheName.TradeIdOkexCache);
        addRequest(binder.tradelistok(this));
        addRequest(binder.tradeall(TradeExchangeCache, this));
    }

    ArrayList fragments;
    InnerPagerAdapter innerPagerAdapter;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    int showFragmentPosition = 0;
    OkexTrOpenFragment okexTrOpenFragment;
    OkexTrCloseFragment okexTrCloseFragment;
    OkexTrOrderFragment okexTrOrderFragment;
    OkexTrPositionFragment okexTrPositionFragment;

    private void initTablelayout() {
        fragments = new ArrayList<>();
        fragments.add(okexTrOpenFragment = new OkexTrOpenFragment());
        fragments.add(okexTrCloseFragment = new OkexTrCloseFragment());
        fragments.add(okexTrOrderFragment = new OkexTrOrderFragment());
        fragments.add(okexTrPositionFragment = new OkexTrPositionFragment());
        if (innerPagerAdapter == null) {
            String[] stringArray = CommonUtils.getStringArray(R.array.sa_select_okex_transaction_title);
            mTabEntities = new ArrayList<>();
            for (int i = 0; i < stringArray.length; i++) {
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
        viewDelegate.viewHolder.tl_2.setCurrentTab(showFragmentPosition);
        viewDelegate.viewHolder.vp_root.setCurrentItem(showFragmentPosition);
    }

    //分配交易对信息
    public void setChildTradeDetail() {
        okexTrOpenFragment.initTradeDetail(tradeDetailBean);
        okexTrCloseFragment.initTradeDetail(tradeDetailBean);
        okexTrOrderFragment.initTradeDetail(tradeDetailBean);
        addRequest(binder.accountgetOrders(
                tradeDetailBean.getExchange(),
                tradeDetailBean.getCurrencyPair(),
                this));
    }

    //分配持仓页分类信息
    private void setPositionCoin() {
        if (!ListUtils.isEmpty(exchCoins)) {
            for (int i = 0; i < exchCoins.size(); i++) {
                if (ObjectUtils.equals(
                        tradeDetailBean.getPositionText(),
                        exchCoins.get(i).getCurrency())) {
                    okexTrPositionFragment.initTradeDetail(exchCoins.get(i), tradeDetailBean);
                }
            }
        }
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
                        if (tradeDetailBean != null) {
                            okexTrOpenFragment.onRefreshData(aLong == 0);
                            okexTrCloseFragment.onRefreshData(aLong == 0);
                            addRequest(binder.accountgetOrders(
                                    tradeDetailBean.getExchange(),
                                    tradeDetailBean.getCurrencyPair(),
                                    OkexTrParentsFragment.this));
                        } else {
                            addRequest(binder.tradeall(TradeExchangeCache, OkexTrParentsFragment.this));
                            addRequest(binder.tradedetail(TradeExchangeCache, TradeIdCache, OkexTrParentsFragment.this));
                        }
                    }
                });
    }

    TradeDetailBean tradeDetailBean;

    List<ExchSelectPositionBean> exchCoins;

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                //订单列表
                List<OrderBean> orderBeans = GsonUtil.getInstance().toList(data, OrderBean.class);
                if (ListUtils.isEmpty(orderBeans) || (!ListUtils.isEmpty(orderBeans) &&
                        ObjectUtils.equals(tradeDetailBean.getCurrencyPair(),
                                orderBeans.get(0).getContract()))) {
                    okexTrOpenFragment.setOrderList(orderBeans);
                    okexTrCloseFragment.setOrderList(orderBeans);
                }
                break;
            case 0x124:
                //交易对信息
                tradeDetailBean = GsonUtil.getInstance().toObj(data, TradeDetailBean.class);
                for (int i = 0; i < tradeDetailOKBeans.size(); i++) {
                    if (tradeDetailBean.getCurrency().equals(tradeDetailOKBeans.get(i).getUnderlying_index()))
                    {
                        if (tradeDetailBean.getCurrencyPairName().contains("当周")&&tradeDetailOKBeans.get(i).getAlias().equals("this_week"))
                        {
                            tradeDetailBean.setKx(tradeDetailBean.getDelivery());
                            tradeDetailBean.setDelivery("futures/depth5:"+tradeDetailOKBeans.get(i).getInstrument_id());
                            tradeDetailBean.setCurrency("futures/ticker:"+tradeDetailOKBeans.get(i).getInstrument_id());
                            tradeDetailBean.setIndex("index/ticker:"+tradeDetailOKBeans.get(i).getUnderlying_index()+"-"+tradeDetailOKBeans.get(i).getQuote_currency());
                        }else if (tradeDetailBean.getCurrencyPairName().contains("次周")&&tradeDetailOKBeans.get(i).getAlias().equals("next_week"))
                        {
                            tradeDetailBean.setKx(tradeDetailBean.getDelivery());
                            tradeDetailBean.setDelivery("futures/depth5:"+tradeDetailOKBeans.get(i).getInstrument_id());
                            tradeDetailBean.setCurrency("futures/ticker:"+tradeDetailOKBeans.get(i).getInstrument_id());
                            tradeDetailBean.setIndex("index/ticker:"+tradeDetailOKBeans.get(i).getUnderlying_index()+"-"+tradeDetailOKBeans.get(i).getQuote_currency());
                        }else if (tradeDetailBean.getCurrencyPairName().contains("季度")&&tradeDetailOKBeans.get(i).getAlias().equals("quarter"))
                        {
                            tradeDetailBean.setKx(tradeDetailBean.getDelivery());
                            tradeDetailBean.setDelivery("futures/depth5:"+tradeDetailOKBeans.get(i).getInstrument_id());
                            tradeDetailBean.setCurrency("futures/ticker:"+tradeDetailOKBeans.get(i).getInstrument_id());
                            tradeDetailBean.setIndex("index/ticker:"+tradeDetailOKBeans.get(i).getUnderlying_index()+"-"+tradeDetailOKBeans.get(i).getQuote_currency());
                        }
                    }
                }
                //请求交易所下币种列表
                addRequest(binder.tradeCoins(tradeDetailBean.getExchange(), this));
                //分配交易对信息
                setChildTradeDetail();
                break;
            case 0x126:
                //交易所币种列表
                exchCoins = GsonUtil.getInstance().toList(data, ExchSelectPositionBean.class);
                coins.clear();
                for (int i = 0; i < exchCoins.size(); i++) {
                    coins.add(exchCoins.get(i).getName());
                }
                //分配持仓页分类信息
                setPositionCoin();
                break;
            case 0x127:
                selectExchCoinBeans = GsonUtil.getInstance().toList(data, SelectExchCoinBean.class);
                break;
            case  0x128:
                tradeDetailOKBeans = GsonUtil.getInstance().toList(data, TradeDetailOKBean.class);
                addRequest(binder.tradedetail(TradeExchangeCache, TradeIdCache, this));
                 break;
        }
    }
    List<TradeDetailOKBean> tradeDetailOKBeans;
    List<SelectExchCoinBean> selectExchCoinBeans;


    DropDownPopu dropDownPopu;
    List<String> coins = new ArrayList<>();

    //选择币种下拉框
    public void showSelectCoins(View view, String coin) {
        if (ListUtils.isEmpty(exchCoins)) {
            if (tradeDetailBean != null) {
                addRequest(binder.tradeCoins(tradeDetailBean.getExchange(), this));
            }
            return;
        }
        if (dropDownPopu == null) {
            dropDownPopu = new DropDownPopu();
        }
        dropDownPopu.show(
                coins, view, getActivity(),
                coins.indexOf(coin),
                new MultiItemTypeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                        //查询当前币种下  第一个交易对
                        dropDownPopu.dismiss();
                        for (int i = 0; i < selectExchCoinBeans.size(); i++) {
                            for (int j = 0; j < selectExchCoinBeans.get(i).getList().size(); j++) {
                                if (StringUtils.equalsIgnoreCase(
                                        exchCoins.get(position).getCurrencyPair(),
                                        selectExchCoinBeans.get(i).getList().get(j).getCurrencyPair())) {
                                    tradeDetailBean = selectExchCoinBeans.get(i).getList().get(j);
                                    setChildTradeDetail();
                                    okexTrPositionFragment.initTradeDetail(exchCoins.get(position),
                                            tradeDetailBean);
                                    return;
                                } else if (TextUtils.isEmpty(exchCoins.get(position).getCurrencyPair())) {
                                    okexTrPositionFragment.initTradeDetail(exchCoins.get(position),
                                            tradeDetailBean);
                                    return;
                                }
                            }
                        }
                    }

                    @Override
                    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                        return false;
                    }
                }
        );
    }


    SelectExchCoinPopu selectExchCoinPopu;

    //选择交易对下拉框
    public void showSelectCurrencyPair(View view) {
        if (tradeDetailBean != null && !ListUtils.isEmpty(selectExchCoinBeans)) {
            if (selectExchCoinPopu == null) {
                selectExchCoinPopu = new SelectExchCoinPopu(getActivity());
            }
            selectExchCoinPopu.showList(
                    selectExchCoinBeans,
                    tradeDetailBean,
                    view,
                    new DefaultClickLinsener() {
                        @Override
                        public void onClick(View view, int position, Object item) {
                            tradeDetailBean = selectExchCoinBeans
                                    .get(position).getList().get((int) item);
                            for (int i = 0; i < tradeDetailOKBeans.size(); i++) {
                                if (tradeDetailBean.getCurrency().equals(tradeDetailOKBeans.get(i).getUnderlying_index()))
                                {
                                    if (tradeDetailBean.getCurrencyPairName().contains("当周")&&tradeDetailOKBeans.get(i).getAlias().equals("this_week"))
                                    {
                                        tradeDetailBean.setKx(tradeDetailBean.getDelivery());
                                        tradeDetailBean.setDelivery("futures/depth5:"+tradeDetailOKBeans.get(i).getInstrument_id());
                                        tradeDetailBean.setCurrency("futures/ticker:"+tradeDetailOKBeans.get(i).getInstrument_id());
                                        tradeDetailBean.setIndex("index/ticker:"+tradeDetailOKBeans.get(i).getUnderlying_index()+"-"+tradeDetailOKBeans.get(i).getQuote_currency());
                                    }else if (tradeDetailBean.getCurrencyPairName().contains("次周")&&tradeDetailOKBeans.get(i).getAlias().equals("next_week"))
                                    {
                                        tradeDetailBean.setKx(tradeDetailBean.getDelivery());
                                        tradeDetailBean.setDelivery("futures/depth5:"+tradeDetailOKBeans.get(i).getInstrument_id());
                                        tradeDetailBean.setCurrency("futures/ticker:"+tradeDetailOKBeans.get(i).getInstrument_id());
                                        tradeDetailBean.setIndex("index/ticker:"+tradeDetailOKBeans.get(i).getUnderlying_index()+"-"+tradeDetailOKBeans.get(i).getQuote_currency());
                                    }else if (tradeDetailBean.getCurrencyPairName().contains("季度")&&tradeDetailOKBeans.get(i).getAlias().equals("quarter"))
                                    {
                                        tradeDetailBean.setKx(tradeDetailBean.getDelivery());
                                        tradeDetailBean.setDelivery("futures/depth5:"+tradeDetailOKBeans.get(i).getInstrument_id());
                                        tradeDetailBean.setCurrency("futures/ticker:"+tradeDetailOKBeans.get(i).getInstrument_id());
                                        tradeDetailBean.setIndex("index/ticker:"+tradeDetailOKBeans.get(i).getUnderlying_index()+"-"+tradeDetailOKBeans.get(i).getQuote_currency());
                                    }
                                }
                            }
                            setChildTradeDetail();
                            setPositionCoin();
                            selectExchCoinPopu.dismiss();
                        }
                    });
        } else {
            addRequest(binder.tradeall(TradeExchangeCache, this));
            addRequest(binder.tradedetail(TradeExchangeCache, TradeIdCache, this));
        }
    }
}
