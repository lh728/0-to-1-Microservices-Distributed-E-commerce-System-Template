package com.ecommercesystemtemplate.product.vo;

import com.ecommercesystemtemplate.product.entity.SkuImagesEntity;
import com.ecommercesystemtemplate.product.entity.SkuInfoEntity;
import com.ecommercesystemtemplate.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.sql.Statement;
import java.util.List;

@Data
public class SkuItemVo {

    // 1. get sku base info
    SkuInfoEntity info;

    // 2. get sku pic info
    List<SkuImagesEntity> images;

    // 3. get spu sale attr info
    List<SkuItemSaleAttrVo> saleAttr;

    // 4. get spu desc info
    SpuInfoDescEntity description;

    // 5. get spu spec attr info
    List<SpuItemAttrGroupVo> groupAttrs;

    @Data
    public static class SkuItemSaleAttrVo{

        private Long attrId;
        private String attrName;
        private List<String> attrValues;
    }

    @Data
    public static class SpuItemAttrGroupVo{

        private String groupName;
        private List<SpuBaseAttrVo> attrs;

    }

    @Data
    public static class SpuBaseAttrVo{

        private String attrName;
        private String attrValues;
    }

}
