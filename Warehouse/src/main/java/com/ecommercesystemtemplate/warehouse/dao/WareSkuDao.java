package com.ecommercesystemtemplate.warehouse.dao;

import com.ecommercesystemtemplate.warehouse.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Commodity stocks
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-21 20:01:18
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    void addStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("skuNum") Integer skuNum);

    Long getSkuHasStock(@Param("skuId") Long skuId);

    List<Long> listWareIdHasSku(@Param("skuId") Long skuId);

    Long lockStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("num") Integer num);

    void unlockStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("skuNum") Integer skuNum, @Param("id") Long id);
}
