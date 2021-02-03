package com.company.demoapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.company.demoapp.model.dto.NewsModel;
import com.company.demoapp.model.repository.NewsRepository;


public class NewsViewModel extends ViewModel {

    private MutableLiveData<NewsModel> mutableLiveDataList;
    private NewsRepository newsRepository;

    public void initialize(String query, String from) {
        newsRepository = NewsRepository.getInstance();
        mutableLiveDataList = newsRepository.getNews(query, from);
    }

    public LiveData<NewsModel> getNewsRepository() {
        return mutableLiveDataList;
    }

}
