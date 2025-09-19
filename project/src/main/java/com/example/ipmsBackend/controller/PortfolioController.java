package com.example.ipmsBackend.controller;

import com.example.ipmsBackend.entity.Asset;
import com.example.ipmsBackend.entity.Portfolio;
import com.example.ipmsBackend.service.PortfolioServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolios")
public class PortfolioController {

    private final PortfolioServiceImpl service;

    public PortfolioController(PortfolioServiceImpl service) {
        this.service = service;
    }

    // Create portfolio
    @PostMapping
    public ResponseEntity<Portfolio> createPortfolio(@RequestBody Portfolio portfolio) {
        Portfolio saved = service.createPortfolio(portfolio);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Portfolio>> getPortfoliosByUser(@PathVariable Long userId) {
        List<Portfolio> portfolios = service.getPortfoliosByUserId(userId);
        return ResponseEntity.ok(portfolios);
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

    @DeleteMapping("/delete/{portfolioId}")
    public ResponseEntity<Void> deletePortfolio(@PathVariable Long portfolioId){
        service.deletePortfolio(portfolioId);
        return ResponseEntity.ok().build();
    }
}
