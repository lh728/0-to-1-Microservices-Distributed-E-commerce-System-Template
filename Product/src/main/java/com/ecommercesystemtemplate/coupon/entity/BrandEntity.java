package com.ecommercesystemtemplate.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * brand
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-18 12:20:53
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * brand_id
	 */
	@TableId
	private Long brandId;
	/**
	 * name
	 */
	private String name;
	/**
	 * logo address
	 */
	private String logo;
	/**
	 * descript
	 */
	private String descript;
	/**
	 * show_status[0-no display；1-display]
	 */
	private Integer showStatus;
	/**
	 * Retrieving initials
	 */
	private String firstLetter;
	/**
	 * sort
	 */
	private Integer sort;

}
