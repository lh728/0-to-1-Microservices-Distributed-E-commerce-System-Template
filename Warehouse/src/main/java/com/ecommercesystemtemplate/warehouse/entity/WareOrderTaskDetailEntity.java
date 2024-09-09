package com.ecommercesystemtemplate.warehouse.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Inventory work order detail
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-21 20:01:18
 */
@Data
@TableName("wms_ware_order_task_detail")
@AllArgsConstructor
@NoArgsConstructor
public class WareOrderTaskDetailEntity implements Serializable {
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
	 * sku_name
	 */
	private String skuName;
	/**
	 * sku_num
	 */
	private Integer skuNum;
	/**
	 * task_id
	 */
	private Long taskId;
	/**
	 * ware_id
	 */
	private Long wareId;
	/**
	 * lock_status
	 */
	private Integer lockStatus;


}
