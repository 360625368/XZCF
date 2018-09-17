package com.xzcf.data.data.response;

import java.io.Serializable;

public class MyResponse implements Serializable {

    private static final long serialVersionUID = 6703727618980700445L;
    protected String code;
    protected String msg;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
