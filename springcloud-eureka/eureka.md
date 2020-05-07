#### Eureka

​	文档地址：https://spring.io/projects/spring-cloud-netflix

​	Eureka是Netflix开发的服务发现框架，本身是一个基于REST的服务，主要用于定位运行在AWS域中的中间层服务，以达到负载均衡和中间层服务故障转移的目的。SpringCloud将它集成在其子项目spring-cloud-netflix中，以实现SpringCloud的服务发现功能。

1、Eureka架构

![](/images/eureka.png)

Eureka包含两个组件：Eureka Server和Eureka Client 

​	Eureka Server提供服务注册服务，Eureka Client是一个java客户端，用于简化与Eureka Server的交互，客户端同时也就是一个内置的、使用轮询(round-robin)负载算法的负载均衡器。

​	在应用启动后，将会向Eureka Server发送心跳,默认周期为30秒，如果Eureka Server在多个心跳周期内没有接收到某个节点的心跳，Eureka Server将会从服务注册表中把这个服务节点移除(默认90秒)。

​	Eureka Server之间通过复制的方式完成数据的同步，Eureka还提供了客户端缓存机制，即使所有的Eureka Server都挂掉，客户端依然可以利用缓存中的信息消费其他服务的API。

​	Eureka通过心跳检查、客户端缓存等机制，确保了系统的高可用性、灵活性和可伸缩性。

+ 服务注册
+ 服务发现
+ 外部配置

以下是测试版本

```xml
<spring-cloud.version>Greenwich.SR1</spring-cloud.version>
<parent>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix</artifactId>
    <version>2.1.1.RELEASE</version>
</parent>
```

##### 注册服务

1、创建一个Eureka Server

pom文件

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

2、配置

```properties
server:
  port: 8761
eureka:
  instance:
    hostname: localhost
  client:
    register-with-eureka: false #是否注册到eureka，默认为true，当前是eureka服务，所以设置为false
    fetch-registry: false #是否在本地缓存注册表信息，默认为true，设置为false
    service-url:
      defaultZone: http://localhost:8761/eureka #可以不配置
spring:
  application:
    name: eureka-server
```

3、开启Eureka服务

```java
@EnableEurekaServer
@SpringBootApplication
public class EurekaserverApplication {
```

##### 注册服务

pom

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

配置

```properties
server:
  port: 8001
spring:
  application:
    name: provider8001
eureka:
  instance:
    prefer-ip-address: true # 优先使用IP地址方式进行注册服务
    instance-id: ${spring.application.name}:${server.port}
    # hostname: provider8002  使用主机名称进行注册，配置hosts映射
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka # 服务注册的地址，如果需要注册多个服务器，用逗号隔开
```

使用注解注册服务

```java
@SpringBootApplication
@EnableEurekaClient
public class ProviderApplication {
```

##### 调用服务

pom

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

配置

```properties
server:
  port: 8200
spring:
  application:
    name: comsumer

eureka:
  instance:
    prefer-ip-address: true # 注册服务的时候使用服务的ip地址
    instance-id: ${spring.application.name}:${server.port}
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
```

开启服务发现功能

```java
@EnableDiscoveryClient //开启发现服务功能
@EnableEurekaClient
@SpringBootApplication
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }
	// 向容器中注入一个restful接口操作模板
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
```

调用服务

```java

@RestController
public class TestController {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    DiscoveryClient client;

    @GetMapping("/{name}/8001")
    public String test1(@PathVariable("name") String name){
        String s = restTemplate.getForObject(getUrl("PROVIDER8001")+"/ticket", String.class);
        return name+"购买了"+s;
    }


    @GetMapping("/{name}/test")
    public String test(@PathVariable("name") String name){
        // [provider8002, comsumer]
        List<String> list = client.getServices();
        System.out.println("************"+list);
        List<ServiceInstance> instances = client.getInstances("PROVIDER8002");
        for(ServiceInstance instance:instances){
            // PROVIDER8002----192.168.222.1----8002
            System.out.println(instance.getServiceId()+"----"+instance.getHost()+"----"+instance.getPort()+"----"+instance.getUri());
            System.out.println();
        }
        return "启动完成";
    }

    @GetMapping("/{name}/8002")
    public String test2(@PathVariable("name") String name){
        String s = restTemplate.getForObject(getUrl("PROVIDER8002")+"/ticket", String.class);
        return name+"购买了"+s;
    }

    @GetMapping("/{name}/8003")
    public String test3(@PathVariable("name") String name){
        String s = restTemplate.getForObject(getUrl("PROVIDER8003")+"/ticket", String.class);
        return name+"购买了"+s;
    }

    /**
     * 根据服务id返回对应的url
     * @param serviceId PROVIDER8002
     * @return http://192.168.222.1:8002
     */
    private String getUrl(String serviceId){
        return client.getInstances(serviceId).get(0).getUri().toString();
    }
}
```















