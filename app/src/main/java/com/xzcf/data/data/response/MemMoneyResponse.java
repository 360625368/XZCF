package com.xzcf.data.data.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MemMoneyResponse extends BaseRes {

    @SerializedName("logInfos" )
    private List<LogInfosBean> logInfos;

    public LogInfosBean getLogInfo() {
        return logInfos == null ? null : logInfos.get(0);
    }

    public List<LogInfosBean> getLogInfos() {
        return logInfos;
    }

    public void setLogInfos(List<LogInfosBean> logInfos) {
        this.logInfos = logInfos;
    }

    public static class LogInfosBean {
        @SerializedName("profitLossAmount" )
        private String profitLossAmount;
        @SerializedName("balanceFreeze" )
        private String balanceFreeze;
        @SerializedName("commissionScale" )
        private String commissionScale;
        @SerializedName("assetsInit" )
        private String assetsInit;
        @SerializedName("balanceUse" )
        private String balanceUse;
        @SerializedName("balanceTake" )
        private String balanceTake;
        @SerializedName("balanceAll" )
        private String balanceAll;
        @SerializedName("transferFee" )
        private String transferFee;
        @SerializedName("assetsReference" )
        private String assetsReference;
        @SerializedName("commissionLowest" )
        private String commissionLowest;
        @SerializedName("assetsAll" )
        private String assetsAll;

        public String getProfitLossAmount() {
            return profitLossAmount;
        }

        public void setProfitLossAmount(String profitLossAmount) {
            this.profitLossAmount = profitLossAmount;
        }

        public String getBalanceFreeze() {
            return balanceFreeze;
        }

        public void setBalanceFreeze(String balanceFreeze) {
            this.balanceFreeze = balanceFreeze;
        }

        public String getCommissionScale() {
            return commissionScale;
        }

        public void setCommissionScale(String commissionScale) {
            this.commissionScale = commissionScale;
        }

        public String getAssetsInit() {
            return assetsInit;
        }

        public void setAssetsInit(String assetsInit) {
            this.assetsInit = assetsInit;
        }

        public String getBalanceUse() {
            return balanceUse;
        }

        public void setBalanceUse(String balanceUse) {
            this.balanceUse = balanceUse;
        }

        public String getBalanceTake() {
            return balanceTake;
        }

        public void setBalanceTake(String balanceTake) {
            this.balanceTake = balanceTake;
        }

        public String getBalanceAll() {
            return balanceAll;
        }

        public void setBalanceAll(String balanceAll) {
            this.balanceAll = balanceAll;
        }

        public String getTransferFee() {
            return transferFee;
        }

        public void setTransferFee(String transferFee) {
            this.transferFee = transferFee;
        }

        public String getAssetsReference() {
            return assetsReference;
        }

        public void setAssetsReference(String assetsReference) {
            this.assetsReference = assetsReference;
        }

        public String getCommissionLowest() {
            return commissionLowest;
        }

        public void setCommissionLowest(String commissionLowest) {
            this.commissionLowest = commissionLowest;
        }

        public String getAssetsAll() {
            return assetsAll;
        }

        public void setAssetsAll(String assetsAll) {
            this.assetsAll = assetsAll;
        }
    }
}
