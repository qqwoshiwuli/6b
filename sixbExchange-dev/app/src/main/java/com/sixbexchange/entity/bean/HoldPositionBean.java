package com.sixbexchange.entity.bean;

/**
 * Created by 郭青枫 on 2019/4/11 0011.
 */

public class HoldPositionBean {
    /**
     {
     "contract": "eth.usd.t",##交易所/币种.市场.t(本周).n(次周).q(季度) -----------------position/symbol-------------------------币种代码
     "totalAmount": 0,#总持有标的资产数量-------------------currentQty----------------正负判断方向
     "available": 0,#可用标的资产数量------------------if currentQty>0 则是currentQty-openOrderSellQty；else 则是currentQty-openOrderBuyQty-----------------可用
     "frozen": 0,#总已冻结标的资产数量------------------if currentQty>0 则是-openOrderSellQty；else 则是-openOrderBuyQty----------------冻结
     "averageOpenPrice": 112.544,#平均开仓均价--------------------avgCostPrice
     "realizedPl": 0,
     "contractFull": "eth.usd.2018-12-28",#币种 合约
     "type": "future",#spot 现货 future 期货------------------BM都是future
     "currentPrice": 128.2535,----------------------------lastPrice
     "usedMargin": 0,-------------------------maintMargin-----------------------------保证金
     "unrealizedPl": 0,
     "unrealized": 0,#未实现收益---------------------------------unrealisedPnl
     "standardSettlePrice": 112.544,#结算基准价--------------------breakEvenPrice
     "pl": 0,
     "availableXtc": 0,#可用换算后持币数量
     "frozenXtc": 0,#已冻结换算后持币数量
     "totalXtcAmount": 0,#换算后持币数量----------------------lastValue--------------------市值（XBT）
     "marketValue": 0,#标的资产市值--------------------markValue
     "marketValueDetail": {#标的资产市值细节
     "eth": 0
     },
     "valueCny": 0,#标的资产价值
     "unrealizedSettle": 0,#未结算收益-------------------------------------unrealisedPnl
     "unrealizedRate": null,#未实现收益率----------------------------------unrealisedPnlPcnt
     "realizedSettle": 0,#已结算收益率-------------------------------------realisedPnl
     "frozenPosition": 0,#已冻结用于当前持仓所需的标的资产数量（杠杆交易）
     "frozenOrder": null,#已冻结用于当前挂单所需的标的资产数量（杠杆交易）
     "totalAmountXtc": null,#换算后持币数量
     "totalAmountLong": 0,#持有多仓数量
     "availableLong": 0,#可用多仓数量
     "averageOpenPriceLong": 0,#多仓平均开仓均价
     "standardSettlePriceLong": 0,#多仓结算基准价
     "unrealizedSettleLong": 0,#多仓未结算收益
     "unrealizedLong": 0,#未实现多仓收益
     "unrealizedLongRate": null,#未实现多仓收益率
     "realizedSettleLong": null,
     "frozenPositionLong": 0,#已冻结多仓持仓保证金
     "totalAmountShort": 0,#可用空仓数量
     "availableShort": 0,#未实现空仓收益
     "averageOpenPriceShort": 0,#空仓平均开仓均价
     "standardSettlePriceShort": 0,#空仓结算基准价
     "unrealizedSettleShort": null,#空仓未结算收益
     "unrealizedShort": 0,#未实现空仓收益
     "unrealizedShortRate": null,#为实现空仓收益率
     "realizedSettleShort": null,
     "frozenPositionShort": 0,#已冻结空仓持仓保证金
     "detail": {#交易所返回原始数据
     "buy_price_avg": 0,
     "lever_rate": 20,
     "buy_available": 0,
     "sell_amount": 0,
     "sell_price_cost": 0,
     "exchange": "okef",
     "buy_price_cost": 0,
     "buy_amount": 0,
     "buy_profit_real": null,
     "sell_price_avg": 0,
     "sell_profit_real": null,
     "sell_available": 0
     },
     "extraInfo": {},
     "liquidationPrice": 0,#预估强平价格-----------------bankruptPrice
     "liquidationPriceRate": -1#预估强平率
     }
     */


