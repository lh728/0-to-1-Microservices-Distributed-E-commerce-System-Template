drop table if exists oms_order;

drop table if exists oms_order_item;

drop table if exists oms_order_operate_history;

drop table if exists oms_order_return_apply;

drop table if exists oms_order_return_reason;

drop table if exists oms_order_setting;

drop table if exists oms_payment_info;

drop table if exists oms_refund_info;

/*==============================================================*/
/* Table: oms_order                                             */
/*==============================================================*/
create table oms_order
(
   id                   bigint not null auto_increment comment 'id',
   member_id            bigint comment 'member_id',
   order_sn             char(64) comment 'order_sn',
   coupon_id            bigint comment 'coupon_id',
   create_time          datetime comment 'create_time',
   member_username      varchar(200) comment 'username',
   total_amount         decimal(18,4) comment 'Total order amount',
   pay_amount           decimal(18,4) comment 'Total amount due',
   freight_amount       decimal(18,4) comment 'freight_amount',
   promotion_amount     decimal(18,4) comment 'Promotion optimization amount (discounted price, Full reduction, tiered pricing)',
   integration_amount   decimal(18,4) comment 'Deducted amount from points',
   coupon_amount        decimal(18,4) comment 'Coupon deduction amount',
   discount_amount      decimal(18,4) comment 'Adjust the discount amount used for orders in the background',
   pay_type             tinyint comment 'Payment method [1->Alipay; 2->WeChat; 3->UnionPay; 4->Cash on delivery;]',
   source_type          tinyint comment 'Order source [0->PC; 1->app]',
   status               tinyint comment 'Order status [0->pending payment; 1->pending shipment; 2->shipped; 3->completed; 4->closed; 5->invalid order]',
   delivery_company     varchar(64) comment 'delivery_company (delivery method)',
   delivery_sn          varchar(64) comment 'delivery_sn',
   auto_confirm_day     int comment 'auto_confirm_day',
   integration          int comment 'points earned',
   growth               int comment 'Achievable growth value',
   bill_type            tinyint comment 'Invoice type [0->No invoice; 1->Electronic invoice; 2->Paper invoice]',
   bill_header          varchar(255) comment 'bill_header',
   bill_content         varchar(255) comment 'bill_content',
   bill_receiver_phone  varchar(32) comment 'bill_receiver_phone',
   bill_receiver_email  varchar(64) comment 'bill_receiver_email',
   receiver_name        varchar(100) comment 'receiver_name',
   receiver_phone       varchar(32) comment 'receiver_phone',
   receiver_post_code   varchar(32) comment 'receiver_post_code',
   receiver_province    varchar(32) comment 'receiver_province',
   receiver_city        varchar(32) comment 'receiver_city',
   receiver_region      varchar(32) comment 'receiver_region',
   receiver_detail_address varchar(200) comment 'receiver_detail_address',
   note                 varchar(500) comment 'note',
   confirm_status       tinyint comment 'Confirm receipt status [0->Unconfirmed; 1->Confirmed]',
   delete_status        tinyint comment 'Deletion status [0->not deleted; 1->deleted]',
   use_integration      int comment 'Points used when placing an order',
   payment_time         datetime comment 'payment_time',
   delivery_time        datetime comment 'Shipping time',
   receive_time         datetime comment 'receive_time',
   comment_time         datetime comment 'comment_time',
   modify_time          datetime comment 'modify_time',
   primary key (id)
);

alter table oms_order comment 'order';

/*==============================================================*/
/* Table: oms_order_item                                        */
/*==============================================================*/
create table oms_order_item
(
   id                   bigint not null auto_increment comment 'id',
   order_id             bigint comment 'order_id',
   order_sn             char(64) comment 'order_sn',
   spu_id               bigint comment 'spu_id',
   spu_name             varchar(255) comment 'spu_name',
   spu_pic              varchar(500) comment 'spu_pic',
   spu_brand            varchar(200) comment 'brand',
   category_id          bigint comment 'category_id',
   sku_id               bigint comment 'sku_id',
   sku_name             varchar(255) comment 'sku_name',
   sku_pic              varchar(500) comment 'sku_pic',
   sku_price            decimal(18,4) comment 'sku_price',
   sku_quantity         int comment 'sku_quantity',
   sku_attrs_vals       varchar(500) comment 'Product sales attribute combination (JSON)',
   promotion_amount     decimal(18,4) comment 'promotion_amount',
   coupon_amount        decimal(18,4) comment 'coupon_amount',
   integration_amount   decimal(18,4) comment 'integration_amount',
   real_amount          decimal(18,4) comment 'real_amount',
   gift_integration     int comment 'gift_integration',
   gift_growth          int comment 'gift_growth',
   primary key (id)
);

alter table oms_order_item comment 'order detail';

/*==============================================================*/
/* Table: oms_order_operate_history                             */
/*==============================================================*/
create table oms_order_operate_history
(
   id                   bigint not null auto_increment comment 'id',
   order_id             bigint comment 'order_id',
   operate_man          varchar(100) comment 'Operator [user; system; background administrator]',
   create_time          datetime comment 'Operating time',
   order_status         tinyint comment 'Order status [0->pending payment; 1->pending shipment; 2->shipped; 3->completed; 4->closed; 5->invalid order]',
   note                 varchar(500) comment 'note',
   primary key (id)
);

alter table oms_order_operate_history comment 'Order operation history';

