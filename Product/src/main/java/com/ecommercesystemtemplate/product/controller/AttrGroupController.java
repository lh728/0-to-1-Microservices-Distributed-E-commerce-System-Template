package com.ecommercesystemtemplate.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ecommercesystemtemplate.product.entity.AttrEntity;
import com.ecommercesystemtemplate.product.entity.AttrGroupEntity;
import com.ecommercesystemtemplate.product.service.AttrAttrgroupRelationService;
import com.ecommercesystemtemplate.product.service.AttrGroupService;
import com.ecommercesystemtemplate.product.service.AttrService;
import com.ecommercesystemtemplate.product.service.CategoryService;
import com.ecommercesystemtemplate.product.vo.AttrGroupRelationVo;
import com.ecommercesystemtemplate.product.vo.AttrGroupWithAttrsVo;
import org.springframework.web.bind.annotation.*;

import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.common.utils.R;



/**
 * Grouping attributes
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-18 14:12:59
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    private final AttrGroupService attrGroupService;

    private final CategoryService categoryService;

    private final AttrService attrService;

    private final AttrAttrgroupRelationService attrAttrgroupRelationService;

    public AttrGroupController(AttrGroupService attrGroupService, CategoryService categoryService, AttrService attrService, AttrAttrgroupRelationService attrAttrgroupRelationService) {
        this.attrGroupService = attrGroupService;
        this.categoryService = categoryService;
        this.attrService = attrService;
        this.attrAttrgroupRelationService = attrAttrgroupRelationService;
    }
    /**
     * add relation data
     */
    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRelationVo> vos){
        attrAttrgroupRelationService.saveBatchRelation(vos);
        return R.ok();
    }

    /**
     * get current category's attribute group and attribute
     */
    @GetMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("catelogId") Long catelogId){
        List<AttrGroupWithAttrsVo> result = attrGroupService.getAttrGroupWithAttrsByCatelogId(catelogId);
        return R.ok().put("page", result);
    }


    /**
     * list relation data
     */
    @GetMapping("/{attrGroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrGroupId") Long attrGroupId){
        List<AttrEntity> result = attrService.queryRelationAttr( attrGroupId);
        return R.ok().put("page", result);
    }

    /**
     * list other data that are not associated with the current group
     */
    @GetMapping("/{attrGroupId}/noattr/relation")
    public R attrNotRelation(@PathVariable("attrGroupId") Long attrGroupId,
                             @RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryNotRelationAttr( attrGroupId, params);
        return R.ok().put("page", page);
    }

    /**
     * delete relation basic attr data
     */
    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrGroupRelationVo[] vos){
        attrService.deleteRelation(vos);
        return R.ok();
    }

    /**
     * list
     */
    @RequestMapping("/list/{catelogId}")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("catelogId") Long catelogId){
        PageUtils page = attrGroupService.queryPage(params, catelogId);

        return R.ok().put("page", page);
    }


    /**
     * info
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);

        Long[] path = categoryService.findCatelogPath(attrGroup.getCatelogId());
        attrGroup.setCatelogPath(path);

        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * save
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * update
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * delete
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
