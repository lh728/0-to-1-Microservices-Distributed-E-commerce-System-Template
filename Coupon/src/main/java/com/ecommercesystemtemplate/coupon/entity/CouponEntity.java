package com.ecommercesystemtemplate.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Coupon information
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-20 20:32:33
 */
@Data
@TableName("sms_coupon")
public class CouponEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * Coupon type [0->Full site coupon; 1->Member coupon; 2->Shopping coupon; 3->Registration coupon]
	 */
	private Integer couponType;
	/**
	 * coupon_img
	 */
	private String couponImg;
	/**
	 * coupon_name
	 */
	private String couponName;
	/**
	 * number
	 */
	private Integer num;
	/**
	 * amount
	 */
	private BigDecimal amount;
	/**
	 * per person limit
	 */
	private Integer perLimit;
	/**
	 * requirement
	 */
	private BigDecimal minPoint;
	/**
	 * start_time
	 */
	private Date startTime;
	/**
	 * end_time
	 */
	private Date endTime;
	/**
	 * Usage type [0->all; 1->specified category; 2->specified product]
	 */
	private Integer useType;
	/**
	 * note
	 */
	private String note;
	/**
	 * publish_count
	 */
	private Integer publishCount;
	/**
	 * use_count
	 */
	private Integer useCount;
	/**
	 * receive_count
	 */
	private Integer receiveCount;
	/**
	 * enable_start_time
	 */
	private Date enableStartTime;
	/**
	 * enable_end_time
	 */
	private Date enableEndTime;
	/**
	 * Promo Code
	 */
	private String code;
	/**
	 * Membership levels that can be get [0-> no level limit, others - corresponding levels]
	 */
	private Integer memberLevel;
	/**
	 * publish status [0-unpublished, 1-published]
	 */
	private Integer publish;

}
