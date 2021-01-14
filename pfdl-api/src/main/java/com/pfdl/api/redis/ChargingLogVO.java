package com.pfdl.api.redis;

import java.io.Serializable;

public class ChargingLogVO implements Serializable {
    private static final long serialVersionUID = -2906151485626782350L;
    /**主键*/
    private String id = ""; //远程启动
    /**累积电能表读数*/
    private String sumValue = "0";
    /**累积电能表时间*/
    private String sumValueTime = "0";
    /**电能表输出电流 */
    private String currentImport = "0";
    /**电能表输出电压*/
    private String voltageImport = "0";
    /** 预付金额 元*/
    private String payMoney = "";
    /** 剩余金额 元*/
    private String surplusMoney= "0";
    /**充电站编号*/
    private String stationNo = "";//远程启动
    /**充电单元编号*/
    private String chargerNo = "";//远程启动
    /**充电终端编号 枪号*/
    private String branchNo = "";//远程启动
    /**会话ID idtag*/
    private String sessionId = "";//远程启动
    /**用户ID*/
    private String cusId = "";//远程启动
    /**IC卡号*/
    private String cardNo = "";
    /** 开始时间 */
    private String startTime = "0";
    /**开始电能表读数*/
    private String startValue = "0";
    /**结束时间*/
    private String endTime = "0";
    /** 结束电能表读数 */
    private String endValue = "0";
    /** 结束原因 */
    private String stopReason = "";
     /** 电池额定容量 */
    private String batFixVol = "0";
    /**电池额定总电压*/
    private String batTotalV = "0";
    /**标称总容量*/
    private String totalVol = "0";
    /**充电次数（bms统计）*/
    private String chargerNum = "0";
    /**车工号(宇通vin)*/
    private String busJobNo = "";
    /**车号*/
    private String busNo = "";
    /**开始电池SOC*/
    private String startSoc = "0";
    /** 结束电池SOC */
    private String endSoc = "0";
    /**入库时间*/
    private String insTime = "";
    /**电池类型(1铅酸 2镍氢 3磷酸铁锂 4锰酸铁锂 5钴酸铁锂 6三元材料 7聚合物锂电池 8钛酸锂 0其它)*/
    private String batType = "0";
    /**充电时长（分钟）*/
    private String chargerLength = "0";
     /**充电电量 wh*/
    private String chargerValue = "0";
    /** 启动方式1服务器控制2主控手动3主控定时4终端手动启动5终端刷卡启动 */
    private String startType = "";//远程启动
    /** 交易类型0：充满为止, 1：充电 时间, 2：充电金额, 3：充电电量 */
    private String tradetype = "";//远程启动 开始充电时更新
    /** 充电记录上传来源0：启动充电上传记录，1：结束充电上传记录，2：桩停电上传记录，3：桩停电恢复继续充电，4：桩离线停止，5：桩离线后上线续充，6：桩费率切换上传记录，7：桩异常停止*/
    private String recordsource = "";//远程启动
    /** 车辆类型0：社会车辆 1：公交车辆 2：物流车辆 3：出租车辆 */
    private String cartype = "";
    /** 尖电量 */
    private String sharpPower = "0";
    /** 尖电费 */
    private String sharpCharge = "0";
    /** 尖电费单价 */
    private String sharpPrice = "0";
    /** 尖服务费单价 */
    private String sharpFeeprice = "0";
    /**尖服务费 */
    private String sharpFee = "0";
    /** 峰电量 */
    private String peakPower = "0";
    /** 峰电费 */
    private String peakCharge = "0";
    /** 峰电费单价 */
    private String peakPrice = "0";
    /** 峰服务费单价 */
    private String peakFeeprice = "0";
    /** 峰服务费 */
    private String peakFee = "0";
    /**平电量 */
    private String flatPower = "0";
    /** 平电费 */
    private String flatCharge = "0";
    /** 平电费单价 */
    private String flatPrice = "0";
    /**平服务费单价*/
    private String flatFeeprice = "0";
    /**平服务费*/
    private String flatFee = "0";
    /**谷电量 kwh*/
    private String valleyPower = "0";
    /**谷电费*/
    private String valleyCharge = "0";
    /**谷电费单价*/
    private String valleyPrice = "0";
    /**谷服务费单价*/
    private String valleyFeeprice = "0";
    /**谷服务费*/
    private String valleyFee = "0";
    /**总费用*/
    private String totalprice = "0";
    /**当前时间到上次充电时间内的运行公里数*/
    private String runDistance = "0";
    /**消耗的能量值*/
    private String extendVol = "0";
    /**null*/
    private String bst = "";
    /**错误码*/
    private String errorCode = "";
    /**总电费*/
    private String elecPrice = "0";
    /**总服务费*/
    private String serviceFee = "0";
    /**尖峰充电时长*/
    private String sharpLength = "0";
    /**高峰充电时长*/
    private String peakLength = "0";
    /**平峰充电时长*/
    private String flatLength = "0";
    /**谷峰充电时长*/
    private String valleyLength = "0";
    //充电状态
    private String chargeStatus =""; //0:开始充电 1:充电中 2:结束充电
    //场站名称
    private String stationName="";
    /**
     * token令牌
     */
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }

    public String getSurplusMoney() {
        return surplusMoney;
    }

    public void setSurplusMoney(String surplusMoney) {
        this.surplusMoney = surplusMoney;
    }

    public String getCurrentImport() {
        return currentImport;
    }

    public void setCurrentImport(String currentImport) {
        this.currentImport = currentImport;
    }

    public String getVoltageImport() {
        return voltageImport;
    }

    public void setVoltageImport(String voltageImport) {
        this.voltageImport = voltageImport;
    }

    public String getSumValueTime() {
        return sumValueTime;
    }

    public void setSumValueTime(String sumValueTime) {
        this.sumValueTime = sumValueTime;
    }

    public String getSumValue() {
        return sumValue;
    }

    public void setSumValue(String sumValue) {
        this.sumValue = sumValue;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(String chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStationNo() {
        return stationNo;
    }

    public void setStationNo(String stationNo) {
        this.stationNo = stationNo;
    }

    public String getChargerNo() {
        return chargerNo;
    }

    public void setChargerNo(String chargerNo) {
        this.chargerNo = chargerNo;
    }

    public String getBranchNo() {
        return branchNo;
    }

    public void setBranchNo(String branchNo) {
        this.branchNo = branchNo;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartValue() {
        return startValue;
    }

    public void setStartValue(String startValue) {
        this.startValue = startValue;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndValue() {
        return endValue;
    }

    public void setEndValue(String endValue) {
        this.endValue = endValue;
    }

    public String getStopReason() {
        return stopReason;
    }

    public void setStopReason(String stopReason) {
        this.stopReason = stopReason;
    }

    public String getBatFixVol() {
        return batFixVol;
    }

    public void setBatFixVol(String batFixVol) {
        this.batFixVol = batFixVol;
    }

    public String getBatTotalV() {
        return batTotalV;
    }

    public void setBatTotalV(String batTotalV) {
        this.batTotalV = batTotalV;
    }

    public String getTotalVol() {
        return totalVol;
    }

    public void setTotalVol(String totalVol) {
        this.totalVol = totalVol;
    }

    public String getChargerNum() {
        return chargerNum;
    }

    public void setChargerNum(String chargerNum) {
        this.chargerNum = chargerNum;
    }

    public String getBusJobNo() {
        return busJobNo;
    }

    public void setBusJobNo(String busJobNo) {
        this.busJobNo = busJobNo;
    }

    public String getBusNo() {
        return busNo;
    }

    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }

    public String getStartSoc() {
        return startSoc;
    }

    public void setStartSoc(String startSoc) {
        this.startSoc = startSoc;
    }

    public String getEndSoc() {
        return endSoc;
    }

    public void setEndSoc(String endSoc) {
        this.endSoc = endSoc;
    }

    public String getInsTime() {
        return insTime;
    }

    public void setInsTime(String insTime) {
        this.insTime = insTime;
    }

    public String getBatType() {
        return batType;
    }

    public void setBatType(String batType) {
        this.batType = batType;
    }

    public String getChargerLength() {
        return chargerLength;
    }

    public void setChargerLength(String chargerLength) {
        this.chargerLength = chargerLength;
    }

    public String getChargerValue() {
        return chargerValue;
    }

    public void setChargerValue(String chargerValue) {
        this.chargerValue = chargerValue;
    }

    public String getStartType() {
        return startType;
    }

    public void setStartType(String startType) {
        this.startType = startType;
    }

    public String getTradetype() {
        return tradetype;
    }

    public void setTradetype(String tradetype) {
        this.tradetype = tradetype;
    }

    public String getRecordsource() {
        return recordsource;
    }

    public void setRecordsource(String recordsource) {
        this.recordsource = recordsource;
    }

    public String getCartype() {
        return cartype;
    }

    public void setCartype(String cartype) {
        this.cartype = cartype;
    }

    public String getSharpPower() {
        return sharpPower;
    }

    public void setSharpPower(String sharpPower) {
        this.sharpPower = sharpPower;
    }

    public String getSharpCharge() {
        return sharpCharge;
    }

    public void setSharpCharge(String sharpCharge) {
        this.sharpCharge = sharpCharge;
    }

    public String getSharpPrice() {
        return sharpPrice;
    }

    public void setSharpPrice(String sharpPrice) {
        this.sharpPrice = sharpPrice;
    }

    public String getSharpFeeprice() {
        return sharpFeeprice;
    }

    public void setSharpFeeprice(String sharpFeeprice) {
        this.sharpFeeprice = sharpFeeprice;
    }

    public String getSharpFee() {
        return sharpFee;
    }

    public void setSharpFee(String sharpFee) {
        this.sharpFee = sharpFee;
    }

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

    public String getPeakPrice() {
        return peakPrice;
    }

    public void setPeakPrice(String peakPrice) {
        this.peakPrice = peakPrice;
    }

    public String getPeakFeeprice() {
        return peakFeeprice;
    }

    public void setPeakFeeprice(String peakFeeprice) {
        this.peakFeeprice = peakFeeprice;
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

    public String getFlatPrice() {
        return flatPrice;
    }

    public void setFlatPrice(String flatPrice) {
        this.flatPrice = flatPrice;
    }

    public String getFlatFeeprice() {
        return flatFeeprice;
    }

    public void setFlatFeeprice(String flatFeeprice) {
        this.flatFeeprice = flatFeeprice;
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

    public String getValleyPrice() {
        return valleyPrice;
    }

    public void setValleyPrice(String valleyPrice) {
        this.valleyPrice = valleyPrice;
    }

    public String getValleyFeeprice() {
        return valleyFeeprice;
    }

    public void setValleyFeeprice(String valleyFeeprice) {
        this.valleyFeeprice = valleyFeeprice;
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

    public String getRunDistance() {
        return runDistance;
    }

    public void setRunDistance(String runDistance) {
        this.runDistance = runDistance;
    }

    public String getExtendVol() {
        return extendVol;
    }

    public void setExtendVol(String extendVol) {
        this.extendVol = extendVol;
    }

    public String getBst() {
        return bst;
    }

    public void setBst(String bst) {
        this.bst = bst;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
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

    public String getSharpLength() {
        return sharpLength;
    }

    public void setSharpLength(String sharpLength) {
        this.sharpLength = sharpLength;
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
