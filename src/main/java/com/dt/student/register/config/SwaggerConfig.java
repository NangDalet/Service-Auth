package com.dt.student.register.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SERVICE AUTH API")
                        .version("1.0")
                        .description("API documentation for the SERVICE AUTH system."))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
    @Bean
    public GroupedOpenApi tagSortedApi() {
        return GroupedOpenApi.builder()
                .group("default")
                .addOpenApiCustomizer(openApi -> {
                    if (openApi.getTags() != null) {
                        openApi.getTags().sort((t1, t2) -> {
                            try {
                                Integer num1 = Integer.parseInt(t1.getName().split("\\.")[0].trim());
                                Integer num2 = Integer.parseInt(t2.getName().split("\\.")[0].trim());
                                return num1.compareTo(num2);
                            } catch (Exception e) {
                                return t1.getName().compareTo(t2.getName());
                            }
                        });
                    }
                })
                .build();
    }
}
