package com.company.demoapp.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsModel {

    @SerializedName("articles")
    @Expose
    private List<NewsArticleModel> newsArticleModelList;
    @SerializedName("info")
    @Expose
    private String info;

    public List<NewsArticleModel> getNewsArticleModelList() {
        return newsArticleModelList;
    }

    public void setNewsArticleModelList(List<NewsArticleModel> newsArticleModelList) {
        this.newsArticleModelList = newsArticleModelList;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
