package com.fivefivelike.mybaselibrary.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.fivefivelike.mybaselibrary.dsbridge.DWebView;
import com.tencent.smtt.sdk.WebSettings;


/**
 * Created by 郭青枫 on 2018/4/2 0002.
 */

public class X5Webview extends DWebView {


    Context mContext;

    public X5Webview(Context context) {
        super(context);
        init(context);
    }

    public X5Webview(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }


    public void init(Context context) {
        mContext = context;
        initWebViewSettings();
    }


    private void initWebViewSettings() {
        WebSettings ws = getSettings();
        ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
        ws.setUserAgent(ws.getUserAgentString() + "bradar");
    }

    public void onDestroy() {
        stopLoading();
        clearHistory();
        ViewParent parent = getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(this);
        }
        removeAllViews();
        this.destroy();
    }
}
