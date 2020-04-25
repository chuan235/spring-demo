package top.gmfcj;

import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 测试fanout路由器
 */
public class ConsumerQueue {

    @Test
    public void consumerQueue1() throws Exception {
        Connection connection = RabbitConfig.buildConnectionLegion();
        final Channel channel = connection.createChannel();
        // 书写消息处理逻辑
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("queue1 监听到消息:" + new String(body, "UTF-8"));
            }
        };
        // 开启监听 自动确认
        channel.basicConsume("queue1", true, consumer);
    }


    @Test
    public void consumerQueue2() throws Exception {
        Connection connection = RabbitConfig.buildConnectionLegion();
        final Channel channel = connection.createChannel();
        // 书写消息处理逻辑
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("queue2 监听到消息:" + new String(body, "UTF-8"));
            }
        };
        // 开启监听 自动确认
        channel.basicConsume("queue2", true, consumer);

    }

    @Test
    public void consumerQueue3() throws Exception {
        Connection connection = RabbitConfig.buildConnectionLegion();
        final Channel channel = connection.createChannel();
        // 书写消息处理逻辑
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("queue3 监听到消息:" + new String(body, "UTF-8"));
            }
        };
        // 开启监听 自动确认
        channel.basicConsume("queue3", true, consumer);
    }
}
