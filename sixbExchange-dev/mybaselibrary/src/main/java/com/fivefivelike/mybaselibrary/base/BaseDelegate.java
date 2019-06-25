package com.fivefivelike.mybaselibrary.base;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.circledialog.CircleDialog;
import com.circledialog.params.ProgressParams;
import com.fivefivelike.mybaselibrary.R;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.fivefivelike.mybaselibrary.mvp.view.IDelegateImpl;
import com.fivefivelike.mybaselibrary.utils.AndroidUtil;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.ListUtils;
import com.fivefivelike.mybaselibrary.view.IconFontTextview;
import com.fivefivelike.mybaselibrary.view.NoDataView;
import com.fivefivelike.mybaselibrary.view.dialog.NetWorkDialog;
import com.githang.statusbar.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郭青枫 on 2017/7/7.
 */

public abstract class BaseDelegate extends IDelegateImpl {
    private TextView mToolbarTitle;
    private TextView mToolbarSubTitle;
    private Toolbar mToolbar;
    private IconFontTextview mToolbarRightImg1;
    private IconFontTextview mToolbarRightImg2;
    private IconFontTextview mToolbarRightImg3;

    private View viewImg2Point;
    private View viewImg1Point;
    private View viewImgPoint;
    private View viewBackPoint;
    private View view_line;
    private DialogFragment netConnectDialog;
    private LinearLayout layoutTitleBar;
    private FrameLayout fl_content;

    private FragmentManager fragmentManager;
    private boolean isNoStatusBarFlag = false;
    private CircleDialog.CircleDialogLinsener circleDialogLinsener;
    private LinearLayout mToolbarBackLin;
    private TextView mToolbarBackTxt;
    private IconFontTextview mToolbarBack;
    private View mViewSubtitlePoint;

    public static int status_hight=0;

    public NetWorkDialog getNetConnectDialog() {
        return initDialog("加载中...", true);
    }

    public NetWorkDialog getNetConnectDialog(String title) {
        return initDialog(title, true);
    }

    public NetWorkDialog getNetConnectDialog(String title, boolean isCancelable) {
        return initDialog(title, isCancelable);
    }


    private NetWorkDialog initDialog(String title, boolean isCancelable) {
        netConnectDialog = new CircleDialog.Builder(this.getActivity())
                .setProgressText(title)
                .setProgressStyle(ProgressParams.STYLE_SPINNER)
                .setCancelable(isCancelable)
                .setCanceledOnTouchOutside(false)
                .setCircleDialogLinsener(circleDialogLinsener)
                .create();
        if (fragmentManager == null) {
            fragmentManager = getActivity().getSupportFragmentManager();
        }
        return new NetWorkDialog(netConnectDialog, fragmentManager);
    }


    public boolean isNoStatusBarFlag() {
        return isNoStatusBarFlag;
    }

    //在 initToolbar 之前调用
    public void setNoStatusBarFlag(boolean noStatusBarFlag) {
        isNoStatusBarFlag = noStatusBarFlag;
    }

    public void setCircleDialogLinsener(CircleDialog.CircleDialogLinsener circleDialogLinsener) {
        this.circleDialogLinsener = circleDialogLinsener;
    }

