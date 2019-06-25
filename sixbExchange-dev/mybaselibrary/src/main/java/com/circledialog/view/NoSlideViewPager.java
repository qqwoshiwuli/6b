package com.circledialog.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 郭青枫 on 2018/3/26 0026.
 */

public class NoSlideViewPager extends ViewPager {

    boolean isNoSlide=true;

    public void setNoSlide(boolean noSlide) {
        isNoSlide = noSlide;
    }

    public NoSlideViewPager(@NonNull Context context) {
        super(context);
    }

    public NoSlideViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(isNoSlide){
            return false;//不要拦截事件   事件要分发给后面的控件
        }else{
            return super.onInterceptTouchEvent(ev);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(isNoSlide){
            return false;//不要拦截事件   事件要分发给后面的控件
        }else{
            return super.onTouchEvent(ev);
        }
    }

}
