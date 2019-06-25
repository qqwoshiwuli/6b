package com.fivefivelike.mybaselibrary.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.tencent.smtt.sdk.QbSdk;

/**
 * Created by 郭青枫 on 2018/4/2 0002.
 */

public class AdvanceLoadX5Service extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initX5();
    }

    private void initX5() {
        //  预加载X5内核
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }

    QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

        @Override
        public void onViewInitFinished(boolean arg0) {
            // TODO Auto-generated method stub
            //初始化完成回调

        }

        @Override
        public void onCoreInitFinished() {
            // TODO Auto-generated method stub
        }
    };

}