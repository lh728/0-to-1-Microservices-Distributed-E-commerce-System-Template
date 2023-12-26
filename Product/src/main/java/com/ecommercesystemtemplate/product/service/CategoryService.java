package com.ecommercesystemtemplate.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * Three-level classification of commodities
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-18 12:20:53
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * Retrieve all categories and subcategories, assembling them into a tree data structure.
     **/
    List<CategoryEntity> listWithTree();

    /**
     * Delete multiple categories
     * @param list
     */
    void removeMenuByIds(List<Long> list);

    /**
     * Find the path of the category
     * [parentCid, ..., catelogId]
     * @param catelogId
     * @return
     */
    Long[] findCatelogPath(Long catelogId);

    /**
     * Update category information and its relationship table
     * @param category
     */
    void updateCascade(CategoryEntity category);
}

