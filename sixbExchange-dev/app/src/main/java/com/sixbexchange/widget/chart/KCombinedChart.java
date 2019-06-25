package com.sixbexchange.widget.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.WindowManager;

import com.blankj.utilcode.util.TimeUtils;
import com.fivefivelike.mybaselibrary.utils.ListUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.kline.DataParse;
import com.sixbexchange.entity.bean.kline.KLineBean;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by 郭青枫 on 2018/1/12 0012.
 */

public class KCombinedChart extends CombinedChart {

    private MarkerView myMarkerViewLeft;
    private MyHMarkerView myMarkerViewH;
    private MyBottomMarkerView myBottomMarkerView;
    private MyBottomMarkerView myBMarkerView;

    public void setMyBMarkerView(MyBottomMarkerView myBMarkerView) {
        this.myBMarkerView = myBMarkerView;
    }


    MyNowPriceMarkerView myNowPriceMarkerView;

    public MyNowPriceMarkerView getMyNowPriceMarkerView() {
        return myNowPriceMarkerView;
    }

    MyInfoMarkerView myInfoMarkerView;

    private DataParse minuteHelper;
    boolean isDrawHeightAndLow = false;
    DefaultClickLinsener defaultClickLinsener;
    Canvas canvas;
    OnMaxLeftLinsener onMaxLeftLinsener;
    int windowWidth;
    MyDashMarkerView dashMarkerView;
    float mlWidth;

    public Canvas getCanvas() {
        return canvas;
    }

    public void setMyInfoMarkerView(MyInfoMarkerView myInfoMarkerView) {
        this.myInfoMarkerView = myInfoMarkerView;
    }

    public void clearHightLisht() {
        mIndicesToHighlight = new Highlight[]{};
        this.invalidate();
    }

    public MarkerView getMyMarkerViewLeft() {
        return myMarkerViewLeft;
    }

    public void setOnMaxLeftLinsener(OnMaxLeftLinsener onMaxLeftLinsener) {
        this.onMaxLeftLinsener = onMaxLeftLinsener;
    }

    public interface OnMaxLeftLinsener {
        void onMaxLeft();
    }


    public void setDefaultClickLinsener(DefaultClickLinsener defaultClickLinsener) {
        this.defaultClickLinsener = defaultClickLinsener;
    }

    public void setDrawHeightAndLow(boolean drawHeightAndLow) {
        isDrawHeightAndLow = drawHeightAndLow;
    }

    public KCombinedChart(Context context) {
        super(context);
        initView(context);
    }

