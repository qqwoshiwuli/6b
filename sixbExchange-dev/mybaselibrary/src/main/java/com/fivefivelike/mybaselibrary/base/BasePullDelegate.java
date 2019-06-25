package com.fivefivelike.mybaselibrary.base;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.R;
import com.fivefivelike.mybaselibrary.utils.AndroidUtil;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GlobleContext;
import com.fivefivelike.mybaselibrary.utils.paginate.LoadingListItemCreator;
import com.fivefivelike.mybaselibrary.utils.paginate.LoadingListItemSpanLookup;
import com.fivefivelike.mybaselibrary.utils.paginate.Paginate;
import com.fivefivelike.mybaselibrary.utils.paginate.ViewHolder;
import com.fivefivelike.mybaselibrary.view.SwipeRefreshLayout;
import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.indicators.BallPulseIndicator;

import java.util.List;

/**
 * Created by 郭青枫 on 2017/7/10.
 */

public abstract class BasePullDelegate extends BaseDelegate {
    // 分页页数
    public int page;
    //分页长度
    public int pagesize = 20;
    private LoadMode mMode = LoadMode.REFRESH;
    private boolean mIsLoadMore = true;
    private String noDataTxt;
    private RelativeLayout no_data;
    private Paginate.Callbacks callbacks;
    private boolean isNoData = false;
    private LoadingListItemCreator loadingListItemCreator;
    private int headerCount = 1;//头布局数量
    private View.OnClickListener noDataClickListener;
    public int defaultPage = 1;
    private int noDataImgId = -1;
    private boolean isCanToTop = true;
    private boolean isPullDown = true;

    /**
     * 下拉刷新控件
     */
    private SwipeRefreshLayout mWwipeRefreshLayout;
    private RecyclerView mPullRecyclerView;
    private FrameLayout fl_top;
    /**
     * 用链式达到一句话设置{@link #mPullRecyclerView}的加载操作
     */
    private Paginate mPaginate;
    /**
     * 当{@link #mPullRecyclerView}设置 GridLayoutManager用到
     */
    private int SPAN_SIZE = 0;
    /**
     * {@link #mPullRecyclerView}的上拉显示布局
     */
    private View mFootView;
    /**
     * 用来判断{@link #mPullRecyclerView}加载状态 true是加载中,fasle是不在加载中
     */
    private boolean isLoading;
    /**
     * 用来判断{@link #mPullRecyclerView}中的是不是已经加载了所有的数据
     */
    private boolean isFinish;

    public void setNoDataClickListener(View.OnClickListener noDataClickListener) {
        this.noDataClickListener = noDataClickListener;
    }

    public void setColorSchemeResources(int... colorResIds) {
        if (mWwipeRefreshLayout != null) {
            mWwipeRefreshLayout.setColorSchemeResources(colorResIds);
        }
    }

    boolean isShowNoData = true;

    public boolean isShowNoData() {
        return isShowNoData;
    }

    public void setCanToTop(boolean canToTop) {
        isCanToTop = canToTop;
        if (!canToTop) {
            if (fl_top != null) {
                fl_top.setVisibility(View.GONE);
            }
        }
    }

    public void setShowNoData(boolean showNoData) {
        isShowNoData = showNoData;
        if (mFootView != null) {
            RelativeLayout nodata = (RelativeLayout) mFootView.findViewById(R.id.no_data);
            nodata.setVisibility(isShowNoData ? View.VISIBLE : View.GONE);
        }
    }

    public void setDefaultPage(int defaultPage) {
        this.defaultPage = defaultPage;
    }

