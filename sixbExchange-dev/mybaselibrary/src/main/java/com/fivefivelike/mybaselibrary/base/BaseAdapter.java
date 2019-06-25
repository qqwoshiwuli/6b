package com.fivefivelike.mybaselibrary.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.fivefivelike.mybaselibrary.utils.ListUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郭青枫 on 2018/11/14 0014.
 */

public abstract class BaseAdapter<T> extends CommonAdapter<T> {

    public BaseAdapter(Context context, int layoutId, List<T> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public List<T> getDatas() {
        List<T> datas = super.getDatas();
        if (datas == null) {
            datas = new ArrayList<>();
            return datas;
        }
        return datas;
    }

    public void setData(List<T> data) {
        getDatas().clear();
        getDatas().addAll(data);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (ListUtils.isEmpty(getDatas())) {
            return 0;
        }
        int itemCount = super.getItemCount();
        return itemCount;
    }

    public boolean isHavePosition(int position) {
        return position < getItemCount() && position > -1;
    }

    @Override
    protected void setListener(final ViewGroup parent, final ViewHolder viewHolder, int viewType) {
        if (!isEnabled(viewType))
            return;
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    if (!isHavePosition(position) && position < getItemCount()) {
                        return;
                    }
                    mOnItemClickListener.onItemClick(v, viewHolder, position);
                }
            }
        });
        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    if (!isHavePosition(position) && position < getItemCount()) {
                        return false;
                    }
                    return mOnItemClickListener.onItemLongClick(v, viewHolder, position);
                }
                return false;
            }
        });
    }
}
