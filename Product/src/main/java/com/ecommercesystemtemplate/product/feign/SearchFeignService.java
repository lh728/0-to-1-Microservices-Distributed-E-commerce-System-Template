package com.ecommercesystemtemplate.product.feign;

import com.ecommercesystemtemplate.common.to.es.SkuEsModel;
import com.ecommercesystemtemplate.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.List;

@FeignClient("search")
public interface SearchFeignService {

    @PostMapping("/search/save/product")
    R productToList(@RequestBody List<SkuEsModel> skuEsModels);
}
