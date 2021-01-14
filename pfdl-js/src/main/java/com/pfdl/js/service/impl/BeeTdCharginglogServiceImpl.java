package com.pfdl.js.service.impl;

import com.pfdl.api.jdbc.BeeTdCharginglog;
import com.pfdl.js.mapper.BeeTdCharginglogMapper;
import com.pfdl.js.service.IBeeTdCharginglogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 充电记录Service业务层处理
 * 
 * @author zhaoyt
 * @date 2020-12-30
 */
@Service
public class BeeTdCharginglogServiceImpl implements IBeeTdCharginglogService
{
    @Autowired
    private BeeTdCharginglogMapper beeTdCharginglogMapper;

    /**
     * 查询充电记录
     * 
     * @param id 充电记录ID
     * @return 充电记录
     */
    @Override
    public BeeTdCharginglog selectBeeTdCharginglogById(String id)
    {
        return beeTdCharginglogMapper.selectBeeTdCharginglogById(id);
    }

    /**
     * 查询充电记录列表
     * 
     * @param beeTdCharginglog 充电记录
     * @return 充电记录
     */
    @Override
    public List<BeeTdCharginglog> selectBeeTdCharginglogList(BeeTdCharginglog beeTdCharginglog)
    {
        return beeTdCharginglogMapper.selectBeeTdCharginglogList(beeTdCharginglog);
    }

    /**
     * 新增充电记录
     * 
     * @param beeTdCharginglog 充电记录
     * @return 结果
     */
    @Override
    public int insertBeeTdCharginglog(BeeTdCharginglog beeTdCharginglog)
    {
        return beeTdCharginglogMapper.insertBeeTdCharginglog(beeTdCharginglog);
    }

    /**
     * 修改充电记录
     * 
     * @param beeTdCharginglog 充电记录
     * @return 结果
     */
    @Override
    public int updateBeeTdCharginglog(BeeTdCharginglog beeTdCharginglog)
    {
        return beeTdCharginglogMapper.updateBeeTdCharginglog(beeTdCharginglog);
    }

    /**
     * 批量删除充电记录
     * 
     * @param ids 需要删除的充电记录ID
     * @return 结果
     */
    @Override
    public int deleteBeeTdCharginglogByIds(String[] ids)
    {
        return beeTdCharginglogMapper.deleteBeeTdCharginglogByIds(ids);
    }

    /**
     * 删除充电记录信息
     * 
     * @param id 充电记录ID
     * @return 结果
     */
    @Override
    public int deleteBeeTdCharginglogById(String id)
    {
        return beeTdCharginglogMapper.deleteBeeTdCharginglogById(id);
    }
}
