package com.company.demoapp.view.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;

import com.company.demoapp.R;
import com.company.demoapp.model.adapter.ViewPagerAdapter;
import com.company.demoapp.model.dto.NewsArticleModel;
import com.company.demoapp.model.dto.NewsModel;
import com.company.demoapp.model.sqlite.DataBaseLite;
import com.company.demoapp.model.interfaces.AlertOnClick;
import com.company.demoapp.model.utils.CheckInternet;
import com.company.demoapp.model.interfaces.IMessages;
import com.company.demoapp.model.utils.Messages;
import com.company.demoapp.viewmodel.NewsViewModel;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener, AlertOnClick {

    private TabLayout tabLayout;
    private ViewPager tabsViewPager;
    private ImageView refreshImageView;
    private IMessages messages;
    private DataBaseLite dataBaseLite;
    private CheckInternet checkInternet;
    private NewsViewModel newsViewModel;
    private ViewPagerAdapter viewPagerAdapter;
    private final CharSequence[] choices = {"Google", "Tesla", "Apple"};
    private final ArrayList<String> tabTitleList = new ArrayList<>();
    private final String query = "Tesla";
    private long backPressed;
    private static final int TIME_INTERVAL = 2000;
    private final String dateFormat = "yyyy-MM-dd";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        try {

            //views
            tabLayout = findViewById(R.id.tabLayout);
            tabsViewPager = findViewById(R.id.view_pagerTabs);
            refreshImageView = findViewById(R.id.image_viewRefresh);

            //obj declare
            messages = new Messages(this, this);
            dataBaseLite = new DataBaseLite(this);
            newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);

            //on click
            refreshImageView.setOnClickListener(this);

            getNewsData(query);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onResume() {
        super.onResume();
    }

    private void getNewsData(String query) {
        try {
            checkInternet = new CheckInternet(this);
            if (checkInternet.isInternetPresent()) {
                //initialize view model
                newsViewModel.initialize(query, messages.getCurrentDate(dateFormat));

                messages.showProgressDialog(getResources().getString(R.string.loading));
                newsViewModel.getNewsRepository().observe(this, new Observer<NewsModel>() {
                    @Override
                    public void onChanged(@Nullable NewsModel newsModel) {
                        try {
                            messages.hideDialog();
                            assert newsModel != null;
                            if (newsModel.getInfo().equalsIgnoreCase(getResources().getString(R.string.ok))) {
                                manageResponse(newsModel.getNewsArticleModelList());
                            } else {
                                messages.toast(newsModel.getInfo());
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                });

            } else {
                messages.toastInternet();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            finishAffinity();
            return;
        } else {
            messages.toast(getString(R.string.exit_info));
        }
        backPressed = System.currentTimeMillis();
    }

    private void manageResponse(List<NewsArticleModel> newsArticleModelList) {
        try {
            if (newsArticleModelList.size() > 0) {
                //insert data
                dataBaseLite.truncateNews();
                dataBaseLite.insertNewsData(newsArticleModelList);
                //get Sources
                tabTitleList.clear();
                tabTitleList.addAll(dataBaseLite.getSourceList());
                //add tabs
                tabLayout.removeAllTabs();
                for (String tab : tabTitleList) {
                    tabLayout.addTab(tabLayout.newTab().setText(tab));
                }
                viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), tabTitleList);
                tabsViewPager.setAdapter(viewPagerAdapter);
                tabsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        tabsViewPager.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                    }

                });

            } else {
                messages.toast(getResources().getString(R.string.no_data));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_viewRefresh:
                try {
                    messages.getChoice(choices);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    @Override
    public void alertOnClick(String choice) {
        try {
            getNewsData(choice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
