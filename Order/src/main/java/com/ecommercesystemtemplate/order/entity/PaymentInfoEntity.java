package com.ecommercesystemtemplate.order.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Payment information form
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-21 19:53:27
 */
@Data
@TableName("oms_payment_info")
public class PaymentInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * Order number (external business number)
	 */
	private String orderSn;
	/**
	 * order_id
	 */
	private Long orderId;
	/**
	 * alipay_trade_no
	 */
	private String alipayTradeNo;
	/**
	 * total_amount
	 */
	private BigDecimal totalAmount;
	/**
	 * subject
	 */
	private String subject;
	/**
	 * payment_status
	 */
	private String paymentStatus;
	/**
	 * create_time
	 */
	private Date createTime;
	/**
	 * confirm_time
	 */
	private Date confirmTime;
	/**
	 * callback_content
	 */
	private String callbackContent;
	/**
	 * callback_time
	 */
	private Date callbackTime;

}
