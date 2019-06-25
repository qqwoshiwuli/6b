package com.sixbexchange.mvp.activity;

import android.app.Activity;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.circledialog.CircleDialogHelper;
import com.fivefivelike.mybaselibrary.base.BaseDataBindActivity;
import com.fivefivelike.mybaselibrary.utils.AppUtil;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.SaveUtil;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.utils.glide.GlideCacheUtil;
import com.fivefivelike.mybaselibrary.view.dialog.NetWorkDialog;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.AppVersion;
import com.sixbexchange.entity.bean.UserLoginInfo;
import com.sixbexchange.mvp.databinder.HomeBinder;
import com.sixbexchange.mvp.delegate.HomeDelegate;
import com.sixbexchange.mvp.dialog.UpdateDialog;
import com.sixbexchange.mvp.fragment.MainFragment;
import com.sixbexchange.server.HttpUrl;
import com.sixbexchange.server.TraceServiceImpl;
import com.sixbexchange.server.UpdateService;
import com.sixbexchange.utils.NdkUtils;
import com.sixbexchange.utils.UserSet;
import com.xdandroid.hellodaemon.DaemonEnv;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.List;

import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class HomeActivity extends BaseDataBindActivity<HomeDelegate, HomeBinder> {

    @Override
    protected Class<HomeDelegate> getDelegateClass() {
        return HomeDelegate.class;
    }

    @Override
    public HomeBinder getDataBinder(HomeDelegate viewDelegate) {
        return new HomeBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        TraceServiceImpl.sShouldStopService = false;
        DaemonEnv.startServiceMayBind(TraceServiceImpl.class);
        //ignoreBatteryOptimization(this);
        NdkUtils.start();
    }

    private void initFragment() {
        addRequest(binder.getlatestversion(AppUtils.getAppVersionName(), this));
        addRequest(binder.getAccountDetail(false, this));
        addRequest(binder.getAccount(false, this));
        if (UserLoginInfo.isLoginNoToast()) {
            if (findFragment(MainFragment.class) == null) {
                loadRootFragment(R.id.fl_container, new MainFragment());
            }
        } else {
            startActivity(new Intent(this, LoginAndRegisteredActivity.class));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (TextUtils.isEmpty(host)) {
            addRequest(binder.gethost("1.1.0", this));
        } else {
            initFragment();
        }
    }


    @Override
    public void onBackPressedSupport() {
        ISupportFragment topFragment = getTopFragment();
        if (topFragment instanceof MainFragment) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            super.onBackPressedSupport();
        }
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }

    String host;

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                //版本更新
                appVersion = GsonUtil.getInstance().toObj(data, AppVersion.class);
                version();
//                if (!TextUtils.isEmpty(appVersion.getHost()))
//                {
//                    SaveUtil.getInstance().saveString("hosts",appVersion.getHost());
//                }
                break;
            case 0x124:
                host = GsonUtil.getInstance().getValue(data, "host");
                HttpUrl.getIntance().delectHttpUrl();
                HttpUrl.setBaseUrl(host);
                initFragment();
                break;
            case 0x125:
                SaveUtil.getInstance().saveString("qianbaodata",data);
                break;
            case 0x126:
                SaveUtil.getInstance().saveString("qianbaodata1",data);
                break;
        }
    }

    AppVersion appVersion;
    boolean isShowDialog = false;

    private void version() {
        UserSet.getinstance().setSystemVersion(appVersion.getSystemVersion());
        if (!isShowDialog && UiHeplUtils.compareVersion(appVersion.getSystemVersion(),
                AppUtils.getAppVersionName()) == 1
                && !SaveUtil.getInstance().getBoolean("isUpdataCancel" + AppUtils.getAppVersionName())) {
            new UpdateDialog(HomeActivity.this)
                    .setAppVersion(appVersion)
                    .setCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            isShowDialog = true;
                        }
                    })
                    .setDefaultClickLinsener(new DefaultClickLinsener() {
                        @Override
                        public void onClick(View view, int position, Object item) {
                            if (position == 0) {
                                //取消
                                if (appVersion.isMustUpdate()) {
                                    ActivityUtils.finishAllActivities();
                                    //ActUtil.getInstance().AppExit(MainActivity.this);
                                } else {
                                    SaveUtil.getInstance().saveBoolean("isUpdataCancel" + AppUtils.getAppVersionName(), true);
                                }
                            } else if (position == 1) {
                                //确认
                                if (AppUtil.isWifi(mContext)) {
                                    updataApp();
                                } else {
                                    CircleDialogHelper.initDefaultDialog(HomeActivity.this, CommonUtils.getString(R.string.isinstall_in_no_wifi), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            updataApp();
                                        }
                                    }).setNegative(CommonUtils.getString(R.string.str_cancel), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (appVersion.isMustUpdate()) {
                                                ActivityUtils.finishAllActivities();
                                                // ActUtil.getInstance().AppExit(MainActivity.this);
                                            }
                                        }
                                    }).show();
                                }
                            }
                        }
                    }).showDialog();
            isShowDialog = true;
            GlideCacheUtil.getInstance().clearImageAllCache();
        }
    }

    private void updataApp() {
        AndPermission.with(this)
                .permission(Permission.WRITE_EXTERNAL_STORAGE,
                        Permission.READ_EXTERNAL_STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        UpdateService.
                                Builder.create(appVersion.getDownloadAddr())
                                .setStoreDir("update")
                                .setIcoResId(R.drawable.artboard)
                                .setDownloadSuccessNotificationFlag(Notification.DEFAULT_ALL)
                                .setDownloadErrorNotificationFlag(Notification.DEFAULT_ALL)
                                .setAppVersion(appVersion)
                                .build(HomeActivity.this);
                        if (appVersion.isMustUpdate()) {
                            NetWorkDialog netConnectDialog = viewDelegate.getNetConnectDialog(CommonUtils.getString(R.string.str_in_download), false);
                            netConnectDialog.showDialog(true);
                        }
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        CircleDialogHelper.initDefaultToastDialog(HomeActivity.this, CommonUtils.getString(R.string.str_permission_read_write), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (appVersion.isMustUpdate()) {
                                    ActivityUtils.finishAllActivities();
                                }
                            }
                        });
                    }
                }).start();
    }

    public void ignoreBatteryOptimization(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            Intent intent = null;
            try {
                intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (intent != null) {
                if (intent.resolveActivity(getPackageManager()) != null) {
                    boolean hasIgnored = powerManager.isIgnoringBatteryOptimizations(activity.getPackageName());
                    //  判断当前APP是否有加入电池优化的白名单，如果没有，弹出加入电池优化的白名单的设置对话框。
                    if (!hasIgnored) {
                        startActivity(intent);
                    }
                }
            }
        }
    }

}
