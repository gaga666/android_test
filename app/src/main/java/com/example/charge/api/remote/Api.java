package com.example.charge.api.remote;

import android.webkit.MimeTypeMap;

import com.example.charge.api.ApiHttpClient;
import com.example.charge.api.enums.ApiUrlEnum;
import com.example.charge.utils.LogUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Api {

    public static final String HEADER_AUTHORIZATION = "Authorization";

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
                             Callback callback) {
        Map<String, String> params = new HashMap<>(2);
        params.put("username", username);
        params.put("password", password);
        ApiHttpClient.asyncPost(ApiUrlEnum.LOGIN.getUrl(), params, null, callback);
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

    public static void getUserInfoByUsername(String username, Callback callback) {
        Map<String, String> params = new HashMap<>(1);
        params.put("uname", username);
        ApiHttpClient.asyncGet(ApiUrlEnum.GET_USER_INFO.getUrl(), params, null, callback);
    }

    public static void getMyInfo(String accessToken, String tokenType, Callback callback) {
        Headers headers = new Headers.Builder()
                .add(HEADER_AUTHORIZATION, tokenType + " " + accessToken)
                .build();
        ApiHttpClient.asyncGet(ApiUrlEnum.GET_MY_INFO.getUrl(), headers, callback);
    }

    public static Response refreshTokenPair(String refreshToken, String tokenType) throws IOException {
        Headers headers = new Headers.Builder()
                .add(HEADER_AUTHORIZATION, tokenType + " " + refreshToken)
                .build();
        return ApiHttpClient.syncGet(ApiUrlEnum.REFRESH_TOKEN_PAIR.getUrl(), headers);
    }
}
