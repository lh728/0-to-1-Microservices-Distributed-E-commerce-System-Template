package com.ecommercesystemtemplate.warehouse.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Inventory work order
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-21 20:01:18
 */
@Data
@TableName("wms_ware_order_task")
public class WareOrderTaskEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * order_id
	 */
	private Long orderId;
	/**
	 * order_sn
	 */
	private String orderSn;
	/**
	 * consignee
	 */
	private String consignee;
	/**
	 * consignee_tel
	 */
	private String consigneeTel;
	/**
	 * delivery_address
	 */
	private String deliveryAddress;
	/**
	 * order_comment
	 */
	private String orderComment;
	/**
	 * Payment method [1: Online payment 2: Cash on delivery]
	 */
	private Integer paymentWay;
	/**
	 * task_status
	 */
	private Integer taskStatus;
	/**
	 * order_desc
	 */
	private String orderBody;
	/**
	 * tracking_number
	 */
	private String trackingNo;
	/**
	 * create_time
	 */
	private Date createTime;
	/**
	 * ware_id
	 */
	private Long wareId;
	/**
	 * task_comment
	 */
	private String taskComment;

}
