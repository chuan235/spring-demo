package top.gmfcj.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class WebSocketController {

    @Autowired
    SimpMessagingTemplate template;

    // 关锁
    @RequestMapping("/stop")
    @ResponseBody
    public String stop(){
        System.out.println("---------开始发送计费信号----------");
        template.convertAndSend("/stop/back/123", "success");
        return "success";
    }
}
