package com.xzcf.utils;

import com.xzcf.data.data.response.StockMarketFiveSpeedBean;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class MathUtils {

    private final static String HundredMillionFormat = "%s亿";
    private final static String TenThousandFormat = "%s万";


    /**
     * double转String,保留小数点后两位
     *
     * @param num
     * @return
     */
    public static String doubleToString(double num) {
        //使用0.00不足位补0，#.##仅保留有效位
        String format = new DecimalFormat("0.00").format(num);
        return format;
    }

    /**
     * double转String,保留小数点后二位
     * @param num
     * @return
     */
    public static String doubleToStringByTwo(double num){
        //使用0.00不足位补0，#.##仅保留有效位
        String format = new DecimalFormat("0.00").format(num);
        if (!format.contains(".")) {
            return format;
        }
        String substring = format.substring(format.indexOf(".") + 1);
        if (substring.equals("00")) {
            return format.substring(0,format.indexOf("."));
        }
        return format;
    }

    /**
     * double转String,保留小数点后一位
     *
     * @param num
     * @return
     */
    public static String doubleToStringByOne(double num) {
        //使用0.00不足位补0，#.##仅保留有效位
        return new DecimalFormat("0.0").format(num);
    }

    /**
     * double转String,不保留小数
     *
     * @param num
     * @return
     */
    public static String doubleToStringByZero(double num) {
        //使用0.00不足位补0，#.##仅保留有效位
        return new DecimalFormat("0").format(num);
    }

    /**
     * 亿
     */
    public static String doubleToHundredMillion(String content) {
        Double valueOf = Double.valueOf(content);
        double num = valueOf / 100000000;
        String toString = doubleToString(num);
        return String.format(Locale.getDefault(), HundredMillionFormat, toString);
    }

    /**
     * 万
     */
    public static String doubleToTenThousand(String content) {
        Double valueOf = Double.valueOf(content);
        double num = valueOf / 10000;
        String toString = doubleToStringByOne(num);
        return String.format(Locale.getDefault(), TenThousandFormat, toString);
    }

    /**
     * 单位转换
     *
     * @param content
     * @return
     */
    public static String checkUnit(String content) {
        Double valueOf = Double.valueOf(content);

        if (valueOf < 10000) {
            // 小于1万
            return doubleToStringByZero(valueOf);
        } else if (valueOf >= 10000 && valueOf < 100000000) {
            // 大于1万 小于1亿
            valueOf = valueOf / 10000;
            String toString = doubleToString(valueOf);
            return String.format(Locale.getDefault(), TenThousandFormat, toString);
        } else {
            // 大于1亿
            valueOf = valueOf / 100000000;
            String toString = doubleToString(valueOf);
            return String.format(Locale.getDefault(), HundredMillionFormat, toString);
        }

    }


    /**
     * 五档排序
     */
    public static List<StockMarketFiveSpeedBean> sortMarketFiveSpeed(HashMap<String, String> hashMap) {
        List<StockMarketFiveSpeedBean> marketFiveSpeedBeanList = new ArrayList<>();
        // 卖
        marketFiveSpeedBeanList.add(0, new StockMarketFiveSpeedBean()
                .setTitle("卖五")
                .setPrice(hashMap.get("卖五价"))
                .setCount(hashMap.get("卖五量")));
        marketFiveSpeedBeanList.add(1, new StockMarketFiveSpeedBean()
                .setTitle("卖四")
                .setPrice(hashMap.get("卖四价"))
                .setCount(hashMap.get("卖四量")));
        marketFiveSpeedBeanList.add(2, new StockMarketFiveSpeedBean()
                .setTitle("卖三")
                .setPrice(hashMap.get("卖三价"))
                .setCount(hashMap.get("卖三量")));
        marketFiveSpeedBeanList.add(3, new StockMarketFiveSpeedBean()
                .setTitle("卖二")
                .setPrice(hashMap.get("卖二价"))
                .setCount(hashMap.get("卖二量")));
        marketFiveSpeedBeanList.add(4, new StockMarketFiveSpeedBean()
                .setTitle("卖一")
                .setPrice(hashMap.get("卖一价"))
                .setCount(hashMap.get("卖一量")));
        // 买
        marketFiveSpeedBeanList.add(5, new StockMarketFiveSpeedBean()
                .setTitle("买一")
                .setPrice(hashMap.get("买一价"))
                .setCount(hashMap.get("买一量")));
        marketFiveSpeedBeanList.add(6, new StockMarketFiveSpeedBean()
                .setTitle("买二")
                .setPrice(hashMap.get("买二价"))
                .setCount(hashMap.get("买二量")));
        marketFiveSpeedBeanList.add(7, new StockMarketFiveSpeedBean()
                .setTitle("买三")
                .setPrice(hashMap.get("买三价"))
                .setCount(hashMap.get("买三量")));
        marketFiveSpeedBeanList.add(8, new StockMarketFiveSpeedBean()
                .setTitle("买四")
                .setPrice(hashMap.get("买四价"))
                .setCount(hashMap.get("买四量")));
        marketFiveSpeedBeanList.add(9, new StockMarketFiveSpeedBean()
                .setTitle("买五")
                .setPrice(hashMap.get("买五价"))
                .setCount(hashMap.get("买五量")));

        return marketFiveSpeedBeanList;
    }


    /**
     * 计算涨跌差
     *
     * @param todayPrice     现价
     * @param yesterdayPrice 昨日收盘价
     * @return 差价
     */
    public static double getZhangDie(Double todayPrice, Double yesterdayPrice) {
        return todayPrice - yesterdayPrice;
    }

    /**
     * 计算涨跌幅
     *
     * @param todayPrice     现价
     * @param yesterdayPrice 昨日收盘价
     * @return 涨跌幅
     */
    // 涨幅=(现价-上一个交易日收盘价）/上一个交易日收盘价*100%
    public static double getZhangFu(double todayPrice, Double yesterdayPrice) {

        return (todayPrice - yesterdayPrice) / yesterdayPrice * 100;
    }


    /**
     * 四舍五入 保留两位小数
     *
     * @param num
     * @return
     */
    public static String formatZhang(Double num) {
        BigDecimal b = new BigDecimal(num);
        BigDecimal f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP);
        System.out.println("zhangFormat:" + String.valueOf(f1));
        String s = String.valueOf(f1);
        long round = Math.round(f1.doubleValue());
        System.out.println("zhangFormat-rount:" + round);
        return s;
    }

    /**
     * 四舍五入 保留两位小数
     *
     * @param num
     * @return
     */
    public static String formatDie(Double num) {
        BigDecimal b = new BigDecimal(num);
        BigDecimal f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP);
        System.out.println("dieFormat:" + String.valueOf(f1));
        String s = String.valueOf(f1);
        long round = Math.round(f1.doubleValue());
        System.out.println("dieFormat-rount:" + round);
        return s;
    }


}
