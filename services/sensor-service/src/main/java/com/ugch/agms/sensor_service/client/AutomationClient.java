package com.ugch.agms.sensor_service.client;

import com.ugch.agms.sensor_service.dto.AutomationRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class AutomationClient {

    private final RestTemplate restTemplate;

    public void sendToAutomation(AutomationRequestDTO dto) {
        restTemplate.postForObject(
                "http://automation-service/api/automation/process",
                dto,
                Void.class
        );
    }
}
