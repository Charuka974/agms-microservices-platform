package com.ugch.agms.sensor_service.controller;

import com.ugch.agms.sensor_service.entity.SensorReading;
import com.ugch.agms.sensor_service.service.SensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorService sensorService;

    @GetMapping("/test")
    public String test() {
        return "Sensor Service is UP and Running!";
    }

    /**
     * Manually triggers the process to fetch data from the IoT service,
     * save it to the DB, and send it to the Automation service.
     */
    @PostMapping("/sync")
    public ResponseEntity<String> syncTelemetry() {
        sensorService.fetchAndProcessTelemetry();
        return ResponseEntity.ok("Telemetry sync triggered successfully.");
    }

    /**
     * Retrieves the most recent reading from the database.
     */
    @GetMapping("/latest")
    public ResponseEntity<SensorReading> getLatestReading() {
        SensorReading reading = sensorService.getLatestReading();
        if (reading == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reading);
    }

}
