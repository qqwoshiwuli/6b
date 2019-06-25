package com.sixbexchange.entity.bean;

import java.util.List;

public class selectExchhuobiBeans {
    /**
     * data : [{"id":null,"baseCurrency":"btc","quoteCurrency":"usdt","pricePrecision":2,"amountPrecision":4,"symbolPartition":"main","symbol":"btcusdt"},{"id":null,"baseCurrency":"eos","quoteCurrency":"usdt","pricePrecision":4,"amountPrecision":2,"symbolPartition":"main","symbol":"eosusdt"},{"id":null,"baseCurrency":"eth","quoteCurrency":"usdt","pricePrecision":2,"amountPrecision":2,"symbolPartition":"main","symbol":"ethusdt"},{"id":null,"baseCurrency":"ltc","quoteCurrency":"usdt","pricePrecision":2,"amountPrecision":4,"symbolPartition":"main","symbol":"ltcusdt"},{"id":null,"baseCurrency":"bch","quoteCurrency":"usdt","pricePrecision":2,"amountPrecision":4,"symbolPartition":"main","symbol":"bchusdt"},{"id":null,"baseCurrency":"xrp","quoteCurrency":"usdt","pricePrecision":4,"amountPrecision":2,"symbolPartition":"main","symbol":"xrpusdt"},{"id":null,"baseCurrency":"bsv","quoteCurrency":"usdt","pricePrecision":4,"amountPrecision":2,"symbolPartition":"main","symbol":"bsvusdt"},{"id":null,"baseCurrency":"eos","quoteCurrency":"btc","pricePrecision":8,"amountPrecision":2,"symbolPartition":"main","symbol":"eosbtc"},{"id":null,"baseCurrency":"eth","quoteCurrency":"btc","pricePrecision":6,"amountPrecision":4,"symbolPartition":"main","symbol":"ethbtc"},{"id":null,"baseCurrency":"ltc","quoteCurrency":"btc","pricePrecision":6,"amountPrecision":4,"symbolPartition":"main","symbol":"ltcbtc"},{"id":null,"baseCurrency":"bch","quoteCurrency":"btc","pricePrecision":6,"amountPrecision":4,"symbolPartition":"main","symbol":"bchbtc"},{"id":null,"baseCurrency":"xrp","quoteCurrency":"btc","pricePrecision":8,"amountPrecision":0,"symbolPartition":"main","symbol":"xrpbtc"},{"id":null,"baseCurrency":"bsv","quoteCurrency":"btc","pricePrecision":6,"amountPrecision":4,"symbolPartition":"main","symbol":"bsvbtc"}]
     * type : 主流
     */
    private String type;
    private List<HuobiDataBean> data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<HuobiDataBean> getData() {
        return data;
    }

    public void setData(List<HuobiDataBean> data) {
        this.data = data;
    }

    public static class HuobiDataBean {
        /**
         * id : null
         * baseCurrency : btc
         * quoteCurrency : usdt
         * pricePrecision : 2
         * amountPrecision : 4
         * symbolPartition : main
         * symbol : btcusdt
         */

        private String baseCurrency;
        private String quoteCurrency;
        private int pricePrecision;
        private int amountPrecision;
        private String symbolPartition;
        private String symbol;

        public String getBaseCurrency() {
            return baseCurrency;
        }

        public void setBaseCurrency(String baseCurrency) {
            this.baseCurrency = baseCurrency;
        }

        public String getQuoteCurrency() {
            return quoteCurrency;
        }

        public void setQuoteCurrency(String quoteCurrency) {
            this.quoteCurrency = quoteCurrency;
        }

        public int getPricePrecision() {
            return pricePrecision;
        }

        public void setPricePrecision(int pricePrecision) {
            this.pricePrecision = pricePrecision;
        }

        public int getAmountPrecision() {
            return amountPrecision;
        }

        public void setAmountPrecision(int amountPrecision) {
            this.amountPrecision = amountPrecision;
        }

        public String getSymbolPartition() {
            return symbolPartition;
        }

        public void setSymbolPartition(String symbolPartition) {
            this.symbolPartition = symbolPartition;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }
    }
}

