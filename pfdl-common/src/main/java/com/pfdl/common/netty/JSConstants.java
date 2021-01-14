package com.pfdl.common.netty;

public class JSConstants {
    public static final Integer SOCKET_SERVER_PROT = 10001; //20000   嘉盛桩
//    public static final Integer SOCKET_SERVER_PROT = 10002; //20001  公司内部桩
//    public static final Integer SOCKET_SERVER_PROT = 10003; //20002  玖行
    public static final String SOCKET_SERVER_IP = "192.168.1.175";
    public static final Integer SOCKET_CLIENT_PROT = 9999;
    public static final String SOCKET_CLIENT_IP = "127.0.0.1";
    /** 嘉盛初始化费率 */
    public static final String JS_INIT_RATE = "JSInitRate";
    /** 嘉盛 枪状态Key 后缀*/
    public static final String JS_KEY_STATUE = "_statue";

    /**
     * 嘉盛头部
     */
    public static final String CLIENT_HEADER_CODE = "4747";
    /** ---------------------public-start--------------------------*/
    /** U8 vendorId vendorId 长度*/
    public static final int vendorIdLength = 1;
    /** ---------------------public-end--------------------------*/
    /** ---------------------鉴权-start--------------------------*/
    /** U8 SN[25+1] SN 长度*/
    public static final int SNLength = 26;
    /** U8 model[20+1] model 长度*/
    public static final int modelLength = 21;
    /** U8 firmwareVersion[50+1] 长度*/
    public static final int firmwareVersionLength = 51;
    /** U8 connectorCount 长度*/
    public static final int connectorCountLength = 1;
    /** U8 powerRated[2] 桩的额定功率，单位：kw*/
    public static final int powerRatedLength = 2;
    /** U8 ICCId[20+1] 如果装配了3G模块，内置SIM卡ICCID*/
    public static final int ICCIdLength = 21;
    /** U8 IMSi[20+1] 如果装配了3G模块，内置模块IMSI*/
    public static final int IMSiLength = 21;
    /** U8 protocolVersion[2] 协议版本号；厦门版本号:0;合肥同智桩:1;宇通版本:2 */
    public static final int protocolVersionLength = 2;
    /** U8 meterType[25+1] 电表类型 */
    public static final int meterTypeLength = 26;
    /** ---------------------鉴权-end--------------------------*/
    /** ---------------------位置上报-start--------------------------*/
    /** U8 messageId[16+1] 传输标题 */
    public static final int messageIdLength = 17;
    /** U8 data[128+1] 传输内容 */
    public static final int positionDataLength = 129;
    /** ---------------------位置上报-end--------------------------*/

