package com.gc.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    DiscoveryClient client;

    @GetMapping("/{name}/8001")
    public String test1(@PathVariable("name") String name){
        String s = restTemplate.getForObject(getUrl("PROVIDER8001")+"/ticket", String.class);
        return name+"购买了"+s;
    }


    @GetMapping("/{name}/test")
    public String test(@PathVariable("name") String name){
        // [provider8002, comsumer]
        List<String> list = client.getServices();
        System.out.println("************"+list);
        List<ServiceInstance> instances = client.getInstances("PROVIDER8002");
        for(ServiceInstance instance:instances){
            // PROVIDER8002----192.168.222.1----8002
            System.out.println(instance.getServiceId()+"----"+instance.getHost()+"----"+instance.getPort()+"----"+instance.getUri());
            System.out.println();
        }
        return "启动完成";
    }

    @GetMapping("/{name}/8002")
    public String test2(@PathVariable("name") String name){
        String s = restTemplate.getForObject(getUrl("PROVIDER8002")+"/ticket", String.class);
        return name+"购买了"+s;
    }

    @GetMapping("/{name}/8003")
    public String test3(@PathVariable("name") String name){
        String s = restTemplate.getForObject(getUrl("PROVIDER8003")+"/ticket", String.class);
        return name+"购买了"+s;
    }

    /**
     * 根据服务id返回对应的url
     * @param serviceId PROVIDER8002
     * @return http://192.168.222.1:8002
     */
    private String getUrl(String serviceId){
        return client.getInstances(serviceId).get(0).getUri().toString();
    }
}
