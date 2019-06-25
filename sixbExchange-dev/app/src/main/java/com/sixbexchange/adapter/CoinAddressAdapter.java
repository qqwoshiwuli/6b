package com.sixbexchange.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.fivefivelike.mybaselibrary.base.BaseAdapter;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.CoinAddressBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by 郭青枫 on 2018/1/10 0010.
 */

public class CoinAddressAdapter extends BaseAdapter<CoinAddressBean> {


    DefaultClickLinsener defaultClickLinsener;
    private TextView tv_address;
    private TextView tv_delect;
    private TextView tv_mark;

    public void setDefaultClickLinsener(DefaultClickLinsener defaultClickLinsener) {
        this.defaultClickLinsener = defaultClickLinsener;
    }

    public CoinAddressAdapter(Context context, List<CoinAddressBean> datas) {
        super(context, R.layout.adapter_coin_address, datas);

    }


    @Override
    protected void convert(ViewHolder holder, CoinAddressBean s, final int position) {
        tv_address = holder.getView(R.id.tv_address);
        tv_delect = holder.getView(R.id.tv_delect);
        tv_mark = holder.getView(R.id.tv_mark);

        tv_delect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultClickLinsener.onClick(v, position, null);
            }
        });
        tv_address.setText(s.getAddr()
                + (((StringUtils.equalsIgnoreCase(s.getCoin(), "eos")) ||
                (StringUtils.equalsIgnoreCase(s.getCoin(), "xrp"))) ? " : " + s.getMemo() : "")
        );
        tv_mark.setText("地址备注：" + s.getRemark());

    }

}