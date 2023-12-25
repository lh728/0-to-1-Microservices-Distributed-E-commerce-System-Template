package com.ecommercesystemtemplate.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Three-level classification of commodities
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-18 12:20:53
 */
@Data
@TableName("pms_category")
public class CategoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * cat_id
	 */
	@TableId
	private Long catId;
	/**
	 * name
	 */
	private String name;
	/**
	 * parent_cid
	 */
	private Long parentCid;
	/**
	 * cat_level
	 */
	private Integer catLevel;
	/**
	 * show_status[0-no display；1-display]
	 */
	@TableLogic(value = "1", delval = "0")
	private Integer showStatus;
	/**
	 * sort
	 */
	private Integer sort;
	/**
	 * icon
	 */
	private String icon;
	/**
	 * product_unit
	 */
	private String productUnit;
	/**
	 * product_count
	 */
	private Integer productCount;
	/**
	 * children list
	 */
	@TableField(exist = false)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<CategoryEntity> children;

}
