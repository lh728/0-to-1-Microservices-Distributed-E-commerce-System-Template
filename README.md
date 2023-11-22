# 0-to-1-Microservices-Distributed-E-commerce-System-Template
This is a microservices-based distributed e-commerce system template designed to leverage a wide range of advanced management tools and practices from 0 to 1.

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

Node.js 12.13.0

#### Microservices Environment

Configuring Spring Cloud Alibaba requires checking the official website to find the corresponding compatible versions.

Spring Cloud Alibaba - **Nacos** (Service Registration and Configuration Center)

Spring Cloud - **Ribbon** (Load Balancing)

Spring Cloud - **openFeign** (Remote Service Invocation)

Spring Cloud Alibaba - **Sentinel** (Service Fault Tolerance — Rate Limiting, Degrade, and Circuit Breaking)

Spring Cloud - **GateWay** (API Gateway)

Spring Cloud - **Sleuth** (Distributed Tracing)

Spring Cloud Alibaba - **Seata** (Formerly Fescar, a Distributed Transaction Solution)

<br>

### Dependency

Springboot 2.7.17

Spring Web

Spring Cloud Routing - openFeign (microservices communication)



## Administraion System

For the backend management, we will directly use the existing "renren-fast" to save time.

The project's GitHub repository can be found at the following address: [https://github.com/renrenio/renren-fast.git](https://github.com/renrenio/renren-fast.git)

For the front-end pages, we use "renren-fast-vue" to achieve rapid development: https://github.com/renrenio/renren-fast-vue.git. The frontend code for this project is here: <a href = "https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template-front-end" >0-to-1-Microservices-Distributed-E-commerce-System-Template-front-end </a> .

To start the administration service, a database named "ADMIN" needs to be created. The table creation statements for this database can also be found in the following location: <a href = "https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template/blob/777679015934b1f745a7cd55b6e66a884eace26e/renren-fast/db/mysql.sql" >Github</a>

The backend starts after modifying the database files. For the frontend, after downloading Node.js, execute the `npm install` command and then run `npm run dev`.

<br>

**Here are some important considerations when upgrading versions:**

- In addition, since the backend spring boot version of renren-fast is relatively low, only 2.6.6, it is necessary to modify it to version 2.7.17 in the pom file.

- Note: To start the frontend of this project, Python needs to be downloaded. I have downloaded version 3.12.0 and also need to make modifications to the `package.json` file, updating `"node-sass": "4.13.1"` and `"sass-loader": "7.3.1"`. Afterward, execute the following commands:

  ```shell
  npm install chromedriver@2.27.2 - -ignore -scripts
  npm install chromedriver --chromedriver_cdnurl=http://cdn.npm.taobao.org/dist/chromedriver
  npm install 
  ```

  Another solution is to change Node.js to version 12.13.0, which requires no modifications. This version is highly recommended.

- Note that after the front-end is launched, it is necessary to modify the directory file `/static/config/index.js` with the statement `window.SITE_CONFIG['baseUrl'] = 'local API interface request address';`.

<br>

