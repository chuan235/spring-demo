package top.gmfcj.fallback;

import org.springframework.stereotype.Component;
import top.gmfcj.service.PowerFeignService;

@Component
public class PowerServiceFallBack implements PowerFeignService {

    @Override
    public Object power() {
        return "power service feign fallback";
    }
}
