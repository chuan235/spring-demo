1、使用memcached的add命令，这个命令是原子操作，只有在key不存在的情况下，才能add成功

2、使用Redis的setnx命令

```properties
# 加锁
setnx(key,uuid)
# 解锁
del(key)
# 锁超时,单位是秒
expire(key,30)
# 如果出现并发，枷锁和设置过期有并发问题，使用set命令一次完成加锁和超时的设置 30代表超时的时间
set(key,uuid,30,NX)
# 如果线程出现执行过慢，导致锁超时(锁自动超时释放，其他线程拿到锁) 最后线程执行完成释放锁的时候删除了别的锁
set(key,threadId,30,NX) # 存储线程id
# 上述情况存在多个线程同时访问某个代码块的情况，可以让获得锁的线程再次开启一个守护线程，用来给快要过期的锁续航
如果设置了30s的过期时间，如果线程执行到了29s，这个时候守护线程就会再次执行expire命令，为这把锁续命20s，这个守护线程每20s执行一次，当拿到锁的线程执行完，会显示的关闭守护线程。
如果当前拿到锁的线程突然断了，这样只有锁到了超时的时候，才会自动释放
```

[Redis Lock  code](https://github.com/Only-TEL/workspace/blob/master/springboot-mq/mq-redis/src/main/java/top/gmfcj/lock/RedisLock.java)

3、利用zookeeper的顺序临时节点

```tex
1、每来一个客户端就会创建一个临时顺序节点
2、制定一个获取锁的规则，顺序值最小的获取锁获取最大的获取锁.../以顺序值最小的获取锁为例
3、创建临时顺序节点的同时，都要去尝试获取锁，判断自己是不是第一个节点，并且要监听前一个临时顺序节点的删除事件，当前一个节点删除的时候，就表示当前节点对应的客户端可以拿到锁了
zookeeper实现分布式锁的特点：
1、有效解决锁无法释放的问题
2、实现阻塞锁，每一个客户端都可以去创建一个锁，只需要循环判断当前节点是不是最小的
3、锁不可重入，将线程的主机信息写入临时顺序节点文件中，下次想要获取锁的时候和当前最小的节点中的数据比对一下就可以了。如果和自己的信息一样，那么自己直接获取到锁，如果不一样就再创建一个临时的顺序节点，参与排队
4、分布集群有效解决单点故障问题
5、锁超时（只能使用sessionTime限制）
```

[Zookeeper Lock Code](https://github.com/Only-TEL/workspace/blob/master/zookeeper-3.4.13/src/java/source/top/gmfcj/distribute/lock/ZkLock.java)

### 搭建redis cluster集群

> redis3.0以上版本实现

官方推荐三从三主的配置方式，使用hash槽的概念。将16384个槽分配给redis集群，每一个key经过CRC16算法计算后的值与16384取模，来决定有哪一个槽位进行处理，具体的槽位会对应唯一的一台redis服务器，就会由这一台服务器来处理这个key的相关操作

使用主从复制的模型，每一个节点都有n-1个slave，如果master不可用，会选举slave为新的master继续服务。如果同个节点的master和slave都失效，整个集群将不可用

Cluster集群的问题

+ 不支持处理多个key的操作，因为这需要在不同的节点间移动数据,从而达不到像Redis那样的性能,在高负载的情况下可能会导致不可预料的错误

> 当两个set映射到不同的redis实例上时，你就不能对这两个set执行交集操作

1、准备工作，创建6个存储配置文件的路径分别是reids7000 ~ redis7005

2、修改redis7000的配置文件

```shell
port 7000 		#端口7000,7002,7003..
daemonize yes 	#redis后台运行
pidfile ./redis_7000.pid #pidfile文件对应7000,7001,7002
cluster-enabled yes #开启集群 把注释#去掉
cluster-config-file nodes_7000.conf #集群的配置 配置文件首次启动自动生成 7000,7001,7002
cluster-node-timeout 15000 #请求超时 默认15秒，可自行设置
appendonly yes #aof日志开启 有需要就开启，它会每次写操作都记录一条日志
#若设置密码，master和slave需同时配置下面两个参数：
masterauth "12345678"    #连接master的密码
requirepass "12345678"   #自己的密码
```

3、批量替换配置文件

```shell
# sed -i “s/查找字段/替换字段/g” grep 查找字段 -rl 路径
sed -i "s/7000/7001/g" redis7001/redis.conf
sed -i "s/7000/7002/g" redis7002/redis.conf
```

4、书写启动脚本

```shell
#!/bin/bash
echo "start all redis 7000-7005"
./bin/redis-server redis7000/redis.conf
./bin/redis-server redis7001/redis.conf
./bin/redis-server redis7002/redis.conf
./bin/redis-server redis7003/redis.conf
./bin/redis-server redis7004/redis.conf
./bin/redis-server redis7005/redis.conf
echo "successed"
ps -ef | grep redis
```

设置脚本权限

```shell
chmod u+x start-all.sh #chmod 777 start-all.sh
```

5、书写停止脚本

https://www.cnblogs.com/wslook/p/9152596.html

```shell
#!/bin/bash
AppName=redis-server

if [ "$1" = "" ];
then
    echo -e "\033[0;31m 未输入具体的端口号 \033[0m  \033[0;34m {6379 9000} \033[0m"
    exit 1
fi
arr=($1)
for port in ${arr[@]}
do
   echo "stop $AppName, port " $port
   cmd="ps -ef | grep ${AppName} | grep -v grep | grep -v vim | grep -v defunct | grep $port | awk '{ print \$2 }'"
   PID=$(eval ${cmd})
   #echo "PID $PID"
   if [ ${PID}"e" = "e" ]; then
       echo "redis-server(port:$port) is not started"
       exit -1
   else
       kill $PID
       fi
       stopfail=1
       for i in `seq 0 30`
       do
           PID=$(eval ${cmd})
           #echo $PID
           if [ ${PID}"e" != "e" ]; then
               echo "redis-server(port:$port) is still running, waiting to stop[${i}]..."
           else
               echo "redis-server(port:$port) is stoped"
               stopfail=0
             break
          fi
          sleep 1
       done
done
echo "all ${AppName} stop"
```

测试结果

```shell
stop redis-server, port  7001
redis-server(port:7001) is still running, waiting to stop[0]...
redis-server(port:7001) is stoped
stop redis-server, port  7002
redis-server(port:7002) is still running, waiting to stop[0]...
redis-server(port:7002) is stoped
all redis-server stop
```

6、建立集群连接

```shell
./bin/redis-cli -h 192.168.222.129 -p 7000 -a redis cluster slots
(empty list or set) # 没有分配槽位 cluster集群无法使用
# 查看所有的节点状态
./bin/redis-cli -h 192.168.222.129 -p 7000 -a redis cluster nodes
# 机器之间建立连接
./bin/redis-cli -h 192.168.222.129 -p 7000 -a redis cluster meet 192.168.222.129 7001
./bin/redis-cli -h 192.168.222.129 -p 7000 -a redis cluster meet 192.168.222.129 7002
./bin/redis-cli -h 192.168.222.129 -p 7000 -a redis cluster meet 192.168.222.129 7003
./bin/redis-cli -h 192.168.222.129 -p 7000 -a redis cluster meet 192.168.222.129 7004
./bin/redis-cli -h 192.168.222.129 -p 7000 -a redis cluster meet 192.168.222.129 7005
# 查看集群信息，节点数量，届数...
./bin/redis-cli -h 192.168.222.129 -p 7000 -a redis cluster info
# 分配主从节点 设置副本
./bin/redis-cli -h 192.168.222.129 -p 7000 -a redis cluster replicate nodeId(slaveId)
# 7000(7003),7001(7004),7002(7005)
```

7、编写脚本分配槽位

分配槽位 一共16384个  10000+3000+3384

```shell
#!/bin/bash
start=$1
end=$2
port=$3
for slot in `seq ${start} ${end}`
do
    echo "give slot: ${slot}"
    /root/training/redis/bin/redis-cli -h 192.168.222.129 -p ${port} -a redis cluster addslots ${slot}
done
```

```shell
./assgin-slot.sh 0 10000 7000
./assgin-slot.sh 10001 13000 7001
./assgin-slot.sh 13001 16383 7002
```

再次查看节点分配的slot

```shell
./bin/redis-cli -h 192.168.222.129 -p 7000 -a redis cluster slots
./bin/redis-cli -h 192.168.222.129 -p 7000 -a redis cluster info
```



### 搭建ruby环境

1)安装ruby前置库

yum install zlib-devel

yum install zlib-devel

2)安装ruby

