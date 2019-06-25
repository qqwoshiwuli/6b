package com.fivefivelike.mybaselibrary.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.fivefivelike.mybaselibrary.R;


/**
 * @ author      Qsy
 * @ date        17/2/14 上午11:36
 * @ description 自定义dialog
 */
public class NetConnectDialog extends Dialog {

    private boolean isBack = false;
    private final Context context;

    public boolean isBack() {
        return isBack;
    }

    public NetConnectDialog setBack(boolean back) {
        isBack = back;
        return this;
    }

    public NetConnectDialog(Context context) {
        super(context, R.style.baseDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_net_connect, null);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(view);


        //        Window                     dialogWindow = getWindow();
        //        WindowManager.LayoutParams lp           = dialogWindow.getAttributes();
        //        DisplayMetrics             d            = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        //        lp.width = (int) (d.widthPixels * 0.8); // 宽度设置为屏幕的0.6
        //        dialogWindow.setAttributes(lp);
    }

}
