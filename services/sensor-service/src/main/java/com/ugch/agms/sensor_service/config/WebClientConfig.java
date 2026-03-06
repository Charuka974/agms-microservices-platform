package com.ugch.agms.sensor_service.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    // Keep your WebClient bean from before...
    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }

    // Add the RestTemplate bean
    @Bean
    @LoadBalanced // Allows you to use service names from Eureka
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
