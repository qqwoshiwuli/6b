package com.fivefivelike.mybaselibrary.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.fivefivelike.mybaselibrary.utils.GlobleContext;
import com.fivefivelike.mybaselibrary.utils.UUIDS;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebView;
import com.yanzhenjie.nohttp.Headers;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * @创建者 CSDN_LQR
 * @描述 Application基类
 */
public abstract class BaseApp extends Application {
    protected static BaseApp instance;

    //以下属性应用于整个应用程序，合理利用资源，减少资源浪费
    public static synchronized BaseApp getInstance() {
        return instance;
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        //        Configuration config=new Configuration();
        //        config.setToDefaults();
        //        res.updateConfiguration(config,res.getDisplayMetrics());
        return res;
    }

    public abstract void startCustomerService(Activity activity);

    //获取登录页面class
    public abstract Class getLoginActivityClass();

    public abstract void errorAndChangeNetUrl();

    public abstract void returnHeader(Headers headers);

    @Override
    public void onCreate() {
        super.onCreate();
        //对全局属性赋值
        if (isMainProcess()) {
            instance = this;
            GlobleContext.init(this);
            UUIDS.buidleID(this).check();
            preinitX5WebCore();
            Utils.init(this);
            closeAndroidPDialog();
        }

    }
    // 去掉android P 反射警告
    private void closeAndroidPDialog() {
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract String getHttpKey();

    private void preinitX5WebCore() {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.i("QbSdk", " onViewInitFinished is " + arg0);
                Log.i("QbSdk", WebView.getCrashExtraMessage(BaseApp.getInstance().getApplicationContext()));
            }

            @Override
            public void onCoreInitFinished() {

            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(this, cb);
    }

    /**
     * OnLowMemory是Android提供的API，在系统内存不足，
     * 所有后台程序（优先级为background的进程，不是指后台运行的进程）都被杀死时，系统会调用OnLowMemory
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //垃圾回收
        System.gc();
    }

    /**
     * 获取当前进程名
     */
    public String getCurrentProcessName() {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
            if (process.pid == pid) {
                processName = process.processName;
            }
        }
        return processName;
    }

    /**
     * 包名判断是否为主进程
     *
     * @param
     * @return
     */
    public boolean isMainProcess() {
        return getApplicationContext().getPackageName().equals(getCurrentProcessName());
    }
}
