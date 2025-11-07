package com.technicalchallenge.dto; 

import java.math.BigDecimal;
import java.util.Map;

import javax.management.loading.PrivateClassLoader;

public class DailySummaryDTO {

    private long todaysTradeCount;


    private Map<String, BigDecimal> todaysTotalNotionalByCurrency;


    private String username; 
    private Long userTodaysTradeCount;
    private Map<String, BigDecimal> userTodaysNotionalByCurrency;

    
    private Map<String, Long> todaysTradeCountByBook;

    
    private Long previousDayTradeCount;
    

    private Map<String, Long> userTradeCount;

    private Map<String, Long> bookActivityCount;


    public long getTodaysTradeCount() {
        return todaysTradeCount;
    }

    public void setTodaysTradeCount(long todaysTradeCount) {
        this.todaysTradeCount = todaysTradeCount;
    }

    public Map<String, BigDecimal> getTodaysTotalNotionalByCurrency() {
        return todaysTotalNotionalByCurrency;
    }

    public void setTodaysTotalNotionalByCurrency(Map<String, BigDecimal> todaysTotalNotionalByCurrency) {
        this.todaysTotalNotionalByCurrency = todaysTotalNotionalByCurrency;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserTodaysTradeCount() {
        return userTodaysTradeCount;
    }

    public void setUserTodaysTradeCount(Long userTodaysTradeCount) {
        this.userTodaysTradeCount = userTodaysTradeCount;
    }

    public Map<String, BigDecimal> getUserTodaysNotionalByCurrency() {
        return userTodaysNotionalByCurrency;
    }

    public void setUserTodaysNotionalByCurrency(Map<String, BigDecimal> userTodaysNotionalByCurrency) {
        this.userTodaysNotionalByCurrency = userTodaysNotionalByCurrency;
    }

    public Map<String, Long> getTodaysTradeCountByBook() {
        return todaysTradeCountByBook;
    }

    public void setTodaysTradeCountByBook(Map<String, Long> todaysTradeCountByBook) {
        this.todaysTradeCountByBook = todaysTradeCountByBook;
    }

    public Long getPreviousDayTradeCount() {
        return previousDayTradeCount;
    }

    public void setPreviousDayTradeCount(Long previousDayTradeCount) {
        this.previousDayTradeCount = previousDayTradeCount;
    }

    public Map<String, Long> getBookActivityCount() {
        return bookActivityCount;
    }

    public Map<String, Long> getUserTradeCount() {
        return userTradeCount;
    }

    public void setUserTradeCount(Map<String, Long> userTradeCount) {
        this.userTradeCount = userTradeCount;
    }
    public void setBookActivityCount(Map<String, Long> bookActivityCount) {
        this.bookActivityCount = bookActivityCount;
    }
}