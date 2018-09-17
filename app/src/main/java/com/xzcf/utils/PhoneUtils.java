package com.xzcf.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PhoneUtils {


    /**
     * 检查是否是电话号码
     *
     * @return
     */
    public static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

}
