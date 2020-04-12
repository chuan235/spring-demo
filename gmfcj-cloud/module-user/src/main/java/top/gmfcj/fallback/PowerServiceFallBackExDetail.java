package top.gmfcj.fallback;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import top.gmfcj.service.PowerFeignService;
import top.gmfcj.util.ResultMap;

/**
 * 如果需要获取调用服务的异常需要使用 FallbackFactory<xxFeignClient>
 */
@Component
public class PowerServiceFallBackExDetail implements FallbackFactory<PowerFeignService> {

    @Override
    public PowerFeignService create(Throwable throwable) {
        return new PowerFeignService() {
            @Override
            public Object power() {
                // 带出失败信息
                String message = throwable.getMessage();
                return ResultMap.error(message);
            }
        };
    }
}
