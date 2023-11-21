package com.ecommercesystemtemplate.order.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Order configuration information
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-21 19:53:27
 */
@Data
@TableName("oms_order_setting")
public class OrderSettingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * Flash sale order timeout closing time (minutes)
	 */
	private Integer flashOrderOvertime;
	/**
	 * Normal order timeout (minutes)
	 */
	private Integer normalOrderOvertime;
	/**
	 * Automatically confirm receipt time after shipment (days)
	 */
	private Integer confirmOvertime;
	/**
	 * The transaction time is automatically completed and returns cannot be applied for (days)
	 */
	private Integer finishOvertime;
	/**
	 * Automatic good review time after order completion (days)
	 */
	private Integer commentOvertime;
	/**
	 * Membership level [0-no limit to membership level, common to all; other-corresponding other membership levels]
	 */
	private Integer memberLevel;

}
