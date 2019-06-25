package com.sixbexchange.entity.bean.kline;

import java.math.BigDecimal;

/**
 * Created by 郭青枫 on 2018/7/20 0020.
 */

public class KlineRateBean {
    /**
     * key : BTC_USDT
     * rate : 7462.18
     * symbol : BTC
     * time : 1532032140
     * unit : USDT
     */

    private String key;
    private BigDecimal rate;
    private String symbol;
    private long time;
    private String unit;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }



    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
