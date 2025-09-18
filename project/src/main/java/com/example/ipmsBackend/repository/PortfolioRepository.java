package com.example.ipmsBackend.repository;

import com.example.ipmsBackend.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    List<Portfolio> findByUserId(Long userId);

    Optional<Portfolio> findByPortfolioId(Long portfolioId);
}

