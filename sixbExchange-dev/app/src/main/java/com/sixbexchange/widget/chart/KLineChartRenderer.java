package com.sixbexchange.widget.chart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.renderer.CombinedChartRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.sixbexchange.utils.UserSet;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 郭青枫 on 2018/1/12 0012.
 */

public class KLineChartRenderer extends CombinedChartRenderer {
    float hLength = Utils.convertDpToPixel(15f);//横线长15dp

    float vLength = Utils.convertDpToPixel(10f);//竖线长10dp

    float rect = Utils.convertDpToPixel(8f);//矩形高低差/2

    float textX = Utils.convertDpToPixel(2f);//文本x坐标偏移量

    float textY = Utils.convertDpToPixel(3f);//文本y偏移量

    boolean isShowHLPoint = true;//是否显示最高点和最低点标识,默认显示

    int mWidth;//屏幕宽度,在构造方法中传进来赋值

    float textSixe = 10f;//文字大小

    CombinedChart combinedChart;

    public KLineChartRenderer(CombinedChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler, int width) {
        super(chart, animator, viewPortHandler);
        combinedChart = chart;
        mWidth = width;
    }

    @Override
    public void drawValues(Canvas c) {
        if (isShowHLPoint) {
            //只画 第0条 设置 k线为 第0条
            CandleDataSet dataSetByIndex = (CandleDataSet) combinedChart.getCandleData().getDataSetByIndex(0);

            Transformer trans = combinedChart.getTransformer(dataSetByIndex.getAxisDependency());
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿画笔
            paint.setTextSize(Utils.convertDpToPixel(textSixe));//设置字体大小

            //画高点标记
            drawHighPoint(dataSetByIndex, trans, paint, c);
            //画低点标记
            drawLowPoint(dataSetByIndex, trans, paint, c);

        }
        super.drawValues(c);
    }

    private float[] getPoint(LineDataSet dataSetByIndex, float[] minFloat) {
        float[] point = new float[2];
        float yChartMax = combinedChart.getYChartMax();//图像最大y
        float xChartMax = combinedChart.getXChartMax();//图像最大x

        float yChartMin = combinedChart.getYChartMin();//图像最小y
        float xChartMin = combinedChart.getXChartMin();//图像最小x

        PointF centerOffsets = combinedChart.getCenterOffsets();


        float yMin = dataSetByIndex.getYMin();//y轴数据最xiao值
        float yMax = dataSetByIndex.getYMax();//y轴数据最da值

        int lowestVisibleXIndex = combinedChart.getLowestVisibleXIndex();//返回 能见到的X的最小值下标 返回值是int类型
        int highestVisibleXIndex = combinedChart.getHighestVisibleXIndex();//返回 能见到的X的最大值下标 返回值是int类型


        point[0] = (minFloat[0] - lowestVisibleXIndex) / (highestVisibleXIndex - lowestVisibleXIndex) * (xChartMax - xChartMin);
        point[1] = (minFloat[1] - yMax) / (yMax - yMin) * (yChartMax - yChartMin);


        return point;
    }


