package com.ecommercesystemtemplate.elsearch.controller;

import com.ecommercesystemtemplate.common.exception.BizCodeEnume;
import com.ecommercesystemtemplate.common.to.es.SkuEsModel;
import com.ecommercesystemtemplate.common.utils.R;
import com.ecommercesystemtemplate.elsearch.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/search/save")
@Slf4j
public class ElasticSaveController {

    final
    ProductSaveService productSaveService;

    public ElasticSaveController(ProductSaveService productSaveService) {
        this.productSaveService = productSaveService;
    }

    // to list
    @PostMapping("/product")
    public R productToList(@RequestBody List<SkuEsModel> skuEsModels){
        boolean b = false;
        try {
            b = productSaveService.productToList(skuEsModels);
        } catch (IOException e) {
            log.error("Commodity data to list failed, {}", e.getMessage());
            return R.error(BizCodeEnume.PRODUCT_TO_LIST_FAILED.getCode(), BizCodeEnume.PRODUCT_TO_LIST_FAILED.getMessage());
        }

        if (b) {
            return R.ok();
        } else {
            return R.error(BizCodeEnume.PRODUCT_TO_LIST_FAILED.getCode(), BizCodeEnume.PRODUCT_TO_LIST_FAILED.getMessage());
        }
    }

}
