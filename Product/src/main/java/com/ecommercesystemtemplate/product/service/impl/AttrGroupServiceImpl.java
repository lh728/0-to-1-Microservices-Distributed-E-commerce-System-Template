package com.ecommercesystemtemplate.product.service.impl;

import com.ecommercesystemtemplate.product.dao.AttrGroupDao;
import com.ecommercesystemtemplate.product.entity.AttrEntity;
import com.ecommercesystemtemplate.product.entity.AttrGroupEntity;
import com.ecommercesystemtemplate.product.service.AttrGroupService;
import com.ecommercesystemtemplate.product.service.AttrService;
import com.ecommercesystemtemplate.product.vo.AttrGroupWithAttrsVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.common.utils.Query;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    final
    AttrService attrService;

    public AttrGroupServiceImpl(AttrService attrService) {
        this.attrService = attrService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        String key = (String) params.get("key");
        // select * from pms_attr_group where catelog_id = ? and (attr_group_id = ? or attr_group_name like %?%)
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(key)) {
            wrapper.and((obj) -> {
                obj.eq("attr_group_id", key).or().like("attr_group_name", key);
            });
        }

        IPage<AttrGroupEntity> page;
        if (catelogId == 0) {
            page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    wrapper
            );
        } else {
            wrapper.eq("catelog_id", catelogId);
            page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    wrapper
            );
        }
        return new PageUtils(page);
    }

    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {
        // 1. get all attribute groups
        List<AttrGroupEntity> list = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        // 2. get all attributes of each attribute group
        List<AttrGroupWithAttrsVo> result = list.stream().map((attrGroupEntity) -> {
            AttrGroupWithAttrsVo attrGroupWithAttrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(attrGroupEntity, attrGroupWithAttrsVo);
            List<AttrEntity> attrEntities = attrService.queryRelationAttr(attrGroupWithAttrsVo.getAttrGroupId());
            attrGroupWithAttrsVo.setAttrs(attrEntities);
            return attrGroupWithAttrsVo;
        }).toList();
        return result;

    }

}
