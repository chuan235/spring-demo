package top.gmfcj.subscribe;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParser;
import org.json.JSONString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @description: 发布
 */
@Component("publish")
public class Publish {

    @Autowired
    private RedisTemplate redisTemplate;

    public void sendMessage(String channel, String message) {
        redisTemplate.convertAndSend(channel, message);
    }

    public void sendObject(String channel,Object o){
        redisTemplate.convertAndSend(channel, JSONObject.toJSONString(o));
    }
}
