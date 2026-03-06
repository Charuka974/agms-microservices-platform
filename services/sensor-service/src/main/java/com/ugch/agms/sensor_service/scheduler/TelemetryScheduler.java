package com.ugch.agms.sensor_service.scheduler;

import com.ugch.agms.sensor_service.service.SensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelemetryScheduler {

    private final SensorService sensorService;

    @Scheduled(fixedRate = 10000)
    public void fetchTelemetry() {
        sensorService.fetchAndProcessTelemetry();
    }
}