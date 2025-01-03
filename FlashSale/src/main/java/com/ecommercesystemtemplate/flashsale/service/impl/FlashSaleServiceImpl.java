package com.ecommercesystemtemplate.flashsale.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.ecommercesystemtemplate.common.utils.R;
import com.ecommercesystemtemplate.flashsale.feign.CouponFeignService;
import com.ecommercesystemtemplate.flashsale.service.FlashSaleService;
import com.ecommercesystemtemplate.flashsale.vo.FlashSaleSessionsWithSku;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlashSaleServiceImpl implements FlashSaleService {
    final
    CouponFeignService couponFeignService;
    final RedisTemplate redisTemplate;

    public FlashSaleServiceImpl(CouponFeignService couponFeignService, RedisTemplate redisTemplate) {
        this.couponFeignService = couponFeignService;
        this.redisTemplate = redisTemplate;
    }


    @Override
    public void uploadFlashSaleSkuLatest30Days() {
        // scan all active flash sale (past 3 days)
        R session = couponFeignService.getLatest3DaySession();
        if (session.getCode() == 0) {
            List<FlashSaleSessionsWithSku> data = session.getData(new TypeReference<List<FlashSaleSessionsWithSku>>() {
            });
            // cache to redis
            // 1. cache activity info
            saveSessionInfos(data);
            // 2. cache sku info
            saveSessionSkuInfos(data);
        }
    }

    private void saveSessionInfos(List<FlashSaleSessionsWithSku> sessions){

    }

    private void saveSessionSkuInfos(List<FlashSaleSessionsWithSku> sessions){

    }

}
