package com.example.ipmsBackend.service;

import com.example.ipmsBackend.entity.Report;
import com.example.ipmsBackend.entity.ReportType;

import java.util.List;

public interface ReportService {
    Report generateReport(Long userId ,Long portfolioId, ReportType reportType, String title);
    List<Report> getPortfolioReports(Long portfolioId);
}
