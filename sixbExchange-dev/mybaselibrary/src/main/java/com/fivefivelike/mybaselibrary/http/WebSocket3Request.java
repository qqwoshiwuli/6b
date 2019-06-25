package com.fivefivelike.mybaselibrary.http;

import android.text.TextUtils;

import com.blankj.utilcode.util.ObjectUtils;
import com.fivefivelike.mybaselibrary.entity.WSBean;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.SaveUtil;
import com.fivefivelike.mybaselibrary.utils.logger.KLog;
import com.yanzhenjie.nohttp.rest.CacheMode;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;
import okhttp3.WebSocket;

/**
 * Created by 郭青枫 on 2018/1/8 0008.
 */

public class WebSocket3Request {
    private Disposable mDisposable;
    private WebSocket mWebSocket;
    private String mUrl;
    private String REQUEST_TAG = "TickerWebsocket";
    private ConcurrentHashMap<String, WebSocket3CallBack> webSocketCallBacks;

    //ws://120.27.233.114:1911/orderws/order?apiKey=rWJ8PSS/dZQMwYtpa+Fwek/kBd+Ou7PgIzIeybNDQXU=
    private String oldSend = "";
    private String uid;
    boolean isOpen = false;

    boolean isSend = false;


    String registerUrl;
    String unregisterUrl;

    int singTime = 5000;
    long returnPongTime = 0;

    Disposable disposable;

