
> Ribbon是Netflix 开发的一套客户端负载均衡工具，主要是提供客户端的负载均衡

Ribbon中的客户端负载均衡策略：
> 接口：com.netflix.loadbalancer.IRule


服务端负载均衡和客户端负载均衡的区别？
+ 服务端负载均衡 nginx
```html
    服务端负载均衡就是一个请求首先到一个代理服务器，也就是nginx
    然后这个代理服务器会通过负载均衡算法从服务器列表中选择一台服务器来处理这个请求
    这就是服务器端的负载均衡
```
+ 客户端负载均衡 ribbon
```vue
    "Ribbon是一个基于HTTP和TCP的客户端负载均衡器，当我们将Ribbon和Eureka一起使用时，Ribbon会从Eureka注册中心获取服务列表
    然后通过一定的算法计算出一个服务去访问，达到负载均衡的目的"
    客户端负载均衡也需要心跳机制去维护服务的有效性，这个过程需要配合注册中心一起完成
```
+ 区别
```properties
最大的区别是服务信息的存放位置不同
    在客户端负载均衡中，每一个客户端都会保存一份自己需要访问的服务列表，这写列表都是从Eureka注册中心获取的
    而在服务端堵在均衡时，所有的服务信息都保存在代理服务器中
```

### Feign负载均衡

> Feign是一个声明式的webservice客户端，让我们更加简单的书写webservice程序

+ 只需要一个接口和一个注解，即可完成webservice请求的书写，支持Feign内部以及JAX-RS规范的注释
+ Spring Cloud对Feign进行了封装，使其支持Spring MVC的注解和HttpMessageConverters
+ Feign可与Eureka和Ribbon组合使用
原文：https://projects.spring.io/spring-cloud/spring-cloud.html#spring-cloud-feign

```text
加入Feign，可以免去我们在controller中频繁使用restTemplate和各种请求路径，不好维护
我们可以使用feign来定义和访问服务，达到接口同一，修改方便，更加规范和已维护
```
> RestTemplate与Feign

+ RestTemplate和Feign都是springcloud提供的一种调用REST服务的方式
+ Feign是一款声明式的REST客户端，它存在的目的就是为简化REST服务的调用，内部提供了HTTP请求的模板
+ springcloud Netflix 的微服务都是通过HTTP接口的形式暴露的，所以可以使用Apache的HttpClient、RestTemplate或者Feign区调用服务
+ 简单来说Feign和RestTemplate一样是用来远程调用服务的，只是RestTemplate的API写起来更加复杂

### 总结Eureka、Ribbon、Feign

+ Eureka提供服务注册与发现
+ Ribbon提供客户端负载均衡
+ Feign提供简洁的REST接口调用，并且Feign内部集成了hystrix和ribbon，支持负载均衡和服务熔断










