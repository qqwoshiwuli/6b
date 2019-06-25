package com.sixbexchange.mvp.fragment.transaction.huobi;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
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
import com.fivefivelike.mybaselibrary.utils.StringUtil;
import com.fivefivelike.mybaselibrary.utils.ToastUtil;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.utils.logger.KLog;
import com.fivefivelike.mybaselibrary.view.dialog.ResultDialog;
import com.sixbexchange.R;
import com.sixbexchange.adapter.HuobiorderhistAdapter;
import com.sixbexchange.adapter.TrBMOrdersAdapter;
import com.sixbexchange.adapter.WsTrBMOrdersAdapter;
import com.sixbexchange.base.AppConst;
import com.sixbexchange.entity.bean.CoinToCoinDetailBean;
import com.sixbexchange.entity.bean.DepthBean;
import com.sixbexchange.entity.bean.HuobiOrListBean;
import com.sixbexchange.entity.bean.OrderBean;
import com.sixbexchange.entity.bean.TradeDetailBean;
import com.sixbexchange.entity.bean.WsOrderBean;
import com.sixbexchange.entity.bean.selectExchhuobiBeans;
import com.sixbexchange.mvp.databinder.BMTrFragmentBinder;
import com.sixbexchange.mvp.databinder.OkexTrBinder;
import com.sixbexchange.mvp.databinder.OneTokenBinder;
import com.sixbexchange.mvp.delegate.BMTrFragmentDelegate;
import com.sixbexchange.mvp.delegate.OkexTrDelegate;
import com.sixbexchange.mvp.delegate.OneTokenDelegate;
import com.sixbexchange.mvp.dialog.LevelDialog;
import com.sixbexchange.mvp.fragment.CoinInfoFragment;
import com.sixbexchange.mvp.fragment.MainFragment;
import com.sixbexchange.mvp.fragment.transaction.bitmex.BMTrFragment;
import com.sixbexchange.mvp.fragment.transaction.bitmex.BMTrSelectTypeFragment;
import com.sixbexchange.mvp.fragment.transaction.okex.OkexTrOpenFragment;
import com.sixbexchange.mvp.fragment.transaction.okex.OkexTrParentsFragment;
import com.sixbexchange.utils.UserSet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class OneTokenFragment extends BaseDataBindFragment<OneTokenDelegate, OneTokenBinder> {

    @Override
    protected Class<OneTokenDelegate> getDelegateClass() {
        return OneTokenDelegate.class;
    }

    @Override
    public OneTokenBinder getDataBinder(OneTokenDelegate viewDelegate) {
        return new OneTokenBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.initWs(0);
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        viewDelegate.initDepth(new ArrayList<DepthBean>(), new ArrayList<DepthBean>());
        ((OneTokenParentsFrgment) getParentFragment()).tradeOnRefreshData(true);
        viewDelegate.viewHolder.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((OneTokenParentsFrgment) getParentFragment()).tradeOnRefreshData(true);
            }
        });
        viewDelegate.viewHolder.reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(viewDelegate.viewHolder.tv_order_num.getText()))
                {
                    viewDelegate.viewHolder.tv_order_num.setText("0");
                }
                else
                {
                    double temp=Double.parseDouble(viewDelegate.viewHolder.tv_order_num.getText().toString());
                    temp--;
                    if (0>temp)
                    {
                        viewDelegate.viewHolder.tv_order_num.setText("0");
                    }else
                    {
                        viewDelegate.viewHolder.tv_order_num.setText(""+temp);
                    }
                }
            }
        });
        viewDelegate.viewHolder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(viewDelegate.viewHolder.tv_order_num.getText()))
                {
                    viewDelegate.viewHolder.tv_order_num.setText("0");
                }
                else
                {
                    double temp=Double.parseDouble(viewDelegate.viewHolder.tv_order_num.getText().toString());
                    temp++;
                    viewDelegate.viewHolder.tv_order_num.setText(""+temp);
                }
            }
        });
        viewDelegate.viewHolder.tv_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(viewDelegate.viewHolder.tv_order_num.getText().toString()))
                {
                    String amount=viewDelegate.viewHolder.tv_order_num.getText().toString();
                    String precision=huobiDataBean.getPricePrecision()+"";;
                    String symbol=huobiDataBean.getBaseCurrency()+"_"+huobiDataBean.getQuoteCurrency();
                    String type=viewDelegate.isbuy?"1":"2";
//                    Log.i("wuli","precision="+precision);
//                    Log.i("wuli","amount="+amount);
//                    Log.i("wuli","symbol="+symbol);
//                    Log.i("wuli","type="+type);
                    addRequest(binder.buy_order(amount,precision,symbol,type,OneTokenFragment.this));
                }
            }
        });
        //选择交易对
        viewDelegate.viewHolder.tv_order_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OneTokenParentsFrgment) getParentFragment()).showpopwindows(v);
            }
        });
