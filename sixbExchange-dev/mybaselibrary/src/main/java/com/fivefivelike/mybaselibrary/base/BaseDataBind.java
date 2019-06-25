package com.fivefivelike.mybaselibrary.base;


import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.EncodeUtils;
import com.fivefivelike.mybaselibrary.entity.ResultDialogEntity;
import com.fivefivelike.mybaselibrary.http.ServiceDataCallback;
import com.fivefivelike.mybaselibrary.mvp.databind.IDataBind;
import com.fivefivelike.mybaselibrary.mvp.view.IDelegate;
import com.fivefivelike.mybaselibrary.utils.SaveUtil;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.utils.logger.KLog;
import com.fivefivelike.mybaselibrary.view.dialog.ResultDialog;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.error.NotFoundCacheError;
import com.yanzhenjie.nohttp.error.TimeoutError;
import com.yanzhenjie.nohttp.error.URLError;
import com.yanzhenjie.nohttp.error.UnKnownHostError;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.ProtocolException;
import java.security.SecureRandom;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by 郭青枫 on 2017/7/3.
 */

public abstract class BaseDataBind<T extends IDelegate> implements IDataBind<T> {
    protected T viewDelegate;
    protected Map<String, Object> baseMap;
    protected CompositeDisposable compositeDisposable;
    public static final int JsonErrorStatu = 110;
    public static final int viewErrorStatu = 120;


    public enum StopNetMode {
        /**
         * 成功请求并加载
         */
        NET_SUCCESS,
        /**
         * 服务端报告错误
         */
        NET_ERROR,
        /**
         * 404 或者 服务端传值 json不规范
         */
        JSON_ERROR,
        /**
         * 请求错误
         */
        ERROR,
        /**
         * 显示错误
         */
        VIEW_ERROR

    }

    public Map<String, Object> getMap() {
        return baseMap;
    }

    public BaseDataBind(T viewDelegate) {
        this.viewDelegate = viewDelegate;
    }

    public static String DES(String data, String key) {
        byte[] bt = encryptByKey(data.getBytes(), key);

        return EncodeUtils.base64Encode2String(bt);
    }

