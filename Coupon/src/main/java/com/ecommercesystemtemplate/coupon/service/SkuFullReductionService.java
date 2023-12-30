package com.ecommercesystemtemplate.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecommercesystemtemplate.common.to.SkuReductionTo;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * Product discount information
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-20 20:32:33
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuReduction(SkuReductionTo skuReductionTo);
}

