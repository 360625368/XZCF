package com.xzcf.data.data.response;

import android.text.TextUtils;

import com.xzcf.utils.MathUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class StockMarketTitleResponse implements Serializable {

    private String title;
    private String content;

    public String getTitle() {
        return title;
    }

    public StockMarketTitleResponse setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public StockMarketTitleResponse setContent(String content) {
        if (TextUtils.equals(this.title,"总金额")){
            this.content = MathUtils.checkUnit(content);
        } else if(TextUtils.equals(this.title,"总量")){
            this.content = MathUtils.checkUnit(content);
        } else if(content.contains(".")){
            int length = content.indexOf(".")+3;// 两位小数位+substring末尾忽略位
            if (length<content.length()){
                this.content = content.substring(0, length);
                if(TextUtils.equals(this.title,"涨速")) {
                    this.content += "%";
                }
            }else{
                this.content = content;
            }
        } else{
            this.content = content;
        }
        return this;
    }

    // 筛选五档
    public List<String> getFiveSpeedConstant() {
        List<String> keyConstant = new ArrayList<>();
        keyConstant.add("买一价");
        keyConstant.add("卖一价");
        keyConstant.add("买一量");
        keyConstant.add("卖一量");

        keyConstant.add("买二价");
        keyConstant.add("卖二价");
        keyConstant.add("买二量");
        keyConstant.add("卖二量");

        keyConstant.add("买三价");
        keyConstant.add("卖三价");
        keyConstant.add("买三量");
        keyConstant.add("卖三量");

        keyConstant.add("买四价");
        keyConstant.add("卖四价");
        keyConstant.add("买四量");
        keyConstant.add("卖四量");

        keyConstant.add("买五价");
        keyConstant.add("卖五价");
        keyConstant.add("买五量");
        keyConstant.add("卖五量");
        return keyConstant;
    }

    // 筛选保留
    public List<String> getReserveConstant() {
        List<String> keyConstant = new ArrayList<>();
        keyConstant.add("保留");
        return keyConstant;
    }
    // 筛选 股票名称、股票代码、市场、开盘、昨收、现价
    public List<String> getStockInfoConstant() {
        List<String> keyConstant = new ArrayList<>();
        keyConstant.add("股票名称");
        keyConstant.add("市场");
        keyConstant.add("代码");
        keyConstant.add("开盘");
        keyConstant.add("昨收");
        keyConstant.add("现价");
        return keyConstant;
    }

}
