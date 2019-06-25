package com.fivefivelike.mybaselibrary.view;

import android.content.Context;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.fivefivelike.mybaselibrary.R;
import com.fivefivelike.mybaselibrary.utils.AndroidUtil;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;

/**
 * Created by 郭青枫 on 2018/1/26 0026.
 */

public class JudgeNestedScrollView extends NestedScrollView {
    private boolean isNeedScroll = true;
    View view;
    private float xDistance, yDistance, xLast, yLast;
    int toolBarPositionY = 0;
    View noScollView = null;
    int[] locationNoScollView = new int[2];
    int noWidth = 0;
    int noHeight = 0;


    View rootView;

    boolean isPriorityDropDown = false;



    public void setPriorityDropDown(boolean priorityDropDown) {
        isPriorityDropDown = priorityDropDown;
    }

    public void setNoScollView(View view) {
        this.noScollView = view;
        noScollView.post(new Runnable() {
            @Override
            public void run() {
                noWidth = noScollView.getMeasuredWidth();
                noHeight = noScollView.getMeasuredHeight();
                noScollView.getLocationOnScreen(locationNoScollView);
            }
        });
    }

    public JudgeNestedScrollView(@NonNull Context context) {
        super(context);
        init();
    }

    public static int getHeight(FragmentActivity context) {
        int statusHeight = AndroidUtil.getStatusHeight(context);
        int bottomStatusHeight = (int) AndroidUtil.getBottomStatusHeight(context);
        return (int) (AndroidUtil.getScreenW(context, true) -
                statusHeight -
                CommonUtils.getDimensionPixelSize(R.dimen.trans_90px) -
                bottomStatusHeight);
    }

    public void setTabAndPager(View tab, int tabViewHeight, View viewPager, boolean isToolbarInside) {
        this.view = tab;
        toolBarPositionY = tabViewHeight + (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_90px);
        if (isToolbarInside) {
            toolBarPositionY = toolBarPositionY + (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_90px);
        }
        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) viewPager.getLayoutParams();
        layoutParams.height = getHeight() - tabViewHeight;
        viewPager.setLayoutParams(layoutParams);
    }

    /**
     * 初始化于页面显示之后
     *
     * @param tab
     * @param viewPager
     * @param isToolbarInside
     */
    public void setTabAndPager(View tab, View viewPager, boolean isToolbarInside) {
        setTabAndPager(tab, viewPager, 0);
    }
    public void setTabAndPager(View tab, View viewPager) {
        setTabAndPager(tab, viewPager, 0);
    }
    public void setTabAndPager(View tab, View viewPager, int hight) {
        rootView = viewPager;
        this.view = tab;
        int[] location = new int[2];
        this.getLocationOnScreen(location);
        toolBarPositionY = location[1];
        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) viewPager.getLayoutParams();
        layoutParams.height = getHeight() - tab.getHeight();
        viewPager.setLayoutParams(layoutParams);
    }

    public void setTabAndPager(View tab, View viewPager, View statuBar) {
        rootView = viewPager;
        this.view = tab;
        int[] location = new int[2];
        this.getLocationOnScreen(location);
        toolBarPositionY = location[1];
        toolBarPositionY = toolBarPositionY + statuBar.getHeight();
        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) viewPager.getLayoutParams();
        layoutParams.height = getHeight() - tab.getHeight();
        layoutParams.height = getHeight() - tab.getHeight() - statuBar.getHeight();
        viewPager.setLayoutParams(layoutParams);
    }

    public void setTabAndPager(View tab, View viewPager, int hight, boolean isToolbarInside) {
        setTabAndPager(tab, viewPager, hight);
    }

    public JudgeNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public JudgeNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public interface OnScrollChangeListener {
        void onScrollChangeListener(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }

    OnScrollChangeListener onScrollChangeListener;

    public void setOnScrollChangeListener(OnScrollChangeListener onScrollChangeListener) {
        this.onScrollChangeListener = onScrollChangeListener;
    }

    private void init() {
        setFocusable(true);
        setFocusableInTouchMode(true);
        setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });
        this.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int[] location = new int[2];

                if (view != null) {
                    view.getLocationOnScreen(location);
                } else {
                    return;
                }
                int xPosition = location[0];
                int yPosition = location[1];

                if (noScollView != null) {
                    noScollView.getLocationOnScreen(locationNoScollView);
                }

                if (yPosition <= toolBarPositionY) {
                    setNeedScroll(false);
                } else {
                    setNeedScroll(true);
                }
                if (onScrollChangeListener != null) {
                    onScrollChangeListener.onScrollChangeListener(v, scrollX, scrollY, oldScrollX, oldScrollY);
                    // Log.i("judgeNestedScrollView","scrollX"+scrollX+"scrollY"+scrollY+"oldScrollX"+oldScrollX+"oldScrollY"+oldScrollY);
                }

            }
        });
    }

    int noNeedScrollXStart = 0;
    int noNeedScrollXEnd = 0;

    public void setNoNeedScrollXEnd(int noNeedScrollXEnd) {
        this.noNeedScrollXEnd = noNeedScrollXEnd;
    }

    public void setNoNeedScrollXStart(int noNeedScrollXStart) {
        this.noNeedScrollXStart = noNeedScrollXStart;
    }

    boolean isDo;

    float yDown = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (toolBarPositionY == 0) {
            return super.onInterceptTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                return super.onInterceptTouchEvent(ev);
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                yDown = ev.getY();
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
                if (noScollView != null && locationNoScollView != null && noWidth != 0) {
                    RectF rectF = new RectF(locationNoScollView[0],
                            locationNoScollView[1], locationNoScollView[0] + noWidth,
                            locationNoScollView[1] + noHeight);
                    if (rectF.contains(ev.getRawX(), ev.getY())) {
                        isDo = false;
                        return false;
                    } else {
                        isDo = true;
                    }
                } else {
                    isDo = true;
                }
                if (isDo) {
                    if (xDistance > yDistance) {
                        return false;
                    } else {
                        if (isPriorityDropDown && yDown < yLast) {
                            if (!isNeedScroll) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                        return isNeedScroll;
                    }
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /*
    改方法用来处理NestedScrollView是否拦截滑动事件
     */
    public void setNeedScroll(boolean isNeedScroll) {
        this.isNeedScroll = isNeedScroll;
    }

    public boolean isNeedScroll() {
        return isNeedScroll;
    }
}
