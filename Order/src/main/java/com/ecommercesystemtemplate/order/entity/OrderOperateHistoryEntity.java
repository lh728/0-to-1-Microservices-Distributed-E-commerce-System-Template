package com.ecommercesystemtemplate.order.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Order operation history
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-21 19:53:27
 */
@Data
@TableName("oms_order_operate_history")
public class OrderOperateHistoryEntity implements Serializable {
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
	 * Operator [user; system; background administrator]
	 */
	private String operateMan;
	/**
	 * Operating time
	 */
	private Date createTime;
	/**
	 * Order status [0->pending payment; 1->pending shipment; 2->shipped; 3->completed; 4->closed; 5->invalid order]
	 */
	private Integer orderStatus;
	/**
	 * note
	 */
	private String note;

}
