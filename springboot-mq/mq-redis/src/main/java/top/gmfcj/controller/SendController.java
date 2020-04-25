package top.gmfcj.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.gmfcj.message.Producer;

@RestController
public class SendController {

    @Autowired
    private Producer producer;

    @GetMapping("/send.do")
    public String send(){
        producer.sendMessage("hello world");
        return "success";
    }

}
