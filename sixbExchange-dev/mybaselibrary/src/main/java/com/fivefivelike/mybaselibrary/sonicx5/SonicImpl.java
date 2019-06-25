package com.fivefivelike.mybaselibrary.sonicx5;

import android.util.Log;

import com.fivefivelike.mybaselibrary.utils.GlobleContext;
import com.just.agentweb.MiddlewareWebClientBase;
import com.tencent.smtt.sdk.WebView;
import com.tencent.sonic.sdk.SonicConfig;
import com.tencent.sonic.sdk.SonicEngine;
import com.tencent.sonic.sdk.SonicSession;
import com.tencent.sonic.sdk.SonicSessionConfig;

/**
 * Created by cenxiaozhong on 2017/12/17.
 */

public class SonicImpl {

    private SonicSession sonicSession;
    private String url;
    private SonicSessionClientImpl sonicSessionClient;

    public SonicSession getSonicSession() {
        return sonicSession;
    }

    public SonicImpl(String url) {
        this.url = url;
    }

    /**
     */
    public void onCreateSession(String userAgent) {
        Log.i("onCreateSession",userAgent);
        SonicSessionConfig.Builder sessionConfigBuilder = new SonicSessionConfig.Builder();
        sessionConfigBuilder.setSupportLocalServer(true);
        SonicEngine.createInstance(new DefaultSonicRuntimeImpl(GlobleContext.getInstance().getApplicationContext(),userAgent), new SonicConfig.Builder().build());
        // create sonic session and run sonic flow

        sonicSession = SonicEngine.getInstance().createSession(url, sessionConfigBuilder.build());
        if (null != sonicSession) {
            sonicSession.bindClient(sonicSessionClient = new SonicSessionClientImpl());
        } else {
            // throw new UnknownError("create session fail!");
            // Toast.makeText(GlobleContext.getInstance().getApplicationContext(), "create sonic session fail!", Toast.LENGTH_LONG).show();
        }
    }


    public SonicSessionClientImpl getSonicSessionClient() {
        return this.sonicSessionClient;
    }

    /**
     * 不使用中间件，使用普通的 WebViewClient 也是可以的。
     *
     * @return MiddlewareWebClientBase
     */
    public MiddlewareWebClientBase createSonicClientMiddleWare() {
        return new SonicWebViewClient(sonicSession);
    }

    public void bindAgentWeb(WebView agentWeb) {
        if (sonicSessionClient != null) {
            sonicSessionClient.bindWebView(agentWeb);
            sonicSessionClient.clientReady();
        } else { // default mode
            agentWeb.loadUrl(url);
        }
    }

    public void destrory() {
        if (sonicSession != null) {
            sonicSession.destroy();
        }
    }


}
