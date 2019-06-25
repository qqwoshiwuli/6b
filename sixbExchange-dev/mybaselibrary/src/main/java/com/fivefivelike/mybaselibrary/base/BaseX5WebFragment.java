package com.fivefivelike.mybaselibrary.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSONObject;
import com.fivefivelike.mybaselibrary.R;
import com.fivefivelike.mybaselibrary.dsbridge.CompletionHandler;
import com.fivefivelike.mybaselibrary.dsbridge.OnReturnValue;
import com.fivefivelike.mybaselibrary.sonicx5.SonicImpl;
import com.fivefivelike.mybaselibrary.utils.AndroidUtil;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.GlobleContext;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.SaveUtil;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.sonic.sdk.SonicSession;
import com.umeng.analytics.MobclickAgent;
import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.indicators.LineSpinFadeLoaderIndicator;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;
import java.util.Map;


/**
 * Created by 郭青枫 on 2018/3/4 0004.
 */

public class BaseX5WebFragment extends BaseFragment<BaseX5WebViewDelegate> {
    private SonicImpl mSonicImpl;
    private String url = "http://47.96.180.179:1904/gameTeam/showWebViewIndex";

    private X5Webview mWebView;
    BaseWebLinsener webLinsener;
    boolean isLoad = true;
    boolean isLoasError = false;
    AVLoadingIndicatorView indicatorView;
    private SonicSession sonicSession;


    public void setWebLinsener(BaseWebLinsener webLinsener) {
        this.webLinsener = webLinsener;
    }

    public void loadUrl(String url) {
        this.url = url;
        initCookie();
        isLoasError = false;
        mWebView.loadUrl(url);
        isLoad = true;
    }

    public boolean goBack() {
        boolean b = mWebView.canGoBack();
        if (b) {
            mWebView.goBack();
        }
        return b;
    }

    private void initCookie() {
        if (url.contains("blz") || url.contains("tblz") || url.contains("bicoin")) {
            String token = SaveUtil.getInstance().getString("token");
            com.tencent.smtt.sdk.CookieSyncManager.createInstance(getActivity());
            com.tencent.smtt.sdk.CookieManager cm = com.tencent.smtt.sdk.CookieManager.getInstance();
            cm.setAcceptCookie(true);
            cm.setCookie(url, "token=" + token);
            if (Build.VERSION.SDK_INT < 21) {
                com.tencent.smtt.sdk.CookieSyncManager.getInstance().sync();
            } else {
                com.tencent.smtt.sdk.CookieManager.getInstance().flush();
            }
        }
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        url = getArguments().getString("url", "http://47.96.180.179:1904/gameTeam/showWebViewIndex");
        //        //3. 创建AgentWeb ，注意创建AgentWeb的时候应该使用加入SonicWebViewClient中间件
        //        //        super.onViewCreated(view, savedInstanceState); // 创建 AgentWeb 注意的 go("") 传入的 mUrl 应该null 或者""
        //        //viewDelegate.viewHolder.mBridgeWebView = new BridgeWebView(getActivity());

        indicatorView = new AVLoadingIndicatorView(viewDelegate.viewHolder.root.getContext());//尾部加载中状态
        indicatorView.setIndicator(new LineSpinFadeLoaderIndicator());
        indicatorView.setIndicatorColor(CommonUtils.getColor(R.color.color_font4));
        mWebView = new X5Webview(getActivity());
        initCookie();
        // 1. 首先创建SonicImpl
        mSonicImpl = new SonicImpl(url);
        //        // 2. 调用 onCreateSession
        mSonicImpl.onCreateSession(mWebView.getSettings().getUserAgentString());
        sonicSession = mSonicImpl.getSonicSession();


        mWebView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        viewDelegate.viewHolder.root.addView(mWebView);


        mWebView.setWebViewClient(mWebViewClient);
        mWebView.setWebChromeClient(mWebChromeClient);

        //允许JavaScript执行
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setSupportMultipleWindows(false);
        mWebView.setVerticalScrollBarEnabled(false);
        //运行webview通过URI获取安卓文件
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);


