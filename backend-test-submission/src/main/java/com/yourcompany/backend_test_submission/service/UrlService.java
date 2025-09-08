package com.yourcompany.backendtestsubmission.service;

import com.yourcompany.backendtestsubmission.dto.UrlCreateRequest;
import com.yourcompany.backendtestsubmission.dto.UrlCreateResponse;
import org.example.LoggingService;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

@Service
public class UrlService {

    private final ReactiveRedisTemplate<String, String> redisTemplate;
    private final LoggingService logger;
    private static final SecureRandom random = new SecureRandom();
    private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

    public UrlService(ReactiveRedisTemplate<String, String> redisTemplate, LoggingService logger) {
        this.redisTemplate = redisTemplate;
        this.logger = logger;
    }

    public Mono<UrlCreateResponse> createShortUrl(UrlCreateRequest request) {
        logger.log("backend", "info", "service", "Processing short URL creation");
        long validityMinutes = request.getValidity() != null ? request.getValidity() : 30;
        Duration duration = Duration.ofMinutes(validityMinutes);

        Mono<String> shortCodeMono = Mono.justOrEmpty(request.getShortcode())
                .flatMap(this::isShortcodeUnique)
                .filter(Boolean::booleanValue)
                .map(unique -> request.getShortcode())
                .switchIfEmpty(generateUniqueShortcode());

        return shortCodeMono.flatMap(shortCode -> {
            String redisKey = "url:" + shortCode;
            Instant expiry = Instant.now().plus(duration);
            return redisTemplate.opsForValue()
                    .set(redisKey, request.getUrl(), duration)
                    .thenReturn(new UrlCreateResponse("http://localhost:8080/" + shortCode, expiry));
        });
    }

    private Mono<Boolean> isShortcodeUnique(String shortcode) {
        return redisTemplate.hasKey("url:" + shortcode).map(exists -> !exists);
    }

    private Mono<String> generateUniqueShortcode() {
        return Mono.defer(() -> {
                    byte[] buffer = new byte[6];
                    random.nextBytes(buffer);
                    String shortcode = encoder.encodeToString(buffer);
                    return isShortcodeUnique(shortcode).filter(Boolean::booleanValue).map(unique -> shortcode);
                }).repeatWhenEmpty(5, flux -> flux.delayElements(Duration.ofMillis(10)))
                .switchIfEmpty(Mono.error(new RuntimeException("Failed to generate a unique shortcode.")));
    }
}
