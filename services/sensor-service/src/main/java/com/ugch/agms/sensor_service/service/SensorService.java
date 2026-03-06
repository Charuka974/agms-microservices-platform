package com.ugch.agms.sensor_service.service;


import com.ugch.agms.sensor_service.entity.SensorReading;

public interface SensorService {

    void fetchAndProcessTelemetry();

    SensorReading getLatestReading();
}
