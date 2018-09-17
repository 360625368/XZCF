package com.xzcf.data.data.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HavingLogResponse extends BaseRes {
    @SerializedName("currentPage" )
    private String currentPage;
    @SerializedName("totalPage" )
    private String totalPage;
    @SerializedName("total" )
    private String total;
    @SerializedName("pageSize" )
    private String pageSize;
    @SerializedName("logInfos" )
    private List<LogInfosBean> logInfos;

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(String totalPage) {
        this.totalPage = totalPage;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public List<LogInfosBean> getLogInfos() {
        return logInfos;
    }

    public void setLogInfos(List<LogInfosBean> logInfos) {
        this.logInfos = logInfos;
    }

    public static class LogInfosBean {
        @SerializedName("countAll" )
        private String countAll;
        @SerializedName("nowAmount" )
        private String nowAmount;
        @SerializedName("created" )
        private String created;
        @SerializedName("profitLossScale" )
        private String profitLossScale;
        @SerializedName("costPriceAll" )
        private String costPriceAll;
        @SerializedName("stockCode" )
        private String stockCode;
        @SerializedName("profitLossAmount" )
        private String profitLossAmount;
        @SerializedName("nowPrice" )
        private String nowPrice;
        @SerializedName("stockName" )
        private String stockName;
        @SerializedName("countCan" )
        private String countCan;
        @SerializedName("stockholderCode" )
        private String stockholderCode;
        @SerializedName("countEntrust" )
        private String countEntrust;
        @SerializedName("costPriceOne" )
        private String costPriceOne;
        @SerializedName("id" )
        private String id;

        public String getCountAll() {
            return countAll;
        }

        public void setCountAll(String countAll) {
            this.countAll = countAll;
        }

        public String getNowAmount() {
            return nowAmount;
        }

        public void setNowAmount(String nowAmount) {
            this.nowAmount = nowAmount;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getProfitLossScale() {
            return profitLossScale;
        }

        public void setProfitLossScale(String profitLossScale) {
            this.profitLossScale = profitLossScale;
        }

        public String getCostPriceAll() {
            return costPriceAll;
        }

        public void setCostPriceAll(String costPriceAll) {
            this.costPriceAll = costPriceAll;
        }

        public String getStockCode() {
            return stockCode;
        }

        public void setStockCode(String stockCode) {
            this.stockCode = stockCode;
        }

        public String getProfitLossAmount() {
            return profitLossAmount;
        }

        public void setProfitLossAmount(String profitLossAmount) {
            this.profitLossAmount = profitLossAmount;
        }

        public String getNowPrice() {
            return nowPrice;
        }

        public void setNowPrice(String nowPrice) {
            this.nowPrice = nowPrice;
        }

        public String getStockName() {
            return stockName;
        }

        public void setStockName(String stockName) {
            this.stockName = stockName;
        }

        public String getCountCan() {
            return countCan;
        }

        public void setCountCan(String countCan) {
            this.countCan = countCan;
        }

        public String getStockholderCode() {
            return stockholderCode;
        }

        public void setStockholderCode(String stockholderCode) {
            this.stockholderCode = stockholderCode;
        }

        public String getCountEntrust() {
            return countEntrust;
        }

        public void setCountEntrust(String countEntrust) {
            this.countEntrust = countEntrust;
        }

        public String getCostPriceOne() {
            return costPriceOne;
        }

        public void setCostPriceOne(String costPriceOne) {
            this.costPriceOne = costPriceOne;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "LogInfosBean{" +
                    "countAll='" + countAll + '\'' +
                    ", nowAmount='" + nowAmount + '\'' +
                    ", created='" + created + '\'' +
                    ", profitLossScale='" + profitLossScale + '\'' +
                    ", costPriceAll='" + costPriceAll + '\'' +
                    ", stockCode='" + stockCode + '\'' +
                    ", profitLossAmount='" + profitLossAmount + '\'' +
                    ", nowPrice='" + nowPrice + '\'' +
                    ", stockName='" + stockName + '\'' +
                    ", countCan='" + countCan + '\'' +
                    ", stockholderCode='" + stockholderCode + '\'' +
                    ", countEntrust='" + countEntrust + '\'' +
                    ", costPriceOne='" + costPriceOne + '\'' +
                    ", id='" + id + '\'' +
                    '}';
        }
    }
}
