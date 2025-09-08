package org.example;

import org.example.dto.LogRequest;
import org.example.dto.LogResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class LoggingService {

    private static final Logger logger = LoggerFactory.getLogger(LoggingService.class);
    private final WebClient webClient;

    private final String bearerToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJNYXBDbGFpbXMiOnsiYXVkIjoiaHR0cDovLzIwLjI0NC41Ni4xNDQvZXZhbHVhdGlvbi1zZXJ2aWNlIiwiZW1haWwiOiJzYW12ZWQ0NDRAZ21haWwuY29tIiwiZXhwIjoxNzU3MzIwOTk2LCJpYXQiOjE3NTczMjAwOTYsImlzcyI6IkFmZm9yZCBNZWRpY2FsIFRlY2hub2xvZ2llcyBQcml2YXRlIExpbWl0ZWQiLCJqdGkiOiIyYTQ2NjE3MS1jY2EzLTQyZjItYWU3YS05MDFmYjU2ZDAwOGYiLCJsb2NhbGUiOiJlbi1JTiIsIm5hbWUiOiJzYW12ZWQgZ3VwdGEiLCJzdWIiOiJlMWFlOWZmNy05NjUwLTRkMTAtOTU2Yi0yZDQ0NjQwYTdkM2QifSwiZW1haWwiOiJzYW12ZWQ0NDRAZ21haWwuY29tIiwibmFtZSI6InNhbXZlZCBndXB0YSIsInJvbGxObyI6IjIzMDE2NDAxMzkwMDciLCJhY2Nlc3NDb2RlIjoic0FXVHVSIiwiY2xpZW50SUQiOiJlMWFlOWZmNy05NjUwLTRkMTAtOTU2Yi0yZDQ0NjQwYTdkM2QiLCJjbGllbnRTZWNyZXQiOiJScEpuWFFZVG5adlJWRXZuIn0.aGUd5ciMZ_DAX9x8tThkLmcgiyFHC9_jnOppvlTp6sk";

    public LoggingService(WebClient.Builder webClientBuilder, @Value("${evaluation.service.base-url}") String baseUrl) {
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public void log(String stack, String level, String pkg, String message) {
        if (bearerToken == null || bearerToken.isBlank()) {
            logger.error("Auth token is not set. Cannot send log.");
            return;
        }

        LogRequest logRequest = new LogRequest(stack, level, pkg, message);

        webClient.post()
                .uri("/logs")
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .body(Mono.just(logRequest), LogRequest.class)
                .retrieve()
                .bodyToMono(LogResponse.class)
                .subscribe(
                        response -> logger.info("Log sent successfully. LogID: {}", response.getLogID()),
                        error -> logger.error("Failed to send log: {}", error.getMessage())
                );
    }
}

