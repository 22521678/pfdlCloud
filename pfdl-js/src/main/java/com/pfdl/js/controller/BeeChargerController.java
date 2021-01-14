package com.pfdl.js.controller;


import com.pfdl.api.controllerUtil.BaseController;
import com.pfdl.api.jdbc.BeeCharger;
import com.pfdl.api.page.TableDataInfo;
import com.pfdl.common.utils.AjaxResult;
import com.pfdl.js.netty.server.NettyServerHandler;
import com.pfdl.js.service.IBeeChargerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 充电桩信息Controller
 * 
 * @author zhaoyt
 * @date 2020-12-10
 */
@RestController
@RequestMapping("/business/charger")
public class BeeChargerController extends BaseController
{
    @Autowired
    private IBeeChargerService beeChargerService;


    @Autowired
    private NettyServerHandler nettyServerHandler;

    /**
     * 查询充电桩信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BeeCharger beeCharger)
    {
        startPage();
        List<BeeCharger> list = beeChargerService.selectBeeChargerList(beeCharger);
//        SpringUtils.getBean(RedisCache.class).setCacheObject("cacheKey", "cacheValue");

        /** 主动发送消息*/
//        ChannelId channelId = CnovertSystem.getValueByKey(nettyServerHandler.pileMap, "ZD002");
//        String testHex = "4747220000000000000000000015019A70D95F2D6B";
//        byte[] bytes = CnovertSystem.hexStringToBytes(testHex);
//        nettyServerHandler.writeMessage(channelId,bytes);
        return getDataTable(list);
    }

    /**
     * 获取充电桩信息详细信息
     */
    @GetMapping(value = "getInfo/{chargerNo}")
    public AjaxResult getInfo(@PathVariable("chargerNo") String chargerNo)
    {
        return AjaxResult.success(beeChargerService.selectBeeChargerById(chargerNo));
    }

    /**
     * 新增充电桩信息
     */
    @PostMapping
    public AjaxResult add(@RequestBody BeeCharger beeCharger)
    {
        return toAjax(beeChargerService.insertBeeCharger(beeCharger));
    }

    /**
     * 修改充电桩信息
     */
    @PutMapping
    public AjaxResult edit(@RequestBody BeeCharger beeCharger)
    {
        return toAjax(beeChargerService.updateBeeCharger(beeCharger));
    }

    /**
     * 删除充电桩信息
     */
	@DeleteMapping("/{chargerNos}")
    public AjaxResult remove(@PathVariable String[] chargerNos)
    {
        return toAjax(beeChargerService.deleteBeeChargerByIds(chargerNos));
    }
}
