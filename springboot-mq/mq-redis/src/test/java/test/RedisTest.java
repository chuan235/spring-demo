package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import top.gmfcj.RedisApp;
import top.gmfcj.message.Consumer;
import top.gmfcj.message.Producer;
import top.gmfcj.subscribe.Publish;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RedisApp.class})// 指定启动类
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void connectionTest(){
        //ValueOperations opsForValue = redisTemplate.opsForValue();
    }


    @Autowired
    private Producer producer;

    @Test
    public void sendTest(){
        producer.sendMessage("helloworld");
    }

    @Autowired
    private Consumer consumer;
    @Test
    public void consumer(){
        Object message = consumer.resiveMessage();
        System.out.println(message);
    }

    @Test
    public void resive(){
        consumer.resive();
    }

    @Autowired
    private Publish publish;
    @Test
    public void publishMessage(){
        publish.sendMessage("cctv4", "消息内容");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
