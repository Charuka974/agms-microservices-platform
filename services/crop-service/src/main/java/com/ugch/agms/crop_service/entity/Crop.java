package com.ugch.agms.crop_service.entity;

import com.ugch.agms.crop_service.enums.CropStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Crop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String batchName;
    private String zoneId;
    @Enumerated(EnumType.STRING)
    private CropStatus status = CropStatus.SEEDLING;
}