    private void sendHandler() {
        if (disposable != null) {
            disposable.dispose();
        }
        returnPongTime=System.currentTimeMillis();
        disposable = Observable.interval(singTime, singTime, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onTerminateDetach()
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (returnPongTime == 0 || returnPongTime + singTime > System.currentTimeMillis()) {
                            if (okWebsocket != null) {
                                WebSocket webSocket = okWebsocket.getWebSocket();
                                if (webSocket != null) {
                                    webSocket.send("{\"apiKey\":\"" +
                                            SaveUtil.getInstance().getString("apiKey")
                                            + "\",\"order\":\"ping\"}");
                                } else {
                                    startSocket();
                                }
                            } else {
                                startSocket();
                            }
                        } else {
                            startSocket();
                        }
                    }
                });

    }

    public boolean isSend() {
        return isSend;
    }

    public void setRegisterUrl(String registerUrl) {
        this.registerUrl = registerUrl;
    }

    public void setUnregisterUrl(String unregisterUrl) {
        this.unregisterUrl = unregisterUrl;
    }

    public interface WebSocket3CallBack {
        void onDataSuccess(String name, String data, String info, int status);

        void onDataError(String name, String data, String info, int status);
    }

    private WebSocket3Request() {
    }

    private static class Helper {
        private static WebSocket3Request webSocketRequest = new WebSocket3Request();
    }

    public static WebSocket3Request getInstance() {
        return Helper.webSocketRequest;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void sendData(List<String> keys) {
        if (keys != null) {
            StringBuffer stringBuffer = new StringBuffer("");
            for (int i = 0; i < keys.size(); i++) {
                stringBuffer.append(",").append(keys.get(i));
            }
            //unregister(oldSend);
            if (disposable != null) {
                disposable.dispose();
            }
            oldSend = stringBuffer.toString();
            register(oldSend);
        }
    }

    public void sendSubData(String type, String set) {
        //cmd sub cannal
        //type key
        //set json订阅内容
        WSBean wsBean = new WSBean();
        wsBean.setCmd("sub");
        wsBean.setType(type);
        wsBean.setSet(set);
        if (okWebsocket != null) {
            WebSocket webSocket = okWebsocket.getWebSocket();
            if (webSocket != null) {
                webSocket.send(GsonUtil.getInstance().toJson(wsBean));
            }
        }
    }

    public void sendCannalData(String type) {
        WSBean wsBean = new WSBean();
        wsBean.setCmd("cannal");
        wsBean.setType(type);
        if (okWebsocket != null && isOpen) {
            WebSocket webSocket = okWebsocket.getWebSocket();
            if (webSocket != null) {
                webSocket.send(GsonUtil.getInstance().toJson(wsBean));
            }
        }
    }

    String hisJson = "";

    private void register(String json) {
        LinkedHashMap baseMap = new LinkedHashMap<>();
        baseMap.put("uid", uid);
        baseMap.put("keys", "" + json + "");
        KLog.i(REQUEST_TAG, "register  " + json);
        hisJson = json;
        isSend = true;
        disposable = new HttpRequest.Builder()
                .setRequestCode(0x123)
                .setRequestUrl(registerUrl)
                .setShowDialog(false)
                .setRequestName("注册web")
                .setCacheMode(CacheMode.ONLY_REQUEST_NETWORK)
                .setRequestMode(HttpRequest.RequestMode.POST)
                .setParameterMode(HttpRequest.ParameterMode.Json)
                .setRequestObj(baseMap)
                .setRequestCallback(new RequestCallback() {
                    @Override
                    public void success(int requestCode, String data, String errorData) {
                        KLog.i(REQUEST_TAG, "success  " + "register");
                        isSend = false;
                    }

                    @Override
                    public void error(int requestCode, Throwable exThrowable, String errorData) {

                    }


                })
                .build()
                .RxSendRequest();
    }


    public void addCallBack(String clss, WebSocket3CallBack webSocketCallBack) {
        if (webSocketCallBacks != null && !webSocketCallBacks.containsKey(clss)) {
            webSocketCallBacks.put(clss, webSocketCallBack);
        }
    }

    public void remoceCallBack(String clss) {
        if (webSocketCallBacks != null && webSocketCallBacks.containsKey(clss)) {
            if (webSocketCallBacks != null) {
                webSocketCallBacks.remove(clss);
            }
        }
    }


    public void intiWebSocket(String url, String name, WebSocket3CallBack webSocketCallBack) {
        webSocketCallBacks = new ConcurrentHashMap<>();
        if (webSocketCallBack != null) {
            webSocketCallBacks.put(name, webSocketCallBack);
        }
        mUrl = url;
        KLog.i(REQUEST_TAG, "mUrl  " + mUrl);
        startSocket();

    }

    OkWebsocket okWebsocket;

    private void startSocket() {
        isOpen = false;
        okWebsocket = new OkWebsocket() {
            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                isOpen = true;
                KLog.i(REQUEST_TAG, "success  " + text);
                if (ObjectUtils.equals("pong", text)) {
                    returnPongTime = System.currentTimeMillis();
                } else {
                    serviceSuccess(text);
                }
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                t.printStackTrace();
                isOpen = false;
            }

            @Override
            void loadHistory() {
                if (!TextUtils.isEmpty(hisJson)) {
                    register(hisJson);
                }
            }
        };
        okWebsocket.start(mUrl);
        returnPongTime = 0;
        sendHandler();

    }


    private void serviceError(Throwable ex) {
        //websocket链接失败
        //KLog.i(REQUEST_TAG, "error  " + ex.getMessage());
        error(ex.getMessage());
    }

    private void serviceSuccess(String msg) {
        //服务器获取成功
        //KLog.i(REQUEST_TAG, "success  " + msg);
        success(msg);
    }

    private void success(String msg) {
        //服务器数据 成功
        Iterator iter = webSocketCallBacks.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            //KLog.i(REQUEST_TAG, "success 接受名称: " + key + "数据: " + msg);
            WebSocket3CallBack webSocketRequest =
                    (WebSocket3CallBack) webSocketCallBacks.get(key);
            webSocketRequest.onDataSuccess(key, msg,
                    GsonUtil.getInstance().getValue(msg, "type"), 0);
        }
    }

    private void error(String msg) {
        //服务器数据 失败
        //KLog.json(RESPONSE_TAG, msg);
        Iterator iter = webSocketCallBacks.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            //KLog.i(REQUEST_TAG, "error 接受名称: " + key + "数据: " + msg);
            WebSocket3CallBack webSocketRequest =
                    (WebSocket3CallBack) webSocketCallBacks.get(key);
            webSocketRequest.onDataError(key, msg, msg, 0);
        }
    }


    public void onDestory() {
        if (okWebsocket != null) {
            try {
                if (disposable != null) {
                    disposable.dispose();
                }
                okWebsocket.close();
            } catch (Exception e) {
                e.printStackTrace();
                KLog.i(REQUEST_TAG, "closeBlocking error: ");
            } finally {
                okWebsocket = null;
            }
        }
    }

}
