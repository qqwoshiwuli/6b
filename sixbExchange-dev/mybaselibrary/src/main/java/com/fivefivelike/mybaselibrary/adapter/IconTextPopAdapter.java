package com.fivefivelike.mybaselibrary.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.R;
import com.fivefivelike.mybaselibrary.view.popupWindow.IconTextPopWindow;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;


/**
 * Created by 郭青枫 on 2017/9/30.
 */

public class IconTextPopAdapter extends CommonAdapter<IconTextPopWindow.Entity> {


    private AppCompatImageView ic_pic;
    private TextView tv_title;
    private LinearLayout lin_dynamic;

    public IconTextPopAdapter(Context context, List<IconTextPopWindow.Entity> datas) {
        super(context, R.layout.adapter_icon_text_pop, datas);
    }

    @Override
    protected void convert(ViewHolder holder, IconTextPopWindow.Entity s, int position) {
        tv_title = holder.getView(R.id.tv_title);
        ic_pic = holder.getView(R.id.ic_pic);
        tv_title.setText(s.getTitile());
        ic_pic.setImageResource(s.getId());

    }
}
