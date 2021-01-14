package com.pfdl.js.mapper;

import com.pfdl.api.jdbc.RateEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author zhaoyt
 * @Date 2020/12/23 11:52
 * @Version 1.0
 */
@Mapper
public interface RateEntityMapper {
    /**
     * 初始化查询费率
     * @return 每个时间段的费率
     */
    public List<RateEntity> selectRateList();
}
