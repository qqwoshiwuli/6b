
package com.sixbexchange.widget.chart;

import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.highlight.Range;

/**
 * Contains information needed to determine the highlighted value.
 * 
 * @author Philipp Jahoda
 */
public class MyHighlight extends Highlight{

    private float mTouchYValue;

    public MyHighlight(int x, float value, int dataIndex, int dataSetIndex) {
        super(x, value, dataIndex, dataSetIndex);
    }

    public MyHighlight(int x, float value, int dataIndex, int dataSetIndex, int stackIndex) {
        super(x, value, dataIndex, dataSetIndex, stackIndex);
    }

    public MyHighlight(int x, float value, int dataIndex, int dataSetIndex, int stackIndex, Range range) {
        super(x, value, dataIndex, dataSetIndex, stackIndex, range);
    }

    public MyHighlight(int x, int dataSetIndex) {
        super(x, dataSetIndex);
    }

    public float getTouchYValue() {
        return mTouchYValue;
    }

    public void setTouchYValue(float touchYValue) {
        this.mTouchYValue = touchYValue;
    }

    private float mTouchY;

    public float getTouchY() {
        return mTouchY;
    }

    public void setTouchY(float touchY) {
        this.mTouchY = touchY;
    }
}
