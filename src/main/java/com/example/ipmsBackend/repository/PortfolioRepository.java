package com.example.ipmsBackend.repository;

import com.example.ipmsBackend.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

}

