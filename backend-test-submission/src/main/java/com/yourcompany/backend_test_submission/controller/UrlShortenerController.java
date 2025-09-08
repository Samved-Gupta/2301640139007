package com.yourcompany.backendtestsubmission.controller;

import com.yourcompany.backendtestsubmission.dto.UrlCreateRequest;
import com.yourcompany.backendtestsubmission.dto.UrlCreateResponse;
import org.example.LoggingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;

@RestController
@RequestMapping("/shorturls")
public class UrlShortenerController {

    private final LoggingService logger;

    public UrlShortenerController(LoggingService loggingService) {
        this.logger = loggingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UrlCreateResponse createShortUrl(@RequestBody UrlCreateRequest request) {
        logger.log("backend", "info", "controller", "Received request to create short URL");

        // Placeholder logic
        String shortLink = "http://localhost:8080/" + (request.getShortcode() != null ? request.getShortcode() : "abcde1");
        Instant expiry = Instant.now().plusSeconds(30 * 60);

        return new UrlCreateResponse(shortLink, expiry);
    }
}
