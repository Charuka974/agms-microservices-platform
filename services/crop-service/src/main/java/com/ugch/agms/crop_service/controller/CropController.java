package com.ugch.agms.crop_service.controller;

import com.ugch.agms.crop_service.entity.Crop;
import com.ugch.agms.crop_service.enums.CropStatus;
import com.ugch.agms.crop_service.repository.CropRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crops")
public class CropController {

    @Autowired
    private CropRepository cropRepository;

    // Register new batch [cite: 158]
    @PostMapping
    public ResponseEntity<Crop> registerCrop(@RequestBody Crop crop) {
        crop.setStatus(CropStatus.SEEDLING);
        return ResponseEntity.ok(cropRepository.save(crop));
    }

    // View current inventory [cite: 161]
    @GetMapping
    public ResponseEntity<List<Crop>> getInventory() {
        return ResponseEntity.ok(cropRepository.findAll());
    }

    // Update lifecycle stage [cite: 159]
    // Update lifecycle stage
    @PutMapping("/{id}/status")
    public ResponseEntity<Crop> updateStatus(@PathVariable Long id, @RequestBody Crop statusUpdate) {
        Crop crop = cropRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Crop not found"));

        // This pulls the status from the JSON body: {"status": "VEGETATIVE"}
        crop.setStatus(statusUpdate.getStatus());
        return ResponseEntity.ok(cropRepository.save(crop));
    }
}