    private String contract;
    private String totalAmount;
    private String available;
    private String frozen;
    private String averageOpenPrice;
    private String realizedPl;
    private String contractFull;
    private String type;
    private String currentPrice;
    private String usedMargin;
    private String unrealizedPl;
    private String unrealized;
    private String standardSettlePrice;
    private String pl;
    private String availableXtc;
    private String frozenXtc;
    private String totalXtcAmount;
    private String marketValue;
    private MarketValueDetailBean marketValueDetail;
    private String valueCny;
    private String unrealizedSettle;
    private String unrealizedRate;
    private String realizedSettle;
    private String frozenPosition;
    private String frozenOrder;
    private String totalAmountXtc;
    private String totalAmountLong;
    private String availableLong;
    private String averageOpenPriceLong;
    private String standardSettlePriceLong;
    private String unrealizedSettleLong;
    private String unrealizedLong;
    private String unrealizedLongRate;
    private String realizedSettleLong;
    private String frozenPositionLong;
    private String totalAmountShort;
    private String availableShort;
    private String averageOpenPriceShort;
    private String standardSettlePriceShort;
    private String unrealizedSettleShort;
    private String unrealizedShort;
    private String unrealizedShortRate;
    private String lever_rate;
    private String realizedSettleShort;
    private String frozenPositionShort;
    private Detail detail;
    private ExtraInfoBean extraInfo;
    private String liquidationPrice;
    private String liquidationPriceRate;
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLever_rate() {
        return lever_rate;
    }

