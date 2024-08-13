package com.ecommercesystemtemplate.product.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecommercesystemtemplate.common.constant.ProductConstant;
import com.ecommercesystemtemplate.common.to.SkuReductionTo;
import com.ecommercesystemtemplate.common.to.SpuBondTo;
import com.ecommercesystemtemplate.common.to.es.SkuEsModel;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.common.utils.Query;
import com.ecommercesystemtemplate.common.utils.R;
import com.ecommercesystemtemplate.product.dao.SpuInfoDao;
import com.ecommercesystemtemplate.product.entity.*;
import com.ecommercesystemtemplate.product.feign.CouponFeignService;
import com.ecommercesystemtemplate.product.feign.SearchFeignService;
import com.ecommercesystemtemplate.product.feign.WareFeignService;
import com.ecommercesystemtemplate.product.service.*;
import com.ecommercesystemtemplate.product.vo.*;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author lhjls
 */
@Service("spuInfoService")
@AllArgsConstructor
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    private final SpuInfoDescService spuInfoDescService;
    private final SpuImagesService spuImagesService;
    private final AttrService attrService;
    private final ProductAttrValueService productAttrValueService;
    private final SkuInfoService skuInfoService;
    private final SkuImagesService skuImagesService;
    private final SkuSaleAttrValueService skuSaleAttrValueService;
    private final CouponFeignService couponFeignService;
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final WareFeignService wareFeignService;
    private final SearchFeignService searchFeignService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveSpuInfo(SpuSaveVo spuInfo) {
        // 1. save spu basic information pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuInfo, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(spuInfoEntity);

        // 2. save spu description pms_spu_info_desc
        List<String> decript = spuInfo.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(",", decript));
        spuInfoDescService.saveSpuInfoDesc(spuInfoDescEntity);

        // 3. save spu images pms_spu_images
        spuImagesService.saveImages(spuInfoEntity.getId(), spuInfo.getImages());

        // 4. save spu attribute pms_product_attr_value
        List<BaseAttrs> baseAttrs = spuInfo.getBaseAttrs();
        List<ProductAttrValueEntity> list = baseAttrs.stream().map((baseAttr) -> {
            ProductAttrValueEntity entity = new ProductAttrValueEntity();
            entity.setAttrId(baseAttr.getAttrId());
            AttrEntity id = attrService.getById(baseAttr.getAttrId());
            entity.setAttrName(id.getAttrName());
            entity.setAttrValue(baseAttr.getAttrValues());
            entity.setQuickShow(baseAttr.getShowDesc());
            entity.setSpuId(spuInfoEntity.getId());
            return entity;
        }).toList();
        productAttrValueService.saveProductAttr(list);

        // 5. openfeign save spu bounds information sms_spu_bounds
        Bounds bounds = spuInfo.getBounds();
        SpuBondTo spuBondTo = new SpuBondTo();
        BeanUtils.copyProperties(bounds, spuBondTo);
        spuBondTo.setSpuId(spuInfoEntity.getId());
        R r = couponFeignService.saveSpuBounds(spuBondTo);
        if (r.getCode() != 0) {
            log.error("Failed to remote save spu bounds information");
        }

        // 6. save spu corresponding sku information
        List<Skus> skus = spuInfo.getSkus();
        if (skus != null && !skus.isEmpty()) {
            skus.forEach((sku) -> {
                // 6.1 save basic sku information pms_sku_info
                String defaultImg = "";
                for (Images image : sku.getImages()) {
                    if (image.getDefaultImg() == 1) {
                        defaultImg = image.getImgUrl();
                    }
                }
                SkuInfoEntity entity = new SkuInfoEntity();
                BeanUtils.copyProperties(sku, entity);
                entity.setBrandId(spuInfoEntity.getBrandId());
                entity.setCatalogId(spuInfoEntity.getCatalogId());
                entity.setSaleCount(0L);
                entity.setSpuId(spuInfoEntity.getId());
                entity.setSkuDefaultImg(defaultImg);
                skuInfoService.saveSkuInfo(entity);

                // 6.2 save sku image information pms_sku_images
                Long skuId = entity.getSkuId();
                List<SkuImagesEntity> entities = sku.getImages().stream().map((image) -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setDefaultImg(image.getDefaultImg());
                    skuImagesEntity.setImgUrl(image.getImgUrl());
                    return skuImagesEntity;
                }).filter(item -> !StringUtils.isEmpty(item.getImgUrl())).toList();
                skuImagesService.saveBatch(entities);

                // 6.3 save sku sale attribute information pms_sku_sale_attr_value
                List<Attr> attrs = sku.getAttr();
                List<SkuSaleAttrValueEntity> collect = attrs.stream().map((attr) -> {
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(attr, skuSaleAttrValueEntity);
                    skuSaleAttrValueEntity.setSkuId(skuId);
                    return skuSaleAttrValueEntity;
                }).toList();
                skuSaleAttrValueService.saveBatch(collect);

                // 6.4 openfeign save sku full reduction information sms_sku_ladder sms_sku_full_reduction sms_member_price
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(sku, skuReductionTo);
                skuReductionTo.setMemberPrice(sku.getMemberPrice());
                skuReductionTo.setSkuId(skuId);
                if (skuReductionTo.getFullCount() > 0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal("0")) > 0) {
                    R r1 = couponFeignService.saveSkuReduction(skuReductionTo);
                    if (r1.getCode() != 0) {
                        log.error("Failed to remote save sku discount information");
                    }
                }

            });
        }
    }

    @Override
    public void saveBaseSpuInfo(SpuInfoEntity spuInfo) {
        this.baseMapper.insert(spuInfo);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and((w) -> {
                w.eq("id", key).or().like("spu_name", key);
            });
        }
        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)) {
            wrapper.eq("publish_status", status);
        }
        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId)) {
            wrapper.eq("brand_id", brandId);
        }
        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId)) {
            wrapper.eq("catalog_id", catelogId);
        }

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }

    @Override
    public void up(Long spuId) {
        // build data
        // get basic information of spu
        List<SkuInfoEntity> skus = skuInfoService.getSkusBySpuId(spuId);
        List<Long> ids = skus.stream().map(SkuInfoEntity::getSkuId).toList();

        // get all attribute information of spu
        List<ProductAttrValueEntity> entities = productAttrValueService.baseAttrListForSpu(spuId);
        List<Long> attrIds = entities.stream().map(ProductAttrValueEntity::getAttrId).toList();

        List<Long> searchAttrs = attrService.selectSearchAttrs(attrIds);

        Set<Long> idSet = new HashSet<>(searchAttrs);
        List<SkuEsModel.Attrs> attrsList = entities.stream().filter(item -> {
            return idSet.contains(item.getAttrId());
        }).map(item -> {
            SkuEsModel.Attrs attrs1 = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(item, attrs1);
            return attrs1;
        }).toList();

        // remote feign if there is stock
        Map<Long, Boolean> stockMap = null;
        try {
            R r = wareFeignService.getSkusHaveStock(ids);
            TypeReference<List<SkuHasStockVo>> typeReference = new TypeReference<>() {
            };
            List<SkuHasStockVo> data = r.getData(typeReference);
            stockMap = data.stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, SkuHasStockVo::getHasStock));
        }catch (Exception e){
            log.error("Failed to remote get sku stock information, reason: {}", e);
        }

        Map<Long, Boolean> finalStockMap = stockMap;
        List<SkuEsModel> list = skus.stream().map(sku -> {
            SkuEsModel skuEsModel = new SkuEsModel();
            BeanUtils.copyProperties(sku, skuEsModel);
            skuEsModel.setSkuImg(sku.getSkuDefaultImg());
            skuEsModel.setSkuPrice(sku.getPrice());
            // if there is stock
            if (finalStockMap == null) {
                skuEsModel.setHasStock(true);
            } else {
                skuEsModel.setHasStock(finalStockMap.get(sku.getSkuId()));
            }
            // set hot score
            skuEsModel.setHotScore(0L);

            // brand and category information
            BrandEntity brandEntity = brandService.getById(sku.getBrandId());
            skuEsModel.setBrandName(brandEntity.getName());
            skuEsModel.setBrandImg(brandEntity.getLogo());
            CategoryEntity entity = categoryService.getById(sku.getCatalogId());
            skuEsModel.setCatalogName(entity.getName());

            // set search attribute information
            skuEsModel.setAttrs(attrsList);

            return skuEsModel;
        }).toList();
        // save to es
        R r = searchFeignService.productToList(list);
        if (r.getCode() == 0) {
            // change spu status
            this.baseMapper.updateSpuStatus(spuId, ProductConstant.StatusEnum.SPU_UP.getCode());
        } else {
            // fail
            // TODO retry later
        }

    }

    @Override
    public SpuInfoEntity getSpuInfoBySkuId(Long skuId) {
        SkuInfoEntity byId = skuInfoService.getById(skuId);
        Long spuId = byId.getSpuId();
        SpuInfoEntity entity = getById(spuId);
        return entity;
    }

}
