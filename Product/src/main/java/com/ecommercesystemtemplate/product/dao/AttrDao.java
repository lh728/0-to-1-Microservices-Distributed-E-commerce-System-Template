package com.ecommercesystemtemplate.product.dao;

import com.ecommercesystemtemplate.product.entity.AttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Product attributes
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-18 12:20:53
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {

    List<Long> selectSearchAttrs(@Param("attrIds") List<Long> attrIds);
}
