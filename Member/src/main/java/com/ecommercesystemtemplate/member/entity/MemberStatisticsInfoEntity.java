package com.ecommercesystemtemplate.member.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Member statistics
 * 
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-20 21:28:50
 */
@Data
@TableName("ums_member_statistics_info")
public class MemberStatisticsInfoEntity implements Serializable {
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
	 * consume_amount
	 */
	private BigDecimal consumeAmount;
	/**
	 * Cumulative discount amount
	 */
	private BigDecimal couponAmount;
	/**
	 * order_count
	 */
	private Integer orderCount;
	/**
	 * coupon_count
	 */
	private Integer couponCount;
	/**
	 * comment_count
	 */
	private Integer commentCount;
	/**
	 * return_order_count
	 */
	private Integer returnOrderCount;
	/**
	 * login_count
	 */
	private Integer loginCount;
	/**
	 * Number of followers
	 */
	private Integer attendCount;
	/**
	 * fans_count
	 */
	private Integer fansCount;
	/**
	 * collect_product_count
	 */
	private Integer collectProductCount;
	/**
	 * collect_subject_count
	 */
	private Integer collectSubjectCount;
	/**
	 * collect_comment_count
	 */
	private Integer collectCommentCount;
	/**
	 * invite_friend_count
	 */
	private Integer inviteFriendCount;

}
