package com.ugch.agms.sensor_service.client;

import com.ugch.agms.sensor_service.dto.TelemetryResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class ExternalIoTClient {
    private final WebClient webClient;

    public TelemetryResponseDTO fetchTelemetry(String deviceId) {
        return webClient.get()
                .uri("http://IOT-SERVICE/devices/telemetry/{deviceId}", deviceId) // Service Name from Eureka
                .retrieve()
                .bodyToMono(TelemetryResponseDTO.class)
                .block(); // Blocks here to return the object directly to the Service
    }
}