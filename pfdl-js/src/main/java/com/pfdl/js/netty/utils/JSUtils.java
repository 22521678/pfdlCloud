package com.pfdl.js.netty.utils;

import cn.hutool.core.util.StrUtil;
import com.pfdl.api.netty.PileIdentity;
import com.pfdl.common.netty.JSConstants;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author zhaoyt
 * @Date 2020/12/8 15:29
 * @Version 1.0
 * 嘉盛解析方法类
 */

public class JSUtils {
    /**
     * Packet Header
     * 两个字节分别为0x47,0x47。
     * Packet Length
     * 	其后的所有字节长度。
     * Packet ID
     * 连续计数器，每发一个包加1。
     * Source / Destination
     * 由系统分配的每个桩的唯一ID号，0表示后台系统。
     * 桩只接受Source为0的数据包。
     * 后台系统收到Source为0的数据包表示是某个桩第一次登录系统，需要为其分配ID，其后该桩都需要用此ID发送数据，否则会被后台系统拒绝。
     * Source为0，Dest 0xFFFFFFFF的数据包表示广播包，所有的桩都应处理此数据包，但应答的时候Source必须是自己桩的ID。
     * Function Code
     * 功能码，对应不同的应用功能，具体见下一节描述。
     * 其中低7位表示功能码，最高位为0表示是请求命令，最高位为1表示是对该请求的应答。注意，应答数据包的Packet ID域的值应当与请求命令相同，且不增加连续计数器。
     * Data
     * 功能码对应的数据域。长度为Packet Length – 12B。
     * 加密后的密文
     * CRC16
     * 前面密文数据的CRC16校验值。
     * 对齐方式
     * 	按字节进行Little Endian对齐。
     */
    /**
     * 头部       长度      数据包ID     数据源      目的地      功能代码         data数据      CRC16
     * 2byte     2byte      1byte       4byte       4byte       1byte           n byte      2 byte
     */

    /**
     * 嘉盛:js 长度解析
     * @param byteMsg
     * @return
     */
    public static String JSPacketLength(byte [] byteMsg){
        byte [] lengthByte = new byte[2];
        /**
         * byteMsg:数据源
         * srcPos:源数组要复制的起始位置
         * dest:目的数组
         * destPos:目的数组放置的起始位置
         * length:要复制的长度
         */
        System.arraycopy(byteMsg, 2, lengthByte, 0, 2);
        return CnovertSystem.bytesToHexString(lengthByte);
    }

    /**
     * 嘉盛:js 数据包ID
     * @param byteMsg
     * @return
     */
    public static String JSPacketID(byte [] byteMsg){
        byte [] packetIDByte = new byte[1];
        System.arraycopy(byteMsg, 4, packetIDByte, 0, 1);
        return CnovertSystem.bytesToHexString(packetIDByte);
    }

    /**
     * 嘉盛:js 数据源
     * @param byteMsg
     * @return
     */
    public static String JSSource(byte [] byteMsg){
        byte [] sourceByte = new byte[4];
        System.arraycopy(byteMsg, 5, sourceByte, 0, 4);
        return CnovertSystem.bytesToHexString(sourceByte);
    }

    /**
     * 嘉盛:js 目的地
     * @param byteMsg
     * @return
     */
    public static String JSDestination(byte [] byteMsg){
        byte [] destinationByte = new byte[4];
        System.arraycopy(byteMsg, 9, destinationByte, 0, 4);
        return CnovertSystem.bytesToHexString(destinationByte);
    }

    /**
     * 嘉盛:js 功能代码
     * @param byteMsg
     * @return
     */
    public static String JSFunctionCode(byte [] byteMsg){
        byte [] functionCodeByte = new byte[1];
        System.arraycopy(byteMsg, 13, functionCodeByte, 0, 1);
        return CnovertSystem.bytesToHexString(functionCodeByte);
    }

