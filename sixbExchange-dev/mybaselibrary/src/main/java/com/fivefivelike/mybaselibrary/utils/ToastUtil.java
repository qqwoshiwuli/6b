package com.fivefivelike.mybaselibrary.utils;

import com.blankj.utilcode.util.ToastUtils;
import com.fivefivelike.mybaselibrary.R;


/**
 * Toast
 * ToastUtils 常用方式整合
 * <p>
 * 统一弹出 toast工具
 */
public class ToastUtil {

    public static void show(String message) {
        ToastUtils.cancel();
        ToastUtils.setBgResource(R.drawable.toast_bg);
        ToastUtils.setMsgColor(CommonUtils.getColor(R.color.white));
        if (message.length() < 15) {
            ToastUtils.showShort(message);
        } else {
            ToastUtils.showLong(message);
        }
    }

}
