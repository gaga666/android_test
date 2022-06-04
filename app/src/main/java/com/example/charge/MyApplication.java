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
    private static String sTokenType;
    private static String sAccessToken;
    private static String sRefreshToken;


    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    private void init() {
        // 初始化网络请求
        ApiHttpClient.init(this);
        sContext = getApplicationContext();
        // 获取本地保存的 Token 信息
        SharedPreferences sp = getSharedPreferences(Constants.SP_NAME_TOKEN_PAIR, MODE_PRIVATE);
        sTokenType = sp.getString(Constants.KEY_TOKEN_TYPE, null);
        sAccessToken = sp.getString(Constants.KEY_ACCESS_TOKEN, null);
        sRefreshToken = sp.getString(Constants.KEY_REFRESH_TOKEN, null);
    }

    public static Context getContext() {
        return sContext;
    }

    public static String getTokenType() {
        return sTokenType;
    }

    public static String getAccessToken() {
        return sAccessToken;
    }

    public static String getRefreshToken() {
        return sRefreshToken;
    }
}
