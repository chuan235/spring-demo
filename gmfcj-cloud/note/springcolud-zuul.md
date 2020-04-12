Zuul的作用: 请求的路由和过滤

+ 路由:就是负责将外部的请求转发到具体的微服务上,实现外部统一访问入口
+ 过滤:可以干预请求的处理,是实现身份校验 服务聚合的基础
+ zuul的容错:zuul在内部自动集成了hystrix和ribbon,提供微服务级别的降级回退
+ zuul集群的搭建思路

### 使用zuul

> zuul和eureka进行整合时,zuul会将自生作为一个服务注册到eureka中,并且它可以从eureka中获取其他微服务的信息

+ 外部访问系统时都会统一使用zuul的网关api进行访问,然后zuul会根据对应的配置将不同的请求转发到不同的服务
+ zuul的服务也会注册到eureka上

> 需要引入eureka和zuul的依赖,具体配置参考:https://cloud.spring.io/spring-cloud-static/Finchley.SR4/single/spring-cloud.html#netflix-zuul-starter

### zuul的过滤器

> 过滤器是zuul中的核心组件,zuul大部分功能都是通过过滤器来实现的 zuul中定义了4种标准过滤器类型

+ PRE 这种过滤器在请求被路由之前调用 用处:身份校验 选择请求的微服务 记录访问信息
+ ROUT 这种过滤器将在请求路由到微服务调用之前执行 用处:构建发送到服务的请求
+ POST 这种过滤器在路由到微服务以后调用 用处:设置header 收集系统性能和指标
+ ERROR 在以上三个的阶段中出现异常时会执行这个过滤器

+ 禁用jar包中的filter
```yml
zuul:
  LogFilter: 
    route: 
      disable: true
```

### zuul继承hystrix实现回退

```java
@Component
class ServiceFallbackProvider implements FallbackProvider {
        // ... 实现方法就ok
}
```
+ 配置请求服务的超时方法
```properties
# 使用eureka中的服务发现机制
ribbon.ReadTimeout=10000
ribbon.SocketTimeout=60000
# 使用url
zuul.host.connect-timeout-millis=30000
zuul.host.socket-timeout-millis=60000
```

### @EnableZuulProxy 与 @EnableZuulServer 的区别

```text
@EnableZuulProxy    导入了 ZuulProxyMarkerConfiguration 
@EnableZuulServer   导入了 ZuulServerMarkerConfiguration

proxy相比于server多了一个注解: @EnableCircuitBreaker => 内部导入了 SpringFactoryImportSelector 类
这个selector会从spring.factory文件中获取key=org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker 的所有value
返回这些className,这些className对应的对象就会被spring创建并管理

区别:
    @EnableZuulProxy是@EnableZuulServer的超集。
    也就是说@EnableZuulProxy中包含了@EnableZuulServer安装的所有过滤器。
    @EnableZuulProxy附加了根据路由筛选过滤器的功能。

@EnableZuulServer => SimpleRouteLocator => load route definitions => springboot configurations
Pre filters
    ServletDetectionFilter  检测请求是否通过了spring的dispatcher调度 对应的值:FilterConstants.IS_DISPATCHER_SERVLET_REQUEST_KEY
    FormBodyWrapperFilter   解析表单数据,并为后续的服务处理重新进行编码,包装表单数据
    DebugFilter             如果设置的调试请求参数,需要设置
        RequestContext.setDebugRouting(true)和RequestContext.setDebugRequest(true),这样所有的请求都可以调试
    SendForwardFilter       使用RequestDispatcher这个servlet去转发请求,转发的具体路径在RequestContext的属性中
        属性key=FilterConstants.FORWARD_TO_KEY 转发
Post filters    
    SendResponseFilter      将代理服务的响应写入当前响应中
Error filters
    SendErrorFilter         默认转发到/error如果RequestContext.getThrowable()不为null,存在异常
        通过配置 error.path 去改变默认的转发路径

@EnableZuulProxy => DiscoveryClientRouteLocator => load route definitions => DiscoveryClient(eureka)
    一个serviceId 对应了一个 DiscoveryClient 
    内部包含了server的所有过滤器,还存在以下过滤器
Pre filters
    PreDecorationFilter     根据RouteLocator去确定如何路由请求,并且可以为后续调用的微服务设置头信息或者代理信息等header
Route filters
    RibbonRoutingFilter     使用Ribbon Hystrix 或者其他的HTTP客户段对服务发起请求,FilterConstants.SERVICE_ID_KEY 这个key对应的就是目标服务的id
        这个过滤器可以使用不同的HTTP去发送请求 默认使用Apache HttpClient
        如果需要使用  Squareup OkHttpClient 需要配置:加入okhttp的包和一个配置ribbon.okhttp.enabled=true
        如果需要使用Ribbon的HTTP客户段
            开启:ribbon.restclient.enabled=true  但是这个客户段有限制,不支持PATCH method,但是内置了重试的策略
    SimpleHostRoutingFilter 通过 apache httpclient 向预定的url发送请求。URL位于requestContext.getRouteHost（）中
    
    
```

### zuul容错集群的搭建

+ 针对客户段的容错集群
+ 针对内部服务的容错集群

客户段可以使用ngnix分发请求到不同的zuul

### zuul的限流保护

> Zuul网关组件也提供了限流保护。当请求并发达到阀值，自动触发限流保护，返回错误结果。只要提供error错误处理机制即可。
  Zuul的限流保护需要额外依赖spring-cloud-zuul-ratelimit组件。

```xml
<dependency>
    <groupId>com.marcosbarbero.cloud</groupId>
    <artifactId>spring-cloud-zuul-ratelimit</artifactId>
</dependency>
```
配置参考: https://blog.csdn.net/k_young1997/article/details/104100361 