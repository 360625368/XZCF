package com.xzcf.data.data.response;


public class CustomizeStockResponse extends BaseRes {

    private String stockCode;
    private String stockName;
    private String todayPrice;
    private String yesterdayPrice;

    public String getStockCode() {
        return stockCode;
    }

    public CustomizeStockResponse setStockCode(String stockCode) {
        this.stockCode = stockCode;
        return this;
    }

    public String getStockName() {
        return stockName;
    }

    public CustomizeStockResponse setStockName(String stockName) {
        this.stockName = stockName;
        return this;
    }

    public String getTodayPrice() {
        return todayPrice;
    }

    public CustomizeStockResponse setTodayPrice(String todayPrice) {
        this.todayPrice = todayPrice;
        return this;
    }

    public String getYesterdayPrice() {
        return yesterdayPrice;
    }

    public CustomizeStockResponse setYesterdayPrice(String yesterdayPrice) {
        this.yesterdayPrice = yesterdayPrice;
        return this;
    }
}
