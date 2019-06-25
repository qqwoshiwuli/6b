package com.sixbexchange.widget.chart;

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.BarChart;

/**
 * Created by 郭青枫 on 2018/10/26 0026.
 */

public class HomeBarChart extends BarChart {
    public HomeBarChart(Context context) {
        super(context);
    }

    public HomeBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeBarChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        mXAxis=new HomeBarXAxis();
    }
}
