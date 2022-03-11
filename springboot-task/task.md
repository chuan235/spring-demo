#### 异步任务

开启异步任务注解：@EnableAsync  采用proxy或者aspectJ代理

标注在方法上表示这是一个异步方法：@Async

标注在类上，类中的方法都是异步方法。

@Async注解需要注意的点

1、如果@Async修饰静态方法，则异步调用失效 

2、@Async修饰的方法所在类不在容器中，则异步调用失效

3、@Async修饰的方法不能与调用异步方法的方法在同一个类中

4、springboot中使用需要使用@EnableAsync

5、在Async 方法上标注@Transactional是没用的。 在Async 方法调用的方法上标注@Transactional 有效

6、使用@Async时要求是不能有返回值的不然会报错的 因为异步要求是不关心结果的


#### 定时任务

开启基于注解的定时任务：@EnableScheduling

任务注解：@Scheduled  可书写cron表达式

```properties
cron表达式:
second(秒), minute（分）, hour（时）, day of month（日）, month（月）, day of week（周几）.
0 * * * * MON-FRI
【0 0/5 14,18 * * ?】 每天14点整，和18点整，每隔5分钟执行一次
【0 15 10 ? * 1-6】 每个月的周一至周六10:15分执行一次
【0 0 2 ? * 6L】每个月的最后一个周六凌晨2点执行一次
【0 0 2 LW * ?】每个月的最后一个工作日凌晨2点执行一次
【0 0 2-4 ? * 1#1】每个月的第一个周一凌晨2点到4点期间，每个整点都执行一次；
```



#### 邮件任务

1、引入spring-boot-starter-mail

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```

2、配置用户名和授权码

....

3、发送邮件

.....