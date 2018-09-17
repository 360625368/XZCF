package com.xzcf.data.data.response;

import android.text.TextUtils;

import com.guoziwei.klinelib.model.HisData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class KMinuteChartRes extends BaseRes {

    private String KPrice;
    private String KCount;

    public String getKPrice() {
        return KPrice;
    }

    public KMinuteChartRes setKPrice(String KPrice) {
        this.KPrice = KPrice;
        return this;
    }

    public String getKCount() {
        return KCount;
    }

    public KMinuteChartRes setKCount(String KCount) {
        this.KCount = KCount;
        return this;
    }

    public List<HisData> splitChartData(){
        SimpleDateFormat sFormat1 = new SimpleDateFormat("HHmm", Locale.getDefault());
        List<HisData> hisDataList = new ArrayList<>();
        String result = data.getResult();
        if (!TextUtils.isEmpty(result)) {
            String[] splitRoot = result.split("\n");
            for (int i = 1,len = splitRoot.length; i <len; i++) {
                HisData data = new HisData();
                String[] splitStr = splitRoot[i].split("\t");
                data.setClose(Double.valueOf(splitStr[0]));
                data.setVol(Long.valueOf(splitStr[1]));
                String[] splitOpenStr = splitRoot[i-1].split("\t");
                data.setOpen(i == 1 ? 0 : Double.valueOf(splitOpenStr[0]));
                String formatTime = sFormat1.format(new Date());
                data.setDate(Long.valueOf(formatTime));
                hisDataList.add(data);
            }
        }

        return hisDataList;
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
