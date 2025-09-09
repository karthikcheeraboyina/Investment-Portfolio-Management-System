package com.example.ipmsBackend.controller;

import com.example.ipmsBackend.entity.Asset;
import com.example.ipmsBackend.entity.Portfolio;
import com.example.ipmsBackend.service.PortfolioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/portfolios")
public class PortfolioController {

    private final PortfolioService service;

    public PortfolioController(PortfolioService service) {
        this.service = service;
    }

    // Create portfolio
    @PostMapping
    public ResponseEntity<Portfolio> createPortfolio(@RequestBody Portfolio portfolio) {
        Portfolio saved = service.createPortfolio(portfolio);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Add asset to a portfolio
    @PostMapping("/{portfolioId}/assets")
    public ResponseEntity<Portfolio> addAsset(
            @PathVariable Long portfolioId,
            @RequestBody Asset asset) {
        Portfolio updated = service.addAssetToPortfolio(portfolioId, asset);
        return ResponseEntity.ok(updated);
    }

    // View portfolio details
    @GetMapping("/{portfolioId}")
    public ResponseEntity<Portfolio> viewPortfolio(@PathVariable Long portfolioId) {
        return ResponseEntity.ok(service.viewPortfolioDetails(portfolioId));
    }
}