    /**
     * 嘉盛:js 数据
     * @param byteMsg
     * @return
     */
    public static String JSData(byte [] byteMsg){
        int beginIndex = 14;//源开始节点
        int byteLength = byteMsg.length-2-beginIndex;
        byte [] functionCodeByte = new byte[byteLength];
        System.arraycopy(byteMsg, beginIndex, functionCodeByte, 0, byteLength);
        return CnovertSystem.bytesToHexString(functionCodeByte);
    }
    /**
     * 嘉盛:js CRC16
     * @param byteMsg
     * @return
     */
    public static String JSCRC16(byte [] byteMsg){
        int beginIndex = byteMsg.length-2;//源开始节点
        byte [] CRC16Byte = new byte[2];
        System.arraycopy(byteMsg, beginIndex, CRC16Byte, 0, 2);
        return CnovertSystem.bytesToHexString(CRC16Byte);
    }
    /**
     * Ascii解码 读取SN值
     * @param hexesBytes
     * @return
     * U8	无符号8位整型数，包含1个char
     * U8 SN[25+1]
     */
    public static String getSN(byte[] hexesBytes) {
        byte [] SNByte = new byte[JSConstants.SNLength];
        System.arraycopy(hexesBytes, 0, SNByte, 0, JSConstants.SNLength);
        String SNStr = CnovertSystem.parseByte2HexStr(SNByte);
//        System.out.println("解析SN: byte转16进制 :--------------------"+SNStr);
        String hexToAsciiString = CnovertSystem.getHexToAsciiString(SNStr);
        return hexToAsciiString.replaceAll(" ","").trim();
    }
    /**
     * Ascii解码 读取model值
     * @param hexesBytes
     * @return
     * U8	无符号8位整型数，包含1个char
     * U8 model[20+1]
     */
    public static String getModel(byte[] hexesBytes) {
        byte [] modelByte = new byte[JSConstants.modelLength];
        System.arraycopy(hexesBytes, JSConstants.SNLength, modelByte, 0, JSConstants.modelLength);
        String modelStr = CnovertSystem.parseByte2HexStr(modelByte);
        return CnovertSystem.getHexToAsciiString(modelStr).replaceAll(" ","").trim();
    }
    /**
     * 读取vendorId值
     * @param hexesBytes
     * @return
     * U8	无符号8位整型数，包含1个char
     * U8 vendorId
     */
    public static String getVendorId(byte[] hexesBytes) {
        byte [] vendorIdByte = new byte[JSConstants.vendorIdLength];
        System.arraycopy(hexesBytes, (JSConstants.modelLength+JSConstants.SNLength), vendorIdByte, 0, JSConstants.vendorIdLength);
        String vendorIdStr = CnovertSystem.parseByte2HexStr(vendorIdByte);
        return Integer.parseInt(vendorIdStr)+"";
    }
    /**
     *  Ascii解码 读取firmwareVersion值
     * @param hexesBytes
     * @return
     * U8	无符号8位整型数，包含1个char
     * U8 firmwareVersion[50+1]
     */
    public static String getFirmwareVersion(byte[] hexesBytes) {
        byte [] firmwareVersionByte = new byte[JSConstants.firmwareVersionLength];
        System.arraycopy(hexesBytes, (JSConstants.modelLength+JSConstants.SNLength+JSConstants.vendorIdLength), firmwareVersionByte, 0, JSConstants.firmwareVersionLength);
        String firmwareVersionStr = CnovertSystem.parseByte2HexStr(firmwareVersionByte);
        return CnovertSystem.getHexToAsciiString(firmwareVersionStr).replaceAll(" ","").trim();
    }
    /**
     * 读取connectorCount值
     * @param hexesBytes
     * @return
     * U8	无符号8位整型数，包含1个char
     * U8 connectorCount
     */
    public static String getConnectorCount(byte[] hexesBytes) {
        byte [] connectorCountByte = new byte[JSConstants.connectorCountLength];
        System.arraycopy(hexesBytes, (JSConstants.modelLength+JSConstants.SNLength+JSConstants.vendorIdLength+JSConstants.firmwareVersionLength), connectorCountByte, 0, JSConstants.connectorCountLength);
        String connectorCountStr = CnovertSystem.parseByte2HexStr(connectorCountByte);
        return Integer.parseInt(connectorCountStr)+"";
    }
    /**
     * 读取powerRated值
     * @param hexesBytes
     * @return
     * U8	无符号8位整型数，包含1个char
     * U8 powerRated[2] 桩的额定功率，单位：kw
     *
     */
    public static String getPowerRated(byte[] hexesBytes) {
        byte [] powerRatedByte = new byte[JSConstants.powerRatedLength+6];
        System.arraycopy(hexesBytes, (JSConstants.modelLength+JSConstants.SNLength+JSConstants.vendorIdLength+JSConstants.firmwareVersionLength+JSConstants.connectorCountLength), powerRatedByte, 0, JSConstants.powerRatedLength);
        return CnovertSystem.bytesToLongLittle(powerRatedByte)+"";
    }
    /**
     * 读取ICCId值
     * @param hexesBytes
     * @return
     * U8	无符号8位整型数，包含1个char
     * U8 ICCId[20+1] 如果装配了3G模块，内置SIM卡ICCID
     */
    public static String getICCId(byte[] hexesBytes) {
        byte [] ICCIdByte = new byte[JSConstants.ICCIdLength];
        System.arraycopy(hexesBytes, (JSConstants.modelLength+JSConstants.SNLength+JSConstants.vendorIdLength+JSConstants.firmwareVersionLength+JSConstants.connectorCountLength+JSConstants.powerRatedLength), ICCIdByte, 0, JSConstants.ICCIdLength);
        String ICCIdStr = CnovertSystem.parseByte2HexStr(ICCIdByte);
        return CnovertSystem.getHexToAsciiString(ICCIdStr).replaceAll(" ","").trim();
    }
    /**
     * 读取IMSi值
     * @param hexesBytes
     * @return
     * U8	无符号8位整型数，包含1个char
     * U8 IMSi[20+1] 如果装配了3G模块，内置模块IMSI
     */
    public static String getIMSi(byte[] hexesBytes) {
        byte [] IMSiByte = new byte[JSConstants.IMSiLength];
        System.arraycopy(hexesBytes, (JSConstants.modelLength+JSConstants.SNLength+JSConstants.vendorIdLength+JSConstants.firmwareVersionLength+JSConstants.connectorCountLength+JSConstants.powerRatedLength+JSConstants.ICCIdLength), IMSiByte, 0, JSConstants.IMSiLength);
        String IMSiStr = CnovertSystem.parseByte2HexStr(IMSiByte);
        return CnovertSystem.getHexToAsciiString(IMSiStr).replaceAll(" ","").trim();
    }
    /**
     * 读取protocolVersion值
     * @param hexesBytes
     * @return
     * U8	无符号8位整型数，包含1个char
     * U8 protocolVersion[2] 协议版本号；厦门版本号:0;合肥同智桩:1;宇通版本:2
     */
    public static String getProtocolVersion(byte[] hexesBytes) {
        byte [] protocolVersionByte = new byte[JSConstants.protocolVersionLength+6];
        System.arraycopy(hexesBytes, (JSConstants.modelLength+JSConstants.SNLength+JSConstants.vendorIdLength+JSConstants.firmwareVersionLength+JSConstants.connectorCountLength+JSConstants.powerRatedLength+JSConstants.ICCIdLength+JSConstants.IMSiLength), protocolVersionByte, 0, JSConstants.protocolVersionLength);
        return CnovertSystem.bytesToLongLittle(protocolVersionByte)+"";
    }
    /**
     * 读取meterType值
     * @param hexesBytes
     * @return
     * U8	无符号8位整型数，包含1个char
     * U8 meterType[25+1] 电表类型
     */
    public static String getMeterType(byte[] hexesBytes) {
        byte [] meterTypeByte = new byte[JSConstants.meterTypeLength];
        System.arraycopy(hexesBytes, (JSConstants.modelLength+JSConstants.SNLength+JSConstants.vendorIdLength+JSConstants.firmwareVersionLength+JSConstants.connectorCountLength+JSConstants.powerRatedLength+JSConstants.ICCIdLength+JSConstants.IMSiLength+JSConstants.protocolVersionLength), meterTypeByte, 0, JSConstants.meterTypeLength);
        String meterTypeStr = CnovertSystem.parseByte2HexStr(meterTypeByte);
        return CnovertSystem.getHexToAsciiString(meterTypeStr);
    }

