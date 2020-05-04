package com.gc.viewer.action;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FreeMarkerController {

    @GetMapping("/reg")
    public String reg(Model model){
        model.addAttribute("title", "FreeMarker Title");
        return "reg";
    }
}
