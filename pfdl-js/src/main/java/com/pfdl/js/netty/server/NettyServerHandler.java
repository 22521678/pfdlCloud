package com.pfdl.js.netty.server;

import cn.hutool.core.util.StrUtil;
import com.pfdl.api.netty.PileIdentity;
import com.pfdl.common.netty.JSConstants;
import com.pfdl.js.netty.entity.StartupStatus;
import com.pfdl.js.netty.utils.CnovertSystem;
import com.pfdl.js.netty.utils.JSUtils;
import com.pfdl.js.service.IBeeChargerService;
import com.pfdl.js.service.IBeeTdCharginglogService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: netty服务端处理类
 **/

@Component
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(NettyServerHandler.class);
    @Autowired
    public IBeeChargerService beeChargerService;

    @Autowired
    public IBeeTdCharginglogService beeTdCharginglogService;

    @Value("${ServiceAddress.url}")
    public String serverUrl;


    public static NettyServerHandler nettyServerHandler; /** 声明本类根构造方法*/
    @PostConstruct //加上该注解表明该方法会在bean初始化后调用
    public void init(){
        nettyServerHandler = this;
        nettyServerHandler.beeChargerService = this.beeChargerService;
        nettyServerHandler.beeTdCharginglogService = this.beeTdCharginglogService;
        nettyServerHandler.serverUrl=this.serverUrl;

    }
    /**
     * 管理一个全局map，保存连接进服务端的通道数量
     */
    private static final ConcurrentHashMap<ChannelId, ChannelHandlerContext> CHANNEL_MAP = new ConcurrentHashMap<>();
    /**
     * 管理一个全局map，保存key连接通道ID value 为 桩的所有身份ID
     */
    public static final ConcurrentHashMap<String, PileIdentity> pileMap = new ConcurrentHashMap<>();
    /**
     * 管理一个全局map远程启动充电状态，保存key getTransactionId value 启动成功失败主体  string : 远程启动成功 manual  是否手动 manual 手动停止 3
     */
    public static  ConcurrentHashMap<String, StartupStatus> remoteStartStatus = new ConcurrentHashMap<>();
//    /**
//     * 管理一个全局map管理 idTag和交易号，保存key transactionId交易号 value idTag:订单唯一标示
//     */
//    public static final ConcurrentHashMap<String, String> transactionMap = new ConcurrentHashMap<>();
    /**
     * 管理一个全局map，控制全局的目的地ID 自增自减
     */
