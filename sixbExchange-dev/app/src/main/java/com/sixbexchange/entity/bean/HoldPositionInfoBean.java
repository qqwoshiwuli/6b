package com.sixbexchange.entity.bean;

/**
 * Created by 郭青枫 on 2019/4/16 0016.
 */

public class HoldPositionInfoBean {
    private String amountUnit;
    private String priceUnit;
    private String marginUnitUnit;
    private String contractName;
    private int leverageAvaliable;

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getAmountUnit() {
        return amountUnit;
    }

    public void setAmountUnit(String amountUnit) {
        this.amountUnit = amountUnit;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    public String getMarginUnitUnit() {
        return marginUnitUnit;
    }

    public void setMarginUnitUnit(String marginUnitUnit) {
        this.marginUnitUnit = marginUnitUnit;
    }

    public int getLeverageAvaliable() {
        return leverageAvaliable;
    }

    public void setLeverageAvaliable(int leverageAvaliable) {
        this.leverageAvaliable = leverageAvaliable;
    }
}
