package springfoxdemo.boot.swagger;

import com.google.common.base.Predicate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.common.SwaggerPluginSupport;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger1.annotations.EnableSwagger;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.petstore.controller.PetController;
import springfoxdemo.boot.swagger.web.FileUploadController;
import springfoxdemo.boot.swagger.web.HomeController;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Predicates.or;
import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.builders.PathSelectors.ant;
import static springfox.documentation.builders.PathSelectors.regex;

@SpringBootApplication
@EnableSwagger //Enable swagger 1.2 spec
@EnableSwagger2 //Enable swagger 2.0 spec
@ComponentScan(basePackageClasses = {
        PetController.class,
        HomeController.class,
        FileUploadController.class
})
public class Application {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
    }

    @Component
    @Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1000)
    public class ClientRequestControllerMissingVersionOperationPlugin implements OperationBuilderPlugin {

        @Override
        public void apply(OperationContext operationContext) {
            operationContext
                    .findControllerAnnotation(RequestMapping.class)
                    .toJavaUtil()
                    .filter(this::isVersionedController)
                    .filter(isMissingVersionParam(operationContext))
                    .ifPresent(r -> operationContext.operationBuilder().parameters(newVersionParam()));

        }

        @NotNull
        private List<Parameter> newVersionParam() {
            return Collections.singletonList(new ParameterBuilder()
                    .required(true)
                    .description("API version")
                    .parameterType("path")
                    .name("version")
                    .parameterType("string")
                    .modelRef(new ModelRef("string"))
                    .build());
        }

        @NotNull
        private java.util.function.Predicate<RequestMapping> isMissingVersionParam(OperationContext operationContext) {
            return requestMapping -> operationContext.getParameters().stream().noneMatch(resolvedMethodParameter -> resolvedMethodParameter.defaultName().or("").equals("version"));
        }

        private boolean isVersionedController(RequestMapping requestMapping) {
            return Arrays.stream(requestMapping.value()).anyMatch(path -> path.matches("/\\{version}"));
        }

        @Override
        public boolean supports(DocumentationType documentationType) {
            return true;
        }
    }

    @Bean
    public Docket petApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("full-petstore-api")
                .apiInfo(apiInfo())
                .select()
                .paths(petstorePaths())
                .build()
                .securitySchemes(newArrayList(oauth()))
                .securityContexts(newArrayList(securityContext()));
    }

    @Bean
    public Docket categoryApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("category-api")
                .apiInfo(apiInfo())
                .select()
                .paths(categoryPaths())
                .build()
                .ignoredParameterTypes(ApiIgnore.class)
                .enableUrlTemplating(true);
    }

    @Bean
    public Docket multipartApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("multipart-api")
                .apiInfo(apiInfo())
                .select()
                .paths(multipartPaths())
                .build();
    }

    private Predicate<String> categoryPaths() {
        return or(regex("/category.*"), regex("/category"), regex("/categories"), regex("/\\{version\\}/category.*"));
    }

    private Predicate<String> multipartPaths() {
        return regex("/upload.*");
    }

    @Bean
    public Docket userApi() {
        AuthorizationScope[] authScopes = new AuthorizationScope[1];
        authScopes[0] = new AuthorizationScopeBuilder()
                .scope("read")
                .description("read access")
                .build();
        SecurityReference securityReference = SecurityReference.builder()
                .reference("test")
                .scopes(authScopes)
                .build();

        ArrayList<SecurityContext> securityContexts = newArrayList(SecurityContext.builder().securityReferences
                (newArrayList(securityReference)).build());
        return new Docket(DocumentationType.SWAGGER_2)
                .securitySchemes(newArrayList(new BasicAuth("test")))
                .securityContexts(securityContexts)
                .groupName("user-api")
                .apiInfo(apiInfo())
                .select()
                .paths(userOnlyEndpoints())
                .build();
    }

    private Predicate<String> petstorePaths() {
        return or(
                regex("/api/pet.*"),
                regex("/api/user.*"),
                regex("/api/store.*")
        );
    }

    private Predicate<String> userOnlyEndpoints() {
        return new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return input.contains("user");
            }
        };
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Springfox petstore API")
                .description("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum " +
                        "has been the industry's standard dummy text ever since the 1500s, when an unknown printer "
                        + "took a " +
                        "galley of type and scrambled it to make a type specimen book. It has survived not only five " +
                        "centuries, but also the leap into electronic typesetting, remaining essentially unchanged. " +
                        "It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum " +
                        "passages, and more recently with desktop publishing software like Aldus PageMaker including " +
                        "versions of Lorem Ipsum.")
                .termsOfServiceUrl("http://springfox.io")
                .contact("springfox")
                .license("Apache License Version 2.0")
                .licenseUrl("https://github.com/springfox/springfox/blob/master/LICENSE")
                .version("2.0")
                .build();
    }

    @Bean
    SecurityContext securityContext() {
        AuthorizationScope readScope = new AuthorizationScope("read:pets", "read your pets");
        AuthorizationScope[] scopes = new AuthorizationScope[1];
        scopes[0] = readScope;
        SecurityReference securityReference = SecurityReference.builder()
                .reference("petstore_auth")
                .scopes(scopes)
                .build();

        return SecurityContext.builder()
                .securityReferences(newArrayList(securityReference))
                .forPaths(ant("/api/pet.*"))
                .build();
    }

    @Bean
    SecurityScheme oauth() {
        return new OAuthBuilder()
                .name("petstore_auth")
                .grantTypes(grantTypes())
                .scopes(scopes())
                .build();
    }

    @Bean
    SecurityScheme apiKey() {
        return new ApiKey("api_key", "api_key", "header");
    }

    List<AuthorizationScope> scopes() {
        return newArrayList(
                new AuthorizationScope("write:pets", "modify pets in your account"),
                new AuthorizationScope("read:pets", "read your pets"));
    }

    List<GrantType> grantTypes() {
        GrantType grantType = new ImplicitGrantBuilder()
                .loginEndpoint(new LoginEndpoint("http://petstore.swagger.io/api/oauth/dialog"))
                .build();
        return newArrayList(grantType);
    }

    @Bean
    public SecurityConfiguration securityInfo() {
        return new SecurityConfiguration("abc", "123", "pets", "petstore", "123", ApiKeyVehicle.HEADER, "", ",");
    }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurerAdapter() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry
            .addMapping("/api/pet")
            .allowedOrigins("http://editor.swagger.io");
        registry
            .addMapping("/v2/api-docs.*")
            .allowedOrigins("http://editor.swagger.io");
      }
    };
  }
}