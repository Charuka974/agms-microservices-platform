package com.ugch.agms.crop_service.repository;

import com.ugch.agms.crop_service.entity.Crop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CropRepository extends JpaRepository<Crop, Long> {

    List<Crop> findByZoneId(String zoneId);
}