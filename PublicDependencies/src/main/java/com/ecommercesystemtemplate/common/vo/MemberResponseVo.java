package com.ecommercesystemtemplate.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lhjls
 */
@Data
public class MemberResponseVo implements Serializable {

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
    /**
     * social_uid
     */
    private String socialUid;
    /**
     * access_token
     */
    private String accessToken;
    /**
     * expire_time
     */
    private Long expireTime;
}
