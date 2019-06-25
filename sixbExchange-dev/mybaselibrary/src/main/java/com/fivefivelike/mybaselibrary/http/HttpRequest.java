package com.fivefivelike.mybaselibrary.http;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fivefivelike.mybaselibrary.base.BaseApp;
import com.fivefivelike.mybaselibrary.utils.GlobleContext;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.SaveUtil;
import com.fivefivelike.mybaselibrary.utils.logger.KLog;
import com.fivefivelike.mybaselibrary.view.dialog.NetWorkDialog;
import com.yanzhenjie.nohttp.Binary;
import com.yanzhenjie.nohttp.BitmapBinary;
import com.yanzhenjie.nohttp.FileBinary;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.OnUploadListener;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;


/**
 * Created by 郭青枫 on 2017/7/6.
 */

public class HttpRequest {

    public static final int uploadWhat = 0x123;
    private String mRequestUrl;//链接
    private RequestCallback mRequestCallback;//回调
    private String mRequestName;//接口名
    private Object mRequestObj;//传参
    private String mFirstCharForGetRequest = "?";
    private boolean mIsShowDialog;//是否显示加载dialog
    public boolean mIsLunban = false;//是否压缩图片
    private String mEncoding;
    public int mConnectTimeOut;//链接超时时间
    public int mReadTimeOut;
    private int mRequestCode;//回调 码
    private NetWorkDialog mDialog;//加载 dialog
    private CacheMode mCacheMode;//缓存模式
    private Map<String, Object> mFileMap;//多key 文件集合
    private List<File> mFileList;//单 key 文件集合
    private Object mRequestTag;
    private RequestMode mRequestMode;//请求方式 get post
    private ParameterMode mParameterMode;//参数 方式 key-value json
    private String REQUEST_TAG = "request";
    private String RESPONSE_TAG = "response";
    private OnUploadListener mOnUploadListener;
    Request<String> mRequest = null;
    StringBuffer errorData;//接口信息 用于打印报错

    // -------------------------------------------构造函数--------------------------------------------------------


    private HttpRequest(Builder builder) {
        this.mRequestUrl = builder.requestUrl;
        this.mRequestCallback = builder.requestCallback;
        this.mRequestName = builder.requestName;
        this.mRequestObj = builder.requestObj;
        this.mConnectTimeOut = builder.connectTimeOut;
        this.mEncoding = builder.enCoding;
        this.mRequestCode = builder.requestCode;
        this.mDialog = builder.dialog;
        this.mIsShowDialog = builder.isShowDialog;
        this.mCacheMode = builder.cacheMode;
        this.mReadTimeOut = builder.readTimeOut;
        this.mFileMap = builder.fileMap;
        this.mFileList = builder.fileList;
        this.mRequestMode = builder.requestMode;
        this.mParameterMode = builder.parameterMode;
        this.mOnUploadListener = builder.onUploadListener;
        this.mRequestTag = builder.requestTag == null ? "abctag" : mRequestTag;
    }
    // -------------------------------------------------公开调用方法------------------------------------------

    /**
     * 普通请求
     */
    public void sendRequest() {
        request();
    }

    /**
     * rx封装的请求
     *
     * @return Disposable
     */
    public Disposable RxSendRequest() {
        rxRequest();
        return sendRxRequest();
    }

    // ------------------------------------------------请求操作---------------------------------------------

    /**
     * 普通的请求操作
     */
    private void request() {
        if (TextUtils.isEmpty(mRequestUrl)) {
            KLog.i(REQUEST_TAG, mRequestName + "请求 Url为空");
            return;
        }
        requestSet();
        //使用单例请求
        if (mRequest != null) {
            SingleRequest.getInstance().addRequest(mRequestCode, mRequest, onResponseListener);
        }
    }


    /**
     * 适配RxJava的请求
     *
     * @return
     */
    private void rxRequest() {
        if (TextUtils.isEmpty(mRequestUrl)) {
            KLog.i(REQUEST_TAG, mRequestName + "请求 Url为空");
            return;
        }
        requestSet();
    }

