package springfoxdemo.swagger2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.builders.AuthorizationScopeBuilder;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.annotations.EnableSwagger;
import springfox.documentation.swagger.ui.EnableSwaggerUi;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.petstore.controller.PetController;

import static com.google.common.collect.Lists.*;

@SpringBootApplication
@EnableSwagger
@EnableSwagger2
@EnableSwaggerUi
@ComponentScan(basePackageClasses = {
    PetController.class
})
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public Docket docket() {
    AuthorizationScope[] authScopes = new AuthorizationScope[1];
    authScopes[0] = new AuthorizationScopeBuilder()
        .scope("read")
        .description("read access")
        .build();
    SecurityReference auth = SecurityReference.builder()
        .reference("test")
        .scopes(authScopes)
        .build();
    return new Docket(DocumentationType.SWAGGER_2)
        .securitySchemes(newArrayList(new BasicAuth("test")))
        .securityContext(SecurityContext.builder()
            .withAuthorizations(newArrayList(auth))
            .build());
  }
}