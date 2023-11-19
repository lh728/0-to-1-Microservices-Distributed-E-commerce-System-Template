package com.ecommercesystemtemplate.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * spu attribute value
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-18 12:20:53
 */
@Data
@TableName("pms_product_attr_value")
public class ProductAttrValueEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * spu_id
	 */
	private Long spuId;
	/**
	 * attr_id
	 */
	private Long attrId;
	/**
	 * attr_name
	 */
	private String attrName;
	/**
	 * attr_value
	 */
	private String attrValue;
	/**
	 * attr_sort
	 */
	private Integer attrSort;
	/**
	 * quick_show【Whether to show it in the introduction 0- No 1- Yes】
	 */
	private Integer quickShow;

}
