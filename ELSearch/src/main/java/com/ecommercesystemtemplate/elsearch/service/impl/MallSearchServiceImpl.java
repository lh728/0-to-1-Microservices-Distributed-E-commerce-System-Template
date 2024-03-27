package com.ecommercesystemtemplate.elsearch.service.impl;

import com.alibaba.fastjson.JSON;
import com.ecommercesystemtemplate.common.to.es.SkuEsModel;
import com.ecommercesystemtemplate.elsearch.config.ElasticConfig;
import com.ecommercesystemtemplate.elsearch.constant.EsConstant;
import com.ecommercesystemtemplate.elsearch.service.MallSearchService;
import com.ecommercesystemtemplate.elsearch.vo.SearchParam;
import com.ecommercesystemtemplate.elsearch.vo.SearchResult;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MallSearchServiceImpl implements MallSearchService {
    private final RestHighLevelClient restHighLevelClient;

    public MallSearchServiceImpl(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @Override
    public SearchResult search(SearchParam searchParam) {
        SearchResult result = null;
        // 1. prepare request
        SearchRequest searchRequest = buildSearchRequest(searchParam);
        try {
            // 2. call ES search
            SearchResponse response = restHighLevelClient.search(searchRequest, ElasticConfig.COMMON_OPTIONS);
            // 3. analyze response
            result = buildSearchResult(response,searchParam);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * build result data
     * @param response
     * @return
     */
    private SearchResult buildSearchResult(SearchResponse response, SearchParam params) {
        SearchResult searchResult = new SearchResult();
        // 1. return product
        SearchHits hits = response.getHits();
        List<SkuEsModel> list = new ArrayList<>();
        if (hits.getHits() != null) {
            for (SearchHit hit : hits.getHits()) {
                String string = hit.getSourceAsString();
                SkuEsModel sku = new SkuEsModel();
                SkuEsModel skuEsModel = JSON.parseObject(string, sku.getClass());
                if (!StringUtils.isEmpty(params.getKeyword())) {
                    HighlightField skuTitle = hit.getHighlightFields().get("skuTitle");
                    skuEsModel.setSkuTitle(skuTitle.getFragments()[0].string());
                }
                list.add(skuEsModel);
            }
        }
        searchResult.setProducts(list);

        // 2. return attrs
        ParsedNested attrsAgg = response.getAggregations().get("attrs_agg");
        ParsedLongTerms attrIdAgg = attrsAgg.getAggregations().get("attr_id_agg");
        List<SearchResult.AttrVo> attrVos = new ArrayList<>();
        for (Terms.Bucket bucket : attrIdAgg.getBuckets()) {
            SearchResult.AttrVo attrVo = new SearchResult.AttrVo();
            // 2.1 attr id
            long id = bucket.getKeyAsNumber().longValue();
            attrVo.setAttrId(id);
            // 2.2 attr name
            ParsedStringTerms attrNameAgg = bucket.getAggregations().get("attr_name_agg");
            String attrName = attrNameAgg.getBuckets().get(0).getKeyAsString();
            attrVo.setAttrName(attrName);
            // 2.3 attr value
            ParsedStringTerms attrValueAgg = bucket.getAggregations().get("attr_value_agg");
            List<String> attrValues = attrValueAgg.getBuckets().stream().map(MultiBucketsAggregation.Bucket::getKeyAsString).toList();
            attrVo.setAttrValue(attrValues);
            attrVos.add(attrVo);
        }
        searchResult.setAttrs(attrVos);

        // 3. return brand
        List<SearchResult.BrandVo> brandVos = new ArrayList<>();
        ParsedLongTerms brandAgg = response.getAggregations().get("brand_agg");
        for (Terms.Bucket bucket : brandAgg.getBuckets()) {
            SearchResult.BrandVo brandVo = new SearchResult.BrandVo();
            // 3.1 brand id
            String keyAsString = bucket.getKeyAsString();
            brandVo.setBrandId(Long.parseLong(keyAsString));
            // 3.2 brand name
            ParsedStringTerms brandNameAgg = bucket.getAggregations().get("brand_name_agg");
            String brandName = brandNameAgg.getBuckets().get(0).getKeyAsString();
            brandVo.setBrandName(brandName);
            // 3.3 brand img
            ParsedStringTerms brandImgAgg = bucket.getAggregations().get("brand_img_agg");
            String brandImg = brandImgAgg.getBuckets().get(0).getKeyAsString();
            brandVo.setBrandImg(brandImg);
            brandVos.add(brandVo);
        }
        searchResult.setBrands(brandVos);

        // 4. return catalog
        List<SearchResult.CatalogVo> catalogVos = new ArrayList<>();
        ParsedLongTerms catalogAgg = response.getAggregations().get("catalog_agg");
        for (Terms.Bucket bucket : catalogAgg.getBuckets()) {
            SearchResult.CatalogVo catalogVo = new SearchResult.CatalogVo();
            // 4.1 catalog id
            String keyAsString = bucket.getKeyAsString();
            catalogVo.setCatalogId(Long.parseLong(keyAsString));
            // 4.2 catalog name
            ParsedStringTerms catalogNameAgg = bucket.getAggregations().get("catalog_name_agg");
            String catalogName = catalogNameAgg.getBuckets().get(0).getKeyAsString();
            catalogVo.setCatalogName(catalogName);
            catalogVos.add(catalogVo);
        }
        searchResult.setCatalogs(catalogVos);

        // 5. pagination
        // 5.1 curr page
        searchResult.setPageNum(params.getPageNum());
        // 5.2 total count
        long total = hits.getTotalHits().value;
        searchResult.setTotal(total);
        // 5.3 page num
        int totalPages = total % EsConstant.PRODUCT_PAGE_SIZE == 0 ?
                (int) total / EsConstant.PRODUCT_PAGE_SIZE : (int) total / EsConstant.PRODUCT_PAGE_SIZE + 1;
        searchResult.setTotalPages(totalPages);
        return searchResult;
    }

    /**
     * build search request
     * @return
     */
    private SearchRequest buildSearchRequest(SearchParam searchParam) {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); // build DSL

        // 1. puzzle match and filter (attributes, brand, catalog, price, stock)
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // 1.1 must match
        if (StringUtils.isNotEmpty(searchParam.getKeyword())) {
            queryBuilder.must(QueryBuilders.matchQuery("skuTitle", searchParam.getKeyword()));
        }
        // 1.2 filter - catalog3Id
        if (searchParam.getCatalog3Id() != null) {
            queryBuilder.filter(QueryBuilders.termQuery("catalogId", searchParam.getCatalog3Id()));
        }
        // 1.2 filter - brand
        if (searchParam.getBrandId() != null && !searchParam.getBrandId().isEmpty()) {
            queryBuilder.filter(QueryBuilders.termsQuery("brandId", searchParam.getBrandId()));
        }
        // 1.2 filter - attributes
        if (searchParam.getAttrs() != null && !searchParam.getAttrs().isEmpty()) {
            // attrs=1_5寸:8寸&attrs=2_16G:8G
            for (String attr : searchParam.getAttrs()) {
                String[] s = attr.split("_");
                queryBuilder.filter(QueryBuilders.nestedQuery("attrs",
                        QueryBuilders.boolQuery()
                                .must(QueryBuilders.termQuery("attrs.attrId", s[0])) // attrId
                                .must(QueryBuilders.termsQuery("attrs.attrValue", s[1].split(":"))), // attrValue
                        ScoreMode.None));
            }
        }
        // 1.2 filter - price
        if (StringUtils.isNotEmpty(searchParam.getSkuPrice())) {
            // 1_500/_500/500_
            String[] s = searchParam.getSkuPrice().split("_");
            if (s.length == 2) {
                queryBuilder.filter(QueryBuilders.rangeQuery("skuPrice").gte(s[0]).lte(s[1]));
            } else if (s.length == 1) {
                if (searchParam.getSkuPrice().startsWith("_")) {
                    queryBuilder.filter(QueryBuilders.rangeQuery("skuPrice").lte(s[0]));
                } else {
                    queryBuilder.filter(QueryBuilders.rangeQuery("skuPrice").gte(s[0]));
                }
            }
        }
        // 1.2 filter - stock
        if (searchParam.getHasStock() != null){
            queryBuilder.filter(QueryBuilders.termQuery("hasStock", searchParam.getHasStock() == 1));
        }

        searchSourceBuilder.query(queryBuilder);
        // 2. order, pagination, highlight
        // 2.1 sort order
        if (StringUtils.isNotEmpty(searchParam.getSort())) {
            String[] s = searchParam.getSort().split(":");
            searchSourceBuilder.sort(s[0], "asc".equalsIgnoreCase(s[1]) ?
                    org.elasticsearch.search.sort.SortOrder.ASC : org.elasticsearch.search.sort.SortOrder.DESC);
        }
        // 2.2 pagination
        searchSourceBuilder.from((searchParam.getPageNum() - 1) * EsConstant.PRODUCT_PAGE_SIZE);
        searchSourceBuilder.size(EsConstant.PRODUCT_PAGE_SIZE);

        // 2.3 highlight
        if (StringUtils.isNotEmpty(searchParam.getKeyword())) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle").preTags("<b style='color:red'>").postTags("</b>");
            searchSourceBuilder.highlighter(highlightBuilder);
        }
        // 3. aggregation
        // 3.1 brand aggregation
        // 3.1.1 brand sub-aggregation
        TermsAggregationBuilder brandAgg = AggregationBuilders.terms("brand_agg");
        brandAgg.field("brandId").size(50);
        brandAgg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName").size(1));
        brandAgg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg").size(1));
        searchSourceBuilder.aggregation(brandAgg);

        // 3.2 catalog aggregation
        TermsAggregationBuilder catalogAgg = AggregationBuilders.terms("catalog_agg");
        catalogAgg.field("catalogId").size(20);
        catalogAgg.subAggregation(AggregationBuilders.terms("catalog_name_agg").field("catalogName").size(1));
        searchSourceBuilder.aggregation(catalogAgg);

        // 3.3 attrs aggregation
        // nested aggregation
        NestedAggregationBuilder nestedAggregationBuilder = AggregationBuilders.nested("attrs_agg", "attrs")
                .subAggregation(AggregationBuilders.terms("attr_id_agg").field("attrs.attrId").size(50)
                        .subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName").size(1))
                        .subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(50)));
        searchSourceBuilder.aggregation(nestedAggregationBuilder);


        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, searchSourceBuilder);
//        System.out.println("DSL: " + searchSourceBuilder.toString());
        return searchRequest;
    }
}
