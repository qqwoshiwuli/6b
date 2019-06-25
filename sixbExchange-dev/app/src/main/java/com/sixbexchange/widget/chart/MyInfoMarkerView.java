package com.sixbexchange.widget.chart;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.circledialog.res.drawable.RadiuBg;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.kline.DataParse;
import com.sixbexchange.entity.bean.kline.KLineBean;
import com.sixbexchange.utils.BigUIUtil;
import com.sixbexchange.utils.UserSet;

import java.text.SimpleDateFormat;

/**
 * Created by loro on 2017/2/8.
 */
public class MyInfoMarkerView extends MarkerView {

    public TextView tv_time;
    public TextView tv_price;
    public LinearLayout lin_price;
    public TextView tv_high;
    public LinearLayout lin_high;
    public TextView tv_low;
    public LinearLayout lin_low;
    public TextView tv_open;
    public LinearLayout lin_open;
    public TextView tv_close;
    public LinearLayout lin_close;
    public TextView tv_rise_rate;
    public LinearLayout lin_rise_rate;
    public TextView tv_rise_vol;
    public LinearLayout lin_rise_vol;
    public TextView tv_vol;
    public LinearLayout lin_vol;
    public LinearLayout lin_kline_info;
    DataParse mDataParse;

    boolean isLine = false;

    public void setLine(boolean line) {
        isLine = line;
    }

    public MyInfoMarkerView(Context context, int layoutResource, DataParse dataParse) {
        super(context, layoutResource);
        mDataParse = dataParse;
        this.tv_time = (TextView) this.findViewById(R.id.tv_time);
        this.tv_price = (TextView) this.findViewById(R.id.tv_price);
        this.lin_price = (LinearLayout) this.findViewById(R.id.lin_price);
        this.tv_high = (TextView) this.findViewById(R.id.tv_high);
        this.lin_high = (LinearLayout) this.findViewById(R.id.lin_high);
        this.tv_low = (TextView) this.findViewById(R.id.tv_low);
        this.lin_low = (LinearLayout) this.findViewById(R.id.lin_low);
        this.tv_open = (TextView) this.findViewById(R.id.tv_open);
        this.lin_open = (LinearLayout) this.findViewById(R.id.lin_open);
        this.tv_close = (TextView) this.findViewById(R.id.tv_close);
        this.lin_close = (LinearLayout) this.findViewById(R.id.lin_close);
        this.tv_rise_rate = (TextView) this.findViewById(R.id.tv_rise_rate);
        this.lin_rise_rate = (LinearLayout) this.findViewById(R.id.lin_rise_rate);
        this.tv_rise_vol = (TextView) this.findViewById(R.id.tv_rise_vol);
        this.lin_rise_vol = (LinearLayout) this.findViewById(R.id.lin_rise_vol);
        this.tv_vol = (TextView) this.findViewById(R.id.tv_vol);
        this.lin_vol = (LinearLayout) this.findViewById(R.id.lin_vol);
        this.lin_kline_info = (LinearLayout) this.findViewById(R.id.lin_kline_info);
    }

    String rateUnit = "";
    String sysmol = "";

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        KLineBean kLineBean = mDataParse.getKLineDatas().get(e.getXIndex());
        rateUnit = mDataParse.getUnit();
        if(TextUtils.isEmpty(rateUnit)){
            rateUnit="";
        }
        if("null".equals(rateUnit)){
            rateUnit="";
        }
        sysmol = mDataParse.getSymbol();
        this.tv_time.setText(TimeUtils.millis2String(kLineBean.timestamp * 1000, new SimpleDateFormat("yyyy-MM-dd HH:mm")) + " " + TimeUtils.getChineseWeek(kLineBean.getTimestamp() * 1000));
        this.tv_open.setText(BigUIUtil.getinstance().bigPrice(kLineBean.open.toPlainString()) + " " + rateUnit);
        this.tv_high.setText(BigUIUtil.getinstance().bigPrice(kLineBean.high.toPlainString()) + " " + rateUnit);
        this.tv_low.setText(BigUIUtil.getinstance().bigPrice(kLineBean.low.toPlainString()) + " " + rateUnit);
        this.tv_close.setText(BigUIUtil.getinstance().bigPrice(kLineBean.close.toPlainString()) + " " + rateUnit);
        this.tv_price.setText(BigUIUtil.getinstance().bigPrice(kLineBean.close.toPlainString()) + " " + rateUnit);

        float v = (kLineBean.close.floatValue() /
                kLineBean.open.floatValue()) - 1;
        this.tv_rise_rate.setText(BigUIUtil.getinstance().bigPrice( v * 100 + "") + "%");
        this.tv_rise_vol.setText(BigUIUtil.getinstance().bigPrice((kLineBean.close.doubleValue() - kLineBean.open.doubleValue()) + "") + " " + rateUnit);
        this.tv_vol.setText(BigUIUtil.getinstance().bigAmount(mDataParse.getKLineDatas().get(e.getXIndex()).getVolume().toPlainString()) + " " + sysmol);

        if (kLineBean.close.doubleValue() - kLineBean.open.doubleValue() > 0) {
            this.tv_rise_vol.setTextColor(CommonUtils.getColor(UserSet.getinstance().getRiseColor()));
        } else {
            this.tv_rise_vol.setTextColor(CommonUtils.getColor(UserSet.getinstance().getDropColor()));
        }
        if (v > 0) {
            this.tv_rise_rate.setTextColor(CommonUtils.getColor(UserSet.getinstance().getRiseColor()));
        } else {
            this.tv_rise_rate.setTextColor(CommonUtils.getColor(UserSet.getinstance().getDropColor()));
        }

        this.lin_kline_info.setBackground(new RadiuBg(
                CommonUtils.getColor(R.color.mark_color),
                5, 5, 5, 5
        ));
        this.lin_kline_info.getBackground().mutate().setAlpha(220);

        if (isLine) {
            this.lin_price.setVisibility(View.VISIBLE);
            this.lin_open.setVisibility(View.GONE);
            this.lin_high.setVisibility(View.GONE);
            this.lin_low.setVisibility(View.GONE);
            this.lin_close.setVisibility(View.GONE);
            this.lin_rise_rate.setVisibility(View.GONE);
            this.lin_rise_vol.setVisibility(View.GONE);
        } else {
            this.lin_price.setVisibility(View.GONE);
            this.lin_open.setVisibility(View.VISIBLE);
            this.lin_high.setVisibility(View.VISIBLE);
            this.lin_low.setVisibility(View.VISIBLE);
            this.lin_close.setVisibility(View.VISIBLE);
            this.lin_rise_rate.setVisibility(View.VISIBLE);
            this.lin_rise_vol.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public int getXOffset(float xpos) {
        return 0;
    }

    @Override
    public int getYOffset(float ypos) {
        return 0;
    }


}
