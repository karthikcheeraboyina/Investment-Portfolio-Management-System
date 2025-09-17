// src/main/java/com/example/repository/ReportRepository.java
package com.example.ipmsBackend.repository;

import com.example.ipmsBackend.entity.Report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByPortfolioId(Long portfolioId);
    List<Report> findByType(String type);
    List<Report> findByPortfolioIdAndType(Long portfolioId, String type);

    @Query("SELECT r FROM Report r WHERE r.date BETWEEN ?1 AND ?2")
    List<Report> findByDateRange(String startDate, String endDate);

    @Query("SELECT r FROM Report r WHERE r.portfolioId = ?1 AND r.date BETWEEN ?2 AND ?3")
    List<Report> findByPortfolioIdAndDateRange(Long portfolioId, String startDate, String endDate);

    Long countByPortfolioIdAndType(Long portfolioId, String type);
}
