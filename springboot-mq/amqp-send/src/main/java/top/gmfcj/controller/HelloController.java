package top.gmfcj.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Correlation;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.gmfcj.bean.MessageInfo;
import top.gmfcj.config.RabbitConfig;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HelloController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 为每一个注入了rabbitTemplate的类设置失败回调
     */
    //@PostConstruct
    public void init() {
        // 消息发送失败会回调这个方法
        /**
         * message: 发送的消息
         * replyCode: 响应码
         * replyText: 响应的内容
         * exchange: 接收消息的交换机
         * routingKey: 接收消息的路由键
         */
        rabbitTemplate.setReturnCallback((Message message, int replyCode, String replyText, String exchange, String routingKey) -> {
            System.out.println("发送失败打印");

        });
        rabbitTemplate.setConfirmCallback((CorrelationData correlationData, boolean ack, String cause) -> {
            System.out.println("确认接收消息的回调打印");
            if (ack) {
                System.out.println("响应成功");
            } else {
                System.out.println("响应失败,失败原因：" + cause);
            }
        });
    }

    /**
     * localhost:8081/send.do?exchange=kap.direct&routingkey=key.kap&content=hello
     * localhost:8081/hello1.do?total=10&content=hello world
     */
    @GetMapping("/hello1.do")
    @ResponseBody
    public String hello(Integer total, String content) {
//        MessageInfo info = new MessageInfo();
//        info.setContent(content);
//        info.setTotal(total);
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_B, "123.asda.abc", content);
        return "success";
    }


    @GetMapping("/hello2.do")
    @ResponseBody
    public String hello(String content) {
        try {
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_A, RabbitConfig.ROUTINGKEY_B, content.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }




    class MyMessagePostProcessor implements MessagePostProcessor {
        @Override
        public Message postProcessMessage(Message message, Correlation correlation) {
            return null;
        }

        @Override
        public Message postProcessMessage(Message message) throws AmqpException {
            return null;
        }
    }
}
