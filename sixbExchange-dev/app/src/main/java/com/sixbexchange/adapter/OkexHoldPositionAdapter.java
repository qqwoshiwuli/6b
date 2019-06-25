package com.sixbexchange.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.circledialog.res.drawable.RadiuBg;
import com.fivefivelike.mybaselibrary.base.BaseAdapter;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.view.IconFontTextview;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.HoldPositionBean;
import com.sixbexchange.entity.bean.TradeDetailBean;
import com.sixbexchange.utils.BigUIUtil;
import com.sixbexchange.utils.UserSet;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Created by 郭青枫 on 2019/4/11 0011.
 * 持仓 页面 adapter
 */

public class OkexHoldPositionAdapter extends BaseAdapter<HoldPositionBean> {


    DefaultClickLinsener defaultClickLinsener;
    private TextView tv_name;
    private TextView tv_type;
    private TextView tv_open;
    private TextView tv_rate;
    private TextView tv_income;
    private TextView tv_amount;
    private TextView tv_close_amount;
    private TextView tv_margin;
    private TextView tv_end_price;
    private TextView tv_change;
    private TextView tv_close;
    TradeDetailBean tradeDetailBean;
    private IconFontTextview tv_share;
    public void setTradeDetailBean(TradeDetailBean tradeDetailBean) {
        this.tradeDetailBean = tradeDetailBean;
    }

    public void setDefaultClickLinsener(DefaultClickLinsener defaultClickLinsener) {
        this.defaultClickLinsener = defaultClickLinsener;
    }

    public OkexHoldPositionAdapter(Context context, List<HoldPositionBean> datas) {
        super(context, R.layout.layout_okex_hold_position, datas);
    }


    @Override
    protected void convert(ViewHolder holder, HoldPositionBean s, final int position) {
        tv_name = holder.getView(R.id.tv_name);
        tv_type = holder.getView(R.id.tv_type);
        tv_open = holder.getView(R.id.tv_open);
        tv_rate = holder.getView(R.id.tv_rate);
        tv_income = holder.getView(R.id.tv_income);
        tv_amount = holder.getView(R.id.tv_amount);
        tv_close_amount = holder.getView(R.id.tv_close_amount);
        tv_margin = holder.getView(R.id.tv_margin);
        tv_end_price = holder.getView(R.id.tv_end_price);
        tv_change = holder.getView(R.id.tv_change);
        tv_close = holder.getView(R.id.tv_close);
        tv_share = holder.getView(R.id.tv_share);
        tv_share.setText(CommonUtils.getString(R.string.ic_fenxiang) + " 晒单");
        tv_name.setText(s.getDetail().getContractName());

        tv_open.setText(BigUIUtil.getinstance().getSymbol(s.getDetail().getPriceUnit()) +
                BigUIUtil.getinstance().bigPrice(s.getAverageOpenPrice()));
        tv_rate.setText(BigUIUtil.getinstance().bigAmount(s.getUnrealizedRate()) + "%");

        tv_income.setText(BigUIUtil.getinstance().bigAmount(s.getUnrealized()) + " " + s.getDetail().getMarginUnit());
        tv_amount.setText(BigUIUtil.getinstance().bigAmount(s.getTotalAmount().replace("-", "")) + " " + s.getDetail().getAmountUnit());
        tv_close_amount.setText(BigUIUtil.getinstance().bigAmount(s.getAvailable().replace("-", "")) + " " + s.getDetail().getAmountUnit());
        tv_margin.setText(TextUtils.isEmpty(s.getFrozenPosition()) ? "--" : new BigDecimal(s.getFrozenPosition()).setScale(4, RoundingMode.DOWN).toPlainString() + " " + s.getDetail().getMarginUnit());
        tv_end_price.setText(BigUIUtil.getinstance().getSymbol(s.getDetail().getPriceUnit()) + " " +
                BigUIUtil.getinstance().bigPrice(s.getLiquidationPrice()));

        //#spot 现货 future 期货------------------BM都是future
        if (ObjectUtils.equals("future", s.getType())) {
            tv_type.setVisibility(View.VISIBLE);
            if (new BigDecimal(s.getTotalAmount()).doubleValue() >= 0) {
                tv_type.setText("多头 " +  "20X");
                tv_type.setBackground(new RadiuBg(
                        CommonUtils.getColor(UserSet.getinstance().getRiseColor()),
                        5, 5, 5, 5
                ));
            } else if (new BigDecimal(s.getTotalAmount()).doubleValue() < 0) {
                tv_type.setText("空头 " +  "20X");
                tv_type.setBackground(new RadiuBg(
                        CommonUtils.getColor(UserSet.getinstance().getDropColor()),
                        5, 5, 5, 5
                ));
            }
        } else {
            tv_type.setVisibility(View.GONE);
        }

        if (s.getDetail().getLeverageAvailable() == 1) {
            tv_change.setVisibility(View.VISIBLE);
        } else {
            tv_change.setVisibility(View.GONE);
        }
        tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultClickLinsener.onClick(v, position, null);
            }
        });
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultClickLinsener.onClick(v, position, null);
            }
        });
        tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultClickLinsener.onClick(v, position, null);
            }
        });
    }

}