    /**
     * 请求设置
     */
    private void requestSet() {
        showDialog();
        errorData = new StringBuffer();
        if (mRequestMode == RequestMode.POST) {
            mRequest = NoHttp.createStringRequest(mRequestUrl, RequestMethod.POST);
        } else if (mRequestMode == RequestMode.GET) {
            mRequest = NoHttp.createStringRequest(mRequestUrl, RequestMethod.GET);
        }
        if (mRequestObj != null && mParameterMode != ParameterMode.Rest) {
            logRequestUrlAndParams();
        }
        KLog.i(REQUEST_TAG, "请求名称: " + mRequestName + "请求Url: " + mRequestUrl.toString());
        errorData.append("请求名称: " + mRequestName + "请求Url: " + mRequestUrl.toString()).append("\n");
        mRequest.setTag(mRequestTag);
        mRequest.setConnectTimeout(mConnectTimeOut);
        mRequest.setReadTimeout(mReadTimeOut);
        mRequest.setCacheMode(mCacheMode);
        mRequest.setParamsEncoding(mEncoding);

        Headers headers = mRequest.getHeaders();
        for (String in : headers.keySet()) {
            //map.keySet()返回的是所有key的值
            List<String> values = headers.getValues(in);//得到每个key多对用value的值
            for (int i = 0; i < values.size(); i++) {
                KLog.i(REQUEST_TAG, "请求Header: " + in + "-参数: " + values.get(i));
                errorData.append("请求Header: " + in + "-参数: " + values.get(i)).append("\n");
            }
        }


        if (mIsLunban) {
            //图片压缩

        } else {
            requestFilesSet();
        }
    }


    private void requestFilesSet() {
        if (mFileMap != null && mFileMap.size() > 0) {
            addFileMap();
        }
        if (mFileList != null && mFileList.size() > 0) {
            addFileList();
        }
    }

