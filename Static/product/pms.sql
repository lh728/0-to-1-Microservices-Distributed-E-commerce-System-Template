drop table if exists pms_attr;

drop table if exists pms_attr_attrgroup_relation;

drop table if exists pms_attr_group;

drop table if exists pms_brand;

drop table if exists pms_category;

drop table if exists pms_category_brand_relation;

drop table if exists pms_comment_replay;

drop table if exists pms_product_attr_value;

drop table if exists pms_sku_images;

drop table if exists pms_sku_info;

drop table if exists pms_sku_sale_attr_value;

drop table if exists pms_spu_comment;

drop table if exists pms_spu_images;

drop table if exists pms_spu_info;

drop table if exists pms_spu_info_desc;

/*==============================================================*/
/* Table: pms_attr                                              */
/*==============================================================*/
create table pms_attr
(
   attr_id              bigint not null auto_increment comment 'attr_id',
   attr_name            char(30) comment 'attr_name',
   search_type          tinyint comment 'Does it need to be retrieved? [0- not required, 1- required]',
  `value_type` tinyint(4) DEFAULT NULL COMMENT 'value_type[0-single，1-multiple choice]',
  `icon` varchar(255) DEFAULT NULL COMMENT 'Attribute ICONS',
  `value_select` char(255) DEFAULT NULL COMMENT 'List of possible values [separated by commas]',
  `attr_type` tinyint(4) DEFAULT NULL COMMENT 'Attribute type [0- sales attribute, 1- basic attribute]',
   enable               bigint comment 'Enabled status [0 - disabled, 1 - enabled]',
   catelog_id           bigint comment 'catelog_id',
   show_desc            tinyint comment 'Quick display [whether to show on the introduction; 0- No 1- yes], can still be adjusted in the sku',
   primary key (attr_id)
);

alter table pms_attr comment 'Product attributes';

/*==============================================================*/
/* Table: pms_attr_attrgroup_relation                           */
/*==============================================================*/
create table pms_attr_attrgroup_relation
(
   id                   bigint not null auto_increment comment 'id',
   attr_id              bigint comment 'attr_id',
   attr_group_id        bigint comment 'attr_group_id',
   attr_sort            int comment 'attr_sort',
   primary key (id)
);

alter table pms_attr_attrgroup_relation comment 'Attribute & Attribute group association';

/*==============================================================*/
/* Table: pms_attr_group                                        */
/*==============================================================*/
create table pms_attr_group
(
   attr_group_id        bigint not null auto_increment comment 'attr_group_id',
   attr_group_name      char(20) comment 'attr_group_name',
   sort                 int comment 'sort',
   descript             varchar(255) comment 'descript',
   icon                 varchar(255) comment 'group icon',
   catelog_id           bigint comment 'catelog_id',
   primary key (attr_group_id)
);

alter table pms_attr_group comment 'Grouping attributes';

/*==============================================================*/
/* Table: pms_brand                                             */
/*==============================================================*/
create table pms_brand
(
   brand_id             bigint not null auto_increment comment 'brand_id',
   name                 char(50) comment 'name',
   logo                 varchar(2000) comment 'logo address',
   descript             longtext comment 'descript',
   show_status          tinyint comment 'show_status[0-no display；1-display]',
   first_letter         char(1) comment 'Retrieving initials',
   sort                 int comment 'sort',
   primary key (brand_id)
);

alter table pms_brand comment 'brand';

/*==============================================================*/
/* Table: pms_category                                          */
/*==============================================================*/
create table pms_category
(
   cat_id               bigint not null auto_increment comment 'cat_id',
   name                 char(50) comment 'name',
   parent_cid           bigint comment 'parent_cid',
   cat_level            int comment 'cat_level',
   show_status          tinyint comment 'show_status[0-no display；1-display]',
   sort                 int comment 'sort',
   icon                 char(255) comment 'icon',
   product_unit         char(50) comment 'product_unit',
   product_count        int comment 'product_count',
   primary key (cat_id)
);

alter table pms_category comment 'Three-level classification of commodities';

/*==============================================================*/
/* Table: pms_category_brand_relation                           */
/*==============================================================*/
create table pms_category_brand_relation
(
   id                   bigint not null auto_increment,
   brand_id             bigint comment 'brand_id',
   catelog_id           bigint comment 'catelog_id',
   brand_name           varchar(255),
   catelog_name         varchar(255),
   primary key (id)
);

alter table pms_category_brand_relation comment 'Brand classification association';

/*==============================================================*/
/* Table: pms_comment_replay                                    */
/*==============================================================*/
create table pms_comment_replay
(
   id                   bigint not null auto_increment comment 'id',
   comment_id           bigint comment 'comment_id',
   reply_id             bigint comment 'reply_id',
   primary key (id)
);

alter table pms_comment_replay comment 'Product review response relationship';

