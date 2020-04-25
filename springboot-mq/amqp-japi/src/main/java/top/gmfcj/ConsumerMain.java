package top.gmfcj;

import com.rabbitmq.client.*;

import java.io.IOException;

public class ConsumerMain {

    public static void main(String[] args) throws Exception {

    }

    /**
     * 消费消息demo
     *
     * @throws Exception
     */
    public static void demo1() throws Exception {
        Connection connection = RabbitConfig.buildConnectionLegion();
        final Channel channel = connection.createChannel();
        // 接收消息  官网demo
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            try{
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println("接收到消息：" + message);
            }finally {
                // 消息确认
                System.out.println("处理消息完成，开始确认消息");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("已取消消费 : " + consumerTag);
        };
        channel.basicConsume(RabbitConfig.DEFAULT_QUEUE, true, deliverCallback, cancelCallback);

        // 书写消息处理逻辑
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            /**
             * 监听类
             * @param consumerTag 消费者标识
             * @param envelope 交换机 路由键 消息发送者的标识
             * @param properties 消息的参数
             * @param body 消息的内容
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(new String(body, "UTF-8"));
                // ----------
                System.out.println("消费消息成功");
                // boolean 值表示是否需要批量确认  手动确认消息消费
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        // 开启监听
        // channel.basicConsume(queueName,autoAck,consumer)
        channel.basicConsume(RabbitConfig.DEFAULT_QUEUE, consumer);
    }
}
