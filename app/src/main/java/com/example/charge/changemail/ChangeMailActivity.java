package com.example.charge.changemail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.charge.BaseActivity;
import com.example.charge.E_mail;
import com.example.charge.R;
import com.example.charge.api.exception.ApiException;
import com.example.charge.api.callback.ApiCallback;
import com.example.charge.api.Api;
import com.example.charge.login.LoginActivity;
import com.example.charge.utils.LogUtils;
import com.example.charge.view.LoadingDialog;

public class ChangeMailActivity extends BaseActivity {

    private static final String TAG = ChangeMailActivity.class.getName();

    EditText mail_old_mail, mail_new_mail,mail_verify;
    Button mail_getVerify, mail_reset;
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

        // set click listener
        mail_getVerify.setOnClickListener(view -> {
            sendEmail(mail_old_mail.getText().toString());
        });
        mail_reset.setOnClickListener(view -> {
            String oldMail = mail_old_mail.getText().toString();
            String newMail = mail_new_mail.getText().toString();
            String code = mail_verify.getText().toString();
            updateMail(oldMail, newMail, code);
        });
    }

    public void updateMail(String oldMail, String newMail, String code) {
        LogUtils.i(TAG, String.format("oldMail -> %s, code -> %s, newMail -> %s", oldMail, code, newMail));
        // show loading dialog
        showLoading();
        Api.changeMail(oldMail, newMail, code, new ApiCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    // destroy loading dialog
                    stopLoading();

                    Toast.makeText(ChangeMailActivity.this, "????????????", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(ChangeMailActivity.this, LoginActivity.class);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                });
            }
            @Override
            public void onFailure(int errCode, @NonNull String errMsg) {
                String log = "errCode: " + errCode + ", errMsg: " + errMsg;
                LogUtils.e(TAG, "sendEmail().onFailure: " + log);

                runOnUiThread(() -> {
                    Toast.makeText(ChangeMailActivity.this, log, Toast.LENGTH_SHORT).show();
                });
            }
            @Override
            public void onException(@NonNull ApiException e) {
                LogUtils.e(TAG, "updateMail().onException: e -> " + e);

                runOnUiThread(() -> {
                    // destroy loading dialog
                    stopLoading();
                });
            }
        });
    }

    public void sendEmail(String to) {
        if(!E_mail.isValidEmail(to)) {
            Toast.makeText(this, "?????????????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        Api.sendMail(to,14, new ApiCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(ChangeMailActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
                });
            }
            @Override
            public void onFailure(int errCode, @NonNull String errMsg) {
                String log = "errCode: " + errCode + ", errMsg: " + errMsg;
                LogUtils.e(TAG, "sendEmail().onFailure: " + log);

                runOnUiThread(() -> {
                    Toast.makeText(ChangeMailActivity.this, log, Toast.LENGTH_SHORT).show();
                });
            }
            @Override
            public void onException(@NonNull ApiException e) {
                LogUtils.e(TAG, "sendEmail().onException: e -> " + e);
            }
        });
    }

    private void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
            mLoadingDialog.setText("?????????...");
            mLoadingDialog.setCancelable(false);
            mLoadingDialog.show();
        }
    }

    private void stopLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }
}