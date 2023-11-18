package com.ecommercesystemtemplate.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Grouping attributes
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-18 12:20:53
 */
@Data
@TableName("pms_attr_group")
public class AttrGroupEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * attr_group_id
	 */
	@TableId
	private Long attrGroupId;
	/**
	 * attr_group_name
	 */
	private String attrGroupName;
	/**
	 * sort
	 */
	private Integer sort;
	/**
	 * descript
	 */
	private String descript;
	/**
	 * group icon
	 */
	private String icon;
	/**
	 * catelog_id
	 */
	private Long catelogId;

}
