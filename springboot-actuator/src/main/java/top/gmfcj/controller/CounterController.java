package top.gmfcj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.gmfcj.mymeter.JobMetrics;

// open API
@RestController
@RequestMapping("/open")
public class CounterController {
    @Autowired
    private JobMetrics jobMetrics;

    @GetMapping(value = "/counter1")
    public void counter1() {
        jobMetrics.job2Counter.increment();
    }

    @GetMapping(value = "/counter2")
    public void counter2() {
        jobMetrics.job2Counter.increment();
    }

    @GetMapping(value = "/gauge")
    public void gauge(@RequestParam(value = "x") String x) {
        System.out.println("gauge controller x" + x);
        jobMetrics.map.put("x", Double.valueOf(x));
    }

}
