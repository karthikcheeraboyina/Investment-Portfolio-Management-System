// src/main/java/com/example/ipmsBackend/model/Report.java
package com.example.ipmsBackend.entity;


import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;

import java.time.LocalDate;

@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long portfolioId;

    private String type;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDate date;

    public Report() {}

    public Report(Long portfolioId, String type, String title) {
        this.portfolioId = portfolioId;
        this.type = type;
        this.title = title;
        this.date = LocalDate.now();
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
