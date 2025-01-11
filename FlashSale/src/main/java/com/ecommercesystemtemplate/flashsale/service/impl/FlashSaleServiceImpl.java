package com.ecommercesystemtemplate.flashsale.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ecommercesystemtemplate.common.utils.R;
import com.ecommercesystemtemplate.flashsale.feign.CouponFeignService;
import com.ecommercesystemtemplate.flashsale.feign.ProductFeignService;
import com.ecommercesystemtemplate.flashsale.service.FlashSaleService;
import com.ecommercesystemtemplate.flashsale.to.FlashSaleSkuRedisTo;
import com.ecommercesystemtemplate.flashsale.vo.FlashSaleSessionsWithSku;
import com.ecommercesystemtemplate.flashsale.vo.SkuInfoVo;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FlashSaleServiceImpl implements FlashSaleService {
    final
    CouponFeignService couponFeignService;
    final RedisTemplate redisTemplate;
    final ProductFeignService productFeignService;

    final RedissonClient redissonClient;
    private final String SESSIONS_CACHE_PREFIX = "flashsale:sessions:";
    private final String SKU_FLASHSALE_CACHE_PREFIX = "flashsale:skus:";
    private final String SKU_STOCK_SEMAPHORE = "flashsale:stock:"; // + random code

    public FlashSaleServiceImpl(CouponFeignService couponFeignService, RedisTemplate redisTemplate, ProductFeignService productFeignService, RedissonClient redissonClient) {
        this.couponFeignService = couponFeignService;
        this.redisTemplate = redisTemplate;
        this.productFeignService = productFeignService;
        this.redissonClient = redissonClient;
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
        sessions.stream().forEach(session -> {
            Long startTime = session.getStartTime().getTime();
            Long endTime = session.getEndTime().getTime();
            String key = SESSIONS_CACHE_PREFIX + startTime + "-" + endTime;
            List<String> list = session.getFlashSaleSkuVos().stream().map(item -> item.getId().toString()).toList();
            redisTemplate.opsForList().leftPushAll(key, list);

        });
    }

    private void saveSessionSkuInfos(List<FlashSaleSessionsWithSku> sessions){
        sessions.stream().forEach(session -> {
            BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(SKU_FLASHSALE_CACHE_PREFIX);
            session.getFlashSaleSkuVos().stream().forEach(item -> {
                FlashSaleSkuRedisTo flashSaleSkuRedisTo = new FlashSaleSkuRedisTo();
                // 1. basic info
                R skuInfo = productFeignService.getSkuInfo(item.getSkuId());
                if (skuInfo.getCode() == 0) {
                    SkuInfoVo data = skuInfo.getData(new TypeReference<SkuInfoVo>() {
                    });
                    flashSaleSkuRedisTo.setSkuInfoVo(data);
                }
                // 2. flash sale info
                BeanUtils.copyProperties(item, flashSaleSkuRedisTo);
                // 3. set time
                flashSaleSkuRedisTo.setStartTime(session.getStartTime().getTime());
                flashSaleSkuRedisTo.setEndTime(session.getEndTime().getTime());
                // 4. set product random code
                String token = UUID.randomUUID().toString().replace("-", "");
                flashSaleSkuRedisTo.setRandomCode(token);
                // 5. use stock number as distribution semaphore (Rate Limiting)
                RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + token);
                semaphore.trySetPermits(item.getSeckillCount().intValue());

                String s = JSON.toJSONString(item);
                ops.put(item.getId().toString(), s);
            });
        });
    }

}
