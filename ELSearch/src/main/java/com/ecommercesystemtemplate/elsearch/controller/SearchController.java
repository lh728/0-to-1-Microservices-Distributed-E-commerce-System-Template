package com.ecommercesystemtemplate.elsearch.controller;

import com.ecommercesystemtemplate.elsearch.service.MallSearchService;
import com.ecommercesystemtemplate.elsearch.vo.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchController {

    final
    MallSearchService mallSearchService;

    public SearchController(MallSearchService mallSearchService) {
        this.mallSearchService = mallSearchService;
    }

    @GetMapping("/list.html")
    public String listPage(SearchParam searchParam) {

        Object result = mallSearchService.search(searchParam);


        return "list";
    }
}
