package com.ecommercesystemtemplate.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Home page topic table [each topic links to a new page to display topic product information]
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-20 20:32:33
 */
@Data
@TableName("sms_home_subject")
public class HomeSubjectEntity implements Serializable {
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
	 * title
	 */
	private String title;
	/**
	 * sub_title
	 */
	private String subTitle;
	/**
	 * status
	 */
	private Integer status;
	/**
	 * url
	 */
	private String url;
	/**
	 * sort
	 */
	private Integer sort;
	/**
	 * img
	 */
	private String img;

}
