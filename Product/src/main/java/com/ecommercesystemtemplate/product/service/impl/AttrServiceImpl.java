package com.ecommercesystemtemplate.product.service.impl;

import com.ecommercesystemtemplate.common.constant.ProductConstant;
import com.ecommercesystemtemplate.product.dao.AttrAttrgroupRelationDao;
import com.ecommercesystemtemplate.product.dao.AttrDao;
import com.ecommercesystemtemplate.product.dao.AttrGroupDao;
import com.ecommercesystemtemplate.product.dao.CategoryDao;
import com.ecommercesystemtemplate.product.entity.AttrAttrgroupRelationEntity;
import com.ecommercesystemtemplate.product.entity.AttrEntity;
import com.ecommercesystemtemplate.product.entity.AttrGroupEntity;
import com.ecommercesystemtemplate.product.entity.CategoryEntity;
import com.ecommercesystemtemplate.product.service.AttrService;
import com.ecommercesystemtemplate.product.service.CategoryService;
import com.ecommercesystemtemplate.product.vo.AttrGroupRelationVo;
import com.ecommercesystemtemplate.product.vo.AttrResponseVo;
import com.ecommercesystemtemplate.product.vo.AttrVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.common.utils.Query;
import org.springframework.transaction.annotation.Transactional;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    final
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;
    final AttrGroupDao attrGroupDao;
    final CategoryDao categoryDao;
    final CategoryService categoryService;

    public AttrServiceImpl(AttrAttrgroupRelationDao attrAttrgroupRelationDao, AttrGroupDao attrGroupDao, CategoryDao categoryDao, CategoryService categoryService) {
        this.attrAttrgroupRelationDao = attrAttrgroupRelationDao;
        this.attrGroupDao = attrGroupDao;
        this.categoryDao = categoryDao;
        this.categoryService = categoryService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        // 1. save basic data
        this.save(attrEntity);

        // 2. save relation data
        if (attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() && attr.getAttrGroupId() != null) {
            AttrAttrgroupRelationEntity entity = new AttrAttrgroupRelationEntity();
            entity.setAttrId(attrEntity.getAttrId());
            entity.setAttrGroupId(attr.getAttrGroupId());
            attrAttrgroupRelationDao.insert(entity);
        }

    }

    @Override
    public PageUtils queryBaseAttr(Map<String, Object> params, Long catelogId, String attrType) {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>().eq("attr_type", "base".equalsIgnoreCase(attrType) ?
                ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() : ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());

        // 1. query all attributes of the current category
        if (catelogId != 0) {
            wrapper.eq("catelog_id", catelogId);
        }
        // 2. query all attributes that are not associated with the attribute group
        String key = (String) params.get("key");
        if (StringUtils.isNotEmpty(key)) {
            wrapper.and((w) -> {
                w.eq("attr_id", key).or().like("attr_name", key);
            });
        }

        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),wrapper
        );

        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();
        List<AttrResponseVo> result = records.stream().map((attrEntity) -> {
            AttrResponseVo vo = new AttrResponseVo();
            BeanUtils.copyProperties(attrEntity, vo);
            // 2.1 set group name and category name
            if ("base".equalsIgnoreCase(attrType)) {
                AttrAttrgroupRelationEntity relationEntity =
                        attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().
                                eq("attr_id", attrEntity.getAttrId()));
                if (relationEntity != null && relationEntity.getAttrGroupId() != null) {
                    AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
                    vo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }

            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                vo.setCatelogName(categoryEntity.getName());
            }
            return vo;
        }).toList();

        pageUtils.setList(result);
        return pageUtils;
    }

    @Cacheable(value = "attr", key = "'attrInfo:'+#root.args[0]")
    @Override
    public AttrResponseVo getAttrInfo(Long attrId) {
        AttrEntity attrEntity = this.getById(attrId);
        AttrResponseVo result = new AttrResponseVo();
        BeanUtils.copyProperties(attrEntity, result);

        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            // 1. set group name and id
            AttrAttrgroupRelationEntity entity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().
                    eq("attr_id", attrId));
            if (entity != null) {
                result.setAttrGroupId(entity.getAttrGroupId());
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(entity.getAttrGroupId());
                if (attrGroupEntity != null) {
                    result.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
        }

        // 2. set category path
        Long[] catelogPath = categoryService.findCatelogPath(attrEntity.getCatelogId());
        result.setCatelogPath(catelogPath);
        CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
        if (categoryEntity != null) {
            result.setCatelogName(categoryEntity.getName());
        }
        return result;
    }

    @Override
    @Transactional
    public void updateAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        this.updateById(attrEntity);

        if (attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            // 1. update relation data
            AttrAttrgroupRelationEntity entity = new AttrAttrgroupRelationEntity();
            entity.setAttrGroupId(attr.getAttrGroupId());
            entity.setAttrId(attr.getAttrId());
            Integer count = attrAttrgroupRelationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().
                    eq("attr_id", attr.getAttrId()));
            if (count > 0) {
                attrAttrgroupRelationDao.update(entity, new QueryWrapper<AttrAttrgroupRelationEntity>().
                        eq("attr_id", attr.getAttrId()));
            } else {
                attrAttrgroupRelationDao.insert(entity);
            }
        }
    }

    /**
     * according to attrGroupId, query all attributes
     */
    @Override
    public List<AttrEntity> queryRelationAttr(Long attrGroupId) {
        List<AttrAttrgroupRelationEntity> list = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().
                eq("attr_group_id", attrGroupId));
        List<Long> attrIds = list.stream().map(AttrAttrgroupRelationEntity::getAttrId).toList();

        if (attrIds.isEmpty()) {
            return null;
        }
        Collection<AttrEntity> attrEntities = this.listByIds(attrIds);
        return (List<AttrEntity>) attrEntities;
    }

    @Override
    public void deleteRelation(AttrGroupRelationVo[] vos) {
        List<AttrAttrgroupRelationEntity> collect = Arrays.stream(vos).map((vo) -> {
            AttrAttrgroupRelationEntity entity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(vo, entity);
            return entity;
        }).collect(Collectors.toList());
        attrAttrgroupRelationDao.deleteBatchRelation(collect);
    }

    @Override
    public PageUtils queryNotRelationAttr(Long attrGroupId, Map<String, Object> params) {
        // 1. current category only associated with itself group attributes
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupId);
        Long catelogId = attrGroupEntity.getCatelogId();

        // 2. current category only associated with other group attributes that are not associated with the current group
        // 2.1 get current category other group
        List<AttrGroupEntity> attrGroupEntities = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().
                eq("catelog_id", catelogId));
        List<Long> list = attrGroupEntities.stream().map(AttrGroupEntity::getAttrGroupId).toList();

        // 2.2 get these groups attributes
        List<AttrAttrgroupRelationEntity> ids = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().
                in("attr_group_id", list));
        List<Long> list1 = ids.stream().map(AttrAttrgroupRelationEntity::getAttrId).toList();
        // 2.3 remove these attributes from the current group
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>().
                eq("catelog_id", catelogId).eq("attr_type", ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        if (!list1.isEmpty()) {
            wrapper.notIn("attr_id", list1);
        }
        if (StringUtils.isNotEmpty((String) params.get("key"))) {
            wrapper.and((w) -> {
                w.eq("attr_id", params.get("key")).or().like("attr_name", params.get("key"));
            });
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper);
        PageUtils pageUtils = new PageUtils(page);
        return pageUtils;
    }

    @Override
    public List<Long> selectSearchAttrs(List<Long> attrIds) {

        return baseMapper.selectSearchAttrs(attrIds);
    }

}
