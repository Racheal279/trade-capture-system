package com.technicalchallenge.dto;
import java.math.BigDecimal;
import java.util.Map;
public class TradeSummaryDTO {


    
    private Map<String, Long> tradeCountByStatus;

    
    private Map<String, BigDecimal> totalNotionalByCurrency;

   
    private Map<String, Long> tradeCountByType;

   
    private Map<String, Long> tradeCountByCounterparty;


    public Map<String, Long> getTradeCountByStatus() {
        return tradeCountByStatus;
    }

    public void setTradeCountByStatus(Map<String, Long> tradeCountByStatus) {
        this.tradeCountByStatus = tradeCountByStatus;
    }

    public Map<String, BigDecimal> getTotalNotionalByCurrency() {
        return totalNotionalByCurrency;
    }

    public void setTotalNotionalByCurrency(Map<String, BigDecimal> totalNotionalByCurrency) {
        this.totalNotionalByCurrency = totalNotionalByCurrency;
    }

    public Map<String, Long> getTradeCountByType() {
        return tradeCountByType;
    }

    public void setTradeCountByType(Map<String, Long> tradeCountByType) {
        this.tradeCountByType = tradeCountByType;
    }

    public Map<String, Long> getTradeCountByCounterparty() {
        return tradeCountByCounterparty;
    }

    public void setTradeCountByCounterparty(Map<String, Long> tradeCountByCounterparty) {
        this.tradeCountByCounterparty = tradeCountByCounterparty;
    }

    
}
