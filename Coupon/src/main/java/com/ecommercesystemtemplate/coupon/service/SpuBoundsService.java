package com.ecommercesystemtemplate.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.coupon.entity.SpuBoundsEntity;

import java.util.Map;

/**
 * Product spu points setting
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-20 20:32:33
 */
public interface SpuBoundsService extends IService<SpuBoundsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

