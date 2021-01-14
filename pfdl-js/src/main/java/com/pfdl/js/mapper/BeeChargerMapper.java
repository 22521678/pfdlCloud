package com.pfdl.js.mapper;

import com.pfdl.api.jdbc.BeeCharger;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 充电桩信息Mapper接口
 * 
 * @author zhaoyt
 * @date 2020-12-10
 */
@Mapper
public interface BeeChargerMapper
{
    /**
     * 查询充电桩信息
     *
     * @param chargerNo 充电桩信息ID
     * @return 充电桩信息
     */
     BeeCharger selectBeeChargerById(String chargerNo);
    /**
     * 查询充电桩信息
     *
     * @param productSno 充电桩信息ID
     * @return 充电桩信息
     */
     BeeCharger selectBeeChargerByProductSno(String productSno);
    /**
     * 查询充电桩信息列表
     *
     * @param beeCharger 充电桩信息
     * @return 充电桩信息集合
     */
     List<BeeCharger> selectBeeChargerList(BeeCharger beeCharger);

    /**
     * 新增充电桩信息
     *
     * @param beeCharger 充电桩信息
     * @return 结果
     */
     int insertBeeCharger(BeeCharger beeCharger);

    /**
     * 修改充电桩信息
     *
     * @param beeCharger 充电桩信息
     * @return 结果
     */
     int updateBeeCharger(BeeCharger beeCharger);

    /**
     * 删除充电桩信息
     *
     * @param chargerNo 充电桩信息ID
     * @return 结果
     */
     int deleteBeeChargerById(String chargerNo);

    /**
     * 批量删除充电桩信息
     *
     * @param chargerNos 需要删除的数据ID
     * @return 结果
     */
     int deleteBeeChargerByIds(String[] chargerNos);
    /**
     * 根据充电桩返回信息更新 数据库状态
     * @param sessionId IdTag
     * @return 执行行数
     */

    int updateOrderLogStatusByIdTag(String sessionId,String status);

    int selectCountNum();

    int updateOrderLogStatusByIdTagMap(Map<String, String> stringMap);
}
