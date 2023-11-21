package com.ecommercesystemtemplate.warehouse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.warehouse.entity.WareOrderTaskEntity;

import java.util.Map;

/**
 * Inventory work order
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-21 20:01:18
 */
public interface WareOrderTaskService extends IService<WareOrderTaskEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

