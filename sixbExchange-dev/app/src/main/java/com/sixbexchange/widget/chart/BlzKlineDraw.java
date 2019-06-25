package com.sixbexchange.widget.chart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.ListUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.utils.logger.KLog;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.kline.DataParse;
import com.sixbexchange.entity.bean.kline.KLineBean;
import com.sixbexchange.utils.BigUIUtil;
import com.sixbexchange.utils.UserSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郭青枫 on 2018/1/11 0011.
 */

public class BlzKlineDraw {
    //X轴标签的类
    protected XAxis xAxisKline, xAxisVolume;
    //Y轴左侧的线
    protected YAxis axisLeftKline, axisLeftVolume;
    //Y轴右侧的线
    protected YAxis axisRightKline, axisRightVolume;

    //解析数据
    private DataParse mData;
    //K线图数据
    private ArrayList<KLineBean> kLineDatas;

    protected KCombinedChart mChartKline;
    protected KCombinedChart mChartVolume;

    public static final int pageSize = 300;

    Context mContext;

    // List<String> klineTypeData;

    int getRiseColor;
    int getDropColor;
    int border_color;
    int color_font2;
    int color_e5e5e5;
    int color_999999;
    int color_font4;
    int ma1;
    int ma5;
    int ma7;
    int ma10;
    int ma20;
    int ma30;


    public BlzKlineDraw() {
        getRiseColor = CommonUtils.getColor(UserSet.getinstance().getRiseColor());
        getDropColor = CommonUtils.getColor(UserSet.getinstance().getDropColor());
        border_color = CommonUtils.getColor(R.color.color_f5f5f5);
        color_font2 = CommonUtils.getColor(R.color.color_font2);
        color_e5e5e5 = CommonUtils.getColor(R.color.color_e5e5e5);
        color_999999 = CommonUtils.getColor(R.color.color_999999);


        color_font4 = CommonUtils.getColor(R.color.color_font3);
        ma1 = CommonUtils.getColor(R.color.mark_color);
        ma5 = CommonUtils.getColor(R.color.ma5);
        ma7 = CommonUtils.getColor(R.color.ma7);
        ma10 = CommonUtils.getColor(R.color.ma10);
        ma20 = CommonUtils.getColor(R.color.ma20);
        ma30 = CommonUtils.getColor(R.color.ma30);
        // klineTypeData = Arrays.asList(CommonUtils.getStringArray(R.array.sa_select_kline_show_type));
    }

    public DataParse getmData() {
        return mData;
    }

    public interface OnClick {
        void click(int xPosition, int startPosition, int endPosition);
    }


    //数据库没数据 只初始化视图
    public void initView(Context context, KCombinedChart chartKline, KCombinedChart chartVolume) {
        mContext = context;
        mChartKline = chartKline;
        mChartVolume = chartVolume;

        initChartKline();
        mChartKline.setAutoScaleMinMaxEnabled(true);

        initChartVolume();
        mChartVolume.setAutoScaleMinMaxEnabled(true);

        setChartListener();
    }

    //数据库有数据 初始化视图加数据
    public void initView(DataParse data, Context context, KCombinedChart chartKline, KCombinedChart chartVolume) {
        mContext = context;
        mChartKline = chartKline;
        mChartVolume = chartVolume;

        initChartKline();
        mChartKline.setAutoScaleMinMaxEnabled(true);

        initChartVolume();
        mChartVolume.setAutoScaleMinMaxEnabled(true);

        setChartListener();

        mData = data;
        setKLineDatas();
        mData.initKLineMA(mData.getKLineDatas());
        //mData.initEXPMA(mData.getKLineDatas());
        mData.initVlumeMA(mData.getKLineDatas());
        setMarkerView(mData, mChartKline);
        setMarkerViewButtom(mData, mChartVolume);
    }

    ValueAnimator animator;
    String toShortString = "";
    int index = 0;
    boolean isEnd = false;


