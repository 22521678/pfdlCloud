package com.pfdl.js.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.pfdl.api.jdbc.RemoteTransaction;
import com.pfdl.api.netty.PileIdentity;
import com.pfdl.api.redis.ChargeAndBraStatue;
import com.pfdl.common.netty.JSConstants;
import com.pfdl.common.utils.spring.SpringUtils;
import com.pfdl.js.config.redis.RedisCache;
import com.pfdl.js.netty.entity.StartupStatus;
import com.pfdl.js.netty.server.NettyServerHandler;
import com.pfdl.js.netty.utils.CnovertSystem;
import com.pfdl.js.netty.utils.JSUtils;
import com.pfdl.js.service.IRemoteTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 远程操作充电桩业务层实现
 * @Author zhaoyt
 * @Date 2020/12/18 13:38
 * @Version 1.0
 */
@Service
public class RemoteTransactionServiceImpl implements IRemoteTransactionService {
    protected final Logger logger = LoggerFactory.getLogger(RemoteTransactionServiceImpl.class);
    @Autowired
    private NettyServerHandler nettyServerHandler;
    @Override
    public String RemoteStartTransactionHex(RemoteTransaction remoteTransaction) {
        PileIdentity pileIdentity = CnovertSystem.getValueByDestIdNumber(nettyServerHandler.pileMap, remoteTransaction.getChargerNo());
//        /**获取交易号存储全局变量transactionMap中*/
//        nettyServerHandler.transactionMap.put(remoteTransaction.getTransactionId(),remoteTransaction.getIdTag());
        String dataBodyHex = ObjToHex(remoteTransaction);
        /** 远程启动充电功能码  */
        String packetFunctionCode = "02";
        /** 封包校验CRC 后进行发送 */
        String returnPackage = JSUtils.packaging(dataBodyHex,packetFunctionCode,pileIdentity.getHexDestId());
        byte[] bytes = CnovertSystem.hexStringToBytes(returnPackage);
        StartupStatus ss = new StartupStatus("remote", "0", null);
        String cabStr = SpringUtils.getBean(RedisCache.class).getCacheObject(remoteTransaction.getChargerNo()+"_"+Integer.parseInt(remoteTransaction.getConnectorId())+JSConstants.JS_KEY_STATUE);//获取当前订单信息
        ChargeAndBraStatue cab = JSON.parseObject(cabStr, ChargeAndBraStatue.class);
        nettyServerHandler.remoteStartStatus.put(cab.getTransactionId(),ss);
        nettyServerHandler.writeMessage(pileIdentity.getChannelId(),bytes);
        logger.info("远程【启动】充电指令下发给 【客户端:[" + pileIdentity.getDestIdNumber() + "]】" + " -指令内容 ]:" + CnovertSystem.parseHexStrToHex(returnPackage) );
        for (int i = 0 ; i < 90;i++){
            if(nettyServerHandler.remoteStartStatus.containsKey(cab.getTransactionId())){
                /** 远程启动是否成功*/
                StartupStatus startupStatus = nettyServerHandler.remoteStartStatus.get(cab.getTransactionId());
                if(StrUtil.isNotEmpty(startupStatus.getStatus()) && "1".equals(startupStatus.getStatus()) && "remote".equals(startupStatus.getStartForm())){
                    logger.info("远程【启动】     - 成功 ....");
                    return "0";
                }
            }
            try {
                Thread.sleep(300);     //设置暂停的时间，300=0.3秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.info("远程【启动】     - 失败 ....");
        return "1";
    }

    @Override
    public String RemoteStopTransaction(RemoteTransaction remoteTransaction) {
        PileIdentity pileIdentity = CnovertSystem.getValueByDestIdNumber(nettyServerHandler.pileMap, remoteTransaction.getChargerNo());
        /** 停止远程充电桩功能码  */
        String packetFunctionCode = "03";
        //根据桩_枪获取交易号
        String cabStr = SpringUtils.getBean(RedisCache.class).getCacheObject(remoteTransaction.getChargerNo()+"_"+remoteTransaction.getConnectorId()+JSConstants.JS_KEY_STATUE);//获取当前订单信息
        ChargeAndBraStatue cab = JSON.parseObject(cabStr, ChargeAndBraStatue.class);
        /** U32 transactionId	交易号，U32. 将对象转换成 HEX*/
        String dataBodyHex = JSUtils.publicIntToHex(cab.getTransactionId(),4);  //需要转16进制
        String returnPackage = JSUtils.packaging(dataBodyHex,packetFunctionCode,pileIdentity.getHexDestId());
        byte[] bytes = CnovertSystem.hexStringToBytes(returnPackage);
        if(nettyServerHandler.remoteStartStatus.containsKey(cab.getTransactionId())){
            /** 设置为远程停止*/
            StartupStatus stopStatus = nettyServerHandler.remoteStartStatus.get(cab.getTransactionId());
            stopStatus.setStopForm("remote");
            nettyServerHandler.remoteStartStatus.put(cab.getTransactionId(),stopStatus);
        }
        nettyServerHandler.writeMessage(pileIdentity.getChannelId(),bytes);
        logger.info("远程【停止】充电指令下发给 【客户端:[" + pileIdentity.getDestIdNumber() + "]】" + " -指令内容 ]:" + CnovertSystem.parseHexStrToHex(returnPackage) );
        for (int i = 0 ; i < 30;i++){
            if(nettyServerHandler.remoteStartStatus.containsKey(cab.getTransactionId())){
                /** 设置为远程启动*/
                StartupStatus stopStatus = nettyServerHandler.remoteStartStatus.get(cab.getTransactionId());
                if(StrUtil.isNotEmpty(stopStatus.getStatus()) && "2".equals(stopStatus.getStatus()) && "remote".equals(stopStatus.getStopForm())){
                    /**销毁MAP*/
                    nettyServerHandler.remoteStartStatus.remove(cab.getTransactionId());
                    return "0";
                }
            }
            try {
                Thread.sleep(500);     //设置暂停的时间，500=0.5秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "1";
    }

    private String ObjToHex(RemoteTransaction rt){
        /** 将对象转换成 HEX */
        /** U8 connectorId	充电枪号 (1-100)*/
        String connectorId = JSUtils.publicIntToHex(rt.getConnectorId(), 1);
        /** U8 idTag[32+ 1] 用户标识 */
        String idTag = JSUtils.publicStringToASCIIHex(rt.getPhoneNumber(), JSConstants.idTagLength);
        System.out.println("idTag  : " +idTag );
        /** U8 cardNumber[32+1] 用户卡号 */
        String cardNumber = JSUtils.publicIntToHex(rt.getCardNumber(), JSConstants.cardNumberLength);
        /** U8 phoneNumber[32+ 1] 用户手机号 */
        String phoneNumber = JSUtils.publicStringToASCIIHex(rt.getPhoneNumber(), JSConstants.phoneNumberLength);
        /** U8 accountType 账户类型: 0x00:手机号 0x01:用户卡  0xFF:255无法识别   文档标红不知道是否可用:0x02:微信 0x03:支付宝*/
        String accountType = JSUtils.publicIntToHex(rt.getAccountType(), 1);
        /** U8 chargeType 0x00:充满为止； 0x01:按时间充（单位：秒）； 0x02：按百分比冲 0x03:按金额充(单位：元) 0x04:按电量充 0x05:整站启动 */
        String chargeType = JSUtils.publicIntToHex(rt.getChargeType(), 1);
        /** U8 chargeParam[4] 0:无含义；1:U32，充电时长，单位：1s; 2:U32，百分比，1% ; 3:U32，金额，单位，0.01元; 4:U32, 电量，单位:0.01kwh */
        String chargeParam = JSUtils.publicIntToHex(rt.getChargeParam(), JSConstants.chargeParamLength);
        /** U8 AccountStatus 0x00: 帐户有效;0x01: 帐户锁定; 0x02: 帐户过期; 0x03: 帐户无效; 0x04: 账户有效，但正在其他桩上充电; 0x05: 账户密码错误*/
        String AccountStatus = JSUtils.publicIntToHex(rt.getAccountStatus(), 1);
        /** U8 balance[4] S32，账户余额，单位：0.01元 桩根据用户余额进行可否开始本次充电判断，如果桩能够计算预计充电金额，使用该值与余额比较判断；如果桩无法预估充电金额，使用配置字段：“min_start_charge_balance”，默认值3000，单位0.01元。*/
        String balance = JSUtils.publicIntToHex(rt.getChargeParam(), JSConstants.balanceLength);
        /** U32 expiryDate 有效期,utc*/
        String expiryDate = JSUtils.publicIntToHex2((CnovertSystem.getSecondTimestampTwo(new Date())+7200*24)+"", JSConstants.expiryDateLength);
        System.out.println("expiryDate   : "+expiryDate   + "  datetime : "  +CnovertSystem.getSecondTimestampTwo(new Date())+7200*24);
        /** U8 parentIdTag[32+1] 用户组ID */
        String parentIdTag = JSUtils.publicStringToASCIIHex(StrUtil.isEmpty(rt.getCardNumber())?"0":rt.getCardNumber(), JSConstants.parentIdTagLength);//JSUtils.publicIntToHex(rt.getParentIdTag(), JSConstants.parentIdTagLength);
        System.out.println("parentIdTag  : " +parentIdTag );
        /** U8 overdraftBalanceForAccount[4]	账户透支额度，单位：0.01元 此设计为了实现运营计费的vip客户的透支功能。 */
        String overdraftBalanceForAccount = JSUtils.publicIntToHex(rt.getOverdraftBalanceForAccount(), JSConstants.overdraftBalanceForAccountLength);
        /** U8 overdraftBalanceForCard[4]	卡透支额度，单位：0.01元 此设计为了实现运营计费的钱包卡的透支功能。 */
        String overdraftBalanceForCard = JSUtils.publicIntToHex(rt.getOverdraftBalanceForCard(), JSConstants.overdraftBalanceForCardLength);
        /** U8is12V	启动参数12V/24V 取值： 0:不启用 1：12V 2:24V */
        String Voltage = JSUtils.publicIntToHex(rt.getVoltage(), 1);
        /** U8 isAllPower	功率标识 0:不启用 1：全功率 2:半功率 */
        String outputPower = JSUtils.publicIntToHex(rt.getOutputPower(), 1);
        /** U8 isNewGB	国标版本 0：不启用 1：新国标 2：老国标 */
        String isNewGB = JSUtils.publicIntToHex(rt.getIsNewGB(), 1);
        StringBuffer sb = new StringBuffer();
        sb.append(connectorId).append(idTag)
                .append(cardNumber).append(phoneNumber)
                .append(accountType).append(chargeType)
                .append(chargeParam).append(AccountStatus)
                .append(balance).append(expiryDate)
                .append(parentIdTag).append(overdraftBalanceForAccount)
                .append(overdraftBalanceForCard).append(Voltage)
                .append(outputPower).append(isNewGB);
        return sb.toString();
    }
}
