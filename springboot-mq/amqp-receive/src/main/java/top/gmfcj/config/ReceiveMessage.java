package top.gmfcj.config;

import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import top.gmfcj.bean.MessageInfo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Component
@RabbitListener(queues = "kap3", containerFactory = "rabbitListenerContainerFactory")
public class ReceiveMessage {

    @RabbitHandler
    public void proccess(Message message, Channel channel) throws IOException {
        System.out.println("接收处理队列KAP2当中的message消息");
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(message.getBody()));
            //int a = 1 / 0;
            System.out.println(ois.readObject());
        } catch (Exception e) {

        }
    }

//    @RabbitHandler
//    public void proccessMessgeInfo(@Payload MessageInfo info) throws IOException {
//        System.out.println("接收处理队列KAP2当中MessageInfo的消息： " + info);
//    }

    @RabbitHandler
    public void proccessText(String content, Message message, Channel channel) throws IOException {
        try {
            TimeUnit.SECONDS.sleep(1);
            System.out.println("接收处理队列KAP2当中的String消息： " + content);
            // 发送确认消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            // 如果出现异常则重新回到队列  参数 deliveryTag multiple: 是否批量操作  requeue: true 消息重回队列，false 将消息丢弃
            // channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            // System.out.println("尝试重发：" + message.getMessageProperties().getConsumerQueue());
            // 直接丢弃消息
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        }


    }

    @RabbitHandler
    public void proccessHashMap(HashMap map) throws IOException {
        System.out.println("接收处理队列KAP2当中的HashMap消息： " + map);
    }
    @RabbitHandler
    public void proccessByteArray(byte[] bytes) throws IOException {
        System.out.println("接收处理队列KAP2当中的byte数组消息： " + new String(bytes,"utf-8"));
    }

}
