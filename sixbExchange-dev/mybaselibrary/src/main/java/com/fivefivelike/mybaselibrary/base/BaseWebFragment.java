package com.fivefivelike.mybaselibrary.base;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.fivefivelike.mybaselibrary.sonic.SonicImpl;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.MiddlewareWebClientBase;
import com.tencent.sonic.sdk.SonicSession;

/**
 * Created by 郭青枫 on 2018/3/4 0004.
 */

public class BaseWebFragment extends BaseFragment<BaseWebViewDelegate> {
    private SonicImpl mSonicImpl;
    private String url = "http://47.96.180.179:1904/gameTeam/showWebViewIndex";
    protected AgentWeb mAgentWeb;
    BaseWebLinsener webLinsener;
    BridgeWebView mBridgeWebView;
    //AVLoadingIndicatorView indicatorView;
    boolean isLoad = true;
    boolean isLoadBridge = true;
    boolean isBacking = false;

    int loadIndex = 0;
    private SonicSession sonicSession;
    long timeIndex;

    public void setWebLinsener(BaseWebLinsener webLinsener) {
        this.webLinsener = webLinsener;
    }


    public void loadUrl(String url) {
        this.url = url;
        mAgentWeb.getWebCreator().getWebView().loadUrl(url);
        isLoad = true;
        //indicatorView.show();
    }

    public boolean goBack() {
        isBacking = true;
        return mAgentWeb.back();
    }

