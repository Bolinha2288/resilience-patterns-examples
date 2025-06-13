package com.example.users.external;

import com.example.users.dto.ResponseDTO;
import com.example.users.dto.UserDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
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
    public ResponseDTO sendWelcomeEmailWithCircuitBreaker(UserDTO userDTO) {
        return sendMessage(userDTO);
    }

    private ResponseDTO fallbackSendEmail(UserDTO userDTO, Throwable t) {
        log.warn("Circuit Breaker Fallback: not send e-mail to '{}'. Cause: {}", userDTO.getEmail(), t.getMessage());
        return new ResponseDTO("Fail to send message email.",List.of(false));
    }

    @Retry(name = EMAIL_MANAGER, fallbackMethod = "fallbackSendEmailRetry")
    public ResponseDTO sendWelcomeEmailWithRetry(UserDTO userDTO) {
        return sendMessage(userDTO);
    }

    private ResponseDTO fallbackSendEmailRetry(UserDTO userDTO, Throwable t) {
        log.warn("Retry fallback: not send e-mail to '{}'. Cause: {}", userDTO.getEmail(), t.getMessage());
        return new ResponseDTO("Fail retry send message email.", List.of(false));
    }

    @RateLimiter(name = EMAIL_MANAGER, fallbackMethod = "fallbackSendEmailRateLimiter")
    public ResponseDTO sendWelcomeEmailWithRateLimit(UserDTO userDTO) {
        return sendMessage(userDTO);
    }

    private ResponseDTO fallbackSendEmailRateLimiter(UserDTO userDTO, Throwable t) {
        log.warn("Rate Limiter fallback: not send e-mail to '{}'. Cause: {}", userDTO.getEmail(), t.getMessage());
        return new ResponseDTO("Limit exceded, await moment before send new message email.", List.of(false));
    }

    private ResponseDTO sendMessage(UserDTO userDTO) {
        return webClientBuilder.build()
                .post()
                .uri(emailManagerBaseUrl)
                .bodyValue(userDTO)
                .retrieve()
                .bodyToMono(ResponseDTO.class)
                .doOnNext(response -> log.info("Response from email manager: {}", response))
                .block();
    }
}
