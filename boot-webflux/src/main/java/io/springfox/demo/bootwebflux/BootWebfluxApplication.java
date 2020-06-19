package io.springfox.demo.bootwebflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class BootWebfluxApplication {

  public static void main(String[] args) {
    SpringApplication.run(BootWebfluxApplication.class, args);
  }

  @RestController
  @RequestMapping("/functional-hello")
  public static class FunctionalHelloWorldController {
    @GetMapping
    public Mono<String> hello() {
      return Mono.fromSupplier(() -> "Hello SpringFox!");
    }
  }

  @Bean
  public RouterFunction<ServerResponse> route(GreetingHandler greetingHandler) {
    return RouterFunctions
        .route(RequestPredicates.GET("/hello")
                .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
            greetingHandler::hello);
  }

  @Component
  public static class GreetingHandler {

    public Mono<ServerResponse> hello(ServerRequest request) {
      return ServerResponse.ok()
          .contentType(MediaType.TEXT_PLAIN)
          .body(BodyInserters.fromValue("Hello, SpringFox!"));
    }
  }
}
