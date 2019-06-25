package com.fivefivelike.mybaselibrary.view.dialog;

import android.content.Context;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by 郭青枫 on 2018/1/10 0010.
 */

public class LogsAdapter extends CommonAdapter<String> {


    private TextView commentTv;

    public LogsAdapter(Context context, List<String> datas) {
        super(context, R.layout.item_comment, datas);
    }

    public void setDatas(List<String> datas){
        getDatas().clear();
        getDatas().addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    protected void convert(ViewHolder holder, String s, final int position) {
        commentTv = holder.getView(R.id.commentTv);
        commentTv.setText(s);
    }

}