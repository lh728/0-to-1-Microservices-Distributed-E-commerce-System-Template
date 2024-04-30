package com.ecommercesystemtemplate.product.app;

import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.common.utils.R;
import com.ecommercesystemtemplate.product.entity.SkuSaleAttrValueEntity;
import com.ecommercesystemtemplate.product.service.SkuSaleAttrValueService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;



/**
 * sku sales attributes & values
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-18 14:12:59
 */
@RestController
@RequestMapping("product/skusaleattrvalue")
public class SkuSaleAttrValueController {
    private final SkuSaleAttrValueService skuSaleAttrValueService;

    public SkuSaleAttrValueController(SkuSaleAttrValueService skuSaleAttrValueService) {
        this.skuSaleAttrValueService = skuSaleAttrValueService;
    }

    @GetMapping("/stringList/{skuId}")
    public List<String> getSkuSaleAttrValues(@PathVariable("skuId") Long skuId){
        List<String> strings = skuSaleAttrValueService.getSkuSaleAttrValuesAsString(skuId);
        return strings;
    }

    /**
     * list
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = skuSaleAttrValueService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * info
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		SkuSaleAttrValueEntity skuSaleAttrValue = skuSaleAttrValueService.getById(id);

        return R.ok().put("skuSaleAttrValue", skuSaleAttrValue);
    }

    /**
     * save
     */
    @RequestMapping("/save")
    public R save(@RequestBody SkuSaleAttrValueEntity skuSaleAttrValue){
		skuSaleAttrValueService.save(skuSaleAttrValue);

        return R.ok();
    }

    /**
     * update
     */
    @RequestMapping("/update")
    public R update(@RequestBody SkuSaleAttrValueEntity skuSaleAttrValue){
		skuSaleAttrValueService.updateById(skuSaleAttrValue);

        return R.ok();
    }

    /**
     * delete
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		skuSaleAttrValueService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
