package top.gmfcj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PowerController {

    @GetMapping("/power")
    @ResponseBody
    public Map index(){
        Map<String,String> map = new HashMap<>();
        map.put("msg","hello power copy!");
        return map;
    }
}
