package com.fivefivelike.mybaselibrary.http;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadQueue;
import com.yanzhenjie.nohttp.download.DownloadRequest;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;

/**
 * Created by 郭青枫 on 2017/7/6.
 */

public class SingleRequest {
    private static SingleRequest singleRequest;
    private static RequestQueue queen;
    private DownloadQueue mDownloadQueue;

    private SingleRequest() {
        queen = NoHttp.newRequestQueue();
        mDownloadQueue = NoHttp.newDownloadQueue();
    }

    public static SingleRequest getInstance() {
        if (singleRequest == null) {
            synchronized (SingleRequest.class) {
                if (singleRequest == null) {
                    singleRequest = new SingleRequest();
                }
            }
        }
        return singleRequest;
    }

    public void addRequest(int requestCode, Request<String> request, OnResponseListener<String> onResponseListener) {
        queen.add(requestCode, request, onResponseListener);
    }

    public void download(int what, DownloadRequest request, DownloadListener listener) {
        mDownloadQueue.add(what, request, listener);
    }

    public void cancelBySign(Object sign) {
        queen.cancelBySign(sign);
    }

    public void cancelAll() {
        queen.cancelAll();
    }
}