    /** ---------------------桩或枪信息-start--------------------------*/
    /** U8 connectorId  桩:0或枪:1状态码*/
    public static final int connectorIdLength = 1;
    /** U8 pointStatusCode 桩或枪的状态信息*/
    public static final int pointStatusCodeLength = 1;
    /** U8 connectorConnected 枪或弓的状态*/
    public static final int connectorConnectedLength = 1;
    /** U16 pointErrorCode 错误代码*/
    public static final int pointErrorCodeLength = 2;
    /** U8 info[50+1] 错误信息*/
    public static final int errorInfoLength = 51;
    /** U8 vendorId  重复 */
    /** U16 vendorErrorCode */
    public static final int vendorErrorCodeLength = 2;
    /** U32 timestamp */
    public static final int timestampLength = 4;
    /** ---------------------桩或枪信息-end--------------------------*/
    /** ---------------------远程启动充电-start--------------------------*/
    /** U8 idTag[32+ 1] 用户标识 */
    public static final int idTagLength = 33;
    /** U8 cardNumber[32+1] 用户卡号 */
    public static final int cardNumberLength = 33;
    /** U8 phoneNumber[32+ 1] 用户手机号 */
    public static final int phoneNumberLength = 33;
    /** U8 chargeParam[4] 0:无含义；1:U32，充电时长，单位：1s; 2:U32，百分比，1% ; 3:U32，金额，单位，0.01元; 4:U32, 电量，单位:0.01kwh */
    public static final int chargeParamLength = 4;
    /** U8 balance[4] S32，账户余额，单位：0.01元 桩根据用户余额进行可否开始本次充电判断，如果桩能够计算预计充电金额，使用该值与余额比较判断；如果桩无法预估充电金额，使用配置字段：“min_start_charge_balance”，默认值3000，单位0.01元。*/
    public static final int balanceLength = 4;
    /** U8 parentIdTag[32+1] 用户组ID */
    public static final int parentIdTagLength = 33;
    /** U8 overdraftBalanceForAccount[4]	账户透支额度，单位：0.01元 此设计为了实现运营计费的vip客户的透支功能。 */
    public static final int overdraftBalanceForAccountLength = 4;
    /** U8 overdraftBalanceForCard[4]	卡透支额度，单位：0.01元 此设计为了实现运营计费的钱包卡的透支功能。 */
    public static final int overdraftBalanceForCardLength = 4;
    /** U32 expiryDate  有效期,utc*/
    public static final int  expiryDateLength = 4;
    /** ---------------------桩或枪信息-end--------------------------*/
    /** ---------------------开始充电信息-start--------------------------*/
    /** 账户类型: 0x00:手机号0x01:用户卡0x02:微信0x03:支付宝0xFF:无法识别 */
    public static final int  accountTypeLength = 1;
    /** U8 userIdTag[32+1] 用户标识 */
    public static final int  userIdTagLength = 33;
    /** U32 startTime 充电开始时间，单位：1s。到秒 */
    public static final int startTimeLength = 4;
    /** U32 expectedStoptime 预估结束时间，单位：1s。无法预估时传0xFFFFFFFF */
    public static final int expectedStoptimeLength = 4;
    /** U8 pointTransactionTag[26] 桩交易号（桩id_枪号_utc开始充电时间） */
    public static final int pointTransactionTagLength = 26;
    /** U8 chargeType 0:充满为止；1:按时间充（单位：秒）；2：按百分比冲；3:按金额充(单位：元)；4：按电量冲 */
    public static final int chargeTypeLength = 1;
    /** U32 chargeParam chargeType=0:无含义；chargeType=1:U32，充电时长，单位：1s chargeType=2:U32，无意义 chargeType=3:U32，金额，单位，0.01元 chargeType=4:U32, 电量，单位0.01kwh */
//    public static final int chargeParamLength = 4;
    /** int meterStart 开始时的电表值 ，单位wh */
    public static final int meterStartLength = 4;
    /** int reservationId 预约号 */
    public static final int reservationIdLength = 4;
    /** int payType 支付方式 */
    public static final int payTypeLength = 4;