    public KCombinedChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public KCombinedChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowWidth = wm.getDefaultDisplay().getWidth();
    }

    public void setMarker(MarkerView markerLeft, MyHMarkerView markerH, DataParse minuteHelper) {
        this.myMarkerViewLeft = markerLeft;
        this.myMarkerViewH = markerH;
        this.minuteHelper = minuteHelper;

        dashMarkerView = new MyDashMarkerView(getContext(), R.layout.mymarkerview_dashline);
        myNowPriceMarkerView = new MyNowPriceMarkerView(getContext(), R.layout.mymarkerview_nowprice);

        setDrawMarkerViews(true);
    }

    public void setMarker(MarkerView markerLeft, MyBottomMarkerView markerBottom, DataParse minuteHelper) {
        this.myMarkerViewLeft = markerLeft;
        this.myBottomMarkerView = markerBottom;
        this.minuteHelper = minuteHelper;
        setDrawMarkerViews(true);
    }

    public void setMarker(MarkerView markerLeft, MyBottomMarkerView markerBottom, MyHMarkerView markerH, DataParse minuteHelper) {
        this.myMarkerViewLeft = markerLeft;
        this.myBottomMarkerView = markerBottom;
        this.myMarkerViewH = markerH;
        this.minuteHelper = minuteHelper;

        setDrawMarkerViews(true);
    }

    private Entry getEntry(Highlight highlight) {
        List<ChartData> dataObjects = mData.getAllData();

        if (highlight.getDataIndex() >= dataObjects.size())
            return null;

        ChartData data = dataObjects.get(highlight.getDataIndex());

        if (highlight.getDataSetIndex() >= data.getDataSetCount())
            return null;
        else {
            return data.getDataSetByIndex(highlight.getDataSetIndex()).getEntryForXIndex(highlight.getXIndex());
        }
    }




    public void drawMarkerView() {
        int lowestVisibleXIndex = this.getLowestVisibleXIndex();
        if (onMaxLeftLinsener != null) {
            if (lowestVisibleXIndex == 0) {
                onMaxLeftLinsener.onMaxLeft();
            }
        }
        if (dashMarkerView != null && myNowPriceMarkerView != null && !ListUtils.isEmpty(minuteHelper.getKLineDatas())) {
            String yChartMax = this.getYChartMax() + "";

            String yChartMin = this.getYChartMin() + "";
            String value;


            value = minuteHelper.getKLineDatas().get(minuteHelper.getKLineDatas().size() - 1).close.toString() + "";

            BigDecimal bigDecimal = new BigDecimal(yChartMin);

            BigDecimal valueB = new BigDecimal(value).subtract(bigDecimal);

            BigDecimal yChartMaxB = new BigDecimal(yChartMax).subtract(bigDecimal);

            String hightS = (this.getMeasuredHeight() - this.getViewPortHandler().offsetBottom() - this.getViewPortHandler().offsetTop()) + "";

            BigDecimal divB = valueB.multiply(new BigDecimal(hightS)).divide(yChartMaxB, 10, BigDecimal.ROUND_HALF_UP);

            float height = this.getMeasuredHeight() - divB.floatValue() - this.getViewPortHandler().offsetBottom();


            dashMarkerView.refreshContent(null, null);
            int width = (int) mViewPortHandler.contentWidth();
            dashMarkerView.setTvWidth(width);
            dashMarkerView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int measuredHeight = dashMarkerView.getMeasuredHeight();
            dashMarkerView.layout(0, 0, width,
                    measuredHeight);

            dashMarkerView.draw(canvas, mViewPortHandler.contentLeft(), height);


            myNowPriceMarkerView.refreshContent(null, null);
            myNowPriceMarkerView.setData(value);
            myNowPriceMarkerView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            myNowPriceMarkerView.layout(0, 0, myNowPriceMarkerView.getMeasuredWidth(),
                    myNowPriceMarkerView.getMeasuredHeight());
            myNowPriceMarkerView.draw(canvas, mViewPortHandler.contentRight(),
                    height - myNowPriceMarkerView.getHeight() / 2);


            //            float v = getViewPortHandler().offsetRight();
            //            if (v < myNowPriceMarkerView.getWidth()) {
            //                setViewPortOffsets(
            //                        mViewPortHandler.offsetLeft(),
            //                        mViewPortHandler.offsetTop(),
            //                        getMyNowPriceMarkerView().getWidth(),
            //                        mViewPortHandler.offsetBottom()
            //                );
            //            }
        }
        //offsetRight926  offsetRight980   getWidth168
        //getExtraRightOffset 294 132
        if (!mDrawMarkerViews || !valuesToHighlight())
            return;
        for (int i = 0; i < mIndicesToHighlight.length; i++) {
            Highlight highlight = mIndicesToHighlight[i];
            int xIndex = mIndicesToHighlight[i].getXIndex();
            if (xIndex > minuteHelper.getKLineDatas().size()) {
                break;
            }
            int dataSetIndex = mIndicesToHighlight[i].getDataSetIndex();
            float deltaX = mXAxis != null
                    ? mXAxis.mAxisRange
                    : ((mData == null ? 0.f : mData.getXValCount()) - 1.f);
            if (xIndex <= deltaX && xIndex <= deltaX * mAnimator.getPhaseX()) {
                if (mData.getAllData().size() < i) {
                    return;
                }
                Entry e = getEntry(mIndicesToHighlight[i]); //mData.getEntryForHighlight(mIndicesToHighlight[i]);
                // make sure entry not null
                if (e == null || e.getXIndex() != mIndicesToHighlight[i].getXIndex())
                    continue;
                float[] pos = getMarkerPosition(e, highlight);
                // check bounds
                if (!mViewPortHandler.isInBounds(pos[0], pos[1]))
                    continue;

                if (defaultClickLinsener != null) {
                    defaultClickLinsener.onClick(null, mIndicesToHighlight[i].getXIndex(), mIndicesToHighlight[i]);
                }

                String yChartMax = this.getYChartMax() + "";

                String yChartMin = this.getYChartMin() + "";
                String value;


                if (null != myBottomMarkerView) {
                    value = minuteHelper.getBarEntries().get(xIndex).getVal() + "";
                } else {
                    value = minuteHelper.getKLineDatas().get(mIndicesToHighlight[i].getXIndex()).close.toString() + "";
                }

                BigDecimal bigDecimal = new BigDecimal(yChartMin);

                BigDecimal valueB = new BigDecimal(value).subtract(bigDecimal);

                BigDecimal yChartMaxB = new BigDecimal(yChartMax).subtract(bigDecimal);

                String hightS = (this.getMeasuredHeight() - this.getViewPortHandler().offsetBottom() - this.getViewPortHandler().offsetTop()) + "";

                BigDecimal divB = valueB.multiply(new BigDecimal(hightS)).divide(yChartMaxB, 10, BigDecimal.ROUND_HALF_UP);

                float height = this.getMeasuredHeight() - divB.floatValue() - this.getViewPortHandler().offsetBottom();


                if (null != myMarkerViewH && null == myBottomMarkerView) {
                    myMarkerViewH.refreshContent(e, mIndicesToHighlight[i]);
                    int width = (int) mViewPortHandler.contentWidth();
                    myMarkerViewH.setTvWidth(width);
                    myMarkerViewH.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                    int measuredHeight = myMarkerViewH.getMeasuredHeight();
                    myMarkerViewH.layout(0, 0, width,
                            measuredHeight);
                    myMarkerViewH.draw(canvas, mViewPortHandler.contentLeft(), height);
                }

                if (null != myBottomMarkerView) {
                    KLineBean kLineBean = minuteHelper.getKLineDatas().get(mIndicesToHighlight[i].getXIndex());
                    String time = TimeUtils.millis2String(kLineBean.timestamp * 1000, new SimpleDateFormat("yyyy-MM-dd HH:mm"));
                    time = time + "  " + TimeUtils.getChineseWeek(kLineBean.getTimestamp() * 1000);
                    myBottomMarkerView.setData(time);
                    myBottomMarkerView.refreshContent(e, mIndicesToHighlight[i]);
                    myBottomMarkerView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                    myBottomMarkerView.layout(0, 0,
                            myBottomMarkerView.getMeasuredWidth(),
                            myBottomMarkerView.getMeasuredHeight());
                    float v = pos[0] - myBottomMarkerView.getWidth() / 2;
                    if (v < 0) {
                        v = 0;
                    } else if (v > windowWidth - myBottomMarkerView.getWidth()) {
                        v = windowWidth - myBottomMarkerView.getWidth();
                    }
                    myBottomMarkerView.draw(canvas,
                            v,
                            mViewPortHandler.contentBottom());
                    Log.i("ract", pos[0] - myBottomMarkerView.getWidth() / 2 + "");
                }

                if (null != myBMarkerView) {
                    KLineBean kLineBean = minuteHelper.getKLineDatas().get(mIndicesToHighlight[i].getXIndex());
                    String time = TimeUtils.millis2String(kLineBean.timestamp * 1000, new SimpleDateFormat("yyyy-MM-dd HH:mm"));
                    time = time + "  " + TimeUtils.getChineseWeek(kLineBean.getTimestamp() * 1000);
                    myBMarkerView.setData(time);
                    myBMarkerView.refreshContent(e, mIndicesToHighlight[i]);
                    myBMarkerView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                    myBMarkerView.layout(0, 0, myBMarkerView.getMeasuredWidth(),
                            myBMarkerView.getMeasuredHeight());
                    float v = pos[0] - myBMarkerView.getWidth() / 2;
                    if (v < 0) {
                        v = 0;
                    } else if (v > windowWidth - myBMarkerView.getWidth()) {
                        v = windowWidth - myBMarkerView.getWidth();
                    }
                    myBMarkerView.draw(canvas,
                            v,
                            mViewPortHandler.contentBottom());
                    Log.i("ract", pos[0] - myBMarkerView.getWidth() / 2 + "");

                }

                if (null != myMarkerViewLeft) {
                    //修改标记值
                    float yValForHighlight;
                    if (null != myBottomMarkerView) {
                        yValForHighlight = minuteHelper.getBarEntries().get(xIndex).getVal();
                    } else {
                        Entry entry = getEntry(mIndicesToHighlight[i]);
                        if (entry instanceof CandleEntry) {
                            yValForHighlight = ((CandleEntry) entry).getClose();
                        } else {
                            yValForHighlight = entry.getVal();
                        }
                    }
                    if (myMarkerViewLeft instanceof MarkViewInterface) {
                        ((MarkViewInterface) myMarkerViewLeft).setData(yValForHighlight);
                    }
                    myMarkerViewLeft.refreshContent(e, mIndicesToHighlight[i]);
                    myMarkerViewLeft.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                    myMarkerViewLeft.layout(0, 0, myMarkerViewLeft.getMeasuredWidth(),
                            myMarkerViewLeft.getMeasuredHeight());

                    myMarkerViewLeft.draw(canvas,
                            mViewPortHandler.contentRight(),
                            height - myMarkerViewLeft.getHeight() / 2);

                }
            }
        }
    }

    public interface MarkViewInterface {
        void setData(float num);
    }

    @Override
    protected void drawMarkers(Canvas canvas) {
        this.canvas = canvas;
        drawMarkerView();
    }

    //    @Override
    //    public boolean dispatchTouchEvent(MotionEvent ev) {
    //        if (isDimess) {
    //            onChartSingleTapped(ev);
    //            isDimess = false;
    //            ev.setAction(MotionEvent.ACTION_POINTER_UP);
    //        }
    //        return super.dispatchTouchEvent(ev);
    //    }


    //    public boolean onChartSingleTapped(MotionEvent ev) {
    //        if (getHighlighted() != null) {
    //            if (getHighlighted().length != 0) {
    //                float x = ev.getX();
    //                float y = ev.getY();
    //                if (myInfoMarkerView != null) {
    //                    if (y < myInfoMarkerView.getMeasuredHeight() + CommonUtils.getDimensionPixelSize(R.dimen.trans_35px)) {
    //                        if (x > mlWidth && x < myInfoMarkerView.getMeasuredWidth()) {
    //                            highlightValues(new Highlight[]{});
    //                            invalidate();
    //                            return false;
    //                        } else if (x > mlWidth && x > myInfoMarkerView.getMeasuredWidth()) {
    //                            highlightValues(new Highlight[]{});
    //                            invalidate();
    //                            return false;
    //                        }
    //                    }
    //                }
    //            }
    //        }
    //        return true;
    //    }


    public void highlightValues(Highlight[] highs) {
        super.highlightValues(highs);
    }

    @Override
    protected void init() {
        super.init();
        /*此两处不能重新示例*/
        mAxisRendererRight = new MyYAxisRenderer(mViewPortHandler, mAxisRight, mRightAxisTransformer);
        //        mChartTouchListener = new BarLineChartTouchListener(((BarLineChartBase) this),
        //                mViewPortHandler.getMatrixTouch()) {
        //            @Override
        //            public boolean onSingleTapUp(MotionEvent e) {
        //                if (!onChartSingleTapped(e)) {
        //                    return false;
        //                }
        //                return super.onSingleTapUp(e);
        //            }
        //        };

    }

    public ChartTouchListener getmChartTouchListener() {
        return mChartTouchListener;
    }

    @Override
    public void setData(CombinedData data) {
        super.setData(data);
        mRenderer = new CombinedChartRenderer(this, mAnimator, mViewPortHandler);
        mRenderer.initBuffers();
    }

}