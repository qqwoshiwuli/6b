package com.sixbexchange.entity.bean;

/**
 * Created by 郭青枫 on 2019/4/4 0004.
 */

public class WithdrawCoinBean {
    double amount;
    double min;
    double fee;
    String notice;

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }
}
