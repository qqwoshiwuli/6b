//package com.fivefivelike.mybaselibrary.view;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.View;
//import android.widget.AbsListView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.fivefivelike.mybaselibrary.utils.logger.KLog;
//
//
///**
// * Created by 郭青枫 on 2016/11/8.
// */
//public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {
//    private int pagesize;
//    private Context context;
//    private Callback callback;
//    private ProgressView loadingView;
//    private TextView endView;
//    private boolean isLoadMoreEnable;
//    private LoadingMoreFooter footView;
//    public LoadMoreListView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        this.context = context;
//        initUI();
//    }
//
//    public void setPagesize(int pagesize) {
//        this.pagesize = pagesize;
//    }
//
//    private void initUI() {
//        footView = new LoadingMoreFooter(context);
//        loadingView = new ProgressView(context);
//        loadingView.setIndicatorId(ProgressView.BallPulse);
//        loadingView.setIndicatorColor(0xff69b3e0);
//        endView = new TextView(context);
//        endView.setText("已经到底啦~");
//        footView.addFootEndView(endView);
//        footView.addFootLoadingView(loadingView);
//        isLoadMoreEnable = true;
//        footView.setVisibility(View.GONE);
//        this.addFooterView(footView);
//        this.setFooterDividersEnabled(false);
//        this.setOnScrollListener(this);
//        footView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                KLog.i("抢夺点击事件，防止尾部点击崩溃");
//            }
//        });
//    }
//    @Override
//    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
//                && this.getLastVisiblePosition() == this.getCount() - 1 && isLoadMoreEnable) {
//            footView.setVisible();
//            callback.loadData();
//        }
//    }
//
//    @Override
//    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//        //int lastIndex = firstVisibleItem + visibleItemCount - 1 - 1;
//
//    }
//
//    public void hideFootView() {
//        footView.setVisibility(View.GONE);
//    }
//    //是否加载更多
//    public void setLoadMoreEnable(boolean loadMoreEnable) {
//        isLoadMoreEnable = loadMoreEnable;
//    }
//
//    public void loadMoreComplete() {
//        if (getAdapter().getCount() > pagesize) {
//            footView.setVisible();
//            isLoadMoreEnable = true;
//        } else {
//            footView.setGone();
//            isLoadMoreEnable = false;
//        }
//    }
//    //到底了
//    public void loadMoreEnd() {
//        if (footView != null) {
//            footView.setEnd();
//            isLoadMoreEnable = false;
//        }
//    }
//    public void setCallback(Callback callback) {
//        this.callback = callback;
//    }
//    public interface Callback {
//        void loadData();
//    }
//
//}
