package top.gmfcj.util;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendMessage {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void send() {
        for (int i = 0; i < 100000; i++) {
            CorrelationData correlationData = new CorrelationData();
            correlationData.setId("业务id");
            rabbitTemplate.convertAndSend("kap.direct", "key3", "hello" + (i + 1), correlationData);
        }
    }

}
