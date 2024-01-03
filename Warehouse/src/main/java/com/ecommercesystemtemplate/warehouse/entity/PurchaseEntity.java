package com.ecommercesystemtemplate.warehouse.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * Purchasing Information
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-21 20:01:18
 */
@Data
@TableName("wms_purchase")
public class PurchaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * assignee_id
	 */
	private Long assigneeId;
	/**
	 * assignee_name
	 */
	private String assigneeName;
	/**
	 * phone
	 */
	private String phone;
	/**
	 * status
	 */
	private Integer status;
	/**
	 * warehouse_id
	 */
	private Long wareId;
	/**
	 * amount
	 */
	private BigDecimal amount;
	/**
	 * create_time
	 */
	private Date createTime;
	/**
	 * update_time
	 */
	private Date updateTime;

}
