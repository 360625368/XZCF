package com.xzcf.utils;

import android.text.TextUtils;

import com.xzcf.App;
import com.xzcf.data.data.response.LoginInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class StockRuleUtils {


    /**
     *
     * 股票代码6打头的是沪市，其他0和3打头的都是深市
     *
     * @param stockCode 股票代码
     * @return 是否是沪市
     */
    public static boolean isShangHaiMarket(String stockCode){
        if (stockCode.startsWith("6")) {
            return true;
        }else{
            return false;
        }
    }


    /**
     * 计算涨停
     * @param price 收盘价
     *  n*(1+10%) 1.1 涨停
     *  n*(1+5%) 1.05 涨停
     *  名称ST打头的股票，挂单价格上下不能超过正负5%，
     *  非ST打头的是10%
     */
    public static String calculateZhangTing(String stockName,String price){
        String value;
        BigDecimal d = new BigDecimal(price);// 昨收
        if (stockName.contains("ST")||stockName.contains("st")) {
            BigDecimal r = new BigDecimal(1.05);
            BigDecimal i = d.multiply(r).setScale(3, RoundingMode.HALF_EVEN);     //使用银行家算法
            value = i.toString();
        }else{
            BigDecimal r = new BigDecimal(1.1);
            BigDecimal i = d.multiply(r).setScale(3, RoundingMode.HALF_EVEN);     //使用银行家算法
            value = i.toString();
        }

        value = overWriteHalfEven(value);

        return value;
    }


    /**
     * 计算跌停
     * n*(1-10%) 0.9 跌停
     * n*(1-5%) 0.95 跌停
     * 名称ST打头的股票，挂单价格上下不能超过正负5%，
     * 非ST打头的是10%
     */
    public static String calculateDieTing(String stockName,String price){
        String value;
        BigDecimal d = new BigDecimal(price);// 昨收
        if (stockName.contains("ST")||stockName.contains("st")) {
            BigDecimal r = new BigDecimal(0.95);
            BigDecimal i = d.multiply(r).setScale(3, RoundingMode.HALF_EVEN);     //使用银行家算法
            value = i.toString();

        }else{
            BigDecimal r = new BigDecimal(0.9);
            BigDecimal bigDecimalDie = d.multiply(r).setScale(3, RoundingMode.HALF_EVEN);     //使用银行家算法
            value = bigDecimalDie.toString();
        }

        value = overWriteHalfEven(value);


        return value;
    }

    /**
     * 是否是正确的股票代码
     * 股票代码长度等于六位
     */
    public static boolean isTrueStockCode(String stockCode){
        if (TextUtils.isEmpty(stockCode)) {
            return false;
        }else if(stockCode.length()==6){
            return true;
        }

        return false;
    }


    /**
     * 校验买入卖出价是否在 收盘价 ±幅度之间
     * @param yesterdayPrice 收盘价
     * @return 价格幅度价格
     *
     * 名称ST打头的股票，挂单价格上下不能超过正负5%，
     *  非ST打头的是10%
        在昨日收盘价上面5%和10%的计算
        比如一只股票的收盘价是10元。那它的买入价格，就是在9元至11元之间选择
     */
    public static Double[] buyPriceCheckInterval(String stockName,String yesterdayPrice){
        Double[] intervalPrice = new Double[2];
        intervalPrice[0] = Double.valueOf(calculateDieTing(stockName,yesterdayPrice));
        intervalPrice[1] = Double.valueOf(calculateZhangTing(stockName,yesterdayPrice));

        return intervalPrice;
    }


    /**
     * 重写银行家算法
     * 防止遇见5 前一位为偶数舍弃 奇数进位；同意改为遇5均进位
     */
    private static String overWriteHalfEven(String price){
        try{
            if (!TextUtils.isEmpty(price) && price.contains(".")) {
                String substring1 = price.substring(price.indexOf(".")+1, price.length());
                if (substring1.length()==3) {
                    String substring = price.substring(price.length()-1, price.length());
                    if (TextUtils.equals(substring,"5")) { // 遇5
                        double v = Double.valueOf(price) + 0.01;//进位
                        String strV = String.valueOf(v);
                        String subV = strV.substring(0, strV.indexOf(".") + 3);// 防止重复进位直接截取
                        return subV;
                    }
                }

            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

        //其余情况直接按正常算法使用
        BigDecimal d = new BigDecimal(price).setScale(2, RoundingMode.HALF_EVEN);

        return d.toString();
    }


    /**
     * 判断是否是测试账号进入
     */
    public static boolean isTestAccount(){
        if (App.context().isLogged()) {
            LoginInfo loginInfo = App.context().getLoginInfo();
            if (loginInfo != null) {
                if (TextUtils.equals(loginInfo.getMemberId(),"54")) {
                    return true;
                }
            }
        }
        return false;
    }


}
