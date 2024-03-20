package com.ecommercesystemtemplate.elsearch.service.impl;

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
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;

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
            result = buildSearchResult(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * build result data
     * @param response
     * @return
     */
    private SearchResult buildSearchResult(SearchResponse response) {


        return null;
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
        queryBuilder.filter(QueryBuilders.termQuery("hasStock", searchParam.getHasStock() == 1));

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
        return searchRequest;
    }
}
