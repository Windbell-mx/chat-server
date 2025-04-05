package com.windbell.mm.config;



import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4JConfig {

    @Bean
    public OpenAPI MessageMasterOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("消息通-api")
                .version("1.0")
                .description("消息通-api"));
    }
    @Bean
    public GroupedOpenApi ServerAPI() {
        return GroupedOpenApi.builder().group("server").
                pathsToMatch(
                        "/user/**"
                ).build();
    }

    @Bean
    public GroupedOpenApi ClientAPI() {
        return GroupedOpenApi.builder().group("client").
                pathsToMatch(
                        "/chat/**"
                ).build();
    }
}
