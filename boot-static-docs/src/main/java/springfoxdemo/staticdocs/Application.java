package springfoxdemo.staticdocs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import springfox.petstore.controller.PetController;

@SpringBootApplication
@ComponentScan(basePackageClasses = {
        SwaggerConfig.class, PetController.class
})
public class Application {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
    }
}
