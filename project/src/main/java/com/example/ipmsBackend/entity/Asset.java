package com.example.ipmsBackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;


import java.math.BigDecimal;

@Entity
public class Asset {

    // Getters and setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assetId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    @JsonIgnore
    private Portfolio portfolio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AssetType assetType;

    @Column(nullable = false, length = 150)
    private String assetName;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal quantity;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal purchasePrice;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal currentPrice;

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }
    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }
    public AssetType getAssetType() {
        return assetType;
    }
    public void setAssetType(AssetType assetType) {
        this.assetType = assetType;
    }
    public String getAssetName() {
        return assetName;
    }
    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }
    public BigDecimal getQuantity() {
        return quantity;
    }
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }
    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }
}

