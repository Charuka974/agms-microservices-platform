package com.ugch.agms.sensor_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutomationRequestDTO {

    private String deviceId;
    private String zoneId;
    private Double temperature;
    private Double humidity;
}
