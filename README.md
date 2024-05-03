# 0-to-1-Microservices-Distributed-E-commerce-System-Template
SpringBoot + Vue2 + Maven3 + Java17 + Spring Cloud + Redis + Docker + OSS + Mysql + MybatisPlus + Nginx + Git + Unit Testing + OAuth2 +  Spring Cache

This is an ongoing construction of a microservices-based distributed e-commerce system template, aimed at leveraging various advanced management tools and practices from 0 to 1, to achieve distributed solutions such as application monitoring, network limiting, gateways, circuit breaker degradation, etc.; distributed transactions, distributed locks; high concurrency, thread pools, asynchronous orchestration; stress testing and performance optimization; cluster technology; and CI/CD

<img src="https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template/raw/369600d596bca82050201cbebbc9395aee96a699/Static/microservice%20architecture.png" style="zoom: 50%;" />

<br>

For the Chinese version of this project, click [中文版本](https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template/blob/37da94e53a7ca79a9574ef6534c75d3aaa2c1b4d/README_ZH.md).

**I will strive to use various high-version programming languages and dependencies.**

<br>

- Backend management system developed with front-end and back-end separation, deployed for both intranet and internet, front-end applications and web, backend cluster deployment for intranet, e-commerce pages achieving dynamic/static separation via NGINX.
- Implemented functionalities include
  - Product service, which involves inventory management, brand management, product management, product attributes, product details, payment processing, and discount management. Rendering of the main pages of the e-commerce system and related remote service calls.
  - User service, covering user profiles, shipping addresses, etc.
  - Warehousing service, managing product inventory, flash sales, etc.
  - Order service, handling order operations.
  - Search service, integrating Elasticsearch for product searches.
  - Centralized authentication service, including features like login, registration, single sign-on, social login, etc.

<br>





<br>



## Project Setup Guide

### **Environment**

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

### Dependency

**Please refer to the pom.xml file under the PublicDependencies package for details.**

Springboot 2.7.17

Spring Web

Spring loadbalancer

Elasticsearch Clients

<br>

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

Spring Cloud Alibaba - **OSS** (Cloud Storage)



<br>

### Virtual Machine

Download a Linux virtual machine based on Windows using **VirtualBox** (Make sure to enable CPU virtualization mode).

<br>

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

#### Install Elasticsearch和Kibana

```bash
# Pull the Elasticsearch image
docker pull elasticsearch:7.4.2

# Run the Elasticsearch container
docker run -d -p 9200:9200 -p 9300:9300 --name elasticsearch docker.elastic.co/elasticsearch/elasticsearch:7.4.2

# Pull the Kibana image
docker pull kibana:7.4.2

# Verify if the Elasticsearch container is running
docker ps

#test Elasticsearch
curl -X GET "localhost:9200/"

#create dir
mkdir -p /mydata/elasticsearch/config
mkdir -p /mydata/elasticsearch/data
echo "http.host: 0.0.0.0" >> /mydata/elasticsearch/config/elasticsearch.yml
chmod -R 777 /mydata/elasticsearch/

# run Elasticsearch, attention S_JAVA_OPTS="-Xms64m -Xmx128m" only for test
docker run --name elasticsearch -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" -e ES_JAVA_OPTS="-Xms64m -Xmx128m" -v /mydata/elasticsearch/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml -v /mydata/elasticsearch/data:/usr/share/elasticsearch/data -v /mydata/elasticsearch/plugins:/usr/share/elasti
csearch/plugins -d elasticsearch:7.4.2

# run kibana
docker run --name kibana -e ELASTICSEARCH_HOSTS=http://<your own vm address>:9200 -p 5601:5601 -d kibana:7.4.2

# check Elasticsearch and kibana
docker ps
```

Continuing with instructions, you can access Elasticsearch by using your virtual machine's IP address or hostname along with port 9200. Please be aware that Elasticsearch might take some time to start, so patience is key during this process.

<br>

#### Install Nginx

```shell
# run nginx container
docker run -d -p 80:80 --name nginx -v /mydata/nginx/html:/usr/share/nginx/html -v /mydata/nginx/logs:/var/log/nginx -v /mydata/nginx/conf:/etc/nginx nginx

# check nginx 
docker ps
```

