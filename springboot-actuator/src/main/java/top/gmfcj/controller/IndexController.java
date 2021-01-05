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

    @GetMapping("/get/{word}")
    public List<CountResult> hello(@PathVariable String word) {
        return indexService.findByWord(word);
    }

    @GetMapping("/add/{word}")
    public String add(@PathVariable String word) {
        List<CountResult> results = indexService.findByWord(word);
        CountResult result = results.get(0);
        result.setTotal(result.getTotal() + 1);
        indexService.save(result);
        return String.format("%s total Count = %d, id = %d", result.getWord(), result.getTotal(), result.getId());
    }

    @GetMapping("/getCount/{low}")
    public List<CountResult> getLowWords(@PathVariable Integer low) {
        return indexService.findAllByTotalGreaterThan(low);
    }

    @GetMapping("/getOne/{id}/{word}")
    public CountResult getLowWords(@PathVariable Long id, @PathVariable String word) {
        return indexService.findByIdAndWord(id, word);
    }

}
