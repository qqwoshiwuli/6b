package com.sixbexchange.widget.chart;

import android.content.Context;
import android.widget.TextView;

import com.circledialog.res.drawable.RadiuBg;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.sixbexchange.R;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by 郭青枫 on 2018/7/26 0026.
 */

public class BlockDataMarkerView extends MarkerView {

    private TextView tv_title;

    public BlockDataMarkerView(Context context) {
        super(context, R.layout.layout_block_marker_view);//这个布局自己定义
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setBackground(new RadiuBg(
                CommonUtils.getColor(R.color.mark_color),
                5, 5, 5, 5
        ));
        tv_title.getBackground().mutate().setAlpha(50);
        tv_title.setTextColor(CommonUtils.getColor(R.color.white));

    }

    //显示的内容
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if (tv_title != null) {
            tv_title.setText(new BigDecimal( e.getVal()+"").setScale(2, RoundingMode.DOWN).toPlainString() + "%");
        }
    }

    @Override
    public int getXOffset(float xpos) {
        return -(int) CommonUtils.getDimensionPixelSize(R.dimen.trans_35px);
    }

    @Override
    public int getYOffset(float ypos) {
        return -(int) CommonUtils.getDimensionPixelSize(R.dimen.trans_45px);
    }

    //    //标记相对于折线图的偏移量
    //    @Override
    //    public MPPointF getOffset() {
    //        return new MPPointF(-(getWidth() / 2), -getHeight());
    //    }


}
