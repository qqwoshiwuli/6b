package com.sixbexchange.server;

import android.text.TextUtils;

import com.fivefivelike.mybaselibrary.utils.SaveUtil;
import com.sixbexchange.base.AppConst;


/**
 * Created by 郭青枫 on 2019/1/12 0012.
 */

public class HttpUrl {
    static HttpUrl httpUrl = new HttpUrl();

    public static HttpUrl getIntance() {
        if (httpUrl == null) {
            httpUrl = new HttpUrl();
        }
        return httpUrl;
    }

    public void setToken(String token) {
        SaveUtil.getInstance().saveString("auth", token);
    }

    public String getToken() {
        return SaveUtil.getInstance().getString("auth");
    }

    public boolean isHaveToken() {
        return !TextUtils.isEmpty(SaveUtil.getInstance().getString("token"));
    }

    public void delectHttpUrl() {
        httpUrl = null;
    }

    private static String baseUrl = "https://www.6b.top" + (AppConst.isTest ? "" : "/api");
    private static String baseUrl2 = "https://www.6b.top" + (AppConst.isTest ? "" : "/api");

    public static void setBaseUrl(String u) {
        baseUrl = u;
    }

    /**
     * 登录
     */
    public String login = baseUrl + "/app/login";
    /**
     * 注册
     */
    public String signup = baseUrl + "/app/open/signup";
    /**
     * 用户信息
     */
    public String userinfo = baseUrl + "/app/user/info";
    /**
     * 注册验证码
     */
    public String vcode = baseUrl + "/app/open/vcode";
    /**
     * 找回密码验证码
     */
    public String preReset = baseUrl + "/app/open/pass/preReset";
    /**
     * 重置密码
     */
    public String setpassword = baseUrl + "/app/user/set/password";
    /**
     * 获取钱包列表
     */
    public String getAccount = baseUrl + "/app/user/getAccount";
    /**
     * 跟单列表
     */
    public String followlist = baseUrl + "/app/follow/list";
    /**
     * 我的跟单
     */
    public String MyFollow = baseUrl + "/app/follow/MyFollow";
    /**
     * 参与跟单
     */
    public String followattend = baseUrl + "/app/follow/attend";
    /**
     * 跟单详情
     */
    public String followdetail = baseUrl + "/app/follow/detail";
    /**
     * 钱包资金明细
     */
    public String getAccountDetail = baseUrl + "/app/user/getAccountDetail";
    /**
     * 提币地址
     */
    public String addrinfo = baseUrl + "/app/addr/info";
    /**
     * bitmex充值地址
     */
    public String depositAddress = baseUrl + "/app/bitmex/depositAddress";
    /**
     * 提币地址列表
     */
    public String extractAddr = baseUrl + "/app/addr/extractAddr";
    /**
     * 删除提币地址
     */
    public String delExtractAddr = baseUrl + "/app/addr/delExtractAddr";
    /**
     * 前登录用户手机号
     */
    public String mobile = baseUrl + "/app/user/mobile";
    /**
     * 新增提币地址
     */
    public String addExtractAddr = baseUrl + "/app/addr/addExtractAddr";
    /**
     * 提币页面
     */
    public String extract = baseUrl + "/app/account/extract";
    /**
     * 发送提币请求
     */
    public String sendExtract = baseUrl + "/app/account/sendExtract";
    /**
     * 版本更新
     */
    public String getlatestversion = baseUrl + "/app/open/appversion/getlatestversion";
    /**
     * 交易所币种列表
     */
    public String allCoins = baseUrl + "/app/open/allCoins";
    /**
     * 资金划转
     */
    public String accounttrans = baseUrl + "/app/account/trans";
    /**
     * 获取持仓
     */
    public String accountgetAccount = baseUrl + "/app/account/getAccount";
    /**
     * 用户订单
     */
    public String accountgetOrders = baseUrl + "/app/account/getOrders";
    /**
     * 交易信息
     */
    public String tradedetail = baseUrl + "/app/trade/detail";
    /**
     * 交易所列表
     */
    public String exchangeList = baseUrl + "/app/trade/exchangeList";
    /**
     * 交易所交易对
     */
    public String tradelist = baseUrl + "/app/trade/list";
    /**
     * 交易所交易对
     */
    public String openokefAll = baseUrl + "/app/open/okefAll";
    /**
     * 获取开仓可用
     */
    public String accountopen = baseUrl + "/app/account/open";
    /**
     * 闪兑下单
     */
    public String spotorder = "https://www.6b.top/api" + "/app/spot/order";
    /**
     * 获取平单可用
     */
    public String accountclose = baseUrl + "/app/account/close";
    /**
     * 获取单个币种持仓
     */
    public String getCoinPosition = baseUrl + "/app/account/getCoinPosition";
    /**
     * 下单
     */
    public String placeOrder = baseUrl + "/app/account/placeOrder";
    /**
     * 轮播图接口
     */
    public String openchart = baseUrl + "/app/open/chart";
    /**
     * 撤单
     */
    public String cancelOrder = baseUrl + "/app/account/cancelOrder";
    /**
     * 全部撤单
     */
    public String cancelAllOrder = baseUrl + "/app/account/cancelAllOrder";
    /**
     * 持仓页面下拉框数据
     */
    public String tradeCoins = baseUrl + "/app/open/tradeCoins";
    /**
     * 修改杠杆
     */
    public String changeLeverage = baseUrl + "/app/account/changeLeverage";
    /**
     * 获取当前杠杆
     */
    public String getLeverage = baseUrl + "/app/account/getLeverage";
    /**
     * 获取k线
     */
    public String candles = baseUrl + "/app/trade/candles";
    /**
     * 交易所交易对
     */
    public String tradeall = baseUrl + "/app/trade/all";
    /**
     * 闪兑交易所交易对
     */
    public String tradeall2 = baseUrl + "/app/spot/symbols";
    /**
     * 管理保证金
     */
    public String transferMargin = baseUrl + "/app/account/transferMargin";
    /**
     * 开启bitmex
     */
    public String bitmexbind = baseUrl + "/app/bitmex/bind";
    /**
     * bitmex开启状态
     */
    public String bitmexbindStatus = baseUrl + "/app/bitmex/bindStatus";
    /**
     * 计划委托
     */
    public String orderstop = baseUrl + "/app/order/stop";
    /**
     * 计划委托弹窗
     */
    public String orderremark = baseUrl + "/app/order/remark";
    /**
     * 是否填写邀请码
     */
    public String inviteState = baseUrl + "/app/user/inviteState";
    /**
     * 补填邀请码
     */
    public String setInviteCode = baseUrl + "/app/user/setInviteCode";
    /**
     * 获取单个币种的资产
     */
    public String getSingleBalance = baseUrl + "/app/account/getSingleBalance";
    /**
     * bitmex持仓页资产
     */
    public String getBitmexDetail = baseUrl + "/app/user/getBitmexDetail";
    /**
     * 切换okex张/币显示
     */
    public String setOkexSetting = baseUrl + "/app/trade/setOkexSetting";
    /**
     * 获取闪兑交易对列表
     */
    public String coinpop = baseUrl2 + "/app/spot/symbols";
    /**
     * 下单相关
     */
    public String coinorder = baseUrl2 + "/app/spot/order";
    /**
     * 获取6b资产明细
     */
    public String coindetail = baseUrl2 + "/app/user/ourAccountDetail";
    /**
     * 交易历史
     */
    public String coinhistory = baseUrl2 + "/app/spot/orderhist";
    /**
     * 获取okex张/币显示标识
     */
    public String getOkexSetting = baseUrl2 + "/app/trade/getOkexSetting";
    /**
     * bitmex设置
     */
    public String saveConfig = baseUrl2 + "/app/bitmex/saveConfig";
    /**
     * 获取bitmex设置
     */
    public String getConfig = baseUrl2 + "/app/bitmex/getConfig";
    /**
     * 获取ws权限
     */
    public String wsAuth = baseUrl2 + "/app/bitmex/wsAuth";
}
