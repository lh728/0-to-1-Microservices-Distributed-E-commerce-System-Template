package com.ecommercesystemtemplate.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.product.entity.SpuInfoDescEntity;
import com.ecommercesystemtemplate.product.entity.SpuInfoEntity;
import com.ecommercesystemtemplate.product.vo.SpuSaveVo;

import java.util.Map;

/**
 * spu information
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-18 12:20:52
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVo spuInfo);

    void saveBaseSpuInfo(SpuInfoEntity spuInfo);

    PageUtils queryPageByCondition(Map<String, Object> params);
}

