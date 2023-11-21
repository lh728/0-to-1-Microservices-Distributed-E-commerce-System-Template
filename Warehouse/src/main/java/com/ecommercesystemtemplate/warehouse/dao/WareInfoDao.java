package com.ecommercesystemtemplate.warehouse.dao;

import com.ecommercesystemtemplate.warehouse.entity.WareInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * Warehouse information
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-21 20:01:18
 */
@Mapper
public interface WareInfoDao extends BaseMapper<WareInfoEntity> {
	
}
