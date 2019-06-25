package com.sixbexchange.entity.bean;

/**
 * Created by 郭青枫 on 2019/4/1 0001.
 */

public class MyFollowBean {
    /**
     * currency : btc
     * allAmount : 500
     * allMoney : 10000
     * nickName : 刘建书
     * icon : http://topcoin.oss-cn-hangzhou.aliyuncs.com/headimg/12.png
     * title : 建书带单
     * startTime : 1553467821000
     * startTimeSrt : null
     * endTime : 1553813424000
     * endTimeStr : 2019-03-29
     * status : 2
     * amount : 3
     * welfare : 30
     * amountValue : null
     * dealType : 合约
     * benitfitRate : 50%
     * principal : 保本
     * closeDay : 50
     * closeTimeStr : 2019-05-18
     */

    private String currency;
    private int allAmount;
    private String allMoney;
    private String nickName;
    private String icon;
    private String title;
    private long startTime;
    private String startTimeSrt;
    private long endTime;
    private String endTimeStr;
    private int status;
    private int amount;
    private String welfare;
    private String amountValue;
    private String dealType;
    private String benitfitRate;
    private String principal;
    private String closeDay;
    private String closeTimeStr;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getAllAmount() {
        return allAmount;
    }

    public void setAllAmount(int allAmount) {
        this.allAmount = allAmount;
    }

    public String getAllMoney() {
        return allMoney;
    }

    public void setAllMoney(String allMoney) {
        this.allMoney = allMoney;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getWelfare() {
        return welfare;
    }

    public void setWelfare(String welfare) {
        this.welfare = welfare;
    }

    public String getAmountValue() {
        return amountValue;
    }

    public void setAmountValue(String amountValue) {
        this.amountValue = amountValue;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
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

    public String getCloseDay() {
        return closeDay;
    }

    public void setCloseDay(String closeDay) {
        this.closeDay = closeDay;
    }

    public String getCloseTimeStr() {
        return closeTimeStr;
    }

    public void setCloseTimeStr(String closeTimeStr) {
        this.closeTimeStr = closeTimeStr;
    }
}
