package com.sixbexchange.mvp.fragment.transaction.bitmex;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.entity.ResultDialogEntity;
import com.fivefivelike.mybaselibrary.http.HandlerHelper;
import com.fivefivelike.mybaselibrary.http.RequestCallback;
import com.fivefivelike.mybaselibrary.http.WebSocket3Request;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.ListUtils;
import com.fivefivelike.mybaselibrary.utils.ToastUtil;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.utils.logger.KLog;
import com.fivefivelike.mybaselibrary.view.dialog.ResultDialog;
import com.sixbexchange.R;
import com.sixbexchange.adapter.TrBMOrdersAdapter;
import com.sixbexchange.adapter.WsTrBMOrdersAdapter;
import com.sixbexchange.base.AppConst;
import com.sixbexchange.entity.bean.OrderBean;
import com.sixbexchange.entity.bean.TradeDetailBean;
import com.sixbexchange.entity.bean.WsOrderBean;
import com.sixbexchange.mvp.databinder.BMTrFragmentBinder;
import com.sixbexchange.mvp.delegate.BMTrFragmentDelegate;
import com.sixbexchange.mvp.dialog.LevelDialog;
import com.sixbexchange.mvp.fragment.CoinInfoFragment;
import com.sixbexchange.mvp.fragment.MainFragment;
import com.sixbexchange.utils.UserSet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BMTrFragment extends BaseDataBindFragment<BMTrFragmentDelegate, BMTrFragmentBinder> {

    @Override
    protected Class<BMTrFragmentDelegate> getDelegateClass() {
        return BMTrFragmentDelegate.class;
    }

    @Override
    public BMTrFragmentBinder getDataBinder(BMTrFragmentDelegate viewDelegate) {
        return new BMTrFragmentBinder(viewDelegate);
    }

    protected void closeDrawer() {
        viewDelegate.viewHolder.main_drawer_layout.closeDrawers();
    }

    protected void showDrawer() {
        viewDelegate.viewHolder.main_drawer_layout.openDrawer(Gravity.LEFT);
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.initWs();
        initWsAdapter();
        addRequest(binder.wsAuth(this));
        viewDelegate.viewHolder.tv_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen || (!isOpen && viewDelegate.stopLossType == 0)) {
                    checkOrder(isOpen ? 1 : 4,
                            BMTrFragment.this);
                } else if (viewDelegate.stopLossType == 2) {
                    binder.checkOrderStop(isOpen ? 1 : 4, BMTrFragment.this);
                } else {
                    binder.checkOrderStop(6, BMTrFragment.this);
                }
            }
        });
        viewDelegate.viewHolder.tv_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen || (!isOpen && viewDelegate.stopLossType == 0)) {
                    checkOrder(isOpen ? 2 : 3,
                            BMTrFragment.this);
                } else if (viewDelegate.stopLossType == 2) {
                    binder.checkOrderStop(isOpen ? 2 : 3, BMTrFragment.this);
                } else {
                    binder.checkOrderStop(5, BMTrFragment.this);
                }
            }
        });
    }

    private void initWsAdapter() {
        viewDelegate.wsTrBMOrdersAdapter = new WsTrBMOrdersAdapter(getActivity(), new ArrayList<WsOrderBean>());
        viewDelegate.wsTrBMOrdersAdapter.setDefaultClickLinsener(new DefaultClickLinsener() {
            @Override
            public void onClick(View view, int position, Object item) {
                //撤单
                addRequest(binder.cancelOrder(
                        viewDelegate.tradeDetailBean.getExchange(),
                        viewDelegate.wsTrBMOrdersAdapter.getDatas().get(position).getExchange_oid(),
                        BMTrFragment.this
                ));
            }
        });
        viewDelegate.viewHolder.recycler_view_bc.setLayoutManager(new LinearLayoutManager(this.getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        viewDelegate.viewHolder.recycler_view_bc.setAdapter(viewDelegate.wsTrBMOrdersAdapter);
    }

    public void checkOrder(int type, RequestCallback requestCallback) {
        if (TextUtils.isEmpty(viewDelegate.viewHolder.tv_order_price.getText().toString())) {
            ToastUtil.show("请输入价格");
            return;
        }
        if (TextUtils.isEmpty(viewDelegate.viewHolder.tv_order_num.getText().toString())) {
            ToastUtil.show("请输入数量");
            return;
        }
        if (!UiHeplUtils.isDouble(viewDelegate.viewHolder.tv_order_num.getText().toString())) {
            ToastUtil.show("请输入正确的数量");
            return;
        }
        if (!UiHeplUtils.isDouble(viewDelegate.viewHolder.tv_order_price.getText().toString()) &&
                !ObjectUtils.equals("对手价", viewDelegate.viewHolder.tv_order_price.getText().toString()) &&
                !ObjectUtils.equals("买一价", viewDelegate.viewHolder.tv_order_price.getText().toString()) &&
                !ObjectUtils.equals("卖一价", viewDelegate.viewHolder.tv_order_price.getText().toString())
                ) {
            ToastUtil.show("请输入正确的价格");
            return;
        }
        boolean isMarketPrice = ObjectUtils.equals("对手价",
                viewDelegate.viewHolder.tv_order_price.getText().toString());
        boolean isBuy = type == 1 || type == 4;

        String price = viewDelegate.viewHolder.tv_order_price.getText().toString();
        if (isMarketPrice) {
            price = viewDelegate.oldPrice;
        } else if (ObjectUtils.equals("买一价", viewDelegate.viewHolder.tv_order_price.getText().toString())) {
            price = getDepthPrice(false);//ListUtils.isEmpty(viewDelegate.bidsAdapter.getDatas()) ? "" : viewDelegate.bidsAdapter.getDatas().get(0).getPrice();
        } else if (ObjectUtils.equals("卖一价", viewDelegate.viewHolder.tv_order_price.getText().toString())) {
            price = getDepthPrice(true); //ListUtils.isEmpty(viewDelegate.asksAdapter.getDatas()) ? "" : viewDelegate.asksAdapter.getDatas().get(viewDelegate.depthSize - 1).getPrice();
        }
        if (TextUtils.isEmpty(price)) {
            ToastUtil.show("数据不完整,请尝试刷新");
            return;
        }
        if (!UiHeplUtils.isDouble(price)) {
            ToastUtil.show("价格数据异常");
            return;
        }
        addRequest(binder.placeOrder(
                viewDelegate.tradeDetailBean.getExchange(),
                isMarketPrice ? "1" : "0",
                price,
                type,
                viewDelegate.tradeDetailBean.getOnlykey(),
                viewDelegate.tradeDetailBean.getCurrencyPair(),
                viewDelegate.viewHolder.tv_order_num.getText().toString(),
                isBuy ? "b" : "s",
                requestCallback
        ));
    }

    private String getDepthPrice(boolean isAsks) {
        if (!ListUtils.isEmpty(viewDelegate.asksAdapter.getDatas()) && !ListUtils.isEmpty(viewDelegate.bidsAdapter.getDatas())) {
            return isAsks ?
                    viewDelegate.asksAdapter.getDatas().get(viewDelegate.depthSize - 1).getPrice() :
                    viewDelegate.bidsAdapter.getDatas().get(0).getPrice();
        }
        return "";
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        viewDelegate.setVisibility(true);
        viewDelegate.sendWs(true);
        if (viewDelegate.tradeDetailBean != null) {
            onRefreshData(false);
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        viewDelegate.setVisibility(false);
    }

    String leverage;
    LevelDialog levelDialog;
    boolean isOpen = true;

    public void initTradeDetail(TradeDetailBean s) {
        viewDelegate.sendWs(false);
        viewDelegate.setTradeDetailBean(s);
        initList(new ArrayList<OrderBean>());
        onRefreshData(true);
        //选择交易对
        viewDelegate.viewHolder.tv_order_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDrawer();
            }
        });
        //跳转k线
        viewDelegate.viewHolder.lin_to_kline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getParentFragment().getParentFragment().getParentFragment() instanceof MainFragment) {
                    ((MainFragment) getParentFragment().getParentFragment().getParentFragment()).startBrotherFragment(
                            CoinInfoFragment.newInstance(
                                    viewDelegate.tradeDetailBean.getDelivery(),
                                    viewDelegate.tradeDetailBean.getCurrencyPairName()
                            )
                    );
                }
            }
        });
        viewDelegate.viewHolder.tv_lever.setText("--");
        viewDelegate.viewHolder.lin_lever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (levelDialog == null) {
                    levelDialog = new LevelDialog(getActivity(), new DefaultClickLinsener() {
                        @Override
                        public void onClick(View view, int position, Object item) {
                            viewDelegate.viewHolder.tv_lever.setText(
                                    (UiHeplUtils.isDouble(((String) item).replace("x", "")) ?
                                            ((String) item).replace("x", "") + "x" : ((String) item).replace("x", ""))
                            );
                            addRequest(binder.changeLeverage(
                                    viewDelegate.tradeDetailBean.getExchange(),
                                    viewDelegate.tradeDetailBean.getOnlykey(),
                                    ((String) item).replace("x", ""),
                                    BMTrFragment.this
                            ));
                        }
                    });
                }
                levelDialog.showDilaog(
                        leverage);
            }
        });
        viewDelegate.viewHolder.tv_lever.setEnabled(true);
        addRequest(binder.getLeverage(
                viewDelegate.tradeDetailBean.getExchange(),
                viewDelegate.tradeDetailBean.getCurrencyPair(),
                this
        ));
        viewDelegate.sendWs(true);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        viewDelegate.viewHolder.tv_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpen = true;
                viewDelegate.changOpenOrClose(isOpen);
                KeyboardUtils.showSoftInput(getActivity());
            }
        });
        viewDelegate.viewHolder.tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpen = false;
                viewDelegate.changOpenOrClose(isOpen);
                KeyboardUtils.showSoftInput(getActivity());
            }
        });
        viewDelegate.changOpenOrClose(isOpen);
        viewDelegate.viewHolder.fl_opponent_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewDelegate.changeTrType(1, viewDelegate.viewHolder.tv_opponent_price.getText().toString());
            }
        });
        viewDelegate.viewHolder.fl_buy_one_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewDelegate.changeTrType(2, viewDelegate.viewHolder.tv_buy_one_price.getText().toString());
            }
        });
        viewDelegate.viewHolder.fl_sell_one_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewDelegate.changeTrType(3, viewDelegate.viewHolder.tv_sell_one_price.getText().toString());
            }
        });
        viewDelegate.viewHolder.swipeRefreshLayout.setOnRefreshListener(new android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshData(true);
            }
        });
        loadDrawerLayout(savedInstanceState == null);
    }

    public void onRefreshData(boolean isRefreshing) {
        if (viewDelegate.tradeDetailBean != null) {
            viewDelegate.viewHolder.swipeRefreshLayout.setRefreshing(isRefreshing);
            addRequest(binder.accountopen(viewDelegate.tradeDetailBean.getExchange(),
                    viewDelegate.tradeDetailBean.getOnlykey(), this));
            addRequest(binder.accountclose(viewDelegate.tradeDetailBean.getExchange(),
                    viewDelegate.tradeDetailBean.getOnlykey(), this));
            addRequest(binder.accountgetOrders(
                    viewDelegate.tradeDetailBean.getExchange(),
                    viewDelegate.tradeDetailBean.getCurrencyPair(),
                    this));
        }
    }

    @Override
    public void onStopNet(int requestCode, BaseDataBind.StopNetMode type) {
        super.onStopNet(requestCode, type);
        viewDelegate.viewHolder.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                //订单列表
                initList(GsonUtil.getInstance().toList(data, OrderBean.class));
                //订阅ws
                viewDelegate.sendWs(true);
                break;
            case 0x124:
                viewDelegate.transactionBean.setAvailableOpenMore(
                        GsonUtil.getInstance().getValue(data, "availableOpenMore", String.class));
                viewDelegate.transactionBean.setUsableOpenMore(
                        GsonUtil.getInstance().getValue(data, "usableOpenMore", String.class));
                viewDelegate.transactionBean.setAvailableOpenSpace(
                        GsonUtil.getInstance().getValue(data, "availableOpenSpace", String.class));
                viewDelegate.transactionBean.setUsableOpenSpace(
                        GsonUtil.getInstance().getValue(data, "usableOpenSpace", String.class));
                //订阅ws
                viewDelegate.sendWs(true);
                viewDelegate.changOpenOrClose(isOpen);
                break;
            case 0x127:
                viewDelegate.transactionBean.setAvailableflatSpace(
                        GsonUtil.getInstance().getValue(data, "availableflatSpace", String.class));
                viewDelegate.transactionBean.setAvailableflatMore(
                        GsonUtil.getInstance().getValue(data, "availableflatMore", String.class));
                //订阅ws
                viewDelegate.sendWs(true);
                viewDelegate.changOpenOrClose(isOpen);
                break;
            case 0x125:
                //下单
                onRefreshTwice();
                break;
            case 0x126:
                //撤单
                onRefreshTwice();
                break;
            case 0x129:
                //修改杠杆
                addRequest(binder.getLeverage(
                        viewDelegate.tradeDetailBean.getExchange(),
                        viewDelegate.tradeDetailBean.getCurrencyPair(),
                        this
                ));
                onRefreshData(true);
                break;
            case 0x128:
                //获取杠杆
                leverage = GsonUtil.getInstance().getValue(data, "leverage");
                viewDelegate.viewHolder.tv_lever.setText(
                        (UiHeplUtils.isDouble(leverage) ?
                                leverage + "x" : leverage)
                );
                break;
            case 0x131:
                UserSet.getinstance().setWsAuth(data);
                WebSocket3Request.getInstance().intiWebSocket(
                        AppConst.wsAddress3
                                + data,
                        viewDelegate.TAG,
                        new WebSocket3Request.WebSocket3CallBack() {
                            @Override
                            public void onDataSuccess(String name, String data, String info, int status) {
                                KLog.i(viewDelegate.TAG, data);
                                WsOrderBean wsOrderBean =
                                        GsonUtil.getInstance().toObj(
                                                GsonUtil.getInstance().getValue(data, "data")
                                                , WsOrderBean.class);
                                HandlerHelper.getinstance().put(wsOrderBean.getExchange_oid(),
                                        wsOrderBean);
                                if (ObjectUtils.equals("2", wsOrderBean.getState())) {
                                    onRefreshData(false);
                                }
                                String value = GsonUtil.getInstance().getValue(data, ResultDialog.DIALOG_KEY);
                                if (!TextUtils.isEmpty(value)) {
                                    ResultDialogEntity resultDialogEntity = ResultDialog.getInstence()
                                            .ShowResultDialog(getActivity(), value, BMTrFragment.this);
                                }
                            }

                            @Override
                            public void onDataError(String name, String data, String info, int status) {

                            }
                        });

                break;
        }
    }

    public void onRefreshTwice() {
        Observable.intervalRange(0, 2, 500, 1500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onTerminateDetach()
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        onRefreshData(false);
                    }
                });
    }

    TrBMOrdersAdapter adapter;

    private void initList(List<OrderBean> data) {
        if (adapter == null) {
            adapter = new TrBMOrdersAdapter(getActivity(), data);
            adapter.setDefaultClickLinsener(new DefaultClickLinsener() {
                @Override
                public void onClick(View view, int position, Object item) {
                    //撤单
                    addRequest(binder.cancelOrder(
                            viewDelegate.tradeDetailBean.getExchange(),
                            adapter.getDatas().get(position).getExchange_oid(),
                            BMTrFragment.this
                    ));
                }
            });
            viewDelegate.viewHolder.recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            viewDelegate.viewHolder.recycler_view.setAdapter(adapter);
        } else {
            adapter.setTradeDetailBean(viewDelegate.tradeDetailBean);
            adapter.setData(data);
            viewDelegate.viewHolder.lin_no_order.setVisibility(
                    ListUtils.isEmpty(data) ? View.VISIBLE : View.GONE
            );
            //            viewDelegate.viewHolder.lin_close_all.setVisibility(
            //                    !ListUtils.isEmpty(data) ? View.VISIBLE : View.GONE
            //            );
            //            viewDelegate.viewHolder.lin_close_all.setOnClickListener(new View.OnClickListener() {
            //                @Override
            //                public void onClick(View v) {
            //                    if (viewDelegate.tradeDetailBean != null) {
            //                        addRequest(binder.cancelAllOrder(
            //                                viewDelegate.tradeDetailBean.getExchange(),
            //                                viewDelegate.tradeDetailBean.getCurrencyPair(),
            //                                BMTrFragment.this
            //                        ));
            //                    }
            //                }
            //            });
        }
    }

    BMTrSelectTypeFragment trSelectTypeFragment;

    private void loadDrawerLayout(boolean isFirst) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (isFirst) {
            trSelectTypeFragment = new BMTrSelectTypeFragment();
            transaction.add(R.id.fl_left, trSelectTypeFragment, "TrSelectTypeFragment");
        } else {
            trSelectTypeFragment = (BMTrSelectTypeFragment) getChildFragmentManager()
                    .findFragmentByTag("TransacitionSetFragment");
            transaction.show(trSelectTypeFragment);
        }
        transaction.commitAllowingStateLoss();
        viewDelegate.viewHolder.main_drawer_layout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                if (drawerView.getId() == R.id.fl_left && trSelectTypeFragment != null) {
                    trSelectTypeFragment.onRefresh();
                }
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

}
