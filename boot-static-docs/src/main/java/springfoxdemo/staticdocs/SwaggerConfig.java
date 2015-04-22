package springfoxdemo.staticdocs;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Profile("swagger")  //Only active when the app is started with the swagger profile
@Configuration
@EnableSwagger2
public class SwaggerConfig {

}
