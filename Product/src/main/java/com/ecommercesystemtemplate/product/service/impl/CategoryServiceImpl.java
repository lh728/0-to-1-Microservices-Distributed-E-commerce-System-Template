package com.ecommercesystemtemplate.product.service.impl;

import com.ecommercesystemtemplate.product.dao.CategoryDao;
import com.ecommercesystemtemplate.product.entity.CategoryEntity;
import com.ecommercesystemtemplate.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.common.utils.Query;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        // 1. find all classification
        List<CategoryEntity> entities = baseMapper.selectList(null);

        // 2. assemble to tree (parent-son)
        // 2.1 find all first-level category
        List<CategoryEntity> firstLevel = entities.stream().filter((item) ->
             item.getParentCid() == 0L
        ).map((cat) -> {
            cat.setChildren(getChildren(cat,entities));
            return cat;
        }).sorted(Comparator.comparingInt(CategoryEntity::getSort)).collect(Collectors.toList());
        return firstLevel;
    }

    /**
     * recursion get each categories' children category
     * @return
     */
    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all){
        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid() == root.getCatId();
        }).map(category -> {
            // find children category
            category.setChildren(getChildren(category,all));
            return category;
        }).sorted(Comparator.comparingInt(cat -> (cat.getSort() == null ? 0 : cat.getSort()))).collect(Collectors.toList());
        return children;
    }

}