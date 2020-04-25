package top.gmfcj.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @description: rabbitMQ 配置类
 * <p>
 * 15214
 * QueryConfigHeadController
 */
@Configuration
@ComponentScan("top.gmfcj")
public class RabbitConfig {

    public static final String EXCHANGE_A = "kap.direct";
    public static final String EXCHANGE_B = "topic";
    public static final String QUEUE_A = "kap1";
    public static final String QUEUE_B = "kap2";
    public static final String QUEUE_C = "kap3";
    public static final String ROUTINGKEY_A = "key.kap";
    public static final String ROUTINGKEY_B = "key2";
    public static final String ROUTINGKEY_C = "key3";

    //@Value("${spring.rabbitmq.host}")
    private String host = "192.168.222.129";

    //@Value("${spring.rabbitmq.port}")
    private int port = 5672;

    //@Value("${spring.rabbitmq.username}")
    private String username = "root";

    //@Value("${spring.rabbitmq.password}")
    private String password = "root";

    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.setPort(8081);
        return tomcat;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory(this.host, this.port);
        factory.setUsername(this.username);
        factory.setPassword(this.password);
        factory.setVirtualHost("/kap");
        // 开启消息确认
        factory.setPublisherConfirms(true);
        return factory;
    }

    @Bean
//    @Scope("prototype")
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // 开启失败回调
         rabbitTemplate.setMandatory(true);
        // rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
//        rabbitTemplate.setMessageConverter(new MessageConverter() {
//            @Override
//            public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
//                SimpleMessageConverter converter = new SimpleMessageConverter();
//                messageProperties.setContentType("text/xml");
//                return converter.toMessage(o,messageProperties);
//            }
//
//            @Override
//            public Object fromMessage(Message message) throws MessageConversionException {
//                message.getMessageProperties().setContentType("text/xml");
//                return message;
//            }
//        });
        rabbitTemplate.setReturnCallback(new MessageProcess());
        rabbitTemplate.setConfirmCallback(new MessageProcess());
        return rabbitTemplate;
    }

    /**
     * 配置消息转换器
     */
//    @Bean
//    public MessageConverter messageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }

//    @Bean
//    public Queue queueA() {
//        // 队列持久
//        return new Queue(QUEUE_A, true);
//    }

    /**
     * 针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     * FanoutExchange : 将消息分发到所有的绑定队列，无routingkey的概念
     * HeadersExchange ：通过添加属性key-value匹配
     * DirectExchange : 按照routingkey分发到指定队列
     * TopicExchange : 多关键字匹配
     */
//    @Bean
//    public DirectExchange defaultExchange() {
//        return new DirectExchange(EXCHANGE_A);
//    }
//
//    @Bean
//    public Binding binding() {
//        return BindingBuilder.bind(queueA()).to(defaultExchange()).with(RabbitConfig.ROUTINGKEY_A);
//    }

//    @Bean
//    public Binding bindingB(){
//        return BindingBuilder.bind(queueB()).to(defaultExchange()).with(RabbitConfig.ROUTINGKEY_B);
//    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
