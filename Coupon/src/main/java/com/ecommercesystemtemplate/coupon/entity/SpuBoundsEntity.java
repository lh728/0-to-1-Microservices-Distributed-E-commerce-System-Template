package com.ecommercesystemtemplate.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Product spu points setting
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-20 20:32:33
 */
@Data
@TableName("sms_spu_bounds")
public class SpuBoundsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 
	 */
	private Long spuId;
	/**
	 * growth points
	 */
	private BigDecimal growBounds;
	/**
	 * Shopping points
	 */
	private BigDecimal buyBounds;
	/**
	 * Discount effectiveness [1111 (four status bits, from right to left); 0 - no discount, whether growth points are given away; 1 - no discount, whether shopping points are given away; 2 - there is a discount, whether growth points are given away; 3 - yes Discount, whether shopping points are given away [status bit 0: not given, 1: given]]
	 */
	private Integer work;

}
