package top.gmfcj.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.internal.org.objectweb.asm.TypeReference;
import org.springframework.util.ClassUtils;
import top.gmfcj.bean.MessageInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @description: json转对象 对象转json  jackson
 */
public class JsonChange {

    /**
     * 将json字符串转为对象
     */
    public static Object tranObject(String jsonStr, Class clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonStr, clazz);
    }

    /**
     * 将对象转为json字符串
     */
    public static String tranObject(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    /**
     * 将json字符串转为对象
     */
    public static List tranList(String jsonStr, Class clazz) {
        ObjectMapper mapper = new ObjectMapper();
//        if(ClassUtils.isAssignable(Collection.class, clazz)){
//           throw new IllegalArgumentException("请输入正确的class参数");
//        }
        // 防止json字符串中存在换行等特殊字符
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS , true);
        //return mapper.readValue(jsonStr, new TypeReference<List<e>>());
        // 转为数组
        // ObjectMapper ob = new ObjectMapper();
        // MessageInfo[] ls = ob.readValue(jsonStr, MessageInfo[].class);
        return null;
    }

}
