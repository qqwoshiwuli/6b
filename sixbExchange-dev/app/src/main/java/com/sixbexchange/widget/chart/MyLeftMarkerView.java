package com.sixbexchange.widget.chart;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.sixbexchange.R;
import com.sixbexchange.utils.BigUIUtil;


/**
 * Created by loro on 2017/2/8.
 */
public class MyLeftMarkerView extends MarkerView implements KCombinedChart.MarkViewInterface {
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    private TextView markerTv;
    private float num;

    public MyLeftMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        //mFormat=new DecimalFormat("#0.00");
        markerTv = (TextView) findViewById(R.id.marker_tv);
    }

    public void setData(float num) {
        this.num = num;
        if (markerTv != null) {
            markerTv.setText(BigUIUtil.getinstance().bigPrice(num + ""));
        }
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        markerTv.setText(BigUIUtil.getinstance().bigPrice(num + ""));
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
