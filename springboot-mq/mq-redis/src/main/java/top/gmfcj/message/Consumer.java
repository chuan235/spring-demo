package top.gmfcj.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.gmfcj.config.RedisConfig;

/**
 * @description: 从消息队列中取出消息
 */
@Component
public class Consumer {

    @Autowired
    private RedisTemplate redisTemplate;

    public Object resiveMessage() {

        for (; ; ) {
            Object element = redisTemplate.opsForList().rightPop(RedisConfig.MESSAGE_KEY);
            if (element != null) {
                System.out.println("成功拿到消息：" + element);
                return element;
            }
        }
    }

    public void resive() {
        long start = System.currentTimeMillis();
        for (int i = 0; ; i++) {
            Object element = redisTemplate.opsForList().rightPop(RedisConfig.MESSAGE_KEY);
            if (i % 500 == 0 || element == null) {
                long end = System.currentTimeMillis();
                System.out.println("消费" + i + "条消息，花费时间：" + (end - start));
            }
            if (i == 1000) {
                break;
            }
        }
    }

}
