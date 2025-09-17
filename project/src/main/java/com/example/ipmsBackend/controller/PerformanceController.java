package com.example.ipmsBackend.controller;

import com.example.ipmsBackend.entity.Performance;
import com.example.ipmsBackend.service.PerformanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/performance")
public class PerformanceController {

    private final PerformanceService performanceService;

    // Use constructor injection - it's the recommended practice
    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    /**
     * Endpoint to calculate and save the performance for a specific asset.
     * This is a POST request because it modifies data on the server.
     *
     * @param assetId The ID of the asset, taken from the URL path.
     * @param currentValue The new current value of the asset, passed as a request parameter.
     * @return The updated Performance object.
     */
    @PostMapping("/asset/{assetId}")
    public ResponseEntity<Performance> calculateAndUpdatePerformance(
            @PathVariable Long assetId,
            @RequestParam BigDecimal currentValue) {

        Performance updatedPerformance = performanceService.calculateAndSavePerformance(assetId, currentValue);
        return ResponseEntity.ok(updatedPerformance);
    }
}