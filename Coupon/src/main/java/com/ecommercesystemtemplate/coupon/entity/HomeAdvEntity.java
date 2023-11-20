package com.ecommercesystemtemplate.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Home page carousel ads
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-20 20:32:33
 */
@Data
@TableName("sms_home_adv")
public class HomeAdvEntity implements Serializable {
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
	 * pic
	 */
	private String pic;
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
	 * click_count
	 */
	private Integer clickCount;
	/**
	 * url
	 */
	private String url;
	/**
	 * note
	 */
	private String note;
	/**
	 * sort
	 */
	private Integer sort;
	/**
	 * publisher_id
	 */
	private Long publisherId;
	/**
	 * auth_id
	 */
	private Long authId;

}
