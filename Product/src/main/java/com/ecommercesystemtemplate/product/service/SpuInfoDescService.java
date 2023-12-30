package com.ecommercesystemtemplate.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.product.entity.SpuInfoDescEntity;

import java.util.Map;

/**
 * spu information introduction
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-18 12:20:52
 */
public interface SpuInfoDescService extends IService<SpuInfoDescEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfoDesc(SpuInfoDescEntity spuInfoDescEntity);
}

