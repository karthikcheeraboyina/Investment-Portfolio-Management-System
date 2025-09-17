package com.example.ipmsBackend.repository;

import com.example.ipmsBackend.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    // Method to find a performance record by its associated asset's ID
    Optional<Performance> findByAsset_AssetId(Long assetId);
}