nginx will be used for reverse proxy and load balancing in the future. If you need to simulate domain hosting, you can modify the hosts file located at C:\Windows\System32\drivers\etc. Simply add a line at the bottom: `192.168.56.10 thellumall.com`, where the left side is your virtual machine address and the right side is the domain you've set.

If you need to add more in the future, simply continue adding below. For example: `192.168.56.10 search.thellumall.com`.

In the future, reverse proxy will be used to redirect services pointing to thellumall.com to the microservice gateway. The configuration is as follows:

```shell
cd mydata/nginx/conf/conf.d
# copy default configuration
cp default.conf thellumall.conf
vi thellumall.conf

cd ../
vi nginx.conf
```

Enter the thellumall.conf file and modify the server_name to `*.thellumall.com` (with your domain name following it), separated by spaces. Delete the contents within the location curly braces and replace it with the following:

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

Then, go to nginx.conf and add the upstream servers. Modify the http section as follows:

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

Using this approach, reverse proxying to the gateway is achieved, and after configuring the gateway, it directs traffic to the microservices.

Simultaneously, to implement dynamic/static resource separation, all project static resources are stored within Nginx to optimize server throughput. Requests targeting the specified path /static/** are directly served with static resources.

```shell
cd /mydata/nginx/html
# create static folder to save all static resource
mkdir static
```

Next, place the static resources of each microservice under the /static/** directory.

```shell
cd /mydata/nginx/conf/conf.d
vi thellumall.conf
```

Enter thellumall.conf，add location /static/ as follows：

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





## Administraion System

For the backend management, we will directly use the existing "renren-fast" to save time.

The project's GitHub repository can be found at the following address: https://github.com/renrenio/renren-fast.git

<br>

For the front-end pages, we use "renren-fast-vue" to achieve rapid development: https://github.com/renrenio/renren-fast-vue.git. 

The frontend code for this project is here: <a href = "https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template-front-end" >0-to-1-Microservices-Distributed-E-commerce-System-Template-front-end </a> .

<br>

To start the administration service, a database named "ADMIN" needs to be created. The table creation statements for this database and the corresponding data generation can be found in the "Static/admin/db" folder of the project.

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

- Please note that, after starting the frontend, you need to modify the directory file `/static/config/index.js` by adding the statement `window.SITE_CONFIG['baseUrl'] = 'local API interface request address';` (for example, 'http://localhost:8080/renren-fast'). Please be aware that in the actual project, the 'http://localhost:88/api' gateway will be used as the address.

<br>

The basic CRUD functionalities of microservices will be generated through the **[renren-generator](https://gitee.com/renrenio/renren-generator)**. This code generator can dynamically produce entity, XML, DAO, service, HTML, JS, and SQL code online, reducing over 70% of development tasks.

<br>

The position of this code generator in the project is as follows: <a href="https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template/tree/3e7badbe5f3cf8acd1607f7e73aeb69f76d2e3a5/renren-generator">renren-generator</a>.

<br>

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

<br>

Therefore, I created the `PublicDependencies` module to store shared dependencies for use by other modules.

<br>

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

In addition, `PublicDependencies` also serves as a repository for various public information, such as error exception codes, group validation details, and more.

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

<br>

To download the [nacos-server-2.3.0-BETA.zip](https://github.com/alibaba/nacos/releases/download/2.3.0-BETA/nacos-server-2.3.0-BETA.zip) version from the official website, please ensure that you are using a Spring Boot version greater than 2.7.15 and that the Java environment variable is set. After downloading, unzip the file and run the `startup.cmd` script located in the Bin folder.

<br>

At this point, you may encounter the error: `org.springframework.context.ApplicationContextException: Unable to start web server; nested exception is org.springframework.boot.web.server.WebServerException: Unable to start embedded Tomcat`, resulting in a crash. You can find the details of the issue in the log files.

<br>

This error occurs because the default startup mode is set to cluster. The solution is to switch to standalone mode. To do this, run the following command in the current directory:

```shell
startup.cmd -m standalone
```

Alternatively, you can modify the `startup.cmd` directly by adding the following line to avoid the need to execute this command every time.

<br>

Afterwards, add the following configuration to the configuration files of each microservice:

```yaml
spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848
spring.cloud.application.name=...
```

To enable service registration and discovery functionality, use the `@EnableDiscoveryClient` annotation. Place this annotation on the **main** function of your application, and then start the service.

<br>

You can now verify if the Nacos server has successfully started by accessing `http://127.0.0.1:8848/nacos`. The default username and password are both set to `nacos`.

<br>

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

<br>

After the configuration is complete, go to `127.0.0.1:8848/nacos`, enter the configuration management section (`configurations`), and then create a new configuration. Typically, fill in the **Data ID** field with `nacos-config-example.properties` and the **Group** field with `DEFAULT_GROUP`.

<br>

Next, change the configuration's format to `properties`, and fill in the content that needs to be updated. This way, you can apply multiple configurations to different microservices at once within Nacos.

<br>

Example as follows:

<img src="https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template/raw/f50a28ecef002c0e2d1f30a7660b9068f9886c45/Static/nacos-config.png" style="zoom: 50%;" />

Finally, click `Publish` and then add the ***@RefreshScope annotation to the class that requires dynamically retrieved configuration values***. Subsequently, you will be able to dynamically obtain the configuration values.

<br>

Modifying the configuration in this manner ensures that the changes take effect in each microservice, and this adjustment dynamically applies without the need to restart the services.

<br/>

**Setting Namespace and Configuration Groups**

In actual development, each microservice's name will be used as a namespace. For example, the "coupon" namespace will be employed for the configuration files of the coupon microservice. Configuration groups are used to differentiate environments; for instance, the development branch will use the "DEV" group.

<br>

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

<br>

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

<br>Feign seamlessly integrates with **Ribbon** for load balancing and **Hystrix** for circuit-breaking, eliminating the explicit need for managing these components.

<br>Spring Cloud Feign extends the support for Spring MVC annotations on the foundation of Netflix Feign. With its implementation, creating an interface and configuring it with annotations is all that's required to bind to the service provider's interface. This simplification reduces the development effort needed to build a custom service invocation client, as opposed to the approach taken by Spring Cloud Ribbon.

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

<br>

Lastly, add the annotation to the main class `MemberApplication` in the Member microservice. The `basePackages` following it should be the package path where you created the `CouponFeignService` interface.

```
@EnableFeignClients(basePackages = "com.ecommercesystemtemplate.member.entity")
```

 

#### GateWay

The common functionalities of a gateway include route forwarding, permission verification, rate limiting, and API management. In this context, Spring Cloud Gateway is utilized as the gateway, replacing the Zuul gateway.

<br>

To enable gateway functionality, the following dependency needs to be added:

```xml
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>
```

To use the gateway, service registration and discovery must be enabled. Therefore, the `GatewayApplication` should be annotated with `@EnableDiscoveryClient`. Additionally, configure the Nacos registry center address, referring to the Nacos configuration center setup mentioned earlier.

<br>

It's worth noting that, since all dependencies from PublicDependencies are directly used, it includes MyBatis configuration, which is currently not required for the gateway. To exclude database configuration, add `(exclude = {DataSourceAutoConfiguration.class})` to the `@SpringBootApplication` annotation.

<br>

To ensure communication between the frontend and backend, you need to modify the frontend to use 'http://localhost:88/api' as the gateway for API interface requests. Therefore, it is necessary to import the 'PublicDependencies' dependency into the 'renren-fast' microservice.

<br>

After importing the dependency, you also need to configure Nacos. This involves specifying `application.name` and `nacos.discovery.server-addr` in the 'application.yml' file of 'renren-fast'. Additionally, add the `@EnableDiscoveryClient` annotation to the `RenrenApplication` class.

<br>

Import the Gson dependency (in this case, version 2.8.5).

<br>

Exclude conflicting dependencies as needed.

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

Certainly, you also need to modify the configuration of the gateway. (You can directly refer to the files in the project.) **Note that the gateway of the microservices project needs to be placed in front of the admin.**

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

After the modification, you also need to address the CORS (Cross-Origin Resource Sharing) issue. Therefore, in the gateway module, you should create a separate class to handle this problem:

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

Additionally, you need to comment out the original configuration in the `renren-fast` project (`renren-fast/src/main/java/io/renren/config/CorsConfig.java`):

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



### Third Party Service

#### OSS

Due to geographical considerations, we have currently opted to use Alibaba Cloud to set up OSS (Object Storage Service) as a cloud file storage for our distributed system, serving as a repository for images and other information. (Alternatively, AWS S3 can also be used.)

<br>

During the testing phase, we have chosen public read permissions (write operations to files require authentication, while files can be anonymously read). Versioning has not been enabled, server-side encryption is not in use, and real-time log querying and HDFS services have not been activated.

You can refer to the diagram below:

<img src="D:\0-to-1-Microservices-Distributed-E-commerce-System-Template\Static\oss.png" alt="image-20231221165409092" style="zoom:50%;" />

Once completed, you can perform file read and write operations through OSS.

(Of course, you'll also need to configure relevant information such as access keys. This part will not be elaborated here; you can refer to the official website for detailed tutorials.)

<br>

To support direct uploads after server-side signing, bypassing the need to send requests to your own server, you need to [modify CORS for cross-origin support](https://help.aliyun.com/zh/oss/use-cases/java-1?spm=a2c4g.11186623.0.0.2b415d03FeF1lg). Next, create a microservice called "Third-Party," dedicated to managing third-party services.

This project requires importing dependencies and dependency management: [Link to Dependencies Documentation]

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

then config:

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

Please note that configuring the corresponding namespace and service discovery in Nacos is similar to configuring microservices, with the exception that MyBatis-Plus configuration is not required.

Afterward, create a dedicated Controller class to manage OSS:

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

        // https://0-to-1-microservices-distributed-e-commerce-system-template.oss-cn-					beijing.aliyuncs.com/test.png
        // format https://bucketname.endpoint。
        String host = "https://" + bucket + "." + endpoint;
        /*
        Set up the upload callback URL, which is the callback server address used for communication between 		the application server and OSS.
        After the file upload is complete, OSS will send the file upload information to the application 			server through this callback URL.
         */
		// String callbackUrl = "https://192.168.0.0:8888";
        // Set the prefix for uploading files to OSS; this field can be left empty.
        // When left empty, files will be uploaded to the root directory of the Bucket.
        String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String dir = format + "/";

        Map<String, String> respMap = null;
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            // PostObject max file 5 GB，CONTENT_LENGTH_RANGE 5*1024*1024*1024。
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

