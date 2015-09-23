package springfoxdemo.boot.swagger.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController {
    @RequestMapping(value = "/category/Resource", method = RequestMethod.GET)
    public ResponseEntity<String> search(@RequestParam(value = "someEnum") Category someEnum) {
        return ResponseEntity.ok(someEnum.name());
    }
}
