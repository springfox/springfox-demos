package com.escalon.springfox.springintegration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.webflux.dsl.WebFlux;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

@SpringBootApplication
@EnableSwagger2WebFlux
public class SpringIntegrationWebFluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringIntegrationWebFluxApplication.class, args);
    }

    @Bean
    public IntegrationFlow toUpperFlow() {
        return IntegrationFlows.from(
                WebFlux.inboundGateway("/conversions/upper")
                        .requestMapping(r -> r.methods(HttpMethod.POST)
                                .consumes("text/plain")))
                .<String>handle((p, h) -> p.toUpperCase())
                .get();
    }

    @Bean
    public IntegrationFlow toUpperGetFlow() {
        return IntegrationFlows.from(
                WebFlux.inboundGateway("/conversions/pathvariable/{upperLower}")
                        .requestMapping(r -> r
                                .methods(HttpMethod.GET)
                                .params("toConvert"))
                        .headerExpression("upperLower", "#pathVariables.upperLower")
                        .payloadExpression("#requestParams['toConvert'][0]"))
                .<String>handle((p, h) -> "upper".equals(h.get("upperLower")) ? p.toUpperCase() : p.toLowerCase())
                .get();
    }

    public static class Foo {
        private String bar;

        public String getBar() {
            return bar;
        }

        public void setBar(String bar) {
            this.bar = bar;
        }
    }

    @Bean
    public IntegrationFlow toLowerFlow() {
        return IntegrationFlows.from(
                WebFlux.inboundGateway("/conversions/lower")
                        .requestMapping(r -> r.methods(HttpMethod.POST)
                                .consumes("application/json"))
                        .requestPayloadType(Foo.class))
                .<String>handle((p, h) -> p.toUpperCase())
                .get();
    }

    public static class Baz {
        public String getBarf() {
            return barf;
        }

        public void setBarf(String barf) {
            this.barf = barf;
        }

        private String barf;
    }

    @PostMapping("/conversion/controller")
    public @ResponseBody
    Baz convert(@RequestBody Baz baz) {
        return baz;
    }
}
