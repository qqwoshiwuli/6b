package com.sixbexchange.mvp.popu;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.view.popupWindow.BasePopupWindow;
import com.sixbexchange.R;
import com.sixbexchange.adapter.SelectExchCoinAdapter;
import com.sixbexchange.entity.bean.SelectExchCoinBean;
import com.sixbexchange.entity.bean.TradeDetailBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郭青枫 on 2017/8/17.
 */

public class SelectExchCoinPopu extends BasePopupWindow {


    private RecyclerView recycler_view;
    SelectExchCoinAdapter selectExchCoinAdapter;
    public SelectExchCoinPopu(Context context) {
        super(context);

    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_select_currency_pair;
    }

    @Override
    public void initView() {
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        recycler_view = findViewById(R.id.recycler_view);
        selectExchCoinAdapter=new SelectExchCoinAdapter(context,new ArrayList<SelectExchCoinBean>());
        recycler_view.setLayoutManager(new LinearLayoutManager(context));
        recycler_view.setAdapter(selectExchCoinAdapter);

    }


    List<SelectExchCoinBean> data;
    TradeDetailBean select;
    public void showList(
            List<SelectExchCoinBean> data, TradeDetailBean select,
            View view,
            DefaultClickLinsener defaultClickLinsener) {
        this.data = data;
        this.select = select;
        selectExchCoinAdapter.setDefaultClickLinsener(defaultClickLinsener);
        selectExchCoinAdapter.setType(select.getCurrencyPairName());

        selectExchCoinAdapter.getDatas().clear();
        selectExchCoinAdapter.getDatas().addAll(data);
        selectExchCoinAdapter.notifyDataSetChanged();
        showAsDropDown(view);
    }

}
