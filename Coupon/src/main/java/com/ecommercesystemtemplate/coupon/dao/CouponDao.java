package com.ecommercesystemtemplate.coupon.dao;

import com.ecommercesystemtemplate.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * Coupon information
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-20 20:32:33
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