    /**
     * 读取DataLength数据长度值
     * @param dataPacketLength
     * @return
     * 读取长度解码
     */
    public static String  getDataLength(String dataPacketLength) {
        byte [] by = CnovertSystem.hexStringToBytes(dataPacketLength);
        int dataLength = 0;
        if(by.length == 4){
            dataLength = CnovertSystem.bytes2IntLittle(by);
        }else{
            byte [] bytes = new byte[4];
            for (int i = 0; i<by.length; i++){
                bytes[i] = by[i];
            }
            dataLength = CnovertSystem.bytes2IntLittle(bytes);
        }
        return dataLength+"";
    }

    /**
     * 心跳上报时间间隔
     * @param ss 间隔秒数
     * @return 十六进制字符
     * U8 heartBeatInterval[4]
     * byte转小端后转为16进制
     */
    public static String getHEXPHeartBeatInterval(int ss) {
        String hexStr = CnovertSystem.IntToHexString(ss);
        byte [] by = CnovertSystem.hexStringToBytes(hexStr);
        int dataLength = 0;
        if(by.length == 4){
            dataLength = CnovertSystem.bytes2IntLittle(by);
        }else{
            byte [] bytes = new byte[4];
            for (int i = 0; i<by.length; i++){
                bytes[i] = by[i];
            }
            dataLength = CnovertSystem.bytes2IntLittle(bytes);
        }
        return CnovertSystem.IntToHexString(dataLength);
    }

