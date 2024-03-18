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
import org.elasticsearch.search.builder.SearchSourceBuilder;
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

        // 3. aggregation



        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, searchSourceBuilder);
        return searchRequest;
    }
}
