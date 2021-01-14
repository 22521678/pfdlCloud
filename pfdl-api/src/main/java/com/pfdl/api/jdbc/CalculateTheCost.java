package com.pfdl.api.jdbc;

/**
 * 费用计算
 * @Author zhaoyt
 * @Date 2020/12/24 14:05
 * @Version 1.0
 */
public class CalculateTheCost {

    /** 峰电量 w*/
    private String peakPower = "0";
    /** 峰电费 */
    private String peakCharge = "0";
    /** 峰服务费 */
    private String peakFee = "0";
    /**高峰充电时长*/
    private String peakLength = "0";

    /**平电量 w*/
    private String flatPower = "0";
    /** 平电费 */
    private String flatCharge = "0";
    /**平服务费*/
    private String flatFee = "0";
    /**平峰充电时长*/
    private String flatLength = "0";

    /**谷电量 w*/
    private String valleyPower = "0";
    /**谷电费*/
    private String valleyCharge = "0";
    /**谷服务费*/
    private String valleyFee = "0";
    /**谷峰充电时长*/
    private String valleyLength = "0";

    /**总费用*/
    private String totalprice = "0";
    /**总电费*/
    private String elecPrice = "0";
    /**总服务费*/
    private String serviceFee = "0";






    public String getPeakPower() {
        return peakPower;
    }

    public void setPeakPower(String peakPower) {
        this.peakPower = peakPower;
    }

    public String getPeakCharge() {
        return peakCharge;
    }

    public void setPeakCharge(String peakCharge) {
        this.peakCharge = peakCharge;
    }

    public String getPeakFee() {
        return peakFee;
    }

    public void setPeakFee(String peakFee) {
        this.peakFee = peakFee;
    }

    public String getFlatPower() {
        return flatPower;
    }

    public void setFlatPower(String flatPower) {
        this.flatPower = flatPower;
    }

    public String getFlatCharge() {
        return flatCharge;
    }

    public void setFlatCharge(String flatCharge) {
        this.flatCharge = flatCharge;
    }

    public String getFlatFee() {
        return flatFee;
    }

    public void setFlatFee(String flatFee) {
        this.flatFee = flatFee;
    }

    public String getValleyPower() {
        return valleyPower;
    }

    public void setValleyPower(String valleyPower) {
        this.valleyPower = valleyPower;
    }

    public String getValleyCharge() {
        return valleyCharge;
    }

    public void setValleyCharge(String valleyCharge) {
        this.valleyCharge = valleyCharge;
    }

    public String getValleyFee() {
        return valleyFee;
    }

    public void setValleyFee(String valleyFee) {
        this.valleyFee = valleyFee;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getElecPrice() {
        return elecPrice;
    }

    public void setElecPrice(String elecPrice) {
        this.elecPrice = elecPrice;
    }

    public String getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getPeakLength() {
        return peakLength;
    }

    public void setPeakLength(String peakLength) {
        this.peakLength = peakLength;
    }

    public String getFlatLength() {
        return flatLength;
    }

    public void setFlatLength(String flatLength) {
        this.flatLength = flatLength;
    }

    public String getValleyLength() {
        return valleyLength;
    }

    public void setValleyLength(String valleyLength) {
        this.valleyLength = valleyLength;
    }
}
