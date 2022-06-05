package com.example.charge.edit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.charge.R;

public class write_blog extends AppCompatActivity {
    private EditText write_edittext;
    private RecyclerView write_recyclerView;
    private ImageView write_imageAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_blog);
        initView();
    }
    private void initView(){
        write_edittext = findViewById(R.id.write_edittext);
        write_recyclerView = findViewById(R.id.write_recyclerView);
        write_imageAdd = findViewById(R.id.write_imageAdd);
    }
}