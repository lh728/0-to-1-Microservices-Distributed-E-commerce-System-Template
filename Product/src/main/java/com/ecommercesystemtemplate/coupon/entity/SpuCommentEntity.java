package com.ecommercesystemtemplate.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Product reviews
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-18 12:20:53
 */
@Data
@TableName("pms_spu_comment")
public class SpuCommentEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * sku_id
	 */
	private Long skuId;
	/**
	 * spu_id
	 */
	private Long spuId;
	/**
	 * spu_name
	 */
	private String spuName;
	/**
	 * member_nick_name
	 */
	private String memberNickName;
	/**
	 * star
	 */
	private Integer star;
	/**
	 * member_ip
	 */
	private String memberIp;
	/**
	 * create_time
	 */
	private Date createTime;
	/**
	 * show_status[0-no display，1-display]
	 */
	private Integer showStatus;
	/**
	 * Attribute combination at purchase time
	 */
	private String spuAttributes;
	/**
	 * likes_count
	 */
	private Integer likesCount;
	/**
	 * reply_count
	 */
	private Integer replyCount;
	/**
	 * Comment image/video [json data; [{type: file type,url: resource path}]]
	 */
	private String resources;
	/**
	 * content
	 */
	private String content;
	/**
	 * member_icon
	 */
	private String memberIcon;
	/**
	 * Review type [0 - direct review to item, 1 - reply to review]
	 */
	private Integer commentType;

}