    public void setShowMsg(boolean isShow) {
        if (mViewSubtitlePoint != null) {
            mViewSubtitlePoint.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 设置返回按钮 文字 或者 iconfont
     *
     * @param txt
     */
    public void setBackIconFontText(String txt) {
        if (mToolbarBack != null) {
            //重置属性
            mToolbarBack.setRotation(0);
            mToolbarBack.setScaleX(1);
            mToolbarBack.setScaleY(1);
            mToolbarBack.setText(txt);
        }
    }

    private int mColorId;
    private boolean mIslight;

    public void checkToolColor() {
        if (mColorId != 0) {
            setToolColor(mColorId, mIslight);
        }
    }

    public void setToolColor(int colorId, boolean isLight) {
        mColorId = colorId;
        mIslight = isLight;
        Log.i("setToolColor", "setToolColor" + colorId + "isLight" + isLight + "mToolbar != null" + (mToolbar != null));
        if (layoutTitleBar != null) {
            setStatusBg(isLight);
            if (colorId != 0) {
                int alpha = 255;
                if (layoutTitleBar.getBackground() != null) {
                    alpha = layoutTitleBar.getBackground().mutate().getAlpha();
                }
                layoutTitleBar.setBackgroundColor(CommonUtils.getColor(colorId));
                layoutTitleBar.getBackground().mutate().setAlpha(alpha);
            }
            if (isLight) {
                mToolbarTitle.setTextColor(CommonUtils.getColor(R.color.color_font1));
                mToolbarSubTitle.setTextColor(CommonUtils.getColor(R.color.mark_color));
                mToolbarRightImg1.setTextColor(CommonUtils.getColor(R.color.color_font1));
                mToolbarRightImg2.setTextColor(CommonUtils.getColor(R.color.color_font1));
                mToolbarRightImg3.setTextColor(CommonUtils.getColor(R.color.color_font1));
                mToolbarBackTxt.setTextColor(CommonUtils.getColor(R.color.color_font1));
                mToolbarBack.setTextColor(CommonUtils.getColor(R.color.color_font1));
                view_line.setVisibility(View.VISIBLE);
            } else {
                mToolbarTitle.setTextColor(CommonUtils.getColor(R.color.color_font1_dark));
                mToolbarSubTitle.setTextColor(CommonUtils.getColor(R.color.color_font1_dark));
                mToolbarRightImg1.setTextColor(CommonUtils.getColor(R.color.color_font1_dark));
                mToolbarRightImg2.setTextColor(CommonUtils.getColor(R.color.color_font1_dark));
                mToolbarRightImg3.setTextColor(CommonUtils.getColor(R.color.color_font1_dark));
                mToolbarBackTxt.setTextColor(CommonUtils.getColor(R.color.color_font1_dark));
                mToolbarBack.setTextColor(CommonUtils.getColor(R.color.color_font1_dark));
                view_line.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 初始化标题栏
     *
     * @param builder 设置标题栏参数的对象
     */
    protected void initToolBar(AppCompatActivity activity, View.OnClickListener listener, ToolbarBuilder builder) {
        mToolbar = getViewById(R.id.toolbar);
        mToolbarTitle = getViewById(R.id.toolbar_title);
        mToolbarSubTitle = getViewById(R.id.toolbar_subtitle);
        layoutTitleBar = getViewById(R.id.layout_title_bar);
        mToolbarRightImg1 = getViewById(R.id.toolbar_img);
        mToolbarRightImg2 = getViewById(R.id.toolbar_img1);
        mToolbarRightImg3 = getViewById(R.id.toolbar_img2);
        viewImg2Point = getViewById(R.id.view_img2_point);
        viewImg1Point = getViewById(R.id.view_img1_point);
        viewImgPoint = getViewById(R.id.view_img_point);
        viewBackPoint = getViewById(R.id.view_back_point);
        view_line = getViewById(R.id.view_line);
        fl_content = getViewById(R.id.fl_content);


        mToolbarBackTxt = getViewById(R.id.toolbar_back_txt);
        mToolbarBackLin = getViewById(R.id.toolbar_lin_back);
        mToolbarBack = getViewById(R.id.toolbar_back);
        mViewSubtitlePoint = getViewById(R.id.view_subtitle_point);


        //标题总背景
        //        if (layoutTitleBar != null) {
        //            if (builder.getLayoutBarBack() != 0) {
        //                layoutTitleBar.setBackgroundResource(builder.getLayoutBarBack());
        //            } else {
        //                layoutTitleBar.setBackgroundResource(R.color.transparent);
        //            }
        //        }


        //状态栏
        initStatus(activity, builder.getStatusBack());
        if (mToolbar != null) {
            //将Toolbar显示到界面
            activity.setSupportActionBar(mToolbar);
        }
        if (mToolbarTitle != null) {
            //getTitle()的值是activity的android:lable属性值
            mToolbarTitle.setText(activity.getTitle());
            //设置默认的标题不显示
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        //设置标题
        if (!TextUtils.isEmpty(builder.getTitle())) {
            mToolbarTitle.setText(builder.getTitle());
        }
        //设置右边的文字并显示
        if (!TextUtils.isEmpty(builder.getSubTitle())) {
            mToolbarSubTitle.setVisibility(View.VISIBLE);
            mToolbarSubTitle.setText(builder.getSubTitle());
            mToolbarSubTitle.setOnClickListener(listener);
        }
        //设置右边第一个按钮并显示
        if (!TextUtils.isEmpty(builder.getmRightImg1())) {
            mToolbarRightImg1.setVisibility(View.VISIBLE);
            mToolbarRightImg1.setText(builder.getmRightImg1());
            mToolbarRightImg1.setOnClickListener(listener);
        }
        //设置右边第二个按钮并显示
        if (!TextUtils.isEmpty(builder.getmRightImg2())) {
            mToolbarRightImg2.setVisibility(View.VISIBLE);
            mToolbarRightImg2.setText(builder.getmRightImg2());
            mToolbarRightImg2.setOnClickListener(listener);
        }   //设置右边第二个按钮并显示
        if (!TextUtils.isEmpty(builder.getmRightImg3())) {
            mToolbarRightImg3.setVisibility(View.VISIBLE);
            mToolbarRightImg3.setText(builder.getmRightImg3());
            mToolbarRightImg3.setOnClickListener(listener);
        }
        //设置显示返回按钮  可自定义图标
        if (builder.isShowBack()) {
            showBack(activity, builder.getBackTxt());

        }
        //        //        //设置标题栏的背景颜色
        //        if (builder.getmToolbarBackColor() != 0) {
        //            mToolbar.setBackgroundColor(builder.getmToolbarBackColor());
        //        } else {
        //            mToolbar.setBackgroundColor(CommonUtils.getColor(R.color.toolbar_bg));
        //        }
        //设置标题是否显示
        if (!builder.isTitleShow()) {
            mToolbarTitle.setVisibility(View.GONE);
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        //boolean isNight = SaveUtil.getInstance().getBoolean("isNight");
        setToolColor(R.color.toolbar_bg, true);
    }

    /**
     * 头部高度
     *
     * @param activity
     */
    public void initStatus(Activity activity, @DrawableRes int statusBack) {
        View v_status = getViewById(R.id.v_status);
        if (v_status != null && isNoStatusBarFlag) {
            if (statusBack != 0) {
                v_status.setBackgroundResource(statusBack);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                v_status.getLayoutParams().height = AndroidUtil.getStatusHeight(activity);
                status_hight=AndroidUtil.getStatusHeight(activity);
            } else {
                v_status.getLayoutParams().height = 0;
            }
            v_status.requestLayout();
        } else {
            v_status.getLayoutParams().height = 0;
            v_status.requestLayout();
        }
    }

    public View getStatus() {
        View v_status = getViewById(R.id.v_status);
        return v_status;
    }


    public void setStatusBg(boolean isLight) {
        View v_status = getViewById(R.id.v_status);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mColorId != 0) {
                if (!isNoStatusBarFlag) {
                    v_status.setBackgroundColor(CommonUtils.getColor(mColorId));
                    StatusBarCompat.setStatusBarColor(this.getActivity(),
                            CommonUtils.getColor(mColorId));
                } else {
                    v_status.setBackgroundColor(CommonUtils.getColor(R.color.transparent));
                }
            }
            if (isLight) {
                if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
                    StatusBarCompat.setLightStatusBar(getActivity().getWindow(), false);
                    v_status.setBackgroundColor(CommonUtils.getColor(R.color.font_grey));
                } else {
                    StatusBarCompat.setLightStatusBar(getActivity().getWindow(), isLight);
                }
            } else {
                StatusBarCompat.setLightStatusBar(getActivity().getWindow(), isLight);
            }
        }
    }

    /**
     * 版本号小于21的后退按钮图片
     */
    private void showBack(final AppCompatActivity activity, String backTxt) {
        //        mToolbar.setNavigationIcon(navigationBar);
        //        initNavigation();
        //        mNavButtonView.setMinimumWidth(0);
        //        mNavButtonView.setPadding(activity.getResources().getDimensionPixelSize(R.dimen.trans_20px), 0,
        //                activity.getResources().getDimensionPixelSize(R.dimen.trans_40px), 0);
        //        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                if (navigationBar == R.drawable.lefticon) {
        //                    activity.onBackPressed();
        //                }
        //            }
        //        });
        mToolbarBackLin.setVisibility(View.VISIBLE);
        mToolbarBackLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
        mToolbarBack.setText(CommonUtils.getString(R.string.ic_Back));
        mToolbarBackTxt.setText(backTxt);
    }


    public void setPointNum(int num, FrameLayout fl_content) {
        if (num > 0) {
            String showNum;
            if (num > 99) {
                showNum = 99 + "+";
            } else {
                showNum = num + "";
            }
            if (fl_content.getChildCount() == 0) {
                ViewGroup.LayoutParams layoutParams = fl_content.getLayoutParams();
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                fl_content.setLayoutParams(layoutParams);
                TextView textView = new TextView(fl_content.getContext());
                textView.setMinWidth((int) CommonUtils.getDimensionPixelSize(R.dimen.trans_25px));
                textView.setMinHeight((int) CommonUtils.getDimensionPixelSize(R.dimen.trans_25px));
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(
                        (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_5px),
                        (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_1px),
                        (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_5px),
                        (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_1px)
                );
                textView.setText(showNum);
                textView.setTextColor(CommonUtils.getColor(R.color.white));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, CommonUtils.getDimensionPixelSize(R.dimen.text_trans_15px));
                fl_content.addView(textView);
            } else {
                TextView textView = (TextView) fl_content.getChildAt(0);
                textView.setText(showNum);
            }
            fl_content.setVisibility(View.VISIBLE);
        } else {
            fl_content.setVisibility(View.GONE);
        }
    }

    public void showNodata(int requestCode, BaseDataBind.StopNetMode type) {
        if (!ListUtils.isEmpty(getRequestCodeNoData())) {
            for (int i = 0; i < getRequestCodeNoData().size(); i++) {
                if (requestCode == (int) getRequestCodeNoData().get(i)) {
                    FrameLayout rootView = getRootView();
                    if (type == BaseDataBind.StopNetMode.NET_SUCCESS) {
                        if (rootView.getChildCount() == 2) {
                            View childAt = rootView.getChildAt(1);
                            if (childAt instanceof NoDataView) {
                                ((NoDataView) childAt).hide();
                            }
                        }
                    } else {
                        if (rootView.getChildCount() == 1) {
                            newNoDataView(rootView);
                        }
                        View childAt = rootView.getChildAt(1);
                        if (childAt instanceof NoDataView) {
                            if (type == BaseDataBind.StopNetMode.NET_ERROR ||
                                    type == BaseDataBind.StopNetMode.JSON_ERROR) {
                                ((NoDataView) childAt).show(CommonUtils.getString(R.string.str_no_data_by_service));
                            } else if (type == BaseDataBind.StopNetMode.VIEW_ERROR) {

                            } else {
                                if (!TextUtils.isEmpty(showNoDataText)) {
                                    ((NoDataView) childAt).show(showNoDataText);
                                } else {
                                    ((NoDataView) childAt).show();
                                }
                            }
                        }

                    }
                    return;
                }
            }
        }
    }

    protected List<Integer> requestCodeNoData;


    public List<Integer> getRequestCodeNoData() {
        return requestCodeNoData;
    }

    public void setNodataCodes(Integer... codes) {
        requestCodeNoData = new ArrayList<>();
        for (int i = 0; i < codes.length; i++) {
            requestCodeNoData.add(codes[0]);
        }
    }

    View.OnClickListener noDataClickReRefresh;

    public void setNodataCodes(View.OnClickListener onClickListener, Integer... codes) {
        noDataClickReRefresh = onClickListener;
        requestCodeNoData = new ArrayList<>();
        for (int i = 0; i < codes.length; i++) {
            requestCodeNoData.add(codes[0]);
        }
    }

    public void showNodata(boolean isShow) {
        showNodata(isShow, TextUtils.isEmpty(showNoDataText) ? CommonUtils.getString(R.string.str_no_data) : showNoDataText);
    }

    String showNoDataText;

    public void setShowNoDataText(String text) {
        if (!TextUtils.isEmpty(text)) {
            showNoDataText = text;
            FrameLayout rootView = getRootView();
            if (rootView.getChildCount() == 2) {
                View childAt = rootView.getChildAt(1);
                if (childAt instanceof NoDataView) {
                    ((NoDataView) childAt).setToastText(text);
                }
            }
        }
    }

    private void newNoDataView(FrameLayout rootView) {
        NoDataView noDataView = new NoDataView(rootView.getContext());
        noDataView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rootView.addView(noDataView);
        if (layoutTitleBar != null) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) noDataView.getLayoutParams();
            layoutParams.setMargins(0, layoutTitleBar.getMeasuredHeight(), 0, 0);
            noDataView.setLayoutParams(layoutParams);
        }
        if (noDataClickReRefresh != null) {
            View viewById = noDataView.findViewById(R.id.ic_nodata);
            viewById.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noDataClickReRefresh.onClick(v);
                }
            });
        }
    }

    public void showNodata(boolean isShow, String toast) {
        FrameLayout rootView = getRootView();
        if (isShow) {
            if (rootView.getChildCount() == 1) {
                newNoDataView(rootView);
            }
            View childAt = rootView.getChildAt(1);
            if (childAt instanceof NoDataView) {
                ((NoDataView) childAt).show(toast);
            }
        } else {
            if (rootView.getChildCount() == 2) {
                View childAt = rootView.getChildAt(1);
                if (childAt instanceof NoDataView) {
                    ((NoDataView) childAt).hide();
                }
            }
        }
    }

    public void hideLayoutTitleBarBg() {
        getLayoutTitleBar().getBackground().mutate().setAlpha(0);
        getView_line().setVisibility(View.GONE);
    }


    public View getView_line() {
        return view_line;
    }

    public void setView_line(View view_line) {
        this.view_line = view_line;
    }

    public TextView getmToolbarTitle() {
        return mToolbarTitle;
    }

    public TextView getmToolbarSubTitle() {
        return mToolbarSubTitle;
    }

    public Toolbar getmToolbar() {
        return mToolbar;
    }

    public IconFontTextview getmToolbarRightImg1() {
        return mToolbarRightImg1;
    }

    public IconFontTextview getmToolbarRightImg2() {
        return mToolbarRightImg2;
    }

    public IconFontTextview getmToolbarRightImg3() {
        return mToolbarRightImg3;
    }

    public LinearLayout getLayoutTitleBar() {
        return layoutTitleBar;
    }




    public LinearLayout getmToolbarBackLin() {
        return mToolbarBackLin;
    }

    public TextView getmToolbarBackTxt() {
        return mToolbarBackTxt;
    }

    public IconFontTextview getmToolbarBack() {
        return mToolbarBack;
    }

    public View getViewImg2Point() {
        return viewImg2Point;
    }

    public View getViewImg1Point() {
        return viewImg1Point;
    }

    public View getViewImgPoint() {
        return viewImgPoint;
    }

    public FrameLayout getFl_content() {
        return fl_content;
    }

    public View getViewBackPoint() {
        return viewBackPoint;
    }

    public int getmColorId() {
        return mColorId;
    }

    public boolean ismIslight() {
        return mIslight;
    }



}