    MiddlewareWebClientBase middlewareWebClient;
    private Handler handler = new Handler() {//进行延时跳转
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (msg.obj.equals(url + timeIndex) && !isBacking) {
                        if (!isLoadBridge && loadIndex < 5) {
                            loadIndex++;
                            mAgentWeb.getWebCreator().getWebView().loadUrl(url);
                        } else {
                            //indicatorView.hide();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        url = getArguments().getString("url", "http://47.96.180.179:1904/gameTeam/showWebViewIndex");
        mBridgeWebView = new BridgeWebView(viewDelegate.viewHolder.root.getContext());//getActivity());
        if (webLinsener != null) {
            webLinsener.onWebStart();
        }
        //indicatorView = new AVLoadingIndicatorView(viewDelegate.viewHolder.root.getContext());//尾部加载中状态
        //indicatorView.setIndicator(new LineSpinFadeLoaderIndicator());
        //indicatorView.setIndicatorColor(CommonUtils.getColor(R.color.color_font4));
        // 1. 首先创建SonicImpl
        mSonicImpl = new SonicImpl(url);
        //        // 2. 调用 onCreateSession
        mSonicImpl.onCreateSession();
        //        //3. 创建AgentWeb ，注意创建AgentWeb的时候应该使用加入SonicWebViewClient中间件
        sonicSession = mSonicImpl.getSonicSession();
        middlewareWebClient = mSonicImpl.createSonicClientMiddleWare();

        BridgeWebViewClient myWebViewClient = new BridgeWebViewClient(mBridgeWebView) {
            @Override
            public void onPageFinished(WebView view, String url) {
                view.getSettings().setJavaScriptEnabled(true);
                super.onPageFinished(view, url);
                view.getSettings().setBlockNetworkImage(false);
                addImageClickListener(mBridgeWebView);
                if (sonicSession != null) {
                    sonicSession.getSessionClient().pageFinish(url);
                }
                if (webLinsener != null) {
                    webLinsener.onLoadEndPage();
                }
                isLoadBridge = false;
                isBacking = false;
                Message message = new Message();
                message.what = 1;
                timeIndex = System.currentTimeMillis();
                message.obj = BaseWebFragment.this.url + timeIndex;
                handler.sendMessageDelayed(message, 200);
                Log.i("webview", "onPageFinished");
            }

            @TargetApi(21)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return shouldInterceptRequest(view, request.getUrl().toString());
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (sonicSession != null) {
                    return (WebResourceResponse) sonicSession.getSessionClient().requestResource(url);
                }
                return null;
            }
        };
        if (mAgentWeb == null) {
            mAgentWeb = AgentWeb.with(this)//
                    .setAgentWebParent((ViewGroup) viewDelegate.viewHolder.rootView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//
                    .closeIndicator()
                    //.setAgentWebWebSettings(getSettings())//
                    .setWebViewClient(myWebViewClient)
                    .setWebChromeClient(mWebChromeClient)
                    .setWebView(mBridgeWebView)
                    .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                    .useMiddlewareWebClient(middlewareWebClient) //设置WebViewClient中间件，支持多个WebViewClient， AgentWeb 3.0.0 加入。
                    .createAgentWeb()//
                    .ready()//
                    .go("");
        }

        WebView webView = mAgentWeb.getWebCreator().getWebView();
        WebSettings webSettings = webView.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setJavaScriptEnabled(true);

        webSettings.setAllowContentAccess(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        ViewGroup.LayoutParams layoutParams2 = webView.getLayoutParams();
        layoutParams2.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams2.height = ViewGroup.LayoutParams.MATCH_PARENT;
        webView.setLayoutParams(layoutParams2);

        //viewDelegate.viewHolder.root.addView(indicatorView);

        //        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) indicatorView.getLayoutParams();
        //        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        //        layoutParams.height = (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_80px);
        //        layoutParams.width = (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_80px);
        //        layoutParams.topMargin = AndroidUtil.getScreenW(getActivity(), true) / 2 - (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_180px);
        //        indicatorView.setLayoutParams(layoutParams);
        //        indicatorView.show();

        //4. 注入 JavaScriptInterface
        //移除漏洞
        mBridgeWebView.removeJavascriptInterface("searchBoxJavaBridge_");

        //5. 最后绑定AgentWeb
        mSonicImpl.bindAgentWeb(mAgentWeb);
        mBridgeWebView.registerHandler("WebToLocal", new BridgeHandler() {
                    @Override
                    public void handler(String data, CallBackFunction function) {
                        if (data.contains("bridge")) {
                            isLoadBridge = true;
                            Log.i("webview", "bridge");
                            return;
                        }
                        if (data.contains("http") || data.contains("bcoin")) {
                            if (webLinsener != null) {
                                //webLinsener.getData(data);
                            }
                        }
                    }
                }
        );

        ViewGroup.LayoutParams layoutParams1 = mBridgeWebView.getLayoutParams();
        layoutParams1.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams1.height = ViewGroup.LayoutParams.MATCH_PARENT;
        mBridgeWebView.setLayoutParams(layoutParams1);

        isLoad = true;

    }

    public BridgeWebView getmBridgeWebView() {
        //外部获取 绑定交互事件
        return mBridgeWebView;
    }

    public static BaseWebFragment newInstance(
            String url) {
        BaseWebFragment newFragment = new BaseWebFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //销毁SonicSession

    }

    public void callHandler(String handlerName, String data, CallBackFunction callBack) {
        mBridgeWebView.callHandler(handlerName, data, callBack);
    }

    @Override
    public void onDestroy() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        if (null != sonicSession) {
            sonicSession.destroy();
            sonicSession = null;
        }
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    //在步骤3的时候应该传入给AgentWeb
    public MiddlewareWebClientBase getMiddlewareWebClient() {
        return mSonicImpl.createSonicClientMiddleWare();
    }

    @Override
    public void onResume() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onResume();//恢复
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onPause(); //暂停应用内所有WebView ， 调用mWebView.resumeTimers();/mAgentWeb.getWebLifeCycle().onResume(); 恢复。
        }
        super.onPause();
    }

    private void addImageClickListener(WebView webView) {
        //获取页面上所有图片地址
        webView.loadUrl("javascript:(function(){ " +
                "var objs = document.getElementsByTagName(\"img\");"
                + " var array=new Array(); "
                + " for(var j=0;j<objs.length;j++){ "
                + "array[j]=objs[j].getAttribute(\"data-src\");"
                + " }  "
                + "window.imagelistener.openImage(array);   })()");
    }

    protected WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //  super.onProgressChanged(view, newProgress);
            Log.i("BaseWebFragment", "onProgressChanged:" + newProgress + "  view:" + view);
            if (newProgress == 100) {
                if (webLinsener != null) {
                    if (isLoad) {
                        isLoad = false;

                    }
                }

            } else {
                isLoad = true;
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (webLinsener != null) {
                webLinsener.onLoadTitle(title);
            }
        }


    };


    @Override
    protected Class getDelegateClass() {
        return BaseWebViewDelegate.class;
    }
}