/*==============================================================*/
/* Table: pms_product_attr_value                                */
/*==============================================================*/
create table pms_product_attr_value
(
   id                   bigint not null auto_increment comment 'id',
   spu_id               bigint comment 'spu_id',
   attr_id              bigint comment 'attr_id',
   attr_name            varchar(200) comment 'attr_name',
   attr_value           varchar(200) comment 'attr_value',
   attr_sort            int comment 'attr_sort',
   quick_show           tinyint comment 'quick_show【Whether to show it in the introduction 0- No 1- Yes】',
   primary key (id)
);

alter table pms_product_attr_value comment 'spu attribute value';

/*==============================================================*/
/* Table: pms_sku_images                                        */
/*==============================================================*/
create table pms_sku_images
(
   id                   bigint not null auto_increment comment 'id',
   sku_id               bigint comment 'sku_id',
   img_url              varchar(255) comment 'img_url',
   img_sort             int comment 'img_sort',
   default_img          int comment 'Default graph [0 - not default graph, 1 - is default graph]',
   primary key (id)
);

alter table pms_sku_images comment 'sku pictures';

/*==============================================================*/
/* Table: pms_sku_info                                          */
/*==============================================================*/
create table pms_sku_info
(
   sku_id               bigint not null auto_increment comment 'skuId',
   spu_id               bigint comment 'spuId',
   sku_name             varchar(255) comment 'sku_name',
   sku_desc             varchar(2000) comment 'sku_desc',
   catalog_id           bigint comment 'catalog_id',
   brand_id             bigint comment 'brand_id',
   sku_default_img      varchar(255) comment 'sku_default_img',
   sku_title            varchar(255) comment 'sku_title',
   sku_subtitle         varchar(2000) comment 'sku_subtitle',
   price                decimal(18,4) comment 'price',
   sale_count           bigint comment 'sale_count',
   primary key (sku_id)
);

alter table pms_sku_info comment 'sku information';

/*==============================================================*/
/* Table: pms_sku_sale_attr_value                               */
/*==============================================================*/
create table pms_sku_sale_attr_value
(
   id                   bigint not null auto_increment comment 'id',
   sku_id               bigint comment 'sku_id',
   attr_id              bigint comment 'attr_id',
   attr_name            varchar(200) comment 'attr_name',
   attr_value           varchar(200) comment 'attr_value',
   attr_sort            int comment 'attr_sort',
   primary key (id)
);

alter table pms_sku_sale_attr_value comment 'sku sales attributes & values';

/*==============================================================*/
/* Table: pms_spu_comment                                       */
/*==============================================================*/
create table pms_spu_comment
(
   id                   bigint not null auto_increment comment 'id',
   sku_id               bigint comment 'sku_id',
   spu_id               bigint comment 'spu_id',
   spu_name             varchar(255) comment 'spu_name',
   member_nick_name     varchar(255) comment 'member_nick_name',
   star                 tinyint(1) comment 'star',
   member_ip            varchar(64) comment 'member_ip',
   create_time          datetime comment 'create_time',
   show_status          tinyint(1) comment 'show_status[0-no display，1-display]',
   spu_attributes       varchar(255) comment 'Attribute combination at purchase time',
   likes_count          int comment 'likes_count',
   reply_count          int comment 'reply_count',
   resources            varchar(1000) comment 'Comment image/video [json data; [{type: file type,url: resource path}]]',
   content              text comment 'content',
   member_icon          varchar(255) comment 'member_icon',
   comment_type         tinyint comment 'Review type [0 - direct review to item, 1 - reply to review]',
   primary key (id)
);

alter table pms_spu_comment comment 'Product reviews';

/*==============================================================*/
/* Table: pms_spu_images                                        */
/*==============================================================*/
create table pms_spu_images
(
   id                   bigint not null auto_increment comment 'id',
   spu_id               bigint comment 'spu_id',
   img_name             varchar(200) comment 'img_name',
   img_url              varchar(255) comment 'img_url',
   img_sort             int comment 'img_sort',
   default_img          tinyint comment 'default_img',
   primary key (id)
);

alter table pms_spu_images comment 'spu image';

/*==============================================================*/
/* Table: pms_spu_info                                          */
/*==============================================================*/
create table pms_spu_info
(
   id                   bigint not null auto_increment comment 'id',
   spu_name             varchar(200) comment 'spu_name',
   spu_description      varchar(1000) comment 'spu_description',
   catalog_id           bigint comment 'catalog_id',
   brand_id             bigint comment 'brand_id',
   weight               decimal(18,4),
   publish_status       tinyint comment 'Listing status [0 - Down, 1 - Up]',
   create_time          datetime,
   update_time          datetime,
   primary key (id)
);

alter table pms_spu_info comment 'spu information';

/*==============================================================*/
/* Table: pms_spu_info_desc                                     */
/*==============================================================*/
create table pms_spu_info_desc
(
   spu_id               bigint not null comment 'spu_id',
   decript              longtext comment 'decript',
   primary key (spu_id)
);

alter table pms_spu_info_desc comment 'spu information introduction';

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
