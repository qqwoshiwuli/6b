package com.sixbexchange.entity.bean;

public class TradeDetailOKBean {

    /**
     * instrument_id : BTC-USD-190628
     * underlying_index : BTC
     * quote_currency : USD
     * alias : this_week
     */

    private String instrument_id;
    private String underlying_index;
    private String quote_currency;
    private String alias;

    public String getInstrument_id() {
        return instrument_id;
    }

    public void setInstrument_id(String instrument_id) {
        this.instrument_id = instrument_id;
    }

    public String getUnderlying_index() {
        return underlying_index;
    }

    public void setUnderlying_index(String underlying_index) {
        this.underlying_index = underlying_index;
    }

    public String getQuote_currency() {
        return quote_currency;
    }

    public void setQuote_currency(String quote_currency) {
        this.quote_currency = quote_currency;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
