package com.diabetesapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AnalyticsService {

    @Value("${PYTHON_SERVICE_URL}")
    private String pythonServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getGlucoseAnalytics(Long userId) {
        String url = pythonServiceUrl + "/analytics/glucose/" + userId;
        return restTemplate.getForObject(url, String.class);
    }

    public String getPredictions(Long userId) {
        String url = pythonServiceUrl + "/analytics/predictions/" + userId;
        return restTemplate.getForObject(url, String.class);
    }
}