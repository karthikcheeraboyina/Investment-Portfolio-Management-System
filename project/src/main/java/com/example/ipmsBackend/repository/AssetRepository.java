package com.example.ipmsBackend.repository;
import com.example.ipmsBackend.entity.Asset;
import com.example.ipmsBackend.entity.AssetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByPortfolio_PortfolioId(Long portfolioId);


    List<Asset> findByAssetType(AssetType assetType);

    List<Asset> findByPortfolio_PortfolioIdAndAssetType(Long portfolioId, AssetType assetType);





    @Query("SELECT SUM(a.quantity * a.purchasePrice) FROM Asset a WHERE a.portfolio.id = :portfolioId")
    BigDecimal getTotalPortfolioValue(@Param("portfolioId") Long portfolioId);
}

