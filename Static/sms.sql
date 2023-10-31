drop table if exists sms_coupon;

drop table if exists sms_coupon_history;

drop table if exists sms_coupon_spu_category_relation;

drop table if exists sms_coupon_spu_relation;

drop table if exists sms_home_adv;

drop table if exists sms_home_subject;

drop table if exists sms_home_subject_spu;

drop table if exists sms_member_price;

drop table if exists sms_seckill_promotion;

drop table if exists sms_seckill_session;

drop table if exists sms_seckill_sku_notice;

drop table if exists sms_seckill_sku_relation;

drop table if exists sms_sku_full_reduction;

drop table if exists sms_sku_ladder;

drop table if exists sms_spu_bounds;

/*==============================================================*/
/* Table: sms_coupon                                            */
/*==============================================================*/
create table sms_coupon
(
   id                   bigint not null auto_increment comment 'id',
   coupon_type          tinyint(1) comment 'Coupon type [0->Full site coupon; 1->Member coupon; 2->Shopping coupon; 3->Registration coupon]',
   coupon_img           varchar(2000) comment 'coupon_img',
   coupon_name          varchar(100) comment 'coupon_name',
   num                  int comment 'number',
   amount               decimal(18,4) comment 'amount',
   per_limit            int comment 'per person limit',
   min_point            decimal(18,4) comment 'requirement',
   start_time           datetime comment 'start_time',
   end_time             datetime comment 'end_time',
   use_type             tinyint(1) comment 'Usage type [0->all; 1->specified category; 2->specified product]',
   note                 varchar(200) comment 'note',
   publish_count        int(11) comment 'publish_count',
   use_count            int(11) comment 'use_count',
   receive_count        int(11) comment 'receive_count',
   enable_start_time    datetime comment 'enable_start_time',
   enable_end_time      datetime comment 'enable_end_time',
   code                 varchar(64) comment 'Promo Code',
   member_level         tinyint(1) comment 'Membership levels that can be get [0-> no level limit, others - corresponding levels]',
   publish              tinyint(1) comment 'publish status [0-unpublished, 1-published]',
   primary key (id)
);

alter table sms_coupon comment 'Coupon information';

/*==============================================================*/
/* Table: sms_coupon_history                                    */
/*==============================================================*/
create table sms_coupon_history
(
   id                   bigint not null auto_increment comment 'id',
   coupon_id            bigint comment 'coupon_id',
   member_id            bigint comment 'member_id',
   member_nick_name     varchar(64) comment 'member_nick_name',
   get_type             tinyint(1) comment 'Obtaining method [0->Backend gift; 1->Active collection]',
   create_time          datetime comment 'create_time',
   use_type             tinyint(1) comment 'Usage status [0->not used; 1->used; 2->expired]',
   use_time             datetime comment 'use_time',
   order_id             bigint comment 'order_id',
   order_sn             bigint comment 'order_sn',
   primary key (id)
);

alter table sms_coupon_history comment 'Coupon history';

/*==============================================================*/
/* Table: sms_coupon_spu_category_relation                      */
/*==============================================================*/
create table sms_coupon_spu_category_relation
(
   id                   bigint not null auto_increment comment 'id',
   coupon_id            bigint comment 'coupon_id',
   category_id          bigint comment 'category_id',
   category_name        varchar(64) comment 'category_name',
   primary key (id)
);

alter table sms_coupon_spu_category_relation comment 'Coupon category association';

/*==============================================================*/
/* Table: sms_coupon_spu_relation                               */
/*==============================================================*/
create table sms_coupon_spu_relation
(
   id                   bigint not null auto_increment comment 'id',
   coupon_id            bigint comment 'coupon_id',
   spu_id               bigint comment 'spu_id',
   spu_name             varchar(255) comment 'spu_name',
   primary key (id)
);

alter table sms_coupon_spu_relation comment 'Coupons associated with products';

/*==============================================================*/
/* Table: sms_home_adv                                          */
/*==============================================================*/
create table sms_home_adv
(
   id                   bigint not null auto_increment comment 'id',
   name                 varchar(100) comment 'name',
   pic                  varchar(500) comment 'pic',
   start_time           datetime comment 'start_time',
   end_time             datetime comment 'end_time',
   status               tinyint(1) comment 'status',
   click_count          int comment 'click_count',
   url                  varchar(500) comment 'url',
   note                 varchar(500) comment 'note',
   sort                 int comment 'sort',
   publisher_id         bigint comment 'publisher_id',
   auth_id              bigint comment 'auth_id',
   primary key (id)
);

alter table sms_home_adv comment 'Home page carousel ads';

/*==============================================================*/
/* Table: sms_home_subject                                      */
/*==============================================================*/
create table sms_home_subject
(
   id                   bigint not null auto_increment comment 'id',
   name                 varchar(200) comment 'name',
   title                varchar(255) comment 'title',
   sub_title            varchar(255) comment 'sub_title',
   status               tinyint(1) comment 'status',
   url                  varchar(500) comment 'url',
   sort                 int comment 'sort',
   img                  varchar(500) comment 'img',
   primary key (id)
);

alter table sms_home_subject comment 'Home page topic table [each topic links to a new page to display topic product information]';

