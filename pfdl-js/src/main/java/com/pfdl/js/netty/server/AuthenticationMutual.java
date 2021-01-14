package com.pfdl.js.netty.server;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.pfdl.api.jdbc.BeeCharger;
import com.pfdl.api.jdbc.BeeTdCharginglog;
import com.pfdl.api.jdbc.CalculateTheCost;
import com.pfdl.api.jdbc.RateEntity;
import com.pfdl.api.netty.PileIdentity;
import com.pfdl.api.redis.ChargeAndBraStatue;
import com.pfdl.api.redis.ChargingLogVO;
import com.pfdl.common.netty.JSConstants;
import com.pfdl.common.utils.DateUtils;
import com.pfdl.common.utils.spring.SpringUtils;
import com.pfdl.js.config.redis.RedisCache;
import com.pfdl.js.netty.entity.StartupStatus;
import com.pfdl.js.netty.utils.CnovertSystem;
import com.pfdl.js.netty.utils.JSUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 嘉盛认证交互内容
 * @Author zhaoyt
 * @Date 2020/12/17 8:43
 * @Version 1.0
 */
public class AuthenticationMutual {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationMutual.class);

    /**
     * 登录交互
     * @param dataPacketData
     * @param dataPacketFunctionCode
     * @param dataPacketCRC16
     * @param clientIp
     * @param ctx
     * @param nettyServerHandler
     */
    public  static void  authentication(String dataPacketData,String dataPacketFunctionCode, String dataPacketCRC16 ,String clientIp, ChannelHandlerContext ctx, NettyServerHandler nettyServerHandler){
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        ChannelId channelId = ctx.channel().id();
//        String dataCovertCRC = CnovertSystem.makeCRC(dataPacketData);
        /** 数据加密CRC16校验   通知服务器召测文件上传功能码 0B 不需要CRC校验*/
//        if((StrUtil.isNotEmpty(dataCovertCRC) && dataCovertCRC.equals(dataPacketCRC16)) || "0B".equals(dataPacketFunctionCode)) {
        /** 回复的十六进制  包含所有数据*/
        StringBuffer returnSB = new StringBuffer();
        /** 回应 十六进制 头部 默认嘉盛 4747  2 Bytes*/
        String returnHeader = JSConstants.CLIENT_HEADER_CODE;
        /** 本地 十六进制 数据包DATA*/
        StringBuffer returnLengthSB = new StringBuffer();
        /** 回应 十六进制 数据包长度 默认 0000  2 Bytes*/
        String returnDataLength ="0000";
        /** 回应 十六进制 数据包ID 默认 00 1 Byte*/
        String returnDataPacketID = "02";
        /** 回应 十六进制 数据包来源 默认 00000000 4 Byte*/
        String returnDataPacketSource = "00000000";
        /** 回应 十六进制 数据包目的地 默认 00000000 4 Byte*/
        String returnDataPacketDestination = "00000000";
        /** 回应 十六进制 功能代码 默认 00 1 Byte*/
        String returnDataPacketFunctionCode = "00";
        /** 回应 十六进制 数据data n Bytes*/
        StringBuffer returnDataPacketData = new StringBuffer();
        /** 回应 CRC16校验码 2 Bytes*/
        String returnDataPacketCRC16 ="0000";
        try {
            byte [] dataPacketDataBytes = CnovertSystem.hexStringToBytes(dataPacketData);

            String DYC = "0";
            /** 判断功能码是否是11   11 为注册认证 桩必须经过认证后才可建立通讯*/
            if("11".equals(dataPacketFunctionCode)){
                log.info("【客户端:[" + clientIp  + " : " +insocket.getPort() + "]】"+ " ----------------------【嘉盛注册认证鉴权开始解析】------------------------" );
                /** 解码Ascii*/
                String SN = JSUtils.getSN(dataPacketDataBytes);
                String model = JSUtils.getModel(dataPacketDataBytes);
                String vendorId = JSUtils.getVendorId(dataPacketDataBytes);
                String firmwareVersion = JSUtils.getFirmwareVersion(dataPacketDataBytes);
                String connectorCount = JSUtils.getConnectorCount(dataPacketDataBytes);
                String powerRated = JSUtils.getPowerRated(dataPacketDataBytes);
                String ICCId = JSUtils.getICCId(dataPacketDataBytes);
                String IMSi = JSUtils.getIMSi(dataPacketDataBytes);
                String protocolVersion = JSUtils.getProtocolVersion(dataPacketDataBytes);
                String meterType = JSUtils.getMeterType(dataPacketDataBytes);
                log.info("【客户端:[" + clientIp  + " : " +insocket.getPort() + "]】" + " -解析内容[ 桩的序列号-SN ]:" + SN );
                log.info("【客户端:[" + clientIp  + " : " +insocket.getPort() + "]】" + " -解析内容[ 桩的型号-model ]:" + model );
                log.info("【客户端:[" + clientIp  + " : " +insocket.getPort() + "]】" + " -解析内容[ 桩生产厂家-vendorId ]:" + vendorId );
                log.info("【客户端:[" + clientIp  + " : " +insocket.getPort() + "]】" + " -解析内容[ 当前充电机版本号-firmwareVersion ]:" + firmwareVersion );
                log.info("【客户端:[" + clientIp  + " : " +insocket.getPort() + "]】" + " -解析内容[ 枪的个数-connectorCount ]:" + connectorCount );
                log.info("【客户端:[" + clientIp  + " : " +insocket.getPort() + "]】" + " -解析内容[ 桩的额定功率-powerRated ]:" + powerRated+ " kw" );
                log.info("【客户端:[" + clientIp  + " : " +insocket.getPort() + "]】" + " -解析内容[ 如果装配了3G模块，内置SIM卡ICCID -ICCId ]:" + ICCId );
                log.info("【客户端:[" + clientIp  + " : " +insocket.getPort() + "]】" + " -解析内容[ 如果装配了3G模块，内置模块IMSI -IMSi ]:" + IMSi );
                log.info("【客户端:[" + clientIp  + " : " +insocket.getPort() + "]】" + " -解析内容[ 协议版本号；厦门版本号:0;合肥同智桩:1;宇通版本:2 -protocolVersion ]:" + protocolVersion );
                log.info("【客户端:[" + clientIp  + " : " +insocket.getPort() + "]】" + " -解析内容[ 电表类型-meterType ]:" + meterType );
                /** 进行查询数据库是否存在桩*/
                BeeCharger beeCharger = null;
                beeCharger = nettyServerHandler.beeChargerService.selectBeeChargerByProductSno(SN);

                if(beeCharger != null && StrUtil.isNotEmpty(beeCharger.getChargerNo())){
                    log.info("【客户端:[" + clientIp  + " : " +insocket.getPort() + "]】" + " -读取数据库认证成功[充电桩单元编号 ]:" + beeCharger.getChargerNo() );
                    /** 回应 十六进制 功能代码11 或 81 */
                    returnDataPacketFunctionCode = "11";
                    /** ------------------------------------data 数据 start -----------------------**/
                    /**0x00: 禁止本桩工作； 0x01:允许本桩工作； 0x02: 服务器在线，本桩正常工作*/
                    String returnCode = "01";
                    /** U8 heartBeatInterval[4] 心跳上报时间间隔，U32，单位：1s。   30秒*/
                    String returnHeartBeatInterval = "1E000000";//JSUtils.getHEXPHeartBeatInterval(30);
                    /** U8 pointId[4] 桩id，U32*/
//                                    String pointId = JSUtils.getHEXPointId(beeCharger.getChargerNo());
                    returnDataPacketDestination = JSUtils.ConvertDestId(Integer.parseInt(beeCharger.getChargerNo()));
                    String pointId = returnDataPacketDestination;
                    returnDataPacketData.append(returnCode).append(returnHeartBeatInterval).append(pointId);
                    /** ------------------------------------data 数据 end-----------------------**/
                    if (nettyServerHandler.pileMap.containsKey(channelId)) {
                        log.info("桩 【" + beeCharger.getChargerNo() + "】 是已认证状态，已认证桩数量: " + nettyServerHandler.pileMap.size());
                    } else {
                        //保存连接
                        PileIdentity pile = new PileIdentity(channelId, SN, returnDataPacketDestination,beeCharger.getChargerNo(), "0");
                        nettyServerHandler.pileMap.put(ctx.channel().id().toString(),pile);
                        log.info("桩出厂ID：【" + pile.getPileSN() + "】分配的destID：【"+pile.getDestIdNumber()+"】nettyID：【"+pile.getChannelId()+"】进行认证连接成功! [IP:" + clientIp + "--->PORT:" + ((InetSocketAddress) ctx.channel().remoteAddress()).getPort() + "]");
                        log.info("已认证桩数量 : " + nettyServerHandler.pileMap.size());
                        DYC= "1";
                    }




                }
            }else if("15".equals(dataPacketFunctionCode)){
                String destIdNumber = nettyServerHandler.pileMap.get(channelId.toString()).getDestIdNumber();
                /** 判断功能码是否是15   15 为心跳应答 */
//                    log.info("【客户端:[" + clientIp  + " : " +insocket.getPort() + "]】"+ " ----------------------【嘉盛心跳应答开始解析】------------------------" );
                log.info("【客户端:[" + destIdNumber + "]】"+ " ----------------------【嘉盛心跳应答开始解析】------------------------" );

                /** 校验通过,进行解析心跳应答 字节序小端排序规则*/
                long pointVersion = JSUtils.getPointVersion(dataPacketDataBytes);
                long centerVersion = JSUtils.getCenterVersion(dataPacketDataBytes);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - pointVersion  :" + pointVersion);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - centerVersion   :" + centerVersion );
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - pointVersion转换日期   :" + new Date(pointVersion*1000));
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - pointVersion转换日期   :" + DateUtils.getDate(new Date(pointVersion*1000),"yyyy-MM-dd HH:mm:ss") );
                /** 功能代码*/
                returnDataPacketFunctionCode = "15";
                /** ------------------------------------data 数据 start -----------------------**/
                /** U8 returnCode 0x01: 成功； 0x02: 校验失败，重新发送; */
                String returnHeartBeatCode = "01";
                String returnHeartBeatTime = JSUtils.returnHeartBeatTime(new Date());
                returnDataPacketData.append(returnHeartBeatCode).append(returnHeartBeatTime);
                /** ------------------------------------data 数据 start -----------------------**/
            }else if("19".equals(dataPacketFunctionCode) || "0B".equals(dataPacketFunctionCode)){
                String destIdNumber = nettyServerHandler.pileMap.get(channelId.toString()).getDestIdNumber();
                /** DataTransfer 19 目前实现功能有：1、上报桩GPS坐标，
                 * DataTransfer 0B 通知服务器召测文件上传成功2、通知服务器召测文件上传成功 */
                log.info("【客户端:[" + destIdNumber + "]】"+ " ----------------------【嘉盛位置上报开始解析】------------------------" );
                String vendorId=JSUtils.getPositionVendorId(dataPacketDataBytes);
                String messageId=JSUtils.getMessageId(dataPacketDataBytes);
                String PositionData=JSUtils.getPositionData(dataPacketDataBytes);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - vendorId  :" + vendorId);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - messageId   :" + messageId );
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - PositionData   :" + PositionData);

                /** 回复状态 U8 returnStatus 0x00:accepted 接受 0x01: rejected 拒绝 0x02: unknowMessageId 未知消息ID 0x03: unknowVendorId 未知运营商 */
                String returnPositionStatus = "";
                /** 回复内容数据 U8 returnData[128+1]*/
                String returnPositionData = "";
                if(StrUtil.isNotEmpty(vendorId) && "01".equals(vendorId)){
                    /** 匹配成功 0x01 嘉盛运营商*/
                    if(StrUtil.isNotEmpty(messageId) && "GPSPosition".equals(messageId)){
                        /** 功能码*/
                        returnDataPacketFunctionCode = "19";
                        /** ------------------------------------data 数据 start -----------------------**/
                        returnPositionStatus = "00";
                        returnPositionData = JSUtils.returnPositionData("GPSPosition");
                        returnDataPacketData.append(returnPositionStatus).append(returnPositionData);
                        /** ------------------------------------data 数据 start -----------------------**/
                    }else if(StrUtil.isNotEmpty(messageId) && "syncWechatUrl".equals(messageId)){
                        /** 功能码*/
                        returnDataPacketFunctionCode = "0B";
                        returnPositionStatus = "00";
                        returnPositionData = JSUtils.returnPositionData("syncWechatUrl");
                        returnDataPacketData.append(returnPositionStatus).append(returnPositionData);
                    }else{
                        returnPositionStatus = "02";
                    }
                }else{
                    returnPositionStatus = "03";
                }

                /**匹配成功 GPSPosition位置上报信息 */

            }else if("16".equals(dataPacketFunctionCode)){
                String destIdNumber = nettyServerHandler.pileMap.get(channelId.toString()).getDestIdNumber();
                /** StatusNotification 16 目前实现功能有：1、上报桩GPS坐标，2、通知服务器召测文件上传成功 */
                log.info("【客户端:[" + destIdNumber + "]】"+ " ----------------------【嘉盛桩或枪状态信息开始解析】------------------------" );
                /** 充电枪号 0：上报的是桩的状态和错误信息。 1-100：上报的是枪的状态和错误信息。 101之后：上报的是受电弓的状态和错误信息。*/
                String connectorId=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.connectorIdLength,0);
                /** 0x00: 桩或枪状态，空闲 0x01: 桩或枪状态，正在使用 0x02: 桩或枪状态，出错 0x03: 桩或枪状态，不可用 0x04: 桩或枪状态，预约状态 0x06: 充电中（OCPP）*/
                String pointStatusCode=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.pointStatusCodeLength,JSConstants.connectorIdLength);
                /** 0x00：枪处于拔出状态； 0x01：枪处于连接状态； 0x02：弓在顶部 0x03：弓在底部 */
                String connectorConnected=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.connectorConnectedLength,(JSConstants.connectorIdLength+JSConstants.pointStatusCodeLength));

                /** pointStatusCode:
                 * 0:Available,1:Occupied,2:Fault,3:Unavailable,4:Reserved
                 * 0:可用，1:占用，2:故障，3:不可用，4:保留 */
                if(StrUtil.isNotEmpty(connectorId) && "0".equals(connectorId)){
                    log.info("【客户端:[" + destIdNumber + "]】" + " -桩的状态信息 -connectorId : " + connectorId +" [上报的是桩的状态和错误信息]");
                    if(StrUtil.isNotEmpty(pointStatusCode)){
                        if("2".equals(pointStatusCode)){
                            log.info("【客户端:[" + destIdNumber + "]】" + " -桩的状态信息 -pointStatusCode : " + pointStatusCode +" [错误状态：至少有一个枪错误]");
                        }else if("0".equals(pointStatusCode)){
                            log.info("【客户端:[" + destIdNumber + "]】" + " -桩的状态信息 -pointStatusCode : " + pointStatusCode +" [空闲状态：至少有一个枪空闲]");
                        }else if("1".equals(pointStatusCode)){
                            log.info("【客户端:[" + destIdNumber + "]】" + " -桩的状态信息 -pointStatusCode : " + pointStatusCode +" [占用状态：表示所有枪都处于充电状态]");
                        }else if("4".equals(pointStatusCode)){
                            log.info("【客户端:[" + destIdNumber + "]】" + " -桩的状态信息 -pointStatusCode : " + pointStatusCode +" [预约状态：表示所有枪都是预约]");
                        }else if("3".equals(pointStatusCode)){
                            log.info("【客户端:[" + destIdNumber + "]】" + " -桩的状态信息 -pointStatusCode : " + pointStatusCode +" [不可用状态：表示所有枪都是不可用]");
                        }else{
                            log.info("【客户端:[" + destIdNumber + "]】" + " -桩的状态信息 -pointStatusCode : " + pointStatusCode +" [无法解读桩的状态信息代码，请联系管理员]");
                        }
                    }
                }else if(StrUtil.isNotEmpty(connectorId) && Integer.parseInt(connectorId) <= 100 && Integer.parseInt(connectorId) >= 1){
                    log.info("【客户端:[" + destIdNumber + "]】" + " -枪的状态信息 -connectorId : " + connectorId +" [上报的是枪的状态和错误信息]");
                    if(StrUtil.isNotEmpty(pointStatusCode)){
                        if("2".equals(pointStatusCode)){
                            log.info("【客户端:[" + destIdNumber + "]】" + " -枪的状态信息 -pointStatusCode : " + pointStatusCode +" [错误状态：该枪目前错误状态]");
                        }else if("3".equals(pointStatusCode)){
                            log.info("【客户端:[" + destIdNumber + "]】" + " -枪的状态信息 -pointStatusCode : " + pointStatusCode +" [不可用状态：该枪目前不可用状态]");
                        }else if("1".equals(pointStatusCode)){
                            //占用状态：该枪目前正在使用状态
                            if("0".equals(connectorConnected)){
                                log.info("【客户端:[" + destIdNumber + "]】" + " -枪的状态信息 -pointStatusCode = connectorConnected: " + pointStatusCode +"="+connectorConnected+" [空闲状态：该枪目前空闲状态] 已经拔枪");
                            }else if("1".equals(connectorConnected)){
                                log.info("【客户端:[" + destIdNumber + "]】" + " -枪的状态信息 -pointStatusCode != connectorConnected: " + pointStatusCode +"!="+connectorConnected+" [空闲状态：该枪目前连接状态]插枪连接状态");
                            }
//                            log.info("【客户端:[" + destIdNumber + "]】" + " -枪的状态信息 -pointStatusCode : " + pointStatusCode +" [占用状态：该枪目前正在使用状态]");
                        }else if("4".equals(pointStatusCode)){
                            log.info("【客户端:[" + destIdNumber + "]】" + " -枪的状态信息 -pointStatusCode : " + pointStatusCode +" [预约状态：该枪目前预约状态]");
                        }else if("0".equals(pointStatusCode)){
                            //空闲状态下  需查看 枪的连接状态是否是空闲
                            if(StrUtil.isNotEmpty(connectorConnected)){
                                if("0".equals(connectorConnected)){
                                    log.info("【客户端:[" + destIdNumber + "]】" + " -枪的状态信息 -pointStatusCode = connectorConnected: " + pointStatusCode +"="+connectorConnected+" [空闲状态：该枪目前空闲状态]");
                                }else if("1".equals(connectorConnected)){
                                    log.info("【客户端:[" + destIdNumber + "]】" + " -枪的状态信息 -pointStatusCode != connectorConnected: " + pointStatusCode +"!="+connectorConnected+" [空闲状态：该枪目前连接状态]插枪状态");
                                }

                            }

                        }else{
                            log.info("【客户端:[" + destIdNumber + "]】" + " -枪的状态信息 -pointStatusCode : " + pointStatusCode +" [无法解读枪的状态信息代码，请联系管理员]");
                        }
                    }
                }else if(StrUtil.isNotEmpty(connectorId) && Integer.parseInt(connectorId) > 100){
                    /** 101之后：上报的是受电弓的状态和错误信息*/

                }else{
                    /** 上报信息错误无法解析*/
                }
                /** 故障码 */
                String pointErrorCode=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.pointErrorCodeLength,(JSConstants.connectorConnectedLength+JSConstants.connectorIdLength+JSConstants.pointStatusCodeLength));
                /** 错误信息2020-12-16 15:01:41*/
                String errorInfo=JSUtils.publicBytesToASCIIByIndex(dataPacketDataBytes,JSConstants.errorInfoLength,(JSConstants.connectorIdLength+JSConstants.pointStatusCodeLength+JSConstants.connectorConnectedLength+JSConstants.pointErrorCodeLength));
                /** 运营商ID  0x01：嘉盛*/
                String vendorId=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.vendorIdLength,(JSConstants.connectorIdLength+JSConstants.pointStatusCodeLength+JSConstants.connectorConnectedLength+JSConstants.pointErrorCodeLength+JSConstants.errorInfoLength));
                /** U16 vendorErrorCode 厂商错误码*/
                String vendorErrorCode=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.vendorErrorCodeLength,(JSConstants.connectorIdLength+JSConstants.pointStatusCodeLength+JSConstants.connectorConnectedLength+JSConstants.pointErrorCodeLength+JSConstants.errorInfoLength+JSConstants.vendorIdLength));
                /** 状态或错误的发生时间，单位：1s。U32,表示从1970年1月1日00：00：00到当前时刻的秒*/
                String timestamp=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.timestampLength,(JSConstants.connectorIdLength+JSConstants.pointStatusCodeLength+JSConstants.connectorConnectedLength+JSConstants.pointErrorCodeLength+JSConstants.errorInfoLength+JSConstants.vendorIdLength+JSConstants.vendorErrorCodeLength));
                log.info("【客户端:[" + destIdNumber + "]】" + " -枪的状态信息 0x00：枪处于拔出状态； 0x01：枪处于连接状态 -connectorConnected : " + connectorConnected );
                log.info("【客户端:[" + destIdNumber + "]】" + " -故障码 -pointErrorCode : " + pointErrorCode);
                log.info("【客户端:[" + destIdNumber + "]】" + " -错误信息 -errorInfo : " + errorInfo);
                log.info("【客户端:[" + destIdNumber + "]】" + " -运营商ID -vendorId : " + vendorId);
                log.info("【客户端:[" + destIdNumber + "]】" + " -厂商错误码 -vendorErrorCode : " + vendorErrorCode);
                log.info("【客户端:[" + destIdNumber + "]】" + " -状态或错误的发生时间秒戳 -timestamp : " + timestamp);
                log.info("【客户端:[" + destIdNumber + "]】" + " -状态或错误的发生时间秒戳 -timestamp : " + DateUtil.date(Long.parseLong(timestamp)*1000));
                /** 需要存redis */
                if(StrUtil.isNotEmpty(connectorId) && Integer.parseInt(connectorId) > 0){
                    String cabStr = SpringUtils.getBean(RedisCache.class).getCacheObject(destIdNumber+"_"+Integer.parseInt(connectorId)+JSConstants.JS_KEY_STATUE);//获取当前订单信息
                    System.out.println(destIdNumber+"_"+Integer.parseInt(connectorId)+JSConstants.JS_KEY_STATUE);
                    ChargeAndBraStatue cab = JSON.parseObject(cabStr, ChargeAndBraStatue.class);
                    cab.setBranchStatue(pointStatusCode);
                    cab.setConnectStatue(connectorConnected);
                    SpringUtils.getBean(RedisCache.class).setCacheObject(destIdNumber+"_"+connectorId+JSConstants.JS_KEY_STATUE, JSON.toJSONString(cab));
                }
                /** 功能码*/
                returnDataPacketFunctionCode = "16";
                /** U8 returnCode  回复数据Data 0x01: 成功 */
                returnDataPacketData.append("01");
            }
            if(StrUtil.isNotEmpty(returnDataPacketData) && !"00".equals(returnDataPacketFunctionCode)){
                /** 回应 目的地ID*/
                returnDataPacketDestination = JSUtils.getReturnDataPacketDestination(nettyServerHandler.pileMap,channelId.toString());
                /** 回应 CRC16校验码 */
                returnDataPacketCRC16 = JSUtils.returnDataCRC16(returnDataPacketData.toString());
                /** 回应 数据包总DATA*/
                returnLengthSB.append(returnDataPacketID).append(returnDataPacketSource).append(returnDataPacketDestination).append(returnDataPacketFunctionCode).append(returnDataPacketData).append(returnDataPacketCRC16);
                /** 回应 的数据包总长度*/
                returnDataLength =JSUtils.returnDataLengthHEX(returnLengthSB.length()/2);
                /** 下面是返回总体16进制数据 */
                returnSB.append(returnHeader).append(returnDataLength).append(returnLengthSB.toString());
                log.info("【客户端:[" + nettyServerHandler.pileMap.get(channelId.toString()).getDestIdNumber() + "]】" + " -应答返回十六进制内容 ]:" + CnovertSystem.parseHexStrToHex(returnSB.toString()) );
                byte [] returnByte= CnovertSystem.hexStringToBytes(returnSB.toString());
                nettyServerHandler.writeMessage(ctx.channel().id(), returnByte);
            }else{
                log.error("【客户端:[" + nettyServerHandler.pileMap.get(channelId.toString()).getDestIdNumber() + "]】" + " -服务端错误未返回returnDataPacketData]:[" + returnDataPacketData +"]  或未填写功能代码: " +returnDataPacketFunctionCode );
            }
