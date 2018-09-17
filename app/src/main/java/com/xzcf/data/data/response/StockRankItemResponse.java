package com.xzcf.data.data.response;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;


public class StockRankItemResponse extends BaseRes {


    /**
     *
     * 我们用的字符串是股票编号，股票名称，股票现价，股票涨幅
     对应字符串以逗号切分分别是第二个、第三个、第四个、第六个
     *
     *
     */

    private String zhangFu;
    private String dieFu;


    private String [] zhangSplit;
    private String [] dieSplit;


    /**
     * 分割 "
     * @return 分割涨幅列表
     */
    public String[] getZhangFuSplit(){
        if (!TextUtils.isEmpty(zhangFu)) {
            zhangSplit = zhangFu.split("\"");
            return zhangSplit;
        }
        return new String[]{};
    }

    /**
     * 分割 "
     * @return 分割跌幅列表
     */
    public String[] getDieFuSplit(){
        if (!TextUtils.isEmpty(dieFu)) {
            dieSplit = dieFu.split("\"");
            return dieSplit;
        }
        return new String[]{};
    }

    public List<String[]> getDieFuArrs() {
        if (!TextUtils.isEmpty(dieFu)) {
            String[] split = dieFu.replace("[\"", "").replace("\"]", "").split("\",\"");
            List<String[]> temp = new ArrayList<>();
            for (String s : split) {
                temp.add(s.split(","));
            }
            return temp;
        }
        return null;
    }

    public List<String[]> getZhangFuArrs() {
        if (!TextUtils.isEmpty(zhangFu)) {
            String[] split = zhangFu.replace("[\"", "").replace("\"]", "").split("\",\"");
            List<String[]> temp = new ArrayList<>();
            for (String s : split) {
                temp.add(s.split(","));
            }
            return temp;
        }
        return null;
    }

    public String getZhangFu() {
        return zhangFu;
    }

    public void setZhangFu(String zhangFu) {
        this.zhangFu = zhangFu;
    }

    public String getDieFu() {
        return dieFu;
    }

    public void setDieFu(String dieFu) {
        this.dieFu = dieFu;
    }
}
