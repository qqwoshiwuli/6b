package com.fivefivelike.mybaselibrary.http;

import android.text.TextUtils;

import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.ObjectUtils;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.SaveUtil;
import com.fivefivelike.mybaselibrary.utils.logger.KLog;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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

public class WebSocketRequest {
    private Disposable mDisposable;
    private WebSocket mWebSocket;
    private String mUrl;
    private String REQUEST_TAG = "TickerWebsocket";
    private ConcurrentHashMap<String, WebSocketCallBack> webSocketCallBacks;

    private String oldSend = "";
    private String uid;
    boolean isOpen = false;

    boolean isSend = false;


    String registerUrl;
    String unregisterUrl;
    Disposable disposable;


    int singTime = 5000;
    long returnPongTime = 0;


    Map<String, String> pingMap;

    private void sendHandler() {
        if (disposable != null) {
            disposable.dispose();
        }
        returnPongTime = System.currentTimeMillis();
        WebSocket webSocket = okWebsocket.getWebSocket();
        pingMap = null;
        if (webSocket != null) {
            pingMap = new HashMap<>();
            pingMap.put("uri", SaveUtil.getInstance().getString("auth"));
            webSocket.send(GsonUtil.getInstance().toJson(pingMap));
            KLog.i(REQUEST_TAG, "send  auth");
        }
        disposable = Observable.interval(singTime, singTime, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onTerminateDetach()
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {

                        if (okWebsocket != null) {
                            WebSocket webSocket = okWebsocket.getWebSocket();
                            if (webSocket != null) {
                                if (pingMap == null) {
                                    pingMap = new HashMap<>();
                                    pingMap.put("uri", "auth");
                                    String s = GsonUtil.getInstance().toJson(pingMap);
                                    webSocket.send(s);
                                    KLog.i(REQUEST_TAG, "send  auth"+s);
                                    if (!TextUtils.isEmpty(sendData)) {
                                        sendData(sendData);
                                    }
                                }
                                webSocket.send("{\"uri\":\"ping\"}");
                                KLog.i(REQUEST_TAG, "send  ping");
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

    public interface WebSocketCallBack {
        void onDataSuccess(String name, String data, String info, int status);

        void onDataError(String name, String data, String info, int status);
    }

    private WebSocketRequest() {
    }

    private static class Helper {
        private static WebSocketRequest webSocketRequest = new WebSocketRequest();
    }

    public static WebSocketRequest getInstance() {
        return Helper.webSocketRequest;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    String sendData;

    public void sendData(String data) {
        if (okWebsocket != null) {
            sendData = data;
            WebSocket webSocket = okWebsocket.getWebSocket();
            if (webSocket != null) {
                KLog.i(REQUEST_TAG, "sendData  " + data);
                webSocket.send(data);
            }
        }
    }

    String hisJson = "";


    public void addCallBack(String clss, WebSocketCallBack webSocketCallBack) {
        if (webSocketCallBacks != null && !webSocketCallBacks.containsKey(clss)) {
            webSocketCallBacks.put(clss, webSocketCallBack);
        }
    }

    public WebSocketCallBack getCallBack(String clss) {
        if (webSocketCallBacks != null && webSocketCallBacks.containsKey(clss)) {
            return webSocketCallBacks.get(clss);
        }
        return null;
    }

    public void remoceCallBack(String clss) {
        if (webSocketCallBacks != null && webSocketCallBacks.containsKey(clss)) {
            if (webSocketCallBacks != null) {
                webSocketCallBacks.remove(clss);
            }
        }
    }


    public void intiWebSocket(String url, String name, WebSocketCallBack webSocketCallBack) {
        webSocketCallBacks = new ConcurrentHashMap<>();
        if (webSocketCallBack != null) {
            webSocketCallBacks.put(name, webSocketCallBack);
        }
        mUrl = url;
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
               // KLog.i(REQUEST_TAG, "success  " + text);
                Map<String, String> data = GsonUtil.getInstance().toMap(text,
                        new TypeReference<Map<String, String>>() {
                        });
                if (ObjectUtils.equals("pong", data.get("uri"))) {
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
                if (!TextUtils.isEmpty(sendData)) {
                    sendData(sendData);
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
//            KLog.i(REQUEST_TAG, "success 接受名称: " + key + "数据: " + msg);
            WebSocketRequest.WebSocketCallBack webSocketRequest =
                    (WebSocketRequest.WebSocketCallBack) webSocketCallBacks.get(key);
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
            WebSocketRequest.WebSocketCallBack webSocketRequest =
                    (WebSocketRequest.WebSocketCallBack) webSocketCallBacks.get(key);
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
