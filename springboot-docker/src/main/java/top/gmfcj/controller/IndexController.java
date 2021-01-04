package top.gmfcj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import top.gmfcj.bean.CountResult;
import top.gmfcj.service.IndexService;

import java.util.List;

@RestController
public class IndexController {

    @Autowired
    private IndexService indexService;

    @GetMapping("/hello")
    public String hello(){
        return "hello page";
    }

    @GetMapping("/add/{word}")
    public String add(@PathVariable String word){
        List<CountResult> results = indexService.findByWord(word);
        CountResult result = results.get(0);
        result.setTotal(result.getTotal() + 1);
        indexService.save(result);
        return String.format("%s total Count = %d, id = %d", result.getWord(), result.getTotal(), result.getId());
    }

}
