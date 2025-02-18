package com.ecommercesystemtemplate.product.fallBack;

import com.ecommercesystemtemplate.common.exception.BizCodeEnume;
import com.ecommercesystemtemplate.common.utils.R;
import com.ecommercesystemtemplate.product.feign.FlashSaleFeignService;
import org.springframework.stereotype.Component;

@Component
public class FlashSaleFeignServiceFallBack implements FlashSaleFeignService {
    @Override
    public R getFlashSaleSkuInfo(Long skuId) {
        return R.error(BizCodeEnume.TOO_MANY_REQUEST.getCode(), BizCodeEnume.TOO_MANY_REQUEST.getMessage());
    }
}
