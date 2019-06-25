package com.sixbexchange.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.CacheUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.circledialog.CircleDialogHelper;
import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.ListUtils;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.utils.glide.GlideUtils;
import com.fivefivelike.mybaselibrary.view.DropDownPopu;
import com.fivefivelike.mybaselibrary.view.InnerPagerAdapter;
import com.sixbexchange.R;
import com.sixbexchange.base.CacheName;
import com.sixbexchange.entity.bean.ExchCurrencyPairBean;
import com.sixbexchange.entity.bean.ExchSelectPositionBean;
import com.sixbexchange.entity.bean.ExchangeListBean;
import com.sixbexchange.entity.bean.TradeDetailBean;
import com.sixbexchange.entity.bean.TradeDetailOKBean;
import com.sixbexchange.mvp.databinder.TransactionBinder;
import com.sixbexchange.mvp.delegate.TransactionDelegate;
import com.sixbexchange.mvp.dialog.LevelDialog;
import com.sixbexchange.mvp.popu.SelectCurrencyPairPopu;
import com.sixbexchange.mvp.popu.SelectExchPopu;
import com.tablayout.TabEntity;
import com.tablayout.listener.CustomTabEntity;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

public class TransactionFragment extends BaseDataBindFragment<TransactionDelegate, TransactionBinder> {

    @Override
    protected Class<TransactionDelegate> getDelegateClass() {
        return TransactionDelegate.class;
    }

    @Override
    public TransactionBinder getDataBinder(TransactionDelegate viewDelegate) {
        return new TransactionBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initToolbar(new ToolbarBuilder().setTitle("").setShowBack(false));
        viewDelegate.getmToolbarTitle().setVisibility(View.GONE);
    }

    String TradeExchangeCache;
    String TradeIdCache;

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initTablelayout();
        TradeExchangeCache = CacheUtils.getInstance().getString(CacheName.TradeExchangeCache);
        TradeIdCache = CacheUtils.getInstance().getString(CacheName.TradeIdCache);

