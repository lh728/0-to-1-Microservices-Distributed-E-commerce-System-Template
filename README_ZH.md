# 从0到1的微服务分布式电子商务系统模板

SpringBoot + Vue2 + Maven3 + Java17 + Spring Cloud + Redis + Redisson + Docker + OSS + Mysql + MybatisPlus + Nginx + Git + Unit Testing + OAuth2 +  Spring Cache

这是一个正在构建中的基于微服务的分布式电子商务系统模板，旨在利用各种先进的管理工具和实践，从零到一实现应用监控、限流、网关、熔断降级等分布式方案；分布式事务、分布式锁；高并发、线程池、异步编排；压力测试与性能优化；集群技术；CI/CD。

详细架构图如下所示：

<img src="https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template/raw/369600d596bca82050201cbebbc9395aee96a699/Static/microservice%20architecture.png" style="zoom: 50%;" />

**我将尽可能使用高版本的编程语言和依赖环境。**

- 后端管理系统前后端分离开发，内外网部署，前端应用和Web，内网部署后端集群，商城页面通过NGINX实现动静分离。
- 已实现功能包括：
  - 商品服务：管理系统库存、品牌、商品、商品属性、商品详情、支付、优惠管理等；商城系统主页面渲染，相关远程服务调用。
  - 用户服务，包括用户档案、收货地址等；
  - 仓储服务，管理商品库存、秒杀等；
  - 订单服务，处理订单操作；
  - 检索服务，集成Elasticsearch进行商品检索；
  - 集中认证服务，包括验证码发送、登录、注册、社交登录等；
- 性能优化：中间件网络交互优化、DB Mysql优化、模板渲染速度优化（缓存）、业务逻辑优化

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

ElasticSearch 7.4.2

Kibana 7.4.2

<br>



### 依赖

详情见PublicDependencies包下的pom.xml文件

Springboot 2.7.17

Spring Web

Spring loadbalancer

Elasticsearch Clients

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

Spring Cloud Alibaba - **OSS** （云存储）

<br>



### 虚拟机

在 Windows 上使用 **VirtualBox** 下载一个基于 Linux 的虚拟机（确保启用了 CPU 虚拟化模式）。

通过 Vagrant 下载官方镜像，使用 `vagrant init centos/7` 命令创建一个 Linux 虚拟机，并修改 Vagrantfile 网络设置（将私有网络更改为计算机的 IP 地址以进行域名映射；可以使用 '`ipconfig`' 命令找到你的 IP 地址）。

<br>

#### 安装 Git

```shell
sudo yum install git

# verify that git is working properly
git --version

```

<br>

#### 安装 Docker

```shell
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

```shell
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

# set Start automatically at boot
docker update mysql-container --restart=always

# Set the character encoding (if needed). Create a my.cnf file in /mydata/mysql/conf using vi.
[mysqld]
character-set-server=utf8

[client]
default-character-set=utf8

```

<br>

#### 安装 Redis

```shell
# Pull the Redis image
docker pull redis

# Run the Redis container
docker run -p 6379:6379 --name redis -v /mydata/redis/data:/data \
-v /mydata/redis/conf/redis.conf:/etc/redis/redis.conf \
-d redis redis-server /etc/redis/redis.conf

# Verify if the Redis container is running
docker ps

# set Start automatically at boot
docker update redis --restart=always

# Interact with Redis (optional)
docker exec -it redis redis-cli

# set Start automatically at boot
docker update redis --restart=always

# The latest Redis versions have persistence enabled by default, so you don't need to modify the configuration file for now. 

```

<br>

#### 安装Elasticsearch和Kibana

```shell
# Pull the Elasticsearch image
docker pull elasticsearch:7.4.2

# add max_map_count number
sudo sysctl -w vm.max_map_count=262144
echo "vm.max_map_count=262144" | sudo tee -a /etc/sysctl.conf

# Pull the Kibana image
docker pull kibana:7.4.2

#create dir
mkdir -p /mydata/elasticsearch/config
mkdir -p /mydata/elasticsearch/data
echo "http.host: 0.0.0.0" >> /mydata/elasticsearch/config/elasticsearch.yml
chmod -R 777 /mydata/elasticsearch/

# run Elasticsearch, attention S_JAVA_OPTS="-Xms64m -Xmx128m" only for test
docker run --name elasticsearch -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" -e ES_JAVA_OPTS="-Xms64m -Xmx512m" -v /mydata/elasticsearch/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml -v /mydata/elasticsearch/data:/usr/share/elasticsearch/data -v /mydata/elasticsearch/plugins:/usr/share/elasti
csearch/plugins -d elasticsearch:7.4.2

