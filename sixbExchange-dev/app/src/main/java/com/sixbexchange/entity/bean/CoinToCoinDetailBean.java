package com.sixbexchange.entity.bean;

public class CoinToCoinDetailBean {

    /**
     * BTC : {"eva":0,"lock":0,"btcEva":0,"avi":0,"img_url":"http://bicoin.oss-cn-beijing.aliyuncs.com/tokenicon/BTC.png","withdrawAvailable":0}
     * BCT : {"eva":0,"lock":0,"btcEva":0,"avi":0,"img_url":"http://bicoin.oss-cn-beijing.aliyuncs.com/tokenicon/BCT.png","withdrawAvailable":0}
     * BSV : {"eva":0,"lock":0,"btcEva":0,"avi":0,"img_url":"http://bicoin.oss-cn-beijing.aliyuncs.com/tokenicon/BSV.png","withdrawAvailable":0}
     * BCH : {"eva":0,"lock":0,"btcEva":0,"avi":0,"img_url":"http://bicoin.oss-cn-beijing.aliyuncs.com/tokenicon/BCH.png","withdrawAvailable":0}
     * XRP : {"eva":0,"lock":0,"btcEva":0,"avi":0,"img_url":"http://bicoin.oss-cn-beijing.aliyuncs.com/tokenicon/XRP.png","withdrawAvailable":0}
     * ETH : {"eva":0,"lock":0,"btcEva":0,"avi":0,"img_url":"http://bicoin.oss-cn-beijing.aliyuncs.com/tokenicon/ETH.png","withdrawAvailable":0}
     * EOS : {"eva":0,"lock":0,"btcEva":0,"avi":0,"img_url":"http://bicoin.oss-cn-beijing.aliyuncs.com/tokenicon/EOS.png","withdrawAvailable":0}
     * USDT : {"eva":0,"lock":0,"btcEva":0,"avi":0,"img_url":"http://bicoin.oss-cn-beijing.aliyuncs.com/tokenicon/USDT.png","withdrawAvailable":0}
     * LTC : {"eva":0,"lock":0,"btcEva":0,"avi":0,"img_url":"http://bicoin.oss-cn-beijing.aliyuncs.com/tokenicon/LTC.png","withdrawAvailable":0}
     */

    private BBean BTC;
    private BBean BCT;
    private BBean BSV;
    private BBean BCH;
    private BBean XRP;
    private BBean ETH;
    private BBean EOS;
    private BBean USDT;
    private BBean LTC;

    public BBean getBTC() {
        return BTC;
    }

    public void setBTC(BBean BTC) {
        this.BTC = BTC;
    }

    public BBean getBCT() {
        return BCT;
    }

    public void setBCT(BBean BCT) {
        this.BCT = BCT;
    }

    public BBean getBSV() {
        return BSV;
    }

    public void setBSV(BBean BSV) {
        this.BSV = BSV;
    }

    public BBean getBCH() {
        return BCH;
    }

    public void setBCH(BBean BCH) {
        this.BCH = BCH;
    }

    public BBean getXRP() {
        return XRP;
    }

    public void setXRP(BBean XRP) {
        this.XRP = XRP;
    }

    public BBean getETH() {
        return ETH;
    }

    public void setETH(BBean ETH) {
        this.ETH = ETH;
    }

    public BBean getEOS() {
        return EOS;
    }

    public void setEOS(BBean EOS) {
        this.EOS = EOS;
    }

    public BBean getUSDT() {
        return USDT;
    }

    public void setUSDT(BBean USDT) {
        this.USDT = USDT;
    }

    public BBean getLTC() {
        return LTC;
    }

    public void setLTC(BBean LTC) {
        this.LTC = LTC;
    }

    public static class BBean {
        /**
         * eva : 0
         * lock : 0
         * btcEva : 0
         * avi : 0
         * img_url : http://bicoin.oss-cn-beijing.aliyuncs.com/tokenicon/BTC.png
         * withdrawAvailable : 0
         */

        private int eva;
        private int lock;
        private int btcEva;
        private String avi;
        private String img_url;
        private int withdrawAvailable;

        public int getEva() {
            return eva;
        }

        public void setEva(int eva) {
            this.eva = eva;
        }

        public int getLock() {
            return lock;
        }

        public void setLock(int lock) {
            this.lock = lock;
        }

        public int getBtcEva() {
            return btcEva;
        }

        public void setBtcEva(int btcEva) {
            this.btcEva = btcEva;
        }

        public String getAvi() {
            return avi;
        }

        public void setAvi(String avi) {
            this.avi = avi;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public int getWithdrawAvailable() {
            return withdrawAvailable;
        }

        public void setWithdrawAvailable(int withdrawAvailable) {
            this.withdrawAvailable = withdrawAvailable;
        }
    }
}
