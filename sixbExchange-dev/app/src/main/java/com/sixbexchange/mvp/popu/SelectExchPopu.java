package com.sixbexchange.mvp.popu;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.view.popupWindow.BasePopupWindow;
import com.sixbexchange.R;
import com.sixbexchange.adapter.SelectExchangeAdapter;
import com.sixbexchange.entity.bean.ExchangeListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郭青枫 on 2017/8/17.
 */

public class SelectExchPopu extends BasePopupWindow {


    private RecyclerView recycler_view;
    private SelectExchangeAdapter adapter;
    DefaultClickLinsener defaultClickLinsener;

    public SelectExchPopu(Context context, int position) {
        super(context);
        adapter.setSelectPosition(position);
        this.defaultClickLinsener = defaultClickLinsener;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_select_exchange;
    }

    @Override
    public void initView() {
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        recycler_view = findViewById(R.id.recycler_view);
        ArrayList list = new ArrayList<>();
        adapter = new SelectExchangeAdapter(context, list);


        recycler_view.setLayoutManager(new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recycler_view.setAdapter(adapter);

    }


    public void showList(List<ExchangeListBean> list, View view, DefaultClickLinsener defaultClickLinsener) {
        adapter.setDefaultClickLinsener(defaultClickLinsener);
        adapter.setData(list);
        showAsDropDown(view);
    }

}
