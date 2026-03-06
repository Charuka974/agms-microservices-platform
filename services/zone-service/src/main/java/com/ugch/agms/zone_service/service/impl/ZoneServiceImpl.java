package com.ugch.agms.zone_service.service.impl;

import com.ugch.agms.zone_service.client.IOTClient;
import com.ugch.agms.zone_service.dto.DeviceResponse;
import com.ugch.agms.zone_service.dto.ZoneRequest;
import com.ugch.agms.zone_service.entity.Zone;
import com.ugch.agms.zone_service.repository.ZoneRepository;
import com.ugch.agms.zone_service.service.ZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ZoneServiceImpl implements ZoneService {

    private final ZoneRepository repository;
    private final IOTClient iotClient;

    @Override
    public Zone createZone(ZoneRequest request, String token) {

        // Business Validation
        if (request.getMinTemperature() >= request.getMaxTemperature()) {
            throw new RuntimeException("minTemperature must be less than maxTemperature");
        }

        if (request.getMinHumidity() >= request.getMaxHumidity()) {
            throw new RuntimeException("minHumidity must be less than maxHumidity");
        }

        // Register device in external IoT API
        Map<String, String> deviceRequest = new HashMap<>();
        deviceRequest.put("name", request.getName() + "-Sensor");
        deviceRequest.put("zoneId", request.getName());

        DeviceResponse deviceResponse = iotClient.registerDevice("Bearer " + token, deviceRequest);

        // Save Zone
        Zone zone = Zone.builder()
                .name(request.getName())
                .minTemperature(request.getMinTemperature())
                .maxTemperature(request.getMaxTemperature())
                .minHumidity(request.getMinHumidity())
                .maxHumidity(request.getMaxHumidity())
                .deviceId(deviceResponse.getDeviceId())
                .build();

        return repository.save(zone);
    }

    @Override
    public Zone getZone(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zone not found"));
    }

    @Override
    public Zone updateZone(Long id, ZoneRequest request) {

        Zone zone = getZone(id);

        zone.setMinTemperature(request.getMinTemperature());
        zone.setMaxTemperature(request.getMaxTemperature());
        zone.setMinHumidity(request.getMinHumidity());
        zone.setMaxHumidity(request.getMaxHumidity());

        return repository.save(zone);
    }

    @Override
    public void deleteZone(Long id) {
        repository.deleteById(id);
    }

}