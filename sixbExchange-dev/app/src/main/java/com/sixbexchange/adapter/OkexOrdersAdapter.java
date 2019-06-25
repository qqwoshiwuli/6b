package com.sixbexchange.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.fivefivelike.mybaselibrary.base.BaseAdapter;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.view.RoundButton;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.OrderBean;
import com.sixbexchange.entity.bean.TradeDetailBean;
import com.sixbexchange.utils.BigUIUtil;
import com.sixbexchange.utils.DateUtils;
import com.sixbexchange.utils.UserSet;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 郭青枫 on 2018/1/10 0010.
 * 用户订单
 */

public class OkexOrdersAdapter extends BaseAdapter<OrderBean> {


    DefaultClickLinsener defaultClickLinsener;


    TradeDetailBean tradeDetailBean;
    DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private TextView tv_name;
    private RoundButton tv_close;
    private TextView tv_num_title;
    private TextView tv_transaction_title;
    private TextView tv_num;
    private TextView tv_transaction;
    private TextView tv_order_price;
    private TextView tv_travg_price;
    private TextView tv_statu;
    private TextView tv_time;
    private TextView tv_margin;
    private TextView tv_fee;


    public void setTradeDetailBean(TradeDetailBean tradeDetailBean) {
        this.tradeDetailBean = tradeDetailBean;
    }

    public void setDefaultClickLinsener(DefaultClickLinsener defaultClickLinsener) {
        this.defaultClickLinsener = defaultClickLinsener;
    }

    public OkexOrdersAdapter(Context context, List<OrderBean> datas) {
        super(context, R.layout.adapter_okex_order, datas);

    }


    @Override
    protected void convert(ViewHolder holder, OrderBean s, final int position) {
        tv_name = holder.getView(R.id.tv_name);
        tv_close = holder.getView(R.id.tv_close);
        tv_num = holder.getView(R.id.tv_num);
        tv_num_title = holder.getView(R.id.tv_num_title);
        tv_transaction = holder.getView(R.id.tv_transaction);
        tv_transaction_title = holder.getView(R.id.tv_transaction_title);
        tv_order_price = holder.getView(R.id.tv_order_price);
        tv_travg_price = holder.getView(R.id.tv_travg_price);
        tv_statu = holder.getView(R.id.tv_statu);
        tv_time = holder.getView(R.id.tv_time);
        tv_margin = holder.getView(R.id.tv_margin);
        tv_fee = holder.getView(R.id.tv_fee);

        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultClickLinsener.onClick(v, position, null);
            }
        });

        tv_name.setText(tradeDetailBean.getCurrencyPairName());

        if (ObjectUtils.equals("b", s.getBs())&&ObjectUtils.equals(GsonUtil.getInstance().getValue(s.getTags(),"type"),"open")) {
            tv_name.setText("买入开多 " + "(" + tradeDetailBean.getCurrencyPairName() + " 20X )");
            tv_name.setTextColor(CommonUtils.getColor(UserSet.getinstance().getRiseColor()));
        } else if (ObjectUtils.equals("s", s.getBs())&&ObjectUtils.equals(GsonUtil.getInstance().getValue(s.getTags(),"type"),"open")) {
            tv_name.setText("卖出开空 " + "(" + tradeDetailBean.getCurrencyPairName() + " 20X )");
            tv_name.setTextColor(CommonUtils.getColor(UserSet.getinstance().getDropColor()));
        }else if (ObjectUtils.equals("b", s.getBs())&&ObjectUtils.equals(GsonUtil.getInstance().getValue(s.getTags(),"type"),"close")) {
            tv_name.setText("买入平空 " + "(" + tradeDetailBean.getCurrencyPairName() + " 20X )");
            tv_name.setTextColor(CommonUtils.getColor(UserSet.getinstance().getRiseColor()));
        }else if (ObjectUtils.equals("s", s.getBs())&&ObjectUtils.equals(GsonUtil.getInstance().getValue(s.getTags(),"type"),"close")) {
            tv_name.setText("卖出平多 " + "(" + tradeDetailBean.getCurrencyPairName() + " 20X )");
            tv_name.setTextColor(CommonUtils.getColor(UserSet.getinstance().getDropColor()));
        }

        tv_num.setText(BigUIUtil.getinstance().bigAmount(s.getEntrust_amount()));
        tv_transaction.setText(BigUIUtil.getinstance().bigAmount(s.getDealt_amount()));

        tv_num_title.setText("委托数量（" + tradeDetailBean.getAmountUnit() + "）");
        tv_transaction_title.setText("已成交数量（" + tradeDetailBean.getAmountUnit() + "）");

        tv_order_price.setText(BigUIUtil.getinstance().getSymbol(tradeDetailBean.getPriceUnit()) + " " +
                BigUIUtil.getinstance().bigPrice(s.getEntrust_price()));
        tv_travg_price.setText(TextUtils.isEmpty(s.getAverage_dealt_price()) ? "--" :
                BigUIUtil.getinstance().getSymbol(tradeDetailBean.getPriceUnit()) + " " +
                        BigUIUtil.getinstance().bigPrice(s.getAverage_dealt_price()));

        tv_margin.setText(BigUIUtil.getinstance().getSymbol(tradeDetailBean.getCurrency()) + " " +
                (TextUtils.isEmpty(s.getFrozen_margin()) ? "--" :
                        BigUIUtil.getinstance().bigAmount(s.getFrozen_margin())));
        tv_fee.setText(BigUIUtil.getinstance().getSymbol(tradeDetailBean.getCurrency()) + " " +
                BigUIUtil.getinstance().bigPrice(s.getCommission()));

        tv_statu.setText(
                s.getStatus()
                        .replace("active", "激活中")
                        .replace("waiting", "已下单")
                        .replace("pending", "等待成交")
                        .replace("part-deal-pending", "部分成交")
                        .replace("withdrawing", "等待撤单")
                        .replace("part-deal-withdrawing", "等待撤单")
                        .replace("end", "已结束")
                        .replace("dealt", "完全成交")
                        .replace("withdrawn", "已撤销")
                        .replace("part-deal-withdrawn", "已撤单")
                        .replace("error-order", "错误订单")
        );

        if (ObjectUtils.equals(s.getStatus(), "active") ||
                ObjectUtils.equals(s.getStatus(), "waiting") ||
                ObjectUtils.equals(s.getStatus(), "pending") ||
                ObjectUtils.equals(s.getStatus(), "part-deal-pending")
                ) {
            tv_close.setVisibility(View.VISIBLE);
        } else {
            tv_close.setVisibility(View.GONE);
        }

        Date time = DateUtils.getTime(s.getEntrust_time(), DateUtils.TIME_STYLE_S13);
        if (time != null) {
            tv_time.setText(
                    TimeUtils.date2String(time, DEFAULT_FORMAT));
        } else {
            tv_time.setText(s.getEntrust_time());
        }

    }

}