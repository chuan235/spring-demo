package top.gmfcj.fallback;

import org.springframework.stereotype.Component;
import top.gmfcj.service.OrderFeignService;

@Component
public class OrderServiceFallBack implements OrderFeignService {

    @Override
    public Object order() {
        return "order service feign fallback";
    }
}
