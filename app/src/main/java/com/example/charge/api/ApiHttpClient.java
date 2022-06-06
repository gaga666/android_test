package com.example.charge.api;

import android.app.Application;

import com.example.charge.api.interceptor.RequestHeadersInterceptor;
import com.example.charge.api.interceptor.TokenInterceptor;
import com.example.charge.utils.LogUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiHttpClient {

    // log tag
    private static final String TAG = ApiHttpClient.class.getName();

    private static final String API_URL = "https://api.objectspace.top/se/%s";

    private static OkHttpClient CLIENT;

    public static void init(Application context) {
        CLIENT = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                // 拦截器统一添加header
                .addInterceptor(new RequestHeadersInterceptor())
                // 拦截器判断 Token 是否过期或失效并刷新
                .addInterceptor(new TokenInterceptor())
                .build();
        log("API client init.");
    }

    public static void log(String log) {
        LogUtils.log(TAG, log);
    }

    /**
     * 获取完整的 API 请求地址
     *
     * @param partUrl 部分地址
     * @return 完整地址
     */
    public static String getAbsoluteApiUrl(String partUrl) {
        String url = partUrl;
        if (!partUrl.startsWith("http:") && !partUrl.startsWith("https:")) {
            url = String.format(API_URL, partUrl);
        }
        log("request: " + url);
        return url;
    }

    /**
     * 构造 POST 请求的表单 (application/x-www-form-urlencoded)
     *
     * @param params the form params
     * @return a new {@link RequestBody} instance
     */
    public static RequestBody buildFormBody(Map<String, String> params) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                formBodyBuilder.add(param.getKey(), param.getValue());
            }
        }
        return formBodyBuilder.build();
    }

    /**
     * 构造 GET 请求的 {@link Call} 实例
     *
     * @param url the request url
     * @param params (optional) query parameters
     * @param headers (optional) the request headers
     * @return a new {@link Call} instance
     */
    public static Call buildGetCall(String url, Map<String, String> params, Map<String, String> headers) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        // add query parameters if any
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        Request.Builder builder = new Request.Builder();
        // add headers if any
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = builder
                .url(urlBuilder.build())
                // method GET
                .get()
                .build();
        return CLIENT.newCall(request);
    }

    /**
     * 构造 POST 请求的 {@link Call} 实例
     *
     * @param url the request url
     * @param requestBody the request body
     * @param headers (optional) the request headers
     * @return a new {@link Call} instance
     */
    public static Call buildPostCall(String url, RequestBody requestBody, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder();
        // add headers if any
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = builder
                .url(url)
                // method POST
                .post(requestBody)
                .build();
        return CLIENT.newCall(request);
    }

    /**
     * 同步(Synchronous)请求
     *
     * @param call the request that has been prepared for execution
     * @return the response of the request
     * @throws IOException if the request could not be executed due to cancellation, a connectivity
     *     problem or timeout.
     */
    public static Response syncRequest(Call call) throws IOException {
        return call.execute();
    }

    /**
     * 异步(Asynchronous)请求
     *
     * @param call the request that has been prepared for execution
     * @param callback the response callback of the request
     */
    public static void asyncRequest(Call call, Callback callback) {
        call.enqueue(callback);
    }

    /**
     * 无参同步(Synchronous) GET 请求
     */
    public static Response syncGet(String partUrl, Map<String, String> headers) throws IOException {
        // no query parameter
        return syncGet(partUrl, null, headers);
    }

    /**
     * 带参同步(Synchronous) GET 请求
     *
     * @param partUrl the part of the request url
     * @param params the query parameters of the request
     * @param headers (optional) the request headers
     * @return the response of the request
     * @throws IOException
     */
    public static Response syncGet(String partUrl, Map<String, String> params, Map<String, String> headers) throws IOException {
        Call call = buildGetCall(getAbsoluteApiUrl(partUrl), params, headers);
        return syncRequest(call);
    }

    /**
     * 无参异步(Asynchronous) GET 请求
     */
    public static void asyncGet(String partUrl, Map<String, String> headers, Callback callback) {
        // no query parameter
        asyncGet(partUrl, null, headers, callback);
    }

    /**
     * 带参异步(Asynchronous) GET 请求
     *
     * @param partUrl the part of the request url
     * @param params the query parameters of the request
     * @param headers (optional) the request headers
     * @param callback the response callback of the request
     */
    public static void asyncGet(String partUrl, Map<String, String> params, Map<String, String> headers,
                                Callback callback) {
        Call call = buildGetCall(getAbsoluteApiUrl(partUrl), params, headers);
        log("GET " + partUrl + ((params == null) ? "" : ("?" + params)));
        asyncRequest(call, callback);
    }

    /**
     * 同步(Synchronous) POST 请求
     *
     * @param partUrl the part of the request url
     * @param params the form data (key-value pairs)
     * @param headers (optional) the request headers
     * @return the response of the request
     * @throws IOException
     */
    public static Response syncPost(String partUrl, Map<String, String> params, Map<String, String> headers)
            throws IOException {
        return syncPost(partUrl, buildFormBody(params), headers);
    }

    /**
     * 同步(Synchronous) POST 请求
     *
     * @param partUrl the part of the request url
     * @param requestBody the request body
     * @param headers (optional) the request headers
     * @return the response of the request
     * @throws IOException
     */
    public static Response syncPost(String partUrl, RequestBody requestBody, Map<String, String> headers)
            throws IOException {
        Call call = buildPostCall(getAbsoluteApiUrl(partUrl), requestBody, headers);
        log("POST " + partUrl + "?" + requestBody);
        return syncRequest(call);
    }

    /**
     * 异步(Asynchronous) POST 请求
     *
     * @param partUrl the part of the request url
     * @param params the form data (key-value pairs)
     * @param headers (optional) the request headers
     * @param callback the response callback of the request
     */
    public static void asyncPost(String partUrl, Map<String, String> params, Map<String, String> headers,
                                 Callback callback) {
        asyncPost(partUrl, buildFormBody(params), headers, callback);
    }

    /**
     * 异步 POST 请求
     *
     * @param partUrl the part of the request url
     * @param requestBody the request body
     * @param headers (optional) the request headers
     * @param callback the response callback of the request
     */
    public static void asyncPost(String partUrl, RequestBody requestBody, Map<String, String> headers,
                                 Callback callback) {
        Call call = buildPostCall(getAbsoluteApiUrl(partUrl), requestBody, headers);
        log("POST " + partUrl + "?" + requestBody);
        asyncRequest(call, callback);
    }
}
