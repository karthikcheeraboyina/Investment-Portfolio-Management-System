// src/main/java/com/example/service/ReportService.java
package com.example.ipmsBackend.service;

import com.example.ipmsBackend.entity.Report;
import com.example.ipmsBackend.repository.ReportRepository;
import com.example.ipmsBackend.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public Report generatePortfolioReport(Long portfolioId, String reportTitle) {
        // Business logic to generate report
        Report report = new Report(portfolioId, "PORTFOLIO", reportTitle);
        return reportRepository.save(report);
    }

    public Report generateRiskReport(Long portfolioId, String reportTitle) {
        Report report = new Report(portfolioId, "RISK", reportTitle);
        return reportRepository.save(report);
    }

    public Report generateAssetAnalysisReport(Long portfolioId, String reportTitle) {
        Report report = new Report(portfolioId, "ASSET_ANALYSIS", reportTitle);
        return reportRepository.save(report);
    }

    public List<Report> getPortfolioReports(Long portfolioId) {
        return reportRepository.findByPortfolioId(portfolioId);
    }

    public List<Report> getReportsByType(String reportType) {
        return reportRepository.findByType(reportType);
    }

    public List<Report> getPortfolioReportsByType(Long portfolioId, String reportType) {
        return reportRepository.findByPortfolioIdAndType(portfolioId, reportType);
    }

    public List<Report> getReportsByDateRange(String startDate, String endDate) {
        return reportRepository.findByDateRange(startDate, endDate);
    }

    public List<Report> getPortfolioReportsByDateRange(Long portfolioId, String startDate, String endDate) {
        return reportRepository.findByPortfolioIdAndDateRange(portfolioId, startDate, endDate);
    }

    public Long getReportCountByType(Long portfolioId, String reportType) {
        return reportRepository.countByPortfolioIdAndType(portfolioId, reportType);
    }

    public Report updateReport(Long reportId, Report reportData) {
        Report report = reportRepository.findById(reportId).orElseThrow();
        report.setTitle(reportData.getTitle());
        report.setContent(reportData.getContent());
        return reportRepository.save(report);
    }
}
