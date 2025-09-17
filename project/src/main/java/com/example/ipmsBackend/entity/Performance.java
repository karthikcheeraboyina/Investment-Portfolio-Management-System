package com.example.ipmsBackend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Performance")
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long performanceId;

    private BigDecimal currentValue;
    private BigDecimal profitLoss;
    private LocalDate lastUpdated;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetId", nullable = false)
    private Asset asset;

    public Performance() {
    }

    public Performance(Long performanceId, BigDecimal currentValue, BigDecimal profitLoss, Asset asset, LocalDate lastUpdated) {
        this.performanceId = performanceId;
        this.currentValue = currentValue;
        this.profitLoss = profitLoss;
        this.asset = asset;
        this.lastUpdated = lastUpdated;
    }

    public Long getPerformanceId() {
        return performanceId;
    }

    public void setPerformanceId(Long performanceId) {
        this.performanceId = performanceId;
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }

    public BigDecimal getProfitLoss() {
        return profitLoss;
    }

    public void setProfitLoss(BigDecimal profitLoss) {
        this.profitLoss = profitLoss;
    }

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }
}