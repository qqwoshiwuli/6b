package com.sixbexchange.entity.bean;

/**
 * Created by 郭青枫 on 2019/4/1 0001.
 */

public class ExchWalletBean {
    /**
     * btc : 61.108731499
     * imgUrl : https://bicoin.oss-cn-beijing.aliyuncs.com/wang/liu/6B.png
     * usdt : 250166.82048
     * exchange : 6bwallet
     * position : 1
     */

    private String btc;
    private String img_url;
    private String usdt;
    private String exchange;
    private String exchangeCn;
    private int position;

    public String getBtc() {
        return btc;
    }

    public void setBtc(String btc) {
        this.btc = btc;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getUsdt() {
        return usdt;
    }

    public void setUsdt(String usdt) {
        this.usdt = usdt;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getExchangeCn() {
        return exchangeCn;
    }

    public void setExchangeCn(String exchangeCn) {
        this.exchangeCn = exchangeCn;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
