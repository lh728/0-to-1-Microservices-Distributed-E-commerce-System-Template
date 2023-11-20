package com.ecommercesystemtemplate.member.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Growth value change history
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-20 21:28:50
 */
@Data
@TableName("ums_growth_change_history")
public class GrowthChangeHistoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * member_id
	 */
	private Long memberId;
	/**
	 * create_time
	 */
	private Date createTime;
	/**
	 * Changed value (positive or negative count)
	 */
	private Integer changeCount;
	/**
	 * note
	 */
	private String note;
	/**
	 * Points source [0-shopping, 1-administrator modification]
	 */
	private Integer sourceType;

}
