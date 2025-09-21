package com.example.ipmsBackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Performance")
@Data
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

}