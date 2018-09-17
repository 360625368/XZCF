package com.xzcf.data.data.req;

import com.xzcf.utils.StockRuleUtils;


public class StockDayChartReq {

    private String market;
    private String stockCode;
    private String category;
    private String start;
    private String count;
    private String isFirst;


    public String getMarket() {
        if (StockRuleUtils.isShangHaiMarket(stockCode)) {
            this.market = "1";
        }else{
            this.market = "0";
        }
        return market;
    }

    public String getStockCode() {
        return stockCode;
    }

    public StockDayChartReq setStockCode(String stockCode) {
        this.stockCode = stockCode;
        getMarket();
        return this;
    }


    public String getCategory() {
        return category;
    }

    public StockDayChartReq setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getStart() {
        return start;
    }

    public StockDayChartReq setStart(String start) {
        this.start = start;
        return this;
    }

    public String getCount() {
        return count;
    }

    public StockDayChartReq setCount(String count) {
        this.count = count;
        return this;
    }

    public String getIsFirst() {
        return isFirst;
    }

    public StockDayChartReq setIsFirst(String isFirst) {
        this.isFirst = isFirst;
        return this;
    }
}
