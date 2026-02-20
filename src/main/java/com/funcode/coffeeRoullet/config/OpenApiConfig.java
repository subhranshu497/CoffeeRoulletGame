package com.funcode.coffeeRoullet.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI coffeeRouletteOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Coffee Roulette API")
                        .description("Automated cross-departmental pairing system for team bonding.")
                        .version("v1.0.0")
                        .contact(new Contact().name("FunCode Team").email("subh@funcode.com")));
    }
}
