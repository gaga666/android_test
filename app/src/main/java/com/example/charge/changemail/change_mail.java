package com.example.charge.changemail;

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
import com.example.charge.login.LoginActivity;
import com.example.charge.utils.LogUtils;
import com.example.charge.view.LoadingDialog;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class change_mail extends AppCompatActivity {
    private static final String TAG = change_mail.class.getName();
    EditText mail_old_mail,mail_new_mail,mail_verify;
    Button mail_getVerify,mail_reset;
    LoadingDialog mLoadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_mail);
        initView();
    }
    private void initView(){
        mail_new_mail = findViewById(R.id.mail_new_mail);
        mail_old_mail = findViewById(R.id.mail_old_mail);
        mail_verify = findViewById(R.id.mail_verify);
        mail_getVerify = findViewById(R.id.mail_getVerify);
        mail_reset = findViewById(R.id.mail_reset);
        OnClick();
    }
    private void OnClick(){
        mail_getVerify.setOnClickListener(view -> send(mail_old_mail.getText().toString()));
        mail_reset.setOnClickListener(view -> update_mail(mail_old_mail.getText().toString(),mail_new_mail.getText().toString(),mail_verify.getText().toString()));
    }

    public void update_mail(String oldMail, String newMail, String code) {
        LogUtils.i(TAG, String.format("oldMail -> %s, code -> %s, newMail -> %s", oldMail, code,newMail));
        // show loading dialog
        showLoading();
        Api.changeMail(oldMail, newMail,code,new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    // destroy loading dialog
                    stopLoading();
                });
                LogUtils.e(TAG, "ChangeMail().onFailure: exception -> " + e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(() -> {
                    // destroy loading dialog
                    stopLoading();
                });
                if (response.body() != null) {
                    String json = response.body().string();
                    ObjectMapper mapper = new ObjectMapper();
                    MessageResponse res = mapper.readValue(json, MessageResponse.class);
                    int code = res.getCode();
                    String message = res.getMessage();
                    LogUtils.i(TAG,
                            String.format("ChangeMail().onResponse: code -> %s, message -> %s", code, message)
                    );
                    runOnUiThread(() -> {
                        Toast.makeText(change_mail.this, message, Toast.LENGTH_SHORT).show();
                        if (code == 0) {
                            Intent i = new Intent(change_mail.this, LoginActivity.class);
                            startActivity(i);
                            overridePendingTransition(0, 0);
                        }
                    });

                } else {
                    LogUtils.e(TAG, "ChangeMail().onResponse: response.body() == null");
                }

            }
        });
    }

    private void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
            mLoadingDialog.setText("修改中...");
            mLoadingDialog.setCancelable(false);
            mLoadingDialog.show();
        }
    }

    public void send(String to) {
        if(!e_mail.isValidEmail(to)){
            Toast.makeText(this, "请输入有效邮箱", Toast.LENGTH_SHORT).show();
            return;
        }
        Api.sendMail(to,14,new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    // destroy loading dialog
                    stopLoading();
                });
                LogUtils.e(TAG, "sendMail14().onFailure: exception -> " + e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(() -> {
                    // destroy loading dialog
                    stopLoading();
                });
                if (response.body() != null) {
                    String json = response.body().string();
                    ObjectMapper mapper = new ObjectMapper();
                    MessageResponse res = mapper.readValue(json, MessageResponse.class);
                    int code = res.getCode();
                    String message = res.getMessage();
                    LogUtils.i(TAG,
                            String.format("sendMail14().onResponse: code -> %s, message -> %s", code, message)
                    );
                    runOnUiThread(() -> {
                        Toast.makeText(change_mail.this, message, Toast.LENGTH_SHORT).show();
                        if (code == 0) {

                        }
                    });

                } else {
                    LogUtils.e(TAG, "sendMail13().onResponse: response.body() == null");
                }

            }
        });
    }

    private void stopLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }
}