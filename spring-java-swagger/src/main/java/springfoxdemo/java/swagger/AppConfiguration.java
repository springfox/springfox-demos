package springfoxdemo.java.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableWebMvc
@ComponentScan("springfoxdemo.java.swagger")
@EnableSwagger2WebMvc
public class AppConfiguration {
  @Bean
  public Docket docket() {
    return new Docket(DocumentationType.SWAGGER_2);
  }
}
