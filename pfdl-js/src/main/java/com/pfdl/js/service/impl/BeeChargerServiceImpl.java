package com.pfdl.js.service.impl;

import com.pfdl.api.jdbc.BeeCharger;
import com.pfdl.js.mapper.BeeChargerMapper;
import com.pfdl.js.service.IBeeChargerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 充电桩信息Service业务层处理
 * 
 * @author zhaoyt
 * @date 2020-12-10
 */
@Service
public class BeeChargerServiceImpl implements IBeeChargerService
{
    @Autowired
    private BeeChargerMapper beeChargerMapper;

    /**
     * 查询充电桩信息
     * 
     * @param chargerNo 充电桩信息ID
     * @return 充电桩信息
     */
    @Override
    public BeeCharger selectBeeChargerById(String chargerNo)
    {
        return beeChargerMapper.selectBeeChargerById(chargerNo);
    }
    /**
     * 查询充电桩信息
     *
     * @param productSno 充电桩信息ID
     * @return 充电桩信息
     */
    @Override
    public BeeCharger selectBeeChargerByProductSno(String productSno)
    {
        return beeChargerMapper.selectBeeChargerByProductSno(productSno);
    }

    /**
     * 查询充电桩信息列表
     * 
     * @param beeCharger 充电桩信息
     * @return 充电桩信息
     */
    @Override
    public List<BeeCharger> selectBeeChargerList(BeeCharger beeCharger)
    {
        return beeChargerMapper.selectBeeChargerList(beeCharger);
    }

    /**
     * 新增充电桩信息
     * 
     * @param beeCharger 充电桩信息
     * @return 结果
     */
    @Override
    public int insertBeeCharger(BeeCharger beeCharger)
    {
        return beeChargerMapper.insertBeeCharger(beeCharger);
    }

    /**
     * 修改充电桩信息
     * 
     * @param beeCharger 充电桩信息
     * @return 结果
     */
    @Override
    public int updateBeeCharger(BeeCharger beeCharger)
    {
        return beeChargerMapper.updateBeeCharger(beeCharger);
    }

    /**
     * 批量删除充电桩信息
     * 
     * @param chargerNos 需要删除的充电桩信息ID
     * @return 结果
     */
    @Override
    public int deleteBeeChargerByIds(String[] chargerNos)
    {
        return beeChargerMapper.deleteBeeChargerByIds(chargerNos);
    }

    /**
     * 删除充电桩信息信息
     * 
     * @param chargerNo 充电桩信息ID
     * @return 结果
     */
    @Override
    public int deleteBeeChargerById(String chargerNo)
    {
        return beeChargerMapper.deleteBeeChargerById(chargerNo);
    }

    @Override
    public int updateOrderLogStatusByIdTag(String sessionId,String status) {
        return beeChargerMapper.updateOrderLogStatusByIdTag(sessionId,status);
    }

    @Override
    public int updateOrderLogStatusByIdTag(Map<String, String> stringMap) {
        return beeChargerMapper.updateOrderLogStatusByIdTagMap(stringMap);
    }

    @Override
    public int list() {
     return beeChargerMapper.selectCountNum();

    }
}
