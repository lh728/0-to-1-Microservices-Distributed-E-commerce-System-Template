package com.ecommercesystemtemplate.member.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Member shipping address
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-20 21:28:50
 */
@Data
@TableName("ums_member_receive_address")
public class MemberReceiveAddressEntity implements Serializable {
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
	 * name
	 */
	private String name;
	/**
	 * phone
	 */
	private String phone;
	/**
	 * post_code
	 */
	private String postCode;
	/**
	 * province
	 */
	private String province;
	/**
	 * city
	 */
	private String city;
	/**
	 * region
	 */
	private String region;
	/**
	 * detail_address
	 */
	private String detailAddress;
	/**
	 * areacode
	 */
	private String areacode;
	/**
	 * default_status
	 */
	private Integer defaultStatus;

}
