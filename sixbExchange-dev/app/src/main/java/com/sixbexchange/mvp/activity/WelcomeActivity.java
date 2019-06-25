package com.sixbexchange.mvp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.fivefivelike.mybaselibrary.base.BaseActivity;
import com.fivefivelike.mybaselibrary.base.BaseDataBindActivity;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.SaveUtil;
import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppWakeUpAdapter;
import com.fm.openinstall.model.AppData;
import com.google.gson.Gson;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.AppVersion;
import com.sixbexchange.entity.bean.ExchWalletBean;
import com.sixbexchange.entity.bean.OpenChartBean;
import com.sixbexchange.entity.bean.OpenChartBeans2;
import com.sixbexchange.mvp.databinder.WebActivityBinder;
import com.sixbexchange.mvp.databinder.WelcomeBinder;
import com.sixbexchange.mvp.delegate.WebActivityDelegate;
import com.sixbexchange.mvp.delegate.WelcomeDelegate;
import com.sixbexchange.mvp.fragment.MineFragment;
import com.sixbexchange.server.HttpUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class WelcomeActivity  extends BaseDataBindActivity<WelcomeDelegate, WelcomeBinder> implements RequestCallback {
    @Override
    protected Class<WelcomeDelegate> getDelegateClass() {
        return WelcomeDelegate.class;
    }

    @Override
    public WelcomeBinder getDataBinder(WelcomeDelegate viewDelegate) {
        return new WelcomeBinder(viewDelegate);
    }
    @Override
    protected void onResume() {
        super.onResume();
        addRequest(binder.getopenchart(
                "3", new RequestCallback() {
                    @Override
                    public void success(int requestCode, String data, String errorData) {
                        try {
//                            Log.i("wuli",""+data);
                            JSONObject jsonObject = new JSONObject(data);
                            JSONArray jsonArray=jsonObject.optJSONArray("data");
                            if (jsonObject.getInt("code")!=0||jsonArray==null)
                            {
                                going();
                            }else
                            {
                                OpenChartBean  openChartBeans=new OpenChartBean();
                                openChartBeans.setImgUrl(jsonArray.optJSONObject(0).getString("imgUrl"));
                                openChartBeans.setSkipUrl(jsonArray.optJSONObject(0).getString("skipUrl"));
                                openChartBeans.setIsSkip(jsonArray.optJSONObject(0).getInt("isSkip"));
                                openChartBeans.setIsNeed(jsonArray.optJSONObject(0).getInt("isNeed"));
                                Intent intent=new Intent();
                                intent.putExtra("openChartBeans",openChartBeans);
                                intent.setClass(WelcomeActivity.this,SplashActivity.class);
                                WelcomeActivity.this.startActivity(intent);

                            }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                            going();
                        }
                    }

                    @Override
                    public void error(int requestCode, Throwable exThrowable, String errorData) {

                    }
                }
        ));
    }
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取唤醒参数
        OpenInstall.getWakeUp(getIntent(), wakeUpAdapter);
        view = findViewById(R.id.rootview);
//        if (!ActivityUtils.isActivityExistsInStack(HomeActivity.class)) {
//            view.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
//                    finish();
//                }
//            }, 500);
//        } else {
//            finish();
//        }
    }
    public void going()
    {
        if (!ActivityUtils.isActivityExistsInStack(HomeActivity.class)) {
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
                    finish();
                }
            }, 500);
        } else {
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 此处要调用，否则App在后台运行时，会无法截获
        OpenInstall.getWakeUp(intent, wakeUpAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wakeUpAdapter = null;
    }

    AppWakeUpAdapter wakeUpAdapter = new AppWakeUpAdapter() {
        @Override
        public void onWakeUp(AppData appData) {
            //获取渠道数据
            String channelCode = appData.getChannel();
            //获取绑定数据
            String bindData = appData.getData();
            //ToastUtil.show(bindData);
//            going();
        }
    };

    List<OpenChartBean> openChartBeans;
    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
        }
    }
}
