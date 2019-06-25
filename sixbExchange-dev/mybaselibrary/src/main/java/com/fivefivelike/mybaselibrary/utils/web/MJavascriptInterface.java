package com.fivefivelike.mybaselibrary.utils.web;

import android.content.Context;

/**
 * Created by 郭青枫 on 2018/3/24 0024.
 */

public abstract class MJavascriptInterface {
    private Context context;
    public MJavascriptInterface(Context context) {
        this.context = context;
    }

    @android.webkit.JavascriptInterface
    public void openImage(String[] urls) {
        getFirstImg(urls);
    }

    protected abstract void getFirstImg(String[] urls);

}
