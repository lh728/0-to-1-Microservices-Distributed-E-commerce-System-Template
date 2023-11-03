# 0-to-1-Microservices-Distributed-E-commerce-System-Template
This is a microservices-based distributed e-commerce system template designed to leverage a wide range of advanced management tools and practices from 0 to 1.

**I would like to use the latest technologies as much as possible to complete this e-commerce template.**

<br>

## Project Setup Guide

### Virtual Machine

Download a Linux virtual machine based on Windows using **VirtualBox** (Make sure to enable CPU virtualization mode).

Download the official image via Vagrant, create a Linux virtual machine by `vagrant init centos/7`, and modify the Vagrantfile network settings (change the private network to your computer's IP address for domain name mapping; you can find your IP address using the '`ipconfig`' command).

<br>

### Install Docker

```bash
# Update the software packages
sudo yum update -y

# Install necessary dependencies to be able to install Docker from the official repository
sudo yum install -y yum-utils device-mapper-persistent-data lvm2

# Add the Docker official repository
sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo

# Install Docker Engine (Docker CE)
sudo yum install docker-ce docker-ce-cli containerd.io

# Start the Docker service and set it to start on boot
sudo systemctl start docker
sudo systemctl enable docker

# Confirm that Docker is correctly installed and running
sudo docker --version

# Verify that Docker is working properly
sudo docker run hello-world

```

<br>

### Install mysql 8.0.17

```bash
# Pull the MySQL 8.0.17 image
docker pull mysql:8.0.17

# Run the MySQL container. Remember to create the necessary data volume directories using mkdir.
docker run -d -p 3306:3306 --name mysql-container \
-v /mydata/mysql/log:/var/log/mysql \
-v /mydata/mysql/data:/var/lib/mysql \
-v /mydata/mysql/conf:/etc/mysql \
-v /mydata/mysql-files:/var/lib/mysql-files -e MYSQL_ROOT_PASSWORD=425658167 mysql:8.0.17

# Verify if the MySQL container is running
docker ps

# Set the character encoding (if needed). Create a my.cnf file in /mydata/mysql/conf using vi.
[mysqld]
character-set-server=utf8

[client]
default-character-set=utf8

```

<br>

### Install redis

```bash
# Pull the Redis image
docker pull redis

# Run the Redis container
docker run -p 6379:6379 --name redis -v /mydata/redis/data:/data \
-v /mydata/redis/conf/redis.conf:/etc/redis/redis.conf \
-d redis redis-server /etc/redis/redis.conf

# Verify if the Redis container is running
docker ps

# Interact with Redis (optional)
docker exec -it redis redis-cli

# The latest Redis versions have persistence enabled by default, so you don't need to modify the configuration file for now. 

```

<br>

### **Environment**

Java17.0.6 

MAVEN 3.9.2

Git

<br>

### Dependency

Springboot 3.1.4

Spring Web

Spring Cloud Routing - openFeign (microservices communication)

<br>

### Database Design

**(No foreign keys will be established due to the hypothetical e-commerce project. This is to avoid potential performance impacts, as e-commerce databases often deal with a large volume of data.)**

The following databases will be established: 

The character set will be set to utf8mb4 to ensure compatibility with utf8 and address potential issues related to character encoding.

You can find the details of the CREATE TABLE statement here. <a href = "https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template/tree/e72007596d45fa6a3885afc0e1dd9b42357ee3d0/Static" >Github</a>

- **OMS(Order Management System)：**
  - oms_order  - order information 
  - oms_order_item - order detail
  - oms_order_operate_history - Order operation history
  - oms_order_return_apply - Order return request
  - oms_order_return_reason - reasons for return
  - oms_order_setting - Order configuration information
  - oms_payment_info - Payment information form
  - oms_refund_info - Refund information


<br>

- **PMS(Product Management System):**
  - pms_attr - Product attributes
  - pms_attr_attrgroup_relation  - Attribute & Attribute group association
  - pms_attr_group  - Grouping attributes
  - pms_brand - brand
  - pms_category  -  Three-level classification of commodities
  - pms_category_brand_relation  -  Brand classification association
  - pms_comment_replay  -  Product review response relationship
  - pms_product_attr_value  -  spu attribute value
  - pms_sku_images  -  sku pictures
  - pms_sku_info  -  sku information
  - pms_sku_sale_attr_value  -  sku sales attributes & values
  - pms_spu_comment  -  Product reviews
  -  pms_spu_images  -  spu image
  - pms_spu_info  -  spu information
  - pms_spu_info_desc  -  spu information introduction


<br>

- **WMS(Warehouse Management System):**
  - wms_purchase  -  Purchasing Information
  - wms_purchase_detail  -  Purchasing detail
  - wms_ware_info  -  Warehouse information
  - wms_ware_order_task  -  Inventory work order
  - wms_ware_order_task_detail  -  Inventory work order detail
  - wms_ware_sku  -  Commodity stocks


<br>

- **SMS(Storage Management System):**
  - sms_coupon - Coupon information
  - sms_coupon_history - Coupon history
  - sms_coupon_spu_category_relation - Coupon category association
  - sms_coupon_spu_relation - Coupons associated with products
  - sms_home_adv - Home page carousel ads
  - sms_home_subject - Home page topic table [each topic links to a new page to display topic product information]
  - sms_home_subject_spu - subject products
  - sms_member_price  - Product membership price
  - sms_seckill_promotion  - flash sale activity
  - sms_seckill_session  - flash sale events
  - sms_seckill_sku_notice  - Flash sale product notification subscription
  - sms_seckill_sku_relation  - Flash sale product association
  - sms_sku_full_reduction  - Product discount information
  - sms_sku_ladder  - Commodity ladder price
  - sms_spu_bounds  - Product spu points setting


<br>

- **UMS(Coupon Management System):** 
  - ums_growth_change_history  - Growth value change history
  - ums_integration_change_history  - Points change history
  - ums_member  - member
  - ums_member_collect_spu  - Products collected by members
  - ums_member_collect_subject  - Special events collected by members
  - ums_member_level  - member level
  - ums_member_login_log  - Member login record
  - ums_member_receive_address  - Member shipping address
  - ums_member_statistics_info  - Member statistics


<br>

## Microservices Design

### Order

<br>

### Member

<br>

### Product

<br>

### Storage

<br>

### Coupon

<br>





## Administraion System

For the backend management, we will directly use the existing "renren-fast" to save time.

The project's GitHub repository can be found at the following address: [https://github.com/renrenio/renren-fast.git](https://github.com/renrenio/renren-fast.git)

For the front-end pages, we use "renren-fast-vue" to achieve rapid development: https://github.com/renrenio/renren-fast-vue.git

To start the administration service, a database named "ADMIN" needs to be created. The table creation statements for this database can also be found in the following location: <a href = "https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template/blob/194bd2f724b6d54b20ba8940742e229caabfef9c/renren-fast/db/mysql.sql" >Github</a>









<br>

## Acknowledgments.

This project is based on "<a href = "https://www.bilibili.com/video/BV1np4y1C7Yf?p=1&vd_source=06f42aa8b3e4f798e9414658938e7773" >Guli Mall</a>"  and adapted from it. Thanks to the individuals who set up this project.
