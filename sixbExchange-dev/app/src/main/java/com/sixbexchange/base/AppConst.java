package com.sixbexchange.base;


import com.sixbexchange.BuildConfig;

/**
 * 全局配置
 */
public class AppConst {

    //http://47.98.56.206:1913/symbol/info?name=testCoin.
    public static final boolean isSSL = BuildConfig.isSSL;

    public static final String app2BaseUrl = BuildConfig.app2BaseUrl;// "https://tblz.bicoin.com.cn";
    public static final boolean isTest = false;

//    public static final String app2BaseUrl = "http://test.6b.top";
//    public static final boolean isTest = true;



    public static final String wsAddress = "wss://1token.trade/api/v1/ws/tick?source=bicoin";//;
    public static final String wsAddressokcoin = "wss://149.129.81.70:10442/ws/v3";//;
    public static final String wsAddress2 = "wss://1token.trade/api/v1/ws/candle";//;
    public static final String wsAddress3 = "wss://tws.6b.top/orderws/order?apiKey=";//;


    public static final String pushId = BuildConfig.pushId;// "5afcd70bf43e483b6900038c";
    public static final String pushSecret = BuildConfig.pushSecret;//"5a7d1aef606a29e7fd89a2db8511a875";
    public static final String xiaoMiPushId = BuildConfig.XiaoMiPushId;// "5afcd70bf43e483b6900038c";
    public static final String xiaoMiPushKey = BuildConfig.XiaoMiPushKey;//"5a7d1aef606a29e7fd89a2db8511a875";
    public static final String meizuAppId = BuildConfig.meizuAppId;// BuildConfig.meizuAppId;//"5a7d1aef606a29e7fd89a2db8511a875";
    public static final String meizuAppKey = BuildConfig.meizuAppKey;//


    public static final String httpsCer = "json/bm.cer";
    public static final String umS1 = "Bradar";
    public static final String aliasS1 = "bradar";
    public static final String packageName = "com.gqfbitmex";
    public static final String apkName = "6b.apk";
    public static final boolean isChinese = true;


}
