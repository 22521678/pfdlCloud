package com.pfdl.js.config;

/**
 * 初始化费率
 * @Author zhaoyt
 * @Date 2020/12/23 13:06
 * @Version 1.0
 */

import com.pfdl.api.jdbc.RateEntity;
import com.pfdl.common.netty.JSConstants;
import com.pfdl.common.utils.spring.SpringUtils;
import com.pfdl.js.config.redis.RedisCache;
import com.pfdl.js.mapper.RateEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class MyApplicationRunner implements ApplicationRunner {
    @Autowired
    private RateEntityMapper rateMapper;
    @Override
    public void run(ApplicationArguments var1) throws Exception{
        /**
         * 初始化费率
         */
//        List<RateEntity> rateList = rateMapper.selectRateList();
//        List<RateEntity> newRateList = new ArrayList<>();
//        for (RateEntity entity : rateList) {
//            RateEntity newR = entity;
//            newR.setFee(new BigDecimal(entity.getFee()).setScale(2,BigDecimal.ROUND_UP).toString());
//            newR.setPrice(new BigDecimal(entity.getPrice()).setScale(2,BigDecimal.ROUND_UP).toString());
//            newRateList.add(newR);
//        }
//        SpringUtils.getBean(RedisCache.class).setCacheObject(JSConstants.JS_INIT_RATE, newRateList);
        System.out.println("intit Rate success..... length : ");
    }
}
