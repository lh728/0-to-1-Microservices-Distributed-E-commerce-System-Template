package com.ecommercesystemtemplate.order.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * order detail
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-21 19:53:27
 */
@Data
@TableName("oms_order_item")
public class OrderItemEntity implements Serializable {
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
	 * spu_id
	 */
	private Long spuId;
	/**
	 * spu_name
	 */
	private String spuName;
	/**
	 * spu_pic
	 */
	private String spuPic;
	/**
	 * brand
	 */
	private String spuBrand;
	/**
	 * category_id
	 */
	private Long categoryId;
	/**
	 * sku_id
	 */
	private Long skuId;
	/**
	 * sku_name
	 */
	private String skuName;
	/**
	 * sku_pic
	 */
	private String skuPic;
	/**
	 * sku_price
	 */
	private BigDecimal skuPrice;
	/**
	 * sku_quantity
	 */
	private Integer skuQuantity;
	/**
	 * Product sales attribute combination (JSON)
	 */
	private String skuAttrsVals;
	/**
	 * promotion_amount
	 */
	private BigDecimal promotionAmount;
	/**
	 * coupon_amount
	 */
	private BigDecimal couponAmount;
	/**
	 * integration_amount
	 */
	private BigDecimal integrationAmount;
	/**
	 * real_amount
	 */
	private BigDecimal realAmount;
	/**
	 * gift_integration
	 */
	private Integer giftIntegration;
	/**
	 * gift_growth
	 */
	private Integer giftGrowth;

}
