package com.ugch.agms.zone_service.dto;

import lombok.Data;

@Data
public class ZoneRequest {

    private String name;

    private Double minTemperature;
    private Double maxTemperature;

    private Double minHumidity;
    private Double maxHumidity;
}
