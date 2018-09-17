package com.xzcf.data.data.response;

import android.text.TextUtils;


public class BaseRes {

    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public BaseRes setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public BaseRes setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public boolean isOk(){
        if (TextUtils.equals(code,"0")) {
            return true;
        }

        return false;
    }


}
