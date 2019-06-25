package com.circledialog.res.values;

import android.graphics.Color;

import com.fivefivelike.mybaselibrary.R;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;

/**
 * Created by hupei on 2017/3/29.
 */
public class CircleColor {


    //public static final int bgDialog = CommonUtils.getColor(R.color.colorPrimary);

    /**
     * 标题颜色
     */
    public static final int title = CommonUtils.getColor(R.color.color_font2);//0xFF000000;
    /**
     * 消息内容颜色
     */
    public static final int content = 0xFF8F8F8F;
    /**
     * 按钮颜色
     */
    public static final int button = 0xFF007AFF;

    /**
     * 按钮点击颜色
     */
    public static final int buttonPress = 0xFFEAEAEA;
    /**
     * 输入框边框颜色
     */
    //public static final int inputStroke = CommonUtils.getColor(R.color.base_mask);

    /**
     * 对话框背景色
     */
    public static int getBgDialog() {
        return CommonUtils.getColor(R.color.colorPrimary);
    }

    public static int getTitle() {
        return CommonUtils.getColor(R.color.color_font2);
    }

    public static int getContent() {
        return content;
    }

    public static int getButton() {
        return button;
    }

    /**
     * 线条颜色
     */
    public static int getDivider() {
        int color = CommonUtils.getColor(R.color.colorPrimary);
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv); // convert to hsv
        // make darker
        hsv[2] = hsv[2] + 0.1f; // less brightness
        int darkerColor = Color.HSVToColor(hsv);
        return darkerColor;
    }

    public static int getButtonPress() {
        return buttonPress;
    }

    public static int getInputStroke() {
        return CommonUtils.getColor(R.color.base_mask);
    }

}
