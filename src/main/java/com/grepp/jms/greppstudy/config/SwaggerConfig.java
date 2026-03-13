package com.grepp.jms.greppstudy.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .name("Authorization")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER);

        return new OpenAPI()
                .info(new Info()
                        .title("단기 심화 부트캠프5 API")
                        .description("Backend5 Swagger API Docs")
                        .version("v1"))
                // .servers() 제거 - Swagger가 자동으로 현재 URL 사용
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME, securityScheme))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
    }
}