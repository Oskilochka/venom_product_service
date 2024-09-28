package com.josk.venom.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@EnableWebMvc
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customApi() {
        return new OpenAPI().info(new Info()
                .title("Product API")
                .version("0.1")
                .description("Documentation for venom project")
        );
    }
}
