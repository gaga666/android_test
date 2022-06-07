package com.example.charge.api;

import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;

import com.example.charge.TokenManager;
import com.example.charge.api.exception.ApiException;
import com.example.charge.api.callback.ApiCallback;
import com.example.charge.api.callback.ApiDataCallback;
import com.example.charge.api.enums.ApiUrlEnum;
import com.example.charge.api.enums.ResponseEnum;
import com.example.charge.api.model.DataResponse;
import com.example.charge.api.model.MessageResponse;
import com.example.charge.api.model.dto.ImageInfo;
import com.example.charge.api.model.dto.TokenPairInfo;
import com.example.charge.api.model.dto.UserInfo;
import com.example.charge.api.utils.ResponseParser;
import com.example.charge.utils.LogUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Api {

    private static final String LOG_TAG = Api.class.getName();

    public static void signup(String username, String password, String mail, String code,
                              ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>(4);
        params.put("username", username);
        params.put("password", password);
        params.put("mail", mail);
        params.put("code", code);
        ApiHttpClient.asyncPost(ApiUrlEnum.SIGNUP.getUrl(), params, null, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                apiCallback.onException(new ApiException(e));
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    MessageResponse res = ResponseParser.parseMsgResponse(response);
                    if (res.getCode() == ResponseEnum.SUCCESS.getCode()) {
                        apiCallback.onSuccess();
                    } else {
                        apiCallback.onFailure(res.getCode(), res.getMessage());
                    }
                } catch (ApiException e) {
                    apiCallback.onException(e);
                }
            }
        });
    }

    public static void register(String username, String password, String mail, String code,
                                ApiCallback apiCallback) {
        signup(username, password, mail, code, apiCallback);
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
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    // Deserialize response JSON value
                    DataResponse<TokenPairInfo> res = ResponseParser.parseDataResponse(response, TokenPairInfo.class);
                    if (res.getCode() == ResponseEnum.SUCCESS.getCode()) {
                        if (res.getData() != null) {
                            apiDataCallback.onSuccess(res.getData());
                        } else {
                            apiDataCallback.onException(new ApiException("data == null"));
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

    public static void logout(ApiCallback apiCallback) {
        ApiHttpClient.asyncPost(ApiUrlEnum.LOGOUT.getUrl(), RequestBody.create("", null), null, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                LogUtils.e(LOG_TAG, "logout.onFailure(): e -> " + e);
                apiCallback.onException(new ApiException(e));
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    // Deserialize response JSON value
                    MessageResponse res = ResponseParser.parseMsgResponse(response);
                    if (res.getCode() == ResponseEnum.SUCCESS.getCode()) {
                        apiCallback.onSuccess();
                    } else {
                        apiCallback.onFailure(res.getCode(), res.getMessage());
                    }
                } catch (ApiException e) {
                    apiCallback.onException(e);
                }
            }
        });
    }

    public static void sendMail(String mail, int type, ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>(2);
        params.put("mail", mail);
        params.put("type", "" + type);
        ApiHttpClient.asyncPost(ApiUrlEnum.SEND_MAIL.getUrl(), params, null, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                apiCallback.onException(new ApiException(e));
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    // Deserialize response JSON value
                    MessageResponse res = ResponseParser.parseMsgResponse(response);
                    if (res.getCode() == ResponseEnum.SUCCESS.getCode()) {
                        apiCallback.onSuccess();
                    } else {
                        apiCallback.onFailure(res.getCode(), res.getMessage());
                    }
                } catch (ApiException e) {
                    apiCallback.onException(e);
                }
            }
        });
    }

    public static void changePwd(String newPwd, String mail, String code,
                                 ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>(3);
        params.put("new_pwd", newPwd);
        params.put("mail", mail);
        params.put("code", code);
        ApiHttpClient.asyncPost(ApiUrlEnum.CHANGE_PWD.getUrl(), params, null, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                apiCallback.onException(new ApiException(e));
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    // Deserialize response JSON value
                    MessageResponse res = ResponseParser.parseMsgResponse(response);
                    if (res.getCode() == ResponseEnum.SUCCESS.getCode()) {
                        apiCallback.onSuccess();
                    } else {
                        apiCallback.onFailure(res.getCode(), res.getMessage());
                    }
                } catch (ApiException e) {
                    apiCallback.onException(e);
                }
            }
        });
    }

    public static void changeMail(String oldMail, String newMail, String code,
                                  ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>(3);
        params.put("old_mail", oldMail);
        params.put("new_mail", newMail);
        params.put("code", code);

        ApiHttpClient.asyncPost(ApiUrlEnum.CHANGE_MAIL.getUrl(), params, null, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                apiCallback.onException(new ApiException(e));
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    // Deserialize response JSON value
                    MessageResponse res = ResponseParser.parseMsgResponse(response);
                    if (res.getCode() == ResponseEnum.SUCCESS.getCode()) {
                        apiCallback.onSuccess();
                    } else {
                        apiCallback.onFailure(res.getCode(), res.getMessage());
                    }
                } catch (ApiException e) {
                    apiCallback.onException(e);
                }
            }
        });
    }

    public static void changeAvatar(File file, ApiDataCallback<ImageInfo> apiDataCallback) {
        // 文件名
        String filename = file.getName();
        int dotPos = filename.lastIndexOf(".");
        // 文件拓展名, eg: png
        String extension = filename.substring(dotPos + 1);
        // 媒体类型, eg: image/png
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

        RequestBody fileBody = MultipartBody.create(file, MediaType.parse(mimeType));

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("avatar", filename, fileBody)
                .build();

        ApiHttpClient.asyncPost(ApiUrlEnum.CHANGE_AVATAR.getUrl(), requestBody, null, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                apiDataCallback.onException(new ApiException(e));
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    // Deserialize response JSON value
                    DataResponse<ImageInfo> res = ResponseParser.parseDataResponse(response, ImageInfo.class);
                    if (res.getCode() == ResponseEnum.SUCCESS.getCode()) {
                        if (res.getData() != null) {
                            apiDataCallback.onSuccess(res.getData());
                        } else {
                            apiDataCallback.onException(new ApiException("data == null"));
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

    public static void uploadImage(File file, ApiDataCallback<ImageInfo> apiDataCallback) {
        // 文件名
        String filename = file.getName();
        int dotPos = filename.lastIndexOf(".");
        // 文件拓展名, eg: png
        String extension = filename.substring(dotPos + 1);
        // 媒体类型, eg: image/png
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
//
//        // print the value of variable for debugging
//        LogUtils.log("filename -> " + filename);
//        LogUtils.log("extension -> " + extension);
//        LogUtils.log("mimeType -> " + mimeType);

        RequestBody fileBody = MultipartBody.create(file, MediaType.parse(mimeType));

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file_up", filename, fileBody)
                .build();

        ApiHttpClient.asyncPost(ApiUrlEnum.UPLOAD_ALBUM.getUrl(), requestBody, null, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                apiDataCallback.onException(new ApiException(e));
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    // Deserialize response JSON value
                    DataResponse<ImageInfo> res = ResponseParser.parseDataResponse(response, ImageInfo.class);
                    if (res.getCode() == ResponseEnum.SUCCESS.getCode()) {
                        if (res.getData() != null) {
                            apiDataCallback.onSuccess(res.getData());
                        } else {
                            apiDataCallback.onException(new ApiException("data == null"));
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

    private static void getUserInfo(Map<String, String> params, ApiDataCallback<UserInfo> apiDataCallback) {
        ApiHttpClient.asyncGet(ApiUrlEnum.GET_USER_INFO.getUrl(), params, null, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                apiDataCallback.onException(new ApiException(e));
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    // Deserialize response JSON value
                    DataResponse<UserInfo> res = ResponseParser.parseDataResponse(response, UserInfo.class);
                    if (res.getCode() == ResponseEnum.SUCCESS.getCode()) {
                        if (res.getData() != null) {
                            apiDataCallback.onSuccess(res.getData());
                        } else {
                            apiDataCallback.onException(new ApiException("data == null"));
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

    public static void getUserInfoByUid(Long uid, ApiDataCallback<UserInfo> apiDataCallback) {
        Map<String, String> params = new HashMap<>(1);
        params.put("uid", "" + uid);
        getUserInfo(params, apiDataCallback);
    }

    public static void getUserInfoByUsername(String username, ApiDataCallback<UserInfo> apiDataCallback) {
        Map<String, String> params = new HashMap<>(1);
        params.put("uname", username);
        getUserInfo(params, apiDataCallback);
    }

    public static void getMyInfo(ApiDataCallback<UserInfo> apiDataCallback) {
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Authorization", TokenManager.getInstance().getTokenType()
                                        + " " + TokenManager.getInstance().getAccessToken());

        ApiHttpClient.asyncGet(ApiUrlEnum.GET_MY_INFO.getUrl(), headers, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                apiDataCallback.onException(new ApiException(e));
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    // Deserialize response JSON value
                    DataResponse<UserInfo> res = ResponseParser.parseDataResponse(response, UserInfo.class);
                    if (res.getCode() == ResponseEnum.SUCCESS.getCode()) {
                        if (res.getData() != null) {
                            apiDataCallback.onSuccess(res.getData());
                        } else {
                            apiDataCallback.onException(
                                new ApiException("data == null")
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

    public static DataResponse<TokenPairInfo> refreshTokenPair() throws IOException {
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Authorization", TokenManager.getInstance().getTokenType() + " " + TokenManager.getInstance().getRefreshToken());
        Response response = ApiHttpClient.syncGet(ApiUrlEnum.REFRESH_TOKEN_PAIR.getUrl(), headers);
        return ResponseParser.parseDataResponse(response, TokenPairInfo.class);
    }
}
