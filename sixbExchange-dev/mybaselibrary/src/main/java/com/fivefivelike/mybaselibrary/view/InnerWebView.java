package com.fivefivelike.mybaselibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by 郭青枫 on 2017/3/1.
 */

public class InnerWebView extends WebView {
    private float downX, downY; // 按下时
    private float currX, currY; // 移动时
    private float moveX; // 移动长度-横向
    public InnerWebView(Context context) {
        super(context);
    }

    public InnerWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InnerWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                currX = ev.getX();
                currY = ev.getY();
                moveX = Math.abs(currX - downX);
                // 垂直滑动
                if (Math.abs(currY - downY) > moveX) {
                    // 处于顶部或者无法滚动，并且继续下滑，交出事件（currY-downY  >0是下滑, <0则是上滑）
                    if (getScrollY() == 0 && currY - downY > 0) {
                        getParent().getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    // 已到底部且继续上滑时，把事件交出去
                    else if(getContentHeight()*getScale() - (getHeight() + getScrollY()) <= 1 && currY - downY < 0){
                        getParent().getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                // 水平滚动,横向滑动长度大于20像素时再交出去，不然都当做是垂直滑动。
                else if(moveX > 20){
                    // 横向滑动事不直接交出去，是因为可能页面出现水平滚动条，就是网页宽度比屏幕还宽的情况下就需要判断滑到左边和滑到右边的情况。
                    // printLog("onTouchEvent ACTION_MOVE 横向滑动 父处理");
                    // getParent().getParent().requestDisallowInterceptTouchEvent(false);

                    // 已在左边且继续右滑时，把事件交出去（currX - downX  >0是右滑, <0则是左滑）
                    if (getScrollX() == 0 && currX - downX > 0) {
                        getParent().getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    // 已在右边且继续左滑时，把事件交出去
                    else if(getRight()*getScale() - (getWidth() + getScrollX()) <= 1 && currX - downX < 0){
                        getParent().getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                break;
        }
        return super.onTouchEvent(ev);
    }
}
