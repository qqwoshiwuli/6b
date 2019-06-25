package com.fivefivelike.mybaselibrary.view;

import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by 郭青枫 on 2018/8/28 0028.
 */

public class DecisionCoordinatorViewpager extends ViewPager implements NestedScrollingChild {


    private static final int INVALID_POINTER = -1;

    private int mTouchSlop;
    private int mActivePointerId = INVALID_POINTER;

    private NestedScrollingChildHelper mChildHelper;

    public DecisionCoordinatorViewpager(Context context) {
        this(context, null);
    }

    public DecisionCoordinatorViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (mChildHelper == null) {
            mChildHelper = new NestedScrollingChildHelper(this);
        }
        setNestedScrollingEnabled(true);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        if (mChildHelper == null) {
            mChildHelper = new NestedScrollingChildHelper(this);
        } else {
            mChildHelper.setNestedScrollingEnabled(enabled);
        }
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    private float mLastMotionY;
    private final int[] mScrollOffset = new int[2];
    private final int[] mScrollConsumed = new int[2];
    private boolean mIsBeginDrag = false;


    int lastX = -1;
    int lastY = -1;
    int dealtX = 0;
    int dealtY = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int x = (int) ev.getRawX();
        int y = (int) ev.getRawY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dealtX = 0;
                dealtY = 0;
                // 保证子View能够接收到Action_move事件
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                dealtX += Math.abs(x - lastX);
                dealtY += Math.abs(y - lastY);
                // 这里是否拦截的判断依据是左右滑动，读者可根据自己的逻辑进行是否拦截
                if (dealtX > dealtY) { // 左右滑动请求父 View 不要拦截
                    stopNestedScroll();
                    return true;
                } else {
                }
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_CANCEL:
                stopNestedScroll();
                break;
            case MotionEvent.ACTION_UP:
                stopNestedScroll();
                break;

        }
        return super.onInterceptTouchEvent(ev);
    }


//    private float xDistance, yDistance, xLast, yLast;
//
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_UP:
//                return super.onInterceptTouchEvent(ev);
//            case MotionEvent.ACTION_DOWN:
//                xDistance = yDistance = 0f;
//                xLast = ev.getX();
//                yLast = ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                final float curX = ev.getX();
//                final float curY = ev.getY();
//                xDistance += Math.abs(curX - xLast);
//                yDistance += Math.abs(curY - yLast);
//                if (Math.abs(curX - xLast) < 5 && Math.abs(curY - yLast) < 5) {
//                    //防止处理点击
//                    break;
//                }
//                xLast = curX;
//                yLast = curY;
//                if (xDistance > yDistance) {
//
//                    return false;
//                }
//                break;
//        }
//        return super.dispatchTouchEvent(ev);
//
//    }
}
