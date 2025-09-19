package com.example.ipmsBackend.service;

import com.example.ipmsBackend.entity.Performance;

import java.math.BigDecimal;

public interface PerformanceService {
    Performance calculateAndSavePerformance(Long assetId, BigDecimal newCurrentPrice);
}
