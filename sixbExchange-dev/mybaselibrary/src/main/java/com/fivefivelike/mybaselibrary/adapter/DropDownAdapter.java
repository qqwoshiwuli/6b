package com.fivefivelike.mybaselibrary.adapter;

import android.content.Context;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.R;
import com.fivefivelike.mybaselibrary.base.BaseAdapter;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by 郭青枫 on 2018/7/16 0016.
 */

public class DropDownAdapter extends BaseAdapter<String> {


    public DropDownAdapter(Context context, List<String> datas) {
        super(context, R.layout.adapter_drop_down_item, datas);
    }

    int select = -1;

    public void setSelect(int select) {
        int old = this.select;
        this.select = select;
        notifyItemChanged(old);
        notifyItemChanged(select);
    }

    @Override
    protected void convert(ViewHolder holder, String s, final int position) {
        TextView tv_item;
        tv_item = holder.getView(R.id.tv_item);
        int left = tv_item.getLeft();
        int right = tv_item.getRight();
        //tv_item.setLayoutParams(new ConstraintLayout.LayoutParams(itemWidth - left - right, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv_item.setText(s);
        tv_item.setBackground(null);

        if (select == position) {
            tv_item.setTextColor(CommonUtils.getColor(R.color.mark_color));
        } else {
            tv_item.setTextColor(CommonUtils.getColor(R.color.color_font2));
        }

    }
}