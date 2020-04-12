Eureka是基于REST(Representational State Transfer)的服务，主要用于服务注册发现、客户端负载均衡以及服务失败迁移

> 使用地址：https://github.com/Netflix/eureka/wiki

### 基于CS架构的服务注册中心

+ Eureka是Netflix的子模块之一，Eureka基于CS架构，所以存在客户端和服务端之分
+ 服务端(EurekaServer):是一个独立的项目，主要用于服务发现和服务中间层的负载均衡
```text
    Eureka is a REST (Representational State Transfer) based service 
    that is primarily used in the AWS cloud for locating services for the purpose of 
    load balancing and failover of middle-tier servers.
    
    Since AWS does not yet provide a middle tier load balancer, Eureka fills a big gap in the area of mid-tier load balancing.
```
+ 客户端(EurekaClient):也是一个独立的项目(微服务)，主要用于与Server进行交互并且内置有简单的循环负载均衡机制
+ Eureka并不限制访问服务的协议，可以使用thrift、http(s)或者其他的RPC之类的协议


### 基于CS架构的Eureka的高可用设计

![](https://raw.githubusercontent.com/Netflix/eureka/master/images/eureka_architecture.png)

+ 在上面的部署方式中，每两个Eureka服务端之间都会互相同步数据，并且会自己的信息注册到对方身上
+ 这样即使，自己突然宕机，也可以从其他Eureka服务端成功调用到服务

#### 监控机制

+ 服务在Eureka中注册后(包括注册到Eureka的其他EurekaServer)，默认每30秒发送一次心跳，如果90秒内没有接收到服务发送的心跳信息，则会将服务从服务列表中移除
+ Eureka服务之间每30s会同步一次注册信息

> 配置参考：https://cloud.spring.io/spring-cloud-static/Finchley.SR4/single/spring-cloud.html

### CAP

+ C（Consistency） 一致性：当一个分区的用户修改了数据之后，在另一分区可以实时查询到数据变化
+ A（Availability）可用性：任意时刻当服务器集群收到请求时，必须响应客户端具体的数据，否则就不满足可用性
+ P（Partition tolerance）分区容错性：一个分区(北京)服务器向另一分区(云南)的服务器通信时，服务器可能无法受到请求。在系统设计时，分区容错性是必然存在的，必须考虑到这种情况

```text
    从上面的概念中就可以看出一致性和可用性是相互矛盾的，不能同时成立
    如果要保证数据的一致性，因为数据同步需要一定的时间。
        所以在一定的时间间隔内，客户端是无法访问服务，获取访问的数据不一致
    如果无法访问服务则可用性不满足，如果获取访问的数据不一致则不满足数据一致性
    如果追求数据一致性，则必须要牺牲掉系统的一定时间段的不可用性
    如果追求集群可用性，则会出现系统一定时间内的数据不一致问题
```

### Eureka对比Zookeeper

+ Zookeeper在设计时遵循CP原则
```text
    存在这样一种问题：当master节点因为网络故障失去联系后，剩余节点会重新选举生成leader。
    这个选举时间一般在30 ~ 120s之内，并且选举期间整个zk集群是不可用的，就导致在选举期间无法注册访问服务
    在云部署的环境下，因网络环境使zk集群失去master节点是较大概率发生的事情，虽然服务能够最终恢复，
    但是漫长的选举时间导致长期的服务注册不可用是不能容忍的
```

+ Eureka在设计时遵循AP原则
```text
    Eureka每个server都是平等的，没有主次之分。只要节点不全部挂掉，整个集群就可以正常访问(可用性)
    Eureka客户端在向某个Eureka服务注册服务时出现连接失败的情况时，会自动切换到其他EurekaServer
    但是存在一个数据的不一致的问题，比如修改了一个数据，但是还没有来得及同步，这个server就挂了
    那么从其他server中获取的数据就不会是最新的数据(不能保证数据的强一致性)
    Eureka存在自我保护机制：如果在15分钟内超过85%节点都没有正常心跳，那么eureka就认为客户端与注册中心
    出现了网络故障，此时会出现以下情况:
        1、Eureka不会移除注册列表中长时间没有接收到心跳而过期的服务
        2、Eureka仍然能够接收服务的查询和新服务的注册，但不会将这些数据同步到其他server节点上，保证当前EurekaServer可用
        3、当网络稳定后，当前实例新的注册信息会被同步到其它节点中
```
