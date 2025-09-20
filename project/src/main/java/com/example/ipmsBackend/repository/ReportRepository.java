package com.example.ipmsBackend.repository;

import com.example.ipmsBackend.entity.Report;
import com.example.ipmsBackend.entity.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByPortfolioId(Long portfolioId);
}
