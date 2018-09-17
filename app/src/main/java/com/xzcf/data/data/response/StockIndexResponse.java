package com.xzcf.data.data.response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


public class StockIndexResponse extends BaseRes {


    /**
     * 上证、深证、中小、创业 按顺序的指数排行，
     *
     * "上证指数,3082.2316,7.2015,0.23,1312256,17952026;\
     * "深证成指,10324.47,32.349,0.31,164776511,23255897;\
     * "中小板指,6998.77,58.084,0.84,19034130,3356191;\
     * "创业板指,1805.60,24.320,1.37,16989168,2847150;
     *
     * 内容顺序是 指数名称，当前点数，当前价格，涨跌率，成交量（手），成交额（万元）
     *
     */

    private String stockIndexs;


    public String getStockIndexsStr(){
        return stockIndexs;
    }

    public List<String> getStockIndexs() {
        List<String> indexList = new ArrayList<>();
        if (stockIndexs.contains(";")) {
            String[] split = stockIndexs.split(";");
            for (int i = 0; i < split.length; i++) {
                indexList.add(split[i].replace("\""," ").trim());
            }
        }

        return indexList;
    }

    public StockIndexResponse setStockIndexs(String stockIndexs) {
        this.stockIndexs = stockIndexs;
        return this;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this, new TypeToken<StockIndexResponse>(){}.getType());
    }

}
