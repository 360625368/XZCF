package com.xzcf.data.data.response;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class IntentBean implements Serializable {


    private List<Map.Entry<String, String>> mStockMarketFiveSpeedBeanList;
    private HashMap<String, String> resultSingleSplit;
    private boolean isUp;
    private String stockCode;
    private String stockName;
    private Double todayPrice;// 今日开盘价
    private Double yesterdayPrice;// 收盘价
    private String diefu;// 涨跌幅
    private String dielv;// 涨跌率
    private String index;// 当前指数
    private List<StockMarketTitleResponse> stockMarketTitleResponseList;
    public List<Map.Entry<String, String>> getStockMarketFiveSpeedBeanList() {
        return mStockMarketFiveSpeedBeanList;
    }

    public IntentBean setStockMarketFiveSpeedBeanList(List<Map.Entry<String, String>> stockMarketFiveSpeedBeanList) {
        mStockMarketFiveSpeedBeanList = stockMarketFiveSpeedBeanList;
        return this;
    }

    public HashMap<String, String> getResultSingleSplit() {
        return resultSingleSplit;
    }

    public IntentBean setResultSingleSplit(HashMap<String, String> resultSingleSplit) {
        this.resultSingleSplit = resultSingleSplit;
        return this;
    }

    public boolean isUp() {
        return isUp;
    }

    public IntentBean setUp(boolean up) {
        isUp = up;
        return this;
    }

    public String getStockCode() {
        return stockCode;
    }

    public IntentBean setStockCode(String stockCode) {
        this.stockCode = stockCode;
        return this;
    }

    /**
     * 昨收
     * @return
     */
    public Double getTodayPrice() {
        return todayPrice;
    }

    public IntentBean setTodayPrice(Double todayPrice) {
        this.todayPrice = todayPrice;
        return this;
    }

    public String getStockName() {
        return stockName;
    }

    public IntentBean setStockName(String stockName) {
        this.stockName = stockName;
        return this;
    }

    public Double getYesterdayPrice() {
        return yesterdayPrice;
    }

    public IntentBean setYesterdayPrice(Double yesterdayPrice) {
        this.yesterdayPrice = yesterdayPrice;
        return this;
    }

    public String getDiefu() {
        return diefu;
    }

    public IntentBean setDiefu(String diefu) {
        this.diefu = diefu;
        return this;
    }

    public String getDielv() {
        return dielv;
    }

    public IntentBean setDielv(String dielv) {
        this.dielv = dielv;
        return this;
    }

    public String getIndex() {
        return index;
    }

    public IntentBean setIndex(String index) {
        this.index = index;
        return this;
    }

    public List<StockMarketTitleResponse> getStockMarketTitleResponseList() {
        return stockMarketTitleResponseList;
    }

    public IntentBean setStockMarketTitleResponseList(List<StockMarketTitleResponse> stockMarketTitleResponseList) {
        this.stockMarketTitleResponseList = stockMarketTitleResponseList;
        return this;
    }

    @Override
    public String toString() {
        return "IntentBean{" +
                "mStockMarketFiveSpeedBeanList=" + mStockMarketFiveSpeedBeanList +
                ", resultSingleSplit=" + resultSingleSplit +
                ", isUp=" + isUp +
                ", stockCode='" + stockCode + '\'' +
                ", todayPrice=" + todayPrice +
                '}';
    }
}
