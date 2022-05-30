package com.example.charge;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.charge.fragment.addFragment;
import com.example.charge.fragment.homeFragment;
import com.example.charge.fragment.personalFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BottomBar extends AppCompatActivity {
    private int TAG = 0;
    private TextView bottom_bar_1text,bottom_bar_2text,bottom_bar_3text;
    private ImageView bottom_bar_1image,bottom_bar_2image,bottom_bar_3image;
    private RelativeLayout bottom_bar_1btn,bottom_bar_2btn,bottom_bar_3btn;
    private com.example.charge.fragment.homeFragment homeFragment;
    private com.example.charge.fragment.addFragment addFragment;
    private com.example.charge.fragment.personalFragment personalFragment;
    private List<Map<String, Object>> lists =new ArrayList<Map<String,Object>>();
    private SimpleAdapter adapter;

    private String[] list_text1 = {"中考词汇","高考词汇","四级词汇","六级词汇","考研词汇","托福词汇","雅思词汇","GRE词汇"};
    private String[] list_text2 = {"这里是中考考纲要求的词汇","这里是高考考纲要求的词汇","这里是四级考纲要求的词汇","这里是六级考纲要求的词汇","这里是研究生入学考试考纲要求的词汇","这里是托福考纲要求的词汇","这里是雅思考纲要求的词汇","这里是GRE考纲要求的词汇"};
    private int[] list_image = new int[]{R.drawable.zk,R.drawable.gk,R.drawable.cet4,R.drawable.cet6,R.drawable.ky,R.drawable.tuofu,R.drawable.yasi,R.drawable.gre};
    //此处暂时使用一个图片，后面要拓宽为图片组
    private FrameLayout bottom_bar_container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar);
        initView();
        newsList();
        homeFragment = new homeFragment();
        addFragment = new addFragment();
        personalFragment = new personalFragment();

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