package com.ecommercesystemtemplate.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
import com.ecommercesystemtemplate.product.vo.AttrResponseVo;
import com.ecommercesystemtemplate.product.vo.AttrVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
        AttrAttrgroupRelationEntity entity = new AttrAttrgroupRelationEntity();
        entity.setAttrId(attrEntity.getAttrId());
        entity.setAttrGroupId(attr.getAttrGroupId());
        attrAttrgroupRelationDao.insert(entity);
    }

    @Override
    public PageUtils queryBaseAttr(Map<String, Object> params, Long catelogId) {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();

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
            AttrAttrgroupRelationEntity relationEntity =
                    attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().
                            eq("attr_id", attrEntity.getAttrId()));
            if (relationEntity != null && relationEntity.getAttrGroupId() != null) {
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
                vo.setGroupName(attrGroupEntity.getAttrGroupName());
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

    @Override
    public AttrResponseVo getAttrInfo(Long attrId) {
        AttrEntity attrEntity = this.getById(attrId);
        AttrResponseVo result = new AttrResponseVo();
        BeanUtils.copyProperties(attrEntity, result);

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

        Integer count = attrAttrgroupRelationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().
                eq("attr_id", attr.getAttrId()));
        AttrAttrgroupRelationEntity entity = new AttrAttrgroupRelationEntity();
        entity.setAttrGroupId(attr.getAttrGroupId());
        entity.setAttrId(attr.getAttrId());
        if (count > 0) {
            // 1. update relation data
            attrAttrgroupRelationDao.update(entity, new QueryWrapper<AttrAttrgroupRelationEntity>().
                    eq("attr_id", attr.getAttrId()));
        } else {
            // 2. insert relation data
            attrAttrgroupRelationDao.insert(entity);
        }
    }

}
