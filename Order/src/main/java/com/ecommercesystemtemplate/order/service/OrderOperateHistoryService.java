package com.ecommercesystemtemplate.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.order.entity.OrderOperateHistoryEntity;

import java.util.Map;

/**
 * Order operation history
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-21 19:53:27
 */
public interface OrderOperateHistoryService extends IService<OrderOperateHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

