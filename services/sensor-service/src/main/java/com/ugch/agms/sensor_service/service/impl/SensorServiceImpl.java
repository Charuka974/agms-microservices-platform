package com.ugch.agms.sensor_service.service.impl;

import com.ugch.agms.sensor_service.client.AutomationClient;
import com.ugch.agms.sensor_service.client.ExternalIoTClient;
import com.ugch.agms.sensor_service.dto.AutomationRequestDTO;
import com.ugch.agms.sensor_service.dto.TelemetryResponseDTO;
import com.ugch.agms.sensor_service.entity.SensorReading;
import com.ugch.agms.sensor_service.repository.SensorReadingRepository;
import com.ugch.agms.sensor_service.service.SensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SensorServiceImpl implements SensorService {

    private final ExternalIoTClient externalIoTClient;
    private final AutomationClient automationClient;
    private final SensorReadingRepository repository;

    @Override
    public void fetchAndProcessTelemetry() {
        // 1. You need a device ID from somewhere (config, database, or param)
        String targetDeviceId = "DEV-101";

        // 2. Fetch (This blocks until the response returns)
        TelemetryResponseDTO response = externalIoTClient.fetchTelemetry(targetDeviceId);

        if (response != null) {
            // 3. Map and Save
            SensorReading reading = SensorReading.builder()
                    .deviceId(response.getDeviceId())
                    .zoneId(response.getZoneId())
                    .temperature(response.getValue().getTemperature())
                    .humidity(response.getValue().getHumidity())
                    .capturedAt(LocalDateTime.now())
                    .build();

            repository.save(reading);

            // 4. Trigger Automation
            AutomationRequestDTO automationDTO = AutomationRequestDTO.builder()
                    .deviceId(reading.getDeviceId())
                    .zoneId(reading.getZoneId())
                    .temperature(reading.getTemperature())
                    .humidity(reading.getHumidity())
                    .build();

            automationClient.sendToAutomation(automationDTO);
        }
    }

    @Override
    public SensorReading getLatestReading() {
        return repository.findTopByOrderByCapturedAtDesc()
                .orElse(null);
    }
}