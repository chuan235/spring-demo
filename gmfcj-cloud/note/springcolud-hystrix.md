### Hystrix

> Hystrix 是一款用于处理分布式系统的延时和容错的开源库。在分布式系统中，极有可能出现调用服务失败，比如宕机、连接超时、异常等等

> Hystrix 可以保证在一个中间服务出现问题(服务不可用)时，不会导致所有的请求卡住或者整体请求调用失败。可以有效的避免级联故障，提高分布式系统的弹性

```text
    Hystrix 就类似与一种断路器，当一个服务出现故障不可用时
    Hystrix 就会通过短路器的故障监控，向调用方法返回一个Fallback响应，而不是一直等待或者抛出无法处理的异常
    这样就保证了服务调用方的线程不会被长时间的等待、占用系统资源，从而避免了故障在分布式系统中的蔓延
```

+ 服务降级
+ 服务熔断
+ 服务限流
+ 服务监控

> 参考：https://blog.csdn.net/justlpf/article/details/84539134
> 配置参考：https://github.com/Netflix/Hystrix/wiki/Configuration

```yml
服务降级: 当某个微服务的响应时间超出一定阈值或者不可用，这时我们不能直接卡在这里
    我们需要要制定一个(对应每一个方法)失败策略，当出现以上错误时，程序会直接调用这个策略方法迅速响应
Hystrix 存在默认的超时监听，默认就是当一个请求超过了1s，就会被认为是超时，这个是可以配置的
    作用一：Hystrix可以监听请求有没有超时
    作用二：如果服务处理超时或者服务出现不可处理的异常，可以直接在调用服务这里截断异常，迅速返回
```

```yml
服务熔断: 类型电路跳闸，比如一个服务调用多次出现出现问题时(默认是10s内20次，可配置)
    Hystrix 就会采取熔断机制，不在继续调用这个服务方法，而是直接去调用降级方法，在一定程度上避免了服务雪崩的问题
    这里会存在一个试探机制，也就是在服务熔断发生之后，会默认在5s之后试探性的关闭熔断机制然后去调用这个服务方法
    如果这时调用失败了，又会开启熔断机制。然后5s后再次尝试，周而复始...

```

```yml
服务限流：故名思意就是限制服务器请求的并发数(通过在服务中固定线程的数量在限制)
    通过  maxConcurrentRequests、coreSize、maxQueueSize、queueSizeRejectionThreshold
    设置  最大并发数             线程池大小   缓冲区大小     缓冲区降级阈值
    如果将 maxQueueSize=-1，就代表不设置缓冲区，当存在coreSize个请求正在访问服务时，
    这时如果来了一个请求访问该服务，则这个请求会直接调用降级方法返回，这就被成为限流
```


### 默认的命令参数配置

> 可以配置自己的commandKey，需要在使用时在注解上指定commandKey

+ 格式：hystrix.xx.HystrixCommandKey.xx
+ yml的配置文件会覆盖config.properties中的属性值

