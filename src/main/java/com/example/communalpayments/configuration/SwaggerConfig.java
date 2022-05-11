package com.example.communalpayments.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket apiTran() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("communal-payments")
                .select().apis(RequestHandlerSelectors.basePackage("com.example.communalpayments.web"))
                .build();
    }
}
