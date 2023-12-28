package com.ecommercesystemtemplate.product.service.impl;

import com.ecommercesystemtemplate.product.dao.AttrAttrgroupRelationDao;
import com.ecommercesystemtemplate.product.entity.AttrAttrgroupRelationEntity;
import com.ecommercesystemtemplate.product.service.AttrAttrgroupRelationService;
import com.ecommercesystemtemplate.product.vo.AttrGroupRelationVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.common.utils.Query;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }
    @Override
    public void saveBatchRelation(List<AttrGroupRelationVo> vos) {
        List<AttrAttrgroupRelationEntity> list = vos.stream().map((vo) -> {
            AttrAttrgroupRelationEntity entity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(vo, entity);
            return entity;
        }).toList();
        this.saveBatch(list);
    }


}
