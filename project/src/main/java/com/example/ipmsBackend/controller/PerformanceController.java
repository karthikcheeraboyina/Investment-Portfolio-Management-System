package com.example.ipmsBackend.controller;

import com.example.ipmsBackend.entity.Performance;
import com.example.ipmsBackend.service.PerformanceServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/performance")
public class PerformanceController {

    private final PerformanceServiceImpl performanceService;

    // Use constructor injection - it's the recommended practice
    public PerformanceController(PerformanceServiceImpl performanceService) {
        this.performanceService = performanceService;
    }

    @PostMapping("/asset/{assetId}")
    public ResponseEntity<Performance> calculateAndUpdatePerformance(
            @PathVariable Long assetId,
            @RequestParam BigDecimal currentValue) {

        Performance updatedPerformance = performanceService.calculateAndSavePerformance(assetId, currentValue);
        return ResponseEntity.ok(updatedPerformance);
    }
}