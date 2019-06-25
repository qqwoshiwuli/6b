package com.circledialog.res.drawable;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;

import com.fivefivelike.mybaselibrary.R;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;

/**
 * 按钮的背景，有点击效果
 * Created by hupei on 2017/3/30.
 */

public class SelectorFrameBtn extends StateListDrawable {

    public SelectorFrameBtn(int frameColor, int leftTopRadius, int rightTopRadius, int rightBottomRadius, int
            leftBottomRadius) {
        //按下
        ShapeDrawable drawablePress = new ShapeDrawable(
                DrawableHelper.getRoundRectShape(
                        leftTopRadius, rightTopRadius,
                rightBottomRadius, leftBottomRadius));

        //drawablePress.getPaint().setColor(getDarkerColor(CommonUtils.getColor(R.color.transparent)));
        drawablePress.getPaint().setColor(getDarkerColor(frameColor));
        drawablePress.getPaint().setStrokeWidth(CommonUtils.getDimensionPixelSize(R.dimen.trans_1px));
        drawablePress.getPaint().setStyle(Paint.Style.STROKE);
        drawablePress.getPaint().setAntiAlias(true);
        drawablePress.getPaint().setDither(true);
        drawablePress.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
        drawablePress.getPaint().setStrokeJoin(Paint.Join.ROUND);
        drawablePress.setColorFilter(getDarkerColor(CommonUtils.getColor(R.color.transparent)), PorterDuff.Mode.DST_IN);


        //默认
        ShapeDrawable defaultDrawable = new ShapeDrawable(
                DrawableHelper.getRoundRectShape(
                        leftTopRadius,
                rightTopRadius,
                rightBottomRadius, leftBottomRadius));
        //defaultDrawable.getPaint().setColor(CommonUtils.getColor(R.color.transparent));
        defaultDrawable.getPaint().setColor(frameColor);
        defaultDrawable.getPaint().setAntiAlias(true);
        defaultDrawable.getPaint().setDither(true);
        defaultDrawable.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
        defaultDrawable.getPaint().setStyle(Paint.Style.STROKE);
        defaultDrawable.getPaint().setStrokeJoin(Paint.Join.ROUND);
        defaultDrawable.getPaint().setStrokeWidth(CommonUtils.getDimensionPixelSize(R.dimen.trans_1px));

        addState(new int[]{android.R.attr.state_pressed}, drawablePress);
        addState(new int[]{-android.R.attr.state_pressed}, defaultDrawable);
    }
    public int getDarkerColor(int color){
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv); // convert to hsv
        // make darker
        hsv[2] = hsv[2] - 0.1f; // less brightness
        int darkerColor = Color.HSVToColor(hsv);
        return  darkerColor ;
    }
}
