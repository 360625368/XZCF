package com.xzcf.data.data.response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.List;

public class NewsResponse {

    private String code;
    private String msg;
    private List<Rss> rssTouTiao;
    private List<Rss> rssZhiBo;
    private List<Rss> rssYiDong;
    private List<Rss> rssYaoWen;
    private List<Rss> rssTuiJian;
    private List<Rss> rssShiKuang;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Rss> getRssTouTiao() {
        return rssTouTiao;
    }

    public void setRssTouTiao(List<Rss> rssTouTiao) {
        this.rssTouTiao = rssTouTiao;
    }

    public List<Rss> getRssZhiBo() {
        return rssZhiBo;
    }

    public void setRssZhiBo(List<Rss> rssZhiBo) {
        this.rssZhiBo = rssZhiBo;
    }

    public List<Rss> getRssYiDong() {
        return rssYiDong;
    }

    public void setRssYiDong(List<Rss> rssYiDong) {
        this.rssYiDong = rssYiDong;
    }

    public List<Rss> getRssYaoWen() {
        return rssYaoWen;
    }

    public void setRssYaoWen(List<Rss> rssYaoWen) {
        this.rssYaoWen = rssYaoWen;
    }

    public List<Rss> getRssTuiJian() {
        return rssTuiJian;
    }

    public void setRssTuiJian(List<Rss> rssTuiJian) {
        this.rssTuiJian = rssTuiJian;
    }

    public List<Rss> getRssShiKuang() {
        return rssShiKuang;
    }

    public void setRssShiKuang(List<Rss> rssShiKuang) {
        this.rssShiKuang = rssShiKuang;
    }

    public static class Rss implements Serializable {

        private static final long serialVersionUID = 5214875059411260121L;
        private String title;
        private String link;
        private String description;
        private String pubDate;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPubDate() {
            return pubDate;
        }

        public void setPubDate(String pubDate) {
            this.pubDate = pubDate;
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this, new TypeToken<NewsResponse>(){}.getType());
    }
}
