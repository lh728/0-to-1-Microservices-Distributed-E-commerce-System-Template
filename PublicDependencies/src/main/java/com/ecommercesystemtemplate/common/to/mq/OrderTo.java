package com.ecommercesystemtemplate.common.to.mq;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderTo {

    private Long id;
    /**
     * member_id
     */
    private Long memberId;
    /**
     * order_sn
     */
    private String orderSn;
    /**
     * coupon_id
     */
    private Long couponId;
    /**
     * create_time
     */
    private Date createTime;
    /**
     * username
     */
    private String memberUsername;
    /**
     * Total order amount
     */
    private BigDecimal totalAmount;
    /**
     * Total amount due
     */
    private BigDecimal payAmount;
    /**
     * freight_amount
     */
    private BigDecimal freightAmount;
    /**
     * Promotion optimization amount (discounted price, Full reduction, tiered pricing)
     */
    private BigDecimal promotionAmount;
    /**
     * Deducted amount from points
     */
    private BigDecimal integrationAmount;
    /**
     * Coupon deduction amount
     */
    private BigDecimal couponAmount;
    /**
     * Adjust the discount amount used for orders in the background
     */
    private BigDecimal discountAmount;
    /**
     * Payment method [1->Alipay; 2->WeChat; 3->UnionPay; 4->Cash on delivery;]
     */
    private Integer payType;
    /**
     * Order source [0->PC; 1->app]
     */
    private Integer sourceType;
    /**
     * Order status [0->pending payment; 1->pending shipment; 2->shipped; 3->completed; 4->closed; 5->invalid order]
     */
    private Integer status;
    /**
     * delivery_company (delivery method)
     */
    private String deliveryCompany;
    /**
     * delivery_sn
     */
    private String deliverySn;
    /**
     * auto_confirm_day
     */
    private Integer autoConfirmDay;
    /**
     * points earned
     */
    private Integer integration;
    /**
     * Achievable growth value
     */
    private Integer growth;
    /**
     * Invoice type [0->No invoice; 1->Electronic invoice; 2->Paper invoice]
     */
    private Integer billType;
    /**
     * bill_header
     */
    private String billHeader;
    /**
     * bill_content
     */
    private String billContent;
    /**
     * bill_receiver_phone
     */
    private String billReceiverPhone;
    /**
     * bill_receiver_email
     */
    private String billReceiverEmail;
    /**
     * receiver_name
     */
    private String receiverName;
    /**
     * receiver_phone
     */
    private String receiverPhone;
    /**
     * receiver_post_code
     */
    private String receiverPostCode;
    /**
     * receiver_province
     */
    private String receiverProvince;
    /**
     * receiver_city
     */
    private String receiverCity;
    /**
     * receiver_region
     */
    private String receiverRegion;
    /**
     * receiver_detail_address
     */
    private String receiverDetailAddress;
    /**
     * note
     */
    private String note;
    /**
     * Confirm receipt status [0->Unconfirmed; 1->Confirmed]
     */
    private Integer confirmStatus;
    /**
     * Deletion status [0->not deleted; 1->deleted]
     */
    private Integer deleteStatus;
    /**
     * Points used when placing an order
     */
    private Integer useIntegration;
    /**
     * payment_time
     */
    private Date paymentTime;
    /**
     * Shipping time
     */
    private Date deliveryTime;
    /**
     * receive_time
     */
    private Date receiveTime;
    /**
     * comment_time
     */
    private Date commentTime;
    /**
     * modify_time
     */
    private Date modifyTime;

}
