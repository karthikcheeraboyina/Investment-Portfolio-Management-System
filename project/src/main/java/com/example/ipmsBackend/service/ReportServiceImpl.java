package com.example.ipmsBackend.service;

import com.example.ipmsBackend.entity.Report;
import com.example.ipmsBackend.entity.ReportType;
import com.example.ipmsBackend.entity.Asset;
import com.example.ipmsBackend.entity.Portfolio;
import com.example.ipmsBackend.entity.Risk;
import com.example.ipmsBackend.repository.ReportRepository;
import com.example.ipmsBackend.repository.AssetRepository;
import com.example.ipmsBackend.repository.RiskRepository;
import com.example.ipmsBackend.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final AssetRepository assetRepository;
    private RiskRepository riskRepository;
    private PortfolioRepository portfolioRepository;


    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository, AssetRepository assetRepository, RiskRepository riskRepository,PortfolioRepository portfolioRepository) {
        this.reportRepository = reportRepository;
        this.assetRepository = assetRepository;
        this.riskRepository = riskRepository;
        this.portfolioRepository = portfolioRepository;
    }

    @Override
    public Report generateReport(Long userId, Long portfolioId, ReportType reportType, String title) {
        String content = "";

        if (reportType == ReportType.PORTFOLIO) {
            List<Portfolio> portfolios = portfolioRepository.findByUserId(userId);
            StringBuilder sb = new StringBuilder();
            sb.append("Portfolio Report for User ID: ").append(userId).append("\n\n");

            for (Portfolio portfolio : portfolios) {
                sb.append("Portfolio ID: ").append(portfolio.getPortfolioId())
                        .append(" - ").append(portfolio.getPortfolioName()).append("\n");

                List<Asset> assets = assetRepository.findByPortfolio_PortfolioId(portfolio.getPortfolioId());

                sb.append(String.format("%-20s %-15s %-10s %-15s %-15s\n",
                        "Asset Name", "Asset Type", "Quantity", "Purchase Price", "Current Price"));
                sb.append("------------------------------------------------------------------------------------------\n");

                BigDecimal totalPurchase = BigDecimal.ZERO;
                BigDecimal totalCurrent = BigDecimal.ZERO;
                BigDecimal totalQuantity = BigDecimal.ZERO;

                for (Asset asset : assets) {
                    BigDecimal quantity = asset.getQuantity();
                    BigDecimal purchasePrice = asset.getPurchasePrice();
                    BigDecimal currentPrice = asset.getCurrentPrice();

                    sb.append(String.format("%-20s %-15s %-10.0f %-15.2f %-15.2f\n",
                            asset.getAssetName(),
                            asset.getAssetType(),
                            quantity,
                            purchasePrice,
                            currentPrice));

                    totalQuantity = totalQuantity.add(quantity);
                    totalPurchase = totalPurchase.add(purchasePrice.multiply(quantity));
                    totalCurrent = totalCurrent.add(currentPrice.multiply(quantity));
                }

                sb.append("Total Quantity: ").append(String.format("%.0f", totalQuantity)).append("\n");
                sb.append("Total Purchase Value: ").append(String.format("%.2f", totalPurchase)).append("\n");
                sb.append("Total Current Value: ").append(String.format("%.2f", totalCurrent)).append("\n\n");
            }

            content = sb.toString();

        } else if (reportType == ReportType.ASSETS) {
            List<Asset> assets = assetRepository.findByPortfolio_PortfolioId(portfolioId);
            StringBuilder sb = new StringBuilder();
            sb.append("Assets Report for Portfolio ID: ").append(portfolioId).append("\n\n");

            sb.append(String.format("%-20s %-15s %-10s %-15s %-15s\n",
                    "Asset Name", "Asset Type", "Quantity", "Purchase Price", "Current Price"));
            sb.append("------------------------------------------------------------------------------------------\n");

            BigDecimal totalPurchase = BigDecimal.ZERO;
            BigDecimal totalCurrent = BigDecimal.ZERO;
            BigDecimal totalQuantity = BigDecimal.ZERO;

            for (Asset asset : assets) {
                BigDecimal quantity = asset.getQuantity();
                BigDecimal purchasePrice = asset.getPurchasePrice();
                BigDecimal currentPrice = asset.getCurrentPrice();

                sb.append(String.format("%-20s %-15s %-10.0f %-15.2f %-15.2f\n",
                        asset.getAssetName(),
                        asset.getAssetType(),
                        quantity,
                        purchasePrice,
                        currentPrice));

                totalQuantity = totalQuantity.add(quantity);
                totalPurchase = totalPurchase.add(purchasePrice.multiply(quantity));
                totalCurrent = totalCurrent.add(currentPrice.multiply(quantity));
            }

            sb.append("Total Quantity: ").append(String.format("%.0f", totalQuantity)).append("\n");
            sb.append("Total Purchase Value: ").append(String.format("%.2f", totalPurchase)).append("\n");
            sb.append("Total Current Value: ").append(String.format("%.2f", totalCurrent)).append("\n");

            content = sb.toString();

        } else if (reportType == ReportType.RISK) {
            List<Risk> risks = riskRepository.findByPortfolio_PortfolioId(portfolioId);

            if (!risks.isEmpty()) {
                Risk latestRisk = risks.get(risks.size() - 1);
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
