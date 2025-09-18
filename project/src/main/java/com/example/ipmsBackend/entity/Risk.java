package com.example.ipmsBackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
//@Table(name = "risk")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Risk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long RiskId;

    @JoinColumn(name = "portfolio_id", nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private Portfolio portfolio;

    @Enumerated(EnumType.STRING)
    @Column(name = "RiskLevel", nullable = false)
    private RiskLevel riskLevel;

    @Column(name = "analysisDate")
    private LocalDateTime analysisDate;

    @PrePersist
    protected void onCreate(){
        analysisDate=LocalDateTime.now();
    }

    public enum RiskLevel{
        LOW,MEDIUM,HIGH
    }

}
