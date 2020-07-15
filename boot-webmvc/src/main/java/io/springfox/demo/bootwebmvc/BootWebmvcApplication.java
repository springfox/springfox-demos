package io.springfox.demo.bootwebmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;

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

    @GetMapping("/double")
    public ResponseEntity<String> testDouble(@RequestParam(value = "test", defaultValue = "10") Double count) {
      return ResponseEntity.ok("Value " + count);
    }
  }
  @Bean
  public SecurityConfiguration securityConfiguration() {
    return SecurityConfigurationBuilder.builder()
        .enableCsrfSupport(true)
        .build();
  }

//  NOTE: Uncomment to personalize. OAS_30 (OpenAPI is the default spec version)
//  @Bean
//  public Docket docket() {
//    return new Docket(DocumentationType.SWAGGER_2);
//  }}
}
