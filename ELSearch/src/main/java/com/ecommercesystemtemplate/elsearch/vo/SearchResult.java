package com.ecommercesystemtemplate.elsearch.vo;

import com.ecommercesystemtemplate.common.to.es.SkuEsModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SearchResult {

    private List<SkuEsModel> products;

    private Integer pageNum;

    private Long total;

    private Integer totalPages;

    private List<Integer> pageNavs;

    private List<BrandVo> brands;

    @Data
    public static class BrandVo {
        private Long brandId;
        private String brandName;
        private String brandImg;
    }

    private List<CatalogVo> catalogs;

    @Data
    public static class CatalogVo {
        private Long catalogId;
        private String catalogName;
    }

    private List<AttrVo> attrs;
    @Data
    public static class AttrVo {
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }

    /**
     * Breadcrumb
     */
    private List<NavVo> navs = new ArrayList<>();
    private List<Long> attrIds = new ArrayList<>();

    @Data
    public static class NavVo{
        private String navName;
        private String navValue;
        private String link;
    }
}