//        viewDelegate.viewHolder.tv_buy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (viewDelegate.isLimit) {
//                    binder.checkOrder(viewDelegate.tradeMode == 0 ? 1 : 4, OneTokenFragment.this);
//                } else {
//                    binder.checkOrderStop(viewDelegate.tradeMode == 0 ? 1 : 4, OneTokenFragment.this);
//                }
//            }
//        });
//        viewDelegate.viewHolder.tv_sell.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (viewDelegate.isLimit) {
//                    binder.checkOrder(viewDelegate.tradeMode == 0 ? 2 : 3, OneTokenFragment.this);
//                } else {
//                    binder.checkOrderStop(viewDelegate.tradeMode == 0 ? 2 : 3, OneTokenFragment.this);
//                }
//            }
//        });
//        viewDelegate.viewHolder.tv_limit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewDelegate.tradeTypeChange(true);
//            }
//        });
//        viewDelegate.viewHolder.tv_trigger.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                viewDelegate.tradeTypeChange(false);
//                addRequest(binder.orderremark(OneTokenFragment.this));
//            }
//        });
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        viewDelegate.setVisibility(true);
        viewDelegate.sendWs(true);
        if (huobiDataBean != null) {
            onRefreshData(true);
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        viewDelegate.setVisibility(false);
    }
    selectExchhuobiBeans.HuobiDataBean huobiDataBean;
    public void initTradeDetail(selectExchhuobiBeans.HuobiDataBean huobiDataBean) {
        this.huobiDataBean=huobiDataBean;
        viewDelegate.sendWs(false);
        viewDelegate.setTradeDetailBean(huobiDataBean);
        onRefreshData(true);
        //跳转k线
        viewDelegate.viewHolder.lin_to_kline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getParentFragment().getParentFragment().getParentFragment() instanceof MainFragment) {
                    ((MainFragment) getParentFragment().getParentFragment().getParentFragment()).startBrotherFragment(
                            CoinInfoFragment.newInstance(
                                    "huobip/"+huobiDataBean.getBaseCurrency()+"."+huobiDataBean.getQuoteCurrency(),
                                    huobiDataBean.getBaseCurrency()+"/"+huobiDataBean.getQuoteCurrency()
                            )
                    );
                }
            }
        });
    }


    public void onRefreshData(boolean isRefreshing) {
//        if (viewDelegate.tradeDetailBean != null) {
         viewDelegate.viewHolder.swipeRefreshLayout.setRefreshing(isRefreshing);
        addRequest(binder.ourdetailCoin(OneTokenFragment.this));
//        Log.i("wuli",huobiDataBean.getBaseCurrency()+"."+huobiDataBean.getQuoteCurrency());
        addRequest(binder.coinhistory(
                huobiDataBean.getBaseCurrency()+huobiDataBean.getQuoteCurrency(),
                "1",
                this));
//            addRequest(binder.accountopen(viewDelegate.tradeDetailBean.getExchange(),
//                    viewDelegate.tradeDetailBean.getOnlykey(), this));
//        }
    }

    @Override
    public void onStopNet(int requestCode, BaseDataBind.StopNetMode type) {
        super.onStopNet(requestCode, type);
        viewDelegate.viewHolder.swipeRefreshLayout.setRefreshing(false);
    }

    private CoinToCoinDetailBean coinToCoinDetailBean;
    List<HuobiOrListBean> huobiOrListBean;
    HuobiorderhistAdapter huobiorderhistAdapter;
    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x125:
                huobiOrListBean=GsonUtil.getInstance().toList(data, HuobiOrListBean.class);
                huobiorderhistAdapter=new HuobiorderhistAdapter(getContext(),huobiOrListBean,huobiDataBean.getBaseCurrency().toUpperCase(),huobiDataBean.getQuoteCurrency().toUpperCase(),huobiDataBean.getPricePrecision());
                viewDelegate.viewHolder.listview.setAdapter(huobiorderhistAdapter);
                if (huobiOrListBean==null&&huobiOrListBean.size()==0)
                {
                    viewDelegate.viewHolder.lin_no_order.setVisibility(View.VISIBLE);
                }else
                {
                    viewDelegate.viewHolder.lin_no_order.setVisibility(View.GONE);
                }
                break;
            case 0x124:
                onRefreshData(true);
//                addRequest(binder.ourdetailCoin(OneTokenFragment.this));
                break;
            case 0x123:
                coinToCoinDetailBean = GsonUtil.getInstance().toObj(data, CoinToCoinDetailBean.class);
                viewDelegate.showCoin( coinToCoinDetailBean);
                break;
//            case 0x125:
//                //下单
//                ((OkexTrParentsFragment) getParentFragment()).tradeOnRefreshData(false);
//                break;
//            case 0x126:
//                //撤单
//                ((OkexTrParentsFragment) getParentFragment()).tradeOnRefreshData(false);
//                break;
//            case 0x128:
//                viewDelegate.showTriggerDialog(data);
//                break;
        }
    }

}
