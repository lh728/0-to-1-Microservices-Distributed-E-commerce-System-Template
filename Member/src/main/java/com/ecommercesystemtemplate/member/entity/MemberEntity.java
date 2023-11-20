package com.ecommercesystemtemplate.member.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * member
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-20 21:28:50
 */
@Data
@TableName("ums_member")
public class MemberEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * level_id
	 */
	private Long levelId;
	/**
	 * username
	 */
	private String username;
	/**
	 * password
	 */
	private String password;
	/**
	 * nickname
	 */
	private String nickname;
	/**
	 * mobile
	 */
	private String mobile;
	/**
	 * email
	 */
	private String email;
	/**
	 * avatar
	 */
	private String header;
	/**
	 * gender
	 */
	private Integer gender;
	/**
	 * birth
	 */
	private Date birth;
	/**
	 * city
	 */
	private String city;
	/**
	 * job
	 */
	private String job;
	/**
	 * sign
	 */
	private String sign;
	/**
	 * source_type
	 */
	private Integer sourceType;
	/**
	 * integration
	 */
	private Integer integration;
	/**
	 * growth
	 */
	private Integer growth;
	/**
	 * status
	 */
	private Integer status;
	/**
	 * create_time
	 */
	private Date createTime;

}
