package com.sixbexchange.widget.chart;


import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.View;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;

/**
 * http://stackoverflow.com/questions/28521004/mpandroidchart-have-one-graph-mirror-the-zoom-swipes-on-a-sister-graph
 */
public class CoupleChartGestureListener implements OnChartGestureListener {

    private static final String TAG = CoupleChartGestureListener.class.getSimpleName();

    private Chart srcChart;
    private Chart[] dstCharts;

    boolean isTranslate=false;


    public CoupleChartGestureListener(Chart srcChart, Chart[] dstCharts) {
        this.srcChart = srcChart;
        this.dstCharts = dstCharts;
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        syncCharts();
        if (srcChart instanceof CombinedChart) {
            if (srcChart.getViewPortHandler().getMinScaleX() == 1f) {
                ((CombinedChart) srcChart).
                        getParent().requestDisallowInterceptTouchEvent(true);
            } else {
                ((CombinedChart) srcChart).
                        getParent().requestDisallowInterceptTouchEvent(false);
            }
        }
       // Log.i("CoupleChartGesture", "onChartGestureStart");
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        syncCharts();
        if (srcChart instanceof CombinedChart) {
            ((CombinedChart) srcChart).setDragEnabled(true);
            ((CombinedChart) srcChart).
                    getParent().requestDisallowInterceptTouchEvent(false);
        }
        isTranslate=false;
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        syncCharts();
        if (srcChart instanceof KCombinedChart) {
            ((KCombinedChart) srcChart).setDragEnabled(false);
            ((KCombinedChart) srcChart).
                    getParent().requestDisallowInterceptTouchEvent(true);
            if(!isTranslate){
                ((KCombinedChart) srcChart).getmChartTouchListener().onSingleTapUp(me);
            }
        }

        //Log.i("CoupleChartGesture", "onChartLongPressed");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        syncCharts();
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        syncCharts();
//        if (srcChart instanceof KCombinedChart) {
//            boolean b = ((KCombinedChart) srcChart).onChartSingleTapped(me);
//        }
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        syncCharts();
        //Log.i("CoupleChartGesture", "onChartFling");
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        //Log.d(TAG, "onChartScale " + scaleX + "/" + scaleY + " X=" + me.getX() + "Y=" + me.getY());
        //Log.i("CoupleChartGesture", "onChartScale");
        syncCharts();
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        //        Log.d(TAG, "onChartTranslate " + dX + "/" + dY + " X=" + me.getX() + "Y=" + me.getY());
        syncCharts();
       // Log.i("CoupleChartGesture", "onChartTranslate");
        if (srcChart instanceof CombinedChart) {
            ((CombinedChart) srcChart).setDragEnabled(true);
            ((CombinedChart) srcChart).
                    getParent().requestDisallowInterceptTouchEvent(false);
        }
        isTranslate=true;
    }

    public void syncCharts() {
        Matrix srcMatrix;
        float[] srcVals = new float[9];
        Matrix dstMatrix;
        float[] dstVals = new float[9];
        // get src chart translation matrix:
        srcMatrix = srcChart.getViewPortHandler().getMatrixTouch();
        srcMatrix.getValues(srcVals);
        // apply X axis scaling and position to dst charts:
        for (Chart dstChart : dstCharts) {
            if (dstChart.getVisibility() == View.VISIBLE) {
                dstMatrix = dstChart.getViewPortHandler().getMatrixTouch();
                dstMatrix.getValues(dstVals);

                dstVals[Matrix.MSCALE_X] = srcVals[Matrix.MSCALE_X];
                dstVals[Matrix.MSKEW_X] = srcVals[Matrix.MSKEW_X];
                dstVals[Matrix.MTRANS_X] = srcVals[Matrix.MTRANS_X];
                dstVals[Matrix.MSKEW_Y] = srcVals[Matrix.MSKEW_Y];
                dstVals[Matrix.MSCALE_Y] = srcVals[Matrix.MSCALE_Y];
                dstVals[Matrix.MTRANS_Y] = srcVals[Matrix.MTRANS_Y];
                dstVals[Matrix.MPERSP_0] = srcVals[Matrix.MPERSP_0];
                dstVals[Matrix.MPERSP_1] = srcVals[Matrix.MPERSP_1];
                dstVals[Matrix.MPERSP_2] = srcVals[Matrix.MPERSP_2];

                dstMatrix.setValues(dstVals);
                dstChart.getViewPortHandler().refresh(dstMatrix, dstChart, true);
            }
        }
    }
}