package com.ecommercesystemtemplate.order.dao;

import com.ecommercesystemtemplate.order.entity.PaymentInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * Payment information form
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-21 19:53:27
 */
@Mapper
public interface PaymentInfoDao extends BaseMapper<PaymentInfoEntity> {
	
}
