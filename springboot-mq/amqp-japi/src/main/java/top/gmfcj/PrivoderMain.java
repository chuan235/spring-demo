package top.gmfcj;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class PrivoderMain {

    public static void main(String[] args) throws Exception {
        String message = "hello world";
//        factory.setUri("amqp://root:root@192.168.222.129:5672/kap");
        Connection connection = RabbitConfig.buildConnectionThinkpad();
        // 消息通道
        Channel channel = connection.createChannel();
        // exchange routingKey queue
        String exchangeName = "kap.direct";
        String routingKey = "key.kap";
        String queueName = "kap1";
        // 绑定队列
        channel.queueBind(queueName, exchangeName, routingKey);
        channel.basicPublish(exchangeName, routingKey, null, message.getBytes());
        channel.close();
        connection.close();
    }


}
