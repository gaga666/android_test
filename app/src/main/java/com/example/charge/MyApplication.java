package com.example.charge;

import android.app.Application;

import com.example.charge.api.ApiHttpClient;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    private void init() {
        // 初始化网络请求
        ApiHttpClient.init(this);
    }
}
