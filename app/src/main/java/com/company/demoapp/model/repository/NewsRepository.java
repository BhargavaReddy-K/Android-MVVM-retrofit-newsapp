package com.company.demoapp.model.repository;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.company.demoapp.model.core.Constants;
import com.company.demoapp.model.dto.NewsArticleModel;
import com.company.demoapp.model.dto.NewsModel;
import com.company.demoapp.model.retrofit.response.ResponseModelArray;
import com.company.demoapp.model.retrofit.server.RestAPICall;
import com.company.demoapp.model.retrofit.server.RetrofitClient;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepository {
    private static NewsRepository newsRepository;
    private final RestAPICall restApi;
    private HashMap<String, String> hashMap;
    private final String API_KEY = "apiKey";
    private final String QUERY = "q";
    private final String FROM = "from";
    private final String SORT_BY = "sortBy";


    public static NewsRepository getInstance() {
        if (newsRepository == null) {
            newsRepository = new NewsRepository();
        }
        return newsRepository;
    }

    private NewsRepository() {
        restApi = RetrofitClient.getClient(Constants.BASE_URL).create(RestAPICall.class);
    }

    public MutableLiveData<NewsModel> getNews(String query, String from) {
        hashMap = new HashMap<>();
        hashMap.put(API_KEY, Constants.API_KEY_VALUE);
        hashMap.put(QUERY, query);
        hashMap.put(SORT_BY, Constants.SORT_BY_VALUE);
        hashMap.put(FROM, from);

        final MutableLiveData<NewsModel> mutableLiveData = new MutableLiveData<>();
        final NewsModel newsModel = new NewsModel();
        restApi.getNewsDetailsApi(hashMap).enqueue(new Callback<ResponseModelArray<NewsArticleModel>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModelArray<NewsArticleModel>> call, @NonNull Response<ResponseModelArray<NewsArticleModel>> response) {
                try {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        if (response.body().getStatus().equalsIgnoreCase("ok")) {
                            try {
                                List<NewsArticleModel> newsArticleModelList = response.body().getData();
                                newsModel.setInfo(response.body().getStatus());
                                newsModel.setNewsArticleModelList(newsArticleModelList);
                                mutableLiveData.setValue(newsModel);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            newsModel.setInfo(response.body().getMessage());
                            newsModel.setNewsArticleModelList(null);
                            mutableLiveData.setValue(newsModel);
                        }
                    } else {
                        newsModel.setInfo(response.message());
                        newsModel.setNewsArticleModelList(null);
                        mutableLiveData.setValue(newsModel);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModelArray<NewsArticleModel>> call, @NonNull Throwable t) {
                try {
                    newsModel.setInfo(t.getMessage());
                    newsModel.setNewsArticleModelList(null);
                    mutableLiveData.setValue(newsModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return mutableLiveData;
    }


}
