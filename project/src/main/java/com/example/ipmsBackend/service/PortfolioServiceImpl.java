package com.example.ipmsBackend.service;

import com.example.ipmsBackend.entity.Asset;
import com.example.ipmsBackend.entity.Portfolio;
import com.example.ipmsBackend.repository.AssetRepository;
import com.example.ipmsBackend.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final AssetRepository assetRepository;
    @Autowired
    public PortfolioServiceImpl(PortfolioRepository portfolioRepository, AssetRepository assetRepository) {
        this.portfolioRepository = portfolioRepository;
        this.assetRepository = assetRepository;
    }
    public List<Portfolio> getPortfoliosByUserId(Long userId) {
        return portfolioRepository.findByUserId(userId);
    }


    @Transactional
    public Portfolio createPortfolio(Portfolio portfolio) {
        // creationDate set in entity default; ensure null id
        portfolio.setPortfolioId(null);
        return portfolioRepository.save(portfolio);
    }

    @Transactional
    public Portfolio addAssetToPortfolio(Long portfolioId, Asset asset) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found: " + portfolioId));

        // attach to portfolio and persist via asset repo (simple and explicit)
        asset.setAssetId(null);
        portfolio.addAsset(asset);
        assetRepository.save(asset);

        // Return current state (assets will be visible due to persistence context)
        return portfolio;
    }

    @Transactional(readOnly = true)
    public Portfolio viewPortfolioDetails(Long portfolioId) {
        return portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found: " + portfolioId));
    }



    public void deletePortfolio(Long portfolioId) {
        Optional<Portfolio> p=portfolioRepository.findById(portfolioId);
        portfolioRepository.deleteById(portfolioId);
    }
}