    public void setLever_rate(String lever_rate) {
        this.lever_rate = lever_rate;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getFrozen() {
        return frozen;
    }

    public void setFrozen(String frozen) {
        this.frozen = frozen;
    }

    public String getAverageOpenPrice() {
        return averageOpenPrice;
    }

    public void setAverageOpenPrice(String averageOpenPrice) {
        this.averageOpenPrice = averageOpenPrice;
    }

    public String getRealizedPl() {
        return realizedPl;
    }

    public void setRealizedPl(String realizedPl) {
        this.realizedPl = realizedPl;
    }

    public String getContractFull() {
        return contractFull;
    }

    public void setContractFull(String contractFull) {
        this.contractFull = contractFull;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getUsedMargin() {
        return usedMargin;
    }

    public void setUsedMargin(String usedMargin) {
        this.usedMargin = usedMargin;
    }

    public String getUnrealizedPl() {
        return unrealizedPl;
    }

    public void setUnrealizedPl(String unrealizedPl) {
        this.unrealizedPl = unrealizedPl;
    }

    public String getUnrealized() {
        return unrealized;
    }

    public void setUnrealized(String unrealized) {
        this.unrealized = unrealized;
    }

    public String getStandardSettlePrice() {
        return standardSettlePrice;
    }

    public void setStandardSettlePrice(String standardSettlePrice) {
        this.standardSettlePrice = standardSettlePrice;
    }

    public String getPl() {
        return pl;
    }

    public void setPl(String pl) {
        this.pl = pl;
    }

    public String getAvailableXtc() {
        return availableXtc;
    }

    public void setAvailableXtc(String availableXtc) {
        this.availableXtc = availableXtc;
    }

    public String getFrozenXtc() {
        return frozenXtc;
    }

    public void setFrozenXtc(String frozenXtc) {
        this.frozenXtc = frozenXtc;
    }

    public String getTotalXtcAmount() {
        return totalXtcAmount;
    }

    public void setTotalXtcAmount(String totalXtcAmount) {
        this.totalXtcAmount = totalXtcAmount;
    }

    public String getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(String marketValue) {
        this.marketValue = marketValue;
    }

    public MarketValueDetailBean getMarketValueDetail() {
        return marketValueDetail;
    }

    public void setMarketValueDetail(MarketValueDetailBean marketValueDetail) {
        this.marketValueDetail = marketValueDetail;
    }

    public String getValueCny() {
        return valueCny;
    }

    public void setValueCny(String valueCny) {
        this.valueCny = valueCny;
    }

    public String getUnrealizedSettle() {
        return unrealizedSettle;
    }

    public void setUnrealizedSettle(String unrealizedSettle) {
        this.unrealizedSettle = unrealizedSettle;
    }

    public String getUnrealizedRate() {
        return unrealizedRate;
    }

    public void setUnrealizedRate(String unrealizedRate) {
        this.unrealizedRate = unrealizedRate;
    }

    public String getRealizedSettle() {
        return realizedSettle;
    }

    public void setRealizedSettle(String realizedSettle) {
        this.realizedSettle = realizedSettle;
    }

    public String getFrozenPosition() {
        return frozenPosition;
    }

    public void setFrozenPosition(String frozenPosition) {
        this.frozenPosition = frozenPosition;
    }

    public String getFrozenOrder() {
        return frozenOrder;
    }

    public void setFrozenOrder(String frozenOrder) {
        this.frozenOrder = frozenOrder;
    }

    public String getTotalAmountXtc() {
        return totalAmountXtc;
    }

    public void setTotalAmountXtc(String totalAmountXtc) {
        this.totalAmountXtc = totalAmountXtc;
    }

    public String getTotalAmountLong() {
        return totalAmountLong;
    }

    public void setTotalAmountLong(String totalAmountLong) {
        this.totalAmountLong = totalAmountLong;
    }

    public String getAvailableLong() {
        return availableLong;
    }

    public void setAvailableLong(String availableLong) {
        this.availableLong = availableLong;
    }

    public String getAverageOpenPriceLong() {
        return averageOpenPriceLong;
    }

    public void setAverageOpenPriceLong(String averageOpenPriceLong) {
        this.averageOpenPriceLong = averageOpenPriceLong;
    }

    public String getStandardSettlePriceLong() {
        return standardSettlePriceLong;
    }

    public void setStandardSettlePriceLong(String standardSettlePriceLong) {
        this.standardSettlePriceLong = standardSettlePriceLong;
    }

    public String getUnrealizedSettleLong() {
        return unrealizedSettleLong;
    }

    public void setUnrealizedSettleLong(String unrealizedSettleLong) {
        this.unrealizedSettleLong = unrealizedSettleLong;
    }

    public String getUnrealizedLong() {
        return unrealizedLong;
    }

    public void setUnrealizedLong(String unrealizedLong) {
        this.unrealizedLong = unrealizedLong;
    }

    public String getUnrealizedLongRate() {
        return unrealizedLongRate;
    }

    public void setUnrealizedLongRate(String unrealizedLongRate) {
        this.unrealizedLongRate = unrealizedLongRate;
    }

    public String getRealizedSettleLong() {
        return realizedSettleLong;
    }

    public void setRealizedSettleLong(String realizedSettleLong) {
        this.realizedSettleLong = realizedSettleLong;
    }

    public String getFrozenPositionLong() {
        return frozenPositionLong;
    }

    public void setFrozenPositionLong(String frozenPositionLong) {
        this.frozenPositionLong = frozenPositionLong;
    }

    public String getTotalAmountShort() {
        return totalAmountShort;
    }

    public void setTotalAmountShort(String totalAmountShort) {
        this.totalAmountShort = totalAmountShort;
    }

    public String getAvailableShort() {
        return availableShort;
    }

    public void setAvailableShort(String availableShort) {
        this.availableShort = availableShort;
    }

    public String getAverageOpenPriceShort() {
        return averageOpenPriceShort;
    }

    public void setAverageOpenPriceShort(String averageOpenPriceShort) {
        this.averageOpenPriceShort = averageOpenPriceShort;
    }

    public String getStandardSettlePriceShort() {
        return standardSettlePriceShort;
    }

    public void setStandardSettlePriceShort(String standardSettlePriceShort) {
        this.standardSettlePriceShort = standardSettlePriceShort;
    }

    public String getUnrealizedSettleShort() {
        return unrealizedSettleShort;
    }

    public void setUnrealizedSettleShort(String unrealizedSettleShort) {
        this.unrealizedSettleShort = unrealizedSettleShort;
    }

    public String getUnrealizedShort() {
        return unrealizedShort;
    }

    public void setUnrealizedShort(String unrealizedShort) {
        this.unrealizedShort = unrealizedShort;
    }

    public String getUnrealizedShortRate() {
        return unrealizedShortRate;
    }

    public void setUnrealizedShortRate(String unrealizedShortRate) {
        this.unrealizedShortRate = unrealizedShortRate;
    }

    public String getRealizedSettleShort() {
        return realizedSettleShort;
    }

    public void setRealizedSettleShort(String realizedSettleShort) {
        this.realizedSettleShort = realizedSettleShort;
    }

    public String getFrozenPositionShort() {
        return frozenPositionShort;
    }

    public void setFrozenPositionShort(String frozenPositionShort) {
        this.frozenPositionShort = frozenPositionShort;
    }



    public ExtraInfoBean getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(ExtraInfoBean extraInfo) {
        this.extraInfo = extraInfo;
    }

    public String getLiquidationPrice() {
        return liquidationPrice;
    }

    public void setLiquidationPrice(String liquidationPrice) {
        this.liquidationPrice = liquidationPrice;
    }

    public String getLiquidationPriceRate() {
        return liquidationPriceRate;
    }

    public void setLiquidationPriceRate(String liquidationPriceRate) {
        this.liquidationPriceRate = liquidationPriceRate;
    }

    public static class MarketValueDetailBean {
        /**
         * eth : 259.77187365197244
         */

        private String eth;

        public String getEth() {
            return eth;
        }

        public void setEth(String eth) {
            this.eth = eth;
        }
    }

    public static class Detail{

        /**
         * buy_price_avg : 0
         * lever_rate : 20
         * priceUnit : USD
         * buy_available : 0
         * leverageAvailable : 0
         * marginUnit : ETH
         * buy_amount : 0
         * buy_profit_real : 0
         * amountUnit : 张
         * sell_amount : 4
         * sell_price_cost : 170.23443303270437
         * exchange : okef
         * contractName : ETH
         * buy_price_cost : 0
         * sell_price_avg : 170.23443303270437
         * sell_profit_real : 0
         * sell_available : 3
         */

        private String buy_price_avg;
        private String lever_rate;
        private String priceUnit;
        private String buy_available;
        private int leverageAvailable;
        private String marginUnit;
        private String buy_amount;
        private String buy_profit_real;
        private String amountUnit;
        private String sell_amount;
        private String sell_price_cost;
        private String exchange;
        private String contractName;
        private String buy_price_cost;
        private String sell_price_avg;
        private String sell_profit_real;
        private String sell_available;
        private String leverage;
        private String value;
        private String valueUnit;
        private String levChange;

        public String getLevChange() {
            return levChange;
        }

        public void setLevChange(String levChange) {
            this.levChange = levChange;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getValueUnit() {
            return valueUnit;
        }

        public void setValueUnit(String valueUnit) {
            this.valueUnit = valueUnit;
        }

        public String getLeverage() {
            return leverage;
        }

        public void setLeverage(String leverage) {
            this.leverage = leverage;
        }

        public String getBuy_price_avg() {
            return buy_price_avg;
        }

        public void setBuy_price_avg(String buy_price_avg) {
            this.buy_price_avg = buy_price_avg;
        }

        public String getLever_rate() {
            return lever_rate;
        }

        public void setLever_rate(String lever_rate) {
            this.lever_rate = lever_rate;
        }

        public String getPriceUnit() {
            return priceUnit;
        }

        public void setPriceUnit(String priceUnit) {
            this.priceUnit = priceUnit;
        }

        public String getBuy_available() {
            return buy_available;
        }

        public void setBuy_available(String buy_available) {
            this.buy_available = buy_available;
        }

        public int getLeverageAvailable() {
            return leverageAvailable;
        }

        public void setLeverageAvailable(int leverageAvailable) {
            this.leverageAvailable = leverageAvailable;
        }

        public String getMarginUnit() {
            return marginUnit;
        }

        public void setMarginUnit(String marginUnit) {
            this.marginUnit = marginUnit;
        }

        public String getBuy_amount() {
            return buy_amount;
        }

        public void setBuy_amount(String buy_amount) {
            this.buy_amount = buy_amount;
        }

        public String getBuy_profit_real() {
            return buy_profit_real;
        }

        public void setBuy_profit_real(String buy_profit_real) {
            this.buy_profit_real = buy_profit_real;
        }

        public String getAmountUnit() {
            return amountUnit;
        }

        public void setAmountUnit(String amountUnit) {
            this.amountUnit = amountUnit;
        }

        public String getSell_amount() {
            return sell_amount;
        }

        public void setSell_amount(String sell_amount) {
            this.sell_amount = sell_amount;
        }

        public String getSell_price_cost() {
            return sell_price_cost;
        }

        public void setSell_price_cost(String sell_price_cost) {
            this.sell_price_cost = sell_price_cost;
        }

        public String getExchange() {
            return exchange;
        }

        public void setExchange(String exchange) {
            this.exchange = exchange;
        }

        public String getContractName() {
            return contractName;
        }

        public void setContractName(String contractName) {
            this.contractName = contractName;
        }

        public String getBuy_price_cost() {
            return buy_price_cost;
        }

        public void setBuy_price_cost(String buy_price_cost) {
            this.buy_price_cost = buy_price_cost;
        }

        public String getSell_price_avg() {
            return sell_price_avg;
        }

        public void setSell_price_avg(String sell_price_avg) {
            this.sell_price_avg = sell_price_avg;
        }

        public String getSell_profit_real() {
            return sell_profit_real;
        }

        public void setSell_profit_real(String sell_profit_real) {
            this.sell_profit_real = sell_profit_real;
        }

        public String getSell_available() {
            return sell_available;
        }

        public void setSell_available(String sell_available) {
            this.sell_available = sell_available;
        }
    }

    public static class ExtraInfoBean {
    }
}
