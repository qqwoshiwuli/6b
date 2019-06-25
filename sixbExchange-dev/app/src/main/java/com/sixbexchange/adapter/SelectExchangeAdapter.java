package com.sixbexchange.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.base.BaseAdapter;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.utils.glide.GlideUtils;
import com.fivefivelike.mybaselibrary.view.IconFontTextview;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.ExchangeListBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by 郭青枫 on 2019/4/12 0012.
 * <p>
 * 交易页面选择交易所
 */

public class SelectExchangeAdapter extends BaseAdapter<ExchangeListBean> {


    private ImageView iv_exch;
    private TextView tv_name;
    private TextView tv_fee;
    private IconFontTextview tv_help;
    private TextView tv_all_money;
    private TextView tv_available_money;
    private ImageView iv_select;

    DefaultClickLinsener defaultClickLinsener;

    int selectPosition = 0;

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    public void setDefaultClickLinsener(DefaultClickLinsener defaultClickLinsener) {
        this.defaultClickLinsener = defaultClickLinsener;
    }

    public SelectExchangeAdapter(Context context, List<ExchangeListBean> datas) {
        super(context, R.layout.adapter_select_exchange, datas);

    }


    @Override
    protected void convert(ViewHolder holder, ExchangeListBean s, final int position) {
        iv_exch = holder.getView(R.id.iv_exch);
        tv_name = holder.getView(R.id.tv_name);
        tv_fee = holder.getView(R.id.tv_fee);
        tv_help = holder.getView(R.id.tv_help);
        tv_all_money = holder.getView(R.id.tv_all_money);
        tv_available_money = holder.getView(R.id.tv_available_money);
        iv_select = holder.getView(R.id.iv_select);


        GlideUtils.loadImage(s.getExchangeImg(), iv_exch);
        tv_name.setText(s.getName());
        tv_fee.setText("手续费" + s.getDiscount() + "折");

        iv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultClickLinsener.onClick(v, position, null);
                selectPosition = position;
                SelectExchangeAdapter.this.notifyDataSetChanged();
            }
        });
        tv_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultClickLinsener.onClick(v, position, null);
            }
        });


        if (position == selectPosition) {
            iv_select.setImageDrawable(CommonUtils.getDrawable(R.drawable.ic_check));
        } else {
            iv_select.setImageDrawable(CommonUtils.getDrawable(R.drawable.ic_check_off));
        }
        tv_all_money.setText("$" + s.getSumAmount());
        tv_available_money.setText("$" + s.getAvailableAmount());


    }

}
