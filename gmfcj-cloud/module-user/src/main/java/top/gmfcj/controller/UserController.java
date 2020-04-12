package top.gmfcj.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import top.gmfcj.service.PowerFeignService;
import top.gmfcj.util.ResultMap;

import java.util.Map;

@RestController
public class UserController {

    private final static String POWER_URL = "http://MODULE-POWER";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PowerFeignService powerFeignService;

    @GetMapping("/user")
    public Map<String,String> userMethod(){
        return ResultMap.success("success to user").set("admin","key:abdsafaohf");
    }

    @GetMapping("/user/print")
    public ResultMap printPower(){
        Object orderObject = restTemplate.postForObject("http://localhost:6003/order",null,Object.class);
        Object powerObj = restTemplate.getForObject("http://localhost:6001/power", Object.class);
        return ResultMap.success("操作成功")
                .set("order",orderObject)
                .set("power",powerObj);
    }

    @GetMapping("/user/power")
    @HystrixCommand(fallbackMethod="powerFallBack",
//        commandKey = "gc"
        // 限流
        threadPoolKey = "power",threadPoolProperties = {
            @HystrixProperty(name = "coreSize",value = "1"),
            // 设置-1表示不设置缓冲队列，如果请求数大于coreSize，多余的请求会直接走降级方法
            @HystrixProperty(name="maxQueueSize",value="1")
    })
    public ResultMap loadPower(){
//        Object powerObj = restTemplate.getForObject(POWER_URL+"/power", Object.class);
        Object power = powerFeignService.power();
        return ResultMap.success("操作成功")
                .set("power",power);
    }

    /**
     * 这里这个降级方法可以将请求的参数传递过来
     * @return
     */
    public ResultMap powerFallBack(){
        // 因为power服务不可用了
        // 这里可以返回默认的查询信息
        // 或者提示服务不可用
        return ResultMap.success("该服务已降级或者超时或者熔断或者限流");
    }

    @GetMapping("/power/feign")
    public Object power(){
        return powerFeignService.power();
    }
}
