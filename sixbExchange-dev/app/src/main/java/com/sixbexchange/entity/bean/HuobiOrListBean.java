package com.sixbexchange.entity.bean;

public class HuobiOrListBean {

    /**
     * id : null
     * userId : null
     * orderId : null
     * apiKey : null
     * fieldAmount : 0.102
     * fieldCashAmount : 814.09566
     * fieldFees : 0.81409566
     * symbol : btcusdt
     * type : 2
     * createdAt : 1558017473000
     * canceledAt : null
     * cTime : null
     * requestAmount : null
     */

    private Object id;
    private Object userId;
    private Object orderId;
    private Object apiKey;
    private double fieldAmount;
    private double fieldCashAmount;
    private double fieldFees;
    private String symbol;
    private String type;
    private long createdAt;
    private Object canceledAt;
    private Object cTime;
    private Object requestAmount;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public Object getOrderId() {
        return orderId;
    }

    public void setOrderId(Object orderId) {
        this.orderId = orderId;
    }

    public Object getApiKey() {
        return apiKey;
    }

    public void setApiKey(Object apiKey) {
        this.apiKey = apiKey;
    }

    public double getFieldAmount() {
        return fieldAmount;
    }

    public void setFieldAmount(double fieldAmount) {
        this.fieldAmount = fieldAmount;
    }

    public double getFieldCashAmount() {
        return fieldCashAmount;
    }

    public void setFieldCashAmount(double fieldCashAmount) {
        this.fieldCashAmount = fieldCashAmount;
    }

    public double getFieldFees() {
        return fieldFees;
    }

    public void setFieldFees(double fieldFees) {
        this.fieldFees = fieldFees;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public Object getCanceledAt() {
        return canceledAt;
    }

    public void setCanceledAt(Object canceledAt) {
        this.canceledAt = canceledAt;
    }

    public Object getCTime() {
        return cTime;
    }

    public void setCTime(Object cTime) {
        this.cTime = cTime;
    }

    public Object getRequestAmount() {
        return requestAmount;
    }

    public void setRequestAmount(Object requestAmount) {
        this.requestAmount = requestAmount;
    }
}
