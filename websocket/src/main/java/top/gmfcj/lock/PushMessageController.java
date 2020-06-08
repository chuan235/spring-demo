package top.gmfcj.lock;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class PushMessageController {

    @GetMapping("/index")
    public void toIndexPage(HttpServletResponse response) throws IOException {
        System.out.println("----");
        response.sendRedirect("index.html");
    }
    @PostMapping("/run/{code}")
    public void toRun(@PathVariable String code, HttpServletResponse response) throws IOException {
        System.out.println(code);
    }

    @GetMapping("/toinfo")
    public void toInfo(HttpServletResponse response) throws IOException {
        System.out.println("to info page");
        response.sendRedirect("info.html");
    }

    @GetMapping("/info")
    @ResponseBody
    public String info() throws IOException {
        System.out.println("查询出正在运行的数据，返回");
        return "success";
    }
}
