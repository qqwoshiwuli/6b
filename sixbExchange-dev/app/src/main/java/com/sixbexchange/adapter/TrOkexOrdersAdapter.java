package com.sixbexchange.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.circledialog.res.drawable.RadiuBg;
import com.fivefivelike.mybaselibrary.base.BaseAdapter;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.view.RoundButton;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.OrderBean;
import com.sixbexchange.entity.bean.TradeDetailBean;
import com.sixbexchange.utils.BigUIUtil;
import com.sixbexchange.utils.UserSet;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by 郭青枫 on 2018/1/10 0010.
 * 用户订单
 */

public class TrOkexOrdersAdapter extends BaseAdapter<OrderBean> {


    DefaultClickLinsener defaultClickLinsener;
    private TextView tv_type;
    private TextView tv_name;
    private RoundButton tv_close;
    private TextView tv_open_amount;
    private TextView tv_open_price;
    private TextView tv_hold_amount;
    private TextView tv_margin;
    TradeDetailBean tradeDetailBean;

    public void setTradeDetailBean(TradeDetailBean tradeDetailBean) {
        this.tradeDetailBean = tradeDetailBean;
    }

    public void setDefaultClickLinsener(DefaultClickLinsener defaultClickLinsener) {
        this.defaultClickLinsener = defaultClickLinsener;
    }

    public TrOkexOrdersAdapter(Context context, List<OrderBean> datas) {
        super(context, R.layout.adapter_tr_order, datas);

    }


    @Override
    protected void convert(ViewHolder holder, OrderBean s, final int position) {
        tv_name = holder.getView(R.id.tv_name);
        tv_type = holder.getView(R.id.tv_type);
        tv_open_amount = holder.getView(R.id.tv_open_amount);
        tv_hold_amount = holder.getView(R.id.tv_hold_amount);
        tv_open_price = holder.getView(R.id.tv_open_price);
        tv_close = holder.getView(R.id.tv_close);
        tv_margin = holder.getView(R.id.tv_margin);

        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultClickLinsener.onClick(v, position, null);
            }
        });

        tv_name.setText(tradeDetailBean.getCurrencyPairName());


        if (ObjectUtils.equals("b", s.getBs()) && ObjectUtils.equals(GsonUtil.getInstance().getValue(s.getTags(),"type"), "open")) {
            tv_type.setText("买入开多");
            tv_type.setBackground(new RadiuBg(
                    CommonUtils.getColor(UserSet.getinstance().getRiseColor()),
                    5, 5, 5, 5
            ));
        } else if (ObjectUtils.equals("s", s.getBs()) && ObjectUtils.equals(GsonUtil.getInstance().getValue(s.getTags(),"type"), "open")) {
            tv_type.setText("卖出开空");
            tv_type.setBackground(new RadiuBg(
                    CommonUtils.getColor(UserSet.getinstance().getDropColor()),
                    5, 5, 5, 5
            ));
        } else if (ObjectUtils.equals("b", s.getBs()) && ObjectUtils.equals(GsonUtil.getInstance().getValue(s.getTags(),"type"), "close")) {
            tv_type.setText("买入平空");
            tv_type.setBackground(new RadiuBg(
                    CommonUtils.getColor(UserSet.getinstance().getRiseColor()),
                    5, 5, 5, 5
            ));
        } else if (ObjectUtils.equals("s", s.getBs()) && ObjectUtils.equals(GsonUtil.getInstance().getValue(s.getTags(),"type"), "close")) {
            tv_type.setText("卖出平多");
            tv_type.setBackground(new RadiuBg(
                    CommonUtils.getColor(UserSet.getinstance().getDropColor()),
                    5, 5, 5, 5
            ));
        }

        tv_open_amount.setText(BigUIUtil.getinstance().bigAmount(s.getEntrust_amount()));
        tv_hold_amount.setText(BigUIUtil.getinstance().bigAmount(s.getDealt_amount()));


        tv_open_price.setText(BigUIUtil.getinstance().getSymbol(tradeDetailBean.getPriceUnit()) + " " +
                BigUIUtil.getinstance().bigPrice(s.getEntrust_price()));


    }

}