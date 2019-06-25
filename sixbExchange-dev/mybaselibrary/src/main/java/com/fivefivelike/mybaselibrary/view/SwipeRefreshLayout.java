package com.fivefivelike.mybaselibrary.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.fivefivelike.mybaselibrary.R;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;

import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by 郭青枫 on 2019/1/18 0018.
 */

public class SwipeRefreshLayout extends PtrFrameLayout implements PtrHandler {
    Context mContext;
    android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener onRefreshListener;

    public SwipeRefreshLayout(Context context) {
        super(context);
        init(context);
    }

    public SwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SwipeRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    PtrDefaultHeader mPtrClassicHeader;

    public void setLoadingShow(String refreshText
    ) {
        if (!isRefreshing()) {
            setRefreshing(true);
            mPtrClassicHeader.setLoadingShow(refreshText, isRefreshing());
        }
    }

    public void setLoadingShow(
            List<String> refreshErrorText,
            long showErrorTime) {
        if (!isRefreshing()) {
            mPtrClassicHeader.setLoadingShow(refreshErrorText, showErrorTime);
            setRefreshEventDaly(showErrorTime);
            setRefreshing(true);
        }
    }

    public PtrDefaultHeader getmPtrClassicHeader() {
        return mPtrClassicHeader;
    }

    private void init(Context context) {
        mContext = context;
        // the following are default settings
        setResistance(1.7f);
        setRatioOfHeaderHeightToRefresh(1.2f);
        setDurationToClose(200);
        setDurationToCloseHeader(1000);
        // default is false
        setPullToRefresh(false);
        // default is true
        setKeepHeaderWhenRefresh(true);
        setPtrHandler(this);
        disableWhenHorizontalMove(true);
        mPtrClassicHeader = new PtrDefaultHeader(getContext());
        setHeaderView(mPtrClassicHeader);
        addPtrUIHandler(mPtrClassicHeader);
        setBackgroundColor(CommonUtils.getColor(R.color.base_mask_less));
    }

    boolean isRefreshNoEvent = false;

    private void setRefreshEventDaly(long time) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                onRefreshListener.onRefresh();
            }
        }, time);
    }

    public void setRefreshing(boolean isRefresh) {
        if (isRefresh) {
            //开始刷新
            if (!isRefreshing()) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isRefreshing()) {
                            isRefreshNoEvent = true;
                            autoRefresh(true);
                        }
                    }
                }, 50);
            }
        } else {
            //结束刷新
            if (isRefreshing()) {
                isRefreshNoEvent = false;
                refreshComplete();
            }
        }
    }

    public void setOnRefreshListener(android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public void setColorSchemeResources(int... colorResIds) {
        //渐变色
    }


    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    RecyclerView recyclerView;

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public boolean dispatchTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                return super.dispatchTouchEvent(e);
        }
        if (recyclerView != null) {
            if (!recyclerView.canScrollVertically(-1) && !isRefreshing()) {
                return super.dispatchTouchEvent(e);
            } else {
                return super.dispatchTouchEventSupper(e);
            }
        }
        return super.dispatchTouchEvent(e);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        if (onRefreshListener != null && !isRefreshNoEvent) {
            onRefreshListener.onRefresh();
        }
    }
}
