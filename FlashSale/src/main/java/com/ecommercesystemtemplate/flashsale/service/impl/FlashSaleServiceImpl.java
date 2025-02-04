package com.ecommercesystemtemplate.flashsale.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.ecommercesystemtemplate.common.utils.R;
import com.ecommercesystemtemplate.common.vo.MemberResponseVo;
import com.ecommercesystemtemplate.flashsale.feign.CouponFeignService;
import com.ecommercesystemtemplate.flashsale.feign.ProductFeignService;
import com.ecommercesystemtemplate.flashsale.interceptor.LoginUserInterceptor;
import com.ecommercesystemtemplate.flashsale.service.FlashSaleService;
import com.ecommercesystemtemplate.flashsale.to.FlashSaleSkuRedisTo;
import com.ecommercesystemtemplate.flashsale.vo.FlashSaleSessionsWithSku;
import com.ecommercesystemtemplate.flashsale.vo.SkuInfoVo;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

    @Override
    public List<FlashSaleSkuRedisTo> getCurrentFlashSaleSkus() {
        // 1. get current time
        Long currentTime = new Date().getTime();
        Set<String> keys = redisTemplate.keys(SESSIONS_CACHE_PREFIX + "*");
        for (String key : keys) {
            String replace = key.replace(SESSIONS_CACHE_PREFIX, "");
            String[] split = replace.split("-");
            Long startTime = Long.parseLong(split[0]);
            Long endTime = Long.parseLong(split[1]);
            if (currentTime >= startTime && currentTime <= endTime) {
                // 2. get sku list from redis
                List<String> list = redisTemplate.opsForList().range(key, -100, 100);
                BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(SKU_FLASHSALE_CACHE_PREFIX);
                List<String> objects = ops.multiGet(list);
                if (objects != null && !objects.isEmpty()) {
                    List<FlashSaleSkuRedisTo> collect = objects.stream().map(item -> {
                        FlashSaleSkuRedisTo redisTo = JSON.parseObject((String) item, FlashSaleSkuRedisTo.class);
//                        redisTo.setRandomCode();
                        return redisTo;
                    }).toList();
                    return collect;
                }
                break;
            }
        }

        return null;

    }

    @Override
    public FlashSaleSkuRedisTo getFlashSaleSkuInfo(Long skuId) {
        // find all product key that needs to be participated in flash sale
        BoundHashOperations<String,String,String> ops = redisTemplate.boundHashOps(SKU_FLASHSALE_CACHE_PREFIX);
        Set<String> keys = ops.keys();
        if (keys != null && !keys.isEmpty()) {
            String regx = "\\d" + skuId;
            for (String key : keys) {
                if (key.matches(regx)) {
                    String json = ops.get(key);
                    FlashSaleSkuRedisTo redisTo = JSON.parseObject(json, FlashSaleSkuRedisTo.class);
                    // random code
                    long current =  new Date().getTime();
                    if (current >= redisTo.getStartTime() && current <= redisTo.getEndTime()) {

                    }else{
                        redisTo.setRandomCode(null);
                    }
                    return redisTo;
                }
            }
        }
        return null;


    }

    @Override
    public String flashSale(String flashSaleId, String key, Integer num) {
        MemberResponseVo memberResponseVo = LoginUserInterceptor.loginUser.get();

        // 1. get all info about current flash sale
        BoundHashOperations<String,String,String> ops = redisTemplate.boundHashOps(SKU_FLASHSALE_CACHE_PREFIX);
        String json = ops.get(flashSaleId);
        if (StringUtils.isEmpty(json)) {
            return null;
        }else{
            FlashSaleSkuRedisTo redisTo = JSON.parseObject(json, FlashSaleSkuRedisTo.class);
            // check time
            Long startTime = redisTo.getStartTime();
            Long endTime = redisTo.getEndTime();
            Long current = new Date().getTime();
            Long ttl = endTime - current;
            if(current >= startTime && current <= endTime){
                // check random code and id
                String randomCode = redisTo.getRandomCode();
                String skuId = redisTo.getPromotionSessionId() + "-" + redisTo.getSkuId();
                if (randomCode.equals(key) && skuId.equals(flashSaleId)) {
                    // check stock
                    if(num <= redisTo.getSeckillLimit()){
                        // check if has bought
                        String redisKey = memberResponseVo.getId() + "_" + skuId;
                        Boolean b = redisTemplate.opsForValue().setIfAbsent(redisKey, num.toString(), ttl, TimeUnit.MILLISECONDS);
                        if (b) {
                            // means this person never buy this sku
                            RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + randomCode);
                            try {
                                boolean tryAcquire = semaphore.tryAcquire(num, 100, TimeUnit.MILLISECONDS);
                                // success and send message
                                String timeId = IdWorker.getTimeId();
                                return timeId;
                            } catch (InterruptedException e) {
                                return null;
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    private void saveSessionInfos(List<FlashSaleSessionsWithSku> sessions){
        sessions.stream().forEach(session -> {
            Long startTime = session.getStartTime().getTime();
            Long endTime = session.getEndTime().getTime();
            String key = SESSIONS_CACHE_PREFIX + startTime + "-" + endTime;
            Boolean hasKey = redisTemplate.hasKey(key);
            if (!hasKey) {
                List<String> list = session.getFlashSaleSkuVos().stream().map(item ->
                        item.getPromotionSessionId().toString() + "-" + item.getSkuId().toString()).toList();
                redisTemplate.opsForList().leftPushAll(key, list);
            }

        });
    }

    private void saveSessionSkuInfos(List<FlashSaleSessionsWithSku> sessions){
        sessions.stream().forEach(session -> {
            BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(SKU_FLASHSALE_CACHE_PREFIX);
            session.getFlashSaleSkuVos().stream().forEach(item -> {
                // set product random code
                String token = UUID.randomUUID().toString().replace("-", "");
                if (Boolean.FALSE.equals(ops.hasKey(item.getPromotionSessionId().toString() + "-" + item.getSkuId().toString()))){
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

                    flashSaleSkuRedisTo.setRandomCode(token);

                    String s = JSON.toJSONString(item);
                    ops.put(item.getPromotionSessionId().toString() + "-" + item.getSkuId().toString(), s);

                    // 4. use stock number as distribution semaphore (Rate Limiting)
                    RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + token);
                    semaphore.trySetPermits(item.getSeckillCount().intValue());
                }

            });
        });
    }

}
