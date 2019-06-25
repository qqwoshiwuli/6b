package com.fivefivelike.mybaselibrary.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.R;
import com.fivefivelike.mybaselibrary.adapter.entity.ShareItemEntity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;


/**
 * Created by 郭青枫 on 2017/9/30.
 */

public class ShareAdapter extends CommonAdapter<ShareItemEntity> {

    private ImageView iv_icon;
    private TextView tv_name;

    public ShareAdapter(Context context, List<ShareItemEntity> datas) {
        super(context, R.layout.adapter_share, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ShareItemEntity shareItemEntity, int position) {
        iv_icon=holder.getView(R.id.iv_icon);
        tv_name=holder.getView(R.id.tv_name);
        tv_name.setText(shareItemEntity.getName());
        iv_icon.setImageResource(shareItemEntity.getRes());
    }
}
