package com.sixbexchange.entity.bean;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.fivefivelike.mybaselibrary.utils.SaveUtil;
import com.fivefivelike.mybaselibrary.utils.ToastUtil;
import com.sixbexchange.base.Application;
import com.sixbexchange.server.HttpUrl;

/**
 * Created by 郭青枫 on 2019/4/1 0001.
 */

public class UserLoginInfo {

    String uid;
    String mobile;
    String name;
    String icode;
    String hImg;

    public String getIcode() {
        return icode;
    }

    public void setIcode(String icode) {
        this.icode = icode;
    }

    public String gethImg() {
        return hImg;
    }

    public void sethImg(String hImg) {
        this.hImg = hImg;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static boolean isLogin() {
        boolean isLogin = isLoginNoToast();
        if (isLogin) {
            return true;
        } else {
            ToastUtil.show("需要登录");
            return false;
        }
    }

    public static boolean isLoginAndToLogin(Context context) {
        boolean isLogin = isLoginNoToast();
        if (isLogin) {
            return true;
        } else {
            context.startActivity(new Intent(context,
                    Application.getInstance().getLoginActivityClass()));
            return false;
        }
    }

    public static boolean isLoginNoToast() {
        boolean isLogin = SaveUtil.getInstance().getBoolean("UserLoginInfo_isLogin");
        String token = HttpUrl.getIntance().getToken();
        if (!isLogin || TextUtils.isEmpty(token)) {
            logout();
        }
        return isLogin;
    }

    public static void addNewLoginInfo(UserLoginInfo userLoginInfo) {
        SaveUtil.getInstance().saveString("uid", userLoginInfo.getUid() + "");
        SaveUtil.getInstance().saveString("UserLoginInfo_getMobile", userLoginInfo.getMobile() + "");
        SaveUtil.getInstance().saveBoolean("UserLoginInfo_isLogin", true);
        SaveUtil.getInstance().saveString("UserLoginInfo_getName", userLoginInfo.getName());
        SaveUtil.getInstance().saveString("UserLoginInfo_getIcode", userLoginInfo.getIcode());
        SaveUtil.getInstance().saveString("UserLoginInfo_gethImg", userLoginInfo.gethImg());

    }

    public static void logout() {
        SaveUtil.getInstance().saveString("uid", "");
        HttpUrl.getIntance().setToken("");
        SaveUtil.getInstance().saveBoolean("UserLoginInfo_isLogin", false);

        SaveUtil.getInstance().saveString("UserLoginInfo_getMobile", "");
        SaveUtil.getInstance().saveString("UserLoginInfo_getName", "");
        SaveUtil.getInstance().saveString("UserLoginInfo_getIcode", "");
        SaveUtil.getInstance().saveString("UserLoginInfo_gethImg", "");
    }


    public static UserLoginInfo getLoginInfo() {
        if (!isLoginNoToast()) {
            return null;
        }
        String uid = SaveUtil.getInstance().getString("uid");
        String UserLoginInfo_getMobile = SaveUtil.getInstance().getString("UserLoginInfo_getMobile");
        String UserLoginInfo_getName = SaveUtil.getInstance().getString("UserLoginInfo_getName");
        String UserLoginInfo_getIcode = SaveUtil.getInstance().getString("UserLoginInfo_getIcode");
        String UserLoginInfo_gethImg = SaveUtil.getInstance().getString("UserLoginInfo_gethImg");


        UserLoginInfo userLoginInfo = new UserLoginInfo();
        userLoginInfo.setUid(uid);
        userLoginInfo.setMobile(UserLoginInfo_getMobile);
        userLoginInfo.setName(UserLoginInfo_getName);
        userLoginInfo.setIcode(UserLoginInfo_getIcode);
        userLoginInfo.sethImg(UserLoginInfo_gethImg);
        return userLoginInfo;
    }

}
