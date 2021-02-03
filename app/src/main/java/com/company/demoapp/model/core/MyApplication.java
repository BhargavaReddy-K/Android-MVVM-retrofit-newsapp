package com.company.demoapp.model.core;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.company.demoapp.model.font.FontsOverride;

public class MyApplication extends MultiDexApplication {

    private static final String FONT_TYPE = "SERIF";
    private static final String FONT_PATH = "fonts/SourceSansPro-Regular.ttf";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, FONT_TYPE, FONT_PATH);
    }

}
