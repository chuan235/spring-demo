package top.gmfcj.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import top.gmfcj.fallback.PowerServiceFallBack;

/**
 * FeignService 旨在简化http client访问的编写
 * 在使用restTemplate和ribbon访问服务时，每一个controller类中都需要手动去指定项目的名称
 * PowerServiceFallBack 没有异常信息的fallback
 * PowerServiceFallBackExDetail 可以获取调用服务的异常信息
 */
@FeignClient(value = "MODULE-POWER",fallback = PowerServiceFallBack.class)
//@FeignClient(value = "MODULE-POWER",fallback = PowerServiceFallBackExDetail.class)
public interface PowerFeignService {

    @RequestMapping("/power")
    public Object power();
}
