package com.ecommercesystemtemplate.warehouse.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Purchasing detail
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-21 20:01:18
 */
@Data
@TableName("wms_purchase_detail")
public class PurchaseDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * purchase_id
	 */
	private Long purchaseId;
	/**
	 * sku_id
	 */
	private Long skuId;
	/**
	 * sku_amount
	 */
	private Integer skuNum;
	/**
	 * sku_price
	 */
	private BigDecimal skuPrice;
	/**
	 * ware_id
	 */
	private Long wareId;
	/**
	 * Status [0 New, 1 Already Assigned, 2 Procuring, 3 Completed, 4 Procurement Failed]
	 */
	private Integer status;

}
