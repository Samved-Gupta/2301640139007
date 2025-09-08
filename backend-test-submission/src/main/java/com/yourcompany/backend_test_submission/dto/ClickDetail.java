package com.yourcompany.backendtestsubmission.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClickDetail {
    private Instant timestamp;
    private String referrer;
    private String location;
}
