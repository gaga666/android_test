package com.example.charge;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class Home1 extends AppCompatActivity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home1);
        initView();
    }
    private void initView(){
        webView = findViewById(R.id.home_web);
        webView.loadUrl("http://baidu.com");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
//                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }
//    //返回按键作用修改
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event){
//        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()){
//            webView.goBack();
//            return true;
//        }
//        return super.onKeyDown(keyCode,event);
//    }

}