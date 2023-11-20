package com.ecommercesystemtemplate.member.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * member level
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-20 21:28:50
 */
@Data
@TableName("ums_member_level")
public class MemberLevelEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * level name
	 */
	private String name;
	/**
	 * growth_point
	 */
	private Integer growthPoint;
	/**
	 * Whether it is the default level [0->no; 1->yes]
	 */
	private Integer defaultStatus;
	/**
	 * Free shipping standard
	 */
	private BigDecimal freeFreightPoint;
	/**
	 * Growth value obtained from each evaluation
	 */
	private Integer commentGrowthPoint;
	/**
	 * Is there any free shipping privilege?
	 */
	private Integer priviledgeFreeFreight;
	/**
	 * Is there any member price privilege?
	 */
	private Integer priviledgeMemberPrice;
	/**
	 * Is there birthday privilege?
	 */
	private Integer priviledgeBirthday;
	/**
	 * note
	 */
	private String note;

}
