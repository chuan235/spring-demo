package top.gmfcj;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 以帧(自定义)为单位处理消息
 * FrameHandlerParams
 * FrameHandler
 * 默认的交换机会隐式的绑定所有的队列,并且以队列的名称为路由键,默认的路由键是direct类型
 */
public class RabbitConfig {

    public static final String EXCHANGE_NAME = "jc.direct";
    public static final String ROUTING_KEY = "jc.ptp";
    public static final String QUEUE_NAME = "jcqueque";


    public static final String DEFAULT_QUEUE = "testqueue";

    public static Connection buildConnectionLegion() throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.112.128");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setVirtualHost("/");
        return factory.newConnection();
    }

    public static Connection buildConnectionThinkpad() throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.222.129");
        // 内部会分割成为各种属性
//        factory.setUri("amqp://root:root@192.168.222.129:5672/kap");
        factory.setPort(5672);
        factory.setUsername("root");
        factory.setPassword("root");
        factory.setVirtualHost("/kap");
        return factory.newConnection();
    }

}
