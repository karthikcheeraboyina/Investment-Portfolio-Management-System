package com.example.ipmsBackend.repository;

import com.example.ipmsBackend.entity.Risk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RiskRepository extends JpaRepository<Risk, Long> {
     List<Risk> findByPortfolio_PortfolioId(Long portfolioId);

     @Query("SELECT r FROM Risk r WHERE r.portfolio.portfolioId = :portfolioId ORDER BY r.analysisDate DESC LIMIT 1")
      Optional<Risk> findLatestByPortfolio_PortfolioId(@Param("portfolioId") Long portfolioId);

}
