package com.xzcf.data.data.event;


public class StockSelfEvent {

    private String stockCode;

    public String getStockCode() {
        return stockCode;
    }

    public StockSelfEvent setStockCode(String stockCode) {
        this.stockCode = stockCode;
        return this;
    }
}
