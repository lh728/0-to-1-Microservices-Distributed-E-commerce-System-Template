package com.ecommercesystemtemplate.warehouse.controller;

import com.ecommercesystemtemplate.common.exception.BizCodeEnume;
import com.ecommercesystemtemplate.common.exception.NoStockException;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.common.utils.R;
import com.ecommercesystemtemplate.warehouse.entity.WareSkuEntity;
import com.ecommercesystemtemplate.warehouse.service.WareSkuService;
import com.ecommercesystemtemplate.warehouse.vo.SkuHasStockVo;
import com.ecommercesystemtemplate.warehouse.vo.WareSkuLockVo;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;



/**
 * Commodity stocks
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-21 20:01:18
 */
@RestController
@RequestMapping("warehouse/waresku")
public class WareSkuController {
    private final WareSkuService wareSkuService;

    public WareSkuController(WareSkuService wareSkuService) {
        this.wareSkuService = wareSkuService;
    }

    @PostMapping("/lock/order")
    public R lockOrderStock(@RequestBody WareSkuLockVo vo){
        try {
            Boolean b = wareSkuService.orderLockStock(vo);
            return R.ok();
        } catch(NoStockException e){
            return R.error(BizCodeEnume.NO_STOCK_EXCEPTION.getCode(), BizCodeEnume.NO_STOCK_EXCEPTION.getMessage());
        }
    }


    /**
     * if there is stock
     */
    @PostMapping("/hasStock")
    public R getSkusHaveStock(@RequestBody List<Long> skuIds){
        List<SkuHasStockVo> vos = wareSkuService.getSkusHaveStock(skuIds);

        return R.ok().setData(vos);
    }

    /**
     * list
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareSkuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * info
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * save
     */
    @RequestMapping("/save")
    public R save(@RequestBody WareSkuEntity wareSku){
		wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * update
     */
    @RequestMapping("/update")
    public R update(@RequestBody WareSkuEntity wareSku){
		wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * delete
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