    private void drawLowPoint(CandleDataSet dataSetByIndex, Transformer trans, Paint paint, Canvas c) {
        int lowestVisibleXIndex = combinedChart.getLowestVisibleXIndex();
        int highestVisibleXIndex = combinedChart.getHighestVisibleXIndex();

        float[] minFloat = getMinFloat(dataSetByIndex.getYVals(), lowestVisibleXIndex, highestVisibleXIndex);//根据数据集获取最低点

        //float[] point = getPoint(dataSetByIndex, minFloat);

        //float width = ((minFloat[0] - combinedChart.getLowestVisibleXIndex()) / (combinedChart.getHighestVisibleXIndex() - combinedChart.getLowestVisibleXIndex())) * (combinedChart.getMinimumWidth() - combinedChart.getViewPortHandler().offsetRight());

        float v1 = minFloat[0];
        float v3 = combinedChart.getMeasuredWidth() - combinedChart.getViewPortHandler().offsetRight() - combinedChart.getViewPortHandler().offsetLeft();
        float v4 = combinedChart.getViewPortHandler().offsetRight();
        float v5 = combinedChart.getViewPortHandler().offsetLeft();
        int measuredWidth = combinedChart.getMeasuredWidth();
        float v2 = ((minFloat[0] - lowestVisibleXIndex) / (highestVisibleXIndex - lowestVisibleXIndex)) * (combinedChart.getMeasuredWidth() - combinedChart.getViewPortHandler().offsetRight() - combinedChart.getViewPortHandler().offsetLeft());
        float width = v2 + combinedChart.getViewPortHandler().offsetLeft();


        String yChartMax = combinedChart.getYChartMax() + "";
        String yChartMin = combinedChart.getYChartMin() + "";
        String value = minFloat[1] + "";
        BigDecimal bigDecimal = new BigDecimal(yChartMin);
        BigDecimal valueB = new BigDecimal(value).subtract(bigDecimal);
        BigDecimal yChartMaxB = new BigDecimal(yChartMax).subtract(bigDecimal);

        String hightS = (combinedChart.getMeasuredHeight() - combinedChart.getViewPortHandler().offsetBottom() - combinedChart.getViewPortHandler().offsetTop()) + "";
        BigDecimal divB = valueB.multiply(new BigDecimal(hightS)).divide(yChartMaxB, 10, BigDecimal.ROUND_HALF_UP);

        float height = combinedChart.getMeasuredHeight() - divB.floatValue() - combinedChart.getViewPortHandler().offsetBottom();


        //通过trans得到最低点的屏幕位置
        // PointD minPoint = trans.getValuesByTouchPoint(width, height);

        float lowX = width;
        float lowY = height;
        paint.setColor(CommonUtils.getColor(UserSet.getinstance().getDropColor()));
        float rectLength = Utils.convertDpToPixel((minFloat[1] + "").length() * Utils.convertDpToPixel(2f));//矩形框长
        //画横竖线
        c.drawLine(lowX, lowY, lowX, lowY + vLength, paint);
        if (lowX > mWidth - mWidth / 3) {//标识朝左
            c.drawLine(lowX, lowY + vLength, lowX - hLength, lowY + vLength, paint);
            //画矩形
            //c.drawRect(new Rect((int) (lowX - hLength - rectLength), (int) (lowY + vLength - rect), (int) (lowX - hLength), (int) (lowY + vLength + rect)), paint);
            //写数字
            paint.setColor(CommonUtils.getColor(UserSet.getinstance().getDropColor()));
            c.drawText(minFloat[1] + "", lowX - rectLength - hLength + textX, lowY + vLength + textY, paint);
        } else {//标识朝右
            c.drawLine(lowX, lowY + vLength, lowX + hLength, lowY + vLength, paint);
            //c.drawRect(new Rect((int) (lowX + hLength), (int) (lowY + vLength - rect), (int) (lowX + hLength + rectLength), (int) (lowY + vLength + rect)), paint);
            paint.setColor(CommonUtils.getColor(UserSet.getinstance().getDropColor()));
            c.drawText(minFloat[1] + "", lowX + hLength + textX, lowY + vLength + textY, paint);
        }

    }

