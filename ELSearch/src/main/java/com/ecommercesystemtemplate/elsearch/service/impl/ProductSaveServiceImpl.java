package com.ecommercesystemtemplate.elsearch.service.impl;

import com.alibaba.fastjson.JSON;
import com.ecommercesystemtemplate.common.to.es.SkuEsModel;
import com.ecommercesystemtemplate.elsearch.config.ElasticConfig;
import com.ecommercesystemtemplate.elsearch.constant.EsConstant;
import com.ecommercesystemtemplate.elsearch.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
@Service
@Slf4j
public class ProductSaveServiceImpl implements ProductSaveService {
    final
    RestHighLevelClient restHighLevelClient;

    public ProductSaveServiceImpl(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @Override
    public boolean productToList(List<SkuEsModel> skuEsModels) throws IOException {
        // 1. build index, data type

        // 2. build mapping

        // 3. save data
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel skuEsModel : skuEsModels) {
            // build save request
            IndexRequest indexRequest = new IndexRequest();
            indexRequest.index(EsConstant.PRODUCT_INDEX).id(skuEsModel.getSkuId().toString());
            String jsonString = JSON.toJSONString(skuEsModel);
            indexRequest.source(jsonString, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, ElasticConfig.COMMON_OPTIONS);
        boolean hasFailures = bulk.hasFailures();
        if (hasFailures) {
            List<String> collect = Arrays.stream(bulk.getItems()).map(BulkItemResponse::getId).toList();
            log.error("Commodity data save failed, {} ", collect);
        }
        return hasFailures;

    }
}
