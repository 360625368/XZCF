package com.xzcf.data.data.req;

import android.text.TextUtils;


public class StockSelfReq {


    /**
     *
     * TRANSID	是	string	任务标识 APP_SELF_SELECTION
     operateType	是	string	类型 1自选列表2新增自选3删除自选
     memberId	是	string	用户ID
     stockCode	是	string	股票编号（仅新增时传入）
     stockName	是	string	股票名称（仅新增时传入）
     selfSelectionId	是	string	自选股票ID （仅删除时传入）
     *
     *
     *
     *
     *
     */


    private String operateType;
    private String memberId;
    private String stockCode;
    private String stockName;
    private String selfSelectionId;


    private String queryCommand = "1";
    private String addCommand = "2";
    private String delCommand = "3";

    public String getOperateType() {
        return operateType;
    }


    public String getMemberId() {
        return memberId;
    }

    public StockSelfReq setMemberId(String memberId) {
        this.memberId = memberId;
        return this;
    }

    public String getStockCode() {
        return stockCode;
    }

    public StockSelfReq setStockCode(String stockCode) {
        this.stockCode = stockCode;
        return this;
    }

    public String getStockName() {
        return stockName;
    }

    public StockSelfReq setStockName(String stockName) {
        this.stockName = stockName;
        return this;
    }

    public String getSelfSelectionId() {
        return selfSelectionId;
    }

    public StockSelfReq setSelfSelectionId(String selfSelectionId) {
        this.selfSelectionId = selfSelectionId;
        return this;
    }


    public boolean isQueryCommand(){
        if (TextUtils.equals(getOperateType(),"1")) {
            return true;
        }
        return false;
    }

    public boolean isAddCommand(){
        if (TextUtils.equals(getOperateType(),"2")) {
            return true;
        }
        return false;
    }

    public boolean isDelCommand(){
        if (TextUtils.equals(getOperateType(),"3")) {
            return true;
        }
        return false;
    }

    public StockSelfReq setQueryCommand(){
        this.operateType = queryCommand;
        return this;
    }

    public StockSelfReq setAddCommand(){
        this.operateType = addCommand;
        return this;
    }

    public StockSelfReq setDelCommand(){
        this.operateType = delCommand;
        return this;
    }

}
