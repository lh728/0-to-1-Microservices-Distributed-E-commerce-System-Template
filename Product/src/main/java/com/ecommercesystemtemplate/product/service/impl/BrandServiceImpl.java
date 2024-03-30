package com.ecommercesystemtemplate.product.service.impl;

import com.ecommercesystemtemplate.product.dao.BrandDao;
import com.ecommercesystemtemplate.product.entity.BrandEntity;
import com.ecommercesystemtemplate.product.service.BrandService;
import com.ecommercesystemtemplate.product.service.CategoryBrandRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.common.utils.Query;
import org.springframework.transaction.annotation.Transactional;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    final
    CategoryBrandRelationService categoryBrandRelationService;

    public BrandServiceImpl(CategoryBrandRelationService categoryBrandRelationService) {
        this.categoryBrandRelationService = categoryBrandRelationService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        // get key
        String key = (String) params.get("key");
        QueryWrapper<BrandEntity> wrapper = new QueryWrapper<>();
        if (key != null && !"".equals(key)){
            wrapper.eq("brand_id",key).or().like("name",key);
        }

        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void updateDetail(BrandEntity brand) {
        this.updateById(brand);

        if (StringUtils.isNotEmpty(brand.getName())){
            // update other tables
            categoryBrandRelationService.updateBrand(brand.getBrandId(),brand.getName());

            // TODO: update other tables
        }
    }

    @Override
    public List<BrandEntity> getBrandsByIds(List<Long> brandIds) {

        List<BrandEntity> brandId = baseMapper.selectList(new QueryWrapper<BrandEntity>().in("brand_id", brandIds));

        return brandId;

    }

}
