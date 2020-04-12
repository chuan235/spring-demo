package top.gmfcj.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class OrderController {

    @PostMapping("/order")
    public Map<String,String> getOrder(){
        Map<String,String> orderInfo = new HashMap<>();
        orderInfo.put("orderId","20190823123421321000");
        orderInfo.put("orderUser","1");
        return orderInfo;
    }

}
