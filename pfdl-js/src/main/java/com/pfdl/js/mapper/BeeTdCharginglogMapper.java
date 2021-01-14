package com.pfdl.js.mapper;

import com.pfdl.api.jdbc.BeeTdCharginglog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 充电记录Mapper接口
 * 
 * @author zhaoyt
 * @date 2020-12-30
 */
@Mapper
public interface BeeTdCharginglogMapper 
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
     * 删除充电记录
     * 
     * @param id 充电记录ID
     * @return 结果
     */
    public int deleteBeeTdCharginglogById(String id);

    /**
     * 批量删除充电记录
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBeeTdCharginglogByIds(String[] ids);
}
