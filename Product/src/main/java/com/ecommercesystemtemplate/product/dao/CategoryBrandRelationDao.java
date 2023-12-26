package com.ecommercesystemtemplate.product.dao;

import com.ecommercesystemtemplate.product.entity.CategoryBrandRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Brand classification association
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-18 12:20:52
 */
@Mapper
public interface CategoryBrandRelationDao extends BaseMapper<CategoryBrandRelationEntity> {

    /**
     * Update the category information and the information in the related table
     * @param catId
     * @param name
     */
    void updateCategory(@Param("catId") Long catId,@Param("name") String name);
}
