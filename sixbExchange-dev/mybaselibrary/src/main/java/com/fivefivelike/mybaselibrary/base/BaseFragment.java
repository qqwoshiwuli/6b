package com.fivefivelike.mybaselibrary.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.fivefivelike.mybaselibrary.R;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.fivefivelike.mybaselibrary.mvp.presenter.FragmentPresenter;

/**
 * Created by 郭青枫 on 2017/7/7.
 */

public abstract class BaseFragment<T extends BaseDelegate> extends FragmentPresenter<T> implements View.OnClickListener {

    /**
     * 去掉状态栏
     */
    protected void addNoStatusBarFlag() {
        clearNoStatusBarFlag();
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    /**
     * 显示状态栏
     */
    protected void clearNoStatusBarFlag() {
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    protected BaseActivity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof BaseActivity) {
            this.mActivity = (BaseActivity) activity;
        }
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.toolbar_img) {
                clickRightIv();
            }
            if (id == R.id.toolbar_img1) {
                clickRightIv1();
            } else if (id == R.id.toolbar_img2) {
                clickRightIv2();
            } else if (id == R.id.toolbar_subtitle) {
                clickRightTv();
            }
        }
    };


    @Override
    public void onStart() {
        super.onStart();

    }

    /**
     * 初始化标题
     *
     * @param toolbarBuilder
     */
    protected void initToolbar(ToolbarBuilder toolbarBuilder) {
        if (viewDelegate != null) {
            viewDelegate.initToolBar((AppCompatActivity) getActivity(), onClickListener, toolbarBuilder);
        }
    }

    @Override
    public void onClick(View v) {

    }

    protected void clickRightIv() {
    }

    protected void clickRightIv1() {
    }

    protected void clickRightIv2() {
    }

    protected void clickRightTv() {
    }

    private View rootView;


    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (viewDelegate != null) {
            if (viewDelegate.getmToolbar() != null) {
                if (viewDelegate.isNoStatusBarFlag()) {
                    addNoStatusBarFlag();
                } else {
                    clearNoStatusBarFlag();
                }
                checkToolbarColor();
            }
        }
    }

    public void checkToolbarColor() {
        if (viewDelegate != null) {
            viewDelegate.checkToolColor();
        }
    }

    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            //fragment 自己对自己
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();

            int colorId = savedInstanceState.getInt("colorId");
            boolean isLight = savedInstanceState.getBoolean("isLight");
            viewDelegate.setToolColor(colorId, isLight);
        } else {
            viewDelegate.setToolColor(R.color.white, true);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
        outState.putInt("colorId", viewDelegate.getmColorId());
        outState.putBoolean("isLight", viewDelegate.ismIslight());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}
