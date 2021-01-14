package com.pfdl.api.jdbc;

import java.io.Serializable;

/**
 * @Author zhaoyt
 * @Date 2020/12/18 11:11
 * @Version 1.0
 */
public class RemoteTransaction implements Serializable {

    private static final long serialVersionUID = -1530088196737463837L;
    /** 桩号*/
    private String chargerNo;
    /**
     * token令牌
     */
    private String token;
    /**充电站编号*/
    private String stationNo;
    /** 用户ID*/
    private String cusId;
    /** U8 connectorId 充电枪号 */
    private String connectorId;
    /**U32 整数  4 byte 交易号 */
    private String transactionId;
    /** U8 idTag[32+ 1] 用户标识 远程启动使用的是手机号  session */
    private String idTag;
    /** U8 cardNumber[32+1] 用户卡号 */
    private String cardNumber;
    /** U8 phoneNumber[32+ 1] 用户手机号 */
    private String phoneNumber;
    /** U8 accountType 账户类型: 0x00:手机号 0x01:用户卡  0xFF:255无法识别   文档标红不知道是否可用:0x02:微信 0x03:支付宝*/
    private String accountType;
    /** U8 chargeType 0x00:充满为止； 0x01:按时间充（单位：秒）； 0x02：按百分比冲 0x03:按金额充(单位：元) 0x04:按电量充 0x05:整站启动 */
    private String chargeType;
    /** U8 chargeParam[4] 0:无含义；1:U32，充电时长，单位：1s; 2:U32，百分比，1% ; 3:U32，金额，单位，0.01元; 4:U32, 电量，单位:0.01kwh */
    private String chargeParam;
    /** U8 AccountStatus 0x00: 帐户有效;0x01: 帐户锁定; 0x02: 帐户过期; 0x03: 帐户无效; 0x04: 账户有效，但正在其他桩上充电; 0x05: 账户密码错误*/
    private String AccountStatus;
    /** U8 balance[4] S32，账户余额，单位：0.01元 桩根据用户余额进行可否开始本次充电判断，如果桩能够计算预计充电金额，使用该值与余额比较判断；如果桩无法预估充电金额，使用配置字段：“min_start_charge_balance”，默认值3000，单位0.01元。*/
    private String balance;
    /** U32 expiryDate 有效期,utc*/
    private String expiryDate;
    /** U8 parentIdTag[32+1] 用户组ID */
    private String parentIdTag;
    /** U8 overdraftBalanceForAccount[4]	账户透支额度，单位：0.01元 此设计为了实现运营计费的vip客户的透支功能。 */
    private String overdraftBalanceForAccount;
    /** U8 overdraftBalanceForCard[4]	卡透支额度，单位：0.01元 此设计为了实现运营计费的钱包卡的透支功能。 */
    private String overdraftBalanceForCard;
    /** U8is12V	启动参数12V/24V 取值： 0:不启用 1：12V 2:24V */
    private String voltage;
    /** U8 isAllPower	功率标识 0:不启用 1：全功率 2:半功率 */
    private String outputPower;
    /** U8 isNewGB	国标版本 0：不启用 1：新国标 2：老国标 */
    private String isNewGB;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStationNo() {
        return stationNo;
    }

    public void setStationNo(String stationNo) {
        this.stationNo = stationNo;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getChargerNo() {
        return chargerNo;
    }

    public void setChargerNo(String chargerNo) {
        this.chargerNo = chargerNo;
    }

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

    public String getIdTag() {
        return idTag;
    }

    public void setIdTag(String idTag) {
        this.idTag = idTag;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getChargeParam() {
        return chargeParam;
    }

    public void setChargeParam(String chargeParam) {
        this.chargeParam = chargeParam;
    }

    public String getAccountStatus() {
        return AccountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        AccountStatus = accountStatus;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getParentIdTag() {
        return parentIdTag;
    }

    public void setParentIdTag(String parentIdTag) {
        this.parentIdTag = parentIdTag;
    }

    public String getOverdraftBalanceForAccount() {
        return overdraftBalanceForAccount;
    }

    public void setOverdraftBalanceForAccount(String overdraftBalanceForAccount) {
        this.overdraftBalanceForAccount = overdraftBalanceForAccount;
    }

    public String getOverdraftBalanceForCard() {
        return overdraftBalanceForCard;
    }

    public void setOverdraftBalanceForCard(String overdraftBalanceForCard) {
        this.overdraftBalanceForCard = overdraftBalanceForCard;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public String getOutputPower() {
        return outputPower;
    }

    public void setOutputPower(String outputPower) {
        this.outputPower = outputPower;
    }

    public String getIsNewGB() {
        return isNewGB;
    }

    public void setIsNewGB(String isNewGB) {
        this.isNewGB = isNewGB;
    }
}
