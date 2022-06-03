package com.example.charge.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.charge.LoopView;
import com.example.charge.R;
import com.example.charge.api.remote.Api;
import com.example.charge.entity.MessageResponse;
import com.example.charge.resetpassword;
import com.example.charge.signup.Register;
import com.example.charge.utils.LogUtils;
import com.example.charge.view.LoadingDialog;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    // loading dialog
    private LoadingDialog mLoadingDialog;
    private ImageView lg_userIcon;
    private EditText lg_username,lg_password;
    private TextView lg_forgetPsd;
    private CheckBox lg_rememberPsd;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    private void initView() {

        lg_username =  findViewById(R.id.lg_username);
        lg_password =  findViewById(R.id.lg_password);
        lg_userIcon = findViewById(R.id.lg_userIcon);
        Glide.with(this).load("http://s0.objectspace.top/fs/face/noface.jpg").into(lg_userIcon);

        Button lg_login =  findViewById(R.id.lg_login);
        lg_login.setOnClickListener(view -> getUser());
        lg_forgetPsd = findViewById(R.id.lg_forgetPsd);
        lg_forgetPsd.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, resetpassword.class);
            startActivity(i);
            overridePendingTransition(0,0);
        });


        /**
         * 跳转到注册页面
         */
        TextView lg_textView =  findViewById(R.id.lg_textview);
        lg_textView.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, Register.class);
            startActivity(i);
            overridePendingTransition(0,0);
        });

        /**
         *
         * 记住密码
         */
        lg_rememberPsd =  findViewById(R.id.lg_rememberPsd);
        sharedPreferences = getSharedPreferences("items",MODE_PRIVATE);
        boolean isRemember = sharedPreferences.getBoolean("flag",false);
        if(isRemember){
            getData();
            lg_rememberPsd.performClick();
        }
        lg_rememberPsd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                if (isCheck){
                    saveData();
                }else {
                    clearData();
                }
            }
        });
    }


    private void clearData() {
        sharedPreferences = getSharedPreferences("items",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        lg_username.setText("");
        lg_password.setText("");
    }

    private void getData(){
        sharedPreferences = getSharedPreferences("items", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("lg_username","");
        String password = sharedPreferences.getString("lg_password","");
        lg_username.setText(username);
        TransformationMethod method = PasswordTransformationMethod.getInstance();
        lg_password.setText(password);
        lg_password.setTransformationMethod(method);
    }

    private void saveData() {
        sharedPreferences = getSharedPreferences("items",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String  username = lg_username.getText().toString();
        String password = lg_password.getText().toString();
        if (!username.equals("") && !password.equals("")){
            editor.putString("lg_username",username);
            editor.putString("lg_password",password);
            editor.putBoolean("flag",true);
            editor.commit();
        }
    }

    /**
     * 判断登录信息
     */
//    @SuppressLint("Range")
    public void getUser() {
        String username = lg_username.getText().toString();
        String password = lg_password.getText().toString();
        LogUtils.i(TAG, String.format("username -> %s, password -> %s", username, password));
        // show loading dialog
        showLoading();

        Api.login(username, password, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // destroy loading dialog
                        stopLoading();
                    }
                });
                LogUtils.e(TAG, "login().onFailure: exception -> " + e);
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
                            String.format("login().onResponse: code -> %s, message -> %s", code, message)
                    );
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                            if (code == 0) {
                                Intent i = new Intent(MainActivity.this, LoopView.class);
                                startActivity(i);
                                overridePendingTransition(0, 0);
                            }
                        }
                    });

                } else {
                    LogUtils.e(TAG, "login().onResponse: response.body() == null");
                }

            }
        });
    }

    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
            mLoadingDialog.setText("登录中...");
            mLoadingDialog.setCancelable(false);
            mLoadingDialog.show();
        }
    }

    public void stopLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }
}