package com.example.ipmsBackend.service;

import com.example.ipmsBackend.entity.Asset;
import com.example.ipmsBackend.entity.Portfolio;

import java.util.List;
//
public interface PortfolioService {
    List<Portfolio> getPortfoliosByUserId(Long userId);
    Portfolio createPortfolio(Portfolio portfolio);
    Portfolio addAssetToPortfolio(Long portfolioId, Asset asset);
    Portfolio viewPortfolioDetails(Long portfolioId);
    void deletePortfolio(Long portfolioId);
}
