package com.example.charge.BottomBar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.charge.R;
import com.example.charge.fragment.AddFragment;
import com.example.charge.fragment.HomeFragment;
import com.example.charge.fragment.PersonalFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BottomBar extends AppCompatActivity {
    private int TAG = 0;
    private TextView bottom_bar_1text,bottom_bar_2text,bottom_bar_3text;
    private ImageView bottom_bar_1image,bottom_bar_2image,bottom_bar_3image;
    private RelativeLayout bottom_bar_1btn,bottom_bar_2btn,bottom_bar_3btn;
    private HomeFragment homeFragment;
    private AddFragment addFragment;
    private PersonalFragment personalFragment;
    private List<Map<String, Object>> lists =new ArrayList<Map<String,Object>>();
    private SimpleAdapter adapter;

    private FrameLayout bottom_bar_container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar);
        initView();
        newsList();
        homeFragment = new HomeFragment();
        addFragment = new AddFragment();
        personalFragment = new PersonalFragment();

    }
    private void newsList(){

    }
    private void initView(){
        bottom_bar_1text = findViewById(R.id.bottom_bar_1text);
        bottom_bar_2text = findViewById(R.id.bottom_bar_2text);
        bottom_bar_3text = findViewById(R.id.bottom_bar_3text);
        bottom_bar_1image = findViewById(R.id.bottom_bar_1image);
        bottom_bar_2image = findViewById(R.id.bottom_bar_2image);
        bottom_bar_3image = findViewById(R.id.bottom_bar_3image);
        bottom_bar_1btn = findViewById(R.id.bottom_bar_1btn);
        bottom_bar_2btn = findViewById(R.id.bottom_bar_2btn);
        bottom_bar_3btn = findViewById(R.id.bottom_bar_3btn);
        bottom_bar_container = findViewById(R.id.bottom_bar_container);

        bottom_bar_1btn.setOnClickListener(this::onClick);
        bottom_bar_2btn.setOnClickListener(this::onClick);
        bottom_bar_3btn.setOnClickListener(this::onClick);
    }
    public void setSelectStatus(int index){
        switch (index){
            case 0:
                bottom_bar_1image.setImageResource(R.drawable.home_sel);
                bottom_bar_1text.setTextColor(Color.parseColor("#0097F7"));

                bottom_bar_2image.setImageResource(R.drawable.add);
                bottom_bar_2text.setTextColor(Color.parseColor("#000000"));
                bottom_bar_3image.setImageResource(R.drawable.username);
                bottom_bar_3text.setTextColor(Color.parseColor("#000000"));
                break;
            case 1:
                bottom_bar_1image.setImageResource(R.drawable.ic_home_black_24dp);
                bottom_bar_1text.setTextColor(Color.parseColor("#000000"));

                bottom_bar_2image.setImageResource(R.drawable.add_sel);
                bottom_bar_2text.setTextColor(Color.parseColor("#0097F7"));
                bottom_bar_3image.setImageResource(R.drawable.username);
                bottom_bar_3text.setTextColor(Color.parseColor("#000000"));
                break;
            case 2:
                bottom_bar_1image.setImageResource(R.drawable.ic_home_black_24dp);
                bottom_bar_1text.setTextColor(Color.parseColor("#000000"));

                bottom_bar_2image.setImageResource(R.drawable.add);
                bottom_bar_2text.setTextColor(Color.parseColor("#000000"));
                bottom_bar_3image.setImageResource(R.drawable.personal_sel);
                bottom_bar_3text.setTextColor(Color.parseColor("#0097F7"));
                break;
        }
    }
    public void onClick(View v){
        if(TAG == 0){
            TAG = 1;
            switch (v.getId()){
                case R.id.bottom_bar_1btn:
                    getSupportFragmentManager().beginTransaction().add(R.id.bottom_bar_container,homeFragment).commitAllowingStateLoss();
                    setSelectStatus(0);
                    break;
                case R.id.bottom_bar_2btn:
                    getSupportFragmentManager().beginTransaction().add(R.id.bottom_bar_container,addFragment).commitAllowingStateLoss();
                    setSelectStatus(1);
                    break;
                case R.id.bottom_bar_3btn:
                    getSupportFragmentManager().beginTransaction().add(R.id.bottom_bar_container,personalFragment).commitAllowingStateLoss();
                    setSelectStatus(2);
                    break;
            }
        }else if(TAG == 1){
            switch (v.getId()){
                case R.id.bottom_bar_1btn:
                    getSupportFragmentManager().beginTransaction().replace(R.id.bottom_bar_container,homeFragment).commitAllowingStateLoss();
                    setSelectStatus(0);
                    break;
                case R.id.bottom_bar_2btn:
                    getSupportFragmentManager().beginTransaction().replace(R.id.bottom_bar_container,addFragment).commitAllowingStateLoss();
                    setSelectStatus(1);
                    break;
                case R.id.bottom_bar_3btn:
                    getSupportFragmentManager().beginTransaction().replace(R.id.bottom_bar_container,personalFragment).commitAllowingStateLoss();
                    setSelectStatus(2);
                    break;
            }
        }
    }
}