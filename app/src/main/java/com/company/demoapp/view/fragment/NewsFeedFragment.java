package com.company.demoapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.demoapp.R;
import com.company.demoapp.model.adapter.NewsFeedAdapter;
import com.company.demoapp.model.dto.NewsArticleModel;
import com.company.demoapp.model.interfaces.IMessages;
import com.company.demoapp.model.sqlite.DataBaseLite;
import com.company.demoapp.model.interfaces.OnRecyclerViewItemClickListener;
import com.company.demoapp.model.utils.Messages;
import com.company.demoapp.view.activity.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

public class NewsFeedFragment extends Fragment {


    private RecyclerView newsDataRecyclerView;
    private DataBaseLite dataBaseLite;
    private IMessages messages;
    private LinearLayoutManager linearLayoutManager;
    private NewsFeedAdapter newsFeedAdapter;
    private final List<NewsArticleModel> newsArticleModelList = new ArrayList<>();
    private static final String SOURCE = "source";
    private String source;

    public NewsFeedFragment newInstance(String value) {
        NewsFeedFragment fragment = new NewsFeedFragment();
        Bundle args = new Bundle();
        args.putString(SOURCE, value);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            source = getArguments().getString(SOURCE);
        }
        dataBaseLite = new DataBaseLite(getActivity());
        messages = new Messages(getActivity());

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for news fragment
        return inflater.inflate(R.layout.fragment_news_feed, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {

            //views
            newsDataRecyclerView = view.findViewById(R.id.recycler_viewNews);

            //get Data
            newsArticleModelList.clear();
            newsArticleModelList.addAll(dataBaseLite.getNewsList(source));
            //obj declare
            linearLayoutManager = new LinearLayoutManager(getActivity());
            newsFeedAdapter = new NewsFeedAdapter(newsArticleModelList, getActivity());

            newsDataRecyclerView.setLayoutManager(linearLayoutManager);
            newsDataRecyclerView.setAdapter(newsFeedAdapter);
            newsFeedAdapter.notifyDataSetChanged();

            newsFeedAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<NewsFeedAdapter.NewsFeedViewHolder, NewsArticleModel>() {
                @Override
                public void onRecyclerViewItemClick( @NonNull View view, @NonNull NewsArticleModel newsArticleModel) {
                    switch (view.getId()) {
                        case R.id.image_viewShare:
                            messages.shareData(newsArticleModel.getUrl());
                            break;
                        default:
                            startActivity(new Intent(getActivity(), WebViewActivity.class).putExtra("url", newsArticleModel.getUrl()));
                            break;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
