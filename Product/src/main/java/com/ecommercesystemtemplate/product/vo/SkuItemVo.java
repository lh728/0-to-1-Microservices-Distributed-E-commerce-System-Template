package com.ecommercesystemtemplate.product.vo;

import com.ecommercesystemtemplate.product.entity.SkuImagesEntity;
import com.ecommercesystemtemplate.product.entity.SkuInfoEntity;
import com.ecommercesystemtemplate.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

@Data
public class SkuItemVo {

    // 1. get sku base info
    SkuInfoEntity info;

    Boolean hasStock = true;

    // 2. get sku pic info
    List<SkuImagesEntity> images;

    // 3. get spu sale attr info
    List<SkuItemSaleAttrVo> saleAttr;

    // 4. get spu desc info
    SpuInfoDescEntity description;

    // 5. get spu spec attr info
    List<SpuItemAttrGroupVo> groupAttrs;

    // 6. get curr sku flashsale info
    FlashSaleInfoVo flashSaleInfo;



}