    /**
     * 初始化使用RecyclerView的上拉页面
     *
     * @param adapter RecyclerView 的adapter
     * @param manager RecyclerView的显示方式
     */
    public void initRecycleviewPull(RecyclerView.Adapter adapter, RecyclerView.LayoutManager manager, final BasePullCallback callback, int headerCount,
                                    android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        mWwipeRefreshLayout = getViewById(R.id.swipeRefreshLayout);
        mPullRecyclerView = getViewById(R.id.pull_recycleview);
        mWwipeRefreshLayout.setRecyclerView(mPullRecyclerView);
        mWwipeRefreshLayout.setEnabled(isPullDown);
        fl_top = getViewById(R.id.fl_top);
        ((SimpleItemAnimator) mPullRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mPullRecyclerView.getItemAnimator().setChangeDuration(0);// 通过设置动画执行时间为0来解决闪烁问题
        mPullRecyclerView.setLayoutManager(manager);
        mPullRecyclerView.setAdapter(adapter);
        mPullRecyclerView.setScrollBarSize(0);
        mPullRecyclerView.setScrollbarFadingEnabled(true);
        mPullRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (fl_top != null) {
                    if (!mPullRecyclerView.canScrollVertically(-1)) {
                        fl_top.setVisibility(View.GONE);
                    } else {
                        if (isCanToTop && mPullRecyclerView.getChildCount() != 0) {
                            fl_top.setVisibility(View.VISIBLE);
                        } else {
                            fl_top.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
        if (fl_top != null) {
            fl_top.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPullRecyclerView.scrollToPosition(0);
                }
            });
            setCanToTop(isCanToTop);
        }
        this.headerCount = headerCount;
        callbacks = new Paginate.Callbacks() {
            @Override
            public void onLoadMore() {
                if (mIsLoadMore) {
                    isLoading = true;
                    callback.loadData();
                }
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean hasLoadedAllItems() {
                return isFinish;
            }
        };
        loadingListItemCreator = new LoadingListItemCreator() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                addFoot(parent);
                return new ViewHolder(mFootView);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            }
        };
        mPaginate = Paginate.with(mPullRecyclerView, callbacks).setLoadingTriggerThreshold(2)
                .addLoadingListItem(true)
                .setLoadingListItemCreator(loadingListItemCreator)
                .setLoadingListItemSpanSizeLookup(new LoadingListItemSpanLookup() {
                    @Override
                    public int getSpanSize() {
                        return SPAN_SIZE;
                    }
                })
                .setPagesize(pagesize)
                .setHeaderCount(headerCount)
                .build();
        mWwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        mWwipeRefreshLayout.setRefreshing(isPullDown);
    }

