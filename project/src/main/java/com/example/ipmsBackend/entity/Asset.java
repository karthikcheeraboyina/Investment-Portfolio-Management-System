package com.example.ipmsBackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;

@Entity
@Getter
@Setter
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

    @Override
    public String toString() {
        return "Asset{" +
                "assetId=" + assetId +
                ", portfolio=" + portfolio +
                ", assetType=" + assetType +
                ", assetName='" + assetName + '\'' +
                ", quantity=" + quantity +
                ", purchasePrice=" + purchasePrice +
                ", currentPrice=" + currentPrice +
                '}';
    }
}

