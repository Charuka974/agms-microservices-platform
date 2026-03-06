package com.ugch.agms.zone_service.service;

import com.ugch.agms.zone_service.dto.ZoneRequest;
import com.ugch.agms.zone_service.entity.Zone;

public interface ZoneService {

    Zone createZone(ZoneRequest request, String token);

    Zone getZone(Long id);

    Zone updateZone(Long id, ZoneRequest request);

    void deleteZone(Long id);
}
