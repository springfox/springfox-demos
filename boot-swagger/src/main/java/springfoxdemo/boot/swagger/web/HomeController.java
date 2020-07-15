package springfoxdemo.boot.swagger.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    public String home() {
        return "redirect:/swagger-ui/index.html";
    }
}
