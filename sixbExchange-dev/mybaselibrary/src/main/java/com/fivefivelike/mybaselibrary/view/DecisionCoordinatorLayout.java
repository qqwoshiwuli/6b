package com.fivefivelike.mybaselibrary.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 郭青枫 on 2018/8/28 0028.
 */

public class DecisionCoordinatorLayout extends CoordinatorLayout {
    public DecisionCoordinatorLayout(Context context) {
        super(context);
    }

    public DecisionCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DecisionCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private float xDistance, yDistance, xLast, yLast;


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                return super.onInterceptTouchEvent(ev);
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();
                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                if (Math.abs(curX - xLast) < 5 && Math.abs(curY - yLast) < 5) {
                    //防止处理点击
                    break;
                }
                xLast = curX;
                yLast = curY;
                if (xDistance > yDistance) {
                    onStopNestedScroll(this);
                    for(int i=0;i<getChildCount();i++){
                        onStopNestedScroll(getChildAt(i));
                    }
                    return false;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

}
