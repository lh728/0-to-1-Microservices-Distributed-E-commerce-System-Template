package com.ecommercesystemtemplate.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ecommercesystemtemplate.product.dao.CategoryDao;
import com.ecommercesystemtemplate.product.entity.CategoryEntity;
import com.ecommercesystemtemplate.product.service.CategoryBrandRelationService;
import com.ecommercesystemtemplate.product.service.CategoryService;
import com.ecommercesystemtemplate.product.vo.Catalog2Vo;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.common.utils.Query;
import org.springframework.transaction.annotation.Transactional;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    final CategoryBrandRelationService categoryBrandRelationService;
    final StringRedisTemplate redisTemplate;
    final RedissonClient redissonClient;

    public CategoryServiceImpl(CategoryBrandRelationService categoryBrandRelationService, StringRedisTemplate redisTemplate,
                               RedissonClient redissonClient) {
        this.categoryBrandRelationService = categoryBrandRelationService;
        this.redisTemplate = redisTemplate;
        this.redissonClient = redissonClient;
    }

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
                item.getParentCid() == 0
        ).map((cat) -> {
            cat.setChildren(getChildren(cat, entities));
            return cat;
        }).sorted(Comparator.comparingInt(CategoryEntity::getSort)).collect(Collectors.toList());
        return firstLevel;
    }

    /**
     * recursion get each categories' children category
     *
     * @return
     */
    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all) {
        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid().equals(root.getCatId());
        }).map(category -> {
            // find children category
            category.setChildren(getChildren(category, all));
            return category;
        }).sorted(Comparator.comparingInt(cat -> (cat.getSort() == null ? 0 : cat.getSort()))).collect(Collectors.toList());
        return children;
    }

    @Override
    public void removeMenuByIds(List<Long> list) {
        // TODO check if the category is used before deleting

        // logic delete
        baseMapper.deleteBatchIds(list);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);

        Collections.reverse(parentPath);

        return paths.toArray(new Long[parentPath.size()]);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"category"}, allEntries = true)
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());

    }

    @Override
    @Cacheable(value = {"category"}, key = "#root.methodName")
    public List<CategoryEntity> getAllFirstLevelCategories() {
        return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
    }

    @Override
    @Cacheable(value = {"category"}, key = "#root.methodName")
    public Map<String, List<Catalog2Vo>> getCategoryJson() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);

        // getAllFirstLevelCategories
        List<CategoryEntity> allFirstLevelCategories = getParentCid(categoryEntities,0L);
        Map<String, List<Catalog2Vo>> parentCid = allFirstLevelCategories.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            // get second level category
            List<CategoryEntity> entities = getParentCid(categoryEntities,v.getCatId());
            List<Catalog2Vo> list = null;
            if (entities != null) {
                list = entities.stream().map(item -> {
                    Catalog2Vo catalog2vo = new Catalog2Vo(v.getCatId().toString(), null, item.getName(), item.getCatId().toString());
                    // get third level category
                    List<CategoryEntity> level3 = getParentCid(categoryEntities,item.getCatId());
                    if (level3 != null) {
                        List<Catalog2Vo.Catalog3Vo> voList = level3.stream().map(level3item -> {
                            Catalog2Vo.Catalog3Vo catalog3vo = new Catalog2Vo.Catalog3Vo(item.getCatId().toString(),
                                    level3item.getCatId().toString(), level3item.getName());
                            return catalog3vo;
                        }).toList();
                        catalog2vo.setCatalog3List(voList);
                    }
                    return catalog2vo;
                }).toList();
            }
            return list;
        }));

        return parentCid;
    }

//    public Map<String, List<Catalog2Vo>> getCategoryJson2() {
//        // 1. add redis cache logic
//        String categoryJson = redisTemplate.opsForValue().get("categoryJson");
//        if (StringUtils.isEmpty(categoryJson)) {
//            // 2. if redis cache is empty, query from database
//            Map<String, List<Catalog2Vo>> categoryJsonFromDb = getCategoryJsonFromDbWithRedissonLock();
//            // 3. put data into redis
//            String jsonString = JSON.toJSONString(categoryJsonFromDb);
//            redisTemplate.opsForValue().set("categoryJson", jsonString, 1, TimeUnit.DAYS);
//            return categoryJsonFromDb;
//        }
//        // convert to defined object
//        Map<String, List<Catalog2Vo>> result = JSON.parseObject(categoryJson, new TypeReference<>() {
//        });
//        return result;
//    }

    public Map<String, List<Catalog2Vo>> getCategoryJsonFromDbWithRedissonLock() {
        // distributed lock
        RLock lock = redissonClient.getLock("CategoryJson-lock");
        lock.lock(10, TimeUnit.SECONDS);
        Map<String, List<Catalog2Vo>> categoryJsonFromDB;
        try{
            categoryJsonFromDB = getCategoryJsonFromDB();
        } finally {
            lock.unlock();
        }
        return categoryJsonFromDB;
    }

    private Map<String, List<Catalog2Vo>> getCategoryJsonFromDB() {
        // Concurrency, get lock and check if redis cache is empty
        String categoryJson = redisTemplate.opsForValue().get("categoryJson");
        if (StringUtils.isNotEmpty(categoryJson)) {
            return JSON.parseObject(categoryJson, new TypeReference<>() {
            });
        }

        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);

        // getAllFirstLevelCategories
        List<CategoryEntity> allFirstLevelCategories = getParentCid(categoryEntities, 0L);
        Map<String, List<Catalog2Vo>> parentCid = allFirstLevelCategories.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            // get second level category
            List<CategoryEntity> entities = getParentCid(categoryEntities, v.getCatId());
            List<Catalog2Vo> list = null;
            if (entities != null) {
                list = entities.stream().map(item -> {
                    Catalog2Vo catalog2vo = new Catalog2Vo(v.getCatId().toString(), null, item.getName(), item.getCatId().toString());
                    // get third level category
                    List<CategoryEntity> level3 = getParentCid(categoryEntities, item.getCatId());
                    if (level3 != null) {
                        List<Catalog2Vo.Catalog3Vo> voList = level3.stream().map(level3item -> {
                            Catalog2Vo.Catalog3Vo catalog3vo = new Catalog2Vo.Catalog3Vo(item.getCatId().toString(),
                                    level3item.getCatId().toString(), level3item.getName());
                            return catalog3vo;
                        }).toList();
                        catalog2vo.setCatalog3List(voList);
                    }
                    return catalog2vo;
                }).toList();
            }
            return list;
        }));

        // 3. put data into redis
        String jsonString = JSON.toJSONString(parentCid);
        redisTemplate.opsForValue().set("categoryJson", jsonString, 1, TimeUnit.DAYS);
        return parentCid;
    }

    private List<CategoryEntity> getParentCid(List<CategoryEntity> categoryEntities, Long parentCid) {
        List<CategoryEntity> list = categoryEntities.stream().filter(item -> {
            return item.getParentCid().equals(parentCid);
        }).toList();
        return list;
    }

    private List<Long> findParentPath(Long catelogId, List<Long> paths) {
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if (byId.getParentCid() != 0) {
            findParentPath(byId.getParentCid(), paths);
        }
        return paths;
    }


}