    /**
     * 加密
     *
     * @param datasource byte[]
     * @param key        String
     * @return byte[]
     */
    private static byte[] encryptByKey(byte[] datasource, String key) {
        try {
            SecureRandom random = new SecureRandom();

            DESKeySpec desKey = new DESKeySpec(key.getBytes());
            //创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            //Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
            //用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            //现在，获取数据并加密
            //正式执行加密操作
            return cipher.doFinal(datasource);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加订阅
     *
     * @param disposable
     */
    public void addRequest(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    /**
     * 取消订阅
     */
    public void cancelpost() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    public boolean isMissToken(int status) {
        //需要重新登录的 错误码
        return status == 4444;
    }

    public void loginAgain(FragmentActivity activity) {
        //重新登录
        Intent intent = new Intent(activity, BaseApp.getInstance().getLoginActivityClass());
        ActivityUtils.finishAllActivities();
        activity.startActivity(intent);
        activity.finish();
    }

    public static int errorNetNum = 0;

    public void showError(Throwable exThrowable, String returnJson, String errorData) {
        // 提示异常信息。
        boolean isErrorChangeNet = true;
        if (exThrowable instanceof NetworkError) {// 网络不好
            //ToastUtil.show("网络不好");
        } else if (exThrowable instanceof TimeoutError) {// 请求超时
            //ToastUtil.show("请求超时");
        } else if (exThrowable instanceof UnKnownHostError) {// 找不到服务器
            //ToastUtil.show("找不到服务器");
        } else if (exThrowable instanceof URLError) {// 找不到服务器
            // ToastUtil.show("找不到服务器");
        } else if (exThrowable instanceof NotFoundCacheError) {
            //ToastUtil.show("没有缓存");
            // 这个异常只会在仅仅查找缓存时没有找到缓存时返回
        } else if (exThrowable instanceof ProtocolException) {
            //ToastUtil.show("系统不支持");
        } else if (exThrowable instanceof ConnectException) {
            //ToastUtil.show("Failed to connect");
        } else if (exThrowable instanceof JSONException) {
            //ToastUtil.show("返回数据格式错误");
            if (!TextUtils.isEmpty(returnJson)) {
                if (returnJson.contains("DOCTYPE")
                        || returnJson.contains("<html>")
                        || returnJson.contains("</div>")
                        || returnJson.contains("Acceptable")) {
                    return;
                }
            }
            isErrorChangeNet = false;
            MobclickAgent.reportError(viewDelegate.getRootView().getContext(),
                    "返回数据格式错误" + "\n" + getExceptionToString(exThrowable) + "\n" +
                            errorData + "\n" + returnJson);
        } else {
            isErrorChangeNet = false;
//            ToastUtil.show("未知错误" + exThrowable.getMessage());
            MobclickAgent.reportError(viewDelegate.getRootView().getContext(),
                    "未知错误" + exThrowable.getClass() + "\n" +
                            getExceptionToString(exThrowable) + "\n" + errorData +
                            "\n" + returnJson);
        }
        if (isErrorChangeNet) {
            errorNetNum++;
            if(errorNetNum>2){
                errorNetNum=0;
                BaseApp.getInstance().errorAndChangeNetUrl();
            }
        } else {
            errorNetNum = 0;
        }
    }

    /**
     * 将 Exception 转化为 String
     */
    public String getExceptionToString(Throwable e) {
        if (e == null) {
            return "";
        }
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    public void onDialogBtnClick(FragmentActivity activity, View view, int position, Object item) {
        if (position == ResultDialog.CONFIRM_POSITION) {
            EventBus.getDefault().post(((ResultDialogEntity) item));
        } else if (position == ResultDialog.CANNEL_POSITION) {
            if (((ResultDialogEntity) item).isCancelAndClose()) {
                activity.onBackPressed();
            }
        }
    }

    public void success(FragmentActivity activity,
                        ServiceDataCallback serviceDataCallback,
                        DefaultClickLinsener defaultClickLinsener,
                        int requestCode, String jsonData, String errorData) {
        String info = "";
        int status = -1;
        String data = "";
        KLog.i(this.getClass().getName(), "请求数据: " + jsonData);
        //String httpKey = BaseApp.getInstance().getHttpKey();
        try {
            JSONObject object = new JSONObject(jsonData);
            info = object.getString(ResultDialog.DIALOG_KEY);
            status = object.getInt("code");
            data = object.getString("data");

        } catch (JSONException e) {
            e.printStackTrace();
            showError(e, jsonData, errorData);
            serviceDataCallback.onDataError(e.toString(), "", JsonErrorStatu, requestCode);
            return;
        }
        try {
            if (status == 0) {
                serviceDataCallback.onDataSuccess(data, info, status, requestCode);
            } else {
                serviceDataCallback.onDataError(data, info, status, requestCode);
                if (status != 4444 && status != 1035) {
                    MobclickAgent.reportError(viewDelegate.getRootView().getContext(),
                            "请求错误：" + status + "\n" +
                                    errorData + "\n" + jsonData);
                }
            }
            if (!TextUtils.isEmpty(info)) {
                ResultDialogEntity resultDialogEntity = ResultDialog.getInstence()
                        .ShowResultDialog(activity, info, defaultClickLinsener);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError(e, jsonData, errorData);
            if (e instanceof JSONException) {
                serviceDataCallback.onDataError(e.toString(), info, JsonErrorStatu, requestCode);
            } else {
                serviceDataCallback.onDataError(e.toString(), info, viewErrorStatu, requestCode);
            }
            return;
        }
    }


    protected Map<String, Object> getBaseMap() {
        baseMap = new LinkedHashMap<>();
        return baseMap;
    }

    public Map<String, Object> getBaseMapWithUid() {
        getBaseMap();
        baseMap = getBaseMapWith(baseMap);

        //        String language = SaveUtil.getInstance().getString("language");
        //        if (TextUtils.isEmpty(language)) {
        //            language = "zh-cn";
        //        }
        //        baseMap.put("language", language);
        return baseMap;
    }

    public static Map<String, Object> getBaseMapWith(Map<String, Object> baseMap) {
        String auth = SaveUtil.getInstance().getString("auth");
        baseMap.put("auth", auth);
        baseMap.put("loginTime", System.currentTimeMillis());
        baseMap.put("token", SaveUtil.getInstance().getString("token"));
        return baseMap;
    }


    public void put(String key, Object val) {
        baseMap.put(key, val);
    }

}
