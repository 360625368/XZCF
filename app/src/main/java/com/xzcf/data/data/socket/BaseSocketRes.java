package com.xzcf.data.data.socket;

/**
 * Description:
 * -
 * Author：chasen
 * Date： 2018/9/17 18:23
 */
public class BaseSocketRes<T> {

    private String Qid;
    private int Err;
    private int Counter;
    private T Data;

    public String getQid() {
        return Qid;
    }

    public int getErr() {
        return Err;
    }

    public int getCounter() {
        return Counter;
    }

    public T getData() {
        return Data;
    }
}
