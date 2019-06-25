package com.sixbexchange.adapter;

import android.content.Context;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.base.BaseAdapter;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.glide.GlideUtils;
import com.fivefivelike.mybaselibrary.view.RoundButton;
import com.fivefivelike.mybaselibrary.view.circleprogress.DonutProgress;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.MyFollowBean;
import com.sixbexchange.utils.UserSet;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Created by 郭青枫 on 2018/1/10 0010.
 */

public class MyFollowAdapter extends BaseAdapter<MyFollowBean> {


    private ImageView iv_piv;
    private TextView tv_name;
    private RoundButton tv_sub;
    private TextView tv_end_time;
    private TextView tv_money;
    private DonutProgress donutProgress;
    private TextView tv_rate;
    private TextView tv_comein;

    public MyFollowAdapter(Context context, List<MyFollowBean> datas) {
        super(context, R.layout.adapter_my_follow, datas);

    }


    @Override
    protected void convert(ViewHolder holder, MyFollowBean s, final int position) {
        iv_piv = holder.getView(R.id.iv_piv);
        tv_name = holder.getView(R.id.tv_name);
        tv_sub = holder.getView(R.id.tv_sub);
        tv_end_time = holder.getView(R.id.tv_end_time);
        tv_money = holder.getView(R.id.tv_money);
        donutProgress = holder.getView(R.id.donutProgress);
        tv_rate = holder.getView(R.id.tv_rate);
        tv_comein = holder.getView(R.id.tv_comein);

        GlideUtils.loadImage(s.getIcon(), iv_piv);
        tv_name.setText(s.getNickName());
        tv_sub.setText(s.getTitle());

        tv_end_time.setText(s.getCloseTimeStr());
        BigDecimal divide = new BigDecimal(s.getAllMoney())
                .multiply(new BigDecimal(s.getAmount()))
                .divide(new BigDecimal(s.getAllAmount()), 4, RoundingMode.DOWN);
        tv_money.setText(
                divide.stripTrailingZeros().toPlainString() + s.getCurrency());

        tv_rate.setText(Html.fromHtml(
                s.getWelfare() +
                        "<font color=\"" + CommonUtils.getStringColor(R.color.color_font3) +
                        "\"<small><small><small>%</small></small></small>"
        ));
        tv_comein.setTextColor(CommonUtils.getColor(
                new BigDecimal(s.getWelfare()).floatValue() >= 0 ?
                        UserSet.getinstance().getRiseColor() : UserSet.getinstance().getDropColor()));
        tv_comein.setText(Html.fromHtml(
                new BigDecimal(s.getWelfare())
                        .multiply(divide)
                        .divide(new BigDecimal("100"), 4, RoundingMode.DOWN)
                        .stripTrailingZeros().toPlainString() +
                        "<font color=\"" + CommonUtils.getStringColor(R.color.color_font3) +
                        "\"<small><small><small>" + s.getCurrency() + "</small></small></small>"
        ));
        donutProgress.setMax(
                new BigDecimal(s.getCloseDay()).intValue()
        );

        // private Integer status;//1 未开始   ，  0 进行中 ，2 运行中 ，3 已结束
        if (s.getStatus() == 1) {
            donutProgress.setProgress(0);
            donutProgress.setText("未开始");
        } else if (s.getStatus() == 0) {
            donutProgress.setProgress(
                    new BigDecimal(
                            ((System.currentTimeMillis() - s.getEndTime()) /
                                    1000 / 60 / 60 / 24) + "")
                            .setScale(0, RoundingMode.DOWN).intValue());
            donutProgress.setText("进行中");
        } else if (s.getStatus() == 2) {
            donutProgress.setProgress(
                    new BigDecimal(
                            ((System.currentTimeMillis() - s.getEndTime()) /
                                    1000 / 60 / 60 / 24) + "")
                            .setScale(0, RoundingMode.DOWN).intValue());
            donutProgress.setText("运行中");
        } else if (s.getStatus() == 3) {
            donutProgress.setProgress(new BigDecimal(s.getCloseDay()).intValue());
            donutProgress.setText("已结束");
        }


    }

}