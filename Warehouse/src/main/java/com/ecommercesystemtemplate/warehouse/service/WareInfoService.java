package com.ecommercesystemtemplate.warehouse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.warehouse.entity.WareInfoEntity;
import com.ecommercesystemtemplate.warehouse.vo.FreightVo;

import java.util.Map;

/**
 * Warehouse information
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-21 20:01:18
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * calculate the freight according to the address
     *
     * @param addrId
     * @return
     */
    FreightVo getFreight(Long addrId);
}

