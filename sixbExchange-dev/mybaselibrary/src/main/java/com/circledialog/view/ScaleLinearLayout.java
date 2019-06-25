package com.circledialog.view;

import android.content.Context;
import android.util.AttributeSet;

import com.circledialog.scale.ScaleLayoutConfig;

import skin.support.widget.SkinCompatLinearLayout;


/**
 * Created by hupei on 2017/3/29.
 */
class ScaleLinearLayout extends SkinCompatLinearLayout {
    public ScaleLinearLayout(Context context) {
        this(context, null);
    }

    public ScaleLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ScaleLayoutConfig.init(context);
    }

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public ScaleLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        ScaleLayoutConfig.init(context);
//    }
}