        addRequest(binder.exchangeList(this));
        addRequest(binder.tradelist(TradeExchangeCache, this));
        addRequest(binder.tradelistok(this));
    }

    ArrayList fragments;
    InnerPagerAdapter innerPagerAdapter;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    TrTransactionFragment trTransactionFragment;
    TrOrderFragment trOrderFragment;
    TrPositionFragment trPositionFragment;
    int showFragmentPosition = 0;

    private void initTablelayout() {
        fragments = new ArrayList<>();
        fragments.add(trTransactionFragment = new TrTransactionFragment());
        fragments.add(trOrderFragment = new TrOrderFragment());
        fragments.add(trPositionFragment = new TrPositionFragment());
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

    List<ExchCurrencyPairBean> selectLists;
    TradeDetailBean tradeDetailBean;
    List<ExchSelectPositionBean> exchCoins;

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                //交易所列表
                initList(data);
                break;
            case 0x124:
                //交易对信息
                tradeDetailBean = GsonUtil.getInstance().toObj(data, TradeDetailBean.class);
                for (int i = 0; i < tradeDetailOKBeans.size(); i++) {
                    if (tradeDetailBean.getCurrency().equals(tradeDetailOKBeans.get(i).getUnderlying_index()))
                    {
                        if (tradeDetailBean.getCurrencyPairName().contains("当周")&&tradeDetailOKBeans.get(i).getAlias().equals("this_week"))
                        {
                            tradeDetailBean.setDelivery("futures/depth5:"+tradeDetailOKBeans.get(i).getInstrument_id());
                            tradeDetailBean.setCurrency("futures/depth5:"+tradeDetailOKBeans.get(i));
                            tradeDetailBean.setIndex("index/ticker:"+tradeDetailOKBeans.get(i).getUnderlying_index()+"-"+tradeDetailOKBeans.get(i).getQuote_currency());
                        }else if (tradeDetailBean.getCurrencyPairName().contains("次周")&&tradeDetailOKBeans.get(i).getAlias().equals("next_week"))
                        {
                            tradeDetailBean.setDelivery("futures/depth5:"+tradeDetailOKBeans.get(i).getInstrument_id());
                            tradeDetailBean.setCurrency("futures/depth5:"+tradeDetailOKBeans.get(i));
                            tradeDetailBean.setIndex("index/ticker:"+tradeDetailOKBeans.get(i).getUnderlying_index()+"-"+tradeDetailOKBeans.get(i).getQuote_currency());
                        }else if (tradeDetailBean.getCurrencyPairName().contains("季度")&&tradeDetailOKBeans.get(i).getAlias().equals("quarter"))
                        {
                            tradeDetailBean.setDelivery("futures/depth5:"+tradeDetailOKBeans.get(i).getInstrument_id());
                            tradeDetailBean.setCurrency("futures/depth5:"+tradeDetailOKBeans.get(i));
                            tradeDetailBean.setIndex("index/ticker:"+tradeDetailOKBeans.get(i).getUnderlying_index()+"-"+tradeDetailOKBeans.get(i).getQuote_currency());
                        }
                    }
                }
                //请求交易所下币种列表
                addRequest(binder.tradeCoins(tradeDetailBean.getExchange(), this));
                //分配交易对信息
                setChildTradeDetail();
                break;
            case 0x125:
                //交易对列表
                selectLists = GsonUtil.getInstance().toList(data, ExchCurrencyPairBean.class);
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
                //修改杠杆
                addRequest(binder.getLeverage(
                        tradeDetailBean.getExchange(),
                        tradeDetailBean.getCurrencyPair(),
                        this
                ));
                initTrTransaction();
                break;
            case 0x128:
                //获取杠杆
                leverage = GsonUtil.getInstance().getValue(data, "leverage");
                viewDelegate.viewHolder.tv_level.setText(
                        (UiHeplUtils.isDouble(leverage) ?
                                leverage + "x" : leverage) + " " +
                                CommonUtils.getString(R.string.ic_Down)
                );
                break;
            case  0x129:
                tradeDetailOKBeans = GsonUtil.getInstance().toList(data, TradeDetailOKBean.class);
                addRequest(binder.tradedetail(TradeExchangeCache, TradeIdCache, this));
                break;
        }
    }
    List<TradeDetailOKBean> tradeDetailOKBeans;

    public void initTrTransaction() {
        if (trTransactionFragment != null) {
            trTransactionFragment.initTradeDetail(tradeDetailBean);
        }
    }

    public void getLeverage() {
        if (tradeDetailBean.getLeverageAvailable() == 1) {
            addRequest(binder.getLeverage(
                    tradeDetailBean.getExchange(),
                    tradeDetailBean.getCurrencyPair(),
                    this
            ));
        }
    }

    String leverage;
    LevelDialog levelDialog;


    //分配交易对信息
    private void setChildTradeDetail() {
        if (tradeDetailBean.getLeverageAvailable() == 1) {
            //可以修改杠杆 并从网络获取当前杠杆
            viewDelegate.viewHolder.tv_level.setText(tradeDetailBean.getTextName() + " " +
                    CommonUtils.getString(R.string.ic_Down)
            );
            viewDelegate.viewHolder.tv_level.setEnabled(true);
            getLeverage();
        } else {
            viewDelegate.viewHolder.tv_level.setEnabled(false);
            viewDelegate.viewHolder.tv_level.setText(tradeDetailBean.getTextName());
        }
        viewDelegate.viewHolder.tv_level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (levelDialog == null) {
                    levelDialog = new LevelDialog(getActivity(), new DefaultClickLinsener() {
                        @Override
                        public void onClick(View view, int position, Object item) {
                            addRequest(binder.changeLeverage(
                                    tradeDetailBean.getExchange(),
                                    tradeDetailBean.getOnlykey(),
                                    ((String) item).replace("x", ""),
                                    TransactionFragment.this
                            ));
                        }
                    });
                }
                levelDialog.showDilaog(
                        leverage);
            }
        });
        trTransactionFragment.initTradeDetail(tradeDetailBean);
        trOrderFragment.initTradeDetail(tradeDetailBean);
    }

    //分配持仓页分类信息
    private void setPositionCoin() {
        if (!ListUtils.isEmpty(exchCoins)) {
            for (int i = 0; i < exchCoins.size(); i++) {
                if (ObjectUtils.equals(
                        tradeDetailBean.getPositionText(),
                        exchCoins.get(i).getCurrency())) {
                    trPositionFragment.initTradeDetail(exchCoins.get(i), tradeDetailBean);
                }
            }
        }
    }

    SelectCurrencyPairPopu selectCurrencyPairPopu;

    //选择交易对下拉框
    public void showSelectCurrencyPair(View view) {
        if (tradeDetailBean != null && !ListUtils.isEmpty(selectLists)) {
            if (selectCurrencyPairPopu == null) {
                selectCurrencyPairPopu = new SelectCurrencyPairPopu(getActivity());
            }
            selectCurrencyPairPopu.showList(
                    selectLists,
                    tradeDetailBean,
                    view,
                    new DefaultClickLinsener() {
                        @Override
                        public void onClick(View view, int position, Object item) {
                            tradeDetailBean = selectLists
                                    .get((int) item).getList().get(position);
                            setChildTradeDetail();
                            setPositionCoin();
                            selectCurrencyPairPopu.dismiss();
                        }
                    });
        } else {
            addRequest(binder.tradelist(TradeExchangeCache, this));
            addRequest(binder.tradedetail(TradeExchangeCache, TradeIdCache, this));
        }
    }

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
                        for (int i = 0; i < selectLists.size(); i++) {
                            for (int j = 0; j < selectLists.get(i).getList().size(); j++) {
                                if (StringUtils.equalsIgnoreCase(
                                        exchCoins.get(position).getCurrencyPair(),
                                        selectLists.get(i).getList().get(j).getCurrencyPair())) {
                                    tradeDetailBean = selectLists.get(i).getList().get(j);
                                    setChildTradeDetail();
                                    trPositionFragment.initTradeDetail(exchCoins.get(position),
                                            tradeDetailBean);
                                    return;
                                } else if (TextUtils.isEmpty(exchCoins.get(position).getCurrencyPair())) {
                                    trPositionFragment.initTradeDetail(exchCoins.get(position),
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

    List<ExchangeListBean> list;
    SelectExchPopu selectExchPopu;
    int position = 0;

    private void initList(String data) {
        list = GsonUtil.getInstance().toList(data, ExchangeListBean.class);
        for (int i = 0; i < list.size(); i++) {
            if (ObjectUtils.equals(TradeExchangeCache, list.get(i).getCode())) {
                position = i;
            }
        }
        GlideUtils.loadImage(list.get(position).getExchangeImg(), viewDelegate.viewHolder.iv_exch);
        viewDelegate.viewHolder.tv_name.setText(list.get(position).getName());

        viewDelegate.viewHolder.lin_exch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectExchPopu == null) {
                    selectExchPopu = new SelectExchPopu(getActivity(), position);
                }
                selectExchPopu.showList(list, viewDelegate.viewHolder.lin_exch, new DefaultClickLinsener() {
                    @Override
                    public void onClick(View view, int position, Object item) {
                        if (view.getId() == R.id.tv_help) {
                            CircleDialogHelper.initDefaultDialog(getActivity(),
                                    list.get(position).getDiscountInfo(), null
                            ).show();
                        } else {
                            //选择交易所
                            CacheUtils.getInstance().put(CacheName.TradeExchangeCache, list.get(position).getCode());
                            CacheUtils.getInstance().put(CacheName.TradeIdCache, "");

                            TradeExchangeCache = list.get(position).getCode();
                            TradeIdCache = "";

                            GlideUtils.loadImage(list.get(position).getExchangeImg(), viewDelegate.viewHolder.iv_exch);
                            viewDelegate.viewHolder.tv_name.setText(list.get(position).getName());

                            //重新请求交易对信息
                            addRequest(binder.tradelist(TradeExchangeCache, TransactionFragment.this));
                            addRequest(binder.tradedetail(TradeExchangeCache, TradeIdCache, TransactionFragment.this));

                        }
                        selectExchPopu.dismiss();
                    }
                });
            }
        });
    }

}