    /**
     * rest方式 地址
     */
    private void setRestUrl() {
        if (mRequestObj != null && mParameterMode == ParameterMode.Rest) {
            Map<String, Object> map = JSON.parseObject(JSON.toJSONString(mRequestObj), new TypeReference<Map<String, Object>>() {
            });
            StringBuilder sb = new StringBuilder(mRequestUrl);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey().toString().trim();
                String value = entry.getValue().toString().trim();
                KLog.i(REQUEST_TAG, "提交参数: " + key + " = " + value);
                if (!TextUtils.isEmpty(value)) {
                    if ("uid".equals(key)) {

                    } else if ("appVersion".equals(key)) {
                    } else if ("token".equals(key)) {
                    } else if ("auth".equals(key)) {

                    } else if ("userTempId".equals(key)) {

                    } else {
                        sb.append("/" + value);
                    }
                }
            }
            mRequestUrl = sb.toString();
            KLog.i(REQUEST_TAG, "全地址: " + mRequestName + "请求全Url: " + sb.toString());
        }
    }

    /**
     * 打印文件 多文件上传
     */
    private void addFileMap() {
        int i = 0;
        if (mFileMap == null) {
            return;
        }
        for (Map.Entry<String, Object> entry : mFileMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof File) {
                FileBinary fileBinary = new FileBinary((File) value);
                mRequest.add(key, fileBinary);//以文件的形式上传
                if (mOnUploadListener != null) {
                    fileBinary.setUploadListener(uploadWhat + i, mOnUploadListener);
                }
                KLog.i(REQUEST_TAG, "上传文件" + key + "     " + ((File) value).getPath());
            } else if (value instanceof Bitmap) {//以bitmap的形式上传
                BitmapBinary bitmapBinary = new BitmapBinary((Bitmap) value, key);
                mRequest.add(key, bitmapBinary);
                if (mOnUploadListener != null) {
                    bitmapBinary.setUploadListener(uploadWhat + i, mOnUploadListener);
                }
            }
            i++;
        }
    }

    /**
     * 打印文件  多文件公用一个key
     */
    private void addFileList() {
        List<Binary> binaries = new ArrayList<>();
        for (int i = 0; i < mFileList.size(); i++) {
            Object value = mFileList.get(i);
            if (value instanceof File) {
                FileBinary fileBinary = new FileBinary((File) value);
                if (mOnUploadListener != null) {
                    fileBinary.setUploadListener(uploadWhat + i, mOnUploadListener);
                }
                binaries.add(fileBinary);
                KLog.i(REQUEST_TAG, "上传文件" + "file" + "     " + ((File) value).getPath());
            } else if (value instanceof Bitmap) {//以bitmap的形式上传
                BitmapBinary bitmapBinary = new BitmapBinary((Bitmap) value, "file" + i);
                if (mOnUploadListener != null) {
                    bitmapBinary.setUploadListener(uploadWhat + i, mOnUploadListener);
                }
                binaries.add(bitmapBinary);
            }
        }
        mRequest.add("files", binaries);//以文件的形式上传
    }

    /**
     * 打印参数和链接  添加参数
     */
    private void logRequestUrlAndParams() {
        Map<String, Object> map = JSON.parseObject(JSON.toJSONString(mRequestObj), new TypeReference<Map<String, Object>>() {
        });
        StringBuilder sb = new StringBuilder(mRequestUrl);
        if (mRequestUrl.contains("?")) {
            mFirstCharForGetRequest = "&";
        }
        sb.append(mFirstCharForGetRequest);
        if (map.containsKey("token")) {
            if (mParameterMode != ParameterMode.Rest) {
                mRequest.addHeader("token", map.get("token").toString());
            }
        }
        if (map.containsKey("loginTime")) {
            if (mParameterMode != ParameterMode.Rest) {
                mRequest.addHeader("loginTime", map.get("loginTime").toString());
            }
        }
        if (map.containsKey("auth")) {
            if (mParameterMode != ParameterMode.Rest) {
                mRequest.addHeader("auth", map.get("auth").toString());
            }
        }
        if (map.containsKey("apiKey")) {
            if (mParameterMode != ParameterMode.Rest) {
                mRequest.addHeader("apiKey", map.get("apiKey").toString());
            }
        }
        if (map.containsKey("apiSecret")) {
            if (mParameterMode != ParameterMode.Rest) {
                mRequest.addHeader("apiSecret", map.get("apiSecret").toString());
            }
        }
        KLog.i(REQUEST_TAG, "请求方式: " + (mRequest.getRequestMethod() == com.yanzhenjie.nohttp.RequestMethod.POST ? "post" : "get"));
        if (mParameterMode == ParameterMode.KeyValue) {
            StringBuffer stringBuffe = new StringBuffer();
            stringBuffe.append("apiKey=" + map.get("apiKey") + "&");
            stringBuffe.append("apiSecret=" + map.get("apiSecret") + "&");
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey().toString().trim();
                String value = entry.getValue().toString().trim();
                KLog.i(REQUEST_TAG, "提交参数: " + key + " = " + value);
                errorData.append("提交参数: " + key + " = " + value).append("\n");
                if (!key.equals("loginTime") && !key.equals("token") &&
                        !key.equals("apiKey") && !key.equals("apiSecret") &&
                        !key.equals("language") && !key.equals("unit")&& !key.equals("auth")) {
                    sb.append(key + "=" + value);
                    sb.append("&");
                    if (null != value && !"null".equals(value)) {
                        mRequest.add(key, value);
                    }
                }
            }
            stringBuffe.append("params=" + GsonUtil.getInstance().toJson(mRequest.getParamKeyValues()));
            //mRequest.addHeader("sign", EncryptUtils.encryptMD5ToString(stringBuffe.toString()));
        } else if (mRequest.getRequestMethod() == RequestMethod.GET && mParameterMode == ParameterMode.Rest) {
            setRestUrl();
        } else if (mRequest.getRequestMethod() == RequestMethod.POST && mParameterMode == ParameterMode.Json) {
            StringBuffer stringBuffe = new StringBuffer();
            stringBuffe.append("apiKey=" + map.get("apiKey") + "&");
            stringBuffe.append("apiSecret=" + map.get("apiSecret") + "&");
            map.remove("token");
            map.remove("loginTime");
            map.remove("apiKey");
            map.remove("auth");
            map.remove("apiSecret");
            String json = GsonUtil.getInstance().toJson(map);
            stringBuffe.append("params=" + json);
            try {
                //将字符串转换成jsonObject对象
                JSONObject myJsonObject = new JSONObject(json);
                KLog.i(REQUEST_TAG, "Json请求: " + json);
                errorData.append("Json请求: " + json).append("\n");
                mRequest.setDefineRequestBodyForJson(myJsonObject);
                //mRequest.addHeader("sign", EncryptUtils.encryptMD5ToString(stringBuffe.toString()));
            } catch (JSONException e) {
                KLog.e(REQUEST_TAG, "Json请求失败 json转换出错: " + json);
            }
        }

        if (mRequest.getRequestMethod() == RequestMethod.POST) {
            mRequest.setMultipartFormEnable(true);
        }

        //        if (map.containsKey("uid")) {
        //            if (mParameterMode != ParameterMode.Rest) {
        //                mRequest.addHeader("uid", map.get("uid").toString());
        //            }
        //        }


        sb.deleteCharAt(sb.length() - 1);
        KLog.i(REQUEST_TAG, "全地址: " + mRequestName + "请求全Url: " + sb.toString());
    }

    /**
     * 获得Disposable对象
     *
     * @return
     */
    private Disposable sendRxRequest() {
        return Observable.create(new ObservableOnSubscribe<Response<String>>() {
            @Override
            public void subscribe(ObservableEmitter<Response<String>> e) throws Exception {
                //同步请求
                if (!e.isDisposed()) {
                    try {
                        if (mRequest != null) {
                            Response<String> response = NoHttp.startRequestSync(mRequest);
                            if (response.isSucceed()) {
                                if (!e.isDisposed()) {
                                    e.onNext(response);
                                }
                            } else {
                                if (!e.isDisposed()) {
                                    e.onError(response.getException());//onError 之后不可可继下游可继续接受 onNext
                                }
                            }
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        e.onError(e1);
                    }
                    e.onComplete();//onComplete 之后可继下游可继续接受 onNext
                }
                //onComplete和onError必须唯一并且互斥, 即不能发多个onComplete, 也不能发多个onError,
                // 也不能先发一个onComplete, 然后再发一个onError, 反之亦然
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //执行了反注册unsubscribes或者发送数据序列中断了，解除上游生产者与下游订阅者之间的引用。
                .onTerminateDetach()
                .subscribe(new Consumer<Response<String>>() {//onNext()
                    @Override
                    public void accept(@NonNull Response<String> stringResponse) throws Exception {
                        //调用成功 返回json
                        success(stringResponse);
                    }
                }, new Consumer<Throwable>() {//onError()
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        //调用失败
                        dismissDialog();
                        throwable.printStackTrace();
                        if (mRequestCallback != null) {
                            mRequestCallback.error(mRequestCode, throwable, errorData.toString());
                        }
                    }
                }, new Action() {//onCompleted()
                    @Override
                    public void run() throws Exception {
                        dismissDialog();
                    }
                });
    }

    //图片压缩
    public static Disposable imgLuban(List<String> paths, final ImgLunban lunban) {
        return Flowable.just(paths)
                .observeOn(Schedulers.io())
                .map(new Function<List<String>, List<File>>() {
                    @Override
                    public List<File> apply(@NonNull List<String> list) throws Exception {
                        // 同步方法直接返回压缩后的文件
                        return Luban.with(GlobleContext.getInstance().getApplicationContext()).load(list).get();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<File>>() {//onNext()
                    @Override
                    public void accept(@NonNull List<File> stringResponse) throws Exception {
                        //调用成功 返回json
                        if (lunban != null) {
                            lunban.success(stringResponse);

                        }

                    }
                }, new Consumer<Throwable>() {//onError()
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        //调用失败
                        throwable.printStackTrace();
                        if (lunban != null) {
                            lunban.failed();
                        }
                    }
                }, new Action() {//onCompleted()
                    @Override
                    public void run() throws Exception {

                    }
                });
    }

    /**
     * 请求成功后的操作
     */
    private void success(Response<String> response) {
        Headers headers = response.getHeaders();
        if (headers.containsKey("token")) {
            KLog.json("token_success", headers.getValues("token").get(0));
            SaveUtil.getInstance().saveString("token", headers.getValues("token").get(0) + "");
        }
        if (headers.containsKey("userTempId")) {
            KLog.json("userTempId_success", headers.getValues("userTempId").get(0));
            SaveUtil.getInstance().saveString("UserLoginInfo_no_login_id", headers.getValues("userTempId").get(0) + "");
        }
        BaseApp.getInstance().returnHeader(headers);
        String json = response.get();
        KLog.i(RESPONSE_TAG, mRequestName);
        KLog.i(RESPONSE_TAG, "请求数据时间: " + response.getNetworkMillis());
        KLog.json(RESPONSE_TAG, json);
        errorData.append("返回header" + GsonUtil.getInstance().toJson(headers)).append("\n");
        errorData.append("返回结果：" + json).append("\n");

        if (mRequestCallback != null) {
            mRequestCallback.success(mRequestCode, json, errorData.toString());
        }
    }
    // --------------------------------------------回调操作------------------------------------------------

    /**
     * 回调监听类 onStart:开始请求回调 onFailure:请求失败回调 onSuccess:请求成功回调 onLoading:请求中回调
     */
    private OnResponseListener<String> onResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {
        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            success(response);
        }

        @Override
        public void onFailed(int what, Response<String> response) {
            Exception exception = response.getException();
            if (mRequestCallback != null) {
                mRequestCallback.error(what, exception, errorData.toString());
            }
//            AppConst.app2BaseUrl = "https://www.6b.top/api";
            KLog.i("错误：" + exception.getMessage());
        }

        @Override
        public void onFinish(int what) {
            dismissDialog();
        }
    };

    private void showDialog() {
        if (mDialog != null) {
            mDialog.showDialog(mIsShowDialog);
        }
    }

    private void dismissDialog() {
        if (mDialog != null) {
            mDialog.dimessDialog(mIsShowDialog);
        }
    }

    public static class Builder {
        private String requestUrl = "";
        private RequestCallback requestCallback;// 回调接口
        private String requestName = "http请求描述";// 请求描述
        private Object requestObj = null;
        private boolean isShowDialog;
        private String enCoding = "UTF-8";
        private Object requestTag;
        private CacheMode cacheMode = CacheMode.ONLY_REQUEST_NETWORK;
        private int requestCode = -1;
        private NetWorkDialog dialog;
        public int connectTimeOut = 10 * 1000;
        public int readTimeOut = 20 * 1000;
        private Map<String, Object> fileMap;
        private List<File> fileList;
        public RequestMode requestMode = RequestMode.POST;
        public ParameterMode parameterMode = ParameterMode.Json;
        private OnUploadListener onUploadListener;


        public Builder() {
        }


        /**
         * post 传参方式
         *
         * @param
         * @return
         */
        public Builder setOnUploadListener(OnUploadListener onUploadListener) {
            this.onUploadListener = onUploadListener;
            return this;
        }


        //设置传参模式
        public Builder setParameterMode(ParameterMode parameterMode) {
            this.parameterMode = parameterMode;
            return this;
        }

        public Builder setFileList(List<File> fileList) {
            this.fileList = fileList;
            return this;
        }

        /**
         * @param requestUrl 请求地址
         * @return
         */
        public Builder setRequestUrl(String requestUrl) {
            this.requestUrl = requestUrl;
            return this;
        }

        /**
         * @param requestCallback 请求回调
         * @return
         */
        public Builder setRequestCallback(RequestCallback requestCallback) {
            this.requestCallback = requestCallback;
            return this;
        }

        /**
         * @param requestName 请求名字,在日志中显示
         * @return
         */
        public Builder setRequestName(String requestName) {
            this.requestName = requestName;
            return this;
        }

        /**
         * @param requestObj 请求参数,可以使一个类,也可以是map集合
         * @return
         */
        public Builder setRequestObj(Object requestObj) {
            this.requestObj = requestObj;
            return this;
        }

        /**
         * @param showDialog 是否显示弹出框
         * @return
         */
        public Builder setShowDialog(boolean showDialog) {
            isShowDialog = showDialog;
            return this;
        }

        /**
         * @param enCoding 编码
         * @return
         */
        public Builder setEnCoding(String enCoding) {
            this.enCoding = enCoding;
            return this;
        }

        /**
         * @param requestTag 请求标识
         * @return
         */
        public Builder setRequestTag(Object requestTag) {
            this.requestTag = requestTag;
            return this;
        }

        /**
         * @param cacheMode 缓存模式
         * @return
         */
        public Builder setCacheMode(CacheMode cacheMode) {
            this.cacheMode = cacheMode;
            return this;
        }

        /**
         * @param requestCode 请求id
         * @return
         */
        public Builder setRequestCode(int requestCode) {
            this.requestCode = requestCode;
            return this;
        }

        /**
         * @param dialog 请求弹出框
         * @return
         */
        public Builder setDialog(NetWorkDialog dialog) {
            this.dialog = dialog;
            return this;
        }

        /**
         * @param connectTimeOut 请求超时时间
         * @return
         */
        public Builder setConnectTimeOut(int connectTimeOut) {
            this.connectTimeOut = connectTimeOut;
            return this;
        }

        /**
         * @param readTimeOut 响应超时时间
         * @return
         */
        public Builder setReadTimeOut(int readTimeOut) {
            this.readTimeOut = readTimeOut;
            return this;
        }

        /**
         * @param fileMap 上传的文件集合
         * @return
         */
        public Builder setFileMap(Map<String, Object> fileMap) {
            this.fileMap = fileMap;
            return this;
        }

        //设置请求模式
        public Builder setRequestMode(RequestMode requestMode) {
            this.requestMode = requestMode;
            return this;
        }

        /**
         * @return 返回一个请求对象
         */
        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }

    public interface ImgLunban {
        void success(List<File> files);

        void failed();
    }

    public static class HttpEntity {
        int requestCode;
        String data;
        String errorData;

        public String getErrorData() {
            return errorData;
        }

        public void setErrorData(String errorData) {
            this.errorData = errorData;
        }

        public int getRequestCode() {
            return requestCode;
        }

        public void setRequestCode(int requestCode) {
            this.requestCode = requestCode;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    public enum RequestMode {
        /**
         * post请求
         */
        POST,
        /**
         * get请求
         */
        GET
    }

    public enum ParameterMode {
        /**
         * post请求
         */
        Json,
        /**
         * get post请求
         */
        KeyValue,
        /**
         * get请求
         */
        Rest
    }
}
