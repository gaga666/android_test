package com.example.charge.api.remote;

import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;

import com.example.charge.api.callback.ApiDataCallback;
import com.example.charge.api.ApiException;
import com.example.charge.api.ApiHttpClient;
import com.example.charge.api.enums.ApiUrlEnum;
import com.example.charge.api.enums.ResponseEnum;
import com.example.charge.api.utils.ResponseParser;
import com.example.charge.entity.DataResponse;
import com.example.charge.entity.TokenPairInfo;
import com.example.charge.entity.UserInfo;
import com.example.charge.utils.LogUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Api {

    private static final String LOG_TAG = Api.class.getName();

    public static void signup(String username, String password, String mail, String code,
                              Callback callback) {
        Map<String, String> params = new HashMap<>(4);
        params.put("username", username);
        params.put("password", password);
        params.put("mail", mail);
        params.put("code", code);
        ApiHttpClient.asyncPost(ApiUrlEnum.SIGNUP.getUrl(), params, null, callback);
    }

    public static void register(String username, String password, String mail, String code,
                              Callback callback) {
        signup(username, password, mail, code, callback);
    }

    public static void login(String username, String password,
                             ApiDataCallback<TokenPairInfo> apiDataCallback) {
        Map<String, String> params = new HashMap<>(2);
        params.put("username", username);
        params.put("password", password);
        ApiHttpClient.asyncPost(ApiUrlEnum.LOGIN.getUrl(), params, null, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LogUtils.e(LOG_TAG, "login.onFailure(): e -> " + e);
                apiDataCallback.onException(new ApiException(e));
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.code() != 200) {
                    String errLog = "login.onResponse(): response.code() == " + response.code();
                    LogUtils.e(LOG_TAG, errLog);
                    apiDataCallback.onException(new ApiException(errLog));
                    return;
                }
                // get response body
                ResponseBody responseBody = response.body();
                // empty response body
                if (responseBody == null) {
                    String errLog = "login.onResponse(): response.body() == null";
                    LogUtils.e(LOG_TAG, errLog);
                    apiDataCallback.onException(new ApiException(errLog));
                    return;
                }
                // Deserialize JSON string to object
                String json = responseBody.string();
                ObjectMapper mapper = new ObjectMapper();
                DataResponse<TokenPairInfo> res = mapper.readValue(json, new TypeReference<DataResponse<TokenPairInfo>>() {});
                if (res.getCode() == ResponseEnum.SUCCESS.getCode()) {
                    if (res.getData() != null) {
                        apiDataCallback.onSuccess(res.getData());
                    } else {
                        String errLog = "login.onResponse(): data == null";
                        LogUtils.e(LOG_TAG, errLog);
                        apiDataCallback.onException(new ApiException(errLog));
                    }
                } else {
                    apiDataCallback.onFailure(res.getCode(), res.getMessage());
                }
            }
        });
    }

    public static void sendMail(String mail, int type, Callback callback) {
        Map<String, String> params = new HashMap<>(2);
        params.put("mail", mail);
        params.put("type", "" + type);
        ApiHttpClient.asyncPost(ApiUrlEnum.SEND_MAIL.getUrl(), params, null, callback);
    }

    public static void changePwd(String newPwd, String mail, String code,
                                 Callback callback) {
        Map<String, String> params = new HashMap<>(3);
        params.put("new_pwd", newPwd);
        params.put("mail", mail);
        params.put("code", code);
        ApiHttpClient.asyncPost(ApiUrlEnum.CHANGE_PWD.getUrl(), params, null, callback);
    }

    public static void changeMail(String oldMail, String newMail, String code,
                                 Callback callback) {
        Map<String, String> params = new HashMap<>(3);
        params.put("old_mail", oldMail);
        params.put("new_mail", newMail);
        params.put("code", code);

        // TODO
//        Headers headers = new Headers.Builder()
//                .add("Authorization", )
        ApiHttpClient.asyncPost(ApiUrlEnum.CHANGE_MAIL.getUrl(), params, null, callback);
    }

    public static void uploadImage(File file, Callback callback) {
        // 文件名
        String filename = file.getName();
        int dotPos = filename.lastIndexOf(".");
        // 文件拓展名, eg: png
        String extension = filename.substring(dotPos + 1);
        // 媒体类型, eg: image/png
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

        // print the value of variable for debugging
        LogUtils.log("filename -> " + filename);
        LogUtils.log("extension -> " + extension);
        LogUtils.log("mimeType -> " + mimeType);

        RequestBody fileBody = MultipartBody.create(file, MediaType.parse(mimeType));

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file_up", filename, fileBody)
                .build();

        ApiHttpClient.asyncPost(ApiUrlEnum.UPLOAD_ALBUM.getUrl(), requestBody, null, callback);
    }

    public static void getUserInfoByUid(Long uid, Callback callback) {
        Map<String, String> params = new HashMap<>(1);
        params.put("uid", "" + uid);
        ApiHttpClient.asyncGet(ApiUrlEnum.GET_USER_INFO.getUrl(), params, null, callback);
    }

    public static void getUserInfoByUsername(String username, ApiDataCallback<UserInfo> apiDataCallback) {
        Map<String, String> params = new HashMap<>(1);
        params.put("uname", username);
        ApiHttpClient.asyncGet(ApiUrlEnum.GET_USER_INFO.getUrl(), params, null, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LogUtils.e(LOG_TAG, "getUserInfoByUsername.onFailure(): e -> " + e);
                apiDataCallback.onException(new ApiException(e));
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.code() != 200) {
                    String errLog = "getUserInfoByUsername.onResponse(): response.code() == " + response.code();
                    LogUtils.e(LOG_TAG, errLog);
                    apiDataCallback.onException(new ApiException(errLog));
                    return;
                }
                // get response body
                ResponseBody responseBody = response.body();
                // empty response body
                if (responseBody == null) {
                    String errLog = "getUserInfoByUsername.onResponse(): response.body() == null";
                    LogUtils.e(LOG_TAG, errLog);
                    apiDataCallback.onException(new ApiException(errLog));
                    return;
                }
                // Deserialize JSON string to object
                String json = responseBody.string();
                ObjectMapper mapper = new ObjectMapper();
                DataResponse<UserInfo> res =
                        mapper.readValue(json, new TypeReference<DataResponse<UserInfo>>() {});
                if (res.getCode() == ResponseEnum.SUCCESS.getCode()) {
                    if (res.getData() != null) {
                        apiDataCallback.onSuccess(res.getData());
                    } else {
                        String errLog = "getUserInfoByUsername.onResponse(): data == null";
                        LogUtils.e(LOG_TAG, errLog);
                        apiDataCallback.onException(new ApiException(errLog));
                    }
                } else {
                    apiDataCallback.onFailure(res.getCode(), res.getMessage());
                }
            }
        });
    }

    public static void getMyInfo(ApiDataCallback<UserInfo> apiDataCallback) {
        ApiHttpClient.asyncGet(ApiUrlEnum.GET_MY_INFO.getUrl(), null, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LogUtils.e(LOG_TAG, "getMyInfo.onFailure(): e -> " + e);
                apiDataCallback.onException(new ApiException(e));
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    DataResponse<UserInfo> res = ResponseParser.parseDataResponse(response);
                    if (res.getCode() == ResponseEnum.SUCCESS.getCode()) {
                        if (res.getData() != null) {
                            LogUtils.d(LOG_TAG, "data: " + res.getData());
                            LogUtils.d(LOG_TAG, "class: " + res.getData().getClass().getName());
                            apiDataCallback.onSuccess(res.getData());
                        } else {
                            apiDataCallback.onException(
                                    new ApiException("getMyInfo.onResponse(): data == null")
                            );
                        }
                    } else {
                        apiDataCallback.onFailure(res.getCode(), res.getMessage());
                    }
                } catch (ApiException e) {
                    apiDataCallback.onException(e);
                }
            }
        });
    }

    public static Response refreshTokenPair() throws IOException {
        return ApiHttpClient.syncGet(ApiUrlEnum.REFRESH_TOKEN_PAIR.getUrl(), null);
    }
}