# Verify if the Elasticsearch container is running
docker ps

#test Elasticsearch
curl -X GET "localhost:9200/"

# run kibana
docker run --name kibana -e ELASTICSEARCH_HOSTS=http://<your own vm address>:9200 -p 5601:5601 -d kibana:7.4.2

# check Elasticsearch and kibana
docker ps

# set Start automatically at boot
docker update elasticsearch --restart=always
docker update kibana --restart=always
```

接着你可以通过你的虚拟机端口号+9200访问Elasticsearch，注意启动很慢，需要耐心等待

<br>

#### 安装Nginx

```shell
# run nginx container
docker run -d -p 80:80 --name nginx -v /mydata/nginx/html:/usr/share/nginx/html -v /mydata/nginx/logs:/var/log/nginx -v /mydata/nginx/conf:/etc/nginx nginx

# check nginx 
docker ps

# set Start automatically at boot
docker update nginx --restart=always
```

nginx会用于后续反向代理和负载均衡。如果需要模拟域名搭建，可以修改C:\Windows\System32\drivers\etc 下的hosts文件，在底部增加一行即可： `192.168.56.10  thellumall.com`左边是你的虚拟机地址，右边是你设置的域名。

如果后续需要继续添加，往下面继续添加即可，例如：`192.168.56.10  search.thellumall.com`

后续会通过反向代理，将指向thellumall.com的服务转向微服务中的网关。配置如下：

```shell
cd mydata/nginx/conf/conf.d
# copy default configuration
cp default.conf thellumall.conf
vi thellumall.conf

cd ../
vi nginx.conf
```

进入thellumall.conf，修改server_name为*.thellumall.com（后面是你的域名）再用空格分隔开域名，删除location大括号中的内容，改为 如下所示：

```shell
server {
    listen       80;
    listen  [::]:80;
    server_name  *.thellumall.com thellumall.com;

    #access_log  /var/log/nginx/host.access.log  main;

    location / {
        proxy_set_header Host $host;
        proxy_pass http://thellumall;
    }

    ...
}

