package com.xzcf.data.data.response;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;


public class StockRankItemTitle extends BaseRes {


    private List<String> upList;
    private List<String> downList;

    public List<String> getUpList() {
        return upList;
    }

    public void setUpList(String[] splitUp) {
        this.upList = new ArrayList<>();
        for (String s : splitUp) {
            if (!TextUtils.equals(s,",")&&!TextUtils.equals(s,"[")&&!TextUtils.equals(s,"]")) {
                upList.add(s);
            }
        }
    }

    public List<String> getDownList() {
        return downList;
    }

    public void setDownList(String[] splitDown) {
        this.downList = new ArrayList<>();
        for (String s : splitDown) {
            if (!TextUtils.equals(s,",")&&!TextUtils.equals(s,"[")&&!TextUtils.equals(s,"]")) {
                downList.add(s);
            }
        }
    }

    private String[] getSplit(String splitStr){
        return splitStr.split(",");
    }

    /**
     * 股票代码
     * @param splitStr 单条待分割字符串
     * @return 分割后数据
     */
    public String getStockCode(String splitStr) {
            return getSplit(splitStr)[1];
    }

    /**
     * 股票名称
     * @param splitStr 单条待分割字符串
     * @return 分割后数据
     */
    public String getStockName(String splitStr) {
        return getSplit(splitStr)[2];
    }

    /**
     * 股票价格
     * @param splitStr 单条待分割字符串
     * @return 分割后数据
     */
    public String getStockPrice(String splitStr) {
        return getSplit(splitStr)[3];
    }

    /**
     * 股票涨跌幅
     * @param splitStr 单条待分割字符串
     * @return 分割后数据
     */
    public String getStockRate(String splitStr) {
        return getSplit(splitStr)[5];
    }

    public String getStockRateOne(String splitStr){
        return getSplit(splitStr)[4];
    }

}
