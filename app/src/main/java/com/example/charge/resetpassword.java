package com.example.charge;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.charge.api.remote.Api;
import com.example.charge.entity.MessageResponse;
import com.example.charge.login.LoginActivity;
import com.example.charge.utils.LogUtils;
import com.example.charge.view.LoadingDialog;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class resetpassword extends AppCompatActivity {
    private static final String TAG = resetpassword.class.getName();
    EditText reset_username;
    EditText reset_password;
    EditText reset_mail;
    EditText reset_verify;
    LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);
        reset_password =  findViewById(R.id.reset_password);
        reset_username =  findViewById(R.id.reset_username);
        reset_verify =  findViewById(R.id.reset_verify);
        reset_mail =  findViewById(R.id.reset_mail);
        Button reset_getVerify =  findViewById(R.id.reset_getVerify);
        reset_getVerify.setOnClickListener(view -> send(reset_mail.getText().toString()));
        Button reset_reset =  findViewById(R.id.reset_reset);
        reset_reset.setOnClickListener(view -> update(reset_username.getText().toString(), reset_password.getText().toString(), reset_verify.getText().toString()));
    }

    public void update(String username, String newPwd, String code) {
        String password = reset_password.getText().toString();
        String mail = reset_mail.getText().toString();
        LogUtils.i(TAG, String.format("username -> %s, password -> %s, mail -> %s, code -> %s, newPwd -> %s", username, password,mail,code,newPwd));
        // show loading dialog
        showLoading();

        Api.changePwd(newPwd, mail,code,new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // destroy loading dialog
                        stopLoading();
                    }
                });
                LogUtils.e(TAG, "ChangePwd().onFailure: exception -> " + e);
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
                            String.format("ChangePwd().onResponse: code -> %s, message -> %s", code, message)
                    );
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(resetpassword.this, message, Toast.LENGTH_SHORT).show();
                            if (code == 0) {
                                Intent i = new Intent(resetpassword.this, LoginActivity.class);
                                startActivity(i);
                                overridePendingTransition(0, 0);
                            }
                        }
                    });

                } else {
                    LogUtils.e(TAG, "ChangePwd().onResponse: response.body() == null");
                }

            }
        });
    }

    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
            mLoadingDialog.setText("修改中...");
            mLoadingDialog.setCancelable(false);
            mLoadingDialog.show();
        }
    }

    public void stopLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    public void send(String to) {
        if(!E_mail.isValidEmail(to)){
            Toast.makeText(this, "请输入有效邮箱", Toast.LENGTH_SHORT).show();
            return;
        }
        Api.sendMail(to,13,new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // destroy loading dialog
                        stopLoading();
                    }
                });
                LogUtils.e(TAG, "sendMail13().onFailure: exception -> " + e);
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
                            String.format("sendMail13().onResponse: code -> %s, message -> %s", code, message)
                    );
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(resetpassword.this, message, Toast.LENGTH_SHORT).show();
                            if (code == 0) {

                            }
                        }
                    });

                } else {
                    LogUtils.e(TAG, "sendMail13().onResponse: response.body() == null");
                }

            }
        });
    }
}
