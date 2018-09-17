package com.xzcf.data.data.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BargainRecordResponse extends BaseRes {


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
        @SerializedName("saleAmount" )
        private String saleAmount;
        @SerializedName("code" )
        private String codeX;
        @SerializedName("salePrice" )
        private String salePrice;
        @SerializedName("created" )
        private String created;
        @SerializedName("saleCount" )
        private String saleCount;
        @SerializedName("type" )
        private String type;
        @SerializedName("stockCode" )
        private String stockCode;
        @SerializedName("tdxEntrustCode" )
        private String tdxEntrustCode;
        @SerializedName("stockName" )
        private String stockName;
        @SerializedName("stampDuty" )
        private String stampDuty;
        @SerializedName("stockholderCode" )
        private String stockholderCode;
        @SerializedName("transferFee" )
        private String transferFee;
        @SerializedName("commission" )
        private String commission;
        @SerializedName("id" )
        private String id;
        @SerializedName("realSaleTime" )
        private String realSaleTime;
        @SerializedName("memberId" )
        private String memberId;

        public String getSaleAmount() {
            return saleAmount;
        }

        public void setSaleAmount(String saleAmount) {
            this.saleAmount = saleAmount;
        }

        public String getCodeX() {
            return codeX;
        }

        public void setCodeX(String codeX) {
            this.codeX = codeX;
        }

        public String getSalePrice() {
            return salePrice;
        }

        public void setSalePrice(String salePrice) {
            this.salePrice = salePrice;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getSaleCount() {
            return saleCount;
        }

        public void setSaleCount(String saleCount) {
            this.saleCount = saleCount;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStockCode() {
            return stockCode;
        }

        public void setStockCode(String stockCode) {
            this.stockCode = stockCode;
        }

        public String getTdxEntrustCode() {
            return tdxEntrustCode;
        }

        public void setTdxEntrustCode(String tdxEntrustCode) {
            this.tdxEntrustCode = tdxEntrustCode;
        }

        public String getStockName() {
            return stockName;
        }

        public void setStockName(String stockName) {
            this.stockName = stockName;
        }

        public String getStampDuty() {
            return stampDuty;
        }

        public void setStampDuty(String stampDuty) {
            this.stampDuty = stampDuty;
        }

        public String getStockholderCode() {
            return stockholderCode;
        }

        public void setStockholderCode(String stockholderCode) {
            this.stockholderCode = stockholderCode;
        }

        public String getTransferFee() {
            return transferFee;
        }

        public void setTransferFee(String transferFee) {
            this.transferFee = transferFee;
        }

        public String getCommission() {
            return commission;
        }

        public void setCommission(String commission) {
            this.commission = commission;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRealSaleTime() {
            return realSaleTime;
        }

        public void setRealSaleTime(String realSaleTime) {
            this.realSaleTime = realSaleTime;
        }

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }
    }
}
