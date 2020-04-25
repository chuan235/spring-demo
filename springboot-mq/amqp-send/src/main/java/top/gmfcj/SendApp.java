package top.gmfcj;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import top.gmfcj.config.RabbitConfig;
import top.gmfcj.util.SendMessage;

/**
<mirror>
    <id>alimaven</id>
    <name>aliyun maven</name>
    <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
    <mirrorOf>central</mirrorOf>
</mirror>
 */
@SpringBootApplication
public class SendApp {
    public static void main(String[] args) {
        //SpringApplication.run(SendApp.class, args);
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(RabbitConfig.class);
        SendMessage sendMessage = applicationContext.getBean(SendMessage.class);
        sendMessage.send();
//        applicationContext.close();
    }
}
