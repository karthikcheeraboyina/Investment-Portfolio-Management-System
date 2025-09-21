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


    @Transactional
    public Performance calculateAndSavePerformance(Long assetId, BigDecimal newCurrentPrice) {
        // 1. Fetch the Asset to get its purchase price and quantity.
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new EntityNotFoundException("Asset not found with ID: " + assetId));

        // 2. Find the existing performance record or create a new one.
        Performance performance = performanceRepository.findByAsset_AssetId(assetId)
                .orElse(new Performance());



        // Calculate the total cost of the initial purchase.
        // totalPurchaseCost = purchasePrice * quantity
        BigDecimal totalPurchaseCost = asset.getPurchasePrice().multiply(asset.getQuantity());

        // Calculate the total current market value of the holding.
        // totalCurrentValue = newCurrentPrice * quantity
        BigDecimal totalCurrentValue = newCurrentPrice;

        // Calculate the total profit or loss.
        // profitLoss = totalCurrentValue - totalPurchaseCost
        BigDecimal profitLoss = totalCurrentValue.multiply(asset.getQuantity()).subtract(totalPurchaseCost);



        // 3. Update the asset's current price for consistency.
        asset.setCurrentPrice(newCurrentPrice);
        assetRepository.save(asset);

        // 4. Update the performance record with the new calculations.
        performance.setAsset(asset); // Link to the asset
        performance.setCurrentValue(totalCurrentValue); // Store the total value of the holding
        performance.setProfitLoss(profitLoss);
        performance.setLastUpdated(LocalDate.now());

        // 5. Save the updated performance record.
        return performanceRepository.save(performance);
    }
}