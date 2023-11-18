package com.ecommercesystemtemplate.coupon.dao;

import com.ecommercesystemtemplate.coupon.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * Three-level classification of commodities
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-18 12:20:53
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
