package com.escalon.springfox.datarest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 11/29/2018
 */
@SpringBootApplication
@EnableSwagger2WebMvc
@Import(SpringDataRestConfiguration.class)
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }
}
