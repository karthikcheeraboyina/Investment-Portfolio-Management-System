package com.example.ipmsBackend.service;

import com.example.ipmsBackend.entity.Asset;
import com.example.ipmsBackend.entity.Performance;
import com.example.ipmsBackend.repository.AssetRepository;
import com.example.ipmsBackend.repository.PerformanceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final AssetRepository assetRepository;

    // Inject both repositories via the constructor
    public PerformanceService(PerformanceRepository performanceRepository, AssetRepository assetRepository) {
        this.performanceRepository = performanceRepository;
        this.assetRepository = assetRepository;
    }

    public Performance calculateAndSavePerformance(Long assetId, BigDecimal newCurrentValue) {
        // Fetch the Asset to get its purchase price.
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new EntityNotFoundException("Asset not found with ID: " + assetId));

        Performance performance = performanceRepository.findByAsset_AssetId(assetId)
                .orElse(new Performance());

        BigDecimal profitLoss = newCurrentValue.subtract(asset.getPurchasePrice());

        performance.setAsset(asset); // Link to the asset
        performance.setCurrentValue(newCurrentValue);
        performance.setProfitLoss(profitLoss);
        performance.setLastUpdated(LocalDate.now());

        return performanceRepository.save(performance);
    }
}