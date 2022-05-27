package com.example.charge.signup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.charge.R;
import com.example.charge.api.remote.Api;
import com.example.charge.e_mail;
import com.example.charge.entity.MessageResponse;
import com.example.charge.login.MainActivity;
import com.example.charge.utils.LogUtils;
import com.example.charge.view.LoadingDialog;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Register extends AppCompatActivity {
    private static final String TAG = Register.class.getName();
    EditText re_username, re_password, re_passwordSecond, re_mail, re_verify;
    LoadingDialog mLoadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button re_register = (Button) findViewById(R.id.re_register);
        re_username = (EditText) findViewById(R.id.re_username);
        re_password = (EditText) findViewById(R.id.re_password);
        re_passwordSecond = (EditText) findViewById(R.id.re_passwordSecond);
        re_mail = (EditText) findViewById(R.id.re_mail);
        re_verify = (EditText) findViewById(R.id.re_verify);
        re_register.setOnClickListener(view -> setUser());
        Button re_getVerify = findViewById(R.id.re_getVerify);
        re_getVerify.setOnClickListener(view -> re_send(re_mail.getText().toString()));
    }

    /**
     * 添加用户
     */
    public void setUser() {
//        FormBody formBody = new FormBody.Builder().add("username", re_username.getText().toString())
//                .add("password", re_password.getText().toString())
//                .add("mail", re_mail.getText().toString())
//                .add("code", re_verify.getText().toString()).build();
//        Request request = new Request.Builder().url("https://api.objectspace.top/se/auth/reg").post(formBody).build();
//        OkHttpClient mOkhttpClient = new OkHttpClient();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Response response = mOkhttpClient.newCall(request).execute();
//                    String json = response.body().string();
//                    ObjectMapper mapper = new ObjectMapper();
//                    MessageResponse res = mapper.readValue(json, MessageResponse.class);
//                    String result = res.toString();
//                    switch (result) {
//                        case "0":
//                            Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
//                            Intent i = new Intent(Register.this, MainActivity.class);
//                            startActivity(i);
//                            overridePendingTransition(0, 0);
//                            break;
//                        case "-400":
//                            Toast.makeText(Register.this, "请求错误（参数异常）", Toast.LENGTH_SHORT).show();
//                            break;
//                        case "-405":
//                            Toast.makeText(Register.this, "请求方法错误", Toast.LENGTH_SHORT).show();
//                            break;
//                        case "-500":
//                            Toast.makeText(Register.this, "注册失败（服务端异常）", Toast.LENGTH_SHORT).show();
//                            break;
//                        case "1001":
//                            Toast.makeText(Register.this, "邮箱已使用", Toast.LENGTH_SHORT).show();
//                            break;
//                        case "1003":
//                            Toast.makeText(Register.this, "用户名已使用", Toast.LENGTH_SHORT).show();
//                            break;
//                        case "1005":
//                            Toast.makeText(Register.this, "验证码无效", Toast.LENGTH_SHORT).show();
//                            break;
//                        case "1007":
//                            Toast.makeText(Register.this, "验证码过期", Toast.LENGTH_SHORT).show();
//                            break;
//                        default:
//                            Toast.makeText(Register.this, "修bug", Toast.LENGTH_SHORT).show();
//                            break;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
        String username = re_username.getText().toString();
        String password = re_password.getText().toString();
        String mail = re_mail.getText().toString();
        String code = re_verify.getText().toString();
        LogUtils.i(TAG, String.format("username -> %s, password -> %s, mail -> %s, code -> %s", username, password,mail,code));
        // show loading dialog
        showLoading();

        Api.register(username, password, mail,code,new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // destroy loading dialog
                        stopLoading();
                    }
                });
                LogUtils.e(TAG, "register().onFailure: exception -> " + e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // destroy loading dialog
                        stopLoading();
                    }
                });
                if (response.body() != null) {
                    String json = response.body().string();
                    ObjectMapper mapper = new ObjectMapper();
                    MessageResponse res = mapper.readValue(json, MessageResponse.class);
                    int code = res.getCode();
                    String message = res.getMessage();
                    LogUtils.i(TAG,
                            String.format("register().onResponse: code -> %s, message -> %s", code, message)
                    );
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Register.this, message, Toast.LENGTH_SHORT).show();
                            if (code == 0) {
                                Intent i = new Intent(Register.this, MainActivity.class);
                                startActivity(i);
                                overridePendingTransition(0, 0);
                            }
                        }
                    });

                } else {
                    LogUtils.e(TAG, "register().onResponse: response.body() == null");
                }

            }
        });
    }

    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
            mLoadingDialog.setText("注册中...");
            mLoadingDialog.setCancelable(false);
            mLoadingDialog.show();
        }
    }

    public void stopLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    public void re_send(String to) {
        if(!e_mail.isValidEmail(to)){
            Toast.makeText(this, "请输入有效邮箱", Toast.LENGTH_SHORT).show();
            return;
        }
        Api.sendMail(to,11,new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // destroy loading dialog
                        stopLoading();
                    }
                });
                LogUtils.e(TAG, "sendMail11().onFailure: exception -> " + e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // destroy loading dialog
                        stopLoading();
                    }
                });
                if (response.body() != null) {
                    String json = response.body().string();
                    ObjectMapper mapper = new ObjectMapper();
                    MessageResponse res = mapper.readValue(json, MessageResponse.class);
                    int code = res.getCode();
                    String message = res.getMessage();
                    LogUtils.i(TAG,
                            String.format("sendMail11().onResponse: code -> %s, message -> %s", code, message)
                    );
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Register.this, message, Toast.LENGTH_SHORT).show();
                            if (code == 0) {

                            }
                        }
                    });

                } else {
                    LogUtils.e(TAG, "sendMail11().onResponse: response.body() == null");
                }

            }
        });
    }
}
