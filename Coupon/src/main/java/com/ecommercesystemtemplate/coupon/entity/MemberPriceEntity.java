package com.ecommercesystemtemplate.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Product membership price
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-20 20:32:33
 */
@Data
@TableName("sms_member_price")
public class MemberPriceEntity implements Serializable {
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
	 * member_level_id
	 */
	private Long memberLevelId;
	/**
	 * member_level_name
	 */
	private String memberLevelName;
	/**
	 * member_price
	 */
	private BigDecimal memberPrice;
	/**
	 * Can other discounts be stacked [0-cannot be stacked, 1-can be stacked]
	 */
	private Integer addOther;

}
