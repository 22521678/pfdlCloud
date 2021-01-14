package com.pfdl.api.jdbc;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 充电记录对象 bee_td_charginglog
 * 
 * @author zhaoyt
 * @date 2020-12-30
 */
public class BeeTdCharginglog
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** 充电站编号 */
    private String stationNo;

    /** 充电单元编号 */
    private String chargerNo;

    /** 充电终端编号 */
    private String branchNo;

    /** 会话ID */
    private String sessionId;

    /** 用户ID */
    private String cusId;

    /** IC卡号 */
    private String cardNo;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 开始电能表读数 */
    private long startValue = 0;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 结束电能表读数 */
    private long endValue = 0;

    /** 结束原因 */
    private String stopReason;

    /** 电池额定容量 */
    private long batFixVol = 0;

    /** 电池额定总电压 */
    private long batTotalV = 0;

    /** 标称总容量 */
    private long totalVol = 0;

    /** 充电次数（bms统计） */
    private long chargerNum = 0;

    /** 车工号(宇通vin) */
    private String busJobNo;

    /** 车号 */
    private long busNo = 0;

    /** 开始电池SOC */
    private long startSoc = 0;

    /** 结束电池SOC */
    private long endSoc = 0;

    /** 入库时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date insTime;

    /** 电池类型(1铅酸 2镍氢 3磷酸铁锂 4锰酸铁锂 5钴酸铁锂 6三元材料 7聚合物锂电池 8钛酸锂 0其它) */
    private long batType = 0;

    /** 充电时长（分钟） */
    private BigDecimal chargerLength;

    /** 充电电量 */
    private Double chargerValue  = 0.0;

    /** 启动方式1服务器控制2主控手动3主控定时4终端手动启动5终端刷卡启动 */
    private long startType = 0;

    /** 交易类型0：充满为止, 1：充电
时间, 2：充电金额, 3：充电电量 */
    private long tradetype = 0;

    /** 充电记录上传来源0：启动充电上传记录，1：结束充电上传记录，2：桩停电上传记录，3：桩停电恢复继续充电，4：桩离线停止，5：桩离线后上线续充，6：桩费率切换上传记录，7：桩异常停止 */
    private long recordsource = 0;

    /** 车辆类型0：社会车辆 1：公交车辆 2：物流车辆 3：出租车辆 */
    private long cartype = 0;

    /** 尖电量 */
    private Double sharpPower = 0.0;

    /** 尖电费 */
    private Double sharpCharge = 0.0;

    /** 尖电费单价 */
    private Double sharpPrice = 0.0;

    /** 尖服务费单价 */
    private Double sharpFeeprice = 0.0;

    /** 尖服务费 */
    private Double sharpFee = 0.0;

    /** 峰电量 */
    private Double peakPower = 0.0;

    /** 峰电费 */
    private Double peakCharge = 0.0;

    /** 峰电费单价 */
    private Double peakPrice = 0.0;

    /** 峰服务费单价 */
    private Double peakFeeprice = 0.0;

    /** 峰服务费 */
    private Double peakFee = 0.0;

    /** 平电量 */
    private Double flatPower = 0.0;

    /** 平电费 */
    private Double flatCharge = 0.0;

    /** 平电费单价 */
    private Double flatPrice = 0.0;

    /** 平服务费单价 */
    private Double flatFeeprice = 0.0;

    /** 平服务费 */
    private Double flatFee = 0.0;

    /** 谷电量 */
    private Double valleyPower = 0.0;

    /** 谷电费 */
    private Double valleyCharge = 0.0;

    /** 谷电费单价 */
    private Double valleyPrice = 0.0;

    /** 谷服务费单价 */
    private Double valleyFeeprice = 0.0;

    /** 谷服务费 */
    private Double valleyFee = 0.0;

    /** 总费用 */
    private Double totalprice = 0.0;

    /** 当前时间到上次充电时间内的运行公里数 */
    private Double runDistance = 0.0;

    /** 消耗的能量值 */
    private Double extendVol = 0.0;

    /** bst */
    private String bst;

    /** 错误码 */
    private String errorCode;

    /** 总电费 */
    private Double elecPrice = 0.0;

    /** 总服务费 */
    private Double serviceFee = 0.0;

    /** 尖峰充电时长 */
    private BigDecimal sharpLength = new BigDecimal(0);

    /** 高峰充电时长 */
    private BigDecimal peakLength= new BigDecimal(0);

    /** 平峰充电时长 */
    private BigDecimal flatLength= new BigDecimal(0);

    /** 谷峰充电时长 */
    private BigDecimal valleyLength= new BigDecimal(0);

    /** 发生时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;



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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public long getStartValue() {
        return startValue;
    }

    public void setStartValue(long startValue) {
        this.startValue = startValue;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public long getEndValue() {
        return endValue;
    }

    public void setEndValue(long endValue) {
        this.endValue = endValue;
    }

    public String getStopReason() {
        return stopReason;
    }

    public void setStopReason(String stopReason) {
        this.stopReason = stopReason;
    }

    public long getBatFixVol() {
        return batFixVol;
    }

    public void setBatFixVol(long batFixVol) {
        this.batFixVol = batFixVol;
    }

    public long getBatTotalV() {
        return batTotalV;
    }

    public void setBatTotalV(long batTotalV) {
        this.batTotalV = batTotalV;
    }

    public long getTotalVol() {
        return totalVol;
    }

    public void setTotalVol(long totalVol) {
        this.totalVol = totalVol;
    }

    public long getChargerNum() {
        return chargerNum;
    }

    public void setChargerNum(long chargerNum) {
        this.chargerNum = chargerNum;
    }

    public String getBusJobNo() {
        return busJobNo;
    }

    public void setBusJobNo(String busJobNo) {
        this.busJobNo = busJobNo;
    }

    public long getBusNo() {
        return busNo;
    }

    public void setBusNo(long busNo) {
        this.busNo = busNo;
    }

    public long getStartSoc() {
        return startSoc;
    }

    public void setStartSoc(long startSoc) {
        this.startSoc = startSoc;
    }

    public long getEndSoc() {
        return endSoc;
    }

    public void setEndSoc(long endSoc) {
        this.endSoc = endSoc;
    }

    public Date getInsTime() {
        return insTime;
    }

    public void setInsTime(Date insTime) {
        this.insTime = insTime;
    }

    public long getBatType() {
        return batType;
    }

    public void setBatType(long batType) {
        this.batType = batType;
    }

    public BigDecimal getChargerLength() {
        return chargerLength;
    }

    public void setChargerLength(BigDecimal chargerLength) {
        this.chargerLength = chargerLength;
    }

    public Double getChargerValue() {
        return chargerValue;
    }

    public void setChargerValue(Double chargerValue) {
        this.chargerValue = chargerValue;
    }

    public long getStartType() {
        return startType;
    }

    public void setStartType(long startType) {
        this.startType = startType;
    }

    public long getTradetype() {
        return tradetype;
    }

    public void setTradetype(long tradetype) {
        this.tradetype = tradetype;
    }

    public long getRecordsource() {
        return recordsource;
    }

    public void setRecordsource(long recordsource) {
        this.recordsource = recordsource;
    }

    public long getCartype() {
        return cartype;
    }

    public void setCartype(long cartype) {
        this.cartype = cartype;
    }

    public Double getSharpPower() {
        return sharpPower;
    }

    public void setSharpPower(Double sharpPower) {
        this.sharpPower = sharpPower;
    }

    public Double getSharpCharge() {
        return sharpCharge;
    }

    public void setSharpCharge(Double sharpCharge) {
        this.sharpCharge = sharpCharge;
    }

    public Double getSharpPrice() {
        return sharpPrice;
    }

    public void setSharpPrice(Double sharpPrice) {
        this.sharpPrice = sharpPrice;
    }

    public Double getSharpFeeprice() {
        return sharpFeeprice;
    }

    public void setSharpFeeprice(Double sharpFeeprice) {
        this.sharpFeeprice = sharpFeeprice;
    }

    public Double getSharpFee() {
        return sharpFee;
    }

    public void setSharpFee(Double sharpFee) {
        this.sharpFee = sharpFee;
    }

    public Double getPeakPower() {
        return peakPower;
    }

    public void setPeakPower(Double peakPower) {
        this.peakPower = peakPower;
    }

    public Double getPeakCharge() {
        return peakCharge;
    }

    public void setPeakCharge(Double peakCharge) {
        this.peakCharge = peakCharge;
    }

    public Double getPeakPrice() {
        return peakPrice;
    }

    public void setPeakPrice(Double peakPrice) {
        this.peakPrice = peakPrice;
    }

    public Double getPeakFeeprice() {
        return peakFeeprice;
    }

    public void setPeakFeeprice(Double peakFeeprice) {
        this.peakFeeprice = peakFeeprice;
    }

    public Double getPeakFee() {
        return peakFee;
    }

    public void setPeakFee(Double peakFee) {
        this.peakFee = peakFee;
    }

    public Double getFlatPower() {
        return flatPower;
    }

    public void setFlatPower(Double flatPower) {
        this.flatPower = flatPower;
    }

    public Double getFlatCharge() {
        return flatCharge;
    }

    public void setFlatCharge(Double flatCharge) {
        this.flatCharge = flatCharge;
    }

    public Double getFlatPrice() {
        return flatPrice;
    }

    public void setFlatPrice(Double flatPrice) {
        this.flatPrice = flatPrice;
    }

    public Double getFlatFeeprice() {
        return flatFeeprice;
    }

    public void setFlatFeeprice(Double flatFeeprice) {
        this.flatFeeprice = flatFeeprice;
    }

    public Double getFlatFee() {
        return flatFee;
    }

    public void setFlatFee(Double flatFee) {
        this.flatFee = flatFee;
    }

    public Double getValleyPower() {
        return valleyPower;
    }

    public void setValleyPower(Double valleyPower) {
        this.valleyPower = valleyPower;
    }

    public Double getValleyCharge() {
        return valleyCharge;
    }

    public void setValleyCharge(Double valleyCharge) {
        this.valleyCharge = valleyCharge;
    }

    public Double getValleyPrice() {
        return valleyPrice;
    }

    public void setValleyPrice(Double valleyPrice) {
        this.valleyPrice = valleyPrice;
    }

    public Double getValleyFeeprice() {
        return valleyFeeprice;
    }

    public void setValleyFeeprice(Double valleyFeeprice) {
        this.valleyFeeprice = valleyFeeprice;
    }

    public Double getValleyFee() {
        return valleyFee;
    }

    public void setValleyFee(Double valleyFee) {
        this.valleyFee = valleyFee;
    }

    public Double getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(Double totalprice) {
        this.totalprice = totalprice;
    }

    public Double getRunDistance() {
        return runDistance;
    }

    public void setRunDistance(Double runDistance) {
        this.runDistance = runDistance;
    }

    public Double getExtendVol() {
        return extendVol;
    }

    public void setExtendVol(Double extendVol) {
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

    public Double getElecPrice() {
        return elecPrice;
    }

    public void setElecPrice(Double elecPrice) {
        this.elecPrice = elecPrice;
    }

    public Double getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(Double serviceFee) {
        this.serviceFee = serviceFee;
    }

    public BigDecimal getSharpLength() {
        return sharpLength;
    }

    public void setSharpLength(BigDecimal sharpLength) {
        this.sharpLength = sharpLength;
    }

    public BigDecimal getPeakLength() {
        return peakLength;
    }

    public void setPeakLength(BigDecimal peakLength) {
        this.peakLength = peakLength;
    }

    public BigDecimal getFlatLength() {
        return flatLength;
    }

    public void setFlatLength(BigDecimal flatLength) {
        this.flatLength = flatLength;
    }

    public BigDecimal getValleyLength() {
        return valleyLength;
    }

    public void setValleyLength(BigDecimal valleyLength) {
        this.valleyLength = valleyLength;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }
    @Override
    public String toString() {
        return "BeeTdCharginglog{" +
                "id='" + id + '\'' +
                ", stationNo='" + stationNo + '\'' +
                ", chargerNo='" + chargerNo + '\'' +
                ", branchNo='" + branchNo + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", cusId='" + cusId + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", startTime=" + startTime +
                ", startValue=" + startValue +
                ", endTime=" + endTime +
                ", endValue=" + endValue +
                ", stopReason='" + stopReason + '\'' +
                ", batFixVol=" + batFixVol +
                ", batTotalV=" + batTotalV +
                ", totalVol=" + totalVol +
                ", chargerNum=" + chargerNum +
                ", busJobNo='" + busJobNo + '\'' +
                ", busNo=" + busNo +
                ", startSoc=" + startSoc +
                ", endSoc=" + endSoc +
                ", insTime=" + insTime +
                ", batType=" + batType +
                ", chargerLength=" + chargerLength +
                ", chargerValue=" + chargerValue +
                ", startType=" + startType +
                ", tradetype=" + tradetype +
                ", recordsource=" + recordsource +
                ", cartype=" + cartype +
                ", sharpPower=" + sharpPower +
                ", sharpCharge=" + sharpCharge +
                ", sharpPrice=" + sharpPrice +
                ", sharpFeeprice=" + sharpFeeprice +
                ", sharpFee=" + sharpFee +
                ", peakPower=" + peakPower +
                ", peakCharge=" + peakCharge +
                ", peakPrice=" + peakPrice +
                ", peakFeeprice=" + peakFeeprice +
                ", peakFee=" + peakFee +
                ", flatPower=" + flatPower +
                ", flatCharge=" + flatCharge +
                ", flatPrice=" + flatPrice +
                ", flatFeeprice=" + flatFeeprice +
                ", flatFee=" + flatFee +
                ", valleyPower=" + valleyPower +
                ", valleyCharge=" + valleyCharge +
                ", valleyPrice=" + valleyPrice +
                ", valleyFeeprice=" + valleyFeeprice +
                ", valleyFee=" + valleyFee +
                ", totalprice=" + totalprice +
                ", runDistance=" + runDistance +
                ", extendVol=" + extendVol +
                ", bst='" + bst + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", elecPrice=" + elecPrice +
                ", serviceFee=" + serviceFee +
                ", sharpLength=" + sharpLength +
                ", peakLength=" + peakLength +
                ", flatLength=" + flatLength +
                ", valleyLength=" + valleyLength +
                ", beginTime=" + beginTime +
                '}';
    }
}