    /** ---------------------开始充电信息-end--------------------------*/
    /** ---------------------充电完成账单信息-end--------------------------*/
    /** U8 transactionId[4]	U32，交易号*/
    public static final int transactionIdLength = 4 ;
    /**U8 boostVoltageType	辅助电压12V/24V: 0x01:12V 0x02:24V*/
    public static final int boostVoltageTypeLength = 1;
    /** int startMeter   4字节	开始充电时电表值 wh */
    public static final int startMeterLength = 4 ;
    /** U32 stopTime	充电结束时间 utc */
    public static final int stopTimeLength = 4 ;
    /** int meterStop	停止充电时电表值 wh */
    public static final int meterStopLength = 4 ;
    /** int cardCost	如果是钱包卡，从卡上扣了多少钱，单位：分(桩计费时生效) */
    public static final int cardCostLength = 4 ;
    /**int cardBalance	卡余额，单位：分(桩计费时生效)*/
    public static final int cardBalanceLength = 4 ;
    /**U32 sharpMeter	尖电量 wh(桩计费时生效)*/
    public static final int sharpMeterLength = 4 ;
    /**U32 peakMeter	峰电量 wh(桩计费时生效)*/
    public static final int peakMeterLength = 4 ;
    /**U32 flatMeter	平电量 wh(桩计费时生效)*/
    public static final int flatMeterLength = 4 ;
    /**U32 vallyMeter	谷电量 wh(桩计费时生效)*/
    public static final int vallyMeterLength = 4 ;
    /**U32 sharpCost	尖时段消费 单位:分(桩计费时生效)*/
    public static final int sharpCostLength = 4 ;
    /**U32 peakCost	峰时段消费 单位:分(桩计费时生效)*/
    public static final int peakCostLength = 4 ;
    /**U32 flatCost	平时段消费 单位:分(桩计费时生效)*/
    public static final int flatCostLength = 4 ;
    /**U32 vallyCost	谷时段消费 单位:分(桩计费时生效)*/
    public static final int vallyCostLength = 4 ;
    /**U32 charpPrice	尖时段电价 单位:分(桩计费时生效)*/
    public static final int charpPriceLength = 4 ;
    /**U32 peakPrice	峰时段电价 单位:分(桩计费时生效)*/
    public static final int peakPriceLength = 4 ;
    /**U32 flatPrice	平时段电价 单位:分(桩计费时生效)*/
    public static final int flatPriceLength = 4 ;
    /**U32 valleyPrice	谷时段电价 单位:分(桩计费时生效)*/
    public static final int valleyPriceLength = 4 ;
    /**U16 sharpTime	尖时段充电时长 单位:s(桩计费时生效)*/
    public static final int sharpTimeLength = 2 ;
    /**U16 peakTime	峰时段充电时长　单位:s(桩计费时生效)*/
    public static final int peakTimeLength = 2 ;
    /**U16 flatTime	平时段充电时长 单位:s(桩计费时生效)*/
    public static final int flatTimeLength = 2 ;
    /**U16 vallyTime	谷时段充电时长 单位:s(桩计费时生效)*/
    public static final int vallyTimeLength = 2 ;
    /**int stopReason	停止原因 新增07远程停止充电*/
    public static final int stopReasonLength = 4 ;
    /**U16 meterValueCount	U16，上报的“时刻-充电量”（下面大括号中的结构体）个数*/
    public static final int meterValueCountLength = 2 ;

