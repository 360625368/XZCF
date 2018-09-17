package com.xzcf.data.data.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StockListResponse extends BaseRes {

    @SerializedName("allCount")
    private int allCount;
    @SerializedName("data")
    private List<StockBean> data;

    public int getAllCount() {
        return allCount;
    }

    public void setAllCount(int allCount) {
        this.allCount = allCount;
    }

    public List<StockBean> getData() {
        return data;
    }

    public void setData(List<StockBean> data) {
        this.data = data;
    }

    public static class StockBean {
        @SerializedName("stockCode")
        private String stockCode;
        @SerializedName("stockName")
        private String stockName;
        @SerializedName("content")
        private Object content;
        @SerializedName("contentTime")
        private Object contentTime;
        @SerializedName("stockPinyin")
        private String stockPinyin;
        @SerializedName("market")
        private int market;

        public String getStockCode() {
            return stockCode;
        }

        public void setStockCode(String stockCode) {
            this.stockCode = stockCode;
        }

        public String getStockName() {
            return stockName;
        }

        public void setStockName(String stockName) {
            this.stockName = stockName;
        }

        public Object getContent() {
            return content;
        }

        public void setContent(Object content) {
            this.content = content;
        }

        public Object getContentTime() {
            return contentTime;
        }

        public void setContentTime(Object contentTime) {
            this.contentTime = contentTime;
        }

        public String getStockPinyin() {
            return stockPinyin;
        }

        public void setStockPinyin(String stockPinyin) {
            this.stockPinyin = stockPinyin;
        }

        public int getMarket() {
            return market;
        }

        public void setMarket(int market) {
            this.market = market;
        }

        @Override
        public String toString() {
            return "StockBean{" +
                    "stockCode='" + stockCode + '\'' +
                    ", stockName='" + stockName + '\'' +
                    ", content=" + content +
                    ", contentTime=" + contentTime +
                    ", stockPinyin='" + stockPinyin + '\'' +
                    ", market=" + market +
                    '}';
        }
    }
}
