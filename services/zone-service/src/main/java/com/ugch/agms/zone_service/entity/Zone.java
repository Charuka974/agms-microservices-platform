package com.ugch.agms.zone_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "zones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double minTemperature;
    private Double maxTemperature;

    private Double minHumidity;
    private Double maxHumidity;
}