/*==============================================================*/
/* Table: oms_order_return_apply                                */
/*==============================================================*/
create table oms_order_return_apply
(
   id                   bigint not null auto_increment comment 'id',
   order_id             bigint comment 'order_id',
   sku_id               bigint comment 'Returned product id',
   order_sn             char(32) comment 'order_sn',
   create_time          datetime comment 'create_time',
   member_username      varchar(64) comment 'member_username',
   return_amount        decimal(18,4) comment 'return_amount',
   return_name          varchar(100) comment 'return_name',
   return_phone         varchar(20) comment 'return_phone',
   status               tinyint(1) comment 'Application status [0->pending; 1->returning; 2->completed; 3->rejected]',
   handle_time          datetime comment 'processing time',
   sku_img              varchar(500) comment 'sku_img',
   sku_name             varchar(200) comment 'sku_name',
   sku_brand            varchar(200) comment 'sku_brand',
   sku_attrs_vals       varchar(500) comment 'Product sales attributes (JSON)',
   sku_count            int comment 'sku_count',
   sku_price            decimal(18,4) comment 'sku_price',
   sku_real_price       decimal(18,4) comment 'sku_real_price',
   reason               varchar(200) comment 'reason',
   description          varchar(500) comment 'description',
   desc_pics            varchar(2000) comment 'Voucher images, separated by commas',
   handle_note          varchar(500) comment 'handle_note',
   handle_man           varchar(200) comment 'handle_man',
   receive_man          varchar(100) comment 'receive_man',
   receive_time         datetime comment 'receive_time',
   receive_note         varchar(500) comment 'receive_note',
   receive_phone        varchar(20) comment 'receive_phone',
   company_address      varchar(500) comment 'company_address',
   primary key (id)
);

alter table oms_order_return_apply comment 'Order return request';

/*==============================================================*/
/* Table: oms_order_return_reason                               */
/*==============================================================*/
create table oms_order_return_reason
(
   id                   bigint not null auto_increment comment 'id',
   name                 varchar(200) comment 'Return reason name',
   sort                 int comment 'sort',
   status               tinyint(1) comment 'status',
   create_time          datetime comment 'create_time',
   primary key (id)
);

alter table oms_order_return_reason comment 'reasons for return';

/*==============================================================*/
/* Table: oms_order_setting                                     */
/*==============================================================*/
create table oms_order_setting
(
   id                   bigint not null auto_increment comment 'id',
   flash_order_overtime int comment 'Flash sale order timeout closing time (minutes)',
   normal_order_overtime int comment 'Normal order timeout (minutes)',
   confirm_overtime     int comment 'Automatically confirm receipt time after shipment (days)',
   finish_overtime      int comment 'The transaction time is automatically completed and returns cannot be applied for (days)',
   comment_overtime     int comment 'Automatic good review time after order completion (days)',
   member_level         tinyint(2) comment 'Membership level [0-no limit to membership level, common to all; other-corresponding other membership levels]',
   primary key (id)
);

alter table oms_order_setting comment 'Order configuration information';

/*==============================================================*/
/* Table: oms_payment_info                                      */
/*==============================================================*/
create table oms_payment_info
(
   id                   bigint not null auto_increment comment 'id',
   order_sn             char(32) comment 'Order number (external business number)',
   order_id             bigint comment 'order_id',
   alipay_trade_no      varchar(50) comment 'alipay_trade_no',
   total_amount         decimal(18,4) comment 'total_amount',
   subject              varchar(200) comment 'subject',
   payment_status       varchar(20) comment 'payment_status',
   create_time          datetime comment 'create_time',
   confirm_time         datetime comment 'confirm_time',
   callback_content     varchar(4000) comment 'callback_content',
   callback_time        datetime comment 'callback_time',
   primary key (id)
);

alter table oms_payment_info comment 'Payment information form';

/*==============================================================*/
/* Table: oms_refund_info                                       */
/*==============================================================*/
create table oms_refund_info
(
   id                   bigint not null auto_increment comment 'id',
   order_return_id      bigint comment 'order_return_id',
   refund               decimal(18,4) comment 'refund amount',
   refund_sn            varchar(64) comment 'refund_sn',
   refund_status        tinyint(1) comment 'refund_status',
   refund_channel       tinyint comment 'Refund channels [1-Alipay, 2-WeChat, 3-UnionPay, 4-Remittance]',
   refund_content       varchar(5000),
   primary key (id)
);

alter table oms_refund_info comment 'Refund information';

CREATE TABLE IF NOT EXISTS `undo_log`
(
    `branch_id`     BIGINT       NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(128) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT(11)      NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   DATETIME(6)  NOT NULL COMMENT 'create datetime',
    `log_modified`  DATETIME(6)  NOT NULL COMMENT 'modify datetime',
    UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
    ) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT ='AT transaction mode undo table';
ALTER TABLE `undo_log` ADD INDEX `ix_log_created` (`log_created`);

-- ----------------------------
-- Table structure for mq_message
-- ----------------------------
DROP TABLE IF EXISTS `mq_message`;
CREATE TABLE `mq_message`  (
                               `message_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                               `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
                               `to_exchange` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                               `routing_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                               `class_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                               `message_status` int(11) NULL DEFAULT 0 COMMENT '0-new, 1-has send, 2-error, 3-arrived',
                               `create_time` datetime NULL DEFAULT NULL,
                               `update_time` datetime NULL DEFAULT NULL,
                               PRIMARY KEY (`message_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;
