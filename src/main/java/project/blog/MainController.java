package project.blog;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/blog")
    public void index() {
        System.out.println("index");
    }
}
