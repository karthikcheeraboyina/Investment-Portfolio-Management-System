package com.example.ipmsBackend.service;

import com.example.ipmsBackend.entity.Risk;

import java.util.List;

public interface RiskService {
    Risk CalculateRisk(Long portfolioId);
    List<Risk> getAllRisks();
    Risk viewRiskAnalysis(Long portfolioId);
}
