package com.ecommercesystemtemplate.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.common.utils.Query;
import com.ecommercesystemtemplate.product.dao.SkuSaleAttrValueDao;
import com.ecommercesystemtemplate.product.entity.SkuSaleAttrValueEntity;
import com.ecommercesystemtemplate.product.service.SkuSaleAttrValueService;
import com.ecommercesystemtemplate.product.vo.SkuItemSaleAttrVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuItemSaleAttrVo> getSaleAttrsBySpuId(Long spuId) {

        List<SkuItemSaleAttrVo> saleAttrVos =  this.baseMapper.getSaleAttrsBySpuId(spuId);

        return saleAttrVos;

    }

    @Override
    public List<String> getSkuSaleAttrValuesAsString(Long skuId) {

        return this.baseMapper.getSkuSaleAttrValuesAsString(skuId);

    }

}
