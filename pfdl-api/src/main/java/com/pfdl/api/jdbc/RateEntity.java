package com.pfdl.api.jdbc;

import java.io.Serializable;

/**
 * 费率查询实体
 * @Author zhaoyt
 * @Date 2020/12/23 11:30
 * @Version 1.0
 */
public class RateEntity implements Serializable {
    private static final long serialVersionUID = 3840581944929261026L;
    /**
     * 开始时间 分钟/60
     */
    private String beginTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 状态 尖峰:3 平:2 谷:1
     */
    private String status;
    /**
     * 电价费用
     */
    private String price;
    /**
     * 服务费
     */
    private String fee;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }
}
