package com.example.ipmsBackend.service;

import com.example.ipmsBackend.entity.Report;
import com.example.ipmsBackend.entity.ReportType;
import com.example.ipmsBackend.entity.Asset;
import com.example.ipmsBackend.entity.Risk;
import com.example.ipmsBackend.repository.ReportRepository;
import com.example.ipmsBackend.repository.AssetRepository;
import com.example.ipmsBackend.repository.RiskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final AssetRepository assetRepository;
    private RiskRepository riskRepository;


    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository, AssetRepository assetRepository, RiskRepository riskRepository) {
        this.reportRepository = reportRepository;
        this.assetRepository = assetRepository;
        this.riskRepository = riskRepository;
    }

    @Override
    public Report generateReport(Long userId ,Long portfolioId, ReportType reportType, String title) {
        String content = "";
        if (reportType == ReportType.PORTFOLIO || reportType == ReportType.ASSETS) {
            List<Asset> assets = assetRepository.findByPortfolio_PortfolioId(portfolioId);
            StringBuilder sb = new StringBuilder();
            sb.append(reportType == ReportType.PORTFOLIO ? "Portfolio Report" : "Assets Report")
                    .append(" for Portfolio ID: ").append(portfolioId).append("\nAssets:\n");
            for (Asset asset : assets) {
                sb.append("Name: ").append(asset.getAssetName())
                        .append(", Type: ").append(asset.getAssetType())
                        .append(", Quantity: ").append(asset.getQuantity())
                        .append(", Purchase Price: ").append(asset.getPurchasePrice())
                        .append(", Current Price: ").append(asset.getCurrentPrice())
                        .append("\n");
            }
            content = sb.toString();
        } else if (reportType == ReportType.RISK) {
            List<Risk> risks = riskRepository.findByPortfolio_PortfolioId(portfolioId);
            if (!risks.isEmpty()) {
                Risk latestRisk = risks.get(risks.size() - 1); // or use your own logic to pick the latest
                content = "Risk Report for Portfolio ID: " + portfolioId + "\n"
                        + "Risk Level: " + latestRisk.getRiskLevel() + "\n"
                        + "Analysis Date: " + latestRisk.getAnalysisDate();
            } else {
                content = "Risk Report for Portfolio ID: " + portfolioId + "\nNo risk analysis available.";
            }
        }
        Report report = new Report(null, reportType, portfolioId, title, content);
        return reportRepository.save(report);
    }

    @Override
    public List<Report> getPortfolioReports(Long portfolioId) {
        return reportRepository.findByPortfolioId(portfolioId);
    }



}
