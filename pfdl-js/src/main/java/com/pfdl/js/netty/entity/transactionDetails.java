package com.pfdl.js.netty.entity;

import java.io.Serializable;

/**
 * 交易详情内容存redis
 * @Author zhaoyt
 * @Date 2020/12/23 8:55
 * @Version 1.0
 */
public class transactionDetails implements Serializable {
    private static final long serialVersionUID = -3547695898901607765L;
    /** 交易号*/
    private String transactionId;
    /** 枪号*/
    private String connectorId;
    /** 当前时间*/
    private String timestamp;
    /** 根据 :meterValue;  算好的已充电量*/
    private String ChargedCapacity; //
    /** 已充时间 s*/
    private String chargedTime;
    /** 电池电量进度*/
    private String soc;
    /** 车辆 VIN*/
    private String vin;
    /** 充电费用*/
    private String chargingCharge;
    /** 服务费用*/
    private String serviceFee;
    /** 功率*/
    private String inputPower;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getConnectorId() {
        return connectorId;
    }

    public void setConnectorId(String connectorId) {
        this.connectorId = connectorId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getChargedCapacity() {
        return ChargedCapacity;
    }

    public void setChargedCapacity(String chargedCapacity) {
        ChargedCapacity = chargedCapacity;
    }

    public String getChargedTime() {
        return chargedTime;
    }

    public void setChargedTime(String chargedTime) {
        this.chargedTime = chargedTime;
    }

    public String getSoc() {
        return soc;
    }

    public void setSoc(String soc) {
        this.soc = soc;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getChargingCharge() {
        return chargingCharge;
    }

    public void setChargingCharge(String chargingCharge) {
        this.chargingCharge = chargingCharge;
    }

    public String getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getInputPower() {
        return inputPower;
    }

    public void setInputPower(String inputPower) {
        this.inputPower = inputPower;
    }
}
