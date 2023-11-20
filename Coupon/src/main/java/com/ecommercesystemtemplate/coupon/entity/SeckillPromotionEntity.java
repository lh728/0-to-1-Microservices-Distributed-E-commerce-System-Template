package com.ecommercesystemtemplate.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * flash sale activity
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-20 20:32:33
 */
@Data
@TableName("sms_seckill_promotion")
public class SeckillPromotionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * title
	 */
	private String title;
	/**
	 * start_time
	 */
	private Date startTime;
	/**
	 * end_time
	 */
	private Date endTime;
	/**
	 * status
	 */
	private Integer status;
	/**
	 * create_time
	 */
	private Date createTime;
	/**
	 * user_id
	 */
	private Long userId;

}
