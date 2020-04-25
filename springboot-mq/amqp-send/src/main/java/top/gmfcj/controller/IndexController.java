package top.gmfcj.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import com.rabbitmq.tools.json.JSONUtil;
import org.apache.tomcat.util.json.JSONParser;
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
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Controller
public class IndexController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 为每一个注入了rabbitTemplate的类设置失败回调,前提是需要将 rabbitTemplate 对象设置为多例对象
     */
//    @PostConstruct
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
            System.out.println("发送消息失败，响应原因:" + replyText);
            System.out.println("replayCode = " + replyCode);
            System.out.println("发送的交换机 = " + exchange + ";发送的路由键 = " + routingKey);

        });
        rabbitTemplate.setConfirmCallback((CorrelationData correlationData, boolean ack, String cause) -> {
            if (ack) {
                System.out.println("响应成功");
            } else {
                System.out.println("响应失败,失败原因：" + cause);
            }
        });
    }


    public static final ObjectMapper objectMapper = new ObjectMapper()
            // 类级别的设置，JsonInclude.Include.NON_EMPTY标识只有非NULL的值才会被纳入json string之中，其余的都将被忽略
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
            // 禁止使用出现未知属性之时，抛出异常
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            // 转化后的json的key命名格式
            .setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);

    /**
     * localhost:8081/send.do?exchange=kap.direct&routingkey=key.kap&content=hello
     * localhost:8081/send.do?total=10&content=hello world
     *
     * @see https://blog.csdn.net/Leon_Jinhai_Sun/article/details/99681584
     */
    @GetMapping("/send.do")
    @ResponseBody
    public String send(Integer total, String content) {

//        MessageInfo info = new MessageInfo();
//        info.setContent(content);
//        info.setTotal(total);
        Map<String, String> data = new HashMap<>();
        data.put("total", total.toString());
        data.put("content", content);
        try {
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_A, RabbitConfig.ROUTINGKEY_C, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    /**
     * 将MessageInfo对象转为byte数组
     */
    public byte[] buildMessageInfoBytes(MessageInfo info) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream outputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            outputStream = new ObjectOutputStream(byteArrayOutputStream);
            outputStream.writeObject(info);
            outputStream.close();
            byteArrayOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[2];
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
