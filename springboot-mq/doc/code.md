```java
@Bean
public ConnectionFactory connectionFactory() {
    CachingConnectionFactory factory = new CachingConnectionFactory("192.168.112.128", 5672);
    factory.setUsername("admin");
    factory.setPassword("admin");
    factory.setVirtualHost("/");
    // 开启消息确认和失败回调
    factory.setPublisherConfirms(true);
    return factory;
}
@Bean
public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    //
    rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            System.out.println("ack="+ack);
            System.out.println("cause="+cause);
            System.out.println("correlationData="+correlationData);
        }
    });
    // 开启失败回调
    rabbitTemplate.setMandatory(true);
    rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(Message message, int replyCode, String replyText, String exchangeName, String soutingKey) {
            System.out.println("路由失败");
            System.out.println("replyCode: "+replyCode);
            System.out.println("replyText: "+replyText);
            System.out.println("exchangeName:"+exchangeName);
            System.out.println("soutingKey:"+soutingKey);
        }
    });
    rabbitTemplate.setMandatory(true);
    return rabbitTemplate;
}
```

发送消息测试

```java
@Component
public class SendMessage {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void send(){
        CorrelationData correlation = new CorrelationData();
        correlation.setId("123");
        rabbitTemplate.convertAndSend("amq.topic", "123abc", "helloworld", correlation);
    }
}
```

消费者

```java
// 批量执行这里，一次性全部确认
if(flag){
    // 发送消息确认
    channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
}else{
    // 批量退回
    channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
    // 单挑退回
    channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
}

```

消息预取

```java
long start = System.currentTimeMillis();
Connection connection = RabbitConfig.buildConnectionThinkpad();
final Channel channel = connection.createChannel();
DeliverCallback deliverCallback = (consumerTag, delivery) -> {
    try {
        //TimeUnit.MILLISECONDS.sleep(200);
        String message = new String(delivery.getBody(), "UTF-8");
        //System.out.println("接收到消息：" + message);
    } catch (Exception ex) {

    } finally {
        i++;
        // 消息确认
        //System.out.println("处理消息完成，开始确认消息");
        // 手动确认，批量确认
        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        // 10000 -> 7801
        // 10000 ->
        if (i % 10000 == 0 || i == 100000) {
            long end = System.currentTimeMillis();
            System.out.println("消费" + i + "条,耗时：" + (end - start));
        }
    }
};
// channel.basicConsume(queueName,autoAck,consumer)
// 设置消息预取，消费完这些消息才会取下一批消息消费 设置的消息预取之后一定要进行手动确认
channel.basicQos(1);
// channel.basicQos(2000);
channel.basicConsume("kap3", false, deliverCallback,
                     (consumerTag) -> {
                         System.out.println("已取消消费 : " + consumerTag);
                     });
}
```

QOS设置为1的时候

```properties
消费10000条,耗时：7832
消费20000条,耗时：14340
消费30000条,耗时：20777
消费40000条,耗时：27993
消费50000条,耗时：34701
消费60000条,耗时：41319
消费70000条,耗时：48031
消费80000条,耗时：54513
消费90000条,耗时：61441
消费100000条,耗时：68092
消费110000条,耗时：74639
```

QOS设置为2000的时候

```properties
消费10000条,耗时：2921
消费20000条,耗时：4395
消费30000条,耗时：15032
消费40000条,耗时：16483
消费50000条,耗时：17623
消费60000条,耗时：18938
消费70000条,耗时：20425
消费80000条,耗时：21526
消费90000条,耗时：22715
消费100000条,耗时：24180
```

### Redis

1、reids的优势

+ 数据都存在内存中，查询和操作的时间复杂度都是O(1)，速度快
+ 丰富的数据类型 string list set hash sortedSet
+ 支持事务，单线程，保证操作的原子性
+ 其他特性：数据持久化、缓存、消息、过期策略

2、redis和memcached

+ memcached所有的值均是字符串，而redis中支持更多的数据类型
+ redis速度比memcached速度快很多
+ reids可以持久化数据

3、常见的redis问题

+ master最好不做任何持久化数据的工作
+ 如果数据比较重要，可以让一个slave开启AOF持久化模式，每秒持久化一次
+ 为了主从复制连接的稳定性，Master和Slave最好在同一局域网内
+ 避免在压力很大的主库上增加从库
+ 主从复制使用单向链表结构更为稳定，即Master << Slave1 << Slave2 << Slave3....

4、回收策略

```properties
volatile-lru：从已设置过期时间的数据集（server.db[i].expires）中挑选最近最少使用的数据淘汰
volatile-ttl：从已设置过期时间的数据集（server.db[i].expires）中挑选将要过期的数据淘汰
volatile-random：从已设置过期时间的数据集（server.db[i].expires）中任意选择数据淘汰
allkeys-lru：从数据集（server.db[i].dict）中挑选最近最少使用的数据淘汰
allkeys-random：从数据集（server.db[i].dict）中任意选择数据淘汰
no-enviction（驱逐）：禁止驱逐数据

# volatile-lru -> Evict using approximated LRU among the keys with an expire set. 	从设置了过期时间的key中移除最近最少使用的一个key
# allkeys-lru -> Evict any key using approximated LRU.								移除最近最少使用的一个key
# volatile-lfu -> Evict using approximated LFU among the keys with an expire set.	从设置了过期时间的key中移除最不经常使用的一个key
# allkeys-lfu -> Evict any key using approximated LFU.								移除最不经常使用的一个key
# volatile-random -> Remove a random key among the ones with an expire set. 从设置了过期时间的key中随机移除一个
# allkeys-random -> Remove a random key, any key. 随机移除一个key
# volatile-ttl -> Remove the key with the nearest expire time (minor TTL) 移除一个剩余时间最短的key
# noeviction -> Don't evict anything, just return an error on write operations. 什么都不在，在写入的时候报错
```





### reids客户端

springboot在1.x.x中默认使用的redis客户端时jedis，现在springboot2.x.x中默认使用lettuce客户端。

区别：

```properties
jedis是直连模式，在多个线程间共享一个jedis实例是线程不安全的
如果想要在多线程的环境下使用jedis，需要使用连接池，每一个线程都去拿自己的jedis实例，当连接数量增多时，物理连接成本较高

lettuce连接是基于Netty，连接实例可以在多个线程之间共享。所以一个多线程的应用可以使用同一个连接实例，而不用担心并发线程的数量。

通过异步的方式可以让我们更好的利用系统资源，而不用浪费线程等待网络或磁盘I/O。Lettuce 是基于 netty 的，netty 是一个多线程、事件驱动的 I/O 框架，所以 Lettuce 可以帮助我们充分利用异步的优势
```

RedisTemplate中的序列化方式

+ GenericToStringSerializer 可以把任何对象泛化为字符串并序列化
+ Jackson2JsonRedisSerializer 序列化Object对象为json字符串
+ JdkSerializationRedisSerializer 序列化java对象
+ StringRedisSerializer 简单的字符串序列化



```shell
subscribe cctv
publish cctv "message"
publish cctv "abd"
```









