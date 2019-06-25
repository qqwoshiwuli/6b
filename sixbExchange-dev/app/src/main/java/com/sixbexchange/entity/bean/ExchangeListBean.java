package com.sixbexchange.entity.bean;

/**
 * Created by 郭青枫 on 2019/4/12 0012.
 * {
 "discount":7,
 "discountInfo":null,
 "name":"OKEx交割合约",
 "code":"okef",
 "sumAmount":0,
 "availableAmount":0,
 "accId":35,
 "exchangeImg":"http://topcoin.oss-cn-hangzhou.aliyuncs.com/exchange/exchange_okex.png"
 }
 */

public class ExchangeListBean {
    String discount;
    String discountInfo;
    String name;
    String code;
    String sumAmount;
    String availableAmount;
    String accId;
    String exchangeImg;

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscountInfo() {
        return discountInfo;
    }

    public void setDiscountInfo(String discountInfo) {
        this.discountInfo = discountInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(String sumAmount) {
        this.sumAmount = sumAmount;
    }

    public String getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(String availableAmount) {
        this.availableAmount = availableAmount;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    public String getExchangeImg() {
        return exchangeImg;
    }

    public void setExchangeImg(String exchangeImg) {
        this.exchangeImg = exchangeImg;
    }
}
