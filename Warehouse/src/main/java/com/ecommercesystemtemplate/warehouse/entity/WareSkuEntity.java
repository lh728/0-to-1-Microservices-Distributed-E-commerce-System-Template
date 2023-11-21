package com.ecommercesystemtemplate.warehouse.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Commodity stocks
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-21 20:01:18
 */
@Data
@TableName("wms_ware_sku")
public class WareSkuEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * sku_id
	 */
	private Long skuId;
	/**
	 * ware_id
	 */
	private Long wareId;
	/**
	 * stock
	 */
	private Integer stock;
	/**
	 * sku_name
	 */
	private String skuName;
	/**
	 * stock_locked
	 */
	private Integer stockLocked;

}