    /** [25+1]
     * 桩ID 转 ASCII 后转16进制
     * @param chargerNo
     * @return
     */
    public static String getHEXPointId(String chargerNo) {
        String str = CnovertSystem.strToHexASCII(chargerNo);
        byte [] by = CnovertSystem.hexStringToBytes(str);
        byte [] bytes = new byte[26];
        String returnStr ="";
        if(by.length==26){
            returnStr= CnovertSystem.parseByte2HexStr(by);
        }else{
            for (int i = 0; i<by.length; i++){
                bytes[i] = by[i];
            }
            returnStr= CnovertSystem.parseByte2HexStr(bytes);
        }
        return returnStr;
    }

    /**
     * 应答数据的16进制CRC16校验码
     * @param returnDataPacketData
     * @return
     */
    public static String returnDataCRC16(String returnDataPacketData) {
        return CnovertSystem.makeCRC(returnDataPacketData);
    }

    public static String returnDataLengthHEX(int returnLength) {
        if(returnLength == 13){
            byte [] newBy = new byte[2];
            byte [] by = CnovertSystem.intToByteLittle(returnLength);
            System.arraycopy(by, 0, newBy, 0, 2);
            return CnovertSystem.parseByte2HexStr(newBy);
        } //   0D 00
//        if(returnLength == 13){
//            return CnovertSystem.numToHex16(returnLength);
//        }
        String hexStr = CnovertSystem.IntToHexString(returnLength);
        byte [] by = CnovertSystem.hexStringToBytes(hexStr);
        byte [] bytes = new byte[2];
        String returnStr ="";
        if(by.length == 2 ){
            returnStr= CnovertSystem.parseByte2HexStr(by);
        }else{
            for (int i = 0; i<by.length; i++){
                bytes[i] = by[i];
            }
            returnStr= CnovertSystem.parseByte2HexStr(bytes);
        }
        return returnStr;
    }
    /** ---------------------------------------------应答心跳-start---------------------------------*/
    /**
     * 解析心跳数据中的 PointVersion
     * @param hexesBytes
     * @return PointVersion
     */
    public static long getPointVersion(byte[] hexesBytes) {
        byte [] pointVersionByte = new byte[8];
        System.arraycopy(hexesBytes, 0, pointVersionByte, 0, 4);
        return CnovertSystem.longFrom8Bytes(pointVersionByte, 0, true);
    }
    /**
     * 解析心跳数据中的 centerVersion
     * @param hexesBytes
     * @return centerVersion
     */
    public static long getCenterVersion(byte[] hexesBytes) {
        byte [] centerVersionByte = new byte[8];
        System.arraycopy(hexesBytes, 4, centerVersionByte, 0, 4);
        return CnovertSystem.longFrom8Bytes(centerVersionByte, 0, true);
    }

