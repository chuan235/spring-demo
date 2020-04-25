package top.gmfcj;

import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 测试消息消费
 */
public class TestConsumer1 {

    public static int i = 0;

    public static void main(String[] args) throws Exception {
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
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), true);
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
        channel.basicQos(2000);
        channel.basicConsume("kap3", false, deliverCallback,
                (consumerTag) -> {
                    System.out.println("已取消消费 : " + consumerTag);
                });
    }
}
