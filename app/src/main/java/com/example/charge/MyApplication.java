package com.example.charge;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.charge.api.ApiHttpClient;
import com.example.charge.common.Constants;

public class MyApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();

        init();
    }

    private void init() {
        // 初始化网络请求
        ApiHttpClient.init(this);
        // 初始化 Token 管理器
        TokenManager.getInstance().init(this);
    }

    public static Context getContext() {
        return sContext;
    }
}
