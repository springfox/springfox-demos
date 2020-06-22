package springfoxdemo.java.swagger;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@ComponentScan
@EnableWebMvc
@EnableSwagger2WebMvc
public class ApplicationConfiguration {
}