//    public static AtomicInteger count = new AtomicInteger(0);
    /**
     * @param ctx
     * @author zhaoyt on 2020-12-17 09:08:32
     * @DESCRIPTION: 有客户端连接服务器会触发此函数
     * @return: void
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = insocket.getAddress().getHostAddress();
        int clientPort = insocket.getPort();
        //获取连接通道唯一标识
        ChannelId channelId = ctx.channel().id();
        System.out.println();
        //如果map中不包含此连接，就保存连接
        if (CHANNEL_MAP.containsKey(channelId)) {
            log.info("客户端【" + channelId + "】是连接状态，连接通道数量: " + CHANNEL_MAP.size());
        } else {
            //保存连接
            CHANNEL_MAP.put(channelId, ctx);

            log.info("客户端【" + channelId + "】连接netty服务器[IP:" + clientIp + "--->PORT:" + clientPort + "]");
            log.info("连接通道数量: " + CHANNEL_MAP.size() +" 现有桩数量: "+ pileMap.size());
        }
    }

    /**
     * @param ctx
     * @author zhaoyt on 2020-12-17 09:08:32
     * @DESCRIPTION: 有客户端终止连接服务器会触发此函数
     * @return: void
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        InetSocketAddress inSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = inSocket.getAddress().getHostAddress();
        ChannelId channelId = ctx.channel().id();
        //包含此客户端才去删除
        if (CHANNEL_MAP.containsKey(channelId)) {
            //删除连接
            CHANNEL_MAP.remove(channelId);
            String pileSN ="";
            String destId = null;
            //删除全局身份ID对象
            if(pileMap.containsKey(channelId.toString())){
                PileIdentity pileIdentity = pileMap.get(channelId.toString());
                pileSN =pileIdentity.getPileSN();
                destId =pileIdentity.getDestIdNumber();
                pileMap.remove(channelId.toString());
            }
            System.out.println();
            log.info("客户端【" + channelId + "】 桩出厂编号【"+ pileSN +"】 目的地编号【"+destId+"】退出服务器[IP:" + clientIp + "--->PORT:" + inSocket.getPort() + "]");
            log.info("连接通道数量: " + CHANNEL_MAP.size() +" 已连接桩的数量: "+ pileMap.size());
        }
    }

    /**
     * @param ctx
     * @author zhaoyt on 2020-12-17 09:08:32
     * @DESCRIPTION: 有客户端发消息会触发此函数
     * @return: void
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = insocket.getAddress().getHostAddress();
        System.out.println();
        log.info("加载客户端报文......");
         log.info("【客户端:[" + clientIp  + " : " +insocket.getPort() + "]】"+ " -原报文  :" + msg);
        byte [] byteMsg = CnovertSystem.hexStringToBytes((String)msg);
        /** 嘉盛数据头部 */
        String headerStr = packetHeader(byteMsg);
        if(StrUtil.isNotEmpty(headerStr)){
            //判断是哪个公司桩
            if(headerStr.equals(JSConstants.CLIENT_HEADER_CODE)){ //嘉盛
                /** 嘉盛数据长度 */
                String dataPacketLength = JSUtils.JSPacketLength(byteMsg);
                /** 嘉盛数据包ID */
                String dataPacketID = JSUtils.JSPacketID(byteMsg);  //嘉盛数据长度
                /** 嘉盛数据包来源 */
                String dataPacketSource = JSUtils.JSSource(byteMsg);
                /** 嘉盛数据包目的地 */
                String dataPacketDestination = JSUtils.JSDestination(byteMsg);
                /** 嘉盛数据功能代码 */
                String dataPacketFunctionCode = JSUtils.JSFunctionCode(byteMsg);
                /** 嘉盛数据内容 */
                String dataPacketData = JSUtils.JSData(byteMsg);
                /** 嘉盛数据16进制的CRC16 */
                String dataPacketCRC16 = JSUtils.JSCRC16(byteMsg);
                 log.info("【客户端:[" + clientIp  + " : " +insocket.getPort() + "]】"+ " -嘉盛数据头部     :" + headerStr);
                 log.info("【客户端:[" + clientIp  + " : " +insocket.getPort() + "]】"+ " -嘉盛数据长度     :" + dataPacketLength);
                 log.info("【客户端:[" + clientIp  + " : " +insocket.getPort() + "]】"+ " -嘉盛数据包ID     :" + dataPacketID);
                 log.info("【客户端:[" + clientIp  + " : " +insocket.getPort() + "]】"+ " -嘉盛数据包来源    :" + dataPacketSource);
                 log.info("【客户端:[" + clientIp  + " : " +insocket.getPort() + "]】"+ " -嘉盛数据包目的地  :" + dataPacketDestination);
                 log.info("【客户端:[" + clientIp  + " : " +insocket.getPort() + "]】"+ " -嘉盛数据功能代码  :" + dataPacketFunctionCode);
                 log.info("【客户端:[" + clientIp  + " : " +insocket.getPort() + "]】"+ " -嘉盛数据内容      :" + dataPacketData);
                 log.info("【客户端:[" + clientIp  + " : " +insocket.getPort() + "]】"+ " -嘉盛数据16进制的CRC16     :" + dataPacketCRC16);
                /**
                 * 首先判断是应答还是请求命令
                 * 其中低7位表示功能码，最高位为0表示是请求命令，最高位为1表示是对该请求的应答。注意，应答数据包的Packet ID域的值应当与请求命令相同，且不增加连续计数器。
                 */
                if(StrUtil.isNotEmpty(dataPacketFunctionCode)){
                    /** 登录交互 功能码为 : 11 15 16 19*/
                    if("11".equals(dataPacketFunctionCode) || "15".equals(dataPacketFunctionCode) || "0B".equals(dataPacketFunctionCode) || "16".equals(dataPacketFunctionCode) || "19".equals(dataPacketFunctionCode)){
                        AuthenticationMutual.authentication(dataPacketData,dataPacketFunctionCode,dataPacketCRC16 ,clientIp, ctx, nettyServerHandler);
                    }else if("13".equals(dataPacketFunctionCode) || "14".equals(dataPacketFunctionCode) || "18".equals(dataPacketFunctionCode) || "1B".equals(dataPacketFunctionCode)){
                            /** 开始充电上报*/
                        AuthenticationMutual.powerOnJS(dataPacketData,dataPacketFunctionCode,dataPacketCRC16 ,clientIp, ctx, nettyServerHandler);
                    }else if("02".equals(dataPacketFunctionCode) ||"0E".equals(dataPacketFunctionCode)){
                            /** 远程启动命令应答结果*/
                        AuthenticationMutual.answerJS(dataPacketData,dataPacketFunctionCode,dataPacketCRC16 ,clientIp, ctx, nettyServerHandler);
                    }else{
                        /** 不是15 需要转二进制查看是什么请求*/
                        byte[] functionCodeER = CnovertSystem.parseHexStr2Byte(dataPacketFunctionCode);
                        if(functionCodeER!=null && functionCodeER.length >0){
                            String functionCodeERStr = CnovertSystem.bytes2BinaryStr(functionCodeER);
                            log.info("【客户端:[" + clientIp  + " : " +insocket.getPort() + "]】"+ " -功能代码转二进制     :" + functionCodeERStr);
                            //请求
                            String statusCode = "1";
                            if(statusCode.equals(functionCodeERStr.substring(0,1))){
                                log.info("【客户端:[" + clientIp  + " : " +insocket.getPort() + "]】"+ " -数据内容转CRC16     :" + CnovertSystem.makeCRC(dataPacketData));
                            }
                        }
                    }

                }
            }
        }
        /**
         *  下面可以解析数据，保存数据，生成返回报文，将需要返回报文写入write函数
         */

        //响应客户端