//            if("1".equals(DYC)){
                // 第一次保存连接  下发二维码前缀
                /** 二维码前缀*/
//                String strVendorId = "01";
//                String strMessageId = JSUtils.publicStringToASCIIHex("syncWechatUrl",17);
//                JSONObject json = new JSONObject();
//                json.put("url",destIdNumber+JSConstants.JS_INIT_QRCODE);
//                System.out.println(json.toString());
//                String strData = JSUtils.publicStringToASCIIHex(json.toString(),129);
//                /** 回应 目的地ID*/
//                String DataPacketDestination = JSUtils.getReturnDataPacketDestination(nettyServerHandler.pileMap,channelId.toString());
//                /**功能码*/
//                String PacketFunctionCode = "0B";
//                /** 回应 十六进制 数据data n Bytes*/
//                StringBuffer DataPacketData = new StringBuffer();
//                DataPacketData.append(strVendorId).append(strMessageId).append(strData);
//                /** 回应 CRC16校验码 */
//                returnDataPacketCRC16 = JSUtils.returnDataCRC16(DataPacketData.toString());
//                /** 回应 数据包总DATA*/
//                StringBuffer  dataLengthSB= new StringBuffer();
//                dataLengthSB.append(returnDataPacketID).append(returnDataPacketSource).append(returnDataPacketDestination).append(PacketFunctionCode).append(DataPacketData).append(returnDataPacketCRC16);
//                /** 回应 的数据包总长度*/
//                returnDataLength =JSUtils.returnDataLengthHEX(dataLengthSB.length()/2);
//                /** 下面是返回总体16进制数据 */
//                StringBuffer  SBCode= new StringBuffer();
//                SBCode.append(returnHeader).append(returnDataLength).append(dataLengthSB.toString());
//                log.info("【客户端:[" + destIdNumber + "]】" + " -二维码前缀发送成功 ]:" + CnovertSystem.parseHexStrToHex(SBCode.toString()) );
//                byte [] returnByte= CnovertSystem.hexStringToBytes(SBCode.toString());
//                nettyServerHandler.writeMessage(ctx.channel().id(), returnByte);
//                nettyServerHandler.writeMessage(ctx.channel().id(),returnByte);
//            }
        }catch (Exception e){
            e.printStackTrace();
        }
