package com.example.ipmsBackend.controller;

import com.example.ipmsBackend.entity.Risk;
import com.example.ipmsBackend.service.RiskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/risk")
public class RiskController {

    private final RiskService riskService;

    public RiskController(RiskService riskService) {
        this.riskService = riskService;
    }

    @PostMapping("/calculate/{portfolioId}")
    public ResponseEntity<Risk> CalculateRisk(@PathVariable Long portfolioId){
        try{
            Risk risk=riskService.CalculateRisk(portfolioId);
            return ResponseEntity.ok(risk);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<Risk> ViewRiskAnalysis(@PathVariable Long portfolioId){
        try{
            Risk risk=riskService.ViewRiskAnalysis(portfolioId);

            return ResponseEntity.ok(risk);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
