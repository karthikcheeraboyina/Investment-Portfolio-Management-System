package com.example.ipmsBackend.controller;

import com.example.ipmsBackend.entity.Report;
import com.example.ipmsBackend.entity.ReportType;
import com.example.ipmsBackend.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/generate")
    public ResponseEntity<Report> generateReport(
            @RequestParam Long userId,
            @RequestParam Long portfolioId,
            @RequestParam ReportType reportType,
            @RequestParam(required = false) String title) {
        Report report = reportService.generateReport(userId, portfolioId, reportType, title);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/portfolio/{portfolioId}")
    public ResponseEntity<List<Report>> getPortfolioReports(@PathVariable Long portfolioId) {
        return ResponseEntity.ok(reportService.getPortfolioReports(portfolioId));
    }

}
