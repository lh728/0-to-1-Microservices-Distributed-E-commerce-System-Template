package com.ecommercesystemtemplate.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.product.entity.BrandEntity;
import com.ecommercesystemtemplate.product.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * Brand classification association
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-18 12:20:52
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    /**
     * Query the association between the brand and the category
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * Save the association between the brand and the category
     * @param categoryBrandRelation
     */
    void saveDetail(CategoryBrandRelationEntity categoryBrandRelation);

    /**
     * Update the association between the brand and the category
     * @param brandId
     * @param name
     */
    void updateBrand(Long brandId, String name);

    /**
     * Update category information and related tables information
     * @param catId
     * @param name
     */
    void updateCategory(Long catId, String name);

    List<BrandEntity> getBrandsByCatId(Long catId);
}

