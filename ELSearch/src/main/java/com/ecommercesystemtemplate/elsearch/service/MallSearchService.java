package com.ecommercesystemtemplate.elsearch.service;

import com.ecommercesystemtemplate.elsearch.vo.SearchParam;
import com.ecommercesystemtemplate.elsearch.vo.SearchResult;

public interface MallSearchService {

    /**
     * return search result
     *
     * @param searchParam search param
     * @return return search result
     */
    SearchResult search(SearchParam searchParam);
}
