package com.ecommercesystemtemplate.product.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.ecommercesystemtemplate.product.entity.BrandEntity;
import com.ecommercesystemtemplate.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.common.utils.R;

import javax.validation.Valid;


/**
 * brand
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-18 14:12:59
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@Valid @RequestBody BrandEntity brand, BindingResult result){
        Map<String,String> map = new HashMap<>();
        if (result.hasErrors()) {
            result.getFieldErrors().forEach((item) -> {
                String message = item.getDefaultMessage();
                String filed = item.getField();
                map.put(filed,message);
            });
            return R.error(400,"Data validation failed").put("data",map);
        } else {
            brandService.save(brand);
            return R.ok();
        }
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody BrandEntity brand){
		brandService.updateById(brand);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
