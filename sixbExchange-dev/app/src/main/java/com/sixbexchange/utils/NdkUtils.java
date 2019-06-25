package com.sixbexchange.utils;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.ObjectUtils;

/**
 * Created by 郭青枫 on 2018/12/28 0028.
 */

public class NdkUtils {
    static {
        System.loadLibrary("ndklib");
    }

    public static void start() {

    }

    public static native String key();

    public static native float getSum(int a, int b, float[] nums, int n);

    public static String getBtcAddress(String data, String md5) {
        if (ObjectUtils.equals(EncryptUtils.encryptMD5ToString(
                data, key()).toLowerCase(), md5.toLowerCase())) {
            return data;
        } else {
            return "服务器返回地址有误，请联系客服！";
        }
    }
}
