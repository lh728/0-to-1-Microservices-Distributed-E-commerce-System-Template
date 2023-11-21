package com.ecommercesystemtemplate.order.dao;

import com.ecommercesystemtemplate.order.entity.OrderOperateHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * Order operation history
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-21 19:53:27
 */
@Mapper
public interface OrderOperateHistoryDao extends BaseMapper<OrderOperateHistoryEntity> {
	
}
