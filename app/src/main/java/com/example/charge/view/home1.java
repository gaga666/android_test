package com.example.charge.view;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.charge.R;

public class home1 extends AppCompatActivity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home1);

        initView();
    }

    private void initView(){
        webView = findViewById(R.id.home_web);
        webView.loadUrl("https://www.zhihu.com/pin/1516790050466004992");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return  true;
//                return super.shouldOverrideUrlLoading(view,url);
            }
        });
    }
}