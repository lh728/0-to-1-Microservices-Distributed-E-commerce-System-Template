package com.ecommercesystemtemplate.warehouse.vo;

import lombok.Data;

@Data
public class MemberAddressVo {

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
