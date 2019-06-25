package com.sixbexchange.base;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.fivefivelike.mybaselibrary.base.BaseApp;
import com.fivefivelike.mybaselibrary.http.SSLSocketClient;
import com.fivefivelike.mybaselibrary.http.WebSocket2Request;
import com.fivefivelike.mybaselibrary.http.WebSocketRequest;
import com.fivefivelike.mybaselibrary.utils.GlobleContext;
import com.fivefivelike.mybaselibrary.utils.SaveUtil;
import com.fivefivelike.mybaselibrary.utils.UUIDS;
import com.fivefivelike.mybaselibrary.utils.logger.KLog;
import com.fm.openinstall.OpenInstall;
import com.sixbexchange.BuildConfig;
import com.sixbexchange.mvp.activity.LoginAndRegisteredActivity;
import com.sixbexchange.server.HttpUrl;
import com.sixbexchange.server.TraceServiceImpl;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.xdandroid.hellodaemon.DaemonEnv;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.OkHttpNetworkExecutor;
import com.yanzhenjie.nohttp.cache.DBCacheStore;
import com.yanzhenjie.nohttp.cookie.DBCookieStore;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.helper.ExceptionHandler;
import okhttp3.OkHttpClient;
import skin.support.SkinCompatManager;
import skin.support.app.SkinCardViewInflater;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;


/**
 * Created by 郭青枫 on 2017/9/25.
 */

public class Application extends BaseApp {

    boolean isInitUm = false;
    public static boolean isInitPush = false;
    boolean isInitNoHttp = false;

    @Override
    public void onCreate() {
        super.onCreate();
        if (isMainProcess()) {
            initClient();
        }
        UMConfigure.setLogEnabled(BuildConfig.isLog);
        UMConfigure.init(this,
                AppConst.pushId,
                AppConst.umS1,
                UMConfigure.DEVICE_TYPE_PHONE,
                AppConst.pushSecret);
        PlatformConfig.setWeixin("wx5d125e55b5a7825d",
                "cc1f32d534dd262602291529009bcc03");
        if (UMConfigure.getInitStatus()) {
            //initUmeng();
        }
    }

    @Override
    public String getHttpKey() {
        return "";
    }

    @Override
    public void returnHeader(Headers headers) {
        if (headers.containsKey("userTempId")) {
            //setAlias();
        }
    }