//        }else{
//            /** 数据解密校验失败CRC16*/
//            log.error("【客户端:[" + clientIp  + " : " +insocket.getPort() + "]】" + " CRC16 解密失败...."+ " 报文dataPacketCRC16 :"+dataPacketCRC16+"  校验dataCovertCRC :  "+dataCovertCRC);
//        }
    }
    /**
     * 解析桩应答报文
     * @param dataPacketData
     * @param dataPacketFunctionCode
     * @param dataPacketCRC16
     * @param clientIp
     * @param ctx
     * @param nettyServerHandler
     */
    public  static void answerJS(String dataPacketData,String dataPacketFunctionCode, String dataPacketCRC16 ,String clientIp, ChannelHandlerContext ctx, NettyServerHandler nettyServerHandler){
        ChannelId channelId = ctx.channel().id();
        try {
            byte[] dataPacketDataBytes = CnovertSystem.hexStringToBytes(dataPacketData);
            String destIdNumber = nettyServerHandler.pileMap.get(channelId.toString()).getDestIdNumber();
            /** 客户端桩应答 远程启动结果*/
            if ("02".equals(dataPacketFunctionCode)) {
                log.info("【客户端:[" + destIdNumber + "]】"+ " ----------------------【嘉盛远程启动应答开始解析】------------------------" );
                /** 校验通过,解析远程启动结果*/
                int returnCode = CnovertSystem.hexStringToAlgorism(dataPacketData);
                if("0".equals(returnCode)){
                    log.info("【客户端:[" + destIdNumber + "]】" + " -桩应答【接受】远程启动命令....");
                }else if("1".equals(returnCode)){
                    log.info("【客户端:[" + destIdNumber + "]】" + " -桩应答【拒绝】远程启动命令....");
                }else if("2".equals(returnCode)){
                    log.info("【客户端:[" + destIdNumber + "]】" + " -桩应答【即时服务】远程启动命令....");
                }else if("3".equals(returnCode)){
                    log.info("【客户端:[" + destIdNumber + "]】" + " -桩应答【未连接】远程启动命令....");
                }
            }else if ("0E".equals(dataPacketFunctionCode)) {
                /** 客户端应答 费率下发 成功失败
                 * U8 returnCode	0x00: 成功
                 * 0x01:失败*/
                log.info("【客户端:[" + destIdNumber + "]】"+ " ----------------------【嘉盛费率下发返回值开始解析】------------------------" );
                /** 费率下发结果*/
                int returnCode = CnovertSystem.hexStringToAlgorism(dataPacketData);
                if("0".equals(returnCode)){
                    log.info("【客户端:[" + destIdNumber + "]】" + " -桩应答费率下发【成功】....");
                }else if("1".equals(returnCode)){
                    log.info("【客户端:[" + destIdNumber + "]】" + " -桩应答费率下发【失败】....");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 上电内容
     * @param dataPacketData
     * @param dataPacketFunctionCode
     * @param dataPacketCRC16
     * @param clientIp
     * @param ctx
     * @param nettyServerHandler
     */
    public  static void powerOnJS(String dataPacketData, String dataPacketFunctionCode, String dataPacketCRC16, String clientIp, ChannelHandlerContext ctx, NettyServerHandler nettyServerHandler){
        ChannelId channelId = ctx.channel().id();
        /** 回复的十六进制  包含所有数据*/
        StringBuffer returnSB = new StringBuffer();
        /** 回应 十六进制 头部 默认嘉盛 4747  2 Bytes*/
        String returnHeader = JSConstants.CLIENT_HEADER_CODE;
        /** 本地 十六进制 数据包DATA*/
        StringBuffer returnLengthSB = new StringBuffer();
        /** 回应 十六进制 数据包长度 默认 0000  2 Bytes*/
        String returnDataLength = "0000";
        /** 回应 十六进制 数据包ID 默认 00 1 Byte*/
        String returnDataPacketID = "02";
        /** 回应 十六进制 数据包来源 默认 00000000 4 Byte*/
        String returnDataPacketDestination = "00000000";
        /** 回应 十六进制 数据包目的地 默认 00000000 4 Byte*/
        String returnDataPacketSource = "00000000";
        /** 回应 十六进制 功能代码 默认 00 1 Byte*/
        String returnDataPacketFunctionCode = "00";
        /** 回应 十六进制 数据data n Bytes*/
        StringBuffer returnDataPacketData = new StringBuffer();
        /** 回应 CRC16校验码 2 Bytes*/
        String returnDataPacketCRC16 = "0000";
        try {
            byte[] dataPacketDataBytes = CnovertSystem.hexStringToBytes(dataPacketData);
            /** 开始充电报文解析*/
            if ("13".equals(dataPacketFunctionCode)) {
                String destIdNumber = nettyServerHandler.pileMap.get(channelId.toString()).getDestIdNumber();
                log.info("【客户端:[" + destIdNumber + "]】"+ " ----------------------【嘉盛开始充电报文开始解析】------------------------" );
                /** 账户类型: 0x00:手机号 0x01:用户卡 0x02:微信 0x03:支付宝 0xFF:无法识别*/
                String accountType = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.accountTypeLength,0);
                String userIdTag = JSUtils.publicBytesToASCIIByIndex(dataPacketDataBytes,JSConstants.userIdTagLength,JSConstants.accountTypeLength);
                String connectorId = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.connectorIdLength,JSConstants.userIdTagLength+JSConstants.accountTypeLength);
                String startTime = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.startTimeLength,JSConstants.connectorIdLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength);
                String expectedStoptime = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.expectedStoptimeLength,JSConstants.startTimeLength+JSConstants.connectorIdLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength);
                String pointTransactionTag = JSUtils.publicBytesToASCIIByIndex(dataPacketDataBytes,JSConstants.pointTransactionTagLength,JSConstants.expectedStoptimeLength+JSConstants.startTimeLength+JSConstants.connectorIdLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength);
                String chargeType = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.chargeTypeLength,JSConstants.pointTransactionTagLength+JSConstants.expectedStoptimeLength+JSConstants.startTimeLength+JSConstants.connectorIdLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength);
                String chargeParam = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.chargeParamLength,JSConstants.chargeTypeLength+JSConstants.pointTransactionTagLength+JSConstants.expectedStoptimeLength+JSConstants.startTimeLength+JSConstants.connectorIdLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength);
                String meterStart = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.meterStartLength,JSConstants.chargeParamLength+JSConstants.chargeTypeLength+JSConstants.pointTransactionTagLength+JSConstants.expectedStoptimeLength+JSConstants.startTimeLength+JSConstants.connectorIdLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength);
                String reservationId = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.reservationIdLength,JSConstants.meterStartLength+JSConstants.chargeParamLength+JSConstants.chargeTypeLength+JSConstants.pointTransactionTagLength+JSConstants.expectedStoptimeLength+JSConstants.startTimeLength+JSConstants.connectorIdLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength);
                String payType = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.payTypeLength,JSConstants.reservationIdLength+JSConstants.meterStartLength+JSConstants.chargeParamLength+JSConstants.chargeTypeLength+JSConstants.pointTransactionTagLength+JSConstants.expectedStoptimeLength+JSConstants.startTimeLength+JSConstants.connectorIdLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength);

                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - accountType  :" + accountType);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - userIdTag  :" + userIdTag);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - connectorId  :" + connectorId);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - startTime  :" + startTime);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - expectedStoptime  :" + expectedStoptime);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - pointTransactionTag  :" + pointTransactionTag);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - chargeType  :" + chargeType);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - chargeParam  :" + chargeParam);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - meterStart  :" + meterStart);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - reservationId  :" + reservationId);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - payType  :" + payType);
                /** -----------------------------将远程启动充电成功存储到MAP中反馈给接口------------------------------------------------ */
                /** 需要存redis */
                String cabStr = SpringUtils.getBean(RedisCache.class).getCacheObject(destIdNumber+"_"+connectorId+JSConstants.JS_KEY_STATUE);//获取当前订单信息
                ChargeAndBraStatue cab = JSON.parseObject(cabStr, ChargeAndBraStatue.class);
                if(nettyServerHandler.remoteStartStatus.containsKey(cab.getTransactionId())){
                    StartupStatus startupStatus = nettyServerHandler.remoteStartStatus.get(cab.getTransactionId());
                    if(StrUtil.isNotEmpty(startupStatus.getStartForm()) && "remote".equals(startupStatus.getStartForm())){
                        //如果是手动启动的话 修改状态码
                        startupStatus.setStatus("1");
                        nettyServerHandler.remoteStartStatus.put(cab.getTransactionId(),startupStatus);
                    }
                }else{
                    //自动启动
                    StartupStatus newStartStatus = new StartupStatus("automatic","1",null);
                    nettyServerHandler.remoteStartStatus.put(cab.getTransactionId(),newStartStatus);
                    log.info("【客户端:[" + destIdNumber + "]】" + " -桩自动启动充电------------------------------------------");
                }
                /** 调用费率下发.*/
                SyncPrice(ctx,cab.getSessionId(),JSUtils.getReturnDataPacketDestination(nettyServerHandler.pileMap,channelId.toString()),nettyServerHandler,connectorId);
                /** -----------------------------存redis数据------------------------------------------------ */
                String clogStr = SpringUtils.getBean(RedisCache.class).getCacheObject(cab.getSessionId());//获取当前订单信息
                ChargingLogVO clog = JSON.parseObject(clogStr, ChargingLogVO.class);
                clog.setStartTime(startTime);
                clog.setStartValue(meterStart);
                clog.setChargeStatus("0");//开始充电
                /** 更新 redis*/
                SpringUtils.getBean(RedisCache.class).setCacheObject(cab.getSessionId(),JSON.toJSONString(clog));
                /** -----------------------------存redis数据------------------------------------------------ */
                /** -----------------------------将远程启动充电成功存储到MAP中反馈给接口------------------------------------------------ */
                /** 功能代码*/
                returnDataPacketFunctionCode = "13";
                /** ------------------------------------data 数据 start ----------------------- */
                /** U8 transactionId[4]	后台交易号 */
                String returnTransactionId = JSUtils.publicIntToHex(cab.getTransactionId(),4);  //需要转16进制
                /**U8 pointTransactionTag[26]	桩交易号（桩id_枪号_utc开始充电时间）*/
                String returnPointTransactionTag = JSUtils.publicStringToASCIIHex(pointTransactionTag,26);
                returnDataPacketData.append(returnTransactionId).append(returnPointTransactionTag);
                /** ------------------------------------data 数据 start -----------------------**/
                nettyServerHandler.beeChargerService.updateOrderLogStatusByIdTag(cab.getSessionId(),"1");
                /** 充电完成账单上报报文*/
            }else if ("14".equals(dataPacketFunctionCode)) {
                    String destIdNumber = nettyServerHandler.pileMap.get(channelId.toString()).getDestIdNumber();
                    log.info("【客户端:[" + destIdNumber + "]】"+ " ----------------------【嘉盛充电完成,账单报文开始解析】------------------------" );
                    /** U8 transactionId[4]	U32，交易号*/
                    String transactionId = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.transactionIdLength,0);
                    /**U8 boostVoltageType	辅助电压12V/24V: 0x01:12V 0x02:24V*/
                    String boostVoltageType = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.boostVoltageTypeLength,JSConstants.transactionIdLength);
                    /** 账户类型*/
                    String accountType = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.accountTypeLength,JSConstants.boostVoltageTypeLength+JSConstants.transactionIdLength);
                    String userIdTag = JSUtils.publicBytesToASCIIByIndex(dataPacketDataBytes,JSConstants.userIdTagLength,JSConstants.accountTypeLength+JSConstants.boostVoltageTypeLength+JSConstants.transactionIdLength);
                    String startTime = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.startTimeLength,JSConstants.userIdTagLength+JSConstants.accountTypeLength+JSConstants.boostVoltageTypeLength+JSConstants.transactionIdLength);
                    String startMeter = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.startMeterLength,JSConstants.startTimeLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength+JSConstants.boostVoltageTypeLength+JSConstants.transactionIdLength);
                    String stopTime = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.stopTimeLength,JSConstants.startMeterLength+JSConstants.startTimeLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength+JSConstants.boostVoltageTypeLength+JSConstants.transactionIdLength);
                    String meterStop = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.meterStopLength,JSConstants.stopTimeLength+JSConstants.startMeterLength+JSConstants.startTimeLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength+JSConstants.boostVoltageTypeLength+JSConstants.transactionIdLength);
                    String connectorId = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.connectorIdLength,JSConstants.meterStopLength+JSConstants.stopTimeLength+JSConstants.startMeterLength+JSConstants.startTimeLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength+JSConstants.boostVoltageTypeLength+JSConstants.transactionIdLength);
                    String cardCost = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.cardCostLength,JSConstants.connectorIdLength+JSConstants.meterStopLength+JSConstants.stopTimeLength+JSConstants.startMeterLength+JSConstants.startTimeLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength+JSConstants.boostVoltageTypeLength+JSConstants.transactionIdLength);
                    String cardBalance = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.cardBalanceLength,JSConstants.cardCostLength+JSConstants.connectorIdLength+JSConstants.meterStopLength+JSConstants.stopTimeLength+JSConstants.startMeterLength+JSConstants.startTimeLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength+JSConstants.boostVoltageTypeLength+JSConstants.transactionIdLength);
                    String sharpMeter = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.sharpMeterLength,JSConstants.cardBalanceLength+JSConstants.cardCostLength+JSConstants.connectorIdLength+JSConstants.meterStopLength+JSConstants.stopTimeLength+JSConstants.startMeterLength+JSConstants.startTimeLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength+JSConstants.boostVoltageTypeLength+JSConstants.transactionIdLength);
                    String peakMeter = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.peakMeterLength,JSConstants.sharpMeterLength+JSConstants.cardBalanceLength+JSConstants.cardCostLength+JSConstants.connectorIdLength+JSConstants.meterStopLength+JSConstants.stopTimeLength+JSConstants.startMeterLength+JSConstants.startTimeLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength+JSConstants.boostVoltageTypeLength+JSConstants.transactionIdLength);
                    String flatMeter = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.flatMeterLength,JSConstants.peakMeterLength+JSConstants.sharpMeterLength+JSConstants.cardBalanceLength+JSConstants.cardCostLength+JSConstants.connectorIdLength+JSConstants.meterStopLength+JSConstants.stopTimeLength+JSConstants.startMeterLength+JSConstants.startTimeLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength+JSConstants.boostVoltageTypeLength+JSConstants.transactionIdLength);
                    String vallyMeter = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.vallyMeterLength,JSConstants.flatMeterLength+JSConstants.peakMeterLength+JSConstants.sharpMeterLength+JSConstants.cardBalanceLength+JSConstants.cardCostLength+JSConstants.connectorIdLength+JSConstants.meterStopLength+JSConstants.stopTimeLength+JSConstants.startMeterLength+JSConstants.startTimeLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength+JSConstants.boostVoltageTypeLength+JSConstants.transactionIdLength);
                    String sharpCost = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.sharpCostLength,JSConstants.vallyMeterLength+JSConstants.flatMeterLength+JSConstants.peakMeterLength+JSConstants.sharpMeterLength+JSConstants.cardBalanceLength+JSConstants.cardCostLength+JSConstants.connectorIdLength+JSConstants.meterStopLength+JSConstants.stopTimeLength+JSConstants.startMeterLength+JSConstants.startTimeLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength+JSConstants.boostVoltageTypeLength+JSConstants.transactionIdLength);
                    String peakCost = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.peakCostLength,JSConstants.sharpCostLength+JSConstants.vallyMeterLength+JSConstants.flatMeterLength+JSConstants.peakMeterLength+JSConstants.sharpMeterLength+JSConstants.cardBalanceLength+JSConstants.cardCostLength+JSConstants.connectorIdLength+JSConstants.meterStopLength+JSConstants.stopTimeLength+JSConstants.startMeterLength+JSConstants.startTimeLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength+JSConstants.boostVoltageTypeLength+JSConstants.transactionIdLength);
                    String flatCost = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.flatCostLength,JSConstants.peakCostLength+JSConstants.sharpCostLength+JSConstants.vallyMeterLength+JSConstants.flatMeterLength+JSConstants.peakMeterLength+JSConstants.sharpMeterLength+JSConstants.cardBalanceLength+JSConstants.cardCostLength+JSConstants.connectorIdLength+JSConstants.meterStopLength+JSConstants.stopTimeLength+JSConstants.startMeterLength+JSConstants.startTimeLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength+JSConstants.boostVoltageTypeLength+JSConstants.transactionIdLength);
                    String vallyCost = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.vallyCostLength,JSConstants.flatCostLength+JSConstants.peakCostLength+JSConstants.sharpCostLength+JSConstants.vallyMeterLength+JSConstants.flatMeterLength+JSConstants.peakMeterLength+JSConstants.sharpMeterLength+JSConstants.cardBalanceLength+JSConstants.cardCostLength+JSConstants.connectorIdLength+JSConstants.meterStopLength+JSConstants.stopTimeLength+JSConstants.startMeterLength+JSConstants.startTimeLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength+JSConstants.boostVoltageTypeLength+JSConstants.transactionIdLength);
                    String charpPrice = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.charpPriceLength,JSConstants.vallyCostLength+JSConstants.flatCostLength+JSConstants.peakCostLength+JSConstants.sharpCostLength+JSConstants.vallyMeterLength+JSConstants.flatMeterLength+JSConstants.peakMeterLength+JSConstants.sharpMeterLength+JSConstants.cardBalanceLength+JSConstants.cardCostLength+JSConstants.connectorIdLength+JSConstants.meterStopLength+JSConstants.stopTimeLength+JSConstants.startMeterLength+JSConstants.startTimeLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength+JSConstants.boostVoltageTypeLength+JSConstants.transactionIdLength);
                    String peakPrice = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.peakPriceLength,JSConstants.charpPriceLength+JSConstants.vallyCostLength+JSConstants.flatCostLength+JSConstants.peakCostLength+JSConstants.sharpCostLength+JSConstants.vallyMeterLength+JSConstants.flatMeterLength+JSConstants.peakMeterLength+JSConstants.sharpMeterLength+JSConstants.cardBalanceLength+JSConstants.cardCostLength+JSConstants.connectorIdLength+JSConstants.meterStopLength+JSConstants.stopTimeLength+JSConstants.startMeterLength+JSConstants.startTimeLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength+JSConstants.boostVoltageTypeLength+JSConstants.transactionIdLength);
                    String flatPrice = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.flatPriceLength,JSConstants.peakPriceLength+JSConstants.charpPriceLength+JSConstants.vallyCostLength+JSConstants.flatCostLength+JSConstants.peakCostLength+JSConstants.sharpCostLength+JSConstants.vallyMeterLength+JSConstants.flatMeterLength+JSConstants.peakMeterLength+JSConstants.sharpMeterLength+JSConstants.cardBalanceLength+JSConstants.cardCostLength+JSConstants.connectorIdLength+JSConstants.meterStopLength+JSConstants.stopTimeLength+JSConstants.startMeterLength+JSConstants.startTimeLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength+JSConstants.boostVoltageTypeLength+JSConstants.transactionIdLength);
                    String valleyPrice = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.valleyPriceLength,JSConstants.flatPriceLength+JSConstants.peakPriceLength+JSConstants.charpPriceLength+JSConstants.vallyCostLength+JSConstants.flatCostLength+JSConstants.peakCostLength+JSConstants.sharpCostLength+JSConstants.vallyMeterLength+JSConstants.flatMeterLength+JSConstants.peakMeterLength+JSConstants.sharpMeterLength+JSConstants.cardBalanceLength+JSConstants.cardCostLength+JSConstants.connectorIdLength+JSConstants.meterStopLength+JSConstants.stopTimeLength+JSConstants.startMeterLength+JSConstants.startTimeLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength+JSConstants.boostVoltageTypeLength+JSConstants.transactionIdLength);
                    String sharpTime = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.sharpTimeLength,JSConstants.valleyPriceLength+JSConstants.flatPriceLength+JSConstants.peakPriceLength+JSConstants.charpPriceLength+JSConstants.vallyCostLength+JSConstants.flatCostLength+JSConstants.peakCostLength+JSConstants.sharpCostLength+JSConstants.vallyMeterLength+JSConstants.flatMeterLength+JSConstants.peakMeterLength+JSConstants.sharpMeterLength+JSConstants.cardBalanceLength+JSConstants.cardCostLength+JSConstants.connectorIdLength+JSConstants.meterStopLength+JSConstants.stopTimeLength+JSConstants.startMeterLength+JSConstants.startTimeLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength+JSConstants.boostVoltageTypeLength+JSConstants.transactionIdLength);
                    String peakTime = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.peakTimeLength,JSConstants.sharpTimeLength+JSConstants.valleyPriceLength+JSConstants.flatPriceLength+JSConstants.peakPriceLength+JSConstants.charpPriceLength+JSConstants.vallyCostLength+JSConstants.flatCostLength+JSConstants.peakCostLength+JSConstants.sharpCostLength+JSConstants.vallyMeterLength+JSConstants.flatMeterLength+JSConstants.peakMeterLength+JSConstants.sharpMeterLength+JSConstants.cardBalanceLength+JSConstants.cardCostLength+JSConstants.connectorIdLength+JSConstants.meterStopLength+JSConstants.stopTimeLength+JSConstants.startMeterLength+JSConstants.startTimeLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength+JSConstants.boostVoltageTypeLength+JSConstants.transactionIdLength);
                    String flatTime = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.flatTimeLength,JSConstants.peakTimeLength+JSConstants.sharpTimeLength+JSConstants.valleyPriceLength+JSConstants.flatPriceLength+JSConstants.peakPriceLength+JSConstants.charpPriceLength+JSConstants.vallyCostLength+JSConstants.flatCostLength+JSConstants.peakCostLength+JSConstants.sharpCostLength+JSConstants.vallyMeterLength+JSConstants.flatMeterLength+JSConstants.peakMeterLength+JSConstants.sharpMeterLength+JSConstants.cardBalanceLength+JSConstants.cardCostLength+JSConstants.connectorIdLength+JSConstants.meterStopLength+JSConstants.stopTimeLength+JSConstants.startMeterLength+JSConstants.startTimeLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength+JSConstants.boostVoltageTypeLength+JSConstants.transactionIdLength);
                    String vallyTime = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.vallyTimeLength,JSConstants.flatTimeLength+JSConstants.peakTimeLength+JSConstants.sharpTimeLength+JSConstants.valleyPriceLength+JSConstants.flatPriceLength+JSConstants.peakPriceLength+JSConstants.charpPriceLength+JSConstants.vallyCostLength+JSConstants.flatCostLength+JSConstants.peakCostLength+JSConstants.sharpCostLength+JSConstants.vallyMeterLength+JSConstants.flatMeterLength+JSConstants.peakMeterLength+JSConstants.sharpMeterLength+JSConstants.cardBalanceLength+JSConstants.cardCostLength+JSConstants.connectorIdLength+JSConstants.meterStopLength+JSConstants.stopTimeLength+JSConstants.startMeterLength+JSConstants.startTimeLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength+JSConstants.boostVoltageTypeLength+JSConstants.transactionIdLength);
                    String stopReason = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.stopReasonLength,JSConstants.vallyTimeLength+JSConstants.flatTimeLength+JSConstants.peakTimeLength+JSConstants.sharpTimeLength+JSConstants.valleyPriceLength+JSConstants.flatPriceLength+JSConstants.peakPriceLength+JSConstants.charpPriceLength+JSConstants.vallyCostLength+JSConstants.flatCostLength+JSConstants.peakCostLength+JSConstants.sharpCostLength+JSConstants.vallyMeterLength+JSConstants.flatMeterLength+JSConstants.peakMeterLength+JSConstants.sharpMeterLength+JSConstants.cardBalanceLength+JSConstants.cardCostLength+JSConstants.connectorIdLength+JSConstants.meterStopLength+JSConstants.stopTimeLength+JSConstants.startMeterLength+JSConstants.startTimeLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength+JSConstants.boostVoltageTypeLength+JSConstants.transactionIdLength);
                    String meterValueCount = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.meterValueCountLength,JSConstants.stopReasonLength+JSConstants.vallyTimeLength+JSConstants.flatTimeLength+JSConstants.peakTimeLength+JSConstants.sharpTimeLength+JSConstants.valleyPriceLength+JSConstants.flatPriceLength+JSConstants.peakPriceLength+JSConstants.charpPriceLength+JSConstants.vallyCostLength+JSConstants.flatCostLength+JSConstants.peakCostLength+JSConstants.sharpCostLength+JSConstants.vallyMeterLength+JSConstants.flatMeterLength+JSConstants.peakMeterLength+JSConstants.sharpMeterLength+JSConstants.cardBalanceLength+JSConstants.cardCostLength+JSConstants.connectorIdLength+JSConstants.meterStopLength+JSConstants.stopTimeLength+JSConstants.startMeterLength+JSConstants.startTimeLength+JSConstants.userIdTagLength+JSConstants.accountTypeLength+JSConstants.boostVoltageTypeLength+JSConstants.transactionIdLength);

                    log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - transactionId  :" + transactionId);
                    log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - boostVoltageType  :" + boostVoltageType);
                    log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - accountType  :" + accountType);
                    log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - userIdTag  :" + userIdTag);
                    log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - startTime  :" + startTime);
                    log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - startMeter  :" + startMeter);
                    log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - stopTime  :" + stopTime);
                    log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - meterStop  :" + meterStop);
                    log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - connectorId  :" + connectorId);
                    log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - cardCost  :" + cardCost);
                    log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - cardBalance  :" + cardBalance);
                    log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - sharpMeter  :" + sharpMeter);
                    log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - peakMeter  :" + peakMeter);
                    log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - flatMeter  :" + flatMeter);
                    log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - vallyMeter  :" + vallyMeter);
                    log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - sharpCost  :" + sharpCost);
                    log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - peakCost  :" + peakCost);
                    log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - flatCost  :" + flatCost);
                    log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - vallyCost  :" + vallyCost);
                    log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - charpPrice  :" + charpPrice);
                    log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - peakPrice  :" + peakPrice);
                    log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - flatPrice  :" + flatPrice);
                    log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - valleyPrice  :" + valleyPrice);
                    log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - sharpTime  :" + sharpTime);
                    log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - peakTime  :" + peakTime);
                    log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - flatTime  :" + flatTime);
                    log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - vallyTime  :" + vallyTime);
                    log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - stopReason  :" + stopReason);
                    log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - meterValueCount  :" + meterValueCount);
                    /** -------------------------------存redis----------------------------------*/
                    String cabStr = SpringUtils.getBean(RedisCache.class).getCacheObject(destIdNumber+"_"+connectorId+JSConstants.JS_KEY_STATUE);//获取当前订单信息
                    ChargeAndBraStatue cab = JSON.parseObject(cabStr, ChargeAndBraStatue.class);
                    String clogStr = SpringUtils.getBean(RedisCache.class).getCacheObject(cab.getSessionId());//获取当前订单信息
                    ChargingLogVO clog = JSON.parseObject(clogStr, ChargingLogVO.class);
                    clog.setEndTime(stopTime);
                    clog.setEndValue(meterStop);
                    clog.setStopReason("测试远程停止后续修改");//结束原因
                    clog.setChargeStatus("2");
                    SpringUtils.getBean(RedisCache.class).setCacheObject(cab.getSessionId(),JSON.toJSONString(clog));

                    /** -------------------------------存redis----------------------------------*/
                    /** 功能代码*/
                    returnDataPacketFunctionCode = "14";
                    /** ------------------------------------data 数据 start -----------------------**/
                    /**
                     * 使用交易号去查询订单。余额、手机等信息、充电电量、充电金额等信息
                     */

                    /** U8 returnCode	0x01: 成功； 0x02: 校验失败，重新发送； */
                    String returnCode = "01";
                    /**0x00: accept；接受  0x01: block；闭塞  0x02: expired;过期； 0x03: invalid;无效； 0x04: concurrentTx;  0x05: passwordNotCorrect;密码不正确；*/
                    String returnStatus = "01";
                    /** U32 expiryDate	有效期*/
                    String returnExpiryDate = JSUtils.publicIntToHex2((CnovertSystem.getSecondTimestampTwo(new Date())+7200*24)+"", JSConstants.expiryDateLength);
                    /** U8 parentIdTag[32+1]	用户组id */
                    String parentIdTag = JSUtils.publicStringToASCIIHex(userIdTag, JSConstants.parentIdTagLength);//JSUtils.publicBytesToASCIIByIndex
                    /** U8 cardNumber[32+1] 	卡号 */
                    String cardNumber =  JSUtils.publicStringToASCIIHex("0", JSConstants.cardNumberLength);
                    /** U8 phoneNumber[32+ 1] 用户手机号 */
                    String phoneNumber = JSUtils.publicStringToASCIIHex("0", JSConstants.phoneNumberLength);
                    /** int balance	余额 分*/
                    String balance = JSUtils.publicIntToHex(String.valueOf((int)NumberUtil.mul(Double.parseDouble(clog.getSurplusMoney()==null||"".equals(clog.getSurplusMoney())?"0":clog.getSurplusMoney()),100)), JSConstants.balanceLength);
                    /** U32meterValue	U32充电电量 单位：0.01kwh。*/
                    String meterValue = JSUtils.publicIntToHex(clog.getChargerValue(), JSConstants.meterValueLength);//"0000";
                    /** U32money	U32充电金额  单位，0.01元*/
                    BigDecimal sub = NumberUtil.sub(clog.getPayMoney()==null||"".equals(clog.getPayMoney())?"0":clog.getPayMoney(), clog.getSurplusMoney()==null||"".equals(clog.getSurplusMoney())?"0":clog.getSurplusMoney());
                    int mul1 = (int)NumberUtil.mul(Double.parseDouble(String.valueOf(sub)), 100);
                    String money = JSUtils.publicIntToHex(mul1+"", JSConstants.balanceLength);
                    /** U8 transactionId[4]	后台交易号 暂时默认  10000011*/
                    String returnTransactionId = JSUtils.publicIntToHex("1602125312",4);  //需要转16进制
                    returnDataPacketData
                            .append(returnCode).append(returnStatus)
                            .append(returnExpiryDate).append(parentIdTag)
                            .append(cardNumber).append(phoneNumber)
                            .append(balance).append(meterValue)
                            .append(money).append(returnTransactionId);
                    /** ------------------------------------data 数据 start -----------------------**/
                    /** 停止状态更新*/
                    if(nettyServerHandler.remoteStartStatus.containsKey(cab.getTransactionId())){
                        nettyServerHandler.beeChargerService.updateOrderLogStatusByIdTag(cab.getSessionId(),"3");

                        StartupStatus startupStatus = nettyServerHandler.remoteStartStatus.get(cab.getTransactionId());
                        /**总服务费*/
                        clog.setServiceFee(StrUtil.isEmpty(clog.getServiceFee())?"0":new BigDecimal(clog.getServiceFee()).setScale(2,BigDecimal.ROUND_UP).toString());
                        /** 总电费*/
                        clog.setElecPrice(StrUtil.isEmpty(clog.getElecPrice())?"0":new BigDecimal(clog.getElecPrice()).setScale(2,BigDecimal.ROUND_UP).toString());
                        /** 总费用*/
                        clog.setTotalprice(NumberUtil.add(clog.getServiceFee(), clog.getElecPrice()).toString());
                        /** 剩余金额*/
                        clog.setSurplusMoney(NumberUtil.sub(clog.getPayMoney(), clog.getTotalprice()).toString());
                        //更新redis
                        SpringUtils.getBean(RedisCache.class).setCacheObject(cab.getSessionId(),JSON.toJSONString(clog));
                        //                        入库 ，更新redis
                        if(clog != null) {
                            BeeTdCharginglog beeTdCharginglog = saveBeeTdCharGinLog(clog);
                            if(beeTdCharginglog!= null &&  StrUtil.isNotEmpty(beeTdCharginglog.getId())){
                                int resultCount = nettyServerHandler.beeTdCharginglogService.insertBeeTdCharginglog(beeTdCharginglog);
                                if(resultCount > 0 ) {
                                    log.info("【客户端:[" + destIdNumber + "]】"+ "结束充电，插入数据库ChargingLog            【成功】" );
                                }else{
                                    log.info("【客户端:[" + destIdNumber + "]】"+ "结束充电，插入数据库ChargingLog               【失败】" );
                                }

                            }
                        }else{
                            log.info("【客户端:[" + destIdNumber + "]】"+ "结束充电，插入数据库ChargingLog 【失败】 未找到 redis 数据 cLog 为null" );
                        }
                        if(StrUtil.isNotEmpty(startupStatus.getStopForm()) && "remote".equals(startupStatus.getStopForm())){
                            //如果是手动停止的话 修改状态码
                            startupStatus.setStatus("2");
                            nettyServerHandler.remoteStartStatus.put(cab.getTransactionId(),startupStatus);
                            JSONObject json = new JSONObject();
                            /** sessionId  唯一标识
                             *  refundMoney  退款钱数（单位为分）*/
                            json.put("sessionId",clog.getSessionId());
                            json.put("refundMoney",Double.parseDouble(clog.getSurplusMoney())*100);
                            Map result = JSON.parseObject(HttpRequest.post(nettyServerHandler.serverUrl)
                                    .header("accesstocken",clog.getToken())
                                    .body(json.toString())
                                    .execute().body());
                            String  msg = String.valueOf(result.get("message"));
                            String code = String.valueOf(result.get("code"));
                            if(Integer.parseInt(code) == 1000){
                                //成功
                                log.info("【客户端:[" + destIdNumber + "]】"+ " --【远程停止充电调用后端服务接口退款成功】[退款金额 ："+json.get("refundMoney")+"]--日志内容："+msg );
                            }else{
                                log.error("【客户端:[" + destIdNumber + "]】"+ " --【远程停止充电调用后端服务接口退款*失败】[退款金额 ："+json.get("refundMoney")+"]--失败原因："+msg );
                            }
                        }else{
                            //自动停止
//                            startupStatus.setStatus("2");
//                            nettyServerHandler.remoteStartStatus.put(userIdTag,startupStatus);
                            /**销毁MAP*/
                            nettyServerHandler.remoteStartStatus.remove(cab.getTransactionId());
                            log.info("【客户端:[" + destIdNumber + "]】" + " -桩自动停止充电成功------------------------------------------");
                            //可以直接销毁 .....map
                            //调用自动停止接口，走退款流程等
                            JSONObject json = new JSONObject();
                            /** sessionId  唯一标识
                             *  refundMoney  退款钱数（单位为分）*/
                            json.put("sessionId",clog.getSessionId());
                            json.put("refundMoney",Double.parseDouble(clog.getSurplusMoney())*100);
                            Map result = JSON.parseObject(HttpRequest.post(nettyServerHandler.serverUrl)
                                    .header("accesstocken",clog.getToken())
                                    .body(json.toString())
                                    .execute().body());
                            String msg = null;
                            String code = null;
                            if(result!=null ) {
                                 msg = String.valueOf(result.get("message"));
                                 code = String.valueOf(result.get("code"));
                                if(Integer.parseInt(code) == 1000){
                                    //成功
                                    log.info("【客户端:[" + destIdNumber + "]】"+ " --【自动停止充电调用后端服务接口退款成功】--日志内容："+msg );
                                }else{
                                    log.error("【客户端:[" + destIdNumber + "]】"+ " --【自动停止充电调用后端服务接口退款*失败】--失败原因："+msg );
                                }
                            }else{
                                log.error("【客户端:[" + destIdNumber + "]】"+ " --【自动停止充电调用后端服务接口退款*失败】--失败原因："+msg );
                            }
                        }
                        log.info("【客户端:[" + destIdNumber + "]】"+ " --【桩号-枪号】--日志内容："+clog.getChargerNo()+" - "+clog.getBranchNo() );
                        log.info("【客户端:[" + destIdNumber + "]】"+ " --【开始充电 电池SOC】--日志内容："+clog.getStartSoc() );
                        log.info("【客户端:[" + destIdNumber + "]】"+ " --【结束充电 电池SOC】--日志内容："+clog.getEndSoc() );
                        log.info("【客户端:[" + destIdNumber + "]】"+ " --【费用结算】--本次充电时长："+clog.getChargerLength() +"分" );
                        log.info("【客户端:[" + destIdNumber + "]】"+ " --【总服务费】--日志内容："+clog.getServiceFee() );
                        log.info("【客户端:[" + destIdNumber + "]】"+ " --【。总电费】--日志内容："+clog.getElecPrice() );
                        log.info("【客户端:[" + destIdNumber + "]】"+ " --【。总费用】--日志内容："+clog.getTotalprice() );
                        log.info("【客户端:[" + destIdNumber + "]】"+ " --【预付金额】--日志内容："+clog.getPayMoney() );
                        log.info("【客户端:[" + destIdNumber + "]】"+ " --【剩余金额】--日志内容："+clog.getSurplusMoney() );
                    }
                } else if ("18".equals(dataPacketFunctionCode)) {
                String destIdNumber = nettyServerHandler.pileMap.get(channelId.toString()).getDestIdNumber();
                log.info("【客户端:[" + destIdNumber + "]】"+ " ----------------------【嘉盛充电详细信息解析】------------------------" );
                 String transactionId = JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.transactionIdLength,0);
                String connectorId=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.connectorIdLength,JSConstants.transactionIdLength);
                String timestamp=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.timestampLength,JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String meterValue=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.meterValueLength,JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String CurrentImport=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.CurrentImportLength,JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String temperature=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.temperatureLength,JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String voltageImport=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.voltageImportLength,JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String chargedTime=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.chargedTimeLength,JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String acVoltageA=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.acVoltageALength,JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String acVoltageB=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.acVoltageBLength,JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String acVoltageC=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.acVoltageCLength,JSConstants.acVoltageBLength+JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String acCurrentA=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.acCurrentALength,JSConstants.acVoltageCLength+JSConstants.acVoltageBLength+JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String acCurrentB=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.acCurrentBLength,JSConstants.acCurrentALength+JSConstants.acVoltageCLength+JSConstants.acVoltageBLength+JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String acCurrentC=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.acCurrentCLength,JSConstants.acCurrentBLength+JSConstants.acCurrentALength+JSConstants.acVoltageCLength+JSConstants.acVoltageBLength+JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String acPower=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.acPowerLength,JSConstants.acCurrentCLength+JSConstants.acCurrentBLength+JSConstants.acCurrentALength+JSConstants.acVoltageCLength+JSConstants.acVoltageBLength+JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String acPowerA=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.acPowerALength,JSConstants.acPowerLength+JSConstants.acCurrentCLength+JSConstants.acCurrentBLength+JSConstants.acCurrentALength+JSConstants.acVoltageCLength+JSConstants.acVoltageBLength+JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String acPowerB=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.acPowerBLength,JSConstants.acPowerALength+JSConstants.acPowerLength+JSConstants.acCurrentCLength+JSConstants.acCurrentBLength+JSConstants.acCurrentALength+JSConstants.acVoltageCLength+JSConstants.acVoltageBLength+JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String acPowerC=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.acPowerCLength,JSConstants.acPowerBLength+JSConstants.acPowerALength+JSConstants.acPowerLength+JSConstants.acCurrentCLength+JSConstants.acCurrentBLength+JSConstants.acCurrentALength+JSConstants.acVoltageCLength+JSConstants.acVoltageBLength+JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String phaseA=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.phaseALength,JSConstants.acPowerCLength+JSConstants.acPowerBLength+JSConstants.acPowerALength+JSConstants.acPowerLength+JSConstants.acCurrentCLength+JSConstants.acCurrentBLength+JSConstants.acCurrentALength+JSConstants.acVoltageCLength+JSConstants.acVoltageBLength+JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String phaseB=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.phaseBLength,JSConstants.phaseALength+JSConstants.acPowerCLength+JSConstants.acPowerBLength+JSConstants.acPowerALength+JSConstants.acPowerLength+JSConstants.acCurrentCLength+JSConstants.acCurrentBLength+JSConstants.acCurrentALength+JSConstants.acVoltageCLength+JSConstants.acVoltageBLength+JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String phaseC=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.phaseCLength,JSConstants.phaseBLength+JSConstants.phaseALength+JSConstants.acPowerCLength+JSConstants.acPowerBLength+JSConstants.acPowerALength+JSConstants.acPowerLength+JSConstants.acCurrentCLength+JSConstants.acCurrentBLength+JSConstants.acCurrentALength+JSConstants.acVoltageCLength+JSConstants.acVoltageBLength+JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String remainTime=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.remainTimeLength,JSConstants.phaseCLength+JSConstants.phaseBLength+JSConstants.phaseALength+JSConstants.acPowerCLength+JSConstants.acPowerBLength+JSConstants.acPowerALength+JSConstants.acPowerLength+JSConstants.acCurrentCLength+JSConstants.acCurrentBLength+JSConstants.acCurrentALength+JSConstants.acVoltageCLength+JSConstants.acVoltageBLength+JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String requestVoltage=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.requestVoltageLength,JSConstants.remainTimeLength+JSConstants.phaseCLength+JSConstants.phaseBLength+JSConstants.phaseALength+JSConstants.acPowerCLength+JSConstants.acPowerBLength+JSConstants.acPowerALength+JSConstants.acPowerLength+JSConstants.acCurrentCLength+JSConstants.acCurrentBLength+JSConstants.acCurrentALength+JSConstants.acVoltageCLength+JSConstants.acVoltageBLength+JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String requestCurrent=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.requestCurrentLength,JSConstants.requestVoltageLength+JSConstants.remainTimeLength+JSConstants.phaseCLength+JSConstants.phaseBLength+JSConstants.phaseALength+JSConstants.acPowerCLength+JSConstants.acPowerBLength+JSConstants.acPowerALength+JSConstants.acPowerLength+JSConstants.acCurrentCLength+JSConstants.acCurrentBLength+JSConstants.acCurrentALength+JSConstants.acVoltageCLength+JSConstants.acVoltageBLength+JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String bmsVoltage=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.bmsVoltageLength,JSConstants.requestCurrentLength+JSConstants.requestVoltageLength+JSConstants.remainTimeLength+JSConstants.phaseCLength+JSConstants.phaseBLength+JSConstants.phaseALength+JSConstants.acPowerCLength+JSConstants.acPowerBLength+JSConstants.acPowerALength+JSConstants.acPowerLength+JSConstants.acCurrentCLength+JSConstants.acCurrentBLength+JSConstants.acCurrentALength+JSConstants.acVoltageCLength+JSConstants.acVoltageBLength+JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String bmsCurrent=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.bmsCurrentLength,JSConstants.bmsVoltageLength+JSConstants.requestCurrentLength+JSConstants.requestVoltageLength+JSConstants.remainTimeLength+JSConstants.phaseCLength+JSConstants.phaseBLength+JSConstants.phaseALength+JSConstants.acPowerCLength+JSConstants.acPowerBLength+JSConstants.acPowerALength+JSConstants.acPowerLength+JSConstants.acCurrentCLength+JSConstants.acCurrentBLength+JSConstants.acCurrentALength+JSConstants.acVoltageCLength+JSConstants.acVoltageBLength+JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String soc=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.socLength,JSConstants.bmsCurrentLength+JSConstants.bmsVoltageLength+JSConstants.requestCurrentLength+JSConstants.requestVoltageLength+JSConstants.remainTimeLength+JSConstants.phaseCLength+JSConstants.phaseBLength+JSConstants.phaseALength+JSConstants.acPowerCLength+JSConstants.acPowerBLength+JSConstants.acPowerALength+JSConstants.acPowerLength+JSConstants.acCurrentCLength+JSConstants.acCurrentBLength+JSConstants.acCurrentALength+JSConstants.acVoltageCLength+JSConstants.acVoltageBLength+JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String maxBatTemp=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.maxBatTempLength,JSConstants.socLength+JSConstants.bmsCurrentLength+JSConstants.bmsVoltageLength+JSConstants.requestCurrentLength+JSConstants.requestVoltageLength+JSConstants.remainTimeLength+JSConstants.phaseCLength+JSConstants.phaseBLength+JSConstants.phaseALength+JSConstants.acPowerCLength+JSConstants.acPowerBLength+JSConstants.acPowerALength+JSConstants.acPowerLength+JSConstants.acCurrentCLength+JSConstants.acCurrentBLength+JSConstants.acCurrentALength+JSConstants.acVoltageCLength+JSConstants.acVoltageBLength+JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String maxBatTIdx=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.maxBatTIdxLength,JSConstants.maxBatTempLength+JSConstants.socLength+JSConstants.bmsCurrentLength+JSConstants.bmsVoltageLength+JSConstants.requestCurrentLength+JSConstants.requestVoltageLength+JSConstants.remainTimeLength+JSConstants.phaseCLength+JSConstants.phaseBLength+JSConstants.phaseALength+JSConstants.acPowerCLength+JSConstants.acPowerBLength+JSConstants.acPowerALength+JSConstants.acPowerLength+JSConstants.acCurrentCLength+JSConstants.acCurrentBLength+JSConstants.acCurrentALength+JSConstants.acVoltageCLength+JSConstants.acVoltageBLength+JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String minBatTemp=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.minBatTempLength,JSConstants.maxBatTIdxLength+JSConstants.maxBatTempLength+JSConstants.socLength+JSConstants.bmsCurrentLength+JSConstants.bmsVoltageLength+JSConstants.requestCurrentLength+JSConstants.requestVoltageLength+JSConstants.remainTimeLength+JSConstants.phaseCLength+JSConstants.phaseBLength+JSConstants.phaseALength+JSConstants.acPowerCLength+JSConstants.acPowerBLength+JSConstants.acPowerALength+JSConstants.acPowerLength+JSConstants.acCurrentCLength+JSConstants.acCurrentBLength+JSConstants.acCurrentALength+JSConstants.acVoltageCLength+JSConstants.acVoltageBLength+JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String minBatTIdx=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.minBatTIdxLength,JSConstants.minBatTempLength+JSConstants.maxBatTIdxLength+JSConstants.maxBatTempLength+JSConstants.socLength+JSConstants.bmsCurrentLength+JSConstants.bmsVoltageLength+JSConstants.requestCurrentLength+JSConstants.requestVoltageLength+JSConstants.remainTimeLength+JSConstants.phaseCLength+JSConstants.phaseBLength+JSConstants.phaseALength+JSConstants.acPowerCLength+JSConstants.acPowerBLength+JSConstants.acPowerALength+JSConstants.acPowerLength+JSConstants.acCurrentCLength+JSConstants.acCurrentBLength+JSConstants.acCurrentALength+JSConstants.acVoltageCLength+JSConstants.acVoltageBLength+JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String maxBatCellVol=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.maxBatCellVolLength,JSConstants.minBatTIdxLength+JSConstants.minBatTempLength+JSConstants.maxBatTIdxLength+JSConstants.maxBatTempLength+JSConstants.socLength+JSConstants.bmsCurrentLength+JSConstants.bmsVoltageLength+JSConstants.requestCurrentLength+JSConstants.requestVoltageLength+JSConstants.remainTimeLength+JSConstants.phaseCLength+JSConstants.phaseBLength+JSConstants.phaseALength+JSConstants.acPowerCLength+JSConstants.acPowerBLength+JSConstants.acPowerALength+JSConstants.acPowerLength+JSConstants.acCurrentCLength+JSConstants.acCurrentBLength+JSConstants.acCurrentALength+JSConstants.acVoltageCLength+JSConstants.acVoltageBLength+JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String maxBatCellVolGrp=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.maxBatCellVolGrpLength,JSConstants.maxBatCellVolLength+JSConstants.minBatTIdxLength+JSConstants.minBatTempLength+JSConstants.maxBatTIdxLength+JSConstants.maxBatTempLength+JSConstants.socLength+JSConstants.bmsCurrentLength+JSConstants.bmsVoltageLength+JSConstants.requestCurrentLength+JSConstants.requestVoltageLength+JSConstants.remainTimeLength+JSConstants.phaseCLength+JSConstants.phaseBLength+JSConstants.phaseALength+JSConstants.acPowerCLength+JSConstants.acPowerBLength+JSConstants.acPowerALength+JSConstants.acPowerLength+JSConstants.acCurrentCLength+JSConstants.acCurrentBLength+JSConstants.acCurrentALength+JSConstants.acVoltageCLength+JSConstants.acVoltageBLength+JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String vinLen=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.vinLenLength,JSConstants.maxBatCellVolGrpLength+JSConstants.maxBatCellVolLength+JSConstants.minBatTIdxLength+JSConstants.minBatTempLength+JSConstants.maxBatTIdxLength+JSConstants.maxBatTempLength+JSConstants.socLength+JSConstants.bmsCurrentLength+JSConstants.bmsVoltageLength+JSConstants.requestCurrentLength+JSConstants.requestVoltageLength+JSConstants.remainTimeLength+JSConstants.phaseCLength+JSConstants.phaseBLength+JSConstants.phaseALength+JSConstants.acPowerCLength+JSConstants.acPowerBLength+JSConstants.acPowerALength+JSConstants.acPowerLength+JSConstants.acCurrentCLength+JSConstants.acCurrentBLength+JSConstants.acCurrentALength+JSConstants.acVoltageCLength+JSConstants.acVoltageBLength+JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String VIN=JSUtils.publicBytesToASCIIByIndex(dataPacketDataBytes,JSConstants.VINLength,JSConstants.vinLenLength+JSConstants.maxBatCellVolGrpLength+JSConstants.maxBatCellVolLength+JSConstants.minBatTIdxLength+JSConstants.minBatTempLength+JSConstants.maxBatTIdxLength+JSConstants.maxBatTempLength+JSConstants.socLength+JSConstants.bmsCurrentLength+JSConstants.bmsVoltageLength+JSConstants.requestCurrentLength+JSConstants.requestVoltageLength+JSConstants.remainTimeLength+JSConstants.phaseCLength+JSConstants.phaseBLength+JSConstants.phaseALength+JSConstants.acPowerCLength+JSConstants.acPowerBLength+JSConstants.acPowerALength+JSConstants.acPowerLength+JSConstants.acCurrentCLength+JSConstants.acCurrentBLength+JSConstants.acCurrentALength+JSConstants.acVoltageCLength+JSConstants.acVoltageBLength+JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String gunTemp=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.gunTempLength,JSConstants.VINLength+JSConstants.vinLenLength+JSConstants.maxBatCellVolGrpLength+JSConstants.maxBatCellVolLength+JSConstants.minBatTIdxLength+JSConstants.minBatTempLength+JSConstants.maxBatTIdxLength+JSConstants.maxBatTempLength+JSConstants.socLength+JSConstants.bmsCurrentLength+JSConstants.bmsVoltageLength+JSConstants.requestCurrentLength+JSConstants.requestVoltageLength+JSConstants.remainTimeLength+JSConstants.phaseCLength+JSConstants.phaseBLength+JSConstants.phaseALength+JSConstants.acPowerCLength+JSConstants.acPowerBLength+JSConstants.acPowerALength+JSConstants.acPowerLength+JSConstants.acCurrentCLength+JSConstants.acCurrentBLength+JSConstants.acCurrentALength+JSConstants.acVoltageCLength+JSConstants.acVoltageBLength+JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String BRMVoltage=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.BRMVoltageLength,JSConstants.gunTempLength+JSConstants.VINLength+JSConstants.vinLenLength+JSConstants.maxBatCellVolGrpLength+JSConstants.maxBatCellVolLength+JSConstants.minBatTIdxLength+JSConstants.minBatTempLength+JSConstants.maxBatTIdxLength+JSConstants.maxBatTempLength+JSConstants.socLength+JSConstants.bmsCurrentLength+JSConstants.bmsVoltageLength+JSConstants.requestCurrentLength+JSConstants.requestVoltageLength+JSConstants.remainTimeLength+JSConstants.phaseCLength+JSConstants.phaseBLength+JSConstants.phaseALength+JSConstants.acPowerCLength+JSConstants.acPowerBLength+JSConstants.acPowerALength+JSConstants.acPowerLength+JSConstants.acCurrentCLength+JSConstants.acCurrentBLength+JSConstants.acCurrentALength+JSConstants.acVoltageCLength+JSConstants.acVoltageBLength+JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String BRMBmsRateCap=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.BRMBmsRateCapLength,JSConstants.BRMVoltageLength+JSConstants.gunTempLength+JSConstants.VINLength+JSConstants.vinLenLength+JSConstants.maxBatCellVolGrpLength+JSConstants.maxBatCellVolLength+JSConstants.minBatTIdxLength+JSConstants.minBatTempLength+JSConstants.maxBatTIdxLength+JSConstants.maxBatTempLength+JSConstants.socLength+JSConstants.bmsCurrentLength+JSConstants.bmsVoltageLength+JSConstants.requestCurrentLength+JSConstants.requestVoltageLength+JSConstants.remainTimeLength+JSConstants.phaseCLength+JSConstants.phaseBLength+JSConstants.phaseALength+JSConstants.acPowerCLength+JSConstants.acPowerBLength+JSConstants.acPowerALength+JSConstants.acPowerLength+JSConstants.acCurrentCLength+JSConstants.acCurrentBLength+JSConstants.acCurrentALength+JSConstants.acVoltageCLength+JSConstants.acVoltageBLength+JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String BCPNominalEnergy=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.BCPNominalEnergyLength,JSConstants.BRMBmsRateCapLength+JSConstants.BRMVoltageLength+JSConstants.gunTempLength+JSConstants.VINLength+JSConstants.vinLenLength+JSConstants.maxBatCellVolGrpLength+JSConstants.maxBatCellVolLength+JSConstants.minBatTIdxLength+JSConstants.minBatTempLength+JSConstants.maxBatTIdxLength+JSConstants.maxBatTempLength+JSConstants.socLength+JSConstants.bmsCurrentLength+JSConstants.bmsVoltageLength+JSConstants.requestCurrentLength+JSConstants.requestVoltageLength+JSConstants.remainTimeLength+JSConstants.phaseCLength+JSConstants.phaseBLength+JSConstants.phaseALength+JSConstants.acPowerCLength+JSConstants.acPowerBLength+JSConstants.acPowerALength+JSConstants.acPowerLength+JSConstants.acCurrentCLength+JSConstants.acCurrentBLength+JSConstants.acCurrentALength+JSConstants.acVoltageCLength+JSConstants.acVoltageBLength+JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
                String BCPChargeVoltageMax=JSUtils.publicBytesToIntByIndex(dataPacketDataBytes,JSConstants.BCPChargeVoltageMaxLength,JSConstants.BCPNominalEnergyLength+JSConstants.BRMBmsRateCapLength+JSConstants.BRMVoltageLength+JSConstants.gunTempLength+JSConstants.VINLength+JSConstants.vinLenLength+JSConstants.maxBatCellVolGrpLength+JSConstants.maxBatCellVolLength+JSConstants.minBatTIdxLength+JSConstants.minBatTempLength+JSConstants.maxBatTIdxLength+JSConstants.maxBatTempLength+JSConstants.socLength+JSConstants.bmsCurrentLength+JSConstants.bmsVoltageLength+JSConstants.requestCurrentLength+JSConstants.requestVoltageLength+JSConstants.remainTimeLength+JSConstants.phaseCLength+JSConstants.phaseBLength+JSConstants.phaseALength+JSConstants.acPowerCLength+JSConstants.acPowerBLength+JSConstants.acPowerALength+JSConstants.acPowerLength+JSConstants.acCurrentCLength+JSConstants.acCurrentBLength+JSConstants.acCurrentALength+JSConstants.acVoltageCLength+JSConstants.acVoltageBLength+JSConstants.acVoltageALength+JSConstants.chargedTimeLength+JSConstants.voltageImportLength+JSConstants.temperatureLength+JSConstants.CurrentImportLength+JSConstants.meterValueLength+JSConstants.timestampLength+JSConstants.connectorIdLength+JSConstants.transactionIdLength);
//                Date timestampDate = new Date(Long.parseLong(timestamp) * 1000);
//                String timestampFormat = DateUtil.format(timestampDate, "yyyy-MM-dd HH:mm:ss");
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - 交易号 transactionId  :" + transactionId);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - 枪号 connectorId  :" + connectorId);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - 当前时间 UTC timestamp  :" + timestamp);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - 电量 wh meterValue  :" + meterValue);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - CurrentImport  :" + CurrentImport);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - temperature  :" + temperature);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - voltageImport  :" + voltageImport);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - 已充时间 s chargedTime  :" + chargedTime);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - acVoltageA  :" + acVoltageA);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - acVoltageB  :" + acVoltageB);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - acVoltageC  :" + acVoltageC);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - acCurrentA  :" + acCurrentA);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - acCurrentB  :" + acCurrentB);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - acCurrentC  :" + acCurrentC);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - acPower  :" + acPower);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - acPowerA  :" + acPowerA);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - acPowerB  :" + acPowerB);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - acPowerC  :" + acPowerC);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - phaseA  :" + phaseA);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - phaseB  :" + phaseB);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - phaseC  :" + phaseC);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - remainTime  :" + remainTime);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - requestVoltage  :" + requestVoltage);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - requestCurrent  :" + requestCurrent);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - 测量电压/0.1V bmsVoltage  :" + bmsVoltage);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - 测量电流/0.1A bmsCurrent  :" + bmsCurrent);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - 电池电量 soc  :" + soc);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - maxBatTemp  :" + maxBatTemp);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - maxBatTIdx  :" + maxBatTIdx);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - minBatTemp  :" + minBatTemp);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - minBatTIdx  :" + minBatTIdx);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - maxBatCellVol  :" + maxBatCellVol);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - maxBatCellVolGrp  :" + maxBatCellVolGrp);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - vinLen  :" + vinLen);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - 车辆 VIN  :" + VIN);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - gunTemp  :" + gunTemp);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - BRMVoltage  :" + BRMVoltage);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - BRMBmsRateCap  :" + BRMBmsRateCap);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - BCPNominalEnergy  :" + BCPNominalEnergy);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - BCPChargeVoltageMax  :" + BCPChargeVoltageMax);
                /** -----------------------------存redis------------------------------------------------ */
                //              远程启动的时候发送 交易号,
                String cabStr = SpringUtils.getBean(RedisCache.class).getCacheObject(destIdNumber+"_"+connectorId+JSConstants.JS_KEY_STATUE);//获取当前订单信息
                ChargeAndBraStatue cab = JSON.parseObject(cabStr, ChargeAndBraStatue.class);
                String clogStr = SpringUtils.getBean(RedisCache.class).getCacheObject(cab.getSessionId());//获取当前订单信息
                ChargingLogVO clog = JSON.parseObject(clogStr, ChargingLogVO.class);
                /**本次充电量 */
                Double thisTimeValue = null;
                /** 当前时间时区费率内容*/
                RateEntity thisTimestampZone = null;
                /** 上次时间时区费率内容*/
                RateEntity lastTimestampZone = null;
                /** 本次充电费用 */
                String thisChargeMoney = null;
                /** 本次充电时间*/
                Long thisDateTime = null;
                /**本次充电功率 = 电压 * 电流 /100(0.1AV) /3600(每秒) * thisDateTime本次充电时间 / 1000(wh 换算 kwh)
                 *  = 651 * 6996 / 100 /3600 / 1000  每秒的充电数量 kw*/
                String thisPower =NumberUtil.roundStr(Double.parseDouble(CurrentImport)*Double.parseDouble(voltageImport) / 100 / 3600  / 1000,4);
                /** 算完之后在进行set*/
                //查看是否是开始充电后的第一次 开始充电时候给的状态是 0  充电中需修改为1
                if (StrUtil.isNotEmpty(clog.getChargeStatus()) && "0".equals(clog.getChargeStatus())) {
                    nettyServerHandler.beeChargerService.updateOrderLogStatusByIdTag(cab.getSessionId(),"5");
                    clog.setChargeStatus("1");//充电中
                    clog.setStartSoc(soc);//开始充电电池百分比
                    clog.setBusNo(VIN);//车VIN号busNo

                    //用起始电表值计算
                    if(StrUtil.isNotEmpty(clog.getStartValue())){
                        thisTimeValue = getThisTimeValue(meterValue,clog.getStartValue());
                        /**本次充电时间/s = 当前时间-起始时间 */
                        /** 判断当前时间是否夸时区*/
                        if(thisTimeValue != 0){
                            thisTimestampZone = getNewDateStatus(Long.parseLong(timestamp));
                            lastTimestampZone = getNewDateStatus(Long.parseLong(clog.getStartTime()));
                        }

                    }

                }else{
                    /** 不是第一次请求 那么就用上次累积电表值计算 时间按累积电能表时间计算*/
                    if(StrUtil.isNotEmpty(clog.getSumValue())){
                        thisTimeValue = getThisTimeValue(meterValue,clog.getSumValue());
                        /**本次充电时间/s = 当前时间-起始时间 */
                        /** 判断当前时间是否夸时区*/
                        if(thisTimeValue != 0){
                            thisTimestampZone = getNewDateStatus(Long.parseLong(timestamp));
                            lastTimestampZone = getNewDateStatus(Long.parseLong(clog.getSumValueTime()));
                        }

                    }
                }
                if(thisTimestampZone!=null && lastTimestampZone != null ){
                /** -------------------本次电费计算 */
                CalculateTheCost calculateTheCost = getCalculateTheCost(thisTimestampZone,lastTimestampZone,thisTimeValue,thisPower,Float.parseFloat(timestamp),Float.parseFloat(clog.getStartTime()));
                //峰费
                clog.setPeakCharge(sumMoney(clog.getPeakCharge(),calculateTheCost.getPeakCharge()));
                clog.setPeakFee(sumMoney(clog.getPeakFee(),calculateTheCost.getPeakFee()));
                clog.setPeakPower(sumMoney(clog.getPeakPower(),calculateTheCost.getPeakPower()));
                clog.setPeakLength(sumMoney(clog.getPeakLength(),calculateTheCost.getPeakLength()));
                //平费
                clog.setFlatCharge(sumMoney(clog.getFlatCharge(),calculateTheCost.getFlatCharge()));
                clog.setFlatFee(sumMoney(clog.getFlatFee(),calculateTheCost.getFlatFee()));
                clog.setFlatPower(sumMoney(clog.getFlatPower(),calculateTheCost.getFlatPower()));
                clog.setFlatLength(sumMoney(clog.getFlatLength(),calculateTheCost.getFlatLength()));
                //谷费
                clog.setValleyCharge(sumMoney(clog.getValleyCharge(),calculateTheCost.getValleyCharge()));
                clog.setValleyFee(sumMoney(clog.getValleyFee(),calculateTheCost.getValleyFee()));
                clog.setValleyPower(sumMoney(clog.getValleyPower(),calculateTheCost.getValleyPower()));
                clog.setValleyLength(sumMoney(clog.getValleyLength(),calculateTheCost.getValleyLength()));
                //总费
                clog.setTotalprice(sumMoney(clog.getTotalprice(),calculateTheCost.getTotalprice()));
                clog.setElecPrice(sumMoney(clog.getElecPrice(),calculateTheCost.getElecPrice()));
                clog.setServiceFee(sumMoney(clog.getServiceFee(),calculateTheCost.getServiceFee()));
                clog.setSurplusMoney(String.valueOf(NumberUtil.sub(clog.getPayMoney(), clog.getTotalprice())));//剩余金额
                }
                /** ----------------------------------*/
                clog.setSumValueTime(timestamp);//本次结束时间
                clog.setSumValue(meterValue);//当前电量
                clog.setCurrentImport(CurrentImport==null || "".equals(CurrentImport) ? clog.getCurrentImport():CurrentImport);//电流 0.1/A
                clog.setVoltageImport(voltageImport==null || "".equals(voltageImport) ? clog.getVoltageImport():voltageImport);//电压 0.1/V
                clog.setChargerLength(String.valueOf(NumberUtil.div(Double.parseDouble(chargedTime),60,2)));//充电时长 min 分
                clog.setChargerValue(String.valueOf(NumberUtil.sub(meterValue,clog.getStartValue())));//已充电电量 wh
                clog.setEndSoc(soc);//结束充电电量
                SpringUtils.getBean(RedisCache.class).setCacheObject(cab.getSessionId(),JSON.toJSONString(clog));

                /** -----------------------------存redis------------------------------------------------ */
                /** -----------------------------剩余金额低于1.5元进行校验是否够充下次30秒时间电量------------------------------------------------ */
