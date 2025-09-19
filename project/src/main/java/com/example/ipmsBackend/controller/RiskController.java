package com.example.ipmsBackend.controller;

import com.example.ipmsBackend.entity.Risk;
import com.example.ipmsBackend.service.RiskServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/risk")
public class RiskController {

    private final RiskServiceImpl riskService;

    public RiskController(RiskServiceImpl riskService) {
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



    @GetMapping
    public List<Risk> GetAllRisk(){
        return riskService.getAllRisks();
    }
}
