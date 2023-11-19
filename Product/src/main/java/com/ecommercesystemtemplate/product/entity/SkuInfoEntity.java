package com.ecommercesystemtemplate.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * sku information
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-18 12:20:53
 */
@Data
@TableName("pms_sku_info")
public class SkuInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * skuId
	 */
	@TableId
	private Long skuId;
	/**
	 * spuId
	 */
	private Long spuId;
	/**
	 * sku_name
	 */
	private String skuName;
	/**
	 * sku_desc
	 */
	private String skuDesc;
	/**
	 * catalog_id
	 */
	private Long catalogId;
	/**
	 * brand_id
	 */
	private Long brandId;
	/**
	 * sku_default_img
	 */
	private String skuDefaultImg;
	/**
	 * sku_title
	 */
	private String skuTitle;
	/**
	 * sku_subtitle
	 */
	private String skuSubtitle;
	/**
	 * price
	 */
	private BigDecimal price;
	/**
	 * sale_count
	 */
	private Long saleCount;

}
