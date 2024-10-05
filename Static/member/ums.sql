drop table if exists ums_growth_change_history;

drop table if exists ums_integration_change_history;

drop table if exists ums_member;

drop table if exists ums_member_collect_spu;

drop table if exists ums_member_collect_subject;

drop table if exists ums_member_level;

drop table if exists ums_member_login_log;

drop table if exists ums_member_receive_address;

drop table if exists ums_member_statistics_info;

/*==============================================================*/
/* Table: ums_growth_change_history                             */
/*==============================================================*/
create table ums_growth_change_history
(
   id                   bigint not null auto_increment comment 'id',
   member_id            bigint comment 'member_id',
   create_time          datetime comment 'create_time',
   change_count         int comment 'Changed value (positive or negative count)',
   note                 varchar(0) comment 'note',
   source_type          tinyint comment 'Points source [0-shopping, 1-administrator modification]',
   primary key (id)
);

alter table ums_growth_change_history comment 'Growth value change history';

/*==============================================================*/
/* Table: ums_integration_change_history                        */
/*==============================================================*/
create table ums_integration_change_history
(
   id                   bigint not null auto_increment comment 'id',
   member_id            bigint comment 'member_id',
   create_time          datetime comment 'create_time',
   change_count         int comment 'change_count',
   note                 varchar(255) comment 'note',
   source_tyoe          tinyint comment 'Source [0->Shopping; 1->Administrator modification; 2->Activity]',
   primary key (id)
);

alter table ums_integration_change_history comment 'Points change history';

/*==============================================================*/
/* Table: ums_member                                            */
/*==============================================================*/
create table ums_member
(
   id                   bigint not null auto_increment comment 'id',
   level_id             bigint comment 'level_id',
   username             char(64) comment 'username',
   password             varchar(64) comment 'password',
   nickname             varchar(64) comment 'nickname',
   mobile               varchar(20) comment 'mobile',
   email                varchar(64) comment 'email',
   header               varchar(500) comment 'avatar',
   gender               tinyint comment 'gender',
   birth                date comment 'birth',
   city                 varchar(500) comment 'city',
   job                  varchar(255) comment 'job',
   sign                 varchar(255) comment 'sign',
   source_type          tinyint comment 'source_type',
   integration          int comment 'integration',
   growth               int comment 'growth',
   status               tinyint comment 'status',
   create_time          datetime comment 'create_time',
   social_uid           varchar(255) COMMENT 'social account uid',
   access_token         varchar(255) COMMENT 'social account access token',
   expires_in           varchar(255) COMMENT 'social account access token visit time',
   primary key (id)
);

alter table ums_member comment 'member';

/*==============================================================*/
/* Table: ums_member_collect_spu                                */
/*==============================================================*/
create table ums_member_collect_spu
(
   id                   bigint not null comment 'id',
   member_id            bigint comment 'member_id',
   spu_id               bigint comment 'spu_id',
   spu_name             varchar(500) comment 'spu_name',
   spu_img              varchar(500) comment 'spu_img',
   create_time          datetime comment 'create_time',
   primary key (id)
);

alter table ums_member_collect_spu comment 'Products collected by members';

/*==============================================================*/
/* Table: ums_member_collect_subject                            */
/*==============================================================*/
create table ums_member_collect_subject
(
   id                   bigint not null auto_increment comment 'id',
   subject_id           bigint comment 'subject_id',
   subject_name         varchar(255) comment 'subject_name',
   subject_img          varchar(500) comment 'subject_img',
   subject_urll         varchar(500) comment 'subject_urll',
   primary key (id)
);

alter table ums_member_collect_subject comment 'Special events collected by members';

/*==============================================================*/
/* Table: ums_member_level                                      */
/*==============================================================*/
create table ums_member_level
(
   id                   bigint not null auto_increment comment 'id',
   name                 varchar(100) comment 'level name',
   growth_point         int comment 'growth_point',
   default_status       tinyint comment 'Whether it is the default level [0->no; 1->yes]',
   free_freight_point   decimal(18,4) comment 'Free shipping standard',
   comment_growth_point int comment 'Growth value obtained from each evaluation',
   priviledge_free_freight tinyint comment 'Is there any free shipping privilege?',
   priviledge_member_price tinyint comment 'Is there any member price privilege?',
   priviledge_birthday  tinyint comment 'Is there birthday privilege?',
   note                 varchar(255) comment 'note',
   primary key (id)
);

alter table ums_member_level comment 'member level';

/*==============================================================*/
/* Table: ums_member_login_log                                  */
/*==============================================================*/
create table ums_member_login_log
(
   id                   bigint not null auto_increment comment 'id',
   member_id            bigint comment 'member_id',
   create_time          datetime comment 'create_time',
   ip                   varchar(64) comment 'ip',
   city                 varchar(64) comment 'city',
   login_type           tinyint(1) comment 'Login type [1-web, 2-app]',
   primary key (id)
);

alter table ums_member_login_log comment 'Member login record';

/*==============================================================*/
/* Table: ums_member_receive_address                            */
/*==============================================================*/
create table ums_member_receive_address
(
   id                   bigint not null auto_increment comment 'id',
   member_id            bigint comment 'member_id',
   name                 varchar(255) comment 'name',
   phone                varchar(64) comment 'phone',
   post_code            varchar(64) comment 'post_code',
   province             varchar(100) comment 'province',
   city                 varchar(100) comment 'city',
   region               varchar(100) comment 'region',
   detail_address       varchar(255) comment 'detail_address',
   areacode             varchar(15) comment 'areacode',
   default_status       tinyint(1) comment 'default_status',
   primary key (id)
);

alter table ums_member_receive_address comment 'Member shipping address';

/*==============================================================*/
/* Table: ums_member_statistics_info                            */
/*==============================================================*/
create table ums_member_statistics_info
(
   id                   bigint not null auto_increment comment 'id',
   member_id            bigint comment 'member_id',
   consume_amount       decimal(18,4) comment 'consume_amount',
   coupon_amount        decimal(18,4) comment 'Cumulative discount amount',
   order_count          int comment 'order_count',
   coupon_count         int comment 'coupon_count',
   comment_count        int comment 'comment_count',
   return_order_count   int comment 'return_order_count',
   login_count          int comment 'login_count',
   attend_count         int comment 'Number of followers',
   fans_count           int comment 'fans_count',
   collect_product_count int comment 'collect_product_count',
   collect_subject_count int comment 'collect_subject_count',
   collect_comment_count int comment 'collect_comment_count',
   invite_friend_count  int comment 'invite_friend_count',
   primary key (id)
);

alter table ums_member_statistics_info comment 'Member statistics';

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
