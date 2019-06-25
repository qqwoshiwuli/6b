package com.sixbexchange.widget.chart;

import android.content.Context;
import android.graphics.Rect;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.sixbexchange.R;


/**
 * Created by loro on 2017/2/8.
 */
public class MyBottomMarkerView extends MarkerView {
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    private TextView markerTv;
    private String time;
    int padding;
    Rect rect;
    int[] locationOnScreen;

    public MyBottomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        markerTv = (TextView) findViewById(R.id.marker_tv);
        padding = (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_6px);
        locationOnScreen=new int[2];
        rect=new Rect();
    }

    public void setData(String time) {

        this.time = time;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        markerTv.setText(time);
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
