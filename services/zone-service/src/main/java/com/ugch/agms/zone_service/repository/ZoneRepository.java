package com.ugch.agms.zone_service.repository;


import com.ugch.agms.zone_service.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneRepository extends JpaRepository<Zone, Long> {
}