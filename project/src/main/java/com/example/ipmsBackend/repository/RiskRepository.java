package com.example.ipmsBackend.repository;

import com.example.ipmsBackend.entity.Risk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiskRepository extends JpaRepository<Risk, Long> {
     Optional<Risk> findByPortfolio_PortfolioId(Long portfolioId);

      Optional<Risk> findLatestByPortfolio_PortfolioId(@Param("portfolioId") Long portfolioId);


}