```properties
# 执行策略配置 `hystrix.command.default`
execution.isolation.strategy=资源隔离的类型，默认是线程THREAD，还有一种信号量SEMAPHORE
execution.timeout.enabled=是否打开超时机制
execution.isolation.thread.timeoutInMilliseconds=请求服务的超时时间，默认1000ms
execution.isolation.thread.interruptOnTimeout=超时的时候是否中断线程
execution.isolation.thread.interruptOnCancel=取消的时候中断线程
execution.isolation.semaphore.maxConcurrentRequests=设置信号量的大小，也就是信号量模式下最大的并发量
# 降级方法配置 `hystrix.command.default`
fallback.isolation.semaphore.maxConcurrentRequests=配置fallback的最大请求数，当请求fallback的请求超过这个值时，抛出异常
fallback.enabled=是否打开降级机制
# 熔断配置 `hystrix.command.default`
circuitBreaker.enabled=是否开启熔断机制
circuitBreaker.forceOpen=强制开启熔断机制
circuitBreaker.forceClosed=强制关闭熔断机制
circuitBreaker.requestVolumeThreshold=
circuitBreaker.errorThresholdPercentage=融断的百分比，当调用同一个服务时，如果失败的百分比超过了这个值，就会自定开启熔断机制
circuitBreaker.sleepWindowInMilliseconds=在一个统计周期(下面度量中的统计周期，默认5s)中，最小需要请求的数量，只有达到了这个数量，熔断才会开启

# 监控(度量)配置 `hystrix.command.default`
metrics.rollingStats.timeInMilliseconds=这里配置统计周期 默认5000,单位ms。
metrics.rollingStats.numBuckets=度量桶的数量，必须是统计周期的倍数
metrics.rollingPercentile.enabled=是否收集执行时间，并计算各个时间段的百分比
metrics.rollingPercentile.timeInMilliseconds=设置执行时间统计周期为多久，用来计算百分比 默认60000,单位ms
metrics.rollingPercentile.numBuckets=执行时间统计周期内，度量桶的数量
metrics.rollingPercentile.bucketSize=执行时间统计周期内，每个度量桶最多统计多少条记录。设置为50，有100次请求，则只会统计最近的10次
metrics.healthSnapshot.intervalInMilliseconds=数据取样时间间隔
# 请求配置
hystrix.command.default.requestCache.enabled=是否缓存请求
hystrix.command.default.requestLog.enabled=是否将HystrixCommand执行和事件记录到HystrixRequestLog
hystrix.collapser.default.maxRequestsInBatch=批处理时最大的批处理请求数
hystrix.collapser.default.timerDelayInMilliseconds=执行批处理任务的延时时间，接受到任务后不会立即执行，会延时
hystrix.collapser.default.requestCache.enabled=这个属性表示请求缓存是否被HystrixCollapser.execute()和HystrixCollapser.queue()开启了
```

### 限流配置 `hystrix.threadpool.default`
```properties
coreSize=核心线程数，表示一次只能处理这么多的请求
maximumSize=设置最大线程数，只有在设置了 allowMaximumSizeToDivergeFromCoreSize 的情况下生效
maxQueueSize=缓冲区大小
queueSizeRejectionThreshold=缓冲区的阈值，缓冲区中的请求超过了这个值，多余的会直接降级
keepAliveTimeMinutes=存活时间
allowMaximumSizeToDivergeFromCoreSize=是否允许设置最大线程，默认是false，只有设置了true，才能使用maximumSize

```

### Feign整合Hystrix

> Feign默认是支持hystrix的,但是在spring-cloud-Dalston版本之后就默认关闭了,所以需要在yml中手动打开

```yml
feign:
  hystrix:
    enabled: true
```

### hystrix整合dashbord
   
Hystrix内部提供的监控模块需要和 spring-boot-actuator 模块一起使用。
比如在user模块中加入 spring-boot-actuator 后，在配置文件中将浏览器的监控端点打开
 `management.endpoints.web.exposure.include=*`,然后访问 `http://localhost:9001/actuator/hystrix.stream` 就可已看到hystrix的监控数据
```json
data: 
  {"type":"HystrixThreadPool",
    "name":"MODULE-POWER",
    "currentTime":1570186432464,
    "currentActiveCount":0,
    "currentCompletedTaskCount":1,
    "currentCorePoolSize":10,
    "currentLargestPoolSize":1,
    "currentMaximumPoolSize":10,
    "currentPoolSize":1,
    "currentQueueSize":0,
    "currentTaskCount":1,
    "rollingCountThreadsExecuted":0,
    "rollingMaxActiveThreads":0,
    "rollingCountCommandRejections":0,
    "propertyValue_queueSizeRejectionThreshold":5,
    "propertyValue_metricsRollingStatisticalWindowInMilliseconds":10000,
    "reportingHosts":1
  }
```

+ 但是这种只是一些数据，我们可以使用 hystrix-dashboar 来解析这个数据，并且hystrix也提供了图形化的界面显示这些数据

### 搭建的独立的项目，用于展示服务的监控信息

> 整个项目的搭建也很简单，引入一个依赖，书写一个启动类，简单配置端口即可完成
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
</dependency>
```
关于仪表盘的说明：
```text
实心圆：共有两种含义。它通过颜色的变化代表了实例的健康程度，它的健康度从绿色
    该实心圆除了颜色的变化之外，它的大小也会根据实例的请求流量发生变化，流量越大该实心圆就越大。
    所以通过该实心圆的展示，就可以在大量的实例中快速的发现故障实例和高压力实例。
    
曲线：用来记录2分钟内流量的相对变化，可以通过它来观察到流量的上升和下降趋势
```






