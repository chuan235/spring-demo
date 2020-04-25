package top.gmfcj.subscribe;

import io.lettuce.core.pubsub.RedisPubSubAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * @description: 订阅
 */
@Component("sub1")
public class Subscribe1 implements MessageListener {

    private StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

    @Override
    public void onMessage(Message message, byte[] bytes) {
        byte[] body = message.getBody();
        byte[] channel = message.getChannel();
        String topic = (String) stringRedisSerializer.deserialize(channel);
        try {
            System.out.println("我是sub1,监听" + topic + ",我收到消息：" + new String(body,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
