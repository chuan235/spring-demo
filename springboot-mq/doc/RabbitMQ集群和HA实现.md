# liunx上搭建rabbitMQ

参考：https://www.rabbitmq.com/install-rpm.html

环境准备：安装依赖的rpm包

yum install build-essential openssl openssl-devel unixODBC unixODBC-devel make gcc gcc-c++ kernel-devel m4 ncurses-devel tk tc xz

第一步：下载 erlang、socat、rabbitmq

wget www.rabbitmq.com/releases/erlang/erlang-18.3-1.el7.centos.x86_64.rpm

wget http://repo.iotti.biz/CentOS/7/x86_64/socat-1.7.3.2-5.el7.lux.x86_64.rpm

wget www.rabbitmq.com/releases/rabbitmq-server/v3.6.5/rabbitmq-server-3.6.5-1.noarch.rpm

第二步：安装

　　rabbitmq 最后一个安装

rpm -ivh erlang-18.3-1.el7.centos.x86_64.rpm

rpm -ivh socat-1.7.3.2-5.el7.lux.x86_64.rpm

rpm -ivh rabbitmq-server-3.6.5-1.noarch.rpm

第三步：启用 web 管控台插件

　　rabbitmq-plugins enable rabbitmq_management

第四步：调整 guest 账户登录限制

　　修改：vim /usr/lib/rabbitmq/lib/rabbitmq_server-3.6.5/ebin/rabbit.app

　　　　修改 loopback_users 中的 [<<"guest">>], 只保留 []，这样就可以远程使用guest登录

　　其他修改如

　　　　修改：heartbeat 为 5，单位为秒

第五步：启动/停止 rabbitmq

　　rabbitmq-server 命令

```shell
/etc/init.d/rabbitmq-server start stop status restart
```

　　使用 rabbitmq-server start & 以后台方式启动 rabbitmq

　　停止 rabbitmq 服务：rabbitmqctl stop_app

　　　　通常会失败... 用 ps -ef|grep rabbit 找出进程号 kill -9 强杀

　　　　当出现 node rabbit is running 警告时也用 kill -9 解决

其他配置/操作：

　　添加自定义配置文件：/etc/rabbitmq/rabbitmq.config

RPM安装方式的默认日志路径：/var/log/rabbitmq

目录下存在以下文件

```properties
rabbit@docker.log
rabbit@docker-sasl.log
shutdown_err
shutdown_log
startup_err
startup_log
```

pathhs路径在web管理页面可以查看

```properties
config File = /etc/rabbitmq/rabbitmq.config
Database directory = /var/lib/rabbitmq/mnesia/rabbit@docker
log file = /var/log/rabbitmq/rabbit@docker.log
SASL log file = /var/log/rabbitmq/rabbit@docker-sasl.log
```

在 broker 启动时有输出提示

访问 web 管控台（关闭防火墙或者暴露端口）

```shell
systemctl stop firewalld.service # 关闭防火墙，系统重启后需要再手动关闭
# 暴露端口
firewall-cmd --zone=public --permanent --add-port=5672/tcp &&
firewall-cmd --zone=public --permanent --add-port=15672/tcp
```

如果没有修改配置文件，那么系统默认的guest用户是不能进行远程访问的，如果需要进行远程访问可以修改配置文件或者是添加用户

修改配置文件的方式在上面安装过程中有记录到

使用命令来添加用户和给用户授权

```shell
#创建用户 rabbitmqctl add_user {用户名} {密码}
rabbitmqctl add_user admin admin
#设置权限 rabbitmqctl set_user_tags {用户名} {权限}
rabbitmqctl set_user_tags admin administrator
#查看用户列表
rabbitmqctl list_users
#添加 Virtual Hosts ：    
rabbitmqctl add_vhost <vhost>    
#删除 Virtual Hosts ：    
rabbitmqctl delete_vhost <vhost>    
#为用户赋权
rabbitmqctl set_permissions [-p <vhost>] <user> <conf> <write> <read>    
#删除用户
rabbitmqctl delete_user Username
#重启密码
rabbitmqctl change_password Username Newpassword
```

rabbitMQ集群参考：https://www.cnblogs.com/xishuai/p/centos-rabbitmq-cluster-and-haproxy.html

```shell
hostnamectl status
```

1、找到 .erlang.cookie 文件

```shell
find / -name ".erlang.cookie"
# /var/lib/rabbitmq/.erlang.cookie
```

2、将当前节点中的文件拷贝到其他节点

```shell
scp /var/lib/rabbitmq/.erlang.cookie root@node2:/var/lib/rabbitmq
```

3、分别启动服务器节点

```shell
rabbitmqctl stop
rabbitmq-server -detached
```

4、指定集群的中心，假设指定node1为集群中心，需要在其他节点上执行加入集群中心的命令

```shell
rabbitmqctl stop_app
rabbitmqctl reset 
rabbitmqctl join_cluster rabbit@docker
# 默认是磁盘节点，如果是内存节点的话，需要加--ram参数
rabbitmqctl start_app
```

5、查看集群状态

```shell
rabbitmqctl cluster_status
```





