package com.ecommercesystemtemplate.elsearch.service;

import com.ecommercesystemtemplate.common.to.es.SkuEsModel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface ProductSaveService {
    boolean productToList(List<SkuEsModel> skuEsModels) throws IOException;
}
