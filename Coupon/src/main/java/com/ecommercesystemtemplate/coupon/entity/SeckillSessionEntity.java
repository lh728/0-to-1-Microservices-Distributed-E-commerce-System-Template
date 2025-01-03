package com.ecommercesystemtemplate.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * flash sale events
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-20 20:32:33
 */
@Data
@TableName("sms_seckill_session")
public class SeckillSessionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * name
	 */
	private String name;
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
	 * seckill_sku_relation
	 */
	@TableField(exist = false)
	private List<SeckillSkuRelationEntity> seckillSkuRelationList;
}
