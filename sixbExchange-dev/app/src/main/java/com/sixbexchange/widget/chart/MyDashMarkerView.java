package com.sixbexchange.widget.chart;

import android.content.Context;
import android.widget.LinearLayout;

import com.fivefivelike.mybaselibrary.view.DashView;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.sixbexchange.R;

import java.text.DecimalFormat;

/**
 * Created by loro on 2017/2/8.
 */
public class MyDashMarkerView extends MarkerView {
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    private DashView view;
    private float num;
    private DecimalFormat mFormat;
    public MyDashMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        view = (DashView) findViewById(R.id.view);
    }

    public void setData(float num){
        this.num=num;
    }
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        view.setVisibility(VISIBLE);
    }

    public void setTvWidth(int width){
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width=width;
        view.setLayoutParams(params);
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
