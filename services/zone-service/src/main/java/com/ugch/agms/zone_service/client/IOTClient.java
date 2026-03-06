package com.ugch.agms.zone_service.client;


import com.ugch.agms.zone_service.dto.DeviceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "external-iot", url = "http://104.211.95.241:8080/api")
public interface IOTClient {

    @PostMapping("/devices")
    DeviceResponse registerDevice(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, String> request
    );
}