final，update gateway congif：

```yaml
        - id: third_party_route
          uri: lb://thirdParty
          predicates:
            - Path=/api/thirdparty/**
          filters:
            - RewritePath=/api/thirdparty/?(?<segment>.*), /$\{segment}
```

Please be aware that in the Gateway configuration YAML file, when setting up load balancing (lb://), make sure to use the custom service name defined in Nacos.

<br>

#### Mobile Verification Code

Due to geographical constraints, we have opted to utilize Alibaba Cloud's Cloud Marketplace to facilitate the functionality of sending SMS verification codes.

For detailed information, please refer to: https://marketplace.alibabacloud.com/?spm=a3c0i.7911826.6791778070.33.64ca3870uyf5vW#!

Upon successful purchase, access to the corresponding SMS verification code interface is granted. Specific details regarding supported API interfaces are available in the post-purchase content. For example, the request address for the API I've selected is: [http://gyytz.market.alicloudapi.com](http://gyytz.market.alicloudapi.com/), invoked via POST request and authenticated using the APPCODE method.

For detailed code implementation, please refer to the built-in code within the ThirdParty service.

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

This method provides the SmsComponent for other microservices to utilize the functionality of sending verification codes.

<br>

### ELSearch Service

It is mainly used for product search. The ElasticSearch index building statements for the Product database are as follows:

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

In addition to the configuration and remote calls related to elasticSearch, there is also the front-end code for the mall search page and some logic code for the home page to jump to the search page.

The mall search page looks like this:

<br>

Here, the relevant business and code logic is implemented when searching for products, including information for interacting with ES.

<br>

### AuthServer Service

Here is a microservice related to security authentication. It is mainly used for business logic related to the registration page and login page. It uses MD5+ salt encryption to store passwords. The mobile phone verification code remotely calls the interface of the ThridParty service. It also involves integrating SpringSession for distributed session processing to handle login issues.

Registration Page as follows：

<br>

Login Page as follows:

<br>

It also uses OAuth 2.0 to support social networking site login. Currently, the only supported login method is Weibo. You can develop login functions for other social networking sites based on this function. The process is similar. The processing idea is roughly as follows:

<img src="C:\Users\lhjls\AppData\Roaming\Typora\typora-user-images\image-20240422102826744.png" alt="image-20240422102826744" style="zoom: 50%;" />









<br>

### Order Service

<br>

### Member Service

#### Membership Level

The route for Membership Level is member/level. This function is used to manage membership levels. Information such as different membership levels and privileges can be set here.

The main information storage of this page is the same as that of the Product microservice. It adopts the dynamic and static separation strategy, puts the static resources in nginx, and places the corresponding front-end content in the Static folder. It uses thymelaef as the template engine and has been commented out for display only.

The search page looks like this:

<br>

### Product Service

The product system added a new "Product System" directory through the backend management system.

#### Category maintenance

The configuration for `product/category` is designed for managing the three-level classification of the category maintenance service. It aims to retrieve all categories and subcategories in a single query, organize them into a tree data structure for efficient management, and support functionalities such as `append`, `delete`, `batch delete`, and `update`.

The main information of this page is stored in the pms_category table

<img src="https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template/raw/e506621a17f5208b5685663765bbde960a9a9305/Static/product_maintenance.png" style="zoom: 50%;" />

#### Brand Management

The route for Brand Management is configured as `product/brand`. It is designed to facilitate the management of electronic product brands, supporting operations such as adding, deleting, modifying, querying, and paginated display. Additionally, it provides support for file upload and retrieval under distributed conditions.

To achieve this, the brand URL is modified to support file upload and retrieval in a distributed environment. Files are uploaded to the OSS (Object Storage Service) cloud server for read and write operations.

<br>The implementation involves server-side signing for direct upload. Prior to uploading, the client requests the brand name and signature from the server, and then submits the request to Alibaba Cloud. This method bypasses the need to send requests to one's own server, thus conserving resources.

By configuring the external network domain of the bucket on the frontend, support for both single and multiple file uploads is enabled. Single file upload functionality is achieved through entering the logo address.

The main information for this page is stored in the "pms_brand" table.

<img src="https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template/raw/722c94adf20cc6e55752cbebe8c1488c4bf7a89c/Static/brand_management2.png" style="zoom:50%;" />

<img src="https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template/raw/6d891db6976c4e3b62de60f101d999bc41a9cd99/Static/brand_management.png" style="zoom:50%;" />

<br>

#### Platform Properties

Platform attributes are used to maintain attributes for Standard Product Units (SPUs) and Stock Keeping Units (SKUs), which include sub-menu attribute groups, specification parameters, and sales attributes.

- Attribute Groups

The route for attribute groups is `product/attrgroup`. Attribute groups primarily record which category belongs to which group. Each attribute group is associated with multiple specification parameters.

This page dynamically displays group information for the corresponding category based on the information of the category tree when clicked, and supports cascade selection of the parent group when adding.

The main information of this page is stored in the pms_attr_group table.

- Specification Parameters

The route for specification parameters is `product/baseattr`. Specification parameters are mainly used to record some basic information of e-commerce products (such as production year, core configuration, etc., which remain constant under different sales attributes). Similar to attribute groups, the corresponding basic attribute information is dynamically displayed based on the information of the category tree when clicked.

The main information of this page is stored in the pms_attr table.

- Sales Attributes

The route for sales attributes is `product/saleattr`. Sales attributes, like specification parameters, are also a type of attrType. Sales attributes are mainly used to record some sales attributes of e-commerce products (such as selecting white, black, etc., for mobile phones). Similar to attribute groups, the corresponding sales attribute information for the corresponding category is dynamically displayed based on the category tree when clicked, but sales attributes do not need to be saved in groups.

The main information of this page is also stored in the pms_attr table.

<br>

#### Product Maintenance

Product maintenance is used to maintain product information and manage the lifecycle of releases. It includes three categories: SPU Management, Product Publishing, and Product Management.

- SPU Management

The route for SPU management is `product/spu`. SPU management is where released product SPUs are managed. By default, newly created products have a status of NEW, and actions such as listing and delisting can be performed. Additionally, detailed specifications of the SPU can be viewed and configured. Within the detailed specifications, SPU information can be updated and confirmed.

The listing functionality is associated with the ES database, where product information to be listed is stored for retrieval.

The main information of this page is stored in the pms_spu_info table.

- Product Publishing

The route for product publishing is `product/spuadd`. Product publishing involves filling in basic information, followed by specification parameters and sales attributes. Subsequently, the confirmation and completion of generating the Cartesian product SKU information based on the sales attributes is required before successful saving. Additionally, this functionality requires support from the member and coupon microservices as it needs to obtain member level information, and related product information will be stored in the coupon database.

This functionality involves saving information across multiple tables and databases, such as pms_sku_images, pms_sku_info, sms_sku_full_reduction, etc.

- Product Management

The route for product management is `product/manager`. Product management is where SKU information is managed. It supports actions such as previewing, commenting, uploading images, participating in flash sales, setting discounts, setting discounts, and setting member prices, as well as search and expanding details.

The main information of this page is stored in the sku_info table.

<br>

#### FrontEnd

By adopting a dynamic/static separation strategy and storing static resources in Nginx, the gateway no longer needs to forward requests to microservices each time a static resource is requested, thus alleviating the load on the microservices. Additionally, this setup allows for the direct serving of static resources in the future.

The frontend content corresponding to microservices is placed in the Static folder, utilizing Thymeleaf as the template engine. Please note that all content here is commented out for demonstration purposes only. In actual usage, uncomment the content and follow the instructions for installing Nginx to place the static resources in Nginx (excluding index.html).

Here is where the homepage of the online store is stored, as shown in the following diagram:



<br>

### Cart Service

The shopping cart service allows users to add items to their shopping cart while logged in (user cart/online cart) or while not logged in (guest cart/offline cart/temporary cart). Additionally, when a user logs in, these items are also added to the online cart and the temporary cart is then emptied.

Furthermore, the service supports the following functionalities:

- Users can proceed to checkout and place orders using the shopping cart.
- Adding items to the shopping cart.
- Users can view their own shopping carts.
- Users can modify the quantity of items in the shopping cart.
- Users can remove items from the shopping cart.
- Selecting/unselecting items.
- Displaying promotional information for items in the shopping cart to indicate price changes.

The shopping cart page is as follows:

<br>

### WareHouse Service

#### Warehouse Maintenance

The route for warehouse maintenance is `ware/wareinfo`. Warehouse maintenance is primarily used to manage information related to warehouses and supports corresponding CRUD operations.

The main information of this page is stored in the wms_ware_info table.

<br>

#### Product Inventory

The route for product warehousing is `ware/sku`. Product warehousing is primarily used to manage storage information for each warehouse. This functionality is associated with the procurement function, where inventory is automatically displayed once procurement is completed.

The main information of this page is stored in the wms_ware_sku table.

<br>

#### Purchase Order Management

- Purchase Requisitions

The route for purchase requisitions is `ware/purchaseitem`. Purchase requisitions are primarily used to manage SKU-related procurement information, determining purchase item IDs, quantities, etc., while also supporting the merging of purchase orders.

The main information of this page is stored in the wms_purchase_detail table.

- Inventory Work Orders

The route for inventory work orders is `ware/task`

- Purchase Orders

The route for purchase orders is `ware/purchase` This is used to generate and manage purchase orders, supporting CRUD operations and associated with the purchase requisitions page.

The main information of this page is stored in the wms_purchase table.

- Receiving Purchase Orders

This functionality is not part of the backend management system and can be configured for procurement personnel. Requests are initiated via mobile devices or other means to the `/api/warehouse/purchase/received` endpoint, which can be simulated using Postman. It is used for procurement personnel to receive purchase orders.

<br>

### Coupon Service

<br>

## Database Design

**(No foreign keys will be established due to this is a e-commerce project. This is to avoid potential performance impacts, as e-commerce databases often deal with a large volume of data.)**

The following databases will be established: 

<br>

The character set will be set to utf8mb4 to ensure compatibility with utf8 and address potential issues related to character encoding.

<br>

You can find the details of the CREATE TABLE statement and insert data here. <a href = "https://github.com/lh728/0-to-1-Microservices-Distributed-E-commerce-System-Template/tree/777679015934b1f745a7cd55b6e66a884eace26e/Static" >Github</a>

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
