package com.example.charge.api.remote;

import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.example.charge.api.ApiHttpClient;
import com.example.charge.api.ApiUrlEnum;
import com.example.charge.utils.LogUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Api {


    public static void signup(String username, String password, String mail, String code,
                              Callback callback) {
        Map<String, String> params = new HashMap<>(4);
        params.put("username", username);
        params.put("password", password);
        params.put("mail", mail);
        params.put("code", code);
        ApiHttpClient.post(ApiUrlEnum.SIGNUP.getUrl(), params, callback);
    }

    public static void register(String username, String password, String mail, String code,
                              Callback callback) {
        signup(username, password, mail, code, callback);
    }

    public static void login(String username, String password, Callback callback) {
        Map<String, String> params = new HashMap<>(2);
        params.put("username", username);
        params.put("password", password);
        ApiHttpClient.post(ApiUrlEnum.LOGIN.getUrl(), params, callback);
    }

    public static void sendMail(String mail, int type, Callback callback) {
        Map<String, String> params = new HashMap<>(2);
        params.put("mail", mail);
        params.put("type", "" + type);
        ApiHttpClient.post(ApiUrlEnum.SEND_MAIL.getUrl(), params, callback);
    }

    public static void changePwd(String newPwd, String mail, String code,
                                 Callback callback) {
        Map<String, String> params = new HashMap<>(3);
        params.put("new_pwd", newPwd);
        params.put("mail", mail);
        params.put("code", code);
        ApiHttpClient.post(ApiUrlEnum.CHANGE_PWD.getUrl(), params, callback);
    }

    public static void changeMail(String oldMail, String newMail, String code,
                                 Callback callback) {
        Map<String, String> params = new HashMap<>(3);
        params.put("old_mail", oldMail);
        params.put("new_mail", newMail);
        params.put("code", code);
        ApiHttpClient.post(ApiUrlEnum.CHANGE_MAIL.getUrl(), params, callback);
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

        RequestBody fileBody = RequestBody.create(file, MediaType.parse(mimeType));

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file_up", filename, fileBody)
                .build();

        ApiHttpClient.post(ApiUrlEnum.UPLOAD_ALBUM.getUrl(), requestBody, callback);
    }
}
