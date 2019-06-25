package com.fivefivelike.mybaselibrary.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.WindowManager;

import com.blankj.utilcode.util.KeyboardUtils;
import com.circledialog.CircleDialog;
import com.fivefivelike.mybaselibrary.R;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.fivefivelike.mybaselibrary.mvp.presenter.ActivityPresenter;
import com.fivefivelike.mybaselibrary.utils.CleanLeakUtils;
import com.fivefivelike.mybaselibrary.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by 郭青枫 on 2017/7/7.
 */

public abstract class BaseActivity<T extends BaseDelegate> extends ActivityPresenter<T> implements View.OnClickListener, CircleDialog.CircleDialogLinsener {
    boolean isDoubleClickExit = false;

    public void setDoubleClickExit(boolean doubleClickExit) {
        isDoubleClickExit = doubleClickExit;
    }

    public Activity mContext;
    private long exitTime = 0;

    private int WindowManagerLayoutParams = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
    public static final int WindowManagerLayoutParamsNone = 0;

    public void setWindowManagerLayoutParams(int windowManagerLayoutParams) {
        WindowManagerLayoutParams = windowManagerLayoutParams;
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
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        viewDelegate.setCircleDialogLinsener(this);
        mContext = this;
        //友盟推送
        super.onCreate(savedInstanceState);
        setStatusBarLightOrNight();
    }


    public void setStatusBarLightOrNight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (viewDelegate.isNoStatusBarFlag()) {
                addNoStatusBarFlag();
            } else {
                clearNoStatusBarFlag();
            }
        }
    }

    public void onCancel(DialogInterface dialog) {
        //fragmentdialog网络加载弹窗返回后关闭 当前页面
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CleanLeakUtils.fixInputMethodManagerLeak(this);
        KeyboardUtils.fixSoftInputLeaks(this);
    }


    /**
     * 去掉状态栏
     */
    protected void addNoStatusBarFlag() {
        //clearNoStatusBarFlag();
        clearNoStatusBarFlag();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    /**
     * 显示状态栏
     */
    protected void clearNoStatusBarFlag() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    protected void initToolbar(ToolbarBuilder toolbarBuilder) {
        if (viewDelegate != null) {
            if (onClickListener != null) {
                viewDelegate.initToolBar(this, onClickListener, toolbarBuilder);
                viewDelegate.setToolColor(R.color.white, true);
            }
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("colorId", viewDelegate.getmColorId());
        outState.putBoolean("isLight", viewDelegate.ismIslight());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int colorId = savedInstanceState.getInt("colorId");
        boolean isLight = savedInstanceState.getBoolean("isLight");
        viewDelegate.setToolColor(colorId, isLight);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            viewDelegate.checkToolColor();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (WindowManagerLayoutParams != 0) {
            getWindow().setSoftInputMode(WindowManagerLayoutParams);
        }
        setStatusBarLightOrNight();
        viewDelegate.checkToolColor();
    }
}
