package com.sixbexchange.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.base.BaseAdapter;
import com.fivefivelike.mybaselibrary.utils.ListUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.utils.glide.GlideUtils;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.SelectExchCoinBean;
import com.sixbexchange.entity.bean.TradeDetailBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郭青枫 on 2018/7/16 0016.
 */

public class SelectCoinsAdapter extends BaseAdapter<SelectExchCoinBean> {


    private ImageView iv_pic;
    private TextView tv_name;
    private RecyclerView recycler_view;

    DefaultClickLinsener defaultClickLinsener;

    public void setDefaultClickLinsener(DefaultClickLinsener defaultClickLinsener) {
        this.defaultClickLinsener = defaultClickLinsener;
    }

    public SelectCoinsAdapter(Context context, List<SelectExchCoinBean> datas) {
        super(context, R.layout.adapter_select_coins, datas);

    }


    @Override
    protected void convert(ViewHolder holder, SelectExchCoinBean s, final int position) {
        iv_pic = holder.getView(R.id.iv_pic);
        tv_name = holder.getView(R.id.tv_name);
        recycler_view = holder.getView(R.id.recycler_view);

        tv_name.setText(s.getName());
        GlideUtils.loadImage(ListUtils.isEmpty(s.getList())?"--":s.getList().get(0).getCurrencyIcon(), iv_pic);
        SelectTrCoinAdapter adapter;

        if (recycler_view.getAdapter() == null) {
            List<TradeDetailBean> data=new ArrayList<>();
            data.addAll(s.getList());
            adapter = new SelectTrCoinAdapter(mContext, data);
            recycler_view.setLayoutManager(new LinearLayoutManager(mContext) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            recycler_view.setAdapter(adapter);
        } else {
            ((BaseAdapter) recycler_view.getAdapter()).setData(s.getList());
            adapter = ((SelectTrCoinAdapter) recycler_view.getAdapter());
        }
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int p) {
                if (defaultClickLinsener != null) {
                    defaultClickLinsener.onClick(view, position, p);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });


    }
}