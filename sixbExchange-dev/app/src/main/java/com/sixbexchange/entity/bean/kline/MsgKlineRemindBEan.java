package com.sixbexchange.entity.bean.kline;

/**
 * Created by 郭青枫 on 2018/7/28 0028.
 */

public class MsgKlineRemindBEan {
    /**
     * alarmType : 预警
     * closedDate : 1532557800000
     * content : 提醒时间 2018-7-26 06:30:00 请注意5分钟线短期有可能形成高点
     * createdDate : 1532557500000
     * ctime : 1532557888000
     * currencyType : eos_usdt
     * description : 5分钟线高点预警 提醒时间 2018-7-26 06:30:00 请注意5分钟线短期有可能形成高点
     * exchange : Okex
     * id : 94
     * isLowPrice : false
     * periodType : 5min
     * price : 0
     * symbol : EOS
     * title : 5分钟线高点预警
     * unit : USDT
     */

    private String alarmType;
    private long closedDate;
    private String content;
    private long createdDate;
    private long ctime;
    private String currencyType;
    private String description;
    private String exchange;
    private int id;
    private boolean isLowPrice;
    private String periodType;
    private Double price;
    private String symbol;
    private String title;
    private String unit;

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public long getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(long closedDate) {
        this.closedDate = closedDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIsLowPrice() {
        return isLowPrice;
    }

    public void setIsLowPrice(boolean isLowPrice) {
        this.isLowPrice = isLowPrice;
    }

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
