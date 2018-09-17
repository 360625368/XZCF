package com.xzcf.data.data.req;

public class StockBuyDoReq {

    private String memberId;
    private String stockCode;
    private String stockName;
    private String allPrice;
    private String allCount;
    private String type;

    public String getMemberId() {
        return memberId;
    }

    public StockBuyDoReq setMemberId(String memberId) {
        this.memberId = memberId;
        return this;
    }

    public String getStockCode() {
        return stockCode;
    }

    public StockBuyDoReq setStockCode(String stockCode) {
        this.stockCode = stockCode;
        return this;
    }

    public String getStockName() {
        return stockName;
    }

    public StockBuyDoReq setStockName(String stockName) {
        this.stockName = stockName;
        return this;
    }

    public String getAllPrice() {
        return allPrice;
    }

    public StockBuyDoReq setAllPrice(String allPrice) {
        this.allPrice = allPrice;
        return this;
    }

    public String getAllCount() {
        return allCount;
    }

    public StockBuyDoReq setAllCount(String allCount) {
        this.allCount = allCount;
        return this;
    }

    public String getType() {
        return type;
    }

    public StockBuyDoReq setType(String type) {
        this.type = type;
        return this;
    }
}
