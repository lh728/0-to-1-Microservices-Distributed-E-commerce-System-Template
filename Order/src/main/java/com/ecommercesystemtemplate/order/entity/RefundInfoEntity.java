package com.ecommercesystemtemplate.order.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Refund information
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-21 19:53:27
 */
@Data
@TableName("oms_refund_info")
public class RefundInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * order_return_id
	 */
	private Long orderReturnId;
	/**
	 * refund amount
	 */
	private BigDecimal refund;
	/**
	 * refund_sn
	 */
	private String refundSn;
	/**
	 * refund_status
	 */
	private Integer refundStatus;
	/**
	 * Refund channels [1-Alipay, 2-WeChat, 3-UnionPay, 4-Remittance]
	 */
	private Integer refundChannel;
	/**
	 * 
	 */
	private String refundContent;

}
