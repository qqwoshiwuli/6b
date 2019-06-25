package com.sixbexchange.entity.bean;

/**
 * Created by 郭青枫 on 2019/6/3 0003.
 */

public class WsOrderBean {
    /**
     * amountUnit : USD
     * bs : b
     * continueTime : 10
     * contract : XBTUSD
     * currencyPairName : XBT永续
     * dealt_amount : 0
     * entrust_amount : 10
     * entrust_price : 888.88
     * entrust_time : Mon Jun 03 11:17:23 CST 2019
     * priceUnit : USD
     * requestTurns : 1
     * state : 0
     */

    private String amountUnit;
    private String bs;
    private String continueTime;
    private String contract;
    private String currencyPairName;
    private String dealt_amount;
    private String entrust_amount;
    private String entrust_price;
    private String entrust_time;
    private String priceUnit;
    private String requestTurns;
    private String state;
    private String exchange_oid;

    public String getAmountUnit() {
        return amountUnit;
    }

    public void setAmountUnit(String amountUnit) {
        this.amountUnit = amountUnit;
    }

    public String getBs() {
        return bs;
    }

    public void setBs(String bs) {
        this.bs = bs;
    }

    public String getContinueTime() {
        return continueTime;
    }

    public void setContinueTime(String continueTime) {
        this.continueTime = continueTime;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getCurrencyPairName() {
        return currencyPairName;
    }

    public void setCurrencyPairName(String currencyPairName) {
        this.currencyPairName = currencyPairName;
    }

    public String getDealt_amount() {
        return dealt_amount;
    }

    public void setDealt_amount(String dealt_amount) {
        this.dealt_amount = dealt_amount;
    }

    public String getEntrust_amount() {
        return entrust_amount;
    }

    public void setEntrust_amount(String entrust_amount) {
        this.entrust_amount = entrust_amount;
    }

    public String getEntrust_price() {
        return entrust_price;
    }

    public void setEntrust_price(String entrust_price) {
        this.entrust_price = entrust_price;
    }

    public String getEntrust_time() {
        return entrust_time;
    }

    public void setEntrust_time(String entrust_time) {
        this.entrust_time = entrust_time;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    public String getRequestTurns() {
        return requestTurns;
    }

    public void setRequestTurns(String requestTurns) {
        this.requestTurns = requestTurns;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getExchange_oid() {
        return exchange_oid;
    }

    public void setExchange_oid(String exchange_oid) {
        this.exchange_oid = exchange_oid;
    }
}
