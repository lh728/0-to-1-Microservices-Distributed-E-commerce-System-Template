package com.ecommercesystemtemplate.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import com.ecommercesystemtemplate.common.to.SkuReductionTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ecommercesystemtemplate.coupon.entity.SkuFullReductionEntity;
import com.ecommercesystemtemplate.coupon.service.SkuFullReductionService;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.common.utils.R;



/**
 * Product discount information
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-20 20:32:33
 */
@RestController
@RequestMapping("coupon/skufullreduction")
public class SkuFullReductionController {
    private final SkuFullReductionService skuFullReductionService;

    public SkuFullReductionController(SkuFullReductionService skuFullReductionService) {
        this.skuFullReductionService = skuFullReductionService;
    }

    /**
     * list
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = skuFullReductionService.queryPage(params);

        return R.ok().put("page", page);
    }

    @PostMapping("/saveInfo")
    public R saveInfo(@RequestBody SkuReductionTo skuReductionTo){
        skuFullReductionService.saveSkuReduction(skuReductionTo);

        return R.ok();
    }


    /**
     * info
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		SkuFullReductionEntity skuFullReduction = skuFullReductionService.getById(id);

        return R.ok().put("skuFullReduction", skuFullReduction);
    }

    /**
     * save
     */
    @RequestMapping("/save")
    public R save(@RequestBody SkuFullReductionEntity skuFullReduction){
		skuFullReductionService.save(skuFullReduction);

        return R.ok();
    }

    /**
     * update
     */
    @RequestMapping("/update")
    public R update(@RequestBody SkuFullReductionEntity skuFullReduction){
		skuFullReductionService.updateById(skuFullReduction);

        return R.ok();
    }

    /**
     * delete
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		skuFullReductionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
