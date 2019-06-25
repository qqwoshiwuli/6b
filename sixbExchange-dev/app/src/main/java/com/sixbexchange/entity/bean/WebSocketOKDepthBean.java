package com.sixbexchange.entity.bean;

import java.util.List;

public class WebSocketOKDepthBean {

    /**
     * table : futures/depth5
     * data : [{"asks":[["11300.02","247","0","10"],["11300.03","491","0","1"],["11300.04","509","0","1"],["11300.05","44","0","1"],["11300.08","176","0","1"]],"bids":[["11300","284","0","2"],["11298.1","114","0","2"],["11297.71","108","0","1"],["11297.23","2","0","1"],["11297.08","1","0","1"]],"instrument_id":"BTC-USD-190927","timestamp":"2019-06-23T13:34:26.524Z"}]
     */

    private String table;
    private List<DataBean> data;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * asks : [["11300.02","247","0","10"],["11300.03","491","0","1"],["11300.04","509","0","1"],["11300.05","44","0","1"],["11300.08","176","0","1"]]
         * bids : [["11300","284","0","2"],["11298.1","114","0","2"],["11297.71","108","0","1"],["11297.23","2","0","1"],["11297.08","1","0","1"]]
         * instrument_id : BTC-USD-190927
         * timestamp : 2019-06-23T13:34:26.524Z
         */

        private String instrument_id;
        private String timestamp;
        private List<List<String>> asks;
        private List<List<String>> bids;
        private String last;
        private String high_24h;
        private String low_24h;
        private String open_24h;

        public String getLast() {
            return last;
        }

        public void setLast(String last) {
            this.last = last;
        }

        public String getHigh_24h() {
            return high_24h;
        }

        public void setHigh_24h(String high_24h) {
            this.high_24h = high_24h;
        }

        public String getLow_24h() {
            return low_24h;
        }

        public void setLow_24h(String low_24h) {
            this.low_24h = low_24h;
        }

        public String getOpen_24h() {
            return open_24h;
        }

        public void setOpen_24h(String open_24h) {
            this.open_24h = open_24h;
        }

        public String getInstrument_id() {
            return instrument_id;
        }

        public void setInstrument_id(String instrument_id) {
            this.instrument_id = instrument_id;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public List<List<String>> getAsks() {
            return asks;
        }

        public void setAsks(List<List<String>> asks) {
            this.asks = asks;
        }

        public List<List<String>> getBids() {
            return bids;
        }

        public void setBids(List<List<String>> bids) {
            this.bids = bids;
        }
    }
}
