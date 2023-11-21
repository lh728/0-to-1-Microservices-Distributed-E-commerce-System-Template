package com.ecommercesystemtemplate.order.dao;

import com.ecommercesystemtemplate.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * order
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-21 19:53:27
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
