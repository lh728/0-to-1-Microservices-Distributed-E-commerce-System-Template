package com.ecommercesystemtemplate.product.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ecommercesystemtemplate.product.entity.ProductAttrValueEntity;
import com.ecommercesystemtemplate.product.service.AttrService;
import com.ecommercesystemtemplate.product.service.ProductAttrValueService;
import com.ecommercesystemtemplate.product.vo.AttrResponseVo;
import com.ecommercesystemtemplate.product.vo.AttrVo;
import org.springframework.web.bind.annotation.*;

import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.common.utils.R;



/**
 * Product attributes
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-18 14:12:59
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    private final AttrService attrService;
    private final ProductAttrValueService productAttrValueService;

    public AttrController(AttrService attrService, ProductAttrValueService productAttrValueService) {
        this.attrService = attrService;
        this.productAttrValueService = productAttrValueService;
    }

    /**
     * listforspu
     */
    @RequestMapping("/base/listforspu/{spuId}")
    public R baseAttrListForSpu(@PathVariable("spuId") Long spuId){
        List<ProductAttrValueEntity> result = productAttrValueService.baseAttrListForSpu(spuId);

        return R.ok().put("page", result);
    }

    /**
     * base attr list
     */
    @RequestMapping("/{attrType}/list/{catelogId}")
    public R baseAttrList(@RequestParam Map<String, Object> params, @PathVariable Long catelogId, @PathVariable String attrType){
        PageUtils page = attrService.queryBaseAttr(params, catelogId, attrType);

        return R.ok().put("page", page);
    }

    /**
     * list
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * info
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){
		AttrResponseVo attr = attrService.getAttrInfo(attrId);

        return R.ok().put("attr", attr);
    }

    /**
     * save
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrVo attr){
		attrService.saveAttr(attr);

        return R.ok();
    }

    /**
     * update
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrVo attr){
		attrService.updateAttr(attr);

        return R.ok();
    }

    /**
     * update spu attr
     */
    @PostMapping("/update/{spuId}")
    public R updateSpuAttr(@PathVariable Long spuId, @RequestBody List<ProductAttrValueEntity> entities){
        productAttrValueService.updateSpuAttr(spuId,entities);

        return R.ok();
    }

    /**
     * delete
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