    private void drawHighPoint(CandleDataSet dataSetByIndex, Transformer trans, Paint paint, Canvas c) {
        int lowestVisibleXIndex = combinedChart.getLowestVisibleXIndex();
        int highestVisibleXIndex = combinedChart.getHighestVisibleXIndex();


        float[] minFloat = getMaxFloat(dataSetByIndex.getYVals(), lowestVisibleXIndex, highestVisibleXIndex);//根据数据集获取最低点

        String yChartMax = combinedChart.getYChartMax() + "";
        String yChartMin = combinedChart.getYChartMin() + "";
        String value = minFloat[1] + "";
        BigDecimal bigDecimal = new BigDecimal(yChartMin);
        BigDecimal valueB = new BigDecimal(value).subtract(bigDecimal);
        BigDecimal yChartMaxB = new BigDecimal(yChartMax).subtract(bigDecimal);

        String hightS = (combinedChart.getMeasuredHeight() - combinedChart.getViewPortHandler().offsetBottom() - combinedChart.getViewPortHandler().offsetTop()) + "";
        BigDecimal divB = valueB.multiply(new BigDecimal(hightS)).divide(yChartMaxB, 10, BigDecimal.ROUND_HALF_UP);

        float height = combinedChart.getMeasuredHeight() - divB.floatValue() - combinedChart.getViewPortHandler().offsetBottom();

        float v2 = ((minFloat[0] - lowestVisibleXIndex) / (highestVisibleXIndex - lowestVisibleXIndex)) * (combinedChart.getMeasuredWidth() - combinedChart.getViewPortHandler().offsetRight() - combinedChart.getViewPortHandler().offsetLeft());
        float width = v2 + combinedChart.getViewPortHandler().offsetLeft();

        //combinedChart.getMeasuredWidth() + combinedChart.getViewPortHandler().offsetRight() -
        //通过trans得到最低点的屏幕位置
        //PointD minPoint = trans.getValuesByTouchPoint(minFloat[0], height);

        float lowX = width;
        float lowY = height;
        paint.setColor(CommonUtils.getColor(UserSet.getinstance().getRiseColor()));
        float rectLength = Utils.convertDpToPixel((minFloat[1] + "").length() * Utils.convertDpToPixel(2f));//矩形框长
        //画横竖线
        c.drawLine(lowX, lowY, lowX, lowY + vLength, paint);
        if (lowX > mWidth - mWidth / 3) {//标识朝左
            c.drawLine(lowX, lowY + vLength, lowX - hLength, lowY + vLength, paint);
            //画矩形
            //c.drawRect(new Rect((int) (lowX - hLength - rectLength), (int) (lowY + vLength - rect), (int) (lowX - hLength), (int) (lowY + vLength + rect)), paint);
            //写数字
            paint.setColor(CommonUtils.getColor(UserSet.getinstance().getRiseColor()));
            c.drawText(minFloat[1] + "", lowX - rectLength - hLength + textX, lowY + vLength + textY, paint);
        } else {//标识朝右
            c.drawLine(lowX, lowY + vLength, lowX + hLength, lowY + vLength, paint);
            //c.drawRect(new Rect((int) (lowX + hLength), (int) (lowY + vLength - rect), (int) (lowX + hLength + rectLength), (int) (lowY + vLength + rect)), paint);
            paint.setColor(CommonUtils.getColor(UserSet.getinstance().getRiseColor()));
            c.drawText(minFloat[1] + "", lowX + hLength + textX, lowY + vLength + textY, paint);
        }
    }

    private float[] getMinFloat(List<CandleEntry> lists, int min, int max) {
        float[] mixEntry = new float[2];
        for (int i = min; i < max; i++) {
            if (i == min) {
                mixEntry[0] = i;
                mixEntry[1] = lists.get(i).getVal();
            }
            if (mixEntry[1] > lists.get(i).getVal()) {
                mixEntry[0] = i;
                mixEntry[1] = lists.get(i).getVal();
            }
        }
        return mixEntry;
    }

    private float[] getMaxFloat(List<CandleEntry> lists, int min, int max) {
        float[] mixEntry = new float[2];
        for (int i = min; i < max; i++) {
            if (i == min) {
                mixEntry[0] = i;
                mixEntry[1] = lists.get(i).getVal();
            }
            if (mixEntry[1] < lists.get(i).getVal()) {
                mixEntry[0] = i;
                mixEntry[1] = lists.get(i).getVal();
            }
        }
        return mixEntry;
    }
}