```

然后进入nginx.conf，增加上游服务器，即修改http内容为如下：

```shell
http {
    include       /etc/nginx/mime.types;
    default_type  application/octet-stream;

    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';

    access_log  /var/log/nginx/access.log  main;

    sendfile        on;
    #tcp_nopush     on;

    keepalive_timeout  65;

    #gzip  on;
    upstream thellumall{
      server 192.168.56.1:88;
    }

    include /etc/nginx/conf.d/*.conf;
}

```

通过这种方式，可以反向代理到网关，网关配置后再指向微服务。 

同时，为了实现动静分离，因此将所有项目的静态资源都储存在Nginx中，优化服务器吞吐量；（指定路径下/static/**的请求直接返回静态资源)：

```shell
cd /mydata/nginx/html
# create static folder to save all static resource
mkdir static
```

接着把各个微服务/static/**下的静态资源放入该文件夹。

```shell
cd /mydata/nginx/conf/conf.d
vi thellumall.conf
```

进入thellumall.conf，新增location /static/如下：

```shell
    listen       80;
    listen  [::]:80;
    server_name  thellumall.com;

    #access_log  /var/log/nginx/host.access.log  main;
    location /static/{
       root /usr/share/nginx/html;
    }

    location / {
        proxy_set_header Host $host;
        proxy_pass http://thellumall;
    }

```



#### 安装RabbitMQ

```shell
docker run -d --name rabbitmq -p 5671:5671 -p 5672:5672 -p 4369:4369 -p 25672:25672 -p 15671:15671 -p 15672:15672 rabbitmq:management

# set Start automatically at boot
docker update rabbitmq --restart=always
```

其中，

- 5671:5671: AMQP 协议的 SSL/TLS 端口
- 5672:5672: AMQP 协议的非 SSL/TLS 端口
- 4369:4369: Erlang Port Mapper Daemon（EPMD）端口，用于节点间通信
- 25672:25672: Erlang 节点间通信端口
- 15671:15671: 管理界面的 SSL/TLS 端口
- 15672:15672: 管理界面的非 SSL/TLS 端口





<br>

## 管理系统

对于后端管理，我们将直接使用现有的 "renren-fast" 以节省时间。

该项目的 GitHub 仓库可以在以下地址找到：https://github.com/renrenio/renren-fast.git

对于前端页面，我们使用 "renren-fast-vue" 来实现快速开发：https://github.com/renrenio/renren-fast-vue.git。该项目的前端代码在这里：<a href="https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template-front-end">0-to-1-Microservices-Distributed-E-commerce-System-Template-front-end </a>。

要启动管理服务，需要创建一个名为 "ADMIN" 的数据库。该数据库的表创建语句和对应的数据生成可以在项目的Static/admin/db文件夹找到。

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

- 请注意，在启动前端之后，需要修改目录文件 `/static/config/index.js`，添加语句 `window.SITE_CONFIG['baseUrl'] = '本地API接口请求地址';`（例如'http://localhost:8080/renren-fast'）。注意，实际项目中会使用 'http://localhost:88/api' 网关作为地址。

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

另外，PublicDependencies 还会存放一些公共信息，例如错误异常码、分组校验信息等等。



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

 

### GateWay

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

为了前后端相应，需要修改前端为'http://localhost:88/api'网关作为api接口请求地址，因此需要在renren-fast微服务中也导入PublicDependencies 依赖。

导入依赖后，还需要在nacos配置，即在renren-fast的 `application.yml` 中写明`application.name`和`nacos.discovery.server-addr`，然后在`RenrenApplication`上加`@EnableDiscoveryClient`注解。

再导入gson依赖，我这里导入的是 2.8.5

还需要排除冲突的依赖：

```xml
		<dependency>
			<groupId>com.EcommerceSystemTemplate</groupId>
			<artifactId>PublicDependencies</artifactId>
			<version>0.0.1-SNAPSHOT</version>
			<exclusions>
				<exclusion>
					<groupId>org.projectlombok</groupId>
					<artifactId>lombok</artifactId>
				</exclusion>
				<exclusion>
					<groupId>org.springframework.cloud</groupId>
					<artifactId>spring-cloud-starter-loadbalancer</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
```

当然，也需要修改网关的配置：（可以直接参考项目内文件）**注意微服务项目的网关需要在admin前面**

```yaml
        - id: product_route
          uri: lb://product
          predicates:
            - Path=/api/product/**
          filters:
            - RewritePath=/api/?(?<segment>.*), /$\{segment}

        - id: admin_route
          uri: lb://renren-fast
          predicates:
            - Path=/api/**
          filters:
            - RewritePath=/api/?(?<segment>.*), /renren-fast/$\{segment}
```

修改完成后，还需要解决CROS跨域问题，所以需要在gateway module单独写一个类用于解决该问题：

```java
@Configuration
public class CorsConfig {
    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // 1. config allow origin
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.setAllowCredentials(true);

        source.registerCorsConfiguration("/**",corsConfiguration );
        return new CorsWebFilter(source);
    }
}
```

另外还需要注释掉原本的renren-fast项目的配置（renren-fast/src/main/java/io/renren/config/CorsConfig.java）：

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//            .allowedOriginPatterns("*")
//            .allowCredentials(true)
//            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//            .maxAge(3600);
//    }
}
```

<br>

### 第三方服务

#### OSS

由于地域问题，目前选择使用阿里云开通OSS对象存储，将作为分布式系统的云文件存储器使用，存储图片等信息。（也可以使用AWS S3)

测试阶段，读写权限选择的公共读（对文件写操作需要进行身份验证，可以对文件进行匿名读），未开通版本控制，服务端未加密，也未开通实时日志查询和HDFS服务。

你可以参考下图：

<img src="https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template/raw/c1e9f9be829f4cffc694bcba5e0209531082ed80/Static/oss.png" style="zoom:50%;" />

完成后，就可以通过OSS进行文件读写了。

（当然，还需要配置accessKey之类的相关信息，这一部分就不赘述了，可以去官网查看，有详细教程）

为了支持服务端签名后直传，即跳过向自己的服务器发送请求，需要 [修改CORS支持跨域](https://help.aliyun.com/zh/oss/use-cases/java-1?spm=a2c4g.11186623.0.0.2b415d03FeF1lg)。，再创建一个微服务Third-Party，专门用于管理第三方服务。

该项目需要导入依赖和依赖管理：

```xml
		<dependency>
			<groupId>com.EcommerceSystemTemplate</groupId>
			<artifactId>PublicDependencies</artifactId>
			<version>0.0.1-SNAPSHOT</version>
			<exclusions>
				<exclusion>
					<groupId>com.baomidou</groupId>
					<artifactId>mybatis-plus-boot-starter</artifactId>
				</exclusion>
			</exclusions>
		</dependency>        

		<dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alicloud-oss</artifactId>
            <version>2.2.0.RELEASE</version>
        </dependency>
```

然后增加配置：

```yaml
spring:
  cloud: 
    alicloud:
      access-key: <your access key>
      secret-key: <your secret key>
      oss:
        endpoint: oss-cn-beijing.aliyuncs.com
    util:
      enabled: false
```

需要注意，在Nacos配置对应的命名空间、服务发现等，这部分内容和微服务配置是一样的，只不过不需要配置mybatis-plus。

之后，设置专门的Controller类用于管理OSS：

```java
@RestController
public class OssController {

    @Resource
    OSSClient ossClient;

    @Value("${spring.cloud.alicloud.oss.endpoint}")
    private String endpoint;

    @Value("${spring.cloud.alicloud.oss.bucket}")
    private String bucket;

    @Value("${spring.cloud.alicloud.access-key}")
    private String accessId;

    @RequestMapping("/oss/policy")
    public Map<String, String> policy() {

        // https://0-to-1-microservices-distributed-e-commerce-system-template.oss-cn-beijing.aliyuncs.com/test.png
        // format https://bucketname.endpoint。
        String host = "https://" + bucket + "." + endpoint;
        /*
        Set up the upload callback URL, which is the callback server address used for communication between the application server and OSS.
        After the file upload is complete, OSS will send the file upload information to the application server through this callback URL.
         */
//        String callbackUrl = "https://192.168.0.0:8888";
        // Set the prefix for uploading files to OSS; this field can be left empty.
        // When left empty, files will be uploaded to the root directory of the Bucket.
        String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String dir = format + "/";

        Map<String, String> respMap = null;
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            respMap = new LinkedHashMap<String, String>();
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            // respMap.put("expire", formatISO8601Date(expiration));

        } catch (Exception e) {
            // Assert.fail(e.getMessage());
            System.out.println(e.getMessage());
        } finally {
            ossClient.shutdown();
        }

        return respMap;
    }
}
```

最后，修改网关配置即可：

```yaml
        - id: third_party_route
          uri: lb://thirdParty
          predicates:
            - Path=/api/thirdparty/**
          filters:
            - RewritePath=/api/thirdparty/?(?<segment>.*), /$\{segment}
```

注意这里gateway的yml中配置负载均衡（lb://)，一定写的是nacos中自定义的服务名称



#### 手机验证码

由于地域问题，目前选择使用阿里云开通云市场，以支持发送手机验证码的功能。

详细内容可以参考：https://marketplace.alibabacloud.com/?spm=a3c0i.7911826.6791778070.33.64ca3870uyf5vW#!

购买成功后可以获得对应的短信验证码接口，具体支持的API接口详情见购买后内容。举例而言，我选择的API调用的请求地址为：http://gyytz.market.alicloudapi.com，通过POST请求调用，通过APPCODE的方式进行身份验证。

详细代码参见ThirdParty服务内置代码：

```java
@Component
@Data
@ConfigurationProperties(prefix = "spring.cloud.alicloud.sms")
public class SmsComponent {

    private String host;
    private String path;
    private String smsSignId;
    private String templateId;
    private String appcode;
    public void sendSms(String phoneNumber, String message) {
        String method = "POST";
        String appcode = "your own APPcode";
        Map<String, String> headers = new HashMap<String, String>();
        //header format: Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phoneNumber);
        // message : "**code**:12345,**minute**:5"
        querys.put("param", message);
        querys.put("smsSignId", smsSignId);
        querys.put("templateId", templateId);
        Map<String, String> bodys = new HashMap<String, String>();

        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

该方法提供SmsComponent以供其他微服务使用发送验证码功能。



### ELSearch

主要用于商品检索，有关Product数据库的建索引语句如下：

```json
PUT product
{
  "mappings": {
    "properties": {
      "skuId": {
        "type": "long"
      },
      "spuId": {
        "type": "keyword"
      },
      "skuTitle": {
        "type": "text"
      },
      "skuPrice": {
        "type": "keyword"
      },
      "skuImage": {
        "type": "keyword"
      },
      "saleCount": {
        "type": "long"
      },
      "hasStock": {
        "type": "boolean"
      },
      "hotScore": {
        "type": "long"
      },
      "brandId": {
        "type": "long"
      },
      "catalogId": {
        "type": "long"
      },
      "brandName": {
        "type": "keyword"
      },
      "brandImg": {
        "type": "keyword"
      },
      "catalogName": {
        "type": "keyword"
      },
      "attrs": {
        "type": "nested",
        "properties": {
          "attrId": {
            "type": "long"
          },
          "attrName": {
            "type": "keyword"
          },
          "attrValue":{
            "type": "keyword"
          }
        }
      }
    }
  }
}
```



这里除了elasticSearch相关的配置与远程调用之外，还有关于商城搜索页的前端代码，和首页跳转搜索页的部分逻辑代码。

商城搜索页如下所示：





这里实现了当搜索商品时，相关的业务和代码逻辑，包括与ES进行交互的信息。



### AuthServer

这里是有关安全认证的微服务。主要用于注册页和登录页相关的业务逻辑，存储密码采用的MD5+盐值加密，手机验证码远程调用ThridParty服务的接口，还涉及整合SpringSession对分布式session处理，以处理登录问题。

注册页如下：



登录页如下：



同时还使用OAuth 2.0 支持社交网站登录。目前支持的登录方式只有微博，可以基于此功能进行其他社交网站登录功能的开发，流程都差不多。处理思路大致如下：

<img src="C:\Users\lhjls\AppData\Roaming\Typora\typora-user-images\image-20240422102826744.png" alt="image-20240422102826744" style="zoom: 50%;" />









<br>

### Order

<br>

### Member

#### Membership Level

Membership Level的路由是member/level。该功能用于管理会员等级，可以在这里设置不同会员等级和特权等信息。

该页主要信息存储同Product微服务一样，采取动静分离策略，把静态资源放在nginx中，对应的前端内容放置在Static文件夹下，使用thymelaef作为模板引擎，已经注释掉仅供展示。

搜索页如下所示：





<br>

### Product

商品系统通过后台管理系统新增Product System目录

#### Category maintenance

Category maintenance的路由是product/category。该配置用于管理商品服务三级分类下，一次性查出所有分类与子分类，并以树数据结构组装起来进行管理，并支持append, delete，batch delete和update功能。

该处功能通过Spring cache缓存优化，并解决了缓存穿透、击穿和雪崩问题。

该页主要信息存储在pms_category表中

<img src="https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template/raw/e506621a17f5208b5685663765bbde960a9a9305/Static/product_maintenance.png" style="zoom: 50%;" />

<br>

#### Brand Management

Brand Management的路由是product/brand。还配置用于管理电商品牌，支持增删改查分页显示，并且与分类信息做关联（即品牌与分类有关联，关联信息保存在pms_category_brand_relation 表中）

同时，通过修改brand URL，支持分布式条件下文件上传与读取，文件上传到OSS云服务器进行读写。

实现方式是服务端签名后直传，即上传前向服务器索要名牌与签名，然后向阿里云提交请求。通过这种方式跳过了向自己的服务器发送请求，占用资源。

前端配置bucket外网域名后，即可支持单文件和多文件上传，单文件上传功能是通过输入 logo address 完成的。

该页主要信息存储在pms_brand表中



<img src="https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template/raw/722c94adf20cc6e55752cbebe8c1488c4bf7a89c/Static/brand_management2.png" style="zoom:50%;" />

<img src="https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template/raw/6d891db6976c4e3b62de60f101d999bc41a9cd99/Static/brand_management.png" style="zoom:50%;" />

<br>

#### Platform Properties

平台属性用于维护SPU（Standard Product Unit）和SPK（Stock Keeping Unit）属性，内含子菜单属性分组、规格参数和销售属性三个类别。

- 属性分组

属性分组的路由是product/attrgroup。属性分组主要记录某个类别属于哪个分组。每个属性分组都会关联多个规格参数。

该页需要根据分类树的信息，点击后动态展示对应分类的分组信息，新增时也支持级联选择器选择所属分组。

该页主要信息存储在pms_attr_group表中

- 规格参数

规格参数的路由是product/baseattr。规格参数主要用于记录电商产品的一些基本信息（例如出产年份、核心配置等，这些信息在不同的销售属性下是不变的）。与属性分组一样，根据分类树的信息，点击后动态展示对应分类的基本属性信息。

该页主要信息存储在pms_attr表中。

- 销售属性

销售属性的路由是product/saleattr。销售属性和规格参数都属于 attrType的一种，销售属性主要用于记录电商产品的部分销售属性（例如可以选择手机的白色款、黑色款等）。与属性分组一样，根据分类树的信息，点击后动态展示对应分类的销售属性信息，但是销售属性不需要分组保存。

该页主要信息也存储在pms_attr表中。



#### Product Maintenance

产品维护用于维护产品信息并维护发布的生命周期，内含子菜单SPU管理、发布产品和产品管理三个类别。

- SPU管理

SPU管理的路由是product/spu。SPU管理是管理发布的产品SPU的地方，默认情况下新建的产品状态是NEW，可以执行上架下架操作，还可以查看设置spu详细规格。在详细规格中，可以对spu信息进行更新和确认。

上架功能关联ES数据库，需要存储需要上架的商品信息至ES数据库以供检索。

该页主要信息存储在pms_spu_info中

- 发布产品

发布产品的路由是product/spuadd。发布产品需要先填写基本信息，再填写规格参数和销售属性，然后确认与完善根据销售属性对应生成的笛卡尔积SKU信息，最后才能成功保存。同时，该功能也需要会员和优惠券微服务的支持，因为需要获取到会员的等级信息，以及相关产品信息会保存到优惠券数据库中。

该功能涉及多个表保存以及跨数据库保存信息，例如pms_sku_images, pms_sku_info, sms_sku_full_reduction等等。

- 产品管理

产品管理的路由是product/manager。产品管理是用于管理SKU信息的地方，支持跳转预览、评论、上传图片、参与秒杀、满减设置、折扣设置和会员价格设置等页面，还可以进行检索和展开详情。

该页主要信息存储在sku_info中。

<br>

#### 前端

采取动静分离策略，把静态资源放在nginx中，这样每次请求静态资源，网关就不需要在把[请求转发](https://so.csdn.net/so/search?q=请求转发&spm=1001.2101.3001.7020)到微服务中了，分担了微服务的压力。同时，后期可以直接转为静态资源。

微服务对应的前端内容放置在Static文件夹下，使用thymelaef作为模板引擎。注意这里的内容全都注释掉了，仅用于展示，实际使用只需要解除注释，按照安装Nginx的指示，把静态资源放在nginx中即可（不包括index.html)。

这里存放商城首页，如下图所示：





### Cart

购物车服务，用户可以在登录状态下将商品添加到购物车（用户购物车/在线购物车），用户可以在未登录状态下将商品添加到购物车（游客购物车/离线购物车/临时购物车）。同时登录后，这些商品也会添加进在线购物车中，然后清空临时购物车。

除此之外，还支持功能：

用户可以使用购物车一起结算下单
给购物车添加商品
用户可以查询自己的购物车
用户可以在购物车中修改购买商品的数量。
用户可以在购物车中删除商品。
选中不选中商品
在购物车中展示商品优惠信息提示购物车商品价格变化

购物车页面如下：



### WareHouse

#### Warehouse Maintenance

仓储维护的路由是ware/wareinfo。仓储维护主要用于管理仓储相关的信息，并支持对应CRUD操作。

该页主要信息存储在wms_ware_info表中。



#### Product Inventory

产品仓储的路由是ware/sku。产品仓储主要用于管理每个仓库下面的仓储信息。该功能关联采购功能，库存由采购完成后自动显示。

该页主要信息存储在wms_ware_sku表中。



#### Purchase Order Management

- 采购需求

采购需求的路由是ware/purchaseitem。采购需求主要用于管理采购SKU相关信息，确定采购商品ID、数量等信息，同时支持采购单合并操作。

该页主要信息存储在wms_purchase_detail表中。

- 库存工作单

库存工作单的路由是ware/task。



- 采购单

采购单的路由是ware/purchase。用于生成采购单并管理，支持CRUD操作，并且关联采购需求页面。

该页主要信息存储在wms_purchase表中。

- 领取采购单

该功能不属于后台管理系统，可以配置给采购人员，通过手机或其他方式对该接口发起请求，请求发送给`/api/warehouse/purchase/received`，可以用postman模拟请求发送。用于给采购人员领取采购单。



<br>

### Coupon

<br>

## 数据库设计

**(由于这是一个电子商务项目，我们将不会建立外键，以避免潜在的性能影响，因为电子商务数据库通常需要处理大量的数据。)**

接下来将建立以下数据库：

字符集将设置为utf8mb4，以确保与utf8的兼容性并解决与字符编码相关的潜在问题。

您可以在此处找到CREATE TABLE语句和插入语句的详细信息：<a href="https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template/tree/777679015934b1f745a7cd55b6e66a884eace26e/Static">Github</a>

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