//                double surplusMoney = Double.parseDouble(clog.getSurplusMoney());
//                if(surplusMoney <= 1.5){
//                    double nextTime = stopCharge(clog.getCurrentImport(),clog.getVoltageImport(),lastTimestampZone,thisTimestampZone);
//                    if(surplusMoney<nextTime){
//                        /** 钱不够停止*/
//                    }
//                }
//                if(Double.parseDouble(soc) >=98){
//                    /** 电量充满停止*/
//                }
                /** -----------------------------低于1.5元进行校验是否够充下次30秒时间电量------------------------------------------------ */
                /** 功能代码*/
                returnDataPacketFunctionCode = "18";
                /** ------------------------------------data 数据 start ----------------------- */
                /** U8 returnCode  0x01:数据有效*/
                String returnCode = CnovertSystem.numToHex8(1); //"01";
                /** U8 transactionId[4]	后台交易号 暂时默认  10000011*/
                String returnTransactionId = JSUtils.publicIntToHex(transactionId,4);  //需要转16进制
                returnDataPacketData.append(returnCode).append(returnTransactionId);
                /** ------------------------------------data 数据 start -----------------------**/
            }else if ("1B".equals(dataPacketFunctionCode)) {
                String destIdNumber = nettyServerHandler.pileMap.get(channelId.toString()).getDestIdNumber();
                log.info("【客户端:[" + destIdNumber + "]】"+ " ----------------------【嘉盛VIN认证报文开始解析】------------------------" );
                /** 账户类型: 0x00:手机号 0x01:用户卡 0x02:微信 0x03:支付宝 0xFF:无法识别*/
                String SN = JSUtils.getSN(dataPacketDataBytes);
                String VIN = JSUtils.publicBytesToASCIIByIndex(dataPacketDataBytes,18,JSConstants.SNLength);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - SN  :" + SN);
                log.info("【客户端:[" + destIdNumber + "]】" + " -解析后 - VIN  :" + VIN);
                /** -----------------------------redis校验------------------------------------------------ */
                /** -----------------------------redis校验------------------------------------------------ */
                /** 功能代码*/
                returnDataPacketFunctionCode = "1B";
                /** ------------------------------------data 数据 start ----------------------- */
                /** U8 returnStatus	0x00:rejected 0x01:accepted*/
                String returnStatus = "00";
                returnDataPacketData.append(returnStatus);
                /** ------------------------------------data 数据 start -----------------------**/
                /** 充电完成账单上报报文*/
            }
            if(StrUtil.isNotEmpty(returnDataPacketData) && !"00".equals(returnDataPacketFunctionCode)){
                /** 回应 目的地ID*/
                returnDataPacketDestination = JSUtils.getReturnDataPacketDestination(nettyServerHandler.pileMap,channelId.toString());
                /** 回应 CRC16校验码 */
                returnDataPacketCRC16 = JSUtils.returnDataCRC16(returnDataPacketData.toString());
                /** 回应 数据包总DATA*/
                returnLengthSB.append(returnDataPacketID).append(returnDataPacketSource).append(returnDataPacketDestination).append(returnDataPacketFunctionCode).append(returnDataPacketData).append(returnDataPacketCRC16);
                /** 回应 的数据包总长度*/
                returnDataLength =JSUtils.returnDataLengthHEX(returnLengthSB.length()/2);
                /** 下面是返回总体16进制数据 */
                returnSB.append(returnHeader).append(returnDataLength).append(returnLengthSB.toString());
                log.info("【客户端:[" + nettyServerHandler.pileMap.get(channelId.toString()).getDestIdNumber() + "]】" + " -应答返回十六进制内容 ]:" + CnovertSystem.parseHexStrToHex(returnSB.toString()) );
                byte [] returnByte= CnovertSystem.hexStringToBytes(returnSB.toString());
                nettyServerHandler.writeMessage(ctx.channel().id(), returnByte);
            }else{
                log.error("【客户端:[" + nettyServerHandler.pileMap.get(channelId.toString()).getDestIdNumber() + "]】" + " -服务端错误未返回returnDataPacketData]:[" + returnDataPacketData +"]  或未填写功能代码: " +returnDataPacketFunctionCode );
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 根据功率时间钱计算是否够充值下次30秒
     * @param currentImport  电流
     * @param voltageImport  电压
     * @param lastTimestampZone 65*700
     * @param thisTimestampZone
     * @return
     */
    private static double stopCharge(String currentImport, String voltageImport, RateEntity lastTimestampZone, RateEntity thisTimestampZone) {
        /** 不需要判断是否夸时区*/
        double power = NumberUtil.div(NumberUtil.mul(currentImport, voltageImport), 100,3).doubleValue();
        double  sumTimePower= power*30/1000/3600;
        double sumMoney = NumberUtil.mul(NumberUtil.add(lastTimestampZone.getFee(), lastTimestampZone.getPrice()), sumTimePower, 2).doubleValue();
        return sumMoney;
    }


    //nettyServerHandler.pileMap,channelId.toString()
    public static void SyncPrice(ChannelHandlerContext ctx, String sessionId, String returnDataPacketDestination, NettyServerHandler nettyServerHandler, String connectorId){
        /** 读取费率*/
        String clogStr = SpringUtils.getBean(RedisCache.class).getCacheObject(sessionId);//获取当前订单信息
        ChargingLogVO clog = JSON.parseObject(clogStr, ChargingLogVO.class);
        /** 回复的十六进制  包含所有数据*/
        StringBuffer returnSB = new StringBuffer();
        /** 回应 十六进制 头部 默认嘉盛 4747  2 Bytes*/
        String returnHeader = JSConstants.CLIENT_HEADER_CODE;
        /** 本地 十六进制 数据包DATA*/
        StringBuffer returnLengthSB = new StringBuffer();
        /** 回应 十六进制 数据包长度 默认 0000  2 Bytes*/
        String returnDataLength = "0000";
        /** 回应 十六进制 数据包ID 默认 00 1 Byte*/
        String returnDataPacketID = "02";
        /** 回应 十六进制 数据包目的地 默认 00000000 4 Byte*/
        String returnDataPacketSource = "00000000";
        /** 回应 十六进制 功能代码 默认 00 1 Byte*/
        String returnDataPacketFunctionCode = "0E";
        /** 回应 十六进制 数据data n Bytes*/
        StringBuffer returnDataPacketData = new StringBuffer();
        /** 回应 CRC16校验码 2 Bytes*/
        String returnDataPacketCRC16 = "0000";
        /** 数据内容----------------------------------------------- */

     /** U16 price_0	尖电价 */
     String price_0 = JSUtils.IntToHex(String.valueOf((int) (new BigDecimal(clog.getSharpPrice()).doubleValue()*100)),2);
     /** U16 price_1	峰电价 0.8787 */
     String price_1 = JSUtils.IntToHex(String.valueOf((int) (new BigDecimal(clog.getSharpPrice()).doubleValue()*100)),2);
     /** U16 price_2	平电价 0.5858*/
     String price_2 = JSUtils.IntToHex(String.valueOf((int) (new BigDecimal(clog.getFlatPrice()).doubleValue()*100)),2);
     /** U16 price_3	谷电价 0.2929 */
     String price_3 = JSUtils.IntToHex(String.valueOf((int) (new BigDecimal(clog.getValleyPrice()).doubleValue()*100)),2);
     /** U16 server_0	尖服务费  0.54*/
     String server_0 = JSUtils.IntToHex(String.valueOf((int) (new BigDecimal(clog.getSharpFeeprice()).doubleValue()*100)),2);
     /**  U16 server_1	峰服务费 0.54*/
     String server_1 = JSUtils.IntToHex(String.valueOf((int) (new BigDecimal(clog.getSharpFeeprice()).doubleValue()*100)),2);
     /**  U16 server_2	平服务费 0.54 */
     String server_2 = JSUtils.IntToHex(String.valueOf((int) (new BigDecimal(clog.getFlatFeeprice()).doubleValue()*100)),2);
     /**  U16 server_3	谷服务费 0.54*/
     String server_3 = JSUtils.IntToHex(String.valueOf((int) (new BigDecimal(clog.getValleyFeeprice()).doubleValue()*100)),2);
     /** U8 period[48]	时段（尖0峰1平2谷3） 根据当前时间获取 尖峰平谷*/
        RateEntity rateEntity = getNewDateStatus(DateUtil.currentSeconds());
        String newDateStatus = null;
        if(StrUtil.isNotEmpty(rateEntity.getStatus()) && "0".equals(rateEntity.getStatus())){
            //数据库 0是峰值  对应嘉盛 0 或 1 尖峰
            newDateStatus = "0";
        }else if(StrUtil.isNotEmpty(rateEntity.getStatus()) && "2".equals(rateEntity.getStatus())){
            //数据库2 平  对应嘉盛 2平
            newDateStatus = "2";
        }else if(StrUtil.isNotEmpty(rateEntity.getStatus()) && "1".equals(rateEntity.getStatus())){
            //数据库1 谷  对应嘉盛 3 谷
            newDateStatus = "3";
        }
        String period = JSUtils.publicIntToHex(newDateStatus,48); //2 平
     /** gunNo：0,1,2,3 ; connectid：1,2,3,4       0：标识桩 */
     String gunNo = CnovertSystem.numToHex8(Integer.parseInt(connectorId)-1);
        returnDataPacketData.append(price_0).append(price_1)
                .append(price_2).append(price_3)
                .append(server_0).append(server_1)
                .append(server_2).append(server_3).append(period).append(gunNo);
        if(StrUtil.isNotEmpty(returnDataPacketData) && !"00".equals(returnDataPacketFunctionCode)){
            /** 回应 CRC16校验码 */
            returnDataPacketCRC16 = JSUtils.returnDataCRC16(returnDataPacketData.toString());
            /** 回应 数据包总DATA*/
            returnLengthSB.append(returnDataPacketID).append(returnDataPacketSource).append(returnDataPacketDestination).append(returnDataPacketFunctionCode).append(returnDataPacketData).append(returnDataPacketCRC16);
            /** 回应 的数据包总长度*/
            returnDataLength =JSUtils.returnDataLengthHEX(returnLengthSB.length()/2);
            /** 下面是返回总体16进制数据 */
            returnSB.append(returnHeader).append(returnDataLength).append(returnLengthSB.toString());
            log.info("【客户端:[" + nettyServerHandler.pileMap.get(ctx.channel().id().toString()).getDestIdNumber() + "]】" + " -【费率下发】十六进制内容 ]:" + CnovertSystem.parseHexStrToHex(returnSB.toString()) );
            byte [] returnByte= CnovertSystem.hexStringToBytes(returnSB.toString());
            nettyServerHandler.writeMessage(ctx.channel().id(), returnByte);
        }
    }

    /**
     * 根据时间获取所属尖峰平谷哪种状态
     * @param getTime 日期时间戳 精确到秒
     * @return 状态值
     */
    public static RateEntity getNewDateStatus(long getTime){
        long now = System.currentTimeMillis() / 1000;
        long daySecond = 60 * 60 * 24;
        long dayTime = now - (now + 8 * 3600) % daySecond;
        long statusTime = getTime - dayTime;
        RateEntity status =null;
        /** 获取当前费率表*/
        List<RateEntity> getRateList = SpringUtils.getBean(RedisCache.class).getCacheObject(JSConstants.JS_INIT_RATE);
        for (RateEntity rateEntity : getRateList) {
            Long  beginTime = Long.parseLong(rateEntity.getBeginTime()) * 60;
            Long  endTime = Long.parseLong(rateEntity.getEndTime()) * 60;
            if(beginTime <= statusTime && statusTime <= endTime){
                status = rateEntity;
                break;
            }
        }
        return status;
    }

    /**
     * 根据时间 获取 跨区时间差
     * @param dateTime 当前时间时间戳
     * @param timestampZone 跨过的时间区域信息
     * @return
     */
    public static long getRegionDateTime(float dateTime ,RateEntity timestampZone){
        long regionDateTime = 0;
        long now = System.currentTimeMillis() / 1000;
        long daySecond = 60 * 60 * 24;
        long dayTime = now - (now + 8 * 3600) % daySecond;
        long statusTime = Long.parseLong(String.valueOf(dateTime)) - dayTime;
        int enTime = Integer.parseInt(timestampZone.getEndTime()) * 60;
        regionDateTime = Long.parseLong(String.valueOf(NumberUtil.sub(Integer.parseInt(String.valueOf(statusTime)), enTime)));
        return regionDateTime;
    }

    /**
     * 获取用电量
     * 本次电量 = 当前电量-起始电量 / 1000
     * 0.110 kwh = 87830-87720 /1000
     * @param meterValue 当前电表值
     * @param lastTime  上次电表值
     * @return
     */
    public static Double getThisTimeValue(String meterValue,String lastTime){
        return Double.parseDouble(String.valueOf(NumberUtil.div(NumberUtil.sub(meterValue, lastTime),1000)));
    }

    /**
     * 计算费用尖峰平谷 计算电量尖峰平谷
     * @param thisTimestampZone 当前时间费率信息（包含费率、时区、）
     * @param lastTimestampZone 上次时间费率信息（包含费率、时区）
     * @param thisTimeValue 本次充电量 kwh
     * @param thisPower 本次功率
     * @param endTime 当前时间
     * @param startTime 上次结束时间
     * @return 费用计算结果
     */
    private static CalculateTheCost getCalculateTheCost(RateEntity thisTimestampZone, RateEntity lastTimestampZone, double thisTimeValue, String thisPower, float endTime, float startTime) {
        /**充电时长*/
        double subLength = NumberUtil.sub(endTime, startTime);
        CalculateTheCost ctc = new CalculateTheCost();
        if(thisTimestampZone != null && lastTimestampZone != null){
            if(thisTimestampZone.getStatus().equals(lastTimestampZone.getStatus())){
                // 没夸时区 直接处理
                ctc = normalMethod(lastTimestampZone,thisTimeValue,ctc,subLength);
                return ctc;
            }else{
                //跨时区
                /** 每秒充电量 保留3位*/
                double second = NumberUtil.div(thisTimeValue,subLength,3);
                /** 查询起始时间与跨区时间差 既 在这个区间充电多少秒 后面的时间 */
                long endTimeZone = getRegionDateTime(endTime, lastTimestampZone);
                /** 开始的时间区域 前面的*/
                long startTimeZone = (long) NumberUtil.sub(subLength,endTimeZone);
                ctc = normalMethod(lastTimestampZone,NumberUtil.mul(startTimeZone, second),ctc,startTimeZone);
                ctc = normalMethod(thisTimestampZone,NumberUtil.mul(endTimeZone, second),ctc,endTimeZone);
//                /**本次充电时间*/
//                thisDateTime =Long.parseLong(timestamp)-Long.parseLong(clog.getStartTime());
//                /**本次充电功率 = 电压 * 电流 /100(0.1AV) /3600(每秒) * thisDateTime本次充电时间 / 1000(wh 换算 kwh)
//                 * 0.38 = 651 * 6996 / 100 /3600 * 30 / 1000*/
//                String thisPower =NumberUtil.roundStr(Double.parseDouble(CurrentImport)*Double.parseDouble(voltageImport) / 100 / 3600  * 30 / 1000,2);
//                /** 本次充电金额 = 功率 * ( 充电费 + 服务费) */
//                thisChargeMoney = NumberUtil.roundStr(Double.parseDouble(thisPower) * (Double.parseDouble(thisTimestampZone.getPrice())+Double.parseDouble(thisTimestampZone.getFee())),4);
            }
            return  ctc;
        }else{
            //解析失败时间有空值
            return null;
        }
    }

    /**
     * 总电费服务费   查看之前是否有累积
     * @param totalprice
     * @param sumChargeMoney
     * @return
     */
    public static String sumMoney(String totalprice ,String sumChargeMoney){
        if (StrUtil.isNotEmpty(totalprice) && !"0".equals(totalprice)) {
            return String.valueOf(NumberUtil.add(totalprice, sumChargeMoney));
        }else{
            return sumChargeMoney;
        }
    }

    /**
     * 时间封装ctc方法
     * @param lastTimestampZone 开始充电的费率信息
     * @param thisTimeValue 充电电量
     * @param ctc 封装对象
     * @param subLength
     * @return
     */
    public static CalculateTheCost normalMethod(RateEntity lastTimestampZone, double thisTimeValue, CalculateTheCost ctc, double subLength){
        /** 本次充电费用 */
        double price = Double.parseDouble(lastTimestampZone.getPrice()); //电费费率
        double fee = Double.parseDouble(lastTimestampZone.getFee()); //服务费费率
        String sumChargeMoney = NumberUtil.roundStr(thisTimeValue*(Double.parseDouble(lastTimestampZone.getPrice())+Double.parseDouble(lastTimestampZone.getFee())),3);
        /** 查看本次所属时区 状态 尖峰:3 平:2 谷:1 */
        switch (lastTimestampZone.getStatus())
        {
            case "3":
                ctc.setPeakPower(thisTimeValue+"");//充电量 kw
                ctc.setPeakCharge(NumberUtil.roundStr(NumberUtil.mul(thisTimeValue,price),3));//充电费
                ctc.setPeakFee(NumberUtil.roundStr(NumberUtil.mul(thisTimeValue,fee),3));//充电服务费
                ctc.setPeakLength(subLength+""); //充电时长
                ctc.setTotalprice(sumMoney(ctc.getTotalprice(),sumChargeMoney));//总费用
                ctc.setElecPrice(sumMoney(ctc.getElecPrice(),ctc.getPeakCharge()));//总电费
                ctc.setServiceFee(sumMoney(ctc.getServiceFee(),ctc.getPeakFee()));//总服务费
                break;
            case "2":
                ctc.setFlatPower(thisTimeValue+"");
                ctc.setFlatCharge(NumberUtil.roundStr(NumberUtil.mul(thisTimeValue,price),3));//充电费
                ctc.setFlatFee(NumberUtil.roundStr(NumberUtil.mul(thisTimeValue,fee),3));//充电服务费
                ctc.setFlatLength(subLength+""); //充电时长
                ctc.setTotalprice(sumMoney(ctc.getTotalprice(),sumChargeMoney));//总费用
                ctc.setElecPrice(sumMoney(ctc.getElecPrice(),ctc.getFlatCharge()));//总电费
                ctc.setServiceFee(sumMoney(ctc.getServiceFee(),ctc.getFlatFee()));//总服务费
                break;
            case "1":
                ctc.setValleyPower(thisTimeValue+"");
                ctc.setValleyCharge(NumberUtil.roundStr(NumberUtil.mul(thisTimeValue,price),3));//充电费
                ctc.setValleyFee(NumberUtil.roundStr(NumberUtil.mul(thisTimeValue,fee),3));//充电服务费
                ctc.setValleyLength(subLength+""); //充电时长
                ctc.setTotalprice(sumMoney(ctc.getTotalprice(),sumChargeMoney));//总费用
                ctc.setElecPrice(sumMoney(ctc.getElecPrice(),ctc.getValleyCharge()));//总电费
                ctc.setServiceFee(sumMoney(ctc.getServiceFee(),ctc.getValleyFee()));//总服务费
                break;
        }
        return ctc;
    }

    /**
     * 账单结束 调用 保存数据库历史记录
     * @param clog
     * @return
     */
    /**
     * 账单结束 调用 保存数据库历史记录
     * @param clog
     * @return
     */
    public static BeeTdCharginglog saveBeeTdCharGinLog(ChargingLogVO clog){
        BeeTdCharginglog beeTdCharginglog = new BeeTdCharginglog();
        beeTdCharginglog.setId(clog.getId());
        beeTdCharginglog.setStationNo(clog.getStationNo());
        beeTdCharginglog.setChargerNo(clog.getChargerNo());
        beeTdCharginglog.setBranchNo(clog.getBranchNo());
        beeTdCharginglog.setSessionId(clog.getSessionId());
        beeTdCharginglog.setCusId(clog.getCusId());
        beeTdCharginglog.setCardNo(clog.getCardNo());
        if(StrUtil.isNotEmpty(clog.getStartTime())){
            beeTdCharginglog.setStartTime(new Date(Long.parseLong(clog.getStartTime())*1000));/** 开始时间 */
        }
        beeTdCharginglog.setStartValue(Long.parseLong(StrUtil.isEmpty(clog.getStartValue())?"0":clog.getStartValue()));
        if(StrUtil.isNotEmpty(clog.getEndTime())){
            beeTdCharginglog.setEndTime(new Date (Long.parseLong(clog.getEndTime())*1000));/** 结束时间 */
        }
        beeTdCharginglog.setEndValue(Long.parseLong(StrUtil.isEmpty(clog.getEndValue())?"0":clog.getEndValue()));
        beeTdCharginglog.setStopReason(clog.getStopReason());
        beeTdCharginglog.setBatFixVol(StrUtil.isEmpty(clog.getBatFixVol())?0:Long.parseLong(clog.getBatFixVol()));
        beeTdCharginglog.setBatTotalV(StrUtil.isEmpty(clog.getBatTotalV())?0:Long.parseLong(clog.getBatTotalV()));
        beeTdCharginglog.setStopReason(clog.getStopReason());
        beeTdCharginglog.setBatTotalV(StrUtil.isEmpty(clog.getBatTotalV())?0:Long.parseLong(clog.getBatTotalV()));
        beeTdCharginglog.setBatFixVol(StrUtil.isEmpty(clog.getBatFixVol())?0:Long.parseLong(clog.getBatFixVol()));
        beeTdCharginglog.setTotalVol(StrUtil.isEmpty(clog.getTotalVol())?0:Long.parseLong(clog.getTotalVol()));
        beeTdCharginglog.setChargerNum(StrUtil.isEmpty(clog.getChargerNum())?0:Long.parseLong(clog.getChargerNum()));
        beeTdCharginglog.setBusJobNo(clog.getBusJobNo());
        beeTdCharginglog.setBusNo(StrUtil.isEmpty(clog.getBusNo())?0:Long.parseLong(clog.getBusNo()));
        beeTdCharginglog.setStartSoc(StrUtil.isEmpty(clog.getStartSoc())?0:Long.parseLong(clog.getStartSoc()));
        beeTdCharginglog.setEndSoc(StrUtil.isEmpty(clog.getEndSoc())?0:Long.parseLong(clog.getEndSoc()));
        beeTdCharginglog.setInsTime(new Date());/** 入库时间当前时间*/
        beeTdCharginglog.setBatType(StrUtil.isEmpty(clog.getBatType())?0:Long.parseLong(clog.getBatType()));
        beeTdCharginglog.setChargerLength(StrUtil.isEmpty(clog.getChargerLength())?new BigDecimal(0):new BigDecimal(clog.getChargerLength()).setScale(0,BigDecimal.ROUND_DOWN));
        beeTdCharginglog.setChargerValue(StrUtil.isEmpty(clog.getChargerValue())?0:new BigDecimal(clog.getChargerValue()).setScale(2,BigDecimal.ROUND_UP).doubleValue());
        beeTdCharginglog.setStartType(StrUtil.isEmpty(clog.getStartType())?0:Long.parseLong(clog.getStartType()));
        beeTdCharginglog.setTradetype(StrUtil.isEmpty(clog.getTradetype())?0:Long.parseLong(clog.getTradetype()));
        beeTdCharginglog.setRecordsource(StrUtil.isEmpty(clog.getRecordsource())?0:Long.parseLong(clog.getRecordsource()));
        beeTdCharginglog.setCartype(StrUtil.isEmpty(clog.getCartype())?0:Long.parseLong(clog.getCartype()));
        beeTdCharginglog.setSharpPower(StrUtil.isEmpty(clog.getSharpPower())?0:new BigDecimal(clog.getSharpPower()).setScale(0,BigDecimal.ROUND_UP).doubleValue());
        beeTdCharginglog.setSharpCharge(StrUtil.isEmpty(clog.getSharpCharge())?0:new BigDecimal(clog.getSharpCharge()).setScale(2,BigDecimal.ROUND_UP).doubleValue());
        beeTdCharginglog.setSharpPrice(StrUtil.isEmpty(clog.getSharpPrice())?0:new BigDecimal(clog.getSharpPrice()).setScale(2,BigDecimal.ROUND_UP).doubleValue());
        beeTdCharginglog.setSharpFeeprice(StrUtil.isEmpty(clog.getSharpFeeprice())?0:new BigDecimal(clog.getSharpFeeprice()).setScale(2,BigDecimal.ROUND_UP).doubleValue());
        beeTdCharginglog.setSharpFee(StrUtil.isEmpty(clog.getSharpFee())?0:new BigDecimal(clog.getSharpFee()).setScale(2,BigDecimal.ROUND_UP).doubleValue());
        beeTdCharginglog.setPeakPower(StrUtil.isEmpty(clog.getPeakPower())?0:new BigDecimal(clog.getPeakPower()).setScale(0,BigDecimal.ROUND_UP).doubleValue());
        beeTdCharginglog.setPeakCharge(StrUtil.isEmpty(clog.getPeakCharge())||"0".equals(clog.getPeakCharge())?0:new BigDecimal(clog.getPeakCharge()).setScale(2,BigDecimal.ROUND_UP).doubleValue());
        beeTdCharginglog.setPeakPrice(StrUtil.isEmpty(clog.getPeakPrice())?0:new BigDecimal(clog.getPeakPrice()).setScale(2,BigDecimal.ROUND_UP).doubleValue());
        beeTdCharginglog.setPeakFeeprice(StrUtil.isEmpty(clog.getPeakFeeprice())?0:new BigDecimal(clog.getPeakFeeprice()).setScale(2,BigDecimal.ROUND_UP).doubleValue());
        beeTdCharginglog.setPeakFee(StrUtil.isEmpty(clog.getPeakFee())?0:new BigDecimal(clog.getPeakFee()).setScale(2,BigDecimal.ROUND_UP).doubleValue());
        beeTdCharginglog.setFlatPower(StrUtil.isEmpty(clog.getFlatPower())?0:new BigDecimal(clog.getFlatPower()).setScale(0,BigDecimal.ROUND_UP).doubleValue());
        beeTdCharginglog.setFlatCharge(StrUtil.isEmpty(clog.getFlatCharge())?0:new BigDecimal(clog.getFlatCharge()).setScale(2,BigDecimal.ROUND_UP).doubleValue());
        beeTdCharginglog.setFlatPrice(StrUtil.isEmpty(clog.getFlatPrice())?0:new BigDecimal(clog.getFlatPrice()).setScale(2,BigDecimal.ROUND_UP).doubleValue());
        beeTdCharginglog.setFlatFeeprice(StrUtil.isEmpty(clog.getFlatFeeprice())?0:new BigDecimal(clog.getFlatFeeprice()).setScale(2,BigDecimal.ROUND_UP).doubleValue());
        beeTdCharginglog.setFlatFee(StrUtil.isEmpty(clog.getFlatFee())?0:new BigDecimal(clog.getFlatFee()).setScale(2,BigDecimal.ROUND_UP).doubleValue());
        beeTdCharginglog.setValleyPower(StrUtil.isEmpty(clog.getValleyPower())?0:new BigDecimal(clog.getValleyPower()).setScale(0,BigDecimal.ROUND_UP).doubleValue());
        beeTdCharginglog.setValleyCharge(StrUtil.isEmpty(clog.getValleyCharge())?0:new BigDecimal(clog.getValleyCharge()).setScale(2,BigDecimal.ROUND_UP).doubleValue());
        beeTdCharginglog.setValleyPrice(StrUtil.isEmpty(clog.getValleyPrice())?0:new BigDecimal(clog.getValleyPrice()).setScale(2,BigDecimal.ROUND_UP).doubleValue());
        beeTdCharginglog.setValleyFeeprice(StrUtil.isEmpty(clog.getValleyFeeprice())?0:new BigDecimal(clog.getValleyFeeprice()).setScale(2,BigDecimal.ROUND_UP).doubleValue());
        beeTdCharginglog.setValleyFee(StrUtil.isEmpty(clog.getValleyFee())?0:new BigDecimal(clog.getValleyFee()).setScale(2,BigDecimal.ROUND_UP).doubleValue());
        beeTdCharginglog.setTotalprice(StrUtil.isEmpty(clog.getTotalprice())?0:new BigDecimal(clog.getTotalprice()).setScale(2,BigDecimal.ROUND_UP).doubleValue());
        beeTdCharginglog.setRunDistance(StrUtil.isEmpty(clog.getRunDistance())?0:new BigDecimal(clog.getRunDistance()).setScale(2,BigDecimal.ROUND_UP).doubleValue());
        beeTdCharginglog.setExtendVol(StrUtil.isEmpty(clog.getExtendVol())?0:new BigDecimal(clog.getExtendVol()).setScale(2,BigDecimal.ROUND_UP).doubleValue());
        beeTdCharginglog.setBst(clog.getBst());
        beeTdCharginglog.setErrorCode(clog.getErrorCode());
        beeTdCharginglog.setElecPrice(StrUtil.isEmpty(clog.getElecPrice())?0:new BigDecimal(clog.getElecPrice()).setScale(2,BigDecimal.ROUND_UP).doubleValue());
        beeTdCharginglog.setServiceFee(StrUtil.isEmpty(clog.getServiceFee())?0:new BigDecimal(clog.getServiceFee()).setScale(2,BigDecimal.ROUND_UP).doubleValue());
        beeTdCharginglog.setSharpLength(StrUtil.isEmpty(clog.getSharpLength())?new BigDecimal(0):new BigDecimal(NumberUtil.div(Double.parseDouble(clog.getSharpLength()), 60.0)).setScale(0,BigDecimal.ROUND_UP));
        beeTdCharginglog.setPeakLength(StrUtil.isEmpty(clog.getPeakLength())?new BigDecimal(0):new BigDecimal(NumberUtil.div(Double.parseDouble(clog.getPeakLength()), 60.0)).setScale(0,BigDecimal.ROUND_UP));
        beeTdCharginglog.setFlatLength(StrUtil.isEmpty(clog.getFlatLength())?new BigDecimal(0):new BigDecimal(NumberUtil.div(Double.parseDouble(clog.getFlatLength()), 60.0)).setScale(0,BigDecimal.ROUND_UP));
        beeTdCharginglog.setValleyLength(StrUtil.isEmpty(clog.getValleyLength())?new BigDecimal(0):new BigDecimal(NumberUtil.div(Double.parseDouble(clog.getValleyLength()), 60.0)).setScale(0,BigDecimal.ROUND_UP));
        beeTdCharginglog.setBeginTime(new Date());/** 发生时间*/
        return beeTdCharginglog;
    }


}
