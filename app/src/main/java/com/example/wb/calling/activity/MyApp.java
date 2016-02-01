package com.example.wb.calling.activity;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * Created by wb on 16/1/29.
 */
public class MyApp extends Application {
    public static final String APPID = "18e3c5746a84e4abcebb8ec8f5b96a0a";
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(getApplicationContext(),APPID);
    }
}
