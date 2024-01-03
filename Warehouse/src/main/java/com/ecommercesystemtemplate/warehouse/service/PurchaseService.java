package com.ecommercesystemtemplate.warehouse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.warehouse.entity.PurchaseEntity;
import com.ecommercesystemtemplate.warehouse.vo.MergeVo;
import com.ecommercesystemtemplate.warehouse.vo.PurchaseCompletedVo;

import java.util.List;
import java.util.Map;

/**
 * Purchasing Information
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-21 20:01:18
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageUnreceive(Map<String, Object> params);

    void mergePurchaseOrder(MergeVo mergeVo);

    void received(List<Long> ids);

    void completed(PurchaseCompletedVo purchaseCompletedVo);
}

