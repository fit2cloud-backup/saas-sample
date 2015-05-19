FIT2CLOUD SaaS Sample
==========

该演示功能如下:

*   可以列出管理的虚机
*   可以向管理的虚机组中添加虚机
*   可以将管理的虚机移除


0.  准备工作

*   安装JDK
	
	yum -y install java-1.7.0-openjdk
	
*   安装maven

	cd /opt
	wget http://mirror.symnds.com/software/Apache/maven/maven-3/3.0.5/binaries/apache-maven-3.0.5-bin.tar.gz
	tar zxf apache-maven-3.0.5-bin.tar.gz
	echo "export M2_HOME=/opt/apache-maven-3.0.5" >> ~/.bashrc
	source ~/.bashrc


1.  开发包说明

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


2.  配置文件

该SaaS Sample示例中使用到了配置文件:/opt/fit2cloud/saas/fit2cloud.properties, 开发之前请确保该文件内容的正确

fit2cloud.properties文件中内容如下:

*   webspace.restapi.endpoint=http://<YOUR_FIT2CLOUD_SERVER_URL>:6608		>>	FIT2CLOUD企业版中RESTAPI的访问地址
*   consumer.key=<YOUR_CONSUMER_KEY>										>>	FIT2CLOUD用户的consumer key
*   secret.key=<YOUR_SECRET_KEY>											>>	FIT2CLOUD用户的secret key, 和consumer key一起标识用户身份
*   multi-tenants.cluster.id=<CLUSTER_ID>									>>	被管理虚机所属集群的ID
*   multi-tenants.clusterrole.id=<CLUSTER_ROLE_ID>							>>	被管理虚机所属虚机组的ID

开发者可以在FIT2CLOUD企业版数据库中获取到上述所需的信息


3.  运行

在saas-sample项目的目录下, 运行命令:

	mvn clean package
	
执行后, 将会在saas-sample项目目录下生成target目录. 该目录中包含了我们所需的运行文件 saas-sample.war

可以使用以下命令来运行编译后的项目:

	java -jar <saas-sample.war文件所属路径>/saas-sample.war


4.  访问

成功运行后, 可以在浏览器中访问http://<YOUR_SERVER_IP>:8080来操作


5. 功能介绍

*   列出管理的虚机

该应用只有一个页面, 访问该应用时将会根据fit2cloud.properties中设置的参数, 列出该用户指定虚机组下所有的虚机情况

*   添加虚机

页面中有"添加"按钮, 点击添加按钮, 将会向FIT2CLOUD发出启动新虚机的请求, 当提示发送添加指令成功后, 用户将可以在列表中看到新添的虚机. 几分钟内, 该虚机将启动完毕

*   移除虚机

管理的每台虚机最后都有"移除"按钮. 点击该按钮, 将会向FIT2CLOUD发出关闭虚机的请求, 当提示发送移除指令成功后, 用户将在列表中看到该虚机处于等待关闭状态. 几分钟内, 该虚机将完全关闭