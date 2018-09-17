package com.xzcf.data.data.response;

import android.text.TextUtils;

import com.guoziwei.klinelib.model.HisData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class KDayChartRes extends BaseRes {


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

        public List<HisData> splitChartData(){
            // 2018-07-30 09:35
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat simpleDateExFormat = new SimpleDateFormat("yyyyMMdd");
            List<HisData> hisDataList = new ArrayList<>();
            try{
                if (!TextUtils.isEmpty(result)) {
                    System.out.println(result);
                    String[] splitRoot = result.split("\n");
                    for (int i = 1,len = splitRoot.length; i <len; i++) {
                        HisData data = new HisData();
                        String[] splitStr = splitRoot[i].split("\t");
                        data.setOpen(Double.valueOf(splitStr[1].trim()));
                        data.setClose(Double.valueOf(splitStr[2].trim()));
                        data.setHigh(Double.valueOf(splitStr[3].trim()));
                        data.setLow(Double.valueOf(splitStr[4].trim()));
                        data.setVol(Long.valueOf(splitStr[5].trim()));
                        //data.setAvePrice(Long.valueOf(splitStr[6].trim()));
                        if (splitStr[0].trim().contains("-")) {
                            Date parse = simpleDateFormat.parse(splitStr[0].trim());
                            data.setDate(parse.getTime());
                        }else{
                            Date parse = simpleDateExFormat.parse(splitStr[0].trim());
                            data.setDate(parse.getTime());
                        }

                        hisDataList.add(data);
                    }
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }

            return hisDataList;
        }

    }
}
