package com.ecommercesystemtemplate.product.dao;

import com.ecommercesystemtemplate.product.entity.SpuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * spu information
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-18 12:20:52
 */
@Mapper
public interface SpuInfoDao extends BaseMapper<SpuInfoEntity> {

    void updateSpuStatus(@Param("spuId") Long spuId, @Param("code") int code);
}
