package com.example.charge.api;

import android.app.Application;

import androidx.annotation.NonNull;

import com.example.charge.utils.LogUtils;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiHttpClient {

    // log tag
    private static final String TAG = ApiHttpClient.class.getName();

    private static final String API_URL = "https://api.objectspace.top/se/%s";
    private static final String HOST = "api.objectspace.top";
    private static final String POST_CONTENT_TYPE = "application/x-www-form-urlencoded";

    private static OkHttpClient CLIENT;

    public static void init(Application context) {
        // 公共请求头
        Headers headers = new Headers.Builder()
                .add("Accept-Language", Locale.getDefault().toString())
                .add("Host", HOST)
                .add("Connection", "Keep-Alive")
                .build();
        CLIENT = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                // 拦截器统一添加header
                .addInterceptor(new Interceptor() {
                    @NonNull
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .headers(headers)
                                .build();
                        return chain.proceed(request);
                    }
                })
                .build();
        log("api http client init.");
    }

    public static OkHttpClient getHttpClient() {
        return CLIENT;
    }

    public static void log(String log) {
        LogUtils.log(TAG, log);
    }

    public static String getAbsoluteApiUrl(String partUrl) {
        String url = partUrl;
        if (!partUrl.startsWith("http:") && !partUrl.startsWith("https:")) {
            url = String.format(API_URL, partUrl);
        }
        log("request: " + url);
        return url;
    }

    public static void get(String partUrl, Callback callback) {
        // no query parameter
        get(partUrl, null, callback);
    }

    public static void get(String partUrl, Map<String, String> params, Callback callback) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(getAbsoluteApiUrl(partUrl)).newBuilder();
        // add query parameters if any
        if (params != null) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                urlBuilder.addQueryParameter(param.getKey(), param.getValue());
            }
        }
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .get()
                .build();
        CLIENT.newCall(request).enqueue(callback);
        log("GET " + partUrl + "?" + params);
    }

    public static void post(String partUrl, Map<String, String> params, Callback callback) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                formBodyBuilder.add(param.getKey(), param.getValue());
            }
        }
        Request request = new Request.Builder()
                .url(getAbsoluteApiUrl(partUrl))
                .addHeader("Content-Type", POST_CONTENT_TYPE)
                .post(formBodyBuilder.build())
                .build();
        CLIENT.newCall(request).enqueue(callback);
        log("POST " + partUrl + "?" + params);
    }

    public static void post(String partUrl, RequestBody requestBody, Callback callback) {
        Request request = new Request.Builder()
                .url(getAbsoluteApiUrl(partUrl))
                .post(requestBody)
                .build();
        CLIENT.newCall(request).enqueue(callback);
        log("POST " + partUrl + "?" + requestBody);
    }


}
