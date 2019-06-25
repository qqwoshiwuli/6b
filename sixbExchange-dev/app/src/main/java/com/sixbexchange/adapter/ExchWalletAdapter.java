package com.sixbexchange.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.base.BaseAdapter;
import com.fivefivelike.mybaselibrary.utils.glide.GlideUtils;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.ExchWalletBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by 郭青枫 on 2018/1/10 0010.
 */

public class ExchWalletAdapter extends BaseAdapter<ExchWalletBean> {


    private TextView tv_name;
    private TextView tv_usdt;
    private ImageView iv_exch;
    private TextView tv_btc;

    public ExchWalletAdapter(Context context, List<ExchWalletBean> datas) {
        super(context, R.layout.adapter_exch_wallet, datas);

    }


    @Override
    protected void convert(ViewHolder holder, ExchWalletBean s, final int position) {
        tv_name = holder.getView(R.id.tv_name);
        tv_usdt = holder.getView(R.id.tv_usdt);
        iv_exch = holder.getView(R.id.iv_exch);
        tv_btc = holder.getView(R.id.tv_btc);

        tv_name.setText(s.getExchangeCn());
        tv_usdt.setText(s.getUsdt());
        GlideUtils.loadImage(s.getImg_url(),iv_exch);
        tv_btc.setText("约合"+s.getBtc()+"个BTC");


    }

}