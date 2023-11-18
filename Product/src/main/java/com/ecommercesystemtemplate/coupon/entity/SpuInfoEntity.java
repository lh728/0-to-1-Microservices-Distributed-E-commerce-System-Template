package com.ecommercesystemtemplate.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * spu information
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-18 12:20:52
 */
@Data
@TableName("pms_spu_info")
public class SpuInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * spu_name
	 */
	private String spuName;
	/**
	 * spu_description
	 */
	private String spuDescription;
	/**
	 * catalog_id
	 */
	private Long catalogId;
	/**
	 * brand_id
	 */
	private Long brandId;
	/**
	 * 
	 */
	private BigDecimal weight;
	/**
	 * Listing status [0 - Down, 1 - Up]
	 */
	private Integer publishStatus;
	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 
	 */
	private Date updateTime;

}