    /**{
     U32 timeStamp	已充电量的度量时刻，单位：s。
     U32,表示从1970年1月1日00：00：00到度量时刻的秒数。
     U32 meterValue	该时刻已经充电的电量，单位：wh。
     }（大括号内数据重复出现）	（连续发送meterValueCount个“时刻-充电量”结构体） */
    /** ---------------------充电完成账单信息-end--------------------------*/
    /** ---------------------充电详细信息-start--------------------------*/
    /**U32 transactionId	交易号         重复  */
    /**U8 connectorId	枪号 0x01:A枪  0x02:B枪          重复     */
    /**U32 timestamp	当前时间 utc时间             重复 */
    /**U32 meterValue	电量/wh */
    public static final int meterValueLength = 4 ;
    /**U32 CurrentImport	直流输出电流/0.1A无：用0xFF填充 */
    public static final int CurrentImportLength = 4 ;
    /**U32 temperature	柜体温度/℃无：用0xFF填充 */
    public static final int temperatureLength = 4 ;
    /**U32 voltageImport	直流输出电压/0.1V	无：用0xFF填充 */
    public static final int voltageImportLength = 4 ;
    /**U32 chargedTime	已冲时间/s */
    public static final int chargedTimeLength = 4 ;
    /**U32 acVoltageA	交流输入电压/0.1V    A相无：用0xFF填充 */
    public static final int acVoltageALength = 4 ;
    /**U32 acVoltageB	交流输入电压/0.1V    B相无：用0xFF填充 */
    public static final int acVoltageBLength = 4 ;
    /**U32 acVoltageC	交流输入电压/0.1V    C相无：用0xFF填充 */
    public static final int acVoltageCLength = 4 ;
    /**U32 acCurrentA	交流输入电流/0.1A    A相无：用0xFF填充 */
    public static final int acCurrentALength = 4 ;
    /**U32 acCurrentB	交流输入电流/0.1A    B相无：用0xFF填充 */
    public static final int acCurrentBLength = 4 ;
    /**U32 acCurrentC	交流输入电流/0.1A    C相无：用0xFF填充 */
    public static final int acCurrentCLength = 4 ;
    /**U32 acPower	交流输入总功率/wh       无：用0xFF填充 */
    public static final int acPowerLength = 4 ;
    /**U32 acPowerA	交流输入功率/wh      A相无：用0xFF填充 */
    public static final int acPowerALength = 4 ;
    /**U32 acPowerB	交流输入功率/wh      B相无：用0xFF填充 */
    public static final int acPowerBLength = 4 ;
    /**U32 acPowerC	交流输入功率/wh      C相无：用0xFF填充 */
    public static final int acPowerCLength = 4 ;
    /**U16 phaseA	相角/0.1度           A相无：用0xFF填充 */
    public static final int phaseALength = 2 ;
    /**U16 phaseB	相角/0.1度           B相无：用0xFF填充 */
    public static final int phaseBLength = 2 ;
    /**U16 phaseC	相角/0.1度           C相无：用0xFF填充 */
    public static final int phaseCLength = 2 ;
    /**U16 remainTime	剩余时间/min无：用0xFF填充 */
    public static final int remainTimeLength = 2 ;
    /**U16 requestVoltage	请求电压/0.1V无：用0xFF填充 */
    public static final int requestVoltageLength = 2 ;
    /**U16 requestCurrent	请求电流/0.1A无：用0xFF填充 */
    public static final int requestCurrentLength = 2 ;
    /**U16 bmsVoltage	Bms测量电压/0.1V无：用0xFF填充 */
    public static final int bmsVoltageLength = 2 ;
    /**U16 bmsCurrent	Bms测量电流/0.1A无：用0xFF填充 */
    public static final int bmsCurrentLength = 2 ;
    /**U8 soc	电池soc/1%无：用0xFF填充 */
    public static final int socLength = 1 ;
    /**U8 maxBatTemp	最高动力蓄电池温度/℃ 无：用0xFF填充 */
    public static final int maxBatTempLength = 1 ;
    /**U8 maxBatTIdx	最高温度监测点编号 无：用0xFF填充 */
    public static final int maxBatTIdxLength = 1 ;
    /**U8 minBatTemp	最低蓄电池温度/℃ 无：用0xFF填充 */
    public static final int minBatTempLength = 1 ;
    /**U8 minBatTIdx	最低温度监测点无：用0xFF填充 */
    public static final int minBatTIdxLength = 1 ;
    /**U16 maxBatCellVol	单体最高电压/0.1V无：用0xFF填充 */
    public static final int maxBatCellVolLength = 2 ;
    /**U8 maxBatCellVolGrp	单体最高电压组号 无：用0xFF填充 */
    public static final int maxBatCellVolGrpLength = 1 ;
    /**U8 vinLen	VIN长度 */
    public static final int vinLenLength = 1 ;
    /**U8 VIN[34]	车辆VIN无：用0xFF填充 */
    public static final int VINLength = 34 ;
    /**U32 gunTemp	枪头温度/℃无：用0xFF填充 */
    public static final int gunTempLength = 4 ;
    /**U32 BRMVoltage	BRM保温额定电压/0.1V	无：用0xFF填充 */
    public static final int BRMVoltageLength = 4 ;
    /**U16 BRMBmsRateCap	BRM额定容量/Ah无：用0xFF填充 */
    public static final int BRMBmsRateCapLength = 2 ;
    /**U16 BCPNominalEnergy	BCP标称总能量/0.1kWh无：用0xFF填充 */
    public static final int BCPNominalEnergyLength = 2 ;
    /**U32 BCPChargeVoltageMax	BCP 最高允许电压/0.1V	无：用0xFF填充 */
    public static final int BCPChargeVoltageMaxLength = 4 ;
    /** ---------------------充电详细信息-end--------------------------*/
}
