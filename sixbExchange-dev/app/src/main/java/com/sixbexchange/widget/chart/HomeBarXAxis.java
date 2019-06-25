package com.sixbexchange.widget.chart;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.utils.Utils;

/**
 * Created by 郭青枫 on 2018/10/26 0026.
 */

public class HomeBarXAxis extends XAxis {

    @Override
    public void setTextSize(float size) {
        mTextSize = Utils.convertDpToPixel(size);
    }
}
