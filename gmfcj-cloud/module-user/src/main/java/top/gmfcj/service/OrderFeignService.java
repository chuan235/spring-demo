package top.gmfcj.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import top.gmfcj.fallback.OrderServiceFallBack;

/**
 * FeignService 旨在简化http client访问的编写
 * 在使用restTemplate和ribbon访问服务时，每一个controller类中都需要手动去指定项目的名称
 */
@FeignClient(value = "MODULE-ORDER",fallback = OrderServiceFallBack.class)
public interface OrderFeignService {

    @RequestMapping("/order")
    public Object order();
}
