package com.sixbexchange.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.base.BaseAdapter;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.utils.glide.GlideUtils;
import com.fivefivelike.mybaselibrary.view.FontTextview;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.WalletCoinBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by 郭青枫 on 2018/1/10 0010.
 */

public class WalletCoinAdapter extends BaseAdapter<WalletCoinBean> {


    private ImageView iv_piv;
    private FontTextview tv_coin;
    private FontTextview tv_all;
    private TextView tv_freeze;
    private TextView tv_usd;
    private TextView tv_transfer;
    private TextView tv_recharge;
    private TextView tv_withdraw;

    DefaultClickLinsener defaultClickLinsener;

    public void setDefaultClickLinsener(DefaultClickLinsener defaultClickLinsener) {
        this.defaultClickLinsener = defaultClickLinsener;
    }
    String exchName;
    public WalletCoinAdapter(Context context, List<WalletCoinBean> datas,String exchName) {
        super(context, R.layout.adapter_wallet_coin, datas);
        this.exchName=exchName;
    }


    @Override
    protected void convert(ViewHolder holder, WalletCoinBean s, final int position) {
        iv_piv = holder.getView(R.id.iv_piv);
        tv_coin = holder.getView(R.id.tv_coin);
        tv_all = holder.getView(R.id.tv_all);
        tv_freeze = holder.getView(R.id.tv_freeze);
        tv_usd = holder.getView(R.id.tv_usd);
        tv_transfer = holder.getView(R.id.tv_transfer);
        tv_recharge = holder.getView(R.id.tv_recharge);
        tv_withdraw = holder.getView(R.id.tv_withdraw);

        tv_coin.setText(s.getCoin());
        tv_all.setText(s.getAvi());
        tv_freeze.setText("冻结 " + s.getLock());
        tv_usd.setText("≈ $ " + s.getEva());
        if (exchName.toLowerCase().contains("bitmex"))
        {
            tv_transfer.setVisibility(View.GONE);
        }else
        {
            tv_transfer.setVisibility(View.VISIBLE);
        }
        tv_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultClickLinsener.onClick(v, position, null);
            }
        });
        tv_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultClickLinsener.onClick(v, position, null);
            }
        });
        tv_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultClickLinsener.onClick(v, position, null);
            }
        });
        GlideUtils.loadImage(s.getImg_url(),iv_piv);
    }

}