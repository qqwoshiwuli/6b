package com.sixbexchange.widget.chart;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.sixbexchange.R;
import com.sixbexchange.utils.BigUIUtil;

/**
 * Created by loro on 2017/2/8.
 */
public class MyNowPriceMarkerView extends MarkerView {
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    private TextView tv_price;

    public MyNowPriceMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tv_price = (TextView) findViewById(R.id.tv_price);
    }

    public void setData(String num) {
        tv_price.setText(BigUIUtil.getinstance().bigPrice(num));
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tv_price.setVisibility(VISIBLE);
    }

    public void setTvWidth(int width) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv_price.getLayoutParams();
        params.width = width;
        tv_price.setLayoutParams(params);
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
