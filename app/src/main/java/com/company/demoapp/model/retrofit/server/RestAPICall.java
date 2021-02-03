package com.company.demoapp.model.retrofit.server;


import com.company.demoapp.model.dto.NewsArticleModel;
import com.company.demoapp.model.retrofit.response.ResponseModelArray;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface RestAPICall {

    @GET("v2/everything")
    Call<ResponseModelArray<NewsArticleModel>> getNewsDetailsApi(@QueryMap Map<String, String> queryMap);

}
