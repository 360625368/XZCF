package com.xzcf.data.data.socket;

/**
 * Description:
 * -
 * Author：chasen
 * Date： 2018/9/18 13:37
 */
public class SocketSub {

    private String qid;
    private String path;

    public SocketSub(String qid, String path) {
        this.qid = qid;
        this.path = path;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