        viewDelegate.viewHolder.root.addView(indicatorView);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) indicatorView.getLayoutParams();
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParams.height = (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_80px);
        layoutParams.width = (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_80px);
        layoutParams.topMargin = AndroidUtil.getScreenW(getActivity(), true) / 2 - (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_180px);
        indicatorView.setLayoutParams(layoutParams);
        indicatorView.show();
        // 注册jsApi
        mSonicImpl.bindAgentWeb(mWebView);
        mWebView.addJavascriptObject(this, null);


        // mWebView.loadUrl("file:///android_asset/show.html");

        //mWebView.loadUrl("https://mapp.bicoin.info/wang/2019/index2019.html");

        //        viewDelegate.viewHolder.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        //            @Override
        //            public void onRefresh() {
        //                if (isLoasError) {
        //                    loadUrl(url);
        //                }
        //            }
        //        });
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {
                UiHeplUtils.startWeb(getActivity(), s);
            }
        });
    }

    String data;
    String forward;

    @JavascriptInterface
    public void WebToLocal(Object msg, CompletionHandler<String> handler) {
        if (webLinsener != null) {
            data = msg.toString();
            forward = GsonUtil.getInstance().getValue(data, "forward");
            if ("bcoin://userLogin".equals(forward)) {
                //登录
                startActivityForResult(new Intent(this.getContext(),
                        BaseApp.getInstance().getLoginActivityClass()), 0x123);
            } else if ("bcoin://buryingPoint".equals(forward)) {
                //埋点
                String parameters = GsonUtil.getInstance().getValue(data, "parameters");
                String name = GsonUtil.getInstance().getValue(parameters, "name");
                MobclickAgent.onEvent(GlobleContext.getInstance().getApplicationContext(), name);
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webLinsener.getData(forward, data);
                    }
                });
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 0x123) {
                loadUrl(url);
            }
        }
    }

    public interface HandlerCallBack {
        void callBack(String o);
    }

    public void callHandler(String handlerName, Map<String, Object> map, final HandlerCallBack callBack) {
        String s = GsonUtil.getInstance().toJson(map);
        JSONObject myJson = JSONObject.parseObject(s);
        mWebView.callHandler(handlerName, new Object[]{myJson}, new OnReturnValue<String>() {
            @Override
            public void onValue(String retValue) {
                //callBack.callBack(retValue);
                callBack.callBack(retValue);
                Log.d("jsbridge", "call succeed,return value is " + retValue);
            }
        });
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(com.tencent.smtt.sdk.WebView view, String url, Bitmap favicon) {
            //do you  work
            Log.i("Info", "BaseWebActivity onPageStarted");
            if (webLinsener != null) {
                webLinsener.onWebStart();
            }
        }

        @Override
        public void onReceivedError(com.tencent.smtt.sdk.WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
            super.onReceivedError(webView, webResourceRequest, webResourceError);
            //            if (!isLoasError && isLoad) {
            //                isLoasError = true;
            //                //webView.loadUrl("file:///android_asset/error.html");
            //                viewDelegate.viewHolder.swipeRefreshLayout.setEnabled(true);
            //                viewDelegate.viewHolder.swipeRefreshLayout.setRefreshing(false);
            //            }
        }

        @Override
        public void onPageFinished(com.tencent.smtt.sdk.WebView webView, String s) {
            super.onPageFinished(webView, s);
            if (webLinsener != null) {
                webLinsener.onLoadEndPage();
                //webView.loadUrl("file:///android_asset/show-1.html");
            }
            isLoad = false;
            indicatorView.hide();
            if (sonicSession != null) {
                sonicSession.getSessionClient().pageFinish(url);
            }

            //            if (!isLoasError) {
            //                viewDelegate.viewHolder.swipeRefreshLayout.setEnabled(false);
            //            }
            //            viewDelegate.viewHolder.swipeRefreshLayout.setRefreshing(false);
        }

        @TargetApi(21)
        @Override
        public WebResourceResponse shouldInterceptRequest(com.tencent.smtt.sdk.WebView webView, WebResourceRequest webResourceRequest) {
            return shouldInterceptRequest(webView, webResourceRequest.getUrl().toString());
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(com.tencent.smtt.sdk.WebView webView, String s) {
            if (sonicSession != null) {
                return (WebResourceResponse) sonicSession.getSessionClient().requestResource(url);
            }
            return null;
        }

        @Override
        public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView webView, String s) {
            return super.shouldOverrideUrlLoading(webView, s);
        }
    };

    public static BaseX5WebFragment newInstance(
            String url,
            boolean isShowToken) {
        BaseX5WebFragment newFragment = new BaseX5WebFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putBoolean("isShowToken", isShowToken);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    public static BaseX5WebFragment newInstance(
            String url) {
        BaseX5WebFragment newFragment = new BaseX5WebFragment();
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

    @Override
    public void onDestroy() {
        mWebView.onDestroy();
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


    @Override
    public void onPause() {
        mWebView.onPause();
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

    private ValueCallback<Uri> mUploadMessage;//回调图片选择，4.4以下
    private ValueCallback<Uri[]> mUploadCallbackAboveL;//回调图片选择，5.0以上
    private static final int FILE_SELECT_CODE = 0;
    private com.tencent.smtt.sdk.WebChromeClient mWebChromeClient = new com.tencent.smtt.sdk.WebChromeClient() {

        @Override
        public void onProgressChanged(com.tencent.smtt.sdk.WebView webView, int i) {
            super.onProgressChanged(webView, i);
            if (i == 100) {

            }
        }

        @Override
        public void onReceivedTitle(com.tencent.smtt.sdk.WebView webView, String s) {
            super.onReceivedTitle(webView, s);
            if (webLinsener != null) {
                webLinsener.onLoadTitle(s);
            }
        }


        @Override
        public void openFileChooser(ValueCallback<Uri> valueCallback, String s, String s1) {
            if (Build.VERSION.SDK_INT < 21) {
                mUploadMessage = valueCallback;
                showChooseImg();
            }
            //super.openFileChooser(valueCallback, s, s1);
        }


        @Override
        public boolean onShowFileChooser(com.tencent.smtt.sdk.WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
            if (Build.VERSION.SDK_INT >= 21) {
                mUploadCallbackAboveL = valueCallback;
                showChooseImg();
            }
            return false; //super.onShowFileChooser(webView, valueCallback, fileChooserParams);
        }

        @Override
        public boolean onCreateWindow(com.tencent.smtt.sdk.WebView webView, boolean isDialog, boolean isUserGesture, Message message) {
            com.tencent.smtt.sdk.WebView.WebViewTransport transport = (com.tencent.smtt.sdk.WebView.WebViewTransport) message.obj;
            transport.setWebView(webView);
            message.sendToTarget();
            return true;
        }
    };

    private void showChooseImg() {
        UiHeplUtils.getPhoto(getActivity(), new Action<String>() {
            @Override
            public void onAction(int requestCode, @NonNull String result) {
                changeHeader(result);
            }
        }, new Action<ArrayList<AlbumFile>>() {
            @Override
            public void onAction(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                changeHeader(result.get(0).getPath());
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadMessage != null) {
                    mUploadMessage.onReceiveValue(null);
                    mUploadMessage = null;
                }
                if (mUploadCallbackAboveL != null) {
                    Uri[] uris = new Uri[]{};
                    mUploadCallbackAboveL.onReceiveValue(uris);
                    mUploadCallbackAboveL = null;
                }
            }
        }, 1);
    }

    @Override
    public void onResume() {
        mWebView.onResume();
        super.onResume();
    }

    private void changeHeader(String path) {
        Uri uri = Uri.parse(path);
        if (mUploadMessage != null) {
            mUploadMessage.onReceiveValue(uri);
            mUploadMessage = null;
        }
        if (mUploadCallbackAboveL != null) {
            Uri[] uris = new Uri[]{uri};
            mUploadCallbackAboveL.onReceiveValue(uris);
            mUploadCallbackAboveL = null;
        }
    }


    @Override
    protected Class getDelegateClass() {
        return BaseX5WebViewDelegate.class;
    }
}
