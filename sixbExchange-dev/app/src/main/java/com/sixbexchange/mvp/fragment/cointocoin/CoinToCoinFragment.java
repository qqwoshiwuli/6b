package com.sixbexchange.mvp.fragment.cointocoin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.sixbexchange.entity.bean.CoinToCoinBean;
import com.sixbexchange.entity.bean.CoinToCoinDetailBean;
import com.sixbexchange.entity.bean.DepthBean;
import com.sixbexchange.mvp.databinder.CoinToCoinBinder;
import com.sixbexchange.mvp.delegate.CoinToCoinDelegate;
import com.sixbexchange.mvp.popu.SelectCoinToCoinPopu;

import java.util.ArrayList;
import java.util.List;

/*
 *闪对
 * @author zze
 * @Description
 * @Date  2019/5/20
 * @Param
 * @return
 **/
public class CoinToCoinFragment extends BaseDataBindFragment<CoinToCoinDelegate, CoinToCoinBinder> {

    @Override
    protected Class<CoinToCoinDelegate> getDelegateClass() {
        return CoinToCoinDelegate.class;
    }

    @Override
    public CoinToCoinBinder getDataBinder(CoinToCoinDelegate viewDelegate) {
        return new CoinToCoinBinder(viewDelegate);
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initToolbar(new ToolbarBuilder().setTitle("闪兑版本 Beta").setShowBack(false));
        viewDelegate.initWs();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        viewDelegate.initDepth(new ArrayList<DepthBean>(), new ArrayList<DepthBean>());

        viewDelegate.viewHolder.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                coinOnRefreshData();
            }
        });
        viewDelegate.viewHolder.tv_order_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectCoinToCoin(v);
            }
        });
        coinOnRefreshData();
        initData();
        viewDelegate.sendWs(false);
    }

    public void coinOnRefreshData() {
        if (coinToCoinBeanList != null) {
            viewDelegate.viewHolder.swipeRefreshLayout.setRefreshing(true);
            addRequest(binder.coinhistory(
                    coinToCoinBeanList.get(leftPos).getData().get(rightPos).getSymbol(),
                    "1",
                    this));
        }else {
            addRequest(binder.orderremarkCoin(this));
            addRequest(binder.ourdetailCoin(this));
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        viewDelegate.sendWs(true);
        viewDelegate.setVisibility(true);
        initData();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        viewDelegate.setVisibility(false);
    }

    @Override
    public void onStopNet(int requestCode, BaseDataBind.StopNetMode type) {
        super.onStopNet(requestCode, type);
        viewDelegate.viewHolder.swipeRefreshLayout.setRefreshing(false);
    }

    private SelectCoinToCoinPopu selectCoinToCoinPopu;
    private List<CoinToCoinBean> coinToCoinBeanList;
    private CoinToCoinDetailBean coinToCoinDetailBean;
    private int leftPos = 0;
    private int leftPosCu = 0;
    private int rightPos = 0;

    //选择闪兑下拉框
    public void showSelectCoinToCoin(View view) {
        if (coinToCoinBeanList != null) {
            if (selectCoinToCoinPopu == null) {
                selectCoinToCoinPopu = new SelectCoinToCoinPopu(getActivity());
            }
            selectCoinToCoinPopu.showList(
                    coinToCoinBeanList,
                    leftPos,
                    rightPos,
                    view,
                    new DefaultClickLinsener() {
                        @Override
                        public void onClick(View view, int position, Object item) {
                            leftPosCu = position;
                            selectCoinToCoinPopu.initLeft(leftPosCu);
                            selectCoinToCoinPopu.initRight(leftPosCu, 0);
                        }
                    },
                    new DefaultClickLinsener() {
                        @Override
                        public void onClick(View view, int position, Object item) {
                            leftPos = leftPosCu;
                            rightPos = position;
                            initData();
                            selectCoinToCoinPopu.dismiss();
                        }
                    });
        } else {
            addRequest(binder.orderremarkCoin(this));
        }
    }

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                coinToCoinBeanList = GsonUtil.getInstance().toList(data, CoinToCoinBean.class);
                break;
            case 0x124:
                coinToCoinDetailBean = GsonUtil.getInstance().toObj(data, CoinToCoinDetailBean.class);
                initData();
                break;
        }
    }

    private void initData() {
        if (coinToCoinBeanList != null) {
            viewDelegate.setCoinDetailBean(coinToCoinBeanList, coinToCoinDetailBean, leftPos, rightPos);
        }
    }
}
