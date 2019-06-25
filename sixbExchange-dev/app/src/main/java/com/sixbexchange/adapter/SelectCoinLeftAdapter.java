package com.sixbexchange.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.base.BaseAdapter;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.CoinToCoinBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class SelectCoinLeftAdapter extends BaseAdapter<CoinToCoinBean> {

    private TextView tv_name;

    private DefaultClickLinsener defaultClickLinsener;

    int selectPosition = 0;

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    public void setDefaultClickLinsener(DefaultClickLinsener defaultClickLinsener) {
        this.defaultClickLinsener = defaultClickLinsener;
    }

    public SelectCoinLeftAdapter(Context context, List<CoinToCoinBean> datas) {
        super(context, R.layout.item_coinleft, datas);

    }

    @Override
    protected void convert(ViewHolder holder, final CoinToCoinBean s, final int position) {
        tv_name = holder.getView(R.id.tv_coinleft);
        tv_name.setText(s.getType());
        if (position == selectPosition) {
            tv_name.setTextColor(CommonUtils.getColor(R.color.blue));
        } else {
            tv_name.setTextColor(CommonUtils.getColor(R.color.color_font1));
        }
        tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultClickLinsener.onClick(v,position,s);
            }
        });
    }
}
