package com.fivefivelike.mybaselibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.R;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.ListUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 郭青枫 on 2019/1/18 0018.
 */

public class PtrDefaultHeader extends FrameLayout implements PtrUIHandler {

    Context mContext;
    private int mRotateAniTime = 150;
    private IconFontTextview tv_ic;
    private IconFontTextview tv_error;
    private TextView tv_refresh;
    private RotateAnimation mFlipAnimation;
    private RotateAnimation mReverseFlipAnimation;
    private RotateAnimation mRefreshAnimation;

    private String refreshTextDefault = "";

    String refreshText = "";
    List<String> refreshErrorText;
    long showErrorTime = 0;

    public void setLoadingShow(String refreshText, boolean isRefresh) {
        if (TextUtils.isEmpty(refreshText)) {
            this.refreshText = refreshTextDefault;
        } else {
            this.refreshText = refreshText;
        }
        if (isRefresh) {
            tv_refresh.setText(refreshText);
        }
    }

    public void setLoadingShow(
            List<String> refreshErrorText,
            long showErrorTime) {
        if (!ListUtils.isEmpty(refreshErrorText)) {
            this.refreshErrorText = refreshErrorText;
            this.showErrorTime = showErrorTime;
        }
    }

    public PtrDefaultHeader(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public PtrDefaultHeader(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PtrDefaultHeader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.PtrClassicHeader, 0, 0);
        if (arr != null) {
            mRotateAniTime = arr.getInt(R.styleable.PtrClassicHeader_ptr_rotate_ani_time, mRotateAniTime);
        }
        buildAnimation();
        View header = LayoutInflater.from(getContext()).inflate(R.layout.layout_pull_header, this);
        tv_ic = header.findViewById(R.id.tv_ic);
        tv_error = header.findViewById(R.id.tv_error);
        tv_refresh = header.findViewById(R.id.tv_refresh);
        refreshTextDefault = CommonUtils.getString(R.string.str_refushing);
        resetView();
    }

    private void buildAnimation() {
        mFlipAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mFlipAnimation.setInterpolator(new LinearInterpolator());
        mFlipAnimation.setDuration(mRotateAniTime);
        mFlipAnimation.setFillAfter(true);

        mReverseFlipAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
        mReverseFlipAnimation.setDuration(mRotateAniTime);
        mReverseFlipAnimation.setFillAfter(true);

        mRefreshAnimation = new RotateAnimation(-360, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mRefreshAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mRefreshAnimation.setDuration(mRotateAniTime);
        mRefreshAnimation.setRepeatCount(Animation.INFINITE);
        mRefreshAnimation.setRepeatMode(Animation.REVERSE);
        mRefreshAnimation.setFillAfter(true);
    }

    private void resetView() {
        hideRotateView();
        showErrorTime = 0;
        refreshErrorText=null;
        refreshText = refreshTextDefault;
        tv_refresh.setVisibility(INVISIBLE);
        tv_refresh.setText(refreshText);
    }

    private void hideRotateView() {
        tv_ic.clearAnimation();
        tv_ic.setVisibility(INVISIBLE);
        tv_error.setVisibility(GONE);

    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        ///刷新完成之后，UI消失之后的接口回调。
        resetView();
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        //开始下拉之前的接口回调。
        tv_ic.setVisibility(VISIBLE);
        tv_refresh.setVisibility(VISIBLE);
        if (frame.isPullToRefresh()) {
            tv_refresh.setText(getResources().getString(in.srain.cube.views.ptr.R.string.cube_ptr_pull_down_to_refresh));
        } else {
            tv_refresh.setText(getResources().getString(in.srain.cube.views.ptr.R.string.cube_ptr_pull_down));
        }
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        //开始刷新的接口回调。
        hideRotateView();
        tv_refresh.setVisibility(VISIBLE);
        if (!ListUtils.isEmpty(refreshErrorText)) {
            tv_ic.setVisibility(GONE);
            tv_error.setVisibility(VISIBLE);
            tv_refresh.setText(refreshErrorText.get(0));
            if (showErrorTime > 0) {
                Flowable.intervalRange(0, (showErrorTime / 1000) + 1,
                        0,
                        1000, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .onTerminateDetach()
                        .doOnNext(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                int size = Integer.parseInt(aLong + "");
                                if (size < refreshErrorText.size()) {
                                    tv_refresh.setText(refreshErrorText.get(size));
                                    tv_ic.setVisibility(GONE);
                                } else {
                                    tv_ic.setVisibility(VISIBLE);
                                    tv_error.setVisibility(GONE);
                                    tv_refresh.setText(refreshText);
                                    if (tv_ic != null) {
                                        tv_ic.setVisibility(VISIBLE);
                                        tv_ic.clearAnimation();
                                        tv_ic.startAnimation(mRefreshAnimation);
                                    }
                                }
                            }
                        })
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {

                            }
                        });
            }
        } else {
            tv_refresh.setText(refreshText);
            if (tv_ic != null) {
                tv_ic.setVisibility(VISIBLE);
                tv_ic.clearAnimation();
                tv_ic.startAnimation(mRefreshAnimation);
            }
        }
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        //刷新完成的接口回调。
        hideRotateView();
        tv_refresh.setVisibility(VISIBLE);
        tv_refresh.setText(CommonUtils.getString(R.string.str_refushing_end));

    }

    long currtTime;

    /**
     * sUnderTouch ：手指是否触摸
     * status：状态值
     * ptrIndicator：滑动偏移量等值的封装
     */
    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        //下拉滑动的接口回调，多次调用。
        final int mOffsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int lastPos = ptrIndicator.getLastPosY();
        currtTime = System.currentTimeMillis();
        if (currentPos < mOffsetToRefresh && lastPos >= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                crossRotateLineFromBottomUnderTouch(frame);
                if (tv_ic != null) {
                    tv_ic.clearAnimation();
                    tv_ic.startAnimation(mReverseFlipAnimation);
                }
            }
        } else if (currentPos > mOffsetToRefresh && lastPos <= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                crossRotateLineFromTopUnderTouch(frame);
                if (tv_ic != null) {
                    tv_ic.clearAnimation();
                    tv_ic.startAnimation(mFlipAnimation);
                }
            }
        }
    }

    private void crossRotateLineFromTopUnderTouch(PtrFrameLayout frame) {
        if (!frame.isPullToRefresh()) {
            tv_refresh.setVisibility(VISIBLE);
            tv_refresh.setText(refreshText);
        }
    }

    private void crossRotateLineFromBottomUnderTouch(PtrFrameLayout frame) {
        tv_refresh.setVisibility(VISIBLE);
        if (frame.isPullToRefresh()) {
            tv_refresh.setText(getResources().getString(in.srain.cube.views.ptr.R.string.cube_ptr_pull_down_to_refresh));
        } else {
            tv_refresh.setText(getResources().getString(in.srain.cube.views.ptr.R.string.cube_ptr_pull_down));
        }
    }
}
