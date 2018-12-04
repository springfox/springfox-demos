package com.escalon.springfox.springintegration;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.http.dsl.Http;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@SpringBootApplication
@EnableSwagger2WebMvc
@RestController
public class SpringIntegrationWebMvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringIntegrationWebMvcApplication.class, args);
    }


    @Bean
    public IntegrationFlow toUpperGetFlow() {
        return IntegrationFlows.from(
                Http.inboundGateway("/conversions/pathvariable/{upperLower}")
                        .requestMapping(r -> r
                                .methods(HttpMethod.GET)
                                .params("toConvert"))
                        .headerExpression("upperLower",
                                "#pathVariables.upperLower")
                        .payloadExpression("#requestParams['toConvert'][0]")
                        .id("toUpperLowerGateway"))
                .<String>handle((p, h) -> "upper".equals(h.get("upperLower")) ? p.toUpperCase() : p.toLowerCase())
                .get();
    }


    @Bean
    public IntegrationFlow toUpperFlow() {
        return IntegrationFlows.from(
                Http.inboundGateway("/conversions/upper")
                        .requestMapping(r -> r.methods(HttpMethod.POST)
                                .consumes("text/plain"))
                        .requestPayloadType(String.class)
                        .id("toUpperGateway"))
                .<String>handle((p, h) -> p.toUpperCase())
                .get();
    }

    @Bean
    public IntegrationFlow toLowerFlow() {
        return IntegrationFlows.from(
                Http.inboundGateway("/conversions/lower")
                        .requestMapping(r -> r.methods(HttpMethod.POST)
                                .consumes("application/json"))
                        .requestPayloadType(Foo.class)
                        .id("toLowerGateway"))
                .<Foo>handle((p, h) -> new Foo(p.getBar()
                        .toLowerCase()))
                .get();
    }

    @ApiResponses(
            @ApiResponse(code = 200, message = "OK",
                    examples = @Example(@ExampleProperty(mediaType = "application/json",
                            value = "{\"gnarf\": \"dragons\"}"))))
    @PostMapping("/conversion/controller")
    public @ResponseBody
    Baz convert(@RequestBody Baz baz) {
        return baz;
    }

    public static class Baz {
        public String getGnarf() {
            return gnarf;
        }

        public void setGnarf(String gnarf) {
            this.gnarf = gnarf;
        }

        private String gnarf;
    }

    public static class Foo {
        private String bar;
        private boolean foo = false;
        private int count = 3;

        public Foo() {

        }

        public Foo(String bar) {
            this.bar = bar;
        }

        public String getBar() {
            return bar;
        }

        public void setBar(String bar) {
            this.bar = bar;
        }

        public boolean isFoo() {
            return foo;
        }

        public void setFoo(boolean foo) {
            this.foo = foo;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }


}
