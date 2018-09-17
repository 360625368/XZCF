package com.xzcf.data.data.req;

import com.xzcf.utils.StockRuleUtils;


public class StockMinuteChartReq {

    private String market;
    private String stockCode;

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

    public StockMinuteChartReq setStockCode(String stockCode) {
        this.stockCode = stockCode;
        getMarket();
        return this;
    }

}