    //往前更新180
    public void upUpdataAll(List<KLineBean> datas, String klineType,
                            final View netWorkDialog,
                            final DefaultClickLinsener defaultClickLinsener) {
        int getHighestVisibleXIndex = (int) mChartKline.getHighestVisibleXIndex();
        int getLowestVisibleXIndex = (int) mChartKline.getLowestVisibleXIndex();
        final int datasSize = datas.size() - 1;
        mData.upDataKline(datas, klineType);
        isEnd = false;
        mChartKline.getXAxis().getValues().clear();
        mChartKline.getXAxis().getValues().addAll(mChartVolume.getXAxis().getValues());


        mChartKline.notifyDataSetChanged();
        mChartVolume.notifyDataSetChanged();

        setHandler(mChartKline);
        setHandler(mChartVolume);

        mChartKline.moveViewToX(datasSize);
        mChartVolume.moveViewToX(datasSize);

        mChartKline.invalidate();
        mChartVolume.invalidate();

        if (animator != null) {
            animator.cancel();
        }
        animator = ValueAnimator.ofInt(0, datasSize);
        animator.setDuration(10000);
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mChartKline != null && mChartVolume != null
                        && netWorkDialog != null && !isEnd) {
                    if (mChartKline.getLowestVisibleXIndex() != datasSize) {
                        mChartKline.moveViewToX(datasSize);
                        mChartVolume.moveViewToX(datasSize);
                    }
                    int animatedValue = (int) animation.getAnimatedValue();
                    String str = mChartKline.getViewPortHandler().getMatrixTouch().toShortString();
                    if (!toShortString.equals(str)) {
                        toShortString = str;
                        index = 0;
                    } else {
                        index++;
                    }
                    if (index > 10 && netWorkDialog.getVisibility() == View.VISIBLE) {
                        index = 0;
                        toShortString = "";
                        netWorkDialog.setVisibility(View.GONE);
                        mChartKline.setVisibility(View.VISIBLE);
                        mChartVolume.setVisibility(View.VISIBLE);
                        mChartKline.setTouchEnabled(true);
                        mChartVolume.setTouchEnabled(true);
                        isEnd = true;
                        defaultClickLinsener.onClick(netWorkDialog, 0, null);
                    }
                }
            }
        });

    }

    //单纯初始化数据
    public void initData(DataParse data) {
        mData = data;
        setKLineDatas();

        mData.initKLineMA(kLineDatas);
        //mData.initEXPMA(kLineDatas);
        mData.initVlumeMA(kLineDatas);

        setMarkerView(mData, mChartKline);
        setMarkerViewButtom(mData, mChartVolume);
    }

    public void isFullScreen(boolean isFull) {
        if (isFull) {
            mChartKline.fitScreen();
            mChartVolume.fitScreen();
            mChartVolume.setVisibility(View.GONE);
            mChartKline.setScaleEnabled(false);
            mChartKline.setScaleYEnabled(false);
        } else {
            mChartVolume.setVisibility(View.VISIBLE);
            mChartKline.setScaleEnabled(true);
            mChartKline.setScaleYEnabled(false);
        }
    }

    public void setKlineShowType(int selectType) {
        CandleData candleData = mChartKline.getCandleData();
        LineData lineData = mChartKline.getLineData();
        if (candleData != null && lineData != null) {
            int indexLast = getLastDataSetIndex(candleData);
            CandleDataSet lastSet = (CandleDataSet) candleData.getDataSetByIndex(indexLast);
            LineDataSet lineDataSetMA1 = (LineDataSet) lineData.getDataSetByIndex(0);
            LineDataSet lineDataSetMA5 = (LineDataSet) lineData.getDataSetByIndex(1);
            LineDataSet lineDataSetMA10 = (LineDataSet) lineData.getDataSetByIndex(2);
            LineDataSet lineDataSetMA20 = (LineDataSet) lineData.getDataSetByIndex(3);
            lineDataSetMA1.setDrawFilled(true);
            if (selectType == 0 || selectType == 5) {
                lineDataSetMA1.setVisible(true);
                lastSet.setVisible(false);
                lineDataSetMA5.setVisible(false);
                lineDataSetMA10.setVisible(false);
                lineDataSetMA20.setVisible(false);
                mChartKline.getXAxis().setDrawLabels(true);
                mChartKline.setExtraOffsets(0f, 0f, 0f, 10f);
                mChartKline.setMyBMarkerView(new MyBottomMarkerView(mContext, R.layout.mymarkerview));
                if (selectType == 0) {
                    lineDataSetMA1.setDrawFilled(false);
                    mChartKline.getXAxis().setDrawLabels(false);
                    mChartKline.setExtraOffsets(0f, 0f, 0f, 0f);
                    mChartKline.setMyBMarkerView(null);
                }

            } else {
                lineDataSetMA1.setVisible(false);
                lastSet.setVisible(true);
                lineDataSetMA5.setVisible(true);
                lineDataSetMA10.setVisible(true);
                lineDataSetMA20.setVisible(true);
                mChartKline.getXAxis().setDrawLabels(false);
                mChartKline.setExtraOffsets(0f, 0f, 0f, 0f);
                mChartKline.setMyBMarkerView(null);
            }
        }
        mChartKline.notifyDataSetChanged();
        mChartKline.resetViewPortOffsets();
        setOffset();
        mChartKline.postInvalidateDelayed(300);
        mChartVolume.postInvalidateDelayed(300);
    }

    BlzKlineDraw.OnClick onClick;

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    //视图绑定数据
    public void setData(DataParse data) {
        mData = data;
        setKLineDatas();

        mData.initKLineMA(kLineDatas);
        //mData.initEXPMA(kLineDatas);
        mData.initVlumeMA(kLineDatas);

        setKLineByChart(mChartKline);
        setVolumeByChart(mChartVolume);

        mChartKline.setDefaultClickLinsener(new DefaultClickLinsener() {
            @Override
            public void onClick(View view, int position, Object item) {
                if (onClick != null) {
                    onClick.click(position, (int) mChartKline.getLowestVisibleXIndex(), (int) mChartKline.getHighestVisibleXIndex());
                }
            }
        });
        mChartVolume.setDefaultClickLinsener(new DefaultClickLinsener() {
            @Override
            public void onClick(View view, int position, Object item) {
                if (onClick != null) {
                    onClick.click(position, (int) mChartVolume.getLowestVisibleXIndex(), (int) mChartVolume.getHighestVisibleXIndex());
                }
            }
        });

        setMarkerView(mData, mChartKline);
        setMarkerViewButtom(mData, mChartVolume);

        setOffset();

        setHandler(mChartKline);
        setHandler(mChartVolume);


        Log.i("KlineDraw", "setData");
        mChartKline.notifyDataSetChanged();
        mChartVolume.notifyDataSetChanged();


        mChartKline.moveViewToX(mData.getXVals().size() - 1);
        mChartVolume.moveViewToX(mData.getXVals().size() - 1);
        mChartKline.postInvalidateDelayed(500);
        mChartVolume.postInvalidateDelayed(500);
    }

    public void setData(Context context, DataParse data, KCombinedChart chartKline, KCombinedChart chartVolume) {
        Log.i("KlineDraw", "setData");

        mData = data;
        mContext = context;
        mChartKline = chartKline;
        mChartVolume = chartVolume;

        setKLineDatas();

        Log.i("KlineDraw", "data_ok");


        initChartKline();
        setKLineByChart(mChartKline);
        mChartKline.setAutoScaleMinMaxEnabled(true);
        Log.i("KlineDraw", "kline_ok");


        initChartVolume();
        setVolumeByChart(mChartVolume);
        mChartVolume.setAutoScaleMinMaxEnabled(true);
        Log.i("KlineDraw", "volume_ok");

        setOffset();

        mChartKline.moveViewToX(kLineDatas.size() - 1);
        mChartVolume.moveViewToX(kLineDatas.size() - 1);
        //        mChartKline.setDefaultClickLinsener(new DefaultClickLinsener() {
        //            @Override
        //            public void onClick(View view, int position, Object item) {
        //                if (onClick != null) {
        //                    onClick.click(position, (int) mChartKline.getXChartMin(), (int) mChartKline.getXChartMax());
        //                }
        //            }
        //        });
        //        mChartVolume.setDefaultClickLinsener(new DefaultClickLinsener() {
        //            @Override
        //            public void onClick(View view, int position, Object item) {
        //                if (onClick != null) {
        //                    onClick.click(position, (int) mChartVolume.getXChartMin(), (int) mChartVolume.getXChartMax());
        //                }
        //            }
        //        });
        setMarkerView(mData, chartKline);
        setMarkerViewButtom(mData, chartVolume);
        setChartListener();


        setHandler(mChartKline);
        setHandler(mChartVolume);

        mChartKline.notifyDataSetChanged();
        mChartVolume.notifyDataSetChanged();

        mChartKline.invalidate();
        mChartVolume.invalidate();

    }


    public void updata(KLineBean val, String klineType) {
        if (mData == null) {
            return;
        }
        if (ListUtils.isEmpty(mData.getKLineDatas())) {
            return;
        }
        if (val.timestamp ==
                mData.getKLineDatas().get(mData.getKLineDatas().size() - 1).getTimestamp() ||
                val.timestamp >
                        mData.getKLineDatas().get(mData.getKLineDatas().size() - 1).getTimestamp()) {
            if (TextUtils.isEmpty(val.date)) {
                val.date = mData.getDateTime(val.timestamp, klineType);                    //kLineBeans.get(i).setNewDate(TimeUtils.string2Date(kLineBeans.get(i).date));
            }

            if (val.timestamp ==
                    mData.getKLineDatas().get(mData.getKLineDatas().size() - 1).getTimestamp()) {
                kLineDatas.remove(kLineDatas.size() - 1);
                kLineDatas.add(val);
                mData.initKLineMA(kLineDatas);
                mData.initVlumeMA(kLineDatas);
                updatLastKline(val, true);
                updatLastVolume(val, true);
            } else if (val.timestamp >
                    mData.getKLineDatas().get(mData.getKLineDatas().size() - 1).getTimestamp()) {
                kLineDatas.add(val);
                mData.getXVals().add(val.date);
                mChartKline.getXAxis().getValues().add(val.date);
                mData.initKLineMA(kLineDatas);
                mData.initVlumeMA(kLineDatas);
                updatLastKline(val, false);
                updatLastVolume(val, false);
            }

            setOffset();
            mChartKline.notifyDataSetChanged();
            mChartVolume.notifyDataSetChanged();

            mChartKline.invalidate();
            mChartVolume.invalidate();
        }

    }

    private void updatLastKline(KLineBean kLineBean, boolean isRemove) {
        CandleData candleData = mChartKline.getCandleData();
        LineData lineData = mChartKline.getLineData();

        if (candleData != null) {
            int indexLast = getLastDataSetIndex(candleData);
            CandleDataSet lastSet = (CandleDataSet) candleData.getDataSetByIndex(indexLast);

            if (lastSet == null) {
                lastSet = createCandleDataSet();
                candleData.addDataSet(lastSet);
            }
            //count = lastSet.getEntryCount();
            // 位最后一个DataSet添加entry
            KLog.i("chart", kLineBean.toString());

            if (isRemove) {
                lastSet.getYVals().remove(lastSet.getYVals().size() - 1);
            }
            CandleEntry candleEntry = new CandleEntry(lastSet.getYVals().size(),
                    kLineBean.high.floatValue(), kLineBean.low.floatValue(), kLineBean.open.floatValue(), kLineBean.close.floatValue());
            candleEntry.setData(kLineBean.getTimestamp());
            lastSet.getYVals().add(candleEntry);

        }

        //        LineDataSet lineDataSetMA1 = (LineDataSet) lineData.getDataSetByIndex(0);
        //        if (lineDataSetMA1 != null) {
        //            lineDataSetMA1.removeEntry(lineDataSetMA1.getYVals().size() - 1);
        //            lineDataSetMA1.addEntry(new Entry(kLineBean.getClose().floatValue(), lineDataSetMA1.getYVals().size()));
        //        }
    }

    private void updatLastVolume(KLineBean kLineBean, boolean isRemove) {
        BarData barData = mChartVolume.getBarData();
        LineData lineData = mChartVolume.getLineData();
        if (barData != null) {
            int indexLast = getLastDataSetIndex(barData);
            BarDataSet lastSet = (BarDataSet) barData.getDataSetByIndex(indexLast);
            int index = 0;
            if (lastSet == null) {
                lastSet = createBarDataSet();
                barData.addDataSet(lastSet);
            }

            if (isRemove) {
                lastSet.getYVals().remove(lastSet.getYVals().size() - 1);
            }
            lastSet.getYVals().add(new BarEntry(kLineBean.volume.floatValue(), lastSet.getYVals().size()));

            List<Integer> colors = lastSet.getColors();
            if (isRemove) {
                colors.remove(colors.size() - 1);
            }
            if (kLineBean.getClose().doubleValue() > kLineBean.getOpen().doubleValue()) {
                colors.add(getRiseColor);
            } else {
                colors.add(getDropColor);
            }
            lastSet.setColors(colors);
            //            if (lineData != null) {
            //                LineDataSet lineDataSet5 = (LineDataSet) lineData.getDataSetByIndex(0);//五日均线;
            //                if (lineDataSet5 != null) {
            //                    if(isRemove) {
            //                        lineDataSet5.removeLast();
            //                    }
            //                    lineData.addEntry(mData.getMa5DataV().get(lineDataSet5.getEntryCount()), 0);
            //                }
            //            }
        }


    }

    private void addKlineData(int index) {
        CandleData candleData = mChartKline.getCandleData();
        LineData lineData = mChartKline.getLineData();

        int i = kLineDatas.size() - index;
        KLineBean kLineBean = kLineDatas.get(i);

        if (candleData != null) {
            int indexLast = getLastDataSetIndex(candleData);
            CandleDataSet lastSet = (CandleDataSet) candleData.getDataSetByIndex(indexLast);

            if (lastSet == null) {
                lastSet = createCandleDataSet();
                candleData.addDataSet(lastSet);
            }
            // 位最后一个DataSet添加entry
            KLog.i("chart", kLineBean.toString());
            CandleEntry candleEntry = new CandleEntry(candleData.getYValCount(), kLineBean.high.floatValue(), kLineBean.low.floatValue(), kLineBean.open.floatValue(), kLineBean.close.floatValue());
            candleEntry.setData(kLineBean.getTimestamp());
            candleData.addEntry(candleEntry, indexLast);
        }

        //        if (lineData != null) {
        //            LineDataSet lineDataSetMA7 = (LineDataSet) lineData.getDataSetByIndex(0);//五日均线;
        //            LineDataSet lineDataSetMA30 = (LineDataSet) lineData.getDataSetByIndex(1);//十日均线;
        //            LineDataSet lineDataSetEMA7 = (LineDataSet) lineData.getDataSetByIndex(2);
        //            LineDataSet lineDataSetEMA30 = (LineDataSet) lineData.getDataSetByIndex(3);
        //            LineDataSet lineDataSetEMA1 = (LineDataSet) lineData.getDataSetByIndex(4);
        //            if (lineDataSetMA7 != null) {1532067780
        //                lineData.addEntry(mData.getMa7DataL().get(mData.getMa7DataL().size() - index), 0);
        //            }1532067780 1532067840 1532067900
        //            if (lineDataSetMA30 != null) {
        //                lineData.addEntry(mData.getMa30DataL().get(mData.getMa30DataL().size() - index), 1);
        //            }
        //            if (lineDataSetEMA7 != null) {
        //                lineData.addEntry(mData.getExpmaData7().get(mData.getExpmaData7().size() - index), 2);
        //            }
        //            if (lineDataSetEMA30 != null) {
        //                lineData.addEntry(mData.getExpmaData30().get(mData.getExpmaData30().size() - index), 3);
        //            }
        //            if (lineDataSetEMA1 != null) {
        //                lineData.addEntry(mData.getMa1DataL().get(mData.getMa1DataL().size() - index), 4);
        //            }
        //        }
        //        if (lineData != null) {
        //            LineDataSet lineDataSetMA1 = (LineDataSet) lineData.getDataSetByIndex(0);
        //            if (lineDataSetMA1 != null) {
        //                lineData.addEntry(mData.getMa1DataL().get(mData.getMa1DataL().size() - index), 0);
        //            }
        //            LineDataSet lineDataSetMA5 = (LineDataSet) lineData.getDataSetByIndex(1);//五日均线;
        //            LineDataSet lineDataSetMA10 = (LineDataSet) lineData.getDataSetByIndex(2);//十日均线;
        //            LineDataSet lineDataSetMA20 = (LineDataSet) lineData.getDataSetByIndex(3);//十日均线;
        //            if (lineDataSetMA5 != null) {
        //                lineData.addEntry(mData.getMa5DataL().get(mData.getMa5DataL().size() - index), 1);
        //            }
        //            if (lineDataSetMA10 != null) {
        //                lineData.addEntry(mData.getMa10DataL().get(mData.getMa10DataL().size() - index), 2);
        //            }
        //            if (lineDataSetMA20 != null) {
        //                lineData.addEntry(mData.getMa20DataL().get(mData.getMa20DataL().size() - index), 3);
        //            }
        //        }
    }

    private void addVolumeData(int index) {
        BarData barData = mChartVolume.getBarData();
        LineData lineData = mChartVolume.getLineData();

        int count = 0;

        int i = kLineDatas.size() - index;
        KLineBean kLineBean = kLineDatas.get(i);


        if (barData != null) {
            int indexLast = getLastDataSetIndex(barData);
            BarDataSet lastSet = (BarDataSet) barData.getDataSetByIndex(indexLast);
            if (lastSet == null) {
                lastSet = createBarDataSet();
                barData.addDataSet(lastSet);
            }
            List<Integer> colors = lastSet.getColors();
            BarEntry barEntry = new BarEntry(kLineBean.volume.floatValue(), colors.size());
            barData.addEntry(barEntry, indexLast);
            if (kLineBean.getClose().doubleValue() > kLineBean.getOpen().doubleValue()) {
                colors.add(getRiseColor);
            } else {
                colors.add(getDropColor);
            }
            lastSet.setColors(colors);

        }

        if (lineData != null) {
            LineDataSet lineDataSet5 = (LineDataSet) lineData.getDataSetByIndex(0);//五日均线;
            //LineDataSet lineDataSet10 = (LineDataSet) lineData.getDataSetByIndex(1);//十日均线;

            if (lineDataSet5 != null) {
                lineData.addEntry(mData.getMa5DataV().get(mData.getMa5DataV().size() - index), 0);
            }

            //                    if (lineDataSet10 != null) {
            //                        lineData.addEntry(mData.getMa10DataV().get(mData.getMa10DataV().size() - index), 1);
            //                    }
        }
    }

    /**
     * 获取最后一个LineDataSet的索引
     */
    private int getDataSetIndexCount(LineData lineData) {
        int dataSetCount = lineData.getDataSetCount();
        return dataSetCount;
    }

    /**
     * 获取最后一个CandleDataSet的索引
     */
    private int getLastDataSetIndex(BarData barData) {
        int dataSetCount = barData.getDataSetCount();
        return dataSetCount > 0 ? (dataSetCount - 1) : 0;
    }

    /**
     * 获取最后一个CandleDataSet的索引
     */
    private int getLastDataSetIndex(CandleData candleData) {
        int dataSetCount = candleData.getDataSetCount();
        return dataSetCount > 0 ? (dataSetCount - 1) : 0;
    }

    private CandleDataSet createCandleDataSet() {
        CandleDataSet dataSet = new CandleDataSet(null, "DataSet 1");
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setValueTextSize(12f);

        return dataSet;
    }

    private LineDataSet createLineDataSet() {
        LineDataSet dataSet = new LineDataSet(null, "DataSet 1");
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setValueTextSize(12f);

        return dataSet;
    }

    private BarDataSet createBarDataSet() {
        BarDataSet dataSet = new BarDataSet(null, "DataSet 1");
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setValueTextSize(12f);

        return dataSet;
    }

    private void setKLineDatas() {
        kLineDatas = mData.getKLineDatas();
        mData.initLineDatas(kLineDatas);
    }

    Paint mPaint = new Paint();

    /**
     * 初始化上面的chart公共属性
     */
    private void initChartKline() {
        mChartKline.setScaleEnabled(true);//启用图表缩放事件
        mChartKline.setDrawBorders(true);//是否绘制边线
        mChartKline.setBorderWidth(1);//边线宽度，单位dp
        mChartKline.setDragEnabled(true);//启用图表拖拽事件
        mChartKline.setScaleYEnabled(false);//启用Y轴上的缩放
        mChartKline.setBorderColor(border_color);//边线颜色
        mChartKline.setDescription("");//右下角对图表的描述信息
        mChartKline.setMinOffset(0f);
        mChartKline.setExtraOffsets(0f, 0f, 0f, 0f);


        Legend lineChartLegend = mChartKline.getLegend();
        lineChartLegend.setEnabled(false);//是否绘制 Legend 图例
        lineChartLegend.setForm(Legend.LegendForm.CIRCLE);


        //bar x y轴
        xAxisKline = mChartKline.getXAxis();
        xAxisKline.setEnabled(true);
        xAxisKline.setDrawLabels(false); //是否显示X坐标轴上的刻度，默认是true
        xAxisKline.setDrawGridLines(false);//是否显示X坐标轴上的刻度竖线，默认是true
        xAxisKline.setDrawAxisLine(true); //是否绘制坐标轴的线，即含有坐标的那条线，默认是true
        xAxisKline.setGridColor(border_color);
        xAxisKline.setAxisLineColor(border_color);
        xAxisKline.setTextSize(9f);
        //xAxisKline.enableGridDashedLine(10f, 10f, 0f);//虚线表示X轴上的刻度竖线(float lineLength, float spaceLength, float phase)三个参数，1.线长，2.虚线间距，3.虚线开始坐标
        xAxisKline.setTextColor(color_font4);//设置字的颜色
        xAxisKline.setPosition(XAxis.XAxisPosition.BOTTOM);//设置值显示在什么位置
        xAxisKline.setAvoidFirstLastClipping(true);//设置首尾的值是否自动调整，避免被遮挡
        xAxisKline.setLabelsToSkip(30);


        axisLeftKline = mChartKline.getAxisRight();
        axisLeftKline.setEnabled(true);
        axisLeftKline.setDrawGridLines(false);
        axisLeftKline.setDrawAxisLine(true);
        axisLeftKline.setDrawZeroLine(true);
        axisLeftKline.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeftKline.setZeroLineColor(border_color);
        axisLeftKline.setGridColor(border_color);
        axisLeftKline.setAxisLineColor(border_color);
        axisLeftKline.setDrawLabels(true);
        //axisLeftKline.enableGridDashedLine(10f, 10f, 0f);
        axisLeftKline.setTextColor(color_font4);
        axisLeftKline.setTextSize(9.4f);
        //        axisLeftKline.setGridColor(CommonUtils.getColor(R.color.minute_grayLine));
        axisLeftKline.setLabelCount(5, false); //第一个参数是Y轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布
        axisLeftKline.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                return getFormattedString(BigUIUtil.getinstance().bigPrice(value + ""));
            }
        });


        axisRightKline = mChartKline.getAxisLeft();
        axisRightKline.setDrawLabels(false);
        axisRightKline.setDrawGridLines(true);
        axisRightKline.setDrawZeroLine(true);
        axisRightKline.setGridColor(border_color);
        axisRightKline.setZeroLineColor(border_color);
        axisRightKline.setAxisLineColor(border_color);
        axisRightKline.setDrawAxisLine(true);
        axisRightKline.setLabelCount(5, false); //第一个参数是Y轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布
        axisRightKline.setDrawTopYLabelEntry(false);

        mChartKline.setDragDecelerationEnabled(true);
        mChartKline.setDragDecelerationFrictionCoef(0.9f);


        setHandler(mChartKline);
    }

    private String getFormattedString(String text) {
        StringBuffer price = new StringBuffer(text);
        if (mChartKline.getMyNowPriceMarkerView() != null) {
            int width = mChartKline.getMyNowPriceMarkerView().getWidth();
            mPaint.setTextSize(CommonUtils.getDimensionPixelSize(R.dimen.text_trans_18px));
            while (true) {
                price.append("\t");
                price.insert(0, "\t");
                float textWidth2 = mPaint.measureText(price.toString());
                if (textWidth2 > width) {
                    price.deleteCharAt(0);
                    price.deleteCharAt(price.length() - 1);
                    price.deleteCharAt(0);
                    price.deleteCharAt(price.length() - 1);
                    break;
                }
            }
        }
        return price.toString();
    }

    /**
     * 初始化下面的chart公共属性
     */
    private void initChartVolume() {
        mChartVolume.setDrawBorders(true);  //边框是否显示
        mChartVolume.setBorderWidth(1);//边框的宽度，float类型，dp单位
        mChartVolume.setBorderColor(border_color);//边框颜色
        mChartVolume.setDescription(""); //图表默认右下方的描述，参数是String对象
        mChartVolume.setDragEnabled(true);// 是否可以拖拽
        mChartVolume.setScaleYEnabled(false); //是否可以缩放 仅y轴
        mChartVolume.setMinOffset(0f);
        mChartVolume.setExtraOffsets(0f, 0f, 0f, 5.22f);
        mChartVolume.setDrawHighlightArrow(false);
        mChartVolume.setDrawMarkerViews(true);


        Legend combinedchartLegend = mChartVolume.getLegend(); // 设置比例图标示，就是那个一组y的value的
        combinedchartLegend.setEnabled(false);//是否绘制比例图
        combinedchartLegend.setForm(Legend.LegendForm.CIRCLE);


        //bar x y轴
        xAxisVolume = mChartVolume.getXAxis();
        xAxisVolume.setEnabled(true);
        xAxisVolume.setDrawGridLines(false);//是否显示X坐标轴上的刻度竖线，默认是true
        xAxisVolume.setDrawAxisLine(true); //是否绘制坐标轴的线，即含有坐标的那条线，默认是true
        xAxisVolume.setDrawLabels(true); //是否显示X坐标轴上的刻度，默认是true
        xAxisVolume.setTextColor(color_font4);//设置字的颜色
        xAxisVolume.setPosition(XAxis.XAxisPosition.BOTTOM);//设置值显示在什么位置
        xAxisVolume.setAvoidFirstLastClipping(true);//设置首尾的值是否自动调整，避免被遮挡
        xAxisVolume.setTextSize(9f);
        xAxisVolume.setGridColor(border_color);
        xAxisVolume.setAxisLineColor(border_color);
        xAxisVolume.setLabelsToSkip(30);


        axisLeftVolume = mChartVolume.getAxisRight();
        axisLeftVolume.setEnabled(true);
        axisLeftVolume.setAxisMinValue(0);//设置Y轴坐标最小为多少
        axisLeftVolume.setDrawGridLines(false);
        axisLeftVolume.setDrawAxisLine(true);
        axisLeftVolume.setDrawZeroLine(true);
        axisLeftVolume.setDrawLabels(true);
        axisLeftVolume.setZeroLineColor(border_color);
        axisLeftVolume.setGridColor(border_color);
        axisLeftVolume.setAxisLineColor(border_color);
        axisLeftVolume.setTextSize(9.4f);
        //axisLeftVolume.enableGridDashedLine(10f, 10f, 0f);
        axisLeftVolume.setTextColor(color_font4);
        axisLeftVolume.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisLeftVolume.setLabelCount(3, false); //第一个参数是Y轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布
        axisLeftVolume.setDrawTopYLabelEntry(false);
        axisLeftVolume.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                return getFormattedString(BigUIUtil.getinstance().bigAmount(value + ""));
            }
        });


        axisRightVolume = mChartVolume.getAxisLeft();
        axisRightVolume.setEnabled(false);
        axisRightVolume.setDrawLabels(false);
        axisRightVolume.setDrawGridLines(false);
        axisRightVolume.setDrawAxisLine(false);
        axisRightVolume.setDrawZeroLine(true);
        axisRightVolume.setGridColor(border_color);
        axisRightVolume.setZeroLineColor(border_color);
        axisRightVolume.setAxisLineColor(border_color);
        axisRightVolume.setLabelCount(3, false); //第一个参数是Y轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布
        axisRightVolume.setDrawTopYLabelEntry(false);

        mChartVolume.setDragDecelerationEnabled(true);
        mChartVolume.setDragDecelerationFrictionCoef(0.9f);
        setHandler(mChartVolume);
    }

    private void setKLineByChart(CombinedChart combinedChart) {
        CandleDataSet set = new CandleDataSet(mData.getCandleEntries(), "");
        set.setDrawHorizontalHighlightIndicator(false);
        set.setHighlightEnabled(true);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setShadowWidth(1f);
        set.setValueTextSize(10f);
        set.setDecreasingColor(getDropColor);//设置开盘价高于收盘价的颜色
        set.setDecreasingPaintStyle(Paint.Style.FILL);
        set.setIncreasingColor(getRiseColor);//设置开盘价地狱收盘价的颜色
        set.setIncreasingPaintStyle(Paint.Style.FILL);
        set.setNeutralColor(getDropColor);//设置开盘价等于收盘价的颜色
        set.setShadowColorSameAsCandle(true);
        set.setHighlightLineWidth(0.5f);
        set.setHighLightColor(color_999999);
        set.setDrawValues(true);
        set.setValueTextColor(color_font2);
        set.setVisible(true);


        List<String> times = new ArrayList<>();
        times.addAll(mData.getXVals());
        CandleData candleData = new CandleData(times, set);


        ArrayList<ILineDataSet> sets = new ArrayList<>();
        /******此处修复如果显示的点的个数达不到MA均线的位置所有的点都从0开始计算最小值的问题******************************/
        LineDataSet lineDataSetMA1 = setMaLine(1, mData.getXVals(), mData.getMa1DataL());
        LineDataSet lineDataSetMA5 = setMaLine(5, mData.getXVals(), mData.getMa5DataL());
        lineDataSetMA5.setHighlightEnabled(false);
        LineDataSet lineDataSetMA10 = setMaLine(10, mData.getXVals(), mData.getMa10DataL());
        LineDataSet lineDataSetMA20 = setMaLine(20, mData.getXVals(), mData.getMa20DataL());
        //        LineDataSet lineDataSetEMA7 = setKDJMaLine(7, mData.getXVals(), (ArrayList<Entry>) mData.getExpmaData7());
        //        LineDataSet lineDataSetEMA30 = setKDJMaLine(30, mData.getXVals(), (ArrayList<Entry>) mData.getExpmaData30());

        lineDataSetMA1.setDrawFilled(true);
        lineDataSetMA1.setFillDrawable(CommonUtils.getDrawable(R.drawable.kline_blue));

        boolean isMA = false;
        boolean isEMA = false;


        //lineDataSetMA7.setVisible(isMA);
        //lineDataSetMA30.setVisible(isMA);
        //lineDataSetEMA7.setVisible(isEMA);
        //lineDataSetEMA30.setVisible(isEMA);
        lineDataSetMA1.setVisible(false);

        sets.add(lineDataSetMA1);
        sets.add(lineDataSetMA5);
        sets.add(lineDataSetMA10);
        sets.add(lineDataSetMA20);

        lineDataSetMA5.setVisible(true);
        lineDataSetMA10.setVisible(true);
        lineDataSetMA20.setVisible(true);

        //sets.add(lineDataSetEMA7);
        //sets.add(lineDataSetEMA30);


        LineData lineData = new LineData(times, sets);

        CombinedData combinedData = new CombinedData(times);

        combinedData.setData(candleData);
        combinedData.setData(lineData);

        combinedChart.setData(combinedData);
        combinedChart.setDrawHighlightArrow(true);

        setHandler(combinedChart);

    }


    private void setVolumeByChart(CombinedChart combinedChart) {
        BarDataSet set = new BarDataSet(mData.getBarEntries(), "成交量");
        set.setBarSpacePercent(20); //bar空隙
        set.setHighlightEnabled(true);
        set.setDrawValues(false);
        set.setValueTextColor(color_font2);
        set.setHighLightColor(color_font2);

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < mData.getKLineDatas().size(); i++) {
            if (mData.getKLineDatas().get(i).getClose().doubleValue()
                    > mData.getKLineDatas().get(i).getOpen().doubleValue()) {
                list.add(getRiseColor);
            } else {
                list.add(getDropColor);
            }
        }
        set.setColors(list);

        BarData barData = new BarData(mData.getXVals(), set);

        ArrayList<ILineDataSet> sets = new ArrayList<>();

        /******此处修复如果显示的点的个数达不到MA均线的位置所有的点都从0开始计算最小值的问题******************************/
        LineDataSet lineDataSet = setMaLine(5, mData.getXVals(), mData.getMa5DataV());
        lineDataSet.setVisible(false);
        sets.add(lineDataSet);
        //        lineDataSet.setVisible(false);
        //        LineDataSet lineDataSet1 = MyUtils.setMaLine(10, mData.getXVals(), mData.getMa10DataV());
        //        sets.add(lineDataSet1);
        //        lineDataSet1.setVisible(false);

        LineData lineData = new LineData(mData.getXVals(), sets);

        CombinedData combinedData = new CombinedData(mData.getXVals());
        combinedData.setData(barData);
        combinedData.setData(lineData);

        combinedChart.setData(combinedData);

        combinedChart.setDrawHighlightArrow(true);
        setHandler(combinedChart);

    }

    float minXRange = 20;
    float maxXRange = 200;
    float xscale = 0;

    public void setHandler(CombinedChart combinedChart) {
        final ViewPortHandler viewPortHandlerBar = combinedChart.getViewPortHandler();
        //设置最大 缩小x
        //viewPortHandlerBar.setMinimumScaleX(MyUtils.culcMaxscale(minXRange));
        xscale = UserSet.getinstance().getKlineScale();
        float i = 1;
        Matrix touchmatrix = viewPortHandlerBar.getMatrixTouch();
        if (mData != null) {
            if (!ListUtils.isEmpty(mData.getXVals())) {
                i = ((float) mData.getXVals().size() / (float) pageSize);
            }
        }
        //mChartKline.getViewPortHandler().setZoom(xscale,1f);
        //touchmatrix.postScale(xscale / i, 1f);
        touchmatrix.reset();
        viewPortHandlerBar.setMaximumScaleX(10 * i);
        viewPortHandlerBar.setMinimumScaleX(2 * i);
        //viewPortHandlerBar.setZoom(xscale * i, 1f);
        //viewPortHandlerBar.getMatrixTouch().reset();
        touchmatrix.postScale(xscale * i, 1f);

        // viewPortHandlerBar.setZoom(xscale * i, 1f);

        //combinedChart.setVisibleXRange(minXRange, maxXRange);
    }

    public void cleanData() {
        if (mData != null) {
            if (mData.getKLineDatas() != null) {
                if (mData.getKLineDatas().size() > 0) {
                    //每次清除前记录缩放级别
                    if (mChartKline.getViewPortHandler().getMinScaleX() != 1) {
                        float v = mChartKline.getViewPortHandler().getScaleX();
                        v = (v * pageSize) / mData.getKLineDatas().size();
                        UserSet.getinstance().setKlineScale(v);
                    }
                    //清除
                    mData.getKLineDatas().clear();
                    mData.getXVals().clear();
                    mData.getCandleEntries().clear();
                    mData.getBarEntries().clear();
                    mChartKline.notifyDataSetChanged();
                    mChartVolume.notifyDataSetChanged();
                    mChartKline.clear();
                    mChartVolume.clear();
                }
            }
        }
    }

    private void setMarkerViewButtom(DataParse mData, KCombinedChart combinedChart) {
        MyVolumeLeftMarkerView leftMarkerView = new MyVolumeLeftMarkerView(mContext, R.layout.myvolumeview);
        MyHMarkerView hMarkerView = new MyHMarkerView(mContext, R.layout.mymarkerview_line);
        MyBottomMarkerView bottomMarkerView;
        bottomMarkerView = new MyBottomMarkerView(mContext, R.layout.mymarkerview);
        combinedChart.setMarker(leftMarkerView, bottomMarkerView, hMarkerView, mData);

    }

    private void setMarkerView(DataParse mData, KCombinedChart combinedChart) {
        MyLeftMarkerView leftMarkerView = new MyLeftMarkerView(mContext, R.layout.mymarkerview_price);
        MyHMarkerView hMarkerView = new MyHMarkerView(mContext, R.layout.mymarkerview_line);

        MyInfoMarkerView myInfoMarkerView = new MyInfoMarkerView(mContext, R.layout.layout_kline_info, mData);
        combinedChart.setMarker(leftMarkerView, hMarkerView, mData);

        combinedChart.setMyInfoMarkerView(myInfoMarkerView);
    }

    @NonNull
    private LineDataSet setMACDMaLine(int type, ArrayList<String> xVals, ArrayList<Entry> lineEntries) {
        LineDataSet lineDataSetMa = new LineDataSet(lineEntries, "ma" + type);
        lineDataSetMa.setHighlightEnabled(false);
        lineDataSetMa.setDrawValues(false);

        //DEA
        if (type == 0) {
            lineDataSetMa.setColor(CommonUtils.getColor(R.color.ma5));
        } else {
            lineDataSetMa.setColor(CommonUtils.getColor(R.color.ma10));
        }

        lineDataSetMa.setLineWidth(1f);
        lineDataSetMa.setDrawCircles(false);
        lineDataSetMa.setAxisDependency(YAxis.AxisDependency.LEFT);

        return lineDataSetMa;
    }

    @NonNull
    private LineDataSet setKDJMaLine(int ma, ArrayList<String> xVals, ArrayList<Entry> lineEntries) {
        LineDataSet lineDataSetMa = new LineDataSet(lineEntries, "ma" + ma);
        lineDataSetMa.setHighlightEnabled(false);
        lineDataSetMa.setDrawValues(false);

        //DEA
        if (ma == 5) {
            lineDataSetMa.setColor(CommonUtils.getColor(R.color.ma5));
        } else if (ma == 7) {
            lineDataSetMa.setColor(ma7);
        } else if (ma == 10) {
            lineDataSetMa.setColor(ma10);
        } else if (ma == 20) {
            lineDataSetMa.setColor(ma20);
        } else {
            lineDataSetMa.setColor(ma30);
        }

        lineDataSetMa.setLineWidth(1f);
        lineDataSetMa.setDrawCircles(false);
        lineDataSetMa.setAxisDependency(YAxis.AxisDependency.LEFT);

        return lineDataSetMa;
    }

    @NonNull
    public LineDataSet setMaLine(int ma, ArrayList<String> xVals, ArrayList<Entry> lineEntries) {
        LineDataSet lineDataSetMa = new LineDataSet(lineEntries, "ma" + ma);
        if (ma == 5) {
            //此处 设置 显示 量图表 的 十字线 的 竖线
            lineDataSetMa.setHighlightEnabled(true);
            lineDataSetMa.setDrawHorizontalHighlightIndicator(false);
        } else {/*此处必须得写*/
            lineDataSetMa.setHighlightEnabled(false);
        }
        lineDataSetMa.setLineWidth(0.5f);
        lineDataSetMa.setHighLightColor(color_999999);
        lineDataSetMa.setDrawValues(false);
        if (ma == 1) {
            lineDataSetMa.setColor(ma1);
            lineDataSetMa.setLineWidth(1f);
        } else if (ma == 5) {
            lineDataSetMa.setColor(ma5);
        } else if (ma == 7) {
            lineDataSetMa.setColor(ma7);
        } else if (ma == 10) {
            lineDataSetMa.setColor(ma10);
        } else if (ma == 20) {
            lineDataSetMa.setColor(ma20);
        } else {
            lineDataSetMa.setColor(ma30);
        }
        lineDataSetMa.setDrawCircles(false);
        lineDataSetMa.setAxisDependency(YAxis.AxisDependency.LEFT);
        return lineDataSetMa;
    }

    private void setChartListener() {
        // 将K线控的滑动事件传递给交易量控件
        mChartKline.setOnChartGestureListener(new CoupleChartGestureListener(mChartKline, new Chart[]{mChartVolume}) {
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
                super.onChartTranslate(me, dX, dY);
                //                if (onClick != null) {
                //                    onClick.click(-1, (int) mChartKline.getXChartMin(), (int) mChartKline.getXChartMax());
                //                }
            }
        });
        // 将交易量控件的滑动事件传递给K线控件
        mChartVolume.setOnChartGestureListener(new CoupleChartGestureListener(mChartVolume, new Chart[]{mChartKline}) {
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
                super.onChartTranslate(me, dX, dY);
                //                if (onClick != null) {
                //                    onClick.click(-1, (int) mChartVolume.getXChartMin(), (int) mChartVolume.getXChartMax());
                //                }
            }


        });

        mChartKline.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                Highlight highlight = new Highlight(h.getXIndex(), h.getValue(),
                        0, h.getDataSetIndex());
                mChartVolume.highlightValues(new Highlight[]{highlight});
            }

            @Override
            public void onNothingSelected() {
                mChartVolume.highlightValue(null);
            }
        });

        mChartVolume.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                Highlight highlight = new Highlight(h.getXIndex(), h.getValue(),
                        1, h.getDataSetIndex());
                mChartKline.highlightValues(new Highlight[]{highlight});
            }

            @Override
            public void onNothingSelected() {
                mChartKline.highlightValue(null);
            }
        });
    }


    /*设置量表对齐*/
    private void setOffset() {
        float lineLeft = mChartKline.getViewPortHandler().offsetLeft();
        float kbLeft = mChartVolume.getViewPortHandler().offsetLeft();

        float lineRight = mChartKline.getViewPortHandler().offsetRight();
        float kbRight = mChartVolume.getViewPortHandler().offsetRight();

        if (mChartKline.getMyNowPriceMarkerView() != null) {
            if (lineRight < mChartKline.getMyNowPriceMarkerView().getWidth()) {
                lineRight = mChartKline.getMyNowPriceMarkerView().getWidth();
            }
        }


        float offsetLeft, offsetRight;
        float transLeft = 0, transRight = 0;
 /*注：setExtraLeft...函数是针对图表相对位置计算，比如A表offLeftA=20dp,B表offLeftB=30dp,则A.setExtraLeftOffset(10),并不是30，还有注意单位转换*/
        if (kbLeft < lineLeft) {
           /* offsetLeft = Utils.convertPixelsToDp(lineLeft - barLeft);
            barChart.setExtraLeftOffset(offsetLeft);*/
            transLeft = lineLeft;
        } else {
            offsetLeft = Utils.convertPixelsToDp(kbLeft - lineLeft);
            mChartKline.setExtraLeftOffset(offsetLeft);
            mChartVolume.setExtraLeftOffset(offsetLeft);
            transLeft = kbLeft;
        }
  /*注：setExtraRight...函数是针对图表绝对位置计算，比如A表offRightA=20dp,B表offRightB=30dp,则A.setExtraLeftOffset(30),并不是10，还有注意单位转换*/
        if (kbRight < lineRight) {
          /*  offsetRight = Utils.convertPixelsToDp(lineRight);
            barChart.setExtraRightOffset(offsetRight);*/
            transRight = lineRight;
        } else {
            offsetRight = Utils.convertPixelsToDp(kbRight - lineRight);
            mChartKline.setExtraRightOffset(offsetRight);
            mChartVolume.setExtraRightOffset(offsetRight);
            transRight = kbRight;
        }
        mChartKline.setViewPortOffsets(transLeft, 0, transRight,
                mChartKline.getViewPortHandler().offsetBottom());
        mChartVolume.setViewPortOffsets(transLeft, 0, transRight,
                mChartVolume.getViewPortHandler().offsetBottom());
    }

}