    private void addFoot(ViewGroup parent) {
        mFootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_layout, parent, false);//初始化尾布局
        mFootView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if (mPullRecyclerView.getLayoutManager() instanceof GridLayoutManager) {
            ViewGroup.LayoutParams layoutParams = mFootView.getLayoutParams();
            layoutParams.width = AndroidUtil.getScreenW(GlobleContext.getInstance().getApplicationContext(), false);
            mFootView.setLayoutParams(layoutParams);
        }
        AVLoadingIndicatorView loadingView = new AVLoadingIndicatorView(parent.getContext());
        //ProgressView loadingView = new ProgressView(parent.getContext());//尾部加载中状态
        loadingView.setIndicator(new BallPulseIndicator());
        loadingView.setIndicatorColor(0xff69b3e0);
        TextView endView = new TextView(parent.getContext());//所有数据加载完布局
        endView.setGravity(Gravity.CENTER);
        endView.setText(CommonUtils.getString(R.string.str_end_data));
        LinearLayout loadLayout = (LinearLayout) mFootView.findViewById(R.id.loading_view_layout);
        LinearLayout endLayout = (LinearLayout) mFootView.findViewById(R.id.end_layout);
        RelativeLayout nodata = (RelativeLayout) mFootView.findViewById(R.id.no_data);
        if (noDataClickListener != null) {
            nodata.findViewById(R.id.ic_nodata).setOnClickListener(noDataClickListener);
            nodata.findViewById(R.id.tv_nodata).setOnClickListener(noDataClickListener);
        }
        ViewGroup.LayoutParams layoutParams = nodata.getLayoutParams();
        int height = mPullRecyclerView.getHeight();
        layoutParams.height = height;
        nodata.setLayoutParams(layoutParams);
        loadLayout.setVisibility(View.GONE);
        loadLayout.addView(loadingView);
        endLayout.addView(endView);
        nodata.setVisibility(isShowNoData ? View.VISIBLE : View.GONE);
        loadLayout.setVisibility(View.GONE);
        endLayout.setVisibility(View.GONE);

        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) loadingView.getLayoutParams();
        layoutParams1.gravity = Gravity.CENTER;
        layoutParams1.height = (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_80px);
        layoutParams1.width = (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_80px);
        loadingView.setLayoutParams(layoutParams1);

        if (!mIsLoadMore) {
            loadLayout.setVisibility(View.GONE);
            endLayout.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(noDataTxt)) {
            ((TextView) mFootView.findViewById(R.id.tv_nodata)).setText(noDataTxt);
        }
        if (noDataImgId != 0 && noDataImgId != -1) {
            ((ImageView) mFootView.findViewById(R.id.ic_nodata)).setBackgroundResource(noDataImgId);
        } else if (noDataImgId == 0) {
            ((ImageView) mFootView.findViewById(R.id.ic_nodata)).setVisibility(View.GONE);
        } else {
            ((ImageView) mFootView.findViewById(R.id.ic_nodata)).setBackgroundResource(R.drawable.ic_nodata);

        }
    }

    /**
     * 数据请求回来调用
     */
    protected void requestBack(List srcList) {
        mWwipeRefreshLayout.setRefreshing(false);
        hideNoData();
        switch (mMode) {
            case REFRESH://下拉
                if (srcList != null) {
                    srcList.clear();
                }
                break;
            case DOWN://上拉
                break;
        }
        if (mPullRecyclerView != null) {
            isFinish = false;
            isLoading = false;
            if (mFootView != null) {
                mFootView.findViewById(R.id.loading_view_layout).setVisibility(mIsLoadMore ? View.VISIBLE : View.GONE);
                mFootView.findViewById(R.id.end_layout).setVisibility(View.GONE);
            }
        }
    }

    public void loadFinish() {
        switch (mMode) {
            case REFRESH://下拉
                showNoData();
                break;
            case DOWN://上拉
                if (mPullRecyclerView != null) {
                    isFinish = true;
                    isLoading = false;
                    if (mFootView != null) {
                        mFootView.findViewById(R.id.loading_view_layout).setVisibility(View.GONE);
                        mFootView.findViewById(R.id.end_layout).setVisibility(mIsLoadMore ? View.VISIBLE : View.GONE);
                    }
                }
                break;
        }
    }

    public void hideNoData() {
        //getViewById(R.id.no_data).setVisibility(View.GONE);
        isNoData = false;
        if (mFootView != null) {
            mFootView.findViewById(R.id.no_data).setVisibility(View.GONE);
        }
    }

    public void showNoData() {
        isNoData = true;
        if (isShowNoData && mFootView != null) {
            mFootView.findViewById(R.id.no_data).setVisibility(View.VISIBLE);
            mFootView.findViewById(R.id.loading_view_layout).setVisibility(View.GONE);
            mFootView.findViewById(R.id.loading_view_layout).setVisibility(View.GONE);
        }
    }

    public TextView getNoDataText() {
        if (mFootView == null) {
            return null;
        }
        return (TextView) mFootView.findViewById(R.id.tv_nodata);
    }

    public ImageView getNoDataImg() {
        if (mFootView == null) {
            return null;
        }
        return (ImageView) mFootView.findViewById(R.id.ic_nodata);
    }

    /**
     * 设置是否上拉加载
     *
     * @param isLoadMore
     */
    public void setIsLoadMore(boolean isLoadMore) {
        this.mIsLoadMore = isLoadMore;
    }

    /**
     * 设置是否下拉刷新
     *
     * @param isPullDown
     */
    public void setIsPullDown(boolean isPullDown) {
        this.isPullDown=isPullDown;
        if (mWwipeRefreshLayout != null) {
            mWwipeRefreshLayout.setEnabled(isPullDown);
            mWwipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * 请求数据
     *
     * @param loadMode 类型
     */
    public void requestData(LoadMode loadMode) {
        switch (loadMode) {
            case REFRESH://下拉刷新
                mMode = LoadMode.REFRESH;
                page = defaultPage;
                break;
            case DOWN://上拉加载
                mMode = LoadMode.DOWN;
                page++;
                break;
        }
    }

    public enum LoadMode {
        /**
         * 下拉刷新
         */
        REFRESH,
        /**
         * 上拉加载
         */
        DOWN
    }

    public void stopRefresh() {
        if (mWwipeRefreshLayout != null) {
            mWwipeRefreshLayout.setRefreshing(false);
        }
    }

    public void pageReduction() {
        if (page > defaultPage) {
            page--;
        }
        isLoading = false;
    }

    public SwipeRefreshLayout getWwipeRefreshLayout() {
        return mWwipeRefreshLayout;
    }

    public RecyclerView getPullRecyclerView() {
        return mPullRecyclerView;
    }

    public void setNoDataTxt(String noDataTxt) {
        this.noDataTxt = noDataTxt;
    }

    //设置为0 不显示
    public void setNoDataImgId(int noDataImgId) {
        this.noDataImgId = noDataImgId;
    }
}
