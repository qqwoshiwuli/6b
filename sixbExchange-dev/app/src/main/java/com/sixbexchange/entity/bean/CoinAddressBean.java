package com.sixbexchange.entity.bean;

/**
 * Created by 郭青枫 on 2019/4/4 0004.
 */

public class CoinAddressBean {
    /**
     * id : 1
     * userId : 1356581551567011840
     * coin : eth
     * addr : 0x8D9F89256B2902cBc0cf35cA8dF65fECce938505
     * remark : eth提币地址
     */

    private int id;
    private long userId;
    private String coin;
    private String addr;
    private String remark;
    private String memo;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
