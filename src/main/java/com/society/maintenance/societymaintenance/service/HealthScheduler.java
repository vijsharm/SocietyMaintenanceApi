package com.society.maintenance.societymaintenance.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HealthScheduler {

    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRate = 600000) // 10 minutes = 600000 ms
    public void hitHealthEndpoint() {
        try {
            String url = "https://societymaintenanceapi.onrender.com/api/v1/health";

            restTemplate.getForObject(url, String.class);

        } catch (Exception e) {
            System.err.println("Health check failed: " + e.getMessage());
        }
    }
}