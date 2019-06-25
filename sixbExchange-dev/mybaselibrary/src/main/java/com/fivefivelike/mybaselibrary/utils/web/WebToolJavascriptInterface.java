package com.fivefivelike.mybaselibrary.utils.web;

import android.webkit.JavascriptInterface;

import com.fivefivelike.mybaselibrary.dsbridge.CompletionHandler;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;

/**
 * Created by 郭青枫 on 2018/3/24 0024.
 */

public abstract class WebToolJavascriptInterface {

    public WebToolJavascriptInterface() {
    }

    @JavascriptInterface
    public void WebToLocal(Object msg, CompletionHandler<String> handler){
        webToLocal(GsonUtil.getInstance().toJson(msg));
        handler.complete(msg+" [ asyn call]");
    }

    protected abstract void webToLocal(String data);

}
