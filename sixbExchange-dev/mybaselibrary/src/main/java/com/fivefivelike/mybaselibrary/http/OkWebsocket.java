package com.fivefivelike.mybaselibrary.http;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * Created by 郭青枫 on 2018/8/22 0022.
 */

public abstract class OkWebsocket extends WebSocketListener {

    private static OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .sslSocketFactory(createSSLSocketFactory())
            .hostnameVerifier(new TrustAllHostnameVerifier())
            .build();
    public static OkHttpClient getClient() {
        return client;
    }

    private static ScheduledExecutorService single = Executors.newSingleThreadScheduledExecutor();
    WebSocket webSocket;

    public WebSocket getWebSocket() {
        return webSocket;
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        this.webSocket = webSocket;
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        single.shutdown();
        this.webSocket = webSocket;
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        this.webSocket = webSocket;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        this.webSocket = webSocket;
    }

    @Override
    public void onFailure(WebSocket w, Throwable t, Response response) {
        this.webSocket = w;
        single.schedule(
                new Runnable() {
                    @Override
                    public void run() {
                        client.newWebSocket(webSocket.request(), OkWebsocket.this);
                        loadHistory();
                    }
                }
                , 5, TimeUnit.SECONDS
        );
    }

    abstract void loadHistory();


    /**
     * 开始websocket连接
     */
    public void start(String url) {
        Request req = new Request.Builder().url(url).build();
        client.newWebSocket(req, this);
        loadHistory();
    }

    public void close() {
        webSocket.cancel();
    }
    OkHttpClient.Builder mBuilder;
    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null,  new TrustManager[] { new TrustAllCerts() }, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }


    private static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];}
    }


    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
