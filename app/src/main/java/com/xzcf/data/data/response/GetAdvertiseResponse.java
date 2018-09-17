package com.xzcf.data.data.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAdvertiseResponse extends BaseRes {
    @SerializedName("advInfos")
    private List<AdvInfosBean> advInfos;

    public List<AdvInfosBean> getAdvInfos() {
        return advInfos;
    }

    public void setAdvInfos(List<AdvInfosBean> advInfos) {
        this.advInfos = advInfos;
    }

    public static class AdvInfosBean {
        @SerializedName("imgUrl")
        private String imgUrl;
        @SerializedName("advUrl")
        private String advUrl;
        @SerializedName("id")
        private String id;
        @SerializedName("title")
        private String title;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getAdvUrl() {
            return advUrl;
        }

        public void setAdvUrl(String advUrl) {
            this.advUrl = advUrl;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