    /**
     * 心跳应答数据Time
     * @param newDate
     * @return
     * long类型转小端BYTE 转 16进制
     */
    public static String returnHeartBeatTime(Date newDate) {
        return CnovertSystem.bytesToHexString(CnovertSystem.intToByteLittle(CnovertSystem.getSecondTimestampTwo(newDate)));
    }

    /** ---------------------------------------------上报位置-start---------------------------------*/
    /**
     * 读取PositionVendorId
     * @param hexesBytes
     * @return
     * U8 VendorId
     */
    public static String getPositionVendorId(byte[] hexesBytes) {
        byte [] positionVendorIdByte = new byte[JSConstants.vendorIdLength];
        System.arraycopy(hexesBytes, 0, positionVendorIdByte, 0, JSConstants.vendorIdLength);
        return CnovertSystem.parseByte2HexStr(positionVendorIdByte);
    }
    /**
     * 读取传输标题 ASCII
     * @param hexesBytes
     * @return
     * U8 messageId[16+1]
     */
    public static String getMessageId(byte[] hexesBytes) {
        byte [] messageIdByte = new byte[JSConstants.messageIdLength];
        System.arraycopy(hexesBytes, JSConstants.vendorIdLength, messageIdByte, 0, JSConstants.messageIdLength);
        String messageIdStr = CnovertSystem.parseByte2HexStr(messageIdByte);
        return CnovertSystem.getHexToAsciiString(messageIdStr).replaceAll(" ","").trim();
    }

    /**
     * 读取传输内容 ASCII
     * @param hexesBytes
     * @return
     * U8 data[128+1]
     */
    public static String getPositionData(byte[] hexesBytes) {
        byte [] positionDataByte = new byte[JSConstants.positionDataLength];
        System.arraycopy(hexesBytes, (JSConstants.vendorIdLength+JSConstants.messageIdLength), positionDataByte, 0, JSConstants.positionDataLength);
        String positionDataStr = CnovertSystem.parseByte2HexStr(positionDataByte);
        return CnovertSystem.getHexToAsciiString(positionDataStr).replaceAll(" ","").trim();
    }

    /**
     * 返回位置上报数据
     * @return
     * U8 returnData[128+1]
     */
    public static String returnPositionData(String returnData) {
        byte [] positionDataByte = new byte [JSConstants.positionDataLength];
        String hexASCII = CnovertSystem.strToHexASCII(returnData);
        byte[] asciiByte = CnovertSystem.hexStringToBytes(hexASCII);
        for (int i = 0; i<asciiByte.length;i++){
            positionDataByte[i] = asciiByte[i];
        }
        return CnovertSystem.parseByte2HexStr(positionDataByte);
    }

    /** --------------------------------------桩或枪状态信息上报----------------------------------------------------*/




    /**
     * 公共方法16进制转int小端
     * @param hexesBytes 数据源
     * @param byteLength 目的源长度
     * @param startIndex 数据源开始下标
     * @return 转小端int[String]类型
     */
    public static String publicBytesToIntByIndex(byte[] hexesBytes,int byteLength,int startIndex) {
        byte [] destinationByte=null;
        if(byteLength < 4){
            destinationByte = new byte[4];
        }else{
            destinationByte = new byte[byteLength];
        }
        System.arraycopy(hexesBytes, startIndex, destinationByte, 0, byteLength);
        return CnovertSystem.bytes2IntLittle(destinationByte)+"";
    }

