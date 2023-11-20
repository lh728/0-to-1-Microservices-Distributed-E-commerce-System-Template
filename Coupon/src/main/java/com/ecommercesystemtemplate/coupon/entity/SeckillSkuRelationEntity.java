package com.ecommercesystemtemplate.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Flash sale product association
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-20 20:32:33
 */
@Data
@TableName("sms_seckill_sku_relation")
public class SeckillSkuRelationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * promotion_id
	 */
	private Long promotionId;
	/**
	 * promotion_session_id
	 */
	private Long promotionSessionId;
	/**
	 * sku_id
	 */
	private Long skuId;
	/**
	 * flash price
	 */
	private BigDecimal seckillPrice;
	/**
	 * flash count
	 */
	private BigDecimal seckillCount;
	/**
	 * flash limit
	 */
	private BigDecimal seckillLimit;
	/**
	 * flash sort
	 */
	private Integer seckillSort;

}
