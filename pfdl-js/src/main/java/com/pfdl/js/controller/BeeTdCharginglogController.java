package com.pfdl.js.controller;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.pfdl.api.controllerUtil.BaseController;
import com.pfdl.api.jdbc.BeeTdCharginglog;
import com.pfdl.api.jdbc.RateEntity;
import com.pfdl.api.page.TableDataInfo;
import com.pfdl.api.redis.ChargingLogVO;
import com.pfdl.common.utils.AjaxResult;
import com.pfdl.common.utils.spring.SpringUtils;
import com.pfdl.js.config.redis.RedisCache;
import com.pfdl.js.service.IBeeTdCharginglogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 充电记录Controller
 * 
 * @author zhaoyt
 * @date 2020-12-30
 */
@RestController
@RequestMapping("/business/charginglog")
public class BeeTdCharginglogController extends BaseController
{
    @Autowired
    private IBeeTdCharginglogService beeTdCharginglogService;

    /**
     * 查询充电记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BeeTdCharginglog beeTdCharginglog)
    {
//        startPage();
//        List<BeeTdCharginglog> list = beeTdCharginglogService.selectBeeTdCharginglogList(beeTdCharginglog);
        List<RateEntity> rateEntities = beeTdCharginglogService.selectRateList();
        return getDataTable(rateEntities);
    }

    /**
     * 获取充电记录详细信息
     */
    @GetMapping(value = "getEntity/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        //
        String clogStr = SpringUtils.getBean(RedisCache.class).getCacheObject("210112143141100000002006059");//获取当前订单信息
        ChargingLogVO clog = JSON.parseObject(clogStr, ChargingLogVO.class);
        BeeTdCharginglog beeTdCharginglog1 = saveBeeTdCharGinLog(clog);
        return toAjax(beeTdCharginglogService.insertBeeTdCharginglog(beeTdCharginglog1));
//        return AjaxResult.success(beeTdCharginglogService.selectBeeTdCharginglogById(id));
    }

    /**
     * 新增充电记录
     */
    @PostMapping
    public AjaxResult add(@RequestBody BeeTdCharginglog beeTdCharginglog)
    {
        return toAjax(beeTdCharginglogService.insertBeeTdCharginglog(beeTdCharginglog));
    }
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
        beeTdCharginglog.setStartTime(new Date(Long.parseLong(clog.getStartTime())*1000));/** 开始时间 */
        beeTdCharginglog.setStartValue(Long.parseLong(clog.getStartValue()));
        beeTdCharginglog.setEndTime(new Date (Long.parseLong(clog.getEndTime())*1000));/** 结束时间 */
        beeTdCharginglog.setEndValue(Long.parseLong(clog.getEndValue()));
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
        beeTdCharginglog.setChargerLength(StrUtil.isEmpty(clog.getChargerLength())?new BigDecimal(0):new BigDecimal(NumberUtil.div(Double.parseDouble(clog.getChargerLength()), 60.0)).setScale(0,BigDecimal.ROUND_UP));
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
    /**
     * 修改充电记录
     */
    @PutMapping
    public AjaxResult edit(@RequestBody BeeTdCharginglog beeTdCharginglog)
    {
        return toAjax(beeTdCharginglogService.updateBeeTdCharginglog(beeTdCharginglog));
    }

    /**
     * 删除充电记录
     */
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(beeTdCharginglogService.deleteBeeTdCharginglogByIds(ids));
    }
}
