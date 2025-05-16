package com.example.account.external;

import com.example.account.dto.ResponseDTO;
import com.example.account.dto.UserDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@Slf4j
public class EmailClient {

    private final WebClient.Builder webClientBuilder;

    private static final String EMAIL_MANAGER = "emailManagerService";

    @Value("${services.email-manager.base-url}")
    private String emailManagerBaseUrl;

    public EmailClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @CircuitBreaker(name = EMAIL_MANAGER, fallbackMethod = "fallbackSendEmail")
    public ResponseDTO sendWelcomeEmail(UserDTO userDTO) {
        return webClientBuilder.build()
                .post()
                .uri(emailManagerBaseUrl)
                .bodyValue(userDTO)
                .retrieve()
                .bodyToMono(ResponseDTO.class)
                .doOnNext(response -> log.info("log response email-manager: {}", response))
                .block();
    }

    private ResponseDTO fallbackSendEmail(UserDTO userDTO, Throwable t) {
        log.warn("Fallback: não foi possível enviar o e-mail para '{}'. Causa: {}", userDTO.getEmail(), t.getMessage());
        ResponseDTO fallbackResponse = new ResponseDTO(
                "Falha ao enviar e-mail: fallback ativado.",
                List.of(false)
        );

        return fallbackResponse;
    }

}