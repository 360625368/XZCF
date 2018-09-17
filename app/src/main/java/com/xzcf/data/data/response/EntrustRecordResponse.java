package com.xzcf.data.data.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EntrustRecordResponse extends BaseRes {


    @SerializedName("currentPage")
    private String currentPage;
    @SerializedName("totalPage")
    private String totalPage;
    @SerializedName("total")
    private String total;
    @SerializedName("pageSize")
    private String pageSize;
    @SerializedName("logInfos")
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
        @SerializedName("code")
        private String codeX;
        @SerializedName("saleCount")
        private String saleCount;
        @SerializedName("timeCancel")
        private String timeCancel;
        @SerializedName("timeOver")
        private String timeOver;
        @SerializedName("type")
        private String type;
        @SerializedName("allCount")
        private String allCount;
        @SerializedName("remarksOrder")
        private String remarksOrder;
        @SerializedName("stockCode")
        private String stockCode;
        @SerializedName("stockName")
        private String stockName;
        @SerializedName("allPrice")
        private String allPrice;
        @SerializedName("avgAmount")
        private String avgAmount;
        @SerializedName("allAmount")
        private String allAmount;
        @SerializedName("timeEntrust")
        private String timeEntrust;
        @SerializedName("id")
        private String id;
        @SerializedName("state")
        private String state;
        @SerializedName("memberId")
        private String memberId;
        @SerializedName("stockholderCode")
        private String stockholderCode;

        public String getCodeX() {
            return codeX;
        }

        public void setCodeX(String codeX) {
            this.codeX = codeX;
        }

        public String getSaleCount() {
            return saleCount;
        }

        public void setSaleCount(String saleCount) {
            this.saleCount = saleCount;
        }

        public String getTimeCancel() {
            return timeCancel;
        }

        public void setTimeCancel(String timeCancel) {
            this.timeCancel = timeCancel;
        }

        public String getTimeOver() {
            return timeOver;
        }

        public void setTimeOver(String timeOver) {
            this.timeOver = timeOver;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAllCount() {
            return allCount;
        }

        public void setAllCount(String allCount) {
            this.allCount = allCount;
        }

        public String getRemarksOrder() {
            return remarksOrder;
        }

        public void setRemarksOrder(String remarksOrder) {
            this.remarksOrder = remarksOrder;
        }

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

        public String getAllPrice() {
            return allPrice;
        }

        public void setAllPrice(String allPrice) {
            this.allPrice = allPrice;
        }

        public String getAvgAmount() {
            return avgAmount;
        }

        public void setAvgAmount(String avgAmount) {
            this.avgAmount = avgAmount;
        }

        public String getAllAmount() {
            return allAmount;
        }

        public void setAllAmount(String allAmount) {
            this.allAmount = allAmount;
        }

        public String getTimeEntrust() {
            return timeEntrust;
        }

        public void setTimeEntrust(String timeEntrust) {
            this.timeEntrust = timeEntrust;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getStockholderCode() {
            return stockholderCode;
        }

        public void setStockholderCode(String stockholderCode) {
            this.stockholderCode = stockholderCode;
        }
    }
}
