package com.xzcf.data.data.req;

import com.xzcf.utils.StockRuleUtils;


public class StockMarketDetailReq {

    private String market;
    private String stockCode;
    private String start;
    private String count;

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

    public StockMarketDetailReq setStockCode(String stockCode) {
        this.stockCode = stockCode;
        getMarket();
        return this;
    }

    public String getStart() {
        return start;
    }

    public StockMarketDetailReq setStart(String start) {
        this.start = start;
        return this;
    }

    public String getCount() {
        return count;
    }

    public StockMarketDetailReq setCount(String count) {
        this.count = count;
        return this;
    }
}
