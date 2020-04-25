package top.gmfcj;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class SendMain {

    public static void main(String[] args) throws Exception {
        debugDemo();
    }

    public static void sendOtherTypeExchangeDemo() throws Exception{
        Connection connection = RabbitConfig.buildConnectionLegion();
        Channel channel = connection.createChannel();
        // 定义一个交换机
        channel.exchangeDeclare("exchange.fanout", BuiltinExchangeType.FANOUT);
        // 绑定队列  fanout不需要路由键
        channel.queueBind("queue1","exchange.fanout","");
        channel.queueBind("queue2","exchange.fanout","");
        // 发送消息
        channel.basicPublish("exchange.fanout","",null,"hello world".getBytes());
        channel.close();
        connection.close();
    }

    public static void sendDefaultExchangeDemo() throws Exception{
        Connection connection = RabbitConfig.buildConnectionLegion();
        Channel channel = connection.createChannel();
        // 声明一个队列 queueName durable(队列持久化) exclusive(队列独立,只有) autoDelete(使用后自动删除) argments
        channel.queueDeclare(RabbitConfig.DEFAULT_QUEUE,true,false,false,null);
        // 发送消息
        channel.basicPublish("",RabbitConfig.DEFAULT_QUEUE,null,"abc".getBytes());
        channel.close();
        connection.close();
    }


    public static void debugDemo() throws Exception{
        Connection connection = RabbitConfig.buildConnectionThinkpad();
        Channel channel = connection.createChannel();
        // 声明一个队列 queueName durable(队列持久化) exclusive(队列独立,只有) autoDelete(使用后自动删除) argments
        channel.queueDeclare(RabbitConfig.DEFAULT_QUEUE,true,false,false,null);
        // 发送消息
        channel.basicPublish("",RabbitConfig.DEFAULT_QUEUE,null,"abc".getBytes());
        channel.close();
        connection.close();
    }
}
