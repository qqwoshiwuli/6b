package com.sixbexchange.mvp.fragment.transaction.okex;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;

import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.sixbexchange.entity.bean.DepthBean;
import com.sixbexchange.entity.bean.OrderBean;
import com.sixbexchange.entity.bean.TradeDetailBean;
import com.sixbexchange.mvp.databinder.OkexTrBinder;
import com.sixbexchange.mvp.delegate.OkexTrDelegate;
import com.sixbexchange.mvp.fragment.CoinInfoFragment;
import com.sixbexchange.mvp.fragment.MainFragment;

import java.util.ArrayList;
import java.util.List;

public class OkexTrOpenFragment extends BaseDataBindFragment<OkexTrDelegate, OkexTrBinder> {

    @Override
    protected Class<OkexTrDelegate> getDelegateClass() {
        return OkexTrDelegate.class;
    }

    @Override
    public OkexTrBinder getDataBinder(OkexTrDelegate viewDelegate) {
        return new OkexTrBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.initWs(0);
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        viewDelegate.tradeTypeChangeOpen(3);
        viewDelegate.initDepth(new ArrayList<DepthBean>(), new ArrayList<DepthBean>());
        viewDelegate.viewHolder.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((OkexTrParentsFragment) getParentFragment()).tradeOnRefreshData(true);
            }
        });
        initList(new ArrayList<OrderBean>());
        viewDelegate.viewHolder.tv_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewDelegate.isLimit) {
                    binder.checkOrder(viewDelegate.tradeMode == 0 ? 1 : 4, OkexTrOpenFragment.this,viewDelegate.transactionBean.getAvailableOpenMore());
                } else {
                    binder.checkOrderStop(viewDelegate.tradeMode == 0 ? 1 : 4, OkexTrOpenFragment.this);
                }
            }
        });
        viewDelegate.viewHolder.tv_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewDelegate.isLimit) {
                    binder.checkOrder(viewDelegate.tradeMode == 0 ? 2 : 3, OkexTrOpenFragment.this,viewDelegate.transactionBean.getAvailableOpenSpace());
                } else {
                    binder.checkOrderStop(viewDelegate.tradeMode == 0 ? 2 : 3, OkexTrOpenFragment.this);
                }
            }
        });
        viewDelegate.viewHolder.tv_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewDelegate.tradeTypeChange(true);
            }
        });
        viewDelegate.viewHolder.tv_trigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewDelegate.tradeTypeChange(false);
                addRequest(binder.orderremark(OkexTrOpenFragment.this));
            }
        });
        if (viewDelegate.tradeDetailBean != null) {
            initTradeDetail(viewDelegate.tradeDetailBean);
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        viewDelegate.setVisibility(true);
        viewDelegate.sendWs(true);
        if (viewDelegate.tradeDetailBean != null) {
            onRefreshData(true);
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        viewDelegate.setVisibility(false);
    }

    public void initTradeDetail(TradeDetailBean s) {
        viewDelegate.Percent(0);
        viewDelegate.sendWs(false);
        viewDelegate.setTradeDetailBean(s);
        initList(new ArrayList<OrderBean>());
        onRefreshData(true);
        //选择交易对
        viewDelegate.viewHolder.tv_order_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OkexTrParentsFragment) getParentFragment()).showSelectCurrencyPair(v);
            }
        });
        //跳转k线
        viewDelegate.viewHolder.lin_to_kline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i("wuli","getDelivery="+ viewDelegate.tradeDetailBean.getDelivery());
//                Log.i("wuli","getCurrencyPairName="+ viewDelegate.tradeDetailBean.getCurrencyPairName());

                if (getParentFragment().getParentFragment().getParentFragment() instanceof MainFragment) {
                    ((MainFragment) getParentFragment().getParentFragment().getParentFragment()).startBrotherFragment(
                            CoinInfoFragment.newInstance(
                                    viewDelegate.tradeDetailBean.getKx(),
                                    viewDelegate.tradeDetailBean.getCurrencyPairName()
                            )
                    );
                }
            }
        });
    }


    private void initList(List<OrderBean> data) {
        viewDelegate.initList(data);
        viewDelegate.viewHolder.lin_close_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //撤销全部
                if (viewDelegate.tradeDetailBean != null) {
                    addRequest(binder.cancelAllOrder(
                            viewDelegate.tradeDetailBean.getExchange(),
                            viewDelegate.tradeDetailBean.getCurrencyPair(),
                            OkexTrOpenFragment.this
                    ));
                }
            }
        });
        viewDelegate.adapter.setDefaultClickLinsener(new DefaultClickLinsener() {
            @Override
            public void onClick(View view, int position, Object item) {
                //撤单
                addRequest(binder.cancelOrder(
                        viewDelegate.tradeDetailBean.getExchange(),
                        viewDelegate.adapter.getDatas().get(position).getExchange_oid(),
                        OkexTrOpenFragment.this
                ));
            }
        });
    }

    public void onRefreshData(boolean isRefreshing) {
        if (viewDelegate.tradeDetailBean != null) {
            viewDelegate.Percent(0);
            viewDelegate.viewHolder.swipeRefreshLayout.setRefreshing(isRefreshing);
            addRequest(binder.accountopen(viewDelegate.tradeDetailBean.getExchange(),
                    viewDelegate.tradeDetailBean.getOnlykey(), this));
        }
    }

    @Override
    public void onStopNet(int requestCode, BaseDataBind.StopNetMode type) {
        super.onStopNet(requestCode, type);
        viewDelegate.viewHolder.swipeRefreshLayout.setRefreshing(false);
    }

    public void setOrderList(List<OrderBean> data) {
        //订单列表
        initList(data);
        //订阅ws
        viewDelegate.sendWs(true);
    }

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x124:
                viewDelegate.transactionBean.setAvailableOpenMore(
                        GsonUtil.getInstance().getValue(data, "availableOpenMore", String.class));
                viewDelegate.transactionBean.setUsableOpenMore(
                        GsonUtil.getInstance().getValue(data, "usableOpenMore", String.class));
                viewDelegate.transactionBean.setAvailableOpenSpace(
                        GsonUtil.getInstance().getValue(data, "availableOpenSpace", String.class));
                viewDelegate.transactionBean.setUsableOpenSpace(
                        GsonUtil.getInstance().getValue(data, "usableOpenSpace", String.class));
                viewDelegate.tradeModeChange(0);
                //订阅ws
                viewDelegate.sendWs(true);
                break;
            case 0x125:
                //下单
                ((OkexTrParentsFragment) getParentFragment()).tradeOnRefreshData(false);
                break;
            case 0x126:
                //撤单
                ((OkexTrParentsFragment) getParentFragment()).tradeOnRefreshData(false);
                break;
            case 0x128:
                viewDelegate.showTriggerDialog(data);
                break;
        }
    }

}
