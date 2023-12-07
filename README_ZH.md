# 从0到1的微服务分布式电子商务系统模板

这是一个正在构建中的基于微服务的分布式电子商务系统模板，旨在利用各种先进的管理工具和实践，从零到一实现微服务、分布式架构、全栈开发、集群、部署、自动化运维以及可视化CI/CD。

**我将尽可能使用高版本的编程语言和依赖环境。**

- 前后端分离开发，内外网部署，前端应用和Web，内网部署后端集群。
- 已实现功能包括：
  - 商品服务，涉及CRUD操作、库存管理、商品详情、支付、优惠等；
  - 用户服务，包括用户档案、收货地址等；
  - 仓储服务，管理商品库存、秒杀等；
  - 订单服务，处理订单操作；
  - 检索服务，集成Elasticsearch进行商品检索；
  - 集中认证服务，包括登录、注册、单点登录、社交登录等。

<br>

##  项目设置指南

### **环境**

Java 17.0.6 

MAVEN 3.9.2

Git

Nacos-server 2.3.0

Node.js 12.13.0

ES 6

Vue 2

<br>



### 依赖

详情见PublicDependencies包下的pom.xml文件

Springboot 2.7.17

Spring Web

Spring Cloud Routing - openFeign (microservices communication)

Spring Cloud Nacos-discovery

Spring Cloud Nacos-config

Spring loadbalancer

Spring gateway

<br>

### 微服务环境

配置 Spring Cloud Alibaba 需要查看官方网站以找到相应的兼容版本。

对于我们的项目，请使用：

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

Spring Cloud Alibaba - **Nacos**（服务发现和配置中心）

Spring Cloud - **Ribbon**（负载均衡）

Spring Cloud - **openFeign** （远程服务调用）

Spring Cloud Alibaba - **Sentinel**（服务容错 — 限流、降级和熔断）

Spring Cloud - **GateWay**（API 网关）

Spring Cloud - **Sleuth** （分布式追踪）

Spring Cloud Alibaba - **Seata**（前身为 Fescar，分布式事务解决方案）



<br>



### 虚拟机

在 Windows 上使用 **VirtualBox** 下载一个基于 Linux 的虚拟机（确保启用了 CPU 虚拟化模式）。

通过 Vagrant 下载官方镜像，使用 `vagrant init centos/7` 命令创建一个 Linux 虚拟机，并修改 Vagrantfile 网络设置（将私有网络更改为计算机的 IP 地址以进行域名映射；可以使用 '`ipconfig`' 命令找到你的 IP 地址）。

<br>

#### 安装 Git

```bash
sudo yum install git

# verify that git is working properly
git --version

```

<br>

#### 安装 Docker

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

#### 安装 mysql 8.0.17

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

#### 安装 Redis

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



## 管理系统

对于后端管理，我们将直接使用现有的 "renren-fast" 以节省时间。

该项目的 GitHub 仓库可以在以下地址找到：https://github.com/renrenio/renren-fast.git

对于前端页面，我们使用 "renren-fast-vue" 来实现快速开发：https://github.com/renrenio/renren-fast-vue.git。该项目的前端代码在这里：<a href="https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template-front-end">0-to-1-Microservices-Distributed-E-commerce-System-Template-front-end </a>。

要启动管理服务，需要创建一个名为 "ADMIN" 的数据库。该数据库的表创建语句也可以在以下位置找到：<a href="https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template/blob/777679015934b1f745a7cd55b6e66a884eace26e/renren-fast/db/mysql.sql">GitHub</a>

在修改数据库文件后，后端将会启动。对于前端，在下载 Node.js 后，执行 `npm install` 命令，然后运行 `npm run dev`。

<br>

**在升级版本时需要考虑以下重要事项：**

- 由于 renren-fast 后端 spring boot 版本相对较低，只有 2.6.6 版本，需要在 pom 文件中将其修改为 2.7.17 版本

- 注意：要启动该项目的前端，需要下载 Python。我已经下载了版本 3.12.0，并且还需要对 `package.json` 文件进行修改，更新 `"node-sass": "4.13.1"` 和 `"sass-loader": "7.3.1"`。之后，执行以下命令：

  ```shell
  npm install chromedriver@2.27.2 - -ignore -scripts
  npm install chromedriver --chromedriver_cdnurl=http://cdn.npm.taobao.org/dist/chromedriver
  npm install 
  ```

  ***另一种解决方案是将 Node.js 版本更改为 12.13.0，无需进行任何修改。强烈推荐使用此版本*。**

- 请注意，在启动前端之后，需要修改目录文件 `/static/config/index.js`，添加语句 `window.SITE_CONFIG['baseUrl'] = '本地API接口请求地址';`

<br>

