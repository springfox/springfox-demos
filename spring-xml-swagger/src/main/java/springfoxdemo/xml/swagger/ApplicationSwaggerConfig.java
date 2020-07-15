package springfoxdemo.xml.swagger;

import org.springframework.context.annotation.Bean;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
public class ApplicationSwaggerConfig {
  @Bean
  public Docket petStore() {
    return new Docket(DocumentationType.SWAGGER_2);
  }
}
