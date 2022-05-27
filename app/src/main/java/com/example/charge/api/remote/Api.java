package com.example.charge.api.remote;

import android.net.Uri;

import com.example.charge.api.ApiHttpClient;
import com.example.charge.api.ApiUrlEnum;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Callback;

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
    /**
     * <string,string><string,file>
     */
    public static  void UPLOAD_ALBUM(Uri file_up, Callback callback){
        Map<String, String> params = new HashMap<>();
        params.put("file_up",file_up.toString());
        ApiHttpClient.post(ApiUrlEnum.UPLOAD_ALBUM.getUrl(),params,callback);
    }
}
