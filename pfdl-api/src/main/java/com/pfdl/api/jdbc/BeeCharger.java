package com.pfdl.api.jdbc;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 充电桩信息对象 bee_charger
 * 
 * @author zhaoyt
 * @date 2020-12-10
 */
public class BeeCharger
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private String id;

    /** 充电单元编号 */
    private String chargerNo;

    /** 充电单元名称 */
    private String chargerName;

    /** 父ID(充电站ID) */
    private String stationNo;

    /** 添加时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date addtime;

    /** 描述 */
    private String note;

    /** 充电类型(0:直流、1:交流、2:直快充、3:直慢充、4:交流直充、5:交流慢充、6:直交流一体、7:无线充电、8:充放电、9:其他) */
    private Long chargerType;

    /** 最大功率(kw) */
    private Long totalpower;

    /** 终端白名单的最大版本号 */
    private Long whitelistEnabled;

    /** 主机白名单状态0停用1启用 */
    private Long statue;

    /** 操作状态0正在操作1成功2失败 */
    private Long operationStatue;

    /** 出厂编号 */
    private String productSno;

    /** 充电桩品牌 */
    private String ftyNo;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setChargerNo(String chargerNo) 
    {
        this.chargerNo = chargerNo;
    }

    public String getChargerNo() 
    {
        return chargerNo;
    }
    public void setChargerName(String chargerName) 
    {
        this.chargerName = chargerName;
    }

    public String getChargerName() 
    {
        return chargerName;
    }
    public void setStationNo(String stationNo) 
    {
        this.stationNo = stationNo;
    }

    public String getStationNo() 
    {
        return stationNo;
    }
    public void setAddtime(Date addtime) 
    {
        this.addtime = addtime;
    }

    public Date getAddtime() 
    {
        return addtime;
    }
    public void setNote(String note) 
    {
        this.note = note;
    }

    public String getNote() 
    {
        return note;
    }
    public void setChargerType(Long chargerType) 
    {
        this.chargerType = chargerType;
    }

    public Long getChargerType() 
    {
        return chargerType;
    }
    public void setTotalpower(Long totalpower) 
    {
        this.totalpower = totalpower;
    }

    public Long getTotalpower() 
    {
        return totalpower;
    }
    public void setWhitelistEnabled(Long whitelistEnabled) 
    {
        this.whitelistEnabled = whitelistEnabled;
    }

    public Long getWhitelistEnabled() 
    {
        return whitelistEnabled;
    }
    public void setStatue(Long statue) 
    {
        this.statue = statue;
    }

    public Long getStatue() 
    {
        return statue;
    }
    public void setOperationStatue(Long operationStatue) 
    {
        this.operationStatue = operationStatue;
    }

    public Long getOperationStatue() 
    {
        return operationStatue;
    }
    public void setProductSno(String productSno) 
    {
        this.productSno = productSno;
    }

    public String getProductSno()
    {
        return productSno;
    }

    public String getFtyNo() {
        return ftyNo;
    }

    public void setFtyNo(String ftyNo) {
        this.ftyNo = ftyNo;
    }

    @Override
    public String toString() {
        return "BeeCharger{" +
                "id='" + id + '\'' +
                ", chargerNo='" + chargerNo + '\'' +
                ", chargerName='" + chargerName + '\'' +
                ", stationNo='" + stationNo + '\'' +
                ", addtime=" + addtime +
                ", note='" + note + '\'' +
                ", chargerType=" + chargerType +
                ", totalpower=" + totalpower +
                ", whitelistEnabled=" + whitelistEnabled +
                ", statue=" + statue +
                ", operationStatue=" + operationStatue +
                ", productSno='" + productSno + '\'' +
                ", ftyNo='" + ftyNo + '\'' +
                '}';
    }
}
