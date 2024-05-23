package project.blog.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class MainController {

    @GetMapping("/")
    public String root() {
        return "redirect:/blog_main";
    }

    @GetMapping("/blog_main")
    public String blogMain() {
        return "blog_main";
    }

}
