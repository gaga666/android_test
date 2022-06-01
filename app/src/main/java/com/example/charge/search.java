package com.example.charge;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

public class search extends AppCompatActivity {
    ImageView search_search;
    SearchView search_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }
    private void initView(){
        search_search = findViewById(R.id.search_search);
        search_edit = findViewById(R.id.search_edit);
        onClick();
    }
    private void onClick(){

    }
}