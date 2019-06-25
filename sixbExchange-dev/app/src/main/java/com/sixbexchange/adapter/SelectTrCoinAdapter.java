package com.sixbexchange.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.circledialog.res.drawable.RadiuBg;
import com.fivefivelike.mybaselibrary.base.BaseAdapter;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.TradeDetailBean;
import com.sixbexchange.utils.BigUIUtil;
import com.sixbexchange.utils.UserSet;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 郭青枫 on 2018/7/16 0016.
 */

public class SelectTrCoinAdapter extends BaseAdapter<TradeDetailBean> {


    private TextView tv_name;
    private TextView tv_time;
    private TextView tv_amount;
    private TextView tv_price;
    private TextView tv_conversion_price;
    private TextView tv_rate;

    public SelectTrCoinAdapter(Context context, List<TradeDetailBean> datas) {
        super(context, R.layout.adapter_select_coin_tr, datas);

    }


    @Override
    protected void convert(ViewHolder holder, TradeDetailBean s, final int position) {
        tv_name = holder.getView(R.id.tv_name);
        tv_time = holder.getView(R.id.tv_time);
        tv_amount = holder.getView(R.id.tv_amount);
        tv_price = holder.getView(R.id.tv_price);
        tv_conversion_price = holder.getView(R.id.tv_conversion_price);
        tv_rate = holder.getView(R.id.tv_rate);


        tv_name.setText(s.getCurrencyPairName());
        tv_amount.setText(BigUIUtil.getinstance().bigAmount(s.getVol()));


        tv_price.setText(BigUIUtil.getinstance().getSymbol(s.getPriceUnit()) + s.getLa());


        if (TextUtils.isEmpty(s.getCh())) {
            tv_rate.setText("--");
            tv_rate.setBackground(new RadiuBg(
                    CommonUtils.getColor(R.color.base_mask_more),
                    10, 10, 10, 10
            ));
        } else {
            tv_rate.setText(BigUIUtil.getinstance().rateText(s.getCh()) + "%");
            tv_rate.setBackground(new RadiuBg(
                    CommonUtils.getColor(
                            new BigDecimal(s.getCh()).doubleValue() >= 0 ?
                                    UserSet.getinstance().getRiseColor() :
                                    UserSet.getinstance().getDropColor()
                    ),
                    10, 10, 10, 10
            ));
        }
        if (TextUtils.isEmpty(s.getLa())) {
            tv_price.setText(BigUIUtil.getinstance().getSymbol(s.getPriceUnit()) + "--");
            tv_conversion_price.setText("--");
        }
        if (TextUtils.isEmpty(s.getVol())) {
            tv_amount.setText("--");
        }


    }
}