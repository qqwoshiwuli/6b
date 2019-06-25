package com.sixbexchange.widget.chart;

import android.graphics.Canvas;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by loro on 2017/2/8.
 */
public class MyYAxisRenderer extends YAxisRenderer {
    protected YAxis mYAxis;
    public MyYAxisRenderer(ViewPortHandler viewPortHandler, YAxis yAxis, Transformer trans) {
        super(viewPortHandler, yAxis, trans);
        mYAxis = yAxis;
    }


    @Override
    protected void drawYLabels(Canvas c, float fixedPosition, float[] positions, float offset) {
        super.drawYLabels(c,fixedPosition,positions,offset);
        // draw
        for (int i = 0; i < mYAxis.mEntryCount; i++) {
            //调用mYAxisValueFormatter.getFormattedValue(mEntries[index], this)得到绘制的文本
            if (!mYAxis.isDrawTopYLabelEntryEnabled() && i >= mYAxis.mEntryCount - 1)
                return;
            c.drawLine(mViewPortHandler.contentRight(), positions[i * 2 + 1] ,
                    mViewPortHandler.contentRight()+10, positions[i * 2 + 1] ,
                    mAxisLinePaint);

        }
    }

}
