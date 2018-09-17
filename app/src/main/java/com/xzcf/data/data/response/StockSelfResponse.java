package com.xzcf.data.data.response;

import java.util.List;


public class StockSelfResponse extends BaseRes {


    private List<InfosBean> infos;

    public List<InfosBean> getInfos() {
        return infos;
    }

    public void setInfos(List<InfosBean> infos) {
        this.infos = infos;
    }

    public static class InfosBean {
        private String selfSelectionId;
        private String stockName;
        private String stockCode;

        public String getSelfSelectionId() {
            return selfSelectionId;
        }

        public void setSelfSelectionId(String selfSelectionId) {
            this.selfSelectionId = selfSelectionId;
        }

        public String getStockName() {
            return stockName;
        }

        public void setStockName(String stockName) {
            this.stockName = stockName;
        }

        public String getStockCode() {
            return stockCode;
        }

        public void setStockCode(String stockCode) {
            this.stockCode = stockCode;
        }
    }
}
