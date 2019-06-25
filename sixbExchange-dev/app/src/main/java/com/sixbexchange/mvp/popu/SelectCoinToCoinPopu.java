package com.sixbexchange.mvp.popu;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.view.popupWindow.BasePopupWindow;
import com.sixbexchange.R;
import com.sixbexchange.adapter.SelectCoinLeftAdapter;
import com.sixbexchange.adapter.SelectCoinRightAdapter;
import com.sixbexchange.entity.bean.CoinToCoinBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zze on 2019/5/21.
 */

public class SelectCoinToCoinPopu extends BasePopupWindow {

    private RecyclerView recycler_view;
    private RecyclerView recycler_view2;
    private SelectCoinLeftAdapter leftAdapter;
    private SelectCoinRightAdapter rightAdapter;
    private List<CoinToCoinBean> coinToCoinBean;

    public SelectCoinToCoinPopu(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_select_cointocoin;
    }

    @Override
    public void initView() {
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view2 = findViewById(R.id.recycler_view2);
        ArrayList leftList = new ArrayList<>();
        ArrayList rightList = new ArrayList<>();
        leftAdapter = new SelectCoinLeftAdapter(context, leftList);
        recycler_view.setLayoutManager(new LinearLayoutManager(context));
        recycler_view.setAdapter(leftAdapter);
        rightAdapter=new SelectCoinRightAdapter(context,rightList);
        recycler_view2.setLayoutManager(new LinearLayoutManager(context));
        recycler_view2.setAdapter(rightAdapter);
    }

    public void initLeft(int leftPos) {
        leftAdapter.setSelectPosition(leftPos);
        leftAdapter.setData(coinToCoinBean);
    }

    public void initRight(int leftPos,int rightPos) {
        rightAdapter.setSelectPosition(rightPos);
        rightAdapter.setData(coinToCoinBean.get(leftPos).getData());
    }

    public void showList(
            List<CoinToCoinBean> coinToCoinBean,
            int leftPos,
            int rightPos,
            View view,
            DefaultClickLinsener leftClickLinsener,
            DefaultClickLinsener rightClickLinsener) {
        this.coinToCoinBean = coinToCoinBean;
        initLeft(leftPos);
        initRight(leftPos,rightPos);
        leftAdapter.setDefaultClickLinsener(leftClickLinsener);
        rightAdapter.setDefaultClickLinsener(rightClickLinsener);
        showAsDropDown(view);
    }
}
