package com.example.ipmsBackend.service;

import com.example.ipmsBackend.entity.Asset;
import com.example.ipmsBackend.entity.Risk;
import com.example.ipmsBackend.repository.AssetRepository;
import com.example.ipmsBackend.repository.RiskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RiskService {
     private RiskRepository riskRepository;
     private AssetRepository assetRepository;

     public Risk CalculateRisk(Long portfolioId){
         List<Asset> assets=assetRepository.findByPortfolio_PortfolioId(portfolioId);
         if(assets.isEmpty()){
             throw new IllegalArgumentException("No assets found for the given portfolio ID");
         }

         int totalRiskScore=0;
         for(Asset asset:assets){
             switch (asset.getAssetType()){
                 case STOCK :
                     totalRiskScore+=3;
                     break;
                 case MUTUAL_FUND:
                     totalRiskScore+=2;
                     break;
                 case BOND:
                     totalRiskScore+=1;
                     break;
                 default:
                     throw new IllegalArgumentException("unknow Assets type "+asset.getAssetType());
             }
         }
         double averageRiskScore=(double) totalRiskScore/assets.size();

          Risk.RiskLevel riskLevel;

          if(averageRiskScore>=2.5){
              riskLevel=Risk.RiskLevel.HIGH;
          }
          else if(averageRiskScore>=1.5){
              riskLevel=Risk.RiskLevel.MEDIUM;
          }
          else{
              riskLevel=Risk.RiskLevel.LOW;
          }

          Risk risk =new Risk();
          risk.setRiskLevel(riskLevel);

          return riskRepository.save(risk);
     }

     public Risk ViewRiskAnalysis(Long portfolioId){
         Optional<Risk> latestRisk=riskRepository.findLatestByPortfolio_PortfolioId(portfolioId);
         return latestRisk.orElseGet(()->CalculateRisk(portfolioId));
     }
}
