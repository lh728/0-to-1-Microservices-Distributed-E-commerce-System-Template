package com.ecommercesystemtemplate.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

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
	@NotBlank(message = "Brand name cannot be empty")
	private String name;
	/**
	 * logo address
	 */
	@NotEmpty(message = "Brand logo address cannot be empty")
	@URL(message = "Brand logo address must be a valid URL")
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
	@NotEmpty(message = "Brand initials cannot be empty")
	@Pattern(regexp = "^[a-zA-Z]$", message = "Brand initials must be a letter")
	private String firstLetter;
	/**
	 * sort
	 */
	@NotNull(message = "Brand sort cannot be empty")
	@Min(value = 0, message = "Brand sort must be greater than or equal to 0")
	private Integer sort;

}
