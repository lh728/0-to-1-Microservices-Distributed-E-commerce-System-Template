package com.ecommercesystemtemplate.product.service.impl;

import com.ecommercesystemtemplate.product.dao.AttrAttrgroupRelationDao;
import com.ecommercesystemtemplate.product.dao.AttrDao;
import com.ecommercesystemtemplate.product.entity.AttrAttrgroupRelationEntity;
import com.ecommercesystemtemplate.product.entity.AttrEntity;
import com.ecommercesystemtemplate.product.service.AttrService;
import com.ecommercesystemtemplate.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public AttrServiceImpl(AttrAttrgroupRelationDao attrAttrgroupRelationDao) {
        this.attrAttrgroupRelationDao = attrAttrgroupRelationDao;
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

}
