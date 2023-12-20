package com.ecommercesystemtemplate.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ecommercesystemtemplate.product.entity.CategoryEntity;
import com.ecommercesystemtemplate.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.common.utils.R;



/**
 * Three-level classification of commodities
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-18 14:12:59
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
    Retrieve all categories and subcategories, assembling them into a tree data structure.
     **/
    @RequestMapping("/list/tree")
    public R list(){
        List<CategoryEntity> entities = categoryService.listWithTree();
        return R.ok().put("page", entities);
    }

    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryService.queryPage(params);
        return R.ok().put("page", page);
    }

    @RequestMapping("/info/{catId}")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);
        return R.ok().put("category", category);
    }

    @RequestMapping("/save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);
        return R.ok();
    }

    @RequestMapping("/update")
    public R update(@RequestBody CategoryEntity category){
		categoryService.updateById(category);
        return R.ok();
    }

    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] catIds){
        // 1. check current menu whether is used by other modules
        categoryService.removeMenuByIds(Arrays.asList(catIds));
        return R.ok();
    }

}