微服务的基本 CRUD 功能将通过 **[renren-generator](https://gitee.com/renrenio/renren-generator)** 生成。该代码生成器可以在线动态生成实体、XML、DAO、服务、HTML、JS 和 SQL 代码，减少了超过70%的开发任务。

该代码生成器在项目中的位置如下：<a href="https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template/tree/3e7badbe5f3cf8acd1607f7e73aeb69f76d2e3a5/renren-generator">renren-generator</a>。

**解决 pom 文件问题的几个注意事项：**

- 直接启动 renren-generator 将失败，因为由于 Oracle 许可问题，无法像 MySQL 一样直接配置 Oracle JDBC。Maven3 不提供 Oracle JDBC 驱动程序，因此需要手动配置。因此，你需要下载 <a href="https://mvnrepository.com/artifact/oracle/ojdbc6/11.2.0.3">ojdbc6.jar</a>，然后执行以下操作：

```shell
mvn install:install-file -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0.3 -Dpackaging=jar -Dfile=ojdbc6-11.2.0.3.jar -DgeneratePom=true -DlocalRepositoryPath=D:\apache-maven-3.9.2\repository
```

​	该命令需要在 Maven 'bin' 目录中执行。`-Dfile` 标志对应于下载文件的路径，而 `-DlocalRepositoryPath` 表示本地存储库的位置。

- 除了提到的 Oracle 外，`microsoft.sqlserver:sqljdbc4:jar:4.0` 也遇到相同的问题。你需要从<a href="https://mvnrepository.com/artifact/com.microsoft.sqlserver/sqljdbc4/4.0">此链接</a>下载 JAR 文件。之后，执行以下步骤：

```shell
mvn install:install-file -Dfile=sqljdbc4-4.0.jar -DgroupId=com.microsoft.sqlserver -DartifactId=sqljdbc4 -Dversion=4.0 -Dpackaging=jar -DlocalRepositoryPath=D:\apache-maven-3.9.2\repository
```

<br>



## 微服务设计

### 环境和依赖

利用 renren-generator 生成微服务的基础代码。在启动之前，请在 application.yml 文件中修改数据库配置，以指向所需的数据库。此外，在启动服务之前，请按照以下方式调整 generator.properties 中的路径配置：

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

启动服务、选择要生成代码的表格并创建生成代码的 zip 文件后，当你将所选的后端代码部分移动到相应的 `main` 文件夹时，可能会观察到导入的文件缺少许多常见的依赖项。

因此，我创建了 `PublicDependencies` 模块，用于存储其他模块使用的共享依赖项。

可以在每个微服务中配置此依赖项，如下所示：

```xml
<dependency>
    <groupId>com.EcommerceSystemTemplate</groupId>
    <artifactId>PublicDependencies</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

需要添加的依赖项和结构调整如下：

- mybatis-plus-boot-starter 3.2.0
- lombok 1.18.8
- Apache HttpCore 4.4.16
- commons-lang 2.6
- mysql drive 8.0.33
- servlet-api 2.3
- jakarta.validation-api 3.0.2
- 对于分页和查询等功能，可以在另一个模块 `renren-fast` 的 `common` 包中找到类似 `PageUtils`、`Query`、`R` 和 `SQLfilter` 的类。请直接复制这些类。
- 此外，由于项目自动添加了权限控制注解 `RequiresPermissions`，目前不需要此注解，因此需要调整逆向工程过程。具体来说，在 `renren-generator` 模块的 `resources-template-Controller.java.vm` 中将该注解注释掉。

完成依赖配置后，每个微服务需要进一步配置自己的数据源，使用 `mybatis-plus` 的 `@MapperScan` 注解，指定 SQL 文件映射的位置等。详细信息请参考每个微服务中的 `application.yml` 文件。

<br>

#### Nacos （发现中心）

要在 `PublicDependencies` 模块中配置 Nacos，首先需要导入相应的依赖项：

```xml
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>

```


Nacos有自己的中间件，Nacos-server需要下载。

从官方网站下载 [nacos-server-2.3.0-BETA.zip](https://github.com/alibaba/nacos/releases/download/2.3.0-BETA/nacos-server-2.3.0-BETA.zip) 版本，请确保使用的 Spring Boot 版本大于 2.7.15，并且已设置 Java 环境变量。下载后，解压文件并运行 Bin 文件夹中的 `startup.cmd` 脚本。

此时，可能会遇到错误：`org.springframework.context.ApplicationContextException: Unable to start web server; nested exception is org.springframework.boot.web.server.WebServerException: Unable to start embedded Tomcat`，导致崩溃。你可以在日志文件中找到问题的详细信息。

此错误发生是因为默认启动模式设置为集群。解决方案是切换到独立模式。为此，在当前目录运行以下命令：

```shell
startup.cmd -m standalone
```

或者，你可以直接修改 `startup.cmd`，添加以下行，以避免每次执行此命令的需求。

然后，在每个微服务的配置文件中添加以下配置：

```yaml
spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848
spring.cloud.application.name=...
```

要启用服务注册和发现功能，使用 `@EnableDiscoveryClient` 注解。将此注解放置在应用程序的 **main** 函数上，然后启动服务。

现在，你可以通过访问 `http://127.0.0.1:8848/nacos` 来验证 Nacos 服务器是否成功启动。默认的用户名和密码都设置为 `nacos`。

你还可以直接下载我项目中存储的 [nacos 文件夹](https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template/tree/5de76cb9e651c8ca79420374efd360b11d1b2e40/nacos)。之后，在 'bin' 目录中双击 `startup.cmd` 文件以启动服务。



#### Nacos （配置中心）

配置 Nacos 配置中心还需要在 `publicDependencies` 微服务中导入以下包：

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
```

然后，在每个微服务的 `/src/main/resources/application.yaml` 配置文件中，配置 Nacos Config 地址并导入服务配置：

```yaml
spring:
  cloud:
    nacos:
      serverAddr: 127.0.0.1:8848
  config:
    import:
      - nacos:nacos-config-example.properties?refresh=true
```

在这里，与我们之前为配置中心配置的内容类似。

配置完成后，打开 `127.0.0.1:8848/nacos`，进入配置管理部分（`configurations`），然后创建一个新的配置。通常，在 **Data ID** 栏填入 `nacos-config-example.properties`，**Group** 栏填入 `DEFAULT_GROUP`。

然后，将配置的格式更改为 `properties`，并在配置内容中填入需要更新的内容。这样，你就可以在 Nacos 中一次性应用多个配置到不同的微服务中。

示例如下：

<img src="https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template/raw/f50a28ecef002c0e2d1f30a7660b9068f9886c45/Static/nacos-config.png" style="zoom: 50%;" />

最后，点击 `Publish`，***然后给需要动态获取配置值的类加上 @RefreshScope 注解***。之后，就可以动态获取到配置值了。

通过这种方式修改配置，更改将在每个微服务中生效，而且这种更改是动态生效的，无需重新启动服务。

<br/>

**设置命名空间和配置组**

正式开发中，将使用每个微服务的名称作为命名空间，例如coupon 命名空间将被用于coupon微服务的配置文件，使用配置分组来区分环境，例如开发分支将会使用DEV group。

然后把原有的`application.properties` 中的内容迁移到 naocs上，例如：

![image-20231203181135230](https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template/raw/2a9178a24f290885048611b19fa35e35779f96a3/Static/nacos-config2.png)

然后新增 `bootstrap.properties` 文件，按照如下配置：

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

现在，可以把原有的 application.yml中的内容注释掉，分别放在 datasource.yml，mybatis.yml 和 other.yml 里面了。之后如果配置文件有变动，只需要更改nacos中的内容即可。

另外，值得注意的是，现在直接启动是会失败的，因为springcloud 2020.0.2版本中把bootstrap的相关依赖从spring-cloud-starter-config中移除了，所以现在在2020.02 以后需要单独引入spring-cloud-starter-bootstrap 依赖：

```xml
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bootstrap</artifactId>
            <version>3.0.3</version>
        </dependency>

```

 **application.yml 的内容将会注释掉但是保持同步更新，用于向大家展示内容。**

<br/>



#### 使用 Feign 进行声明式远程调用

在微服务架构中，远程调用是至关重要的，而 Feign 作为一种声明式的 HTTP 客户端，旨在简化这个过程。Feign 提供了 HTTP 请求的模板，通过创建简单的接口并添加注解，可以定义参数、格式、地址等细节。

Feign 与 **Ribbon**（负载均衡）和 **Hystrix**（熔断器）无缝集成，消除了显式管理这些组件的需求。

Spring Cloud Feign 在 Netflix Feign 的基础上扩展了对 Spring MVC 注解的支持。通过它的实现，只需创建一个接口并配置相应的注解，就可以与服务提供者的接口绑定。这种简化减少了构建自定义服务调用客户端所需的开发工作，与 Spring Cloud Ribbon 采用的方法相比更为简单。

**为了引入这一功能，建立每个微服务时需要引入以下依赖项：**

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

然后，为了启用远程服务调用，你需要编写一个接口，通知 Spring Cloud 该接口需要进行远程服务调用。

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

根据上述提供的代码，我在 Member 微服务中实现了对 Coupon 微服务方法的远程调用。这涉及填写 `FeignClient` 注解中的远程服务的名称（即在 Nacos 中注册的名称），并指定方法的完整路径和签名。

最后，在 Member 微服务的主类 `MemberApplication` 中添加上述接口的注解。其后的 `basePackages` 应该是你创建 `CouponFeignService` 接口的包路径。

```
@EnableFeignClients(basePackages = "com.ecommercesystemtemplate.member.entity")
```

 

#### GateWay

网关的常用功能包括路由转发、权限校验、限流控制、API管理等。这里使用的是Spring Cloud Gateway作为网关，取代Zuul网关。

网关功能需要导入依赖:

```xml
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>
```

如果要使用网关，需要开启服务注册发现，因此需要往 `GatewayApplication` 添加注解：  `@EnableDiscoveryClient`，然后配置nacos相关的注册中心地址，这里参照前文的nacos 配置中心配置即可。

另外这里注意，由于我们直接使用了 PublicDependencies 的所有依赖，因此会引入 mybatis 的配置，但是gateway 暂时不需要，因此可以在 @SpringBootApplication 注解增加 `(exclude = {DataSourceAutoConfiguration.class})`排除数据库配置。





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

## 数据库设计

**(由于这是一个电子商务项目，我们将不会建立外键，以避免潜在的性能影响，因为电子商务数据库通常需要处理大量的数据。)**

接下来将建立以下数据库：

字符集将设置为utf8mb4，以确保与utf8的兼容性并解决与字符编码相关的潜在问题。

您可以在此处找到CREATE TABLE语句的详细信息：<a href="https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template/tree/777679015934b1f745a7cd55b6e66a884eace26e/Static">Github</a>

- **OMS（订单管理系统）：**
  - oms_order - 订单信息
  - oms_order_item - 订单详情
  - oms_order_operate_history - 订单操作历史
  - oms_order_return_apply - 订单退货申请
  - oms_order_return_reason - 退货原因
  - oms_order_setting - 订单配置信息
  - oms_payment_info - 付款信息表
  - oms_refund_info - 退款信息


<br>

- **PMS（产品管理系统）：**
  - pms_attr - 产品属性
  - pms_attr_attrgroup_relation - 属性与属性组关联
  - pms_attr_group - 属性分组
  - pms_brand - 品牌
  - pms_category - 商品三级分类
  - pms_category_brand_relation - 品牌分类关联
  - pms_comment_replay - 产品评价回复关系
  - pms_product_attr_value - spu属性值
  - pms_sku_images - sku图片
  - pms_sku_info - sku信息
  - pms_sku_sale_attr_value - sku销售属性与值
  - pms_spu_comment - 产品评价
  - pms_spu_images - spu图片
  - pms_spu_info - spu信息
  - pms_spu_info_desc - spu信息介绍


<br>

- **WMS（仓库管理系统）：**
  - wms_purchase - 采购信息
  - wms_purchase_detail - 采购明细
  - wms_ware_info - 仓库信息
  - wms_ware_order_task - 库存工作单
  - wms_ware_order_task_detail - 库存工作单明细
  - wms_ware_sku - 商品库存


<br>

-  **SMS（优惠券管理系统）：**
  - sms_coupon - 优惠券信息
  - sms_coupon_history - 优惠券历史
  - sms_coupon_spu_category_relation - 优惠券分类关联
  - sms_coupon_spu_relation - 与产品关联的优惠券
  - sms_home_adv - 首页轮播广告
  - sms_home_subject - 首页专题表 [每个专题链接到一个新页面以显示专题产品信息]
  - sms_home_subject_spu - 专题产品
  - sms_member_price - 产品会员价格
  - sms_seckill_promotion - 秒杀活动
  - sms_seckill_session - 秒杀活动场次
  - sms_seckill_sku_notice - 秒杀商品通知订阅
  - sms_seckill_sku_relation - 秒杀商品关联
  - sms_sku_full_reduction - 产品满减信息
  - sms_sku_ladder - 商品阶梯价格
  - sms_spu_bounds - 产品spu积分设置

<br>

- **UMS（会员管理系统）：**
  - ums_growth_change_history - 成长值变化历史
  - ums_integration_change_history - 积分变化历史
  - ums_member - 会员
  - ums_member_collect_spu - 会员收藏的商品
  - ums_member_collect_subject - 会员收藏的专题
  - ums_member_level - 会员等级
  - ums_member_login_log - 会员登录记录
  - ums_member_receive_address - 会员收货地址
  - ums_member_statistics_info - 会员统计信息







<br>

##  致谢

本项目基于 "<a href="https://www.bilibili.com/video/BV1np4y1C7Yf?p=1&vd_source=06f42aa8b3e4f798e9414658938e7773">Guli Mall</a>" 进行了改进和优化。感谢那些创建并贡献了该项目的个人。















