package com.example.ipmsBackend.service;

import com.example.ipmsBackend.entity.Asset;
import com.example.ipmsBackend.entity.Performance;
import com.example.ipmsBackend.repository.AssetRepository;
import com.example.ipmsBackend.repository.PerformanceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class PerformanceServiceImpl implements PerformanceService {

    private final AssetRepository assetRepository;
    private final PerformanceRepository performanceRepository;

    @Autowired
    public PerformanceServiceImpl(AssetRepository assetRepository, PerformanceRepository performanceRepository) {
        this.assetRepository = assetRepository;
        this.performanceRepository = performanceRepository;
    }

    @Override
    @Transactional
    public Performance calculateAndSavePerformance(Long assetId, BigDecimal newCurrentPrice) {

        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new EntityNotFoundException("Asset not found with ID: " + assetId));

        asset.setCurrentPrice(newCurrentPrice);

        assetRepository.save(asset);

        Performance performance = performanceRepository.findByAsset_AssetId(assetId)
                .orElse(new Performance());

        BigDecimal totalCurrentValue = newCurrentPrice.multiply(asset.getQuantity());
        BigDecimal totalPurchaseCost = asset.getPurchasePrice().multiply(asset.getQuantity());
        BigDecimal profitLoss = totalCurrentValue.subtract(totalPurchaseCost);

        performance.setAsset(asset);
        performance.setCurrentValue(totalCurrentValue);
        performance.setProfitLoss(profitLoss);
        performance.setLastUpdated(LocalDate.now());

        return performanceRepository.save(performance);
    }
}