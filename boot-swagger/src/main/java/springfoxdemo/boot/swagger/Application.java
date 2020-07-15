package springfoxdemo.boot.swagger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.AuthorizationScopeBuilder;
import springfox.documentation.builders.ImplicitGrantBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.Contact;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.LoginEndpoint;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger1.annotations.EnableSwagger;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.petstore.controller.PetController;
import springfox.petstore.controller.PetStoreResource;
import springfox.petstore.controller.UserController;
import springfoxdemo.boot.swagger.web.HomeController;

import javax.print.Doc;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static springfox.documentation.builders.PathSelectors.*;

@SpringBootApplication
@EnableSwagger //Enable swagger 1.2 spec
@EnableSwagger2 //Enable swagger 2.0 spec
@EnableOpenApi //Enable open api 3.0.3 spec
public class Application {
  public static void main(String[] args) {
    ApplicationContext ctx = SpringApplication.run(Application.class, args);
  }

  @Bean
  public PetController petController() {
    return new PetController();
  }

  @Bean
  public PetStoreResource petStoreController() {
    return new PetStoreResource();
  }

  @Bean
  public UserController userController() {
    return new UserController();
  }

  @Bean
  public Docket petApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .groupName("full-petstore-api")
        .apiInfo(apiInfo())
        .select()
        .paths(petstorePaths())
        .build()
        .securitySchemes(Collections.singletonList(oauth()))
        .securityContexts(Collections.singletonList(securityContext()));
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
    return regex(".*/category.*")
        .or(regex(".*/category")
            .or(regex(".*/categories")));
  }

  private Predicate<String> multipartPaths() {
    return regex(".*/upload.*");
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

    List<SecurityContext> securityContexts =
        Collections.singletonList(
            SecurityContext.builder()
                .securityReferences(Collections.singletonList(securityReference))
                .build());
    return new Docket(DocumentationType.SWAGGER_2)
        .securitySchemes(Collections.singletonList(new BasicAuth("test")))
        .securityContexts(securityContexts)
        .groupName("user-api")
        .apiInfo(apiInfo())
        .select()
        .paths(input -> input.contains("user"))
        .build();
  }

  @Bean
  public Docket openApiPetStore() {
    return new Docket(DocumentationType.OAS_30)
        .groupName("open-api-pet-store")
        .select()
        .paths(petstorePaths())
        .build();
  }

  private Predicate<String> petstorePaths() {
    return regex(".*/api/pet.*")
        .or(regex(".*/api/user.*")
            .or(regex(".*/api/store.*")));
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
        .contact(new Contact("springfox", "", ""))
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
        .securityReferences(Collections.singletonList(securityReference))
        .forPaths(ant(".*/api/pet.*"))
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
    return Arrays.asList(
        new AuthorizationScope("write:pets", "modify pets in your account"),
        new AuthorizationScope("read:pets", "read your pets"));
  }

  List<GrantType> grantTypes() {
    GrantType grantType = new ImplicitGrantBuilder()
        .loginEndpoint(new LoginEndpoint("http://petstore.swagger.io/api/oauth/dialog"))
        .build();
    return Collections.singletonList(grantType);
  }

  @Bean
  public SecurityConfiguration securityInfo() {
    return SecurityConfigurationBuilder.builder()
        .clientId("abc")
        .clientSecret("123")
        .realm("pets")
        .appName("petstore")
        .scopeSeparator(",")
        .build();
  }
}