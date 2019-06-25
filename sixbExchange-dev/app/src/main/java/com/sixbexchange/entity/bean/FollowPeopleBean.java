package com.sixbexchange.entity.bean;

/**
 * Created by 郭青枫 on 2019/4/1 0001.
 */

public class FollowPeopleBean {
    private Long id;//跟单用户id

    private Integer amountMax;//最大份数

    private Integer amountMin;//最小份数

    private Integer allAmount;//总份数

    private Integer restAmount;//剩余份数

    private String nickName;//名称

    private String icon;//头像

    private String exchange;//交易所

    private String memo;//详细介绍

    private String title;//介绍

    private long startTime;//开始时间

    private String startTimeSrt;//开始时间字符串

    private long endTime;//结束时间

    private String endTimeStr;//结束时间字符串

    private Integer closeDay;//封闭时间（天数）

    private String ownMoney;//大V金额

    private Integer stopRate;//自动止损

    private String dealType;//交易类型

    private Integer leverage;//杠杆

    private String moneyManage;//资金管理

    private String allMoney;//大V带单总金额

    private String currency;//币种

    private Integer status;//1 未开始   ，  0 进行中 ，2 运行中 ，3 已结束

    private String shareMemo;//盈利分成

    private String benitfitRate;//收益分成

    private String principal;//保本类型

    private String bicoinRate;//大V实盘收益率

    private String restTime;//剩余时间

    private String welfare;//盈利

    private String closeTimeStr;//结束时间
    private String standard;//结束时间

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmountMax() {
        return amountMax;
    }

    public void setAmountMax(Integer amountMax) {
        this.amountMax = amountMax;
    }

    public Integer getAmountMin() {
        return amountMin;
    }

    public void setAmountMin(Integer amountMin) {
        this.amountMin = amountMin;
    }

    public Integer getAllAmount() {
        return allAmount;
    }

    public void setAllAmount(Integer allAmount) {
        this.allAmount = allAmount;
    }

    public Integer getRestAmount() {
        return restAmount;
    }

    public void setRestAmount(Integer restAmount) {
        this.restAmount = restAmount;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getStartTimeSrt() {
        return startTimeSrt;
    }

    public void setStartTimeSrt(String startTimeSrt) {
        this.startTimeSrt = startTimeSrt;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public Integer getCloseDay() {
        return closeDay;
    }

    public void setCloseDay(Integer closeDay) {
        this.closeDay = closeDay;
    }

    public String getOwnMoney() {
        return ownMoney;
    }

    public void setOwnMoney(String ownMoney) {
        this.ownMoney = ownMoney;
    }

    public Integer getStopRate() {
        return stopRate;
    }

    public void setStopRate(Integer stopRate) {
        this.stopRate = stopRate;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    public Integer getLeverage() {
        return leverage;
    }

    public void setLeverage(Integer leverage) {
        this.leverage = leverage;
    }

    public String getMoneyManage() {
        return moneyManage;
    }

    public void setMoneyManage(String moneyManage) {
        this.moneyManage = moneyManage;
    }

    public String getAllMoney() {
        return allMoney;
    }

    public void setAllMoney(String allMoney) {
        this.allMoney = allMoney;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getShareMemo() {
        return shareMemo;
    }

    public void setShareMemo(String shareMemo) {
        this.shareMemo = shareMemo;
    }

    public String getBenitfitRate() {
        return benitfitRate;
    }

    public void setBenitfitRate(String benitfitRate) {
        this.benitfitRate = benitfitRate;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getBicoinRate() {
        return bicoinRate;
    }

    public void setBicoinRate(String bicoinRate) {
        this.bicoinRate = bicoinRate;
    }

    public String getRestTime() {
        return restTime;
    }

    public void setRestTime(String restTime) {
        this.restTime = restTime;
    }

    public String getWelfare() {
        return welfare;
    }

    public void setWelfare(String welfare) {
        this.welfare = welfare;
    }

    public String getCloseTimeStr() {
        return closeTimeStr;
    }

    public void setCloseTimeStr(String closeTimeStr) {
        this.closeTimeStr = closeTimeStr;
    }
}
