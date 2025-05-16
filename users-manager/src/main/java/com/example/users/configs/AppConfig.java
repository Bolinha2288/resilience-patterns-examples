package com.example.users.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Value("${services.email-manager.base-url}")
    private String emailManagerBaseUrl;

    @Bean
    public WebClient emailManagerWebClient() {
        return WebClient.builder().baseUrl(emailManagerBaseUrl).build();
    }

}