    /**
     * 公共方法16进制转ASCII
     * @param hexesBytes 数据源
     * @param byteLength 目的源长度
     * @param startIndex 数据源开始下标
     * @return 解析ASCII码后字符串
     */
    public static String publicBytesToASCIIByIndex(byte[] hexesBytes,int byteLength,int startIndex) {
        byte [] destinationByte=null;
        if(byteLength < 4){
            destinationByte = new byte[4];
        }else{
            destinationByte = new byte[byteLength];
        }
        System.arraycopy(hexesBytes, startIndex, destinationByte, 0, byteLength);
        String hexString = CnovertSystem.bytesToHexString(destinationByte);
        return CnovertSystem.getHexToAsciiString(hexString).replaceAll(" ","").trim();
    }

    /**
     * 将int转小端转hex
     * @param maxValue
     * @return
     */
    public static String ConvertDestId(Integer maxValue) {
        byte[] bytes = CnovertSystem.intToByteLittle(maxValue);
        String hexStr = CnovertSystem.parseByte2HexStr(bytes);
        return hexStr;
    }

    /**
     * 根据channelId 查看 destId
     *
     * @param pileMap
     * @param channelId
     * @return
     */
    public static String getReturnDataPacketDestination(ConcurrentHashMap<String, PileIdentity> pileMap, String channelId) {
        if(pileMap!= null && pileMap.size()>0){
            PileIdentity pileIdentity = pileMap.get(channelId);
            if(StrUtil.isNotEmpty(pileIdentity.getHexDestId())){
                return pileIdentity.getHexDestId();
            }
        }
        System.out.println("----------------error-----------------------没有根据连接通道ID找到身份验证成功信息，请先认证鉴权后进行通讯");
         return "00000000";

    }
    /**
     * 根据idTag 查看 交易号
     *
     * @param cmap
     * @param idTag
     * @return
     */
    public static String getKeyByValueIdTag(ConcurrentHashMap<String, String> cmap, String idTag) {
        if(cmap!= null && cmap.size()>0){
            for (String key : cmap.keySet()) {
                String valueId = cmap.get(key);
                if(valueId.equals(idTag)){
                    return key;
                }
            }
        }
        System.out.println("没有根据此idTag 找到相应的交易号");
        return "";

    }
    /**
     * 将字符串 转换为 ASCII 在转换为 16进制编码
     * @return
     * @param param 字符串
     * @param idTagLength
     */
    public static String publicStringToASCIIHex(String param, int idTagLength){
        String asciiStr = CnovertSystem.strToHexASCII(param);
        byte [] by = CnovertSystem.hexStringToBytes(asciiStr);
        byte [] bytes = new byte[idTagLength];
        String returnStr ="";
        if(by.length==idTagLength){
            returnStr= CnovertSystem.parseByte2HexStr(by);
        }else{
            for (int i = 0; i<by.length; i++){
                bytes[i] = by[i];
            }
            returnStr= CnovertSystem.parseByte2HexStr(bytes);
        }

        return returnStr;
    }
    /**
     * 将String类型整数转 int或Long 转小端byte 转hex
     * @param parameter
     * @param byteLength 字段长度
     * @return
     */
    public static String publicIntToHex(String parameter,Integer byteLength) {
        Integer paramInt = null;
        Long paramLong = null;
        if(StrUtil.isNotEmpty(parameter)){
            if(parameter.length() <= 10){
                paramInt = Integer.parseInt(parameter);
            }else{
                paramLong = Long.parseLong(parameter);
            }
        }else{
            paramInt = 0;
        }
        String hexStr = null;
        byte[] doneBytes = new byte[byteLength];
        if(byteLength == 1 ){
            /** 十进制int转16进制字符串 IntToHexString */
            hexStr = CnovertSystem.numToHex8(paramInt);
        }else{
            byte[] bytes = null;
            if(paramInt != null){
                bytes = CnovertSystem.intToByteLittle(paramInt);
            }else{
                bytes = CnovertSystem.longToBytesLittle(paramLong);
            }

            System.arraycopy(bytes, 0, doneBytes, 0, bytes.length);
            hexStr = CnovertSystem.parseByte2HexStr(doneBytes);
        }
        return hexStr;
    }
    public static String publicIntToHex2(String parameter,Integer byteLength) {
        Integer paramInt = null;
        Long paramLong = null;
        if(StrUtil.isNotEmpty(parameter)){
            if(parameter.length() <= 10){
                paramInt = Integer.parseInt(parameter);
            }else{
                paramLong = Long.parseLong(parameter);
            }
        }else{
            paramInt = 0;
        }
        String hexStr = null;
        byte[] doneBytes = new byte[byteLength];
        if(byteLength == 1 ){
            /** 十进制int转16进制字符串 IntToHexString */
            hexStr = CnovertSystem.numToHex8(paramInt);
        }else{
            byte[] bytes = null;
            if(paramInt != null){
                bytes = CnovertSystem.intToByteLittle(paramInt);
            }else{
                bytes = CnovertSystem.longToBytesLittle(paramLong);
            }

            System.arraycopy(bytes, 0, doneBytes, 0, bytes.length);
            hexStr = CnovertSystem.parseByte2HexStr(doneBytes);
        }
        return hexStr;
    }
    /**
     * 嘉盛封装打包 ，动态数据body
     * @param dataBodyHex 数据内容16进制
     * @param packetFunctionCode 功能码
     * @param hexDestId 桩的16进制编号 目的ID
     * @return 完整包的HEX
     */
    public static String packaging(String dataBodyHex,String packetFunctionCode,String hexDestId) {
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
        String returnDataPacketData = "0000";
        /** 回应 CRC16校验码 2 Bytes*/
        String returnDataPacketCRC16 ="0000";
        if(StrUtil.isNotEmpty(dataBodyHex) && StrUtil.isNotEmpty(packetFunctionCode) && StrUtil.isNotEmpty(hexDestId)){
            /** 转换成CRC校验码*/
            returnDataPacketCRC16 = JSUtils.returnDataCRC16(dataBodyHex);
            /** 数据内容*/
            returnDataPacketData = dataBodyHex;
            /** 功能码*/
            returnDataPacketFunctionCode = packetFunctionCode;
            /** 数据包目的地ID*/
            returnDataPacketDestination = hexDestId;
        }
        returnLengthSB.append(returnDataPacketID).append(returnDataPacketSource).append(returnDataPacketDestination).append(returnDataPacketFunctionCode).append(returnDataPacketData).append(returnDataPacketCRC16);
        returnDataLength =JSUtils.returnDataLengthHEX(returnLengthSB.length()/2);
        returnSB.append(returnHeader).append(returnDataLength).append(returnLengthSB);
        return returnSB.toString();
    }
    /**
     * 费率换算
     * @param parameter
     * @param byteLength 字段长度
     * @return
     */
    public static String IntToHex(String parameter,Integer byteLength) {
        Integer paramInt = null;
        Long paramLong = null;
        if(StrUtil.isNotEmpty(parameter)){
                paramInt = Integer.parseInt(parameter);

        }else{
            paramInt = 0;
        }
        String hexStr = null;
        byte[] doneBytes = new byte[byteLength];

            byte[] bytes = null;
            if(paramInt != null){
                bytes = CnovertSystem.intToByteLittle(paramInt);
            }else{
                bytes = CnovertSystem.longToBytesLittle(paramLong);
            }
            System.arraycopy(bytes, 0, doneBytes, 0, byteLength);
            hexStr = CnovertSystem.parseByte2HexStr(doneBytes);

        return hexStr;
    }
}
