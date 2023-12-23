package com.ecommercesystemtemplate.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.ecommercesystemtemplate.common.group.AddGroup;
import com.ecommercesystemtemplate.common.group.UpdateGroup;
import com.ecommercesystemtemplate.common.valid.ListValue;
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
	@Null(message = "new Brand ID must be empty", groups = {AddGroup.class})
	@NotNull(message = "update Brand ID cannot be empty", groups = {UpdateGroup.class})
	private Long brandId;
	/**
	 * name
	 */
	@NotBlank(message = "Brand name cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
	private String name;
	/**
	 * logo address
	 */
	@NotEmpty(message = "Brand logo address cannot be empty", groups = {AddGroup.class})
	@URL(message = "Brand logo address must be a valid URL", groups = {AddGroup.class, UpdateGroup.class})
	private String logo;
	/**
	 * descript
	 */
	private String descript;
	/**
	 * show_status[0-no display；1-display]
	 */
	@ListValue(value = {0, 1}, message = "Brand display status must be 0 or 1", groups = {AddGroup.class, UpdateGroup.class})
	private Integer showStatus;
	/**
	 * Retrieving initials
	 */
	@NotEmpty(message = "Brand initials cannot be empty", groups = {AddGroup.class})
	@Pattern(regexp = "^[a-zA-Z]$", message = "Brand initials must be a letter", groups = {AddGroup.class, UpdateGroup.class})
	private String firstLetter;
	/**
	 * sort
	 */
	@NotNull(message = "Brand sort cannot be empty", groups = {AddGroup.class})
	@Min(value = 0, message = "Brand sort must be greater than or equal to 0", groups = {AddGroup.class, UpdateGroup.class})
	private Integer sort;

}
