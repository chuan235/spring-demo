package top.gmfcj.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.gmfcj.config.RedisConfig;

/**
 * @description: 消息生产者
 * 使用redis的List数据结构来实现队列
 * lpush -> [] -> rpop
 * lpop  <- [] -> rpush
 */
@Component
public class Producer {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 统计队列中的消息数量
     */
    public long messageTotal() {
        return redisTemplate.opsForList().size(RedisConfig.MESSAGE_KEY);
    }

    /**
     * 发送消息
     *
     * @param message
     */
    public void sendMessage(String message) {
        for (int i = 0; i < 1000; i++) {
            redisTemplate.opsForList().leftPush(RedisConfig.MESSAGE_KEY, message + (i + 1));
        }
    }
}
