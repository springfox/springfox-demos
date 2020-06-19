package io.springfox.demo.bootwebmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class BootWebmvcApplication {

  public static void main(String[] args) {
    SpringApplication.run(BootWebmvcApplication.class, args);
  }

  @RestController
  @RequestMapping("/hello")
  public static class HelloWorldController {
    @GetMapping
    public ResponseEntity<String> hello() {
      return ResponseEntity.ok("Hello SpringFox!");
    }
  }
}
