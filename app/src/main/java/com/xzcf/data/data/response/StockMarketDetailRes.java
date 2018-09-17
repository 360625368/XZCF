package com.xzcf.data.data.response;

import android.text.TextUtils;

import com.xzcf.utils.MathUtils;

import java.util.ArrayList;
import java.util.List;


public class StockMarketDetailRes extends BaseRes {

    private String detailTime;// 时间
    private String detailPrice;// 价格
    private String detailCount; // 现量
    private String detailBuyType;// 买卖

    public String getDetailTime() {
        return detailTime;
    }

    public StockMarketDetailRes setDetailTime(String detailTime) {
        this.detailTime = detailTime;
        return this;
    }

    public String getDetailPrice() {

        return MathUtils.doubleToString(Double.valueOf(detailPrice));
    }

    public StockMarketDetailRes setDetailPrice(String detailPrice) {
        this.detailPrice = detailPrice;
        return this;
    }

    public String getDetailCount() {
        return detailCount;
    }

    public StockMarketDetailRes setDetailCount(String detailCount) {
        this.detailCount = detailCount;
        return this;
    }

    public Boolean isDetailBuy() {
        if (TextUtils.equals(detailBuyType,"0")) {
            // 买 红色
            return true;
        }
        // 卖 绿色
        return false;
    }

    public StockMarketDetailRes setDetailBuyType(String detailBuyType) {
        this.detailBuyType = detailBuyType;
        return this;
    }

    // 拆分详情数据
    public List<StockMarketDetailRes> splitDetailData(){
        List<StockMarketDetailRes> stockMarketDetailResList = new ArrayList<>();
        String result = data.getResult();
        if (!TextUtils.isEmpty(result)) {
            String[] splitRoot = result.split("\n");
            for (int i = 1; i < splitRoot.length; i++) {
                StockMarketDetailRes stockMarketDetailRes = new StockMarketDetailRes();
                String[] splitStr = splitRoot[i].split("\t");
                stockMarketDetailRes.setDetailTime(splitStr[0]).setDetailPrice(splitStr[1]).setDetailCount(splitStr[2]).setDetailBuyType(splitStr[4]);
                stockMarketDetailResList.add(stockMarketDetailRes);
            }
        }

        return stockMarketDetailResList;
    }

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String result;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }
}
