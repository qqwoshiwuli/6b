package com.fivefivelike.mybaselibrary.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import skin.support.content.res.SkinCompatResources;

/**
 * 像素转换和资源获取类
 * dip转化成px{@link #dip2px(Context, float)}
 * px转化成dip{@link #px2dip(Context, float)}
 * px转化成sp{@link #px2sp(Context, float)}
 * sp转化成px{@link #sp2px(Context, float)}
 * 获取Resource对象{@link #getResources()}
 * 获取Drawable资源{@link #getDrawable(int)}
 * 获取字符串资源{@link #getString(int)}
 * 获取color资源{@link #getColor(int)}
 * 获取字符串数组资源{@link #getStringArray(int)}
 */
public class CommonUtils {
    /**
     * dip转化成px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转化成dip
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * px转化成sp
     */
    public static int px2sp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转化成px
     */
    public static int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * scale + 0.5f);
    }

    /**
     * 获取Resource对象
     */
    public static Resources getResources() {
        return GlobleContext.getInstance().getApplicationContext().getResources();
    }

    public static Context getContext() {
        return GlobleContext.getInstance().getApplicationContext();
    }

    /**
     * 获取Drawable资源
     */
    public static Drawable getDrawable(int resId) {
        return SkinCompatResources.getInstance().getDrawable(resId);
    }

    /**
     * 获取字符串资源
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    public static String getStringColor(int resId) {
        String string = getResources().getString(resId);
        string = string.substring(0, 1) + string.substring(3, string.length());
        return string;
    }

    public static String getString(int resId, Object... content) {
        return String.format(getResources().getString(resId), content);
    }


    /**
     * 获取color资源
     */
    public static int getColor(int resId) {
        return SkinCompatResources.getColor(getContext(), resId);
    }

    public static ColorStateList getColorStateList(int resId) {
        return SkinCompatResources.getColorStateList(getContext(), resId);
    }

    /**
     * 获取dimens资源
     */
    public static float getDimensionPixelSize(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    public static float getDimension(int resId) {
        return getResources().getDimension(resId);
    }

    /**
     * 获取字符串数组资源
     */
    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }
}


