package com.sixbexchange.mvp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.bumptech.glide.Glide;
import com.fivefivelike.mybaselibrary.base.BaseActivity;
import com.fivefivelike.mybaselibrary.utils.glide.GlideUtils;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.OpenChartBean;
import com.sixbexchange.mvp.fragment.MainFragment;
import com.sixbexchange.mvp.fragment.SecurityCenterFragment;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class SplashActivity extends Activity  {
    OpenChartBean openChartBean;
    ImageView imageView;
    private static final int WHAT = 101;
    private Context mContext;
    private TextView mTimerTv;

    private Timer mTimer;
    private TimerTask mTimerTask;

    private static final long MAX_TIME = 12000;
    private long curTime = 0;
    private boolean isPause = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//满屏显示
        setContentView(R.layout.activity_splash);
        imageView=findViewById(R.id.image);
        openChartBean=(OpenChartBean)getIntent().getSerializableExtra("openChartBeans");
        GlideUtils.loadImage(openChartBean.getImgUrl(), imageView,GlideUtils.getNoCacheRO());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (openChartBean.getIsSkip()==1)
                {
                    if (openChartBean.getIsNeed()==1)
                    {
                        Intent intent= new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(openChartBean.getSkipUrl());
                        intent.setData(content_url);
                        startActivity(intent);
                    }else
                    {
                        if (openChartBean.getSkipUrl().contains("invitationPage"))
                        {
//                            if (getParentFragment() instanceof MainFragment) {
//                                ((MainFragment) getParentFragment()).startBrotherFragment(new SecurityCenterFragment());
//                            }
                        }else
                        {
                            if (countDownTimer!=null)
                            {
                                countDownTimer.cancel();
                            }
                            WebActivityActivity.startAct(SplashActivity.this,openChartBean.getSkipUrl());
                        }
                    }
                }
            }
        });
//        new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
//                    finish();
//                }
//            }, 5000);
        // 注意：倒计时时间都是毫秒。倒计时总时间+间隔
        countDownTimer=new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
//                mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
//                Log.i("wuli","millisUntilFinished="+millisUntilFinished);
            }

            public void onFinish() {
//                mTextField.setText("done!");
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
            }
        }.start();
    }
    CountDownTimer countDownTimer;

    @Override
    protected void onRestart() {
        super.onRestart();
                new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                }
            }, 1500);
    }
}