/*==============================================================*/
/* Table: sms_home_subject_spu                                  */
/*==============================================================*/
create table sms_home_subject_spu
(
   id                   bigint not null auto_increment comment 'id',
   name                 varchar(200) comment 'name',
   subject_id           bigint comment 'subject_id',
   spu_id               bigint comment 'spu_id',
   sort                 int comment 'sort',
   primary key (id)
);

alter table sms_home_subject_spu comment 'subject products';

/*==============================================================*/
/* Table: sms_member_price                                      */
/*==============================================================*/
create table sms_member_price
(
   id                   bigint not null auto_increment comment 'id',
   sku_id               bigint comment 'sku_id',
   member_level_id      bigint comment 'member_level_id',
   member_level_name    varchar(100) comment 'member_level_name',
   member_price         decimal(18,4) comment 'member_price',
   add_other            tinyint(1) comment 'Can other discounts be stacked [0-cannot be stacked, 1-can be stacked]',
   primary key (id)
);

alter table sms_member_price comment 'Product membership price';

/*==============================================================*/
/* Table: sms_seckill_promotion                                 */
/*==============================================================*/
create table sms_seckill_promotion
(
   id                   bigint not null auto_increment comment 'id',
   title                varchar(255) comment 'title',
   start_time           datetime comment 'start_time',
   end_time             datetime comment 'end_time',
   status               tinyint comment 'status',
   create_time          datetime comment 'create_time',
   user_id              bigint comment 'user_id',
   primary key (id)
);

alter table sms_seckill_promotion comment 'flash sale activity';

/*==============================================================*/
/* Table: sms_seckill_session                                   */
/*==============================================================*/
create table sms_seckill_session
(
   id                   bigint not null auto_increment comment 'id',
   name                 varchar(200) comment 'name',
   start_time           datetime comment 'start_time',
   end_time             datetime comment 'end_time',
   status               tinyint(1) comment 'status',
   create_time          datetime comment 'create_time',
   primary key (id)
);

alter table sms_seckill_session comment 'flash sale events';

/*==============================================================*/
/* Table: sms_seckill_sku_notice                                */
/*==============================================================*/
create table sms_seckill_sku_notice
(
   id                   bigint not null auto_increment comment 'id',
   member_id            bigint comment 'member_id',
   sku_id               bigint comment 'sku_id',
   session_id           bigint comment 'session_id',
   subcribe_time        datetime comment 'subcribe_time',
   send_time            datetime comment 'send_time',
   notice_type          tinyint(1) comment 'Notification method [0-SMS, 1-Email]',
   primary key (id)
);

alter table sms_seckill_sku_notice comment 'Flash sale product notification subscription';

/*==============================================================*/
/* Table: sms_seckill_sku_relation                              */
/*==============================================================*/
create table sms_seckill_sku_relation
(
   id                   bigint not null auto_increment comment 'id',
   promotion_id         bigint comment 'promotion_id',
   promotion_session_id bigint comment 'promotion_session_id',
   sku_id               bigint comment 'sku_id',
   seckill_price        decimal comment 'flash price',
   seckill_count        decimal comment 'flash count',
   seckill_limit        decimal comment 'flash limit',
   seckill_sort         int comment 'flash sort',
   primary key (id)
);

alter table sms_seckill_sku_relation comment 'Flash sale product association';

/*==============================================================*/
/* Table: sms_sku_full_reduction                                */
/*==============================================================*/
create table sms_sku_full_reduction
(
   id                   bigint not null auto_increment comment 'id',
   sku_id               bigint comment 'spu_id',
   full_price           decimal(18,4) comment 'full_price',
   reduce_price         decimal(18,4) comment 'reduce_price',
   add_other            tinyint(1) comment 'Whether to participate in other promotions',
   primary key (id)
);

alter table sms_sku_full_reduction comment 'Product discount information';

/*==============================================================*/
/* Table: sms_sku_ladder                                        */
/*==============================================================*/
create table sms_sku_ladder
(
   id                   bigint not null auto_increment comment 'id',
   sku_id               bigint comment 'spu_id',
   full_count           int comment 'full_count',
   discount             decimal(4,2) comment 'discount',
   price                decimal(18,4) comment 'price',
   add_other            tinyint(1) comment 'Whether to stack other discounts [0-cannot be stacked, 1-can be stacked]',
   primary key (id)
);

alter table sms_sku_ladder comment 'Commodity ladder price';

/*==============================================================*/
/* Table: sms_spu_bounds                                        */
/*==============================================================*/
create table sms_spu_bounds
(
   id                   bigint not null auto_increment comment 'id',
   spu_id               bigint,
   grow_bounds          decimal(18,4) comment 'growth points',
   buy_bounds           decimal(18,4) comment 'Shopping points',
   work                 tinyint(1) comment 'Discount effectiveness [1111 (four status bits, from right to left); 0 - no discount, whether growth points are given away; 1 - no discount, whether shopping points are given away; 2 - there is a discount, whether growth points are given away; 3 - yes Discount, whether shopping points are given away [status bit 0: not given, 1: given]]',
   primary key (id)
);

alter table sms_spu_bounds comment 'Product spu points setting';
