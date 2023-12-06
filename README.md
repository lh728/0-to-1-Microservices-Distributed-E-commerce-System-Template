# 0-to-1-Microservices-Distributed-E-commerce-System-Template
This is an ongoing construction of a microservices-based distributed e-commerce system template, aimed at leveraging various advanced management tools and practices from 0 to 1, to achieve microservices, distributed architecture, full-stack development, clustering, deployment, automated operations, and visualized CI/CD.

**I will strive to use various high-version programming languages and dependencies.**

- Front-end and back-end separation development, deployment for both intranet and internet, front-end apps, and web, backend cluster deployment for intranet.
- Implemented functionalities include
  - Product service, which involves CRUD operations, inventory management, product details, payment, discounts, etc.；
  - User service, covering user profiles, shipping addresses, etc.
  - Warehousing service, managing product inventory, flash sales, etc.
  - Order service, handling order operations.
  - Search service, integrating Elasticsearch for product searches.
  - Centralized authentication service, including features like login, registration, single sign-on, social login, etc.

<br>

For the Chinese version of this project, click [中文版本](https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template/blob/38ec75d25befae9a1489247aec3c9567c0caac3e/README_ZH.md).



<br>

## Project Setup Guide

### **Environment**

Java17.0.6 

MAVEN 3.9.2

Git

Node.js 12.13.0

Nacos-server 2.3.0

### Microservices Environment

Configuring Spring Cloud Alibaba requires checking the official website to find the corresponding compatible versions.

For our project. use:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-alibaba-dependencies</artifactId>
            <version>2021.0.5.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

Spring Cloud Alibaba - **Nacos** (Service Discovery and Configuration Center)

Spring Cloud - **Ribbon** (Load Balancing)

Spring Cloud - **openFeign** (Remote Service Invocation)

Spring Cloud Alibaba - **Sentinel** (Service Fault Tolerance — Rate Limiting, Degrade, and Circuit Breaking)

Spring Cloud - **GateWay** (API Gateway)

Spring Cloud - **Sleuth** (Distributed Tracing)

Spring Cloud Alibaba - **Seata** (Formerly Fescar, a Distributed Transaction Solution)



<br>

### Dependency

**Please refer to the pom.xml file under the PublicDependencies package for details.**

Springboot 2.7.17

Spring Web

Spring Cloud Routing - openFeign (microservices communication)

Spring Cloud Nacos-discovery

Spring Cloud Nacos-config

Spring loadbalancer

Spring gateway





### Virtual Machine

Download a Linux virtual machine based on Windows using **VirtualBox** (Make sure to enable CPU virtualization mode).

