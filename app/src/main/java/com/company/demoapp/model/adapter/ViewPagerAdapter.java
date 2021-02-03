package com.company.demoapp.model.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.company.demoapp.view.fragment.NewsFeedFragment;

import java.util.ArrayList;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private final int numOfTabs;
    private final ArrayList<String> tabTitle;

    public ViewPagerAdapter(FragmentManager fm, int numOfTabs, ArrayList<String> tabTitle) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.tabTitle = tabTitle;
    }

    @Override
    public Fragment getItem(int position) {
        return new NewsFeedFragment().newInstance(tabTitle.get(position));
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}