package com.ecommercesystemtemplate.order.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Order return request
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-21 19:53:27
 */
@Data
@TableName("oms_order_return_apply")
public class OrderReturnApplyEntity implements Serializable {
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
	 * Returned product id
	 */
	private Long skuId;
	/**
	 * order_sn
	 */
	private String orderSn;
	/**
	 * create_time
	 */
	private Date createTime;
	/**
	 * member_username
	 */
	private String memberUsername;
	/**
	 * return_amount
	 */
	private BigDecimal returnAmount;
	/**
	 * return_name
	 */
	private String returnName;
	/**
	 * return_phone
	 */
	private String returnPhone;
	/**
	 * Application status [0->pending; 1->returning; 2->completed; 3->rejected]
	 */
	private Integer status;
	/**
	 * processing time
	 */
	private Date handleTime;
	/**
	 * sku_img
	 */
	private String skuImg;
	/**
	 * sku_name
	 */
	private String skuName;
	/**
	 * sku_brand
	 */
	private String skuBrand;
	/**
	 * Product sales attributes (JSON)
	 */
	private String skuAttrsVals;
	/**
	 * sku_count
	 */
	private Integer skuCount;
	/**
	 * sku_price
	 */
	private BigDecimal skuPrice;
	/**
	 * sku_real_price
	 */
	private BigDecimal skuRealPrice;
	/**
	 * reason
	 */
	private String reason;
	/**
	 * description
	 */
	private String description;
	/**
	 * Voucher images, separated by commas
	 */
	private String descPics;
	/**
	 * handle_note
	 */
	private String handleNote;
	/**
	 * handle_man
	 */
	private String handleMan;
	/**
	 * receive_man
	 */
	private String receiveMan;
	/**
	 * receive_time
	 */
	private Date receiveTime;
	/**
	 * receive_note
	 */
	private String receiveNote;
	/**
	 * receive_phone
	 */
	private String receivePhone;
	/**
	 * company_address
	 */
	private String companyAddress;

}
