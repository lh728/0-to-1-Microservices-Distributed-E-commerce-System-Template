package com.ecommercesystemtemplate.flashsale.scheduled;

import com.ecommercesystemtemplate.flashsale.service.FlashSaleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FlashSaleSkuSchedule {
    final
    FlashSaleService flashSaleService;

    public FlashSaleSkuSchedule(FlashSaleService flashSaleService) {
        this.flashSaleService = flashSaleService;
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void uploadFlashSaleSkuLatest30Days() {
        flashSaleService.uploadFlashSaleSkuLatest30Days();
    }


}
