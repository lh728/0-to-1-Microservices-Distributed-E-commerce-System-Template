package com.ecommercesystemtemplate.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Coupon history
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-20 20:32:33
 */
@Data
@TableName("sms_coupon_history")
public class CouponHistoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * coupon_id
	 */
	private Long couponId;
	/**
	 * member_id
	 */
	private Long memberId;
	/**
	 * member_nick_name
	 */
	private String memberNickName;
	/**
	 * Obtaining method [0->Backend gift; 1->Active collection]
	 */
	private Integer getType;
	/**
	 * create_time
	 */
	private Date createTime;
	/**
	 * Usage status [0->not used; 1->used; 2->expired]
	 */
	private Integer useType;
	/**
	 * use_time
	 */
	private Date useTime;
	/**
	 * order_id
	 */
	private Long orderId;
	/**
	 * order_sn
	 */
	private Long orderSn;

}
