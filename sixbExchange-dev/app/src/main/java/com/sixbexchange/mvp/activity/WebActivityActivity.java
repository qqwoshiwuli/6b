package com.sixbexchange.mvp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.blankj.utilcode.util.AppUtils;
import com.bumptech.glide.Glide;
import com.fivefivelike.mybaselibrary.base.BaseDataBindActivity;
import com.fivefivelike.mybaselibrary.base.BaseWebLinsener;
import com.fivefivelike.mybaselibrary.base.BaseX5WebFragment;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.fivefivelike.mybaselibrary.view.DropDownPopu;
import com.fivefivelike.mybaselibrary.view.dialog.NetWorkDialog;
import com.sixbexchange.R;
import com.sixbexchange.mvp.databinder.WebActivityBinder;
import com.sixbexchange.mvp.delegate.WebActivityDelegate;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class WebActivityActivity extends BaseDataBindActivity<WebActivityDelegate, WebActivityBinder> {

    BaseX5WebFragment baseWebFragment;
    boolean isBack = false;//是否正在返回
    String activityTitle;

    public BaseX5WebFragment getBaseWebFragment() {
        return baseWebFragment;
    }

    @Override
    protected Class<WebActivityDelegate> getDelegateClass() {
        return WebActivityDelegate.class;
    }

    @Override
    public WebActivityBinder getDataBinder(WebActivityDelegate viewDelegate) {
        return new WebActivityBinder(viewDelegate);
    }

    private void initCookie() {
        com.tencent.smtt.sdk.CookieSyncManager.createInstance(this);
        com.tencent.smtt.sdk.CookieManager cm = com.tencent.smtt.sdk.CookieManager.getInstance();
        cm.setAcceptCookie(true);
        cm.setCookie(type, "appVersion=" + AppUtils.getAppVersionName());
        if (Build.VERSION.SDK_INT < 21) {
            com.tencent.smtt.sdk.CookieSyncManager.getInstance().sync();
        } else {
            com.tencent.smtt.sdk.CookieManager.getInstance().flush();
        }
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        //viewDelegate.setNoStatusBarFlag(false);
        getIntentData();
        setStatusBarLightOrNight();
        setWindowManagerLayoutParams(WindowManagerLayoutParamsNone);

        viewDelegate.getmToolbarTitle().setMaxWidth((int) CommonUtils.getDimensionPixelSize(R.dimen.trans_350px));

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        baseWebFragment = BaseX5WebFragment.newInstance(type);
        transaction.replace(R.id.fl_root, baseWebFragment, "BaseWebFragment");
        transaction.commitAllowingStateLoss();

        viewDelegate.getmToolbarBackLin().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBack = true;
                if (!baseWebFragment.goBack()) {
                    onBackPressed();
                }
            }
        });
        baseWebFragment.setWebLinsener(new BaseWebLinsener() {
            @Override
            public void onLoadEndPage() {
            }

            @Override
            public void getData(String forward, String data) {
                bridgeWeb(forward, data);
            }

            @Override
            public void onWebStart() {

            }

            @Override
            public void onLoadTitle(String title) {


                if (isBack && "about:blank".equals(title) && !baseWebFragment.goBack()) {
                    onBackPressed();
                } else {
                    if (TextUtils.isEmpty(activityTitle)) {
                        if (!"about:blank".equals(title)) {
                            initToolbar(new ToolbarBuilder().setTitle(title));
                        }
                    } else {
                        initToolbar(new ToolbarBuilder().setTitle(activityTitle));
                    }
                    if (isShowMarkBgColor) {
                        viewDelegate.setToolColor(R.color.mark_color, false);
                    }
                }
                isBack = false;
            }
        });
        initCookie();

        //        HashMap<String, List<String>> stringListHashMap = new HashMap<>();
        //        List<? extends HashMap> hashMaps = GsonUtil.getInstance().toList(json, stringListHashMap.getClass());
        //        coins = new ArrayList<>();
        //        jsonDatas = new HashMap<>();
        //        //遍历map中的键
        //        for (int i = 0; i < hashMaps.size(); i++) {
        //            for (Object key : hashMaps.get(i).keySet()) {
        //                coins.add(key.toString());
        //            }
        //        }
        //        addRequest(binder.symbolinfo(coins.get(index), this));
    }

    List<String> coins;
    int index = 0;
    Map<String, List<String>> jsonDatas;


    private void bridgeWeb(String forward, String data) {


    }

    private Handler handler = new Handler() {//进行延时跳转
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //图片保存本地
                    if (netConnectDialog != null) {
                        netConnectDialog.dimessDialog(true);
                    }
                    doSave();
                    break;
                case 2:
                    if (netConnectDialog != null) {
                        netConnectDialog.dimessDialog(true);
                    }
                    send();
                    break;
                case 3:

                    break;
            }
        }
    };
    Drawable loadDrawable;

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);//清空消息方便gc回收
        //清楚token
        super.onDestroy();
    }

    NetWorkDialog netConnectDialog;
    DropDownPopu dropDownPopu;

    @Override
    protected void clickRightIv() {
        super.clickRightIv();
        if (isShowSetting) {
            //外部链接打开
            if (dropDownPopu == null) {
                dropDownPopu = new DropDownPopu();
            }
            String[] string = CommonUtils.getStringArray(R.array.sa_select_web_setting);
            dropDownPopu.show(
                    Arrays.asList(string), viewDelegate.getmToolbarRightImg1(), this,
                    new MultiItemTypeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                            if (position == 0) {
                                UiHeplUtils.copy(WebActivityActivity.this, type, true);
                            } else {
                                UiHeplUtils.startWeb(WebActivityActivity.this, type);
                            }
                        }

                        @Override
                        public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                            return false;
                        }
                    }
            );

        }
    }

    private void loadImg(final String url, final int what) {
        //保存当前展示图片到手机
        netConnectDialog = viewDelegate.getNetConnectDialog();
        netConnectDialog.showDialog(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!TextUtils.isEmpty(url)) {
                        loadDrawable = Glide.with(WebActivityActivity.this)
                                .asDrawable().load(url).submit().get();
                        handler.sendEmptyMessage(what);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void doSave() {
        //保存当前展示图片到手机
        if (loadDrawable != null) {
            Bitmap image = ((BitmapDrawable) loadDrawable).getBitmap();
            List<Bitmap> bitmaps = new ArrayList<>();
            List<String> names = new ArrayList<>();
            bitmaps.add(image);
            names.add("/sdcard/" + CommonUtils.getString(R.string.app_name) + "_pic" + ".png");
            //ShareDialog.downBitmapToFile(this, bitmaps, names, true);
        }
    }

    private void send() {
        //分享
        if (loadDrawable != null) {
            Bitmap image = ((BitmapDrawable) loadDrawable).getBitmap();
            List<Bitmap> bitmaps = new ArrayList<>();
            bitmaps.add(image);
            UiHeplUtils.shareImgs(this, bitmaps);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            isBack = true;
            if (!baseWebFragment.goBack()) {
                onBackPressed();
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    public static void startAct(Context activity,
                                String type
    ) {
        startAct(activity, type, "", false);
    }

    public static void startAct(Context activity,
                                String type,
                                String activityTitle
    ) {
        startAct(activity, type, activityTitle, false);
    }


    public static void startAct(Context activity,
                                String type,
                                boolean isShowMarkBgColor
    ) {
        startAct(activity, type, "", isShowMarkBgColor);
    }

    public static void startActNewTask(Context activity,
                                       String type
    ) {
        Intent intent = new Intent(activity, WebActivityActivity.class);
        intent.putExtra("type", type);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static void startAct(Context activity,
                                String type,
                                String activityTitle,
                                boolean isShowMarkBgColor
    ) {
        Intent intent = new Intent(activity, WebActivityActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("isShowMarkBgColor", isShowMarkBgColor);
        intent.putExtra("activityTitle", activityTitle);
        activity.startActivity(intent);
    }




    private String type;
    private boolean isShowSetting = false;
    private boolean isShowMarkBgColor = false;

    private void getIntentData() {
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        activityTitle = intent.getStringExtra("activityTitle");
        isShowMarkBgColor = intent.getBooleanExtra("isShowMarkBgColor", false);

        if (!TextUtils.isEmpty(type)) {
            isShowSetting = !(type.contains("blz.bicoin") || type.contains("tblz.bicoin"));
        }

        initToolbar(new ToolbarBuilder().setTitle(CommonUtils.getString(R.string.app_name)).setmRightImg1(isShowSetting ? CommonUtils.getString(R.string.ic_Dots1) : ""));
        if (isShowMarkBgColor) {
            viewDelegate.setToolColor(R.color.mark_color, false);
        }
        Uri uri = getIntent().getData();
        if (uri != null) {
            String url = uri.toString();
            if (!TextUtils.isEmpty(url) && TextUtils.isEmpty(type)) {
                type = url;
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            baseWebFragment.loadUrl(type);
        }
    }

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {

        }
    }

}
