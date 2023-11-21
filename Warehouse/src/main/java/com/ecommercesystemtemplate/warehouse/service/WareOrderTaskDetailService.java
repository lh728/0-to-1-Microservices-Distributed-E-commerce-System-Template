package com.ecommercesystemtemplate.warehouse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.warehouse.entity.WareOrderTaskDetailEntity;

import java.util.Map;

/**
 * Inventory work order detail
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-21 20:01:18
 */
public interface WareOrderTaskDetailService extends IService<WareOrderTaskDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

