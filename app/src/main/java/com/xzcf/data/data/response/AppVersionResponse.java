package com.xzcf.data.data.response;

public class AppVersionResponse extends BaseRes {

    /**
     * version : 1.1.0
     * describe : 最新APP，赶紧下载哦
     * fileUrl : /down/aa.apk
     * fileSize : 20340
     */

    private String version;
    private String describe;
    private String fileUrl;
    private String fileSize;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
}
