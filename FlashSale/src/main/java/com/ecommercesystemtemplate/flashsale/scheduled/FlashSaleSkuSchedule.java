package com.ecommercesystemtemplate.flashsale.scheduled;

import com.ecommercesystemtemplate.flashsale.service.FlashSaleService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class FlashSaleSkuSchedule {
    final
    FlashSaleService flashSaleService;
    final RedissonClient redissonClient;
    private final String UPLOAD_LOCK = "flashsale:upload:lock";

    public FlashSaleSkuSchedule(FlashSaleService flashSaleService, RedissonClient redissonClient) {
        this.flashSaleService = flashSaleService;
        this.redissonClient = redissonClient;
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void uploadFlashSaleSkuLatest30Days() {
        // add distributed lock
        RLock lock = redissonClient.getLock(UPLOAD_LOCK);
        lock.lock(10, TimeUnit.SECONDS);
        // upload
        try{
            flashSaleService.uploadFlashSaleSkuLatest30Days();
        } finally {
            // release lock
            lock.unlock();
        }
    }


}
