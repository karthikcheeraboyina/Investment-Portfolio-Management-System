package com.example.ipmsBackend.service;

import com.example.ipmsBackend.entity.Report;

import java.util.List;

public interface ReportService {
    Report generatePortfolioReport(Long portfolioId, String reportTitle);
    Report generateRiskReport(Long portfolioId, String reportTitle);
    Report generateAssetAnalysisReport(Long portfolioId, String reportTitle);

    List<Report> getPortfolioReports(Long portfolioId);
    List<Report> getReportsByType(String reportType);
    List<Report> getPortfolioReportsByType(Long portfolioId, String reportType);
    List<Report> getReportsByDateRange(String startDate, String endDate);
    List<Report> getPortfolioReportsByDateRange(Long portfolioId, String startDate, String endDate);

    Long getReportCountByType(Long portfolioId, String reportType);
    Report updateReport(Long reportId, Report reportData);
}
