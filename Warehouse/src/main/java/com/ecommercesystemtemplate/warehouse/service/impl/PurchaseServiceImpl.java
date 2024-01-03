package com.ecommercesystemtemplate.warehouse.service.impl;

import com.ecommercesystemtemplate.common.constant.WareHouseConstant;
import com.ecommercesystemtemplate.warehouse.entity.PurchaseDetailEntity;
import com.ecommercesystemtemplate.warehouse.service.PurchaseDetailService;
import com.ecommercesystemtemplate.warehouse.vo.MergeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.common.utils.Query;

import com.ecommercesystemtemplate.warehouse.dao.PurchaseDao;
import com.ecommercesystemtemplate.warehouse.entity.PurchaseEntity;
import com.ecommercesystemtemplate.warehouse.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {
    final
    PurchaseDetailService purchaseDetailService;

    public PurchaseServiceImpl(PurchaseDetailService purchaseDetailService) {
        this.purchaseDetailService = purchaseDetailService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceive(Map<String, Object> params) {

        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status", 0).or().eq("status", 1)
        );

        return new PageUtils(page);


    }

    @Override
    @Transactional
    public void mergePurchaseOrder(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        if (purchaseId == null) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(WareHouseConstant.PurchaseStatusEnum.CREATED.getCode());
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }
        // Make sure the purchase order status is 0 and 1 before merging it
        List<PurchaseDetailEntity> entities = purchaseDetailService.listDetailByPurchaseId(purchaseId);
        for (PurchaseDetailEntity entity : entities) {
            if (entity.getStatus() == WareHouseConstant.PurchaseDetailStatusEnum.CREATED.getCode() ||
                    entity.getStatus() == WareHouseConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode()) {
                throw new RuntimeException("The purchase order must be in the 0 or 1 status before merging");
            }
        }

        List<Long> items = mergeVo.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> list = items.stream().map((item) -> {
            PurchaseDetailEntity entity = new PurchaseDetailEntity();
            entity.setId(item);
            entity.setPurchaseId(finalPurchaseId);
            entity.setStatus(WareHouseConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
            return entity;
        }).toList();
        purchaseDetailService.updateBatchById(list);

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);

    }

    @Override
    @Transactional
    public void received(List<Long> ids) {
        // 1. determine the status of the purchase order is 0 or 1
        List<PurchaseEntity> list = ids.stream().map(this::getById).
                filter(item -> item.getStatus() == WareHouseConstant.PurchaseStatusEnum.CREATED.getCode() || item.getStatus() == WareHouseConstant.PurchaseStatusEnum.ASSIGNED.getCode()).
                peek(item -> {
                    item.setStatus(WareHouseConstant.PurchaseStatusEnum.RECEIVED.getCode());
                    item.setUpdateTime(new Date());
                }).toList();
        // 2. change the status of the purchase order
        this.updateBatchById(list);

        // 3. change the status of the purchase order detail
        list.forEach(item -> {
            List<PurchaseDetailEntity> entities = purchaseDetailService.listDetailByPurchaseId(item.getId());
            List<PurchaseDetailEntity> collect = entities.stream().peek(entity ->
                    entity.setStatus(WareHouseConstant.PurchaseDetailStatusEnum.PURCHASING.getCode())).toList();
            purchaseDetailService.updateBatchById(collect);
        });
    }

}
