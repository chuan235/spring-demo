package top.gmfcj.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class MessageProcess implements RabbitTemplate.ReturnCallback, RabbitTemplate.ConfirmCallback {

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        System.out.println("发送失败打印");
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        System.out.println("确认接收消息的回调打印,correlationData="+correlationData);
        if (ack) {
            System.out.println("响应成功");
        } else {
            System.out.println("响应失败,失败原因：" + cause);
        }
    }
}
