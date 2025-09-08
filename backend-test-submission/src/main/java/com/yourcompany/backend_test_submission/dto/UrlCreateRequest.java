package com.yourcompany.backendtestsubmission.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlCreateRequest {
    private String url;
    private Integer validity;
    private String shortcode;
}
