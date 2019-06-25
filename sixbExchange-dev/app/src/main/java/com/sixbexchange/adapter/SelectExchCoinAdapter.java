package com.sixbexchange.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.base.BaseAdapter;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.SelectExchCoinBean;
import com.sixbexchange.entity.bean.TradeDetailBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郭青枫 on 2019/5/6 0006.
 */

public class SelectExchCoinAdapter extends BaseAdapter<SelectExchCoinBean> {


    DefaultClickLinsener defaultClickLinsener;
    private TextView tv_name;
    private RecyclerView recycler_view;
    String type;

    public void setType(String type) {
        this.type = type;
    }

    public void setDefaultClickLinsener(DefaultClickLinsener defaultClickLinsener) {
        this.defaultClickLinsener = defaultClickLinsener;
    }

    public SelectExchCoinAdapter(Context context, List<SelectExchCoinBean> datas) {
        super(context, R.layout.adapter_select_currency_pair, datas);

    }


    @Override
    protected void convert(ViewHolder holder, SelectExchCoinBean s, final int position) {
        tv_name = holder.getView(R.id.tv_name);
        recycler_view = holder.getView(R.id.recycler_view);

        tv_name.setText(s.getName());


        ExchCoinAdapter exchCoinAdapter = (ExchCoinAdapter) recycler_view.getAdapter();
        if (exchCoinAdapter == null) {
            exchCoinAdapter = new ExchCoinAdapter(mContext, new ArrayList<TradeDetailBean>());
            recycler_view.setLayoutManager(new GridLayoutManager(mContext, 3) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
        }
        exchCoinAdapter.setDefaultClickLinsener(new DefaultClickLinsener() {
            @Override
            public void onClick(View view, int p, Object item) {
                defaultClickLinsener.onClick(view,position,p);
            }
        });
        exchCoinAdapter.setType(type);
        exchCoinAdapter.setData(s.getList());
        recycler_view.setAdapter(exchCoinAdapter);


    }

}