Download the official image via Vagrant, create a Linux virtual machine by `vagrant init centos/7`, and modify the Vagrantfile network settings (change the private network to your computer's IP address for domain name mapping; you can find your IP address using the '`ipconfig`' command).

<br>

#### Install Git

```bash
sudo yum install git

# verify that git is working properly
git --version

```

<br>

#### Install Docker

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

#### Install mysql 8.0.17

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

#### Install Redis

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

<br/>



## Administraion System

For the backend management, we will directly use the existing "renren-fast" to save time.

The project's GitHub repository can be found at the following address: [https://github.com/renrenio/renren-fast.git](https://github.com/renrenio/renren-fast.git)

For the front-end pages, we use "renren-fast-vue" to achieve rapid development: https://github.com/renrenio/renren-fast-vue.git. The frontend code for this project is here: <a href = "https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template-front-end" >0-to-1-Microservices-Distributed-E-commerce-System-Template-front-end </a> .

To start the administration service, a database named "ADMIN" needs to be created. The table creation statements for this database can also be found in the following location: <a href = "https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template/blob/777679015934b1f745a7cd55b6e66a884eace26e/renren-fast/db/mysql.sql" >Github</a>

The backend starts after modifying the database files. For the frontend, after downloading Node.js, execute the `npm install` command and then run `npm run dev`.

<br>

**Here are some important considerations when upgrading versions:**

- Since the backend spring boot version of renren-fast is relatively low, only 2.6.6, it is necessary to modify it to version 2.7.17 in the pom file.

- Note: To start the frontend of this project, Python needs to be downloaded. I have downloaded version 3.12.0 and also need to make modifications to the `package.json` file, updating `"node-sass": "4.13.1"` and `"sass-loader": "7.3.1"`. Afterward, execute the following commands:

  ```shell
  npm install chromedriver@2.27.2 - -ignore -scripts
  npm install chromedriver --chromedriver_cdnurl=http://cdn.npm.taobao.org/dist/chromedriver
  npm install 
  ```

  ***Another solution is to change Node.js to version 12.13.0, which requires no modifications. This version is highly recommended.***

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

#### Environment and Dependencies

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

This dependency can be configured in each microservice as follows:

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

<br>

#### Nacos （Discovery Center）

To configure Nacos in the `PublicDependencies` modules, you need to first import the corresponding dependencies:

```xml
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>

```

Nacos has its own middleware, and the Nacos server needs to be downloaded.

To download the [nacos-server-2.3.0-BETA.zip](https://github.com/alibaba/nacos/releases/download/2.3.0-BETA/nacos-server-2.3.0-BETA.zip) version from the official website, please ensure that you are using a Spring Boot version greater than 2.7.15 and that the Java environment variable is set. After downloading, unzip the file and run the `startup.cmd` script located in the Bin folder.

At this point, you may encounter the error: `org.springframework.context.ApplicationContextException: Unable to start web server; nested exception is org.springframework.boot.web.server.WebServerException: Unable to start embedded Tomcat`, resulting in a crash. You can find the details of the issue in the log files.

This error occurs because the default startup mode is set to cluster. The solution is to switch to standalone mode. To do this, run the following command in the current directory:

```shell
startup.cmd -m standalone
```

Alternatively, you can modify the `startup.cmd` directly by adding the following line to avoid the need to execute this command every time.

Afterwards, add the following configuration to the configuration files of each microservice:

```yaml
spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848
spring.cloud.application.name=...
```

To enable service registration and discovery functionality, use the `@EnableDiscoveryClient` annotation. Place this annotation on the **main** function of your application, and then start the service.

You can now verify if the Nacos server has successfully started by accessing `http://127.0.0.1:8848/nacos`. The default username and password are both set to `nacos`.

You can also directly download the [nacos folder](https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template/tree/5de76cb9e651c8ca79420374efd360b11d1b2e40/nacos) stored in my project. After that, double-click on the `startup.cmd` file in the 'bin' directory to initiate the service.



#### Nacos （Configuration Center）

Configuring Nacos Configuration Center also requires importing packages in the `publicDependencies` microservice:

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
```

Then, in the each moicroservice  `/src/main/resources/application.yaml` configuration file, configure the Nacos Config address and import the service configuration.

```yaml
spring:
  cloud:
    nacos:
      serverAddr: 127.0.0.1:8848
  config:
    import:
      - nacos:nacos-config-example.properties?refresh=true
```

Here, it is similar to what we previously configured for the configuration center.

After the configuration is complete, go to `127.0.0.1:8848/nacos`, enter the configuration management section (`configurations`), and then create a new configuration. Typically, fill in the **Data ID** field with `nacos-config-example.properties` and the **Group** field with `DEFAULT_GROUP`.

Next, change the configuration's format to `properties`, and fill in the content that needs to be updated. This way, you can apply multiple configurations to different microservices at once within Nacos.

Example as follows:

<img src="https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template/raw/f50a28ecef002c0e2d1f30a7660b9068f9886c45/Static/nacos-config.png" style="zoom: 50%;" />

Finally, click `Publish` and then add the ***@RefreshScope annotation to the class that requires dynamically retrieved configuration values***. Subsequently, you will be able to dynamically obtain the configuration values.

Modifying the configuration in this manner ensures that the changes take effect in each microservice, and this adjustment dynamically applies without the need to restart the services.

<br/>

**Setting Namespace and Configuration Groups**

In actual development, each microservice's name will be used as a namespace. For example, the "coupon" namespace will be employed for the configuration files of the coupon microservice. Configuration groups are used to differentiate environments; for instance, the development branch will use the "DEV" group.

Subsequently, migrate the content from the original `application.properties` to Nacos, for example:

![image-20231203181135230](https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template/raw/2a9178a24f290885048611b19fa35e35779f96a3/Static/nacos-config2.png)

Then, add a new `bootstrap.properties` file and configure it as follows:

```properties
spring.application.name=coupon

spring.cloud.nacos.config.server-addr=127.0.0.1:8848
# namespace id as picture
spring.cloud.nacos.config.namespace=7201755b-6f7f-4300-8d26-eea32695c636
spring.cloud.nacos.config.group=DEV
spring.config.import=nacos:coupon.properties?refresh=true

# config extension configs
spring.cloud.nacos.config.extension-configs[0].data-id=datasource.yml
spring.cloud.nacos.config.extension-configs[0].group=DEV
spring.cloud.nacos.config.extension-configs[0].refresh=true

spring.cloud.nacos.config.extension-configs[1].data-id=mybatis.yml
spring.cloud.nacos.config.extension-configs[1].group=DEV
spring.cloud.nacos.config.extension-configs[1].refresh=true

spring.cloud.nacos.config.extension-configs[2].data-id=other.yml
spring.cloud.nacos.config.extension-configs[2].group=DEV
spring.cloud.nacos.config.extension-configs[2].refresh=true
```

Now, you can comment out the contents of the original `application.yml` and place them separately in `datasource.yml`, `mybatis.yml`, and `other.yml`. If there are changes to the configuration files later on, you only need to update the content in Nacos.

Additionally, it's worth noting that attempting to start the application directly will fail now. This is because, starting from the Spring Cloud 2020.0.2 version, the relevant dependencies for bootstrap have been removed from `spring-cloud-starter-config`. Therefore, from version 2020.02 onwards, you need to include the `spring-cloud-starter-bootstrap` dependency separately:

```xml
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bootstrap</artifactId>
            <version>3.0.3</version>
        </dependency>

```

**The content of `application.yml` will be commented out but kept in sync for demonstration purposes.**

<br/>

#### Using Feign for Declarative Remote Invocation

Remote invocations are essential in microservices architecture, and Feign serves as a declarative HTTP client designed to simplify this process. Feign provides templates for HTTP requests, allowing for the definition of parameters, formats, addresses, and other details through the creation of simple interfaces and the inclusion of annotations.
Feign seamlessly integrates with **Ribbon** for load balancing and **Hystrix** for circuit-breaking, eliminating the explicit need for managing these components.
Spring Cloud Feign extends the support for Spring MVC annotations on the foundation of Netflix Feign. With its implementation, creating an interface and configuring it with annotations is all that's required to bind to the service provider's interface. This simplification reduces the development effort needed to build a custom service invocation client, as opposed to the approach taken by Spring Cloud Ribbon.

**To incorporate this functionality, it is necessary to introduce these  dependencies when establishing each microservice:**

```xml
         <!-- put loadbalancer in publicDependencies-->
		<dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-loadbalancer</artifactId>
            <version>2.2.0.RELEASE</version>
        </dependency>
        <!-- put openFeign in each modules-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency> 
```

Afterwards, to enable remote service invocation, you need to write an interface that informs Spring Cloud that this interface requires remote service invocation.

```java
import com.ecommercesystemtemplate.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("coupon")
@Service
public interface CouponFeignService {

    @RequestMapping("coupon/coupon/member/list")
    R memberCoupons();
}
```

Following the code provided above, I have implemented remote invocation of Coupon microservice methods within the Member microservice. This involves filling in the FeignClient annotation with the name of the remote service (i.e., the name registered in Nacos), and specifying the complete path and signature for the method.

Lastly, add the annotation to the main class `MemberApplication` in the Member microservice. The `basePackages` following it should be the package path where you created the `CouponFeignService` interface.

```
@EnableFeignClients(basePackages = "com.ecommercesystemtemplate.member.entity")
```

 

#### GateWay

The common functionalities of a gateway include route forwarding, permission verification, rate limiting, and API management. In this context, Spring Cloud Gateway is utilized as the gateway, replacing the Zuul gateway.

To enable gateway functionality, the following dependency needs to be added:

```xml
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>
```

To use the gateway, service registration and discovery must be enabled. Therefore, the `GatewayApplication` should be annotated with `@EnableDiscoveryClient`. Additionally, configure the Nacos registry center address, referring to the Nacos configuration center setup mentioned earlier.

It's worth noting that, since all dependencies from PublicDependencies are directly used, it includes MyBatis configuration, which is currently not required for the gateway. To exclude database configuration, add `(exclude = {DataSourceAutoConfiguration.class})` to the `@SpringBootApplication` annotation.



<br>

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

## Database Design

**(No foreign keys will be established due to this is a e-commerce project. This is to avoid potential performance impacts, as e-commerce databases often deal with a large volume of data.)**

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
