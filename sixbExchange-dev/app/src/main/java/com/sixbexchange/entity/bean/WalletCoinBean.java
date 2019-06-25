package com.sixbexchange.entity.bean;

/**
 * Created by 郭青枫 on 2019/4/1 0001.
 */

public class WalletCoinBean {
    /**
     * eva : 250112.2
     * lock : 60
     * btcEva : 61
     * avi : 1
     */

    private String eva;
    private String lock;
    private String btcEva;
    private String avi;
    private String coin;
    private String withdrawAvailable;
    private String img_url;

    public String getWithdrawAvailable() {
        return withdrawAvailable;
    }

    public void setWithdrawAvailable(String withdrawAvailable) {
        this.withdrawAvailable = withdrawAvailable;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getEva() {
        return eva;
    }

    public void setEva(String eva) {
        this.eva = eva;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public String getBtcEva() {
        return btcEva;
    }

    public void setBtcEva(String btcEva) {
        this.btcEva = btcEva;
    }

    public String getAvi() {
        return avi;
    }

    public void setAvi(String avi) {
        this.avi = avi;
    }
}
