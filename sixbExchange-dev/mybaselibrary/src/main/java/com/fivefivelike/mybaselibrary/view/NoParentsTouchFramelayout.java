package com.fivefivelike.mybaselibrary.view;

import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;

import skin.support.widget.SkinCompatFrameLayout;

/**
 * Created by 郭青枫 on 2018/1/31 0031.
 */

public class NoParentsTouchFramelayout extends SkinCompatFrameLayout implements NestedScrollingParent ,NestedScrollingChild {

    private  NestedScrollingParentHelper mParentHelper;
    private  NestedScrollingChildHelper mChildHelper;

    public NoParentsTouchFramelayout(Context context) {
        super(context);
        init();
    }

    public NoParentsTouchFramelayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NoParentsTouchFramelayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

    }

    boolean isNoTouch=true;

    public void setNoTouch(boolean noTouch) {
        isNoTouch = noTouch;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(isNoTouch);
        return super.dispatchTouchEvent(ev);
    }
}