The basic CRUD functionalities of microservices will be generated through the **[renren-generator](https://gitee.com/renrenio/renren-generator)**. This code generator can dynamically produce entity, XML, DAO, service, HTML, JS, and SQL code online, reducing over 70% of development tasks.

The position of this code generator in the project is as follows: <a href="https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template/tree/3e7badbe5f3cf8acd1607f7e73aeb69f76d2e3a5/renren-generator">renren-generator</a>.

**Several points to note to solve pom file problems:**

- Directly launching renren-generator will fail because Oracle JDBC cannot be configured directly like MySQL due to Oracle licensing issues. Maven3 does not provide the Oracle JDBC driver, so it needs to be configured manually. Therefore, you need to download <a href="https://mvnrepository.com/artifact/oracle/ojdbc6/11.2.0.3">ojdbc6.jar</a>and then call this:

```shell
mvn install:install-file -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0.3 -Dpackaging=jar -Dfile=ojdbc6-11.2.0.3.jar -DgeneratePom=true -DlocalRepositoryPath=D:\apache-maven-3.9.2\repository
```

​	The command needs to be executed in the Maven 'bin' directory. The `-Dfile` flag corresponds to the path of the downloaded file, while `-DlocalRepositoryPath` represents the location of your local repository.

- Apart from the mentioned Oracle, the `microsoft.sqlserver:sqljdbc4:jar:4.0` also encounters the same issue. You need to download the JAR file from <a href="https://mvnrepository.com/artifact/com.microsoft.sqlserver/sqljdbc4/4.0">this link</a>. After that, proceed with the following steps:

```shell
mvn install:install-file -Dfile=sqljdbc4-4.0.jar -DgroupId=com.microsoft.sqlserver -DartifactId=sqljdbc4 -Dversion=4.0 -Dpackaging=jar -DlocalRepositoryPath=D:\apache-maven-3.9.2\repository
```

<br>



## Microservices Design

Utilize renren-generator to generate foundational code for microservices. Prior to launching, please modify the database configuration in the application.yml file to point to the desired database. Additionally, adjust the path configuration in generator.properties as follows before initiating the service:

```properties
mainPath=com.<your min path>
#\u5305\u540D
package=com.<your package path>
moduleName=<your microserivce module name>
#\u4F5C\u8005
author=...
#Email
email=...@gmail.com
#\u8868\u524D\u7F00(\u7C7B\u540D\u4E0D\u4F1A\u5305\u542B\u8868\u524D\u7F00)
tablePrefix=<your datatable prefix>_
```

After starting the service, selecting the tables to generate code, and creating a zip file of the generated code, when you move the chosen backend code section to the corresponding `main` folder, you may observe that the imported files lack many common dependencies.

Therefore, I created the `PublicDependencies` module to store shared dependencies for use by other modules.

这个依赖在每个微服务按这样配置即可：

```xml
<dependency>
    <groupId>com.EcommerceSystemTemplate</groupId>
    <artifactId>PublicDependencies</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

Dependencies to be added and structural adjustments needed:

- mybatis-plus-boot-starter 3.2.0
- lombok 1.18.8
- Apache HttpCore 4.4.16
- commons-lang 2.6
- mysql drive 8.0.33
- servlet-api 2.3
- jakarta.validation-api 3.0.2
- For functionalities such as pagination and querying, classes like `PageUtils`, `Query`, `R`, and `SQLfilter` can be found in the `common` package of another module, `renren-fast`. Please copy these classes directly.
- Additionally, as the project automatically adds the permission control annotation `RequiresPermissions`, which is not currently needed, it is necessary to adjust the reverse engineering process. Specifically, comment out this annotation in `resources-template-Controller.java.vm` within the `renren-generator` module.

After completing the dependency configuration, each microservice needs to further configure its own data source, utilize `mybatis-plus's @MapperScan`, specify the location for SQL file mappings, and so on. For detailed information, refer to the `application.yml` file in each respective microservice.



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

### Database Design

**(No foreign keys will be established due to the hypothetical e-commerce project. This is to avoid potential performance impacts, as e-commerce databases often deal with a large volume of data.)**

The following databases will be established: 

The character set will be set to utf8mb4 to ensure compatibility with utf8 and address potential issues related to character encoding.

You can find the details of the CREATE TABLE statement here. <a href = "https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template/tree/777679015934b1f745a7cd55b6e66a884eace26e/Static" >Github</a>

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
  - pms_spu_images  -  spu image
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

- **SMS(Coupon Management System):**
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

- **UMS(Member Management System):** 
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

## Acknowledgments.

This project is based on "<a href = "https://www.bilibili.com/video/BV1np4y1C7Yf?p=1&vd_source=06f42aa8b3e4f798e9414658938e7773" >Guli Mall</a>"  and adapted from it. Thanks to the individuals who set up this project.
