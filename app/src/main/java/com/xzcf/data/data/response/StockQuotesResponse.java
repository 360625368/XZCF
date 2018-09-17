package com.xzcf.data.data.response;

import android.text.TextUtils;

import java.util.HashMap;


public class StockQuotesResponse extends BaseRes {


    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String result;
        private String count;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }


        public HashMap<String, String> getResultSingleSplit() {
            HashMap<String, String> stockMap = new HashMap<>();
            if (!TextUtils.isEmpty(result)) {
                String[] split = result.split("\n");
                String[] splitTitleSplit = split[0].split("\t");
                String[] splitCodeSplit = split[1].split("\t");
                for (int i = 0, len = splitCodeSplit.length; i < len; i++) {
                    stockMap.put(splitTitleSplit[i], splitCodeSplit[i]);
                }
                return stockMap;
            }
            return stockMap;
        }

        public String getResultTodayPrice(int index) {
            String todayPrice = "";
            String[] split = result.split("\n");
            String[] spTitle = split[0].split("\t");
            String[] spContent;
            if (index == split.length) {
                spContent = split[index - 1].split("\t");
            } else {
                spContent = split[index].split("\t");
            }
            for (int i = 0; i < spTitle.length; i++) {
                String sTitle = spTitle[i];
                if (TextUtils.equals(sTitle, "现价")) {
                    todayPrice = spContent[i];
                    break;
                }
            }
            return todayPrice;
        }

        public String getResultYesterdayPrice(int index) {
            String yesterdayPrice = "";
            String[] split = result.split("\n");
            String[] spTitle = split[0].split("\t");
            String[] spContent;
            if (index == split.length) {
                spContent = split[index - 1].split("\t");
            } else {
                spContent = split[index].split("\t");
            }
            for (int i = 0; i < spTitle.length; i++) {
                String sTitle = spTitle[i];
                if (TextUtils.equals(sTitle, "昨收")) {
                    yesterdayPrice = spContent[i];
                    break;
                }
            }
            return yesterdayPrice;
        }


        @Override
        public String toString() {
            return "DataBean{" +
                    "result='" + result + '\'' +
                    ", count='" + count + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "StockQuotesResponse{" +
                "data=" + data.toString() +
                '}';
    }
}
