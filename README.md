FIT2CLOUD 应用SaaS化示例
==========

简述

基于FIT2CLOUD REST集成API动态弹性创建虚拟机并自动部署应用，获取自动创建虚拟机信息，移除虚拟机，实现SaaS化。典型场景，比如一个大型的某行业应用平台，有客户企业注册使用后，为该客户企业自动创建一台虚拟机并部署一套专属该企业的业务系统，并计费。如电商，物流等。

该演示SaaS管理控制台包含的功能, 基于FIT2CLOUD REST集成API:

```shell
*   SaaS管理控制台可以列出管理的虚机
*   SaaS管理控制台可以向管理的虚机组中添加虚机
*   SaaS管理控制台可以将管理的虚机移除
```shell


一  准备工作

*   安装JDK
```shell
	yum -y install java-1.7.0-openjdk
```
	
*   安装maven
```shell
	cd /opt
	wget http://mirror.symnds.com/software/Apache/maven/maven-3/3.0.5/binaries/apache-maven-3.0.5-bin.tar.gz
	tar zxf apache-maven-3.0.5-bin.tar.gz
	echo "export M2_HOME=/opt/apache-maven-3.0.5" >> ~/.bashrc
	source ~/.bashrc
```

二  开发包说明

```shell
开发SaaS Sample, 我们需要用到fit2cloud-java-sdk. 这是由FIT2CLOUD提供的java开发工具包, 通过该工具包, 开发者可以调用FIT2CLOUD对外提供的REST接口.

目前该工具包提供的功能有以下几点:

*   getClusters 			(获取用户的所有集群信息)
*   getCluster				(获取用户指定的集群信息)
*   getClusterRoles			(获取指定集群下所有虚机组信息)
*   getClusterServers		(获取指定集群下所有虚机信息)
*   getClusterServers		(获取指定集群的虚机组下所有虚机信息)
*   executeScript			(在指定的虚机上执行脚本)
*   getLoggingsByEventId	(获取指定事件的日志信息)

fit2cloud-java-sdk项目的开源地址 : <https://github.com/fit2cloud/fit2cloud-java-sdk> 

开发FIT2CLOUD SaaS应用之前, 请按照fit2cloud-java-sdk项目中的说明, 将其包含在自己的开发项目中.
```


三  配置文件
```shell
该SaaS Sample示例中使用到了配置文件:/opt/fit2cloud/saas/fit2cloud.properties, 开发之前请确保该文件内容的正确

fit2cloud.properties文件中内容如下:

*   webspace.restapi.endpoint=http://YOUR_RESTAPI_ENDPOINT				>>	FIT2CLOUD企业版中RESTAPI的访问地址
*   consumer.key=YOUR_CONSUMER_KEY										>>	FIT2CLOUD用户的consumer key
*   secret.key=YOUR_SECRET_KEY											>>	FIT2CLOUD用户的secret key, 和consumer key一起标识用户身份
*   multi-tenants.cluster.id=CLUSTER_ID									>>	被管理虚机所属集群的ID
*   multi-tenants.clusterrole.id=CLUSTER_ROLE_ID						>>	被管理虚机所属虚机组的ID

开发者可以在FIT2CLOUD企业版右上角的"API信息"中获取到上述所需的信息
```

四  运行

在saas-sample项目的目录下, 运行命令:
```shell
	mvn clean package
```

执行后, 将会在saas-sample项目目录下生成target目录. 该目录中包含了我们所需的运行文件 saas-sample.war

可以使用以下命令来运行编译后的项目:
```shell
	java -jar <saas-sample.war文件所属路径>/saas-sample.war
```

五  访问
```shell
成功运行后, 可以在浏览器中访问http://YOUR_SERVER_IP:8080来操作
```
