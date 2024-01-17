package com.ecommercesystemtemplate.product.feign;

import com.ecommercesystemtemplate.common.to.SkuReductionTo;
import com.ecommercesystemtemplate.common.utils.R;
import com.ecommercesystemtemplate.product.vo.SkuHasStockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("warehouse")
public interface WareFeignService {
    @PostMapping("/warehouse/waresku/hasStock")
    R getSkusHaveStock(@RequestBody List<Long> skuIds);
}
