package com.ecommercesystemtemplate.elsearch.service;

import com.ecommercesystemtemplate.elsearch.vo.SearchParam;

public interface MallSearchService {

    /**
     * return search result
     * @param searchParam search param
     * @return return search result
     */
    Object search(SearchParam searchParam);
}
