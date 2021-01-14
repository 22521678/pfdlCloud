package com.pfdl.js.service;


import com.pfdl.api.jdbc.BeeTdCharginglog;

import java.util.List;

/**
 * 充电记录Service接口
 * 
 * @author zhaoyt
 * @date 2020-12-30
 */
public interface IBeeTdCharginglogService 
{
    /**
     * 查询充电记录
     * 
     * @param id 充电记录ID
     * @return 充电记录
     */
    public BeeTdCharginglog selectBeeTdCharginglogById(String id);

    /**
     * 查询充电记录列表
     * 
     * @param beeTdCharginglog 充电记录
     * @return 充电记录集合
     */
    public List<BeeTdCharginglog> selectBeeTdCharginglogList(BeeTdCharginglog beeTdCharginglog);

    /**
     * 新增充电记录
     * 
     * @param beeTdCharginglog 充电记录
     * @return 结果
     */
    public int insertBeeTdCharginglog(BeeTdCharginglog beeTdCharginglog);

    /**
     * 修改充电记录
     * 
     * @param beeTdCharginglog 充电记录
     * @return 结果
     */
    public int updateBeeTdCharginglog(BeeTdCharginglog beeTdCharginglog);

    /**
     * 批量删除充电记录
     * 
     * @param ids 需要删除的充电记录ID
     * @return 结果
     */
    public int deleteBeeTdCharginglogByIds(String[] ids);

    /**
     * 删除充电记录信息
     * 
     * @param id 充电记录ID
     * @return 结果
     */
    public int deleteBeeTdCharginglogById(String id);
}
