package com.sixbexchange.mvp.fragment.transaction.bitmex;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.CacheUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.ListUtils;
import com.fivefivelike.mybaselibrary.view.DropDownPopu;
import com.fivefivelike.mybaselibrary.view.InnerPagerAdapter;
import com.sixbexchange.R;
import com.sixbexchange.base.CacheName;
import com.sixbexchange.entity.bean.ExchSelectPositionBean;
import com.sixbexchange.entity.bean.SelectExchCoinBean;
import com.sixbexchange.entity.bean.TradeDetailBean;
import com.sixbexchange.mvp.databinder.ExchTrParentsBinder;
import com.sixbexchange.mvp.delegate.ExchTrParentsDelegate;
import com.tablayout.TabEntity;
import com.tablayout.listener.CustomTabEntity;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

public class BMTrParentsFragment extends BaseDataBindFragment<ExchTrParentsDelegate, ExchTrParentsBinder> {

    @Override
    protected Class<ExchTrParentsDelegate> getDelegateClass() {
        return ExchTrParentsDelegate.class;
    }

    @Override
    public ExchTrParentsBinder getDataBinder(ExchTrParentsDelegate viewDelegate) {
        return new ExchTrParentsBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();

    }


    String TradeIdCache;
    String TradeExchangeCache = "bitmex";

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initTablelayout();
        TradeIdCache = CacheUtils.getInstance().getString(CacheName.TradeIdBMCache);
        addRequest(binder.tradeall(TradeExchangeCache, this));
        addRequest(binder.tradedetail(TradeExchangeCache, TradeIdCache, this));

    }

    ArrayList fragments;
    InnerPagerAdapter innerPagerAdapter;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    int showFragmentPosition = 0;
    BMTrFragment bmTrFragment;
    BMTrOrderFragment bmTrOrderFragment;
    BMTrPositionFragment bmTrPositionFragment;

    private void initTablelayout() {
        fragments = new ArrayList<>();
        fragments.add(bmTrFragment = new BMTrFragment());
        fragments.add(bmTrOrderFragment = new BMTrOrderFragment());
        fragments.add(bmTrPositionFragment = new BMTrPositionFragment());
        if (innerPagerAdapter == null) {
            String[] stringArray = CommonUtils.getStringArray(R.array.sa_select_transaction_title);
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
        viewDelegate.viewHolder.tl_2.setCurrentTab(showFragmentPosition);
        viewDelegate.viewHolder.vp_root.setCurrentItem(showFragmentPosition);

    }

    //分配交易对信息
    private void setChildTradeDetail() {
        bmTrFragment.initTradeDetail(tradeDetailBean);
        bmTrOrderFragment.initTradeDetail(tradeDetailBean);
    }

    //分配持仓页分类信息
    private void setPositionCoin() {
        if (!ListUtils.isEmpty(exchCoins)) {
            for (int i = 0; i < exchCoins.size(); i++) {
                if (ObjectUtils.equals(
                        tradeDetailBean.getPositionText(),
                        exchCoins.get(i).getCurrency())) {
                    bmTrPositionFragment.initTradeDetail(
                            exchCoins.get(bmTrPositionFragment.getTradeDetailBean() == null ? 0 : i),
                            tradeDetailBean);
                }
            }
        }
    }

    TradeDetailBean tradeDetailBean;
    List<ExchSelectPositionBean> exchCoins;

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x124:
                //交易对信息
                tradeDetailBean = GsonUtil.getInstance().toObj(data, TradeDetailBean.class);
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

        }
    }

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
                                    bmTrPositionFragment.initTradeDetail(exchCoins.get(position),
                                            tradeDetailBean);
                                    return;
                                } else if (TextUtils.isEmpty(exchCoins.get(position).getCurrencyPair())) {
                                    bmTrPositionFragment.initTradeDetail(exchCoins.get(position),
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


    //选择交易对下拉框
    public void selectCurrencyPair(TradeDetailBean item) {
        tradeDetailBean = item;
        setChildTradeDetail();
        setPositionCoin();
    }

}
