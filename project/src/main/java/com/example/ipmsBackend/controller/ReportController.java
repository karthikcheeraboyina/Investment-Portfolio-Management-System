// src/main/java/com/example/controller/ReportController.java
package com.example.ipmsBackend.controller;

import com.example.ipmsBackend.entity.Report;
import com.example.ipmsBackend.service.ReportServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportServiceImpl reportService;

    @PostMapping("/portfolio/{portfolioId}")
    public Report generatePortfolioReport(@PathVariable Long portfolioId, @RequestParam(required = false) String reportTitle) {
        return reportService.generatePortfolioReport(portfolioId, reportTitle);
    }

    @PostMapping("/risk/{portfolioId}")
    public Report generateRiskReport(@PathVariable Long portfolioId, @RequestParam(required = false) String reportTitle) {
        return reportService.generateRiskReport(portfolioId, reportTitle);
    }

    @PostMapping("/asset-analysis/{portfolioId}")
    public Report generateAssetAnalysisReport(@PathVariable Long portfolioId, @RequestParam(required = false) String reportTitle) {
        return reportService.generateAssetAnalysisReport(portfolioId, reportTitle);
    }

    @GetMapping("/portfolio/{portfolioId}")
    public List<Report> getPortfolioReports(@PathVariable Long portfolioId) {
        return reportService.getPortfolioReports(portfolioId);
    }

    @GetMapping("/type/{reportType}")
    public List<Report> getReportsByType(@PathVariable String reportType) {
        return reportService.getReportsByType(reportType);
    }

    @GetMapping("/portfolio/{portfolioId}/type/{reportType}")
    public List<Report> getPortfolioReportsByType(@PathVariable Long portfolioId, @PathVariable String reportType) {
        return reportService.getPortfolioReportsByType(portfolioId, reportType);
    }

    @GetMapping("/daterange")
    public List<Report> getReportsByDateRange(@RequestParam String startDate, @RequestParam String endDate) {
        return reportService.getReportsByDateRange(startDate, endDate);
    }

    @GetMapping("/portfolio/{portfolioId}/daterange")
    public List<Report> getPortfolioReportsByDateRange(@PathVariable Long portfolioId, @RequestParam String startDate, @RequestParam String endDate) {
        return reportService.getPortfolioReportsByDateRange(portfolioId, startDate, endDate);
    }

    @GetMapping("/portfolio/{portfolioId}/count/{reportType}")
    public Long getReportCountByType(@PathVariable Long portfolioId, @PathVariable String reportType) {
        return reportService.getReportCountByType(portfolioId, reportType);
    }

    @PutMapping("/{reportId}")
    public Report updateReport(@PathVariable Long reportId, @RequestBody Report reportData) {
        return reportService.updateReport(reportId, reportData);
    }
}
