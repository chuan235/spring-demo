package top.gmfcj.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory(){
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.setPort(8082);
        return tomcat;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory(this.host, this.port);
        factory.setUsername(this.username);
        factory.setPassword(this.password);
        factory.setVirtualHost("/kap");
        return factory;
    }

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory){
        SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
        containerFactory.setConnectionFactory(connectionFactory);
        // 手动确认消息
        containerFactory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return containerFactory;
    }

    /**
     * 绑定多个队列
     */
    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory){
        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory);
//        listenerContainer.addQueueNames("queue1","queue2","queue3","queue4");
//        listenerContainer.addQueues(new Queue("queue1"));
        return listenerContainer;
    }



    @Bean(name = "singleListenerContainer")
    public SimpleRabbitListenerContainerFactory listenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        setConnectionFactory：设置spring-amqp的ConnectionFactory。
        factory.setConnectionFactory(connectionFactory());
        //消息序列化类型
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        //setConcurrentConsumers：设置每个MessageListenerContainer将会创建的Consumer的最小数量，默认是1个。
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(1);
        //setPrefetchCount：设置每次请求发送给每个Consumer的消息数量。
        factory.setPrefetchCount(1);
        //是否设置Channel的事务。
        factory.setChannelTransacted(false);
        //setTxSize：设置事务当中可以处理的消息数量。
        factory.setTxSize(1);
        //设置当rabbitmq收到nack/reject确认信息时的处理方式，设为true，扔回queue头部，设为false，丢弃。
        factory.setDefaultRequeueRejected(true);
        //setErrorHandler：实现ErrorHandler接口设置进去，所有未catch的异常都会由ErrorHandler处理。
        //factory.setErrorHandler();
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return factory;
    }

}
