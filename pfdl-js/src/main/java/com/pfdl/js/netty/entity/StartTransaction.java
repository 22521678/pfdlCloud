package com.pfdl.js.netty.entity;

import java.io.Serializable;

/**
 * 开始充电信息
 * @Author zhaoyt
 * @Date 2020/12/21 10:36
 * @Version 1.0
 */
public class StartTransaction implements Serializable {
    private static final long serialVersionUID = -1155342551202732761L;
    /** U8 accountType 账户类型: 0x00:手机号 0x01:用户卡  0xFF:255无法识别   文档标红不知道是否可用:0x02:微信 0x03:支付宝*/
    private String accountType;
    /** U8 idTag[32+ 1] 用户标识 */
    private String idTag;
    /** U8 connectorId 充电枪号 */
    private String connectorId;
    /** U32 startTime 充电开始时间，单位：1s。 */
    private String startTime;
    /** U32 expectedStoptime 预估结束时间，单位：1s。无法预估时传0xFFFFFFFF*/
    private String expectedStoptime;
    /** U8 pointTransactionTag[26]	桩交易号（桩id_枪号_utc开始充电时间）*/
    private String pointTransactionTag;
    /** U8 chargeType 0x00:充满为止； 0x01:按时间充（单位：秒）； 0x02：按百分比冲 0x03:按金额充(单位：元) 0x04:按电量充 0x05:整站启动 */
    private String chargeType;
    /** U8 chargeParam[4] 0:无含义；1:U32，充电时长，单位：1s; 2:U32，百分比，1% ; 3:U32，金额，单位，0.01元; 4:U32, 电量，单位:0.01kwh */
    private String chargeParam;
    /** int meterStart	开始时的电表值 ，单位wh */
    private String meterStart;
    /** int reservationId	预约号 */
    private String reservationId;
    /** int payType	支付方式 */
    private String payType;
}