//        this.channelWrite(ctx.channel().id(), msg);
    }
    /**
     * 头部解析公共方法
     * @param byteMsg
     * @return
     */
    private String packetHeader(byte [] byteMsg){
        byte [] headerByte = new byte[2];
        System.arraycopy(byteMsg, 0, headerByte, 0, 2);
        return CnovertSystem.bytesToHexString(headerByte);
    }
    /**
     * @param msg        需要发送的消息内容
     * @param channelId 连接通道唯一id
     * @author zhaoyt on 2020-12-17 09:08:32
     * @DESCRIPTION: 服务端给客户端发送消息
     * @return: void
     */
    public void channelWrite(ChannelId channelId, Object msg) throws Exception {

        ChannelHandlerContext ctx = CHANNEL_MAP.get(channelId);

        if (ctx == null) {
            log.info("通道【" + channelId + "】不存在");
            return;
        }

        if (msg == null || msg == "") {
            log.info("服务端响应空的消息");
            return;
        }

        //将客户端的信息直接返回写入ctx
        ctx.write(msg);
        //刷新缓存区
        ctx.flush();
    }

    /**
        obj：byte数组
     */

    public void writeMessage(ChannelId channelId, byte[] obj) {
        ChannelHandlerContext ctx = CHANNEL_MAP.get(channelId);
        ByteBuf bufff = Unpooled.buffer();//netty需要用ByteBuf传输
        bufff.writeBytes(obj);//对接需要16进制

        ctx.writeAndFlush(bufff).addListener(new ChannelFutureListener() { //获取当前的handle
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                StringBuilder sb = new StringBuilder("");

                if (future.isSuccess()) {
                    log.info(sb.toString() + "应答成功.");
                } else {
                    log.error(sb.toString() + "回写失败.");
                }
            }
        });
    }
    /**
     * 用户事件触发方法   判断事件
     *
     * @param ctx 上下文对象
     * @param evt 事件对象
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        String socketString = ctx.channel().remoteAddress().toString();

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                log.info("Client: " + socketString + " READER_IDLE 读超时");
                ctx.disconnect();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                log.info("Client: " + socketString + " WRITER_IDLE 写超时");
                ctx.disconnect();
            } else if (event.state() == IdleState.ALL_IDLE) {
                log.info("Client: " + socketString + " ALL_IDLE 总超时");
                ctx.disconnect();
            }
        }
    }

    /**
     * @param ctx
     * @author zhaoyt on 2020-12-17 09:08:32
     * @DESCRIPTION: 发生异常会触发此函数
     * @return: void
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println();
//        ctx.close();
        log.info(ctx.channel().id() + " 发生了错误,此连接被关闭" + "此时连通数量: " + CHANNEL_MAP.size());
        //cause.printStackTrace();
    }

//    public static void main(String[] args) {
//        System.out.println(DateUtils.getDate(new Date(1524882021),"yyyy-MM-dd HH:mm:ss"));
//    }


}
