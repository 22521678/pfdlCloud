package com.pfdl.api.redis;

public class ChargeAndBraStatue {
    //场站id
    private String stationNo;
    //场站名称
    private String stationName;
    //sessionId
    private String sessionId;
    //交易号
    private String transactionId;
    //枪的状态值 0空闲 1使用 2故障
    private String branchStatue;
    //连接状态 0 拔出 1 连接
    private String connectStatue;
    //桩的类型
    private String ChargeType;

    public String getStationNo() {
        return stationNo;
    }

    public void setStationNo(String stationNo) {
        this.stationNo = stationNo;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getBranchStatue() {
        return branchStatue;
    }

    public void setBranchStatue(String branchStatue) {
        this.branchStatue = branchStatue;
    }

    public String getConnectStatue() {
        return connectStatue;
    }

    public void setConnectStatue(String connectStatue) {
        this.connectStatue = connectStatue;
    }

    public String getChargeType() {
        return ChargeType;
    }

    public void setChargeType(String chargeType) {
        ChargeType = chargeType;
    }
}
