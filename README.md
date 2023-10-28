# 0-to-1-Microservices-Distributed-E-commerce-System-Template
This is a microservices-based distributed e-commerce system template designed to leverage a wide range of advanced management tools and practices from 0 to 1.

**I would like to use the latest technologies as much as possible to complete this e-commerce template.**



## Project Setup Guide

### Virtual Machine

Download a Linux virtual machine based on Windows using **VirtualBox** (Make sure to enable CPU virtualization mode).

Download the official image via Vagrant, create a Linux virtual machine, and modify the Vagrantfile network settings (change the private network to your computer's IP address for domain name mapping; you can find your IP address using the '`ipconfig`' command).



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



### **Environment**

Java17.0.6 

MAVEN 3.9.2

Git



### Dependency

Springboot 3.1.4

Spring Web

Spring Cloud Routing - openFeign (microservices communication)



### Database Design

**(No foreign keys will be established due to the hypothetical e-commerce project. This is to avoid potential performance impacts, as e-commerce databases often deal with a large volume of data.)**

The following databases will be established: 

- oms (Order Management System), 
- pms (Product Management System), 
- mms (Member Management System), 
- sms (Storage Management System), 
- ums (Coupon Management System). 

The character set will be set to utf8mb4 to ensure compatibility with utf8 and address potential issues related to character encoding.

#### Table Structure

These table structure will be defined as follows:













## Microservices Design

### Order



### Member



### Product



### Storage



### Coupon







## Administraion System

For the backend management, we will directly use the existing "vue-element-admin" to save time.

The project's GitHub repository can be found at the following address: [https://github.com/PanJiaChen/vue-element-admin.git](https://github.com/PanJiaChen/vue-element-admin.git)
