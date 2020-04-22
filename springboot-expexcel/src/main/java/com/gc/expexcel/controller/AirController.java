package com.gc.expexcel.controller;

import com.gc.expexcel.bean.AirQuality;
import com.gc.expexcel.service.AirService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
public class AirController {

    @Autowired
    @Setter
    private AirService airService;

    @GetMapping("/air/show")
    public String showAirData(Model model){
        List<AirQuality> allData = airService.getAllData();
        if(allData!=null && allData.size()>0){
            log.info("get all data success");
            model.addAttribute("data",allData);
        }
        return "show";

    }

}
