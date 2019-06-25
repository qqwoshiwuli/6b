package com.sixbexchange.entity.bean;

/**
 * Created by 郭青枫 on 2019/4/16 0016.
 * 持仓页面下拉框 选择数据
 */

public class ExchSelectPositionBean {

    String name;
    String currency;
    String currencyPair;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrencyPair() {
        return currencyPair;
    }

    public void setCurrencyPair(String currencyPair) {
        this.currencyPair = currencyPair;
    }
}
