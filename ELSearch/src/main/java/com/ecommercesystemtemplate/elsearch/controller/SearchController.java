package com.ecommercesystemtemplate.elsearch.controller;

import com.ecommercesystemtemplate.elsearch.service.MallSearchService;
import com.ecommercesystemtemplate.elsearch.vo.SearchParam;
import com.ecommercesystemtemplate.elsearch.vo.SearchResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SearchController {

    final
    MallSearchService mallSearchService;

    public SearchController(MallSearchService mallSearchService) {
        this.mallSearchService = mallSearchService;
    }

    @GetMapping("/list.html")
    public String listPage(SearchParam searchParam, Model model, HttpServletRequest request){

        String queryString = request.getQueryString();
        searchParam.set_queryString(queryString);
        SearchResult result = mallSearchService.search(searchParam);
        model.addAttribute("result", result);


        return "list";
    }
}
