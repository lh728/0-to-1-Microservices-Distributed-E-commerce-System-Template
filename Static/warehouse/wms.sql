drop table if exists wms_purchase;

drop table if exists wms_purchase_detail;

drop table if exists wms_ware_info;

drop table if exists wms_ware_order_task;

drop table if exists wms_ware_order_task_detail;

drop table if exists wms_ware_sku;

/*==============================================================*/
/* Table: wms_purchase                                          */
/*==============================================================*/
create table wms_purchase
(
   id                   bigint not null auto_increment comment 'id',
   assignee_id          bigint comment 'assignee_id',
   assignee_name        varchar(255) comment 'assignee_name',
   phone                char(13) comment 'phone',
   status               int(4) comment 'status',
   ware_id              bigint comment 'warehouse_id',
   amount               decimal(18,4) comment 'amount',
   create_time          datetime comment 'create_time',
   update_time          datetime comment 'update_time',
   primary key (id)
);

alter table wms_purchase comment 'Purchasing Information';

/*==============================================================*/
/* Table: wms_purchase_detail                                   */
/*==============================================================*/
create table wms_purchase_detail
(
   id                   bigint not null auto_increment,
   purchase_id          bigint comment 'purchase_id',
   sku_id               bigint comment 'sku_id',
   sku_num              int comment 'sku_amount',
   sku_price            decimal(18,4) comment 'sku_price',
   ware_id              bigint comment 'ware_id',
   status               int comment 'Status [0 New, 1 Already Assigned, 2 Procuring, 3 Completed, 4 Procurement Failed]',
   primary key (id)
);

alter table wms_purchase_detail comment 'Purchasing detail';

/*==============================================================*/
/* Table: wms_ware_info                                         */
/*==============================================================*/
create table wms_ware_info
(
   id                   bigint not null auto_increment comment 'id',
   name                 varchar(255) comment 'name',
   address              varchar(255) comment 'address',
   areacode             varchar(20) comment 'areacode',
   primary key (id)
);

alter table wms_ware_info comment 'Warehouse information';

/*==============================================================*/
/* Table: wms_ware_order_task                                   */
/*==============================================================*/
create table wms_ware_order_task
(
   id                   bigint not null auto_increment comment 'id',
   order_id             bigint comment 'order_id',
   order_sn             varchar(255) comment 'order_sn',
   consignee            varchar(100) comment 'consignee',
   consignee_tel        char(15) comment 'consignee_tel',
   delivery_address     varchar(500) comment 'delivery_address',
   order_comment        varchar(200) comment 'order_comment',
   payment_way          tinyint(1) comment 'Payment method [1: Online payment 2: Cash on delivery]',
   task_status          tinyint(2) comment 'task_status',
   order_body           varchar(255) comment 'order_desc',
   tracking_no          char(30) comment 'tracking_number',
   create_time          datetime comment 'create_time',
   ware_id              bigint comment 'ware_id',
   task_comment         varchar(500) comment 'task_comment',
   primary key (id)
);

alter table wms_ware_order_task comment 'Inventory work order';

/*==============================================================*/
/* Table: wms_ware_order_task_detail                            */
/*==============================================================*/
create table wms_ware_order_task_detail
(
   id                   bigint not null auto_increment comment 'id',
   sku_id               bigint comment 'sku_id',
   sku_name             varchar(255) comment 'sku_name',
   sku_num              int comment 'sku_num',
   task_id              bigint comment 'task_id',
   ware_id              bigint comment 'warehouse_id',
   lock_status          int comment '1-lock 2-unlock 3-success',
   primary key (id)
);

alter table wms_ware_order_task_detail comment 'Inventory work order detail';

/*==============================================================*/
/* Table: wms_ware_sku                                          */
/*==============================================================*/
create table wms_ware_sku
(
   id                   bigint not null auto_increment comment 'id',
   sku_id               bigint comment 'sku_id',
   ware_id              bigint comment 'ware_id',
   stock                int comment 'stock',
   sku_name             varchar(200) comment 'sku_name',
   stock_locked         int comment 'stock_locked',
   primary key (id)
);

alter table wms_ware_sku comment 'Commodity stocks';

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