    private void initSkin() {
        SkinCompatManager.withoutActivity(GlobleContext.getInstance().getApplicationContext())                         // 基础控件换肤初始化
                .addInflater(new SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
                .addInflater(new SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
                .addInflater(new SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
                .setSkinStatusBarColorEnable(true)                     // 关闭状态栏换肤，默认打开[可选]
                .setSkinWindowBackgroundEnable(true)                  // 关闭windowBackground换肤，默认打开[可选]
                .loadSkin();
    }


    private void initClient() {
        //客户端进程中初始化操作
        if (isMainProcess()) {
            //EventBus.getDefault().register(this);
            //initNohttp();
            OpenInstall.init(this);
            initNohttp();
            //开启log日志
            KLog.init(!AppConst.isSSL);
            //英文切换
            Configuration configuration = getResources().getConfiguration();
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            configuration.fontScale = 1;
            //更新配置
            getResources().updateConfiguration(configuration, displayMetrics);
            //初始化换肤
            initSkin();

            DaemonEnv.initialize(this, TraceServiceImpl.class, DaemonEnv.DEFAULT_WAKE_UP_INTERVAL);
            TraceServiceImpl.sShouldStopService = false;
            DaemonEnv.startServiceMayBind(TraceServiceImpl.class);


            KLog.i("NdkUtils", getSign());
            Fragmentation.builder()
                    // 设置 栈视图 模式为 （默认）悬浮球模式   SHAKE: 摇一摇唤出  NONE：隐藏， 仅在Debug环境生效
                    .stackViewMode(Fragmentation.NONE)
                    .debug(BuildConfig.isLog) // 实际场景建议.debug(BuildConfig.DEBUG)
                    /**
                     * 可以获取到{@link me.yokeyword.fragmentation.exception.AfterSaveStateTransactionWarning}
                     * 在遇到After onSaveInstanceState时，不会抛出异常，会回调到下面的ExceptionHandler
                     */
                    .handleException(new ExceptionHandler() {
                        @Override
                        public void onException(Exception e) {
                            // 以Bugtags为例子: 把捕获到的 Exception 传到 Bugtags 后台。
                            // Bugtags.sendException(e);
                        }
                    })
                    .install();
        }
    }

    /**
     * 展示了如何用Java代码获取签名
     */
    private String getSign() {
        try {
            // 下面几行代码展示如何任意获取Context对象，在jni中也可以使用这种方式
            //            Class<?> activityThreadClz = Class.forName("android.app.ActivityThread");
            //            Method currentApplication = activityThreadClz.getMethod("currentApplication");
            //            Application application = (Application) currentApplication.invoke(null);
            //            PackageManager pm = application.getPackageManager();
            //            PackageInfo pi = pm.getPackageInfo(application.getPackageName(), PackageManager.GET_SIGNATURES);

            PackageManager pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signatures = pi.signatures;
            Signature signature0 = signatures[0];
            return signature0.toCharsString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    //    public String getHttpKey() {
    //        return NdkUtils.key();
    //    }

    private void initNohttp() {
        InitializationConfig config = InitializationConfig.newBuilder(this)
                // 全局连接服务器超时时间，单位毫秒，默认10s。
                .connectionTimeout(30 * 1000)
                // 全局等待服务器响应超时时间，单位毫秒，默认10s。
                .readTimeout(30 * 1000)
                .addHeader("from", "Android")
                .addHeader("mobilId", UUIDS.getUUID())
                .addHeader("mobilKey", EncryptUtils.encryptMD5ToString(
                        UUIDS.getUUID(), UUIDS.getUUID()))
                .addHeader("appVersion", AppUtils.getAppVersionName())
                // 配置缓存，默认保存数据库DBCacheStore，保存到SD卡使用DiskCacheStore。
                .cacheStore(
                        // 如果不使用缓存，setEnable(false)禁用。
                        new DBCacheStore(this).setEnable(false)
                )
                // 配置Cookie，默认保存数据库DBCookieStore，开发者可以自己实现CookieStore接口。
                .cookieStore(
                        // 如果不维护cookie，setEnable(false)禁用。
                        new DBCookieStore(this).setEnable(false)
                )
                // 配置网络层，默认URLConnectionNetworkExecutor，如果想用OkHttp：OkHttpNetworkExecutor。
                .networkExecutor(new OkHttpNetworkExecutor())
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .retry(1) // 全局重试次数，配置后每个请求失败都会重试x次。
                .build();
        Logger.setDebug(false);
        Logger.setTag("NoHttpSample");// 打印Log的tag。
        NoHttp.initialize(config);
        isInitNoHttp = true;

    }


    public static final String START_APP = "startApp";
    public static final String INITED_APP = "initedApp";

    //    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    //    public void onEventBackgroundThread(StartPageEventBus event) {
    //        if (event.getMsg() != null && START_APP.equals(event.getMsg())) {
    //            Log.i("eventlog", "进行初始化操作");
    //
    //            Thread.setDefaultUncaughtExceptionHandler(new CrashHandler());
    //
    //
    //            //设置配置画廊可以加载网络图片
    //            Album.initialize(
    //                    AlbumConfig.newBuilder(this)
    //                            .setAlbumLoader(new GlideAlbumLoader()) // 设置Album加载器。
    //                            .setLocale(Locale.CHINA) // 比如强制设置在任何语言下都用中文显示。
    //                            .build()
    //            );
    //            //初始化完成以后发出消息在启动页接收，通知启动页可以进入主页面了，然后进行eventbus解绑
    //            EventBus.getDefault().post(new StartPageEventBus(INITED_APP));
    //            EventBus.getDefault().unregister(this);
    //        }
    //    }


    //客户服务
    public void startCustomerService(Activity activity) {

    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        WebSocketRequest.getInstance().onDestory();
        WebSocket2Request.getInstance().onDestory();
        super.onTerminate();
    }

    public Class getLoginActivityClass() {
        return LoginAndRegisteredActivity.class;//LoginRegisteredActivity.class;
    }

    public void errorAndChangeNetUrl() {
//        HttpUrl.getIntance().delectHttpUrl();
//        HttpUrl.setBaseUrl(BuildConfig.app2BaseUrl);
    }

}
