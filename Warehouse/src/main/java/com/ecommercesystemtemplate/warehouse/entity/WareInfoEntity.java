package com.ecommercesystemtemplate.warehouse.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Warehouse information
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-21 20:01:18
 */
@Data
@TableName("wms_ware_info")
public class WareInfoEntity implements Serializable {
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
	 * address
	 */
	private String address;
	/**
	 * areacode
	 */
	private String areacode;

}
