package io.springfox.demo.bootwebflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootApplication
public class BootWebfluxApplication {

  public static void main(String[] args) {
    SpringApplication.run(BootWebfluxApplication.class, args);
  }

  @RestController
  @RequestMapping("/functional-hello")
  public static class FunctionalHelloWorldController {
    @GetMapping("/response-mono")
    public ResponseEntity<Mono<String>> helloMono() {
      return ResponseEntity.ok(Mono.fromSupplier(() -> "Hello SpringFox!"));
    }

    @GetMapping("/mono")
    public Mono<String> helloPerson(String name) {
      return Mono.just("Hello " + name + "!");
    }

    @GetMapping("/flux")
    public Flux<String> helloPeople(String... names) {
      return Flux.fromArray(names).map(name -> "Hello " + name);
    }

    @GetMapping("/response-flux")
    public ResponseEntity<Flux<String>> helloPeople(@RequestParam List<String> names) {
      return ResponseEntity.of(Optional.of(Flux.fromStream(names.stream().map(name -> "Hello " + name))));
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

//  NOTE: Uncomment to personalize. OAS_30 (OpenAPI is the default spec version)
//  @Bean
//  public Docket docket() {
//    return new Docket(DocumentationType.SWAGGER_2);
//  }
}
