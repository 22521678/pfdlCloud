package com.pfdl.js.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;

import com.pfdl.api.controllerUtil.BaseController;
import com.pfdl.api.jdbc.RateEntity;
import com.pfdl.api.jdbc.RemoteTransaction;
import com.pfdl.api.redis.ChargingLogVO;
import com.pfdl.common.netty.JSConstants;
import com.pfdl.common.utils.AjaxResult;
import com.pfdl.common.utils.SnowflakeIdUtils;
import com.pfdl.common.utils.spring.SpringUtils;
import com.pfdl.js.config.redis.RedisCache;
import com.pfdl.js.service.IBeeChargerService;
import com.pfdl.js.service.IRemoteTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Author zhaoyt
 * @Date 2020/12/18 10:47
 * @Version 1.0
 * 远程操作充电桩：启动等命令
 */



@RestController
@RequestMapping("/remote/transaction")
public class RemoteTransactionController extends BaseController {
//    @Autowired
//    NettyServerHandler nettyServerHandler;
    private static SnowflakeIdUtils idWorker = new SnowflakeIdUtils(3, 9);
    @Autowired
    private IRemoteTransactionService remoteTransactionService;
    @Autowired
    private IBeeChargerService beeChargerService;
    /**
     * 下发远程启动充电指令
     */
    @PostMapping("/remoteStartTransaction")
    public AjaxResult RemoteStartTransaction(@RequestBody RemoteTransaction remoteTransaction)
    {
        if(StrUtil.isNotEmpty(remoteTransaction.getTransactionId()) || StrUtil.isNotEmpty(remoteTransaction.getIdTag())){
            /** 读取费率*/
            List<RateEntity> getRateList = SpringUtils.getBean(RedisCache.class).getCacheObject(JSConstants.JS_INIT_RATE);
            //存入redis  建立账单信息
            ChargingLogVO clog = new ChargingLogVO();
            /**充电站编号*/
            clog.setToken(remoteTransaction.getToken());  //如自动停止需找到token   调用接口
            clog.setStationNo(remoteTransaction.getStationNo());
            clog.setChargerNo(remoteTransaction.getChargerNo());
            clog.setBranchNo(remoteTransaction.getConnectorId());
            clog.setSessionId(remoteTransaction.getIdTag());
            clog.setCusId(remoteTransaction.getCusId());
            /** 启动方式 1服务器控制 2主控手动 3主控定时 4终端手动启动 5终端刷卡启动 目前默认都是服务器控制 */
            clog.setStartType("1");
            /** 交易类型0：充满为止, 1：充电 时间, 2：充电金额, 3：充电电量 目前默认都是按金额充电2*/
            clog.setTradetype("2");
            /** 充电记录上传来源0：启动充电上传记录，1：结束充电上传记录，2：桩停电上传记录，3：桩停电恢复继续充电，4：桩离线停止，5：桩离线后上线续充，6：桩费率切换上传记录，7：桩异常停止 待处理默认0*/
            clog.setRecordsource("0");
            /** 预付金额/元*/
            clog.setPayMoney(remoteTransaction.getChargeParam());
            clog.setId(idWorker.nextId()+"");
            if(getRateList!= null && getRateList.size()>0){
                for (Iterator<RateEntity> iterator = getRateList.iterator(); iterator.hasNext(); ) {
                    RateEntity rate =  iterator.next();
                    /** 费率状态 尖峰:3 平:2 谷:1 */
                    if(StrUtil.isNotEmpty(rate.getStatus()) && "1".equals(rate.getStatus())){
                        clog.setValleyPrice(rate.getPrice());
                        clog.setValleyFeeprice(rate.getFee());
                    }else if(StrUtil.isNotEmpty(rate.getStatus()) && "2".equals(rate.getStatus())){
                        clog.setFlatPrice(rate.getPrice());
                        clog.setFlatFeeprice(rate.getFee());
                    }else if(StrUtil.isNotEmpty(rate.getStatus()) && "3".equals(rate.getStatus())){
                        clog.setSharpPrice(rate.getPrice());
                        clog.setSharpFeeprice(rate.getFee());
                        clog.setPeakPrice(rate.getPrice());
                        clog.setPeakFeeprice(rate.getFee());
                    }
                }
            }

            SpringUtils.getBean(RedisCache.class).setCacheObject(remoteTransaction.getIdTag(), JSON.toJSONString(clog));

        }else{
            return new AjaxResult(1,"参数异常未检查到transactionId或idTag");
        }
        if(StrUtil.isNotEmpty(remoteTransaction.getChargeType()) && "3".equals(remoteTransaction.getChargeType())){
            DecimalFormat df = new DecimalFormat("0");
            //按金额充值 金额数量乘以100
            remoteTransaction.setChargeParam((df.format(Double.parseDouble(remoteTransaction.getChargeParam())*100)));
            remoteTransaction.setBalance(StrUtil.isNotEmpty(remoteTransaction.getBalance())?df.format(Double.parseDouble(remoteTransaction.getBalance())*100):"0");
        }

        String returnCode = remoteTransactionService.RemoteStartTransactionHex(remoteTransaction);

        if(StrUtil.isNotEmpty(returnCode) && "0".equals(returnCode)){
            return new AjaxResult(0,"success");
        }else{
//            int count = beeChargerService.list();
            int updateCount = beeChargerService.updateOrderLogStatusByIdTag(remoteTransaction.getIdTag(), "2");
//            Map<String,String> strMap = new HashMap<String,String>();
//            strMap.put("idTag",remoteTransaction.getIdTag());
//            strMap.put("status","2");
//            int updateCount = beeChargerService.updateOrderLogStatusByIdTag(strMap);
//            System.out.println("updateCount  :"  +updateCount );
            return new AjaxResult(1,"fail");
        }
    }

    /**
     * 停止远程充电桩
     * @param remoteTransaction
     * @return
     */
    @PostMapping("/remoteStopTransaction")
    public AjaxResult RemoteStopTransaction(@RequestBody RemoteTransaction remoteTransaction)
    {
        String returnCode = remoteTransactionService.RemoteStopTransaction(remoteTransaction);
        if("0".equals(returnCode)){
            return new AjaxResult(0,"success");
        }
        return new AjaxResult(1,"fail");
    }
    /**
     * http请求测试
     * @param
     * @return
     */
    @GetMapping("/httpSendTest")
    public AjaxResult RemoteStopTransaction()
    {
        String url=" ";
        JSONObject json = new JSONObject();
        json.put("idTag","A8A5A4A6A2A1A3A8A7A8A9");
                            /*发送post请求并接收响应数据
                             * 采用的是一种叫链式编程的方式):
                                header对应的是请求头。
                                body对应的是请求体(包含参数和参数值)。
                                HttpRequest里面包含Post、GET、Delete、Put等常用的RestFul方式。*/
//                            Header.AUTHORIZATION Restful
        Map sss = JSON.parseObject(HttpRequest.post("http://127.0.0.1:9012/remote/transaction/httpTest")
                .header(Header.AUTHORIZATION,"token")
                .body(json.toString())
                .execute().body());
        String  msg = String.valueOf(sss.get("msg"));
        String code = String.valueOf(sss.get("code"));
        System.out.println("返回值---------msg : " + msg  + "--------------code : "+ code);
        return new AjaxResult(0,"success");
    }





}