下载：wget https://cache.ruby-lang.org/pub/ruby/2.3/ruby-2.3.7.tar.gz

解压到特定目录：tar -zxvf ruby-2.3.7.tar.gz -C /root/training

进入目录：cd /root/training/ruby-2.3.7

配置：./configure --prefix=/usr/local/ruby (把解压的源文件放在/usr/local/ruby)

编译：make

安装：make install

进入目录：cd /usr/local/ruby

让ruby添加到用户的访问变量中

```shell
cp bin/ruby /usr/local/bin
```

让gem添加到用户的访问变量中

```shell
cp /usr/local/ruby/bin/gem /usr/local/bin
```

输入ruby -v，查看版本正确则说明安装成功

执行gem -v，同理

### 安装ruby的redis客户端

```shell
# 下载
wget https://rubygems.org/downloads/redis-4.0.1.gem
# 安装
gem install -l redis-4.0.1.gem
# 校验安装
gem list -- check redis gem
```



```
cd /root/training/redis-4.0.0/src/
./redis-trib.rb fix 192.168.222.129:7000
./redis-cli -c -p 7000
```

shell函数

```shell
#!/bin/bash
IFS=' '
arr=($1)
for v in ${arr[@]}
do
	echo "$v"
done
# 执行shell的参数都在这里
echo "param1 $1"
echo "param1 $2"
echo "param1 $3"
echo "param1 $4"
```

