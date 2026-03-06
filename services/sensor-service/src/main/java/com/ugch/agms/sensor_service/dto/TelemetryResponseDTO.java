package com.ugch.agms.sensor_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TelemetryResponseDTO {

    private String deviceId;
    private String zoneId;
    private Value value;
    private LocalDateTime capturedAt;

    @Data
    public static class Value {
        private Double temperature;
        private Double humidity;
    }
}
