package com.yourcompany.backendtestsubmission.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlStatsResponse {
    private long totalClicks;
    private String originalUrl;
    private Instant createdAt;
    private Instant expiresAt;
    private List<ClickDetail> detailedClicks;
}
