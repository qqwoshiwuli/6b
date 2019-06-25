package com.sixbexchange.entity.bean;

import java.util.List;

/**
 * Created by 郭青枫 on 2019/1/14 0014.
 */

public class WebSocketDepthBean {
    /**
     * onlyKey : Bitmex_XBT_USD
     * tick : {"asks":[{"count":1514852,"price":3537},{"count":415927,"price":3537.5},{"count":126153,"price":3538},{"count":116899,"price":3538.5},{"count":104387,"price":3539},{"count":72276,"price":3539.5},{"count":112726,"price":3540},{"count":107012,"price":3540.5},{"count":235699,"price":3541},{"count":85444,"price":3541.5}],"bids":[{"count":1483137,"price":3536.5},{"count":149189,"price":3536},{"count":609497,"price":3535.5},{"count":7342109,"price":3535},{"count":88089,"price":3534.5},{"count":175830,"price":3534},{"count":94001,"price":3533.5},{"count":750750,"price":3533},{"count":47613,"price":3532.5},{"count":157177,"price":3532}]}
     * timestamp : 1547454731939
     * type : 0
     * msgType : depth
     * "markPrice": 3548.37,
     * "fundingRate": -0.000051
     */

    List<DepthBean> asks;
    List<DepthBean> bids;

    public List<DepthBean> getAsks() {
        return asks;
    }

    public void setAsks(List<DepthBean> asks) {
        this.asks = asks;
    }

    public List<DepthBean> getBids() {
        return bids;
    }

    public void setBids(List<DepthBean> bids) {
        this.bids = bids;
    }
    private String contract;
    private String last;
    private String time;
    private String exchange_time;
    private String amount;
    private String volume;


    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getExchange_time() {
        return exchange_time;
    }

    public void setExchange_time(String exchange_time) {
        this.exchange_time = exchange_time;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }


}
