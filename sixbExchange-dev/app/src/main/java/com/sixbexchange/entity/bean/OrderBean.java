package com.sixbexchange.entity.bean;

/**
 * Created by 郭青枫 on 2019/4/11 0011.
 */

public class OrderBean {
    /*
    *
    {
        "account": "okef/liujianshu",#交易所/账户名-----------------BITMEX
        "average_dealt_price": 144.69,#平均成交价-------------avgPx
        "bs": "b",#b 买 s 卖----------------------side，buy买 sell卖
        "client_oid": "okef/eth.usd.q-20181224-105448-nzvllradewb82qx0k6v27e5on8f",#用户定义的订单号
        "comment": null,#订单附加信息
        "commission": 0.00001727831916511162,#手续费-----------------
        "contract": "okef/eth.usd.q",#交易所/币种.市场.t(本周).n(次周).q(季度)----------------symbol
        "dealt_amount": 1,#成交数量-----------------cumQty
        "entrust_amount": 1,#委托数量------------------orderQty
        "entrust_price": 145,#委托价格-----------------price
        "entrust_time": "2018-12-24T10:54:48+08:00",#委托时间---------------------timestamp
        "exchange_oid": "okef/eth.usd.q-2024019653510144",#交易所生成订单号-----------rderID
        "last_dealt_amount": 1,#最近一次成交数量
        "canceled_time": null,#订单在交易所成功撤单的时间,交易所返回的数据不包含这项则为空
        "closed_time": null,#订单在交易所变为end状态的时间,交易所返回的数据不包含这项则为空
        "ots_closed_time": "2018-12-24T10:54:49.133271+08:00",#Onetoken系统检测到订单状态变为end状态的时间
        "last_update": "2018-12-24T10:54:49.133271+08:00",#订单最近一次在OneToken系统更新的时间
        "exchange_update": null,#订单最近一次在交易所更新的时间,交易所返回的数据不包含这项则为空
        "options": [
            {}
        ],
        "status": "dealt",-----------------new（未成交）Partly-Filled（部分成交）Filled（已成交）Canceled（已撤销）
        "tags": "{"type":"open"}"
    }
    **/
    String account;
    String average_dealt_price;
    String bs;
    String client_oid;
    String comment;
    String contract;
    String dealt_amount;
    String entrust_amount;
    String entrust_price;
    String entrust_time;
    String exchange_oid;
    String last_dealt_amount;
    String canceled_time;
    String closed_time;
    String ots_closed_time;
    String last_update;
    String exchange_update;
    String status;
    String commission;
    String frozen_margin;
    /**
     * tags : {"type":"open"}
     */

    private String tags;

    public String getFrozen_margin() {
        return frozen_margin;
    }

    public void setFrozen_margin(String frozen_margin) {
        this.frozen_margin = frozen_margin;
    }

    public String getEntrust_amount() {
        return entrust_amount;
    }

    public void setEntrust_amount(String entrust_amount) {
        this.entrust_amount = entrust_amount;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAverage_dealt_price() {
        return average_dealt_price;
    }

    public void setAverage_dealt_price(String average_dealt_price) {
        this.average_dealt_price = average_dealt_price;
    }

    public String getBs() {
        return bs;
    }

    public void setBs(String bs) {
        this.bs = bs;
    }

    public String getClient_oid() {
        return client_oid;
    }

    public void setClient_oid(String client_oid) {
        this.client_oid = client_oid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getDealt_amount() {
        return dealt_amount;
    }

    public void setDealt_amount(String dealt_amount) {
        this.dealt_amount = dealt_amount;
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

    public String getExchange_oid() {
        return exchange_oid;
    }

    public void setExchange_oid(String exchange_oid) {
        this.exchange_oid = exchange_oid;
    }

    public String getLast_dealt_amount() {
        return last_dealt_amount;
    }

    public void setLast_dealt_amount(String last_dealt_amount) {
        this.last_dealt_amount = last_dealt_amount;
    }

    public String getCanceled_time() {
        return canceled_time;
    }

    public void setCanceled_time(String canceled_time) {
        this.canceled_time = canceled_time;
    }

    public String getClosed_time() {
        return closed_time;
    }

    public void setClosed_time(String closed_time) {
        this.closed_time = closed_time;
    }

    public String getOts_closed_time() {
        return ots_closed_time;
    }

    public void setOts_closed_time(String ots_closed_time) {
        this.ots_closed_time = ots_closed_time;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public String getExchange_update() {
        return exchange_update;
    }

    public void setExchange_update(String exchange_update) {
        this.exchange_update = exchange_update;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }


}
