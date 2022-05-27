package com.example.charge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LoopView extends AppCompatActivity {

    private ViewPager viewPager;  //轮播图模块
    private int[] mImg;
    private int[] mImg_id;
    private String[] mDec;
    private ArrayList<ImageView> mImgList;
    private LinearLayout ll_dots_container;
    private TextView loop_dec;
    private int previousSelectedPosition = 0;
    boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loop_view);
        initLoopView();
        Button loop_button = findViewById(R.id.loop_button);
        loop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(LoopView.this,First.class);
                startActivity(i);
                overridePendingTransition(0,0);
            }
        });
    }
    private void initLoopView(){
        viewPager = (ViewPager) findViewById(R.id.loopviewpager);
        ll_dots_container = (LinearLayout) findViewById(R.id.ll_dots_loop);
        loop_dec = (TextView) findViewById(R.id.loop_dec);
        // 图片资源id数组
        mImg = new int[]{
                R.drawable.le,
                R.drawable.beila,
                R.drawable.xiangwan,
                R.drawable.jiaran,
                R.drawable.nailin,
        };

        // 文本描述
        mDec = new String[]{
                "啵啵小狗ww",
                "风妤_嘟嘟嘟",
                "小粒Q_Channel",
                "...",
                "小K_KL"
        };

        mImg_id = new int[]{
                R.id.pager_img1,
                R.id.pager_img2,
                R.id.pager_img3,
                R.id.pager_img4,
                R.id.pager_img5
        };

        // 初始化要展示的5个ImageView
        mImgList = new ArrayList<ImageView>();
        ImageView imageView;
        View dotView;
        LinearLayout.LayoutParams layoutParams;
        for(int i=0;i<mImg.length;i++){
            //初始化要显示的图片对象
            imageView = new ImageView(this);
            imageView.setBackgroundResource(mImg[i]);
            imageView.setId(mImg_id[i]);
            imageView.setOnClickListener(new pagerOnClickListener(getApplicationContext()));
            mImgList.add(imageView);
            //加引导点
            dotView = new View(this);
            dotView.setBackgroundResource(R.drawable.point);
            layoutParams = new LinearLayout.LayoutParams(10,10);
            if(i!=0){
                layoutParams.leftMargin=10;
            }
            //设置默认所有都不可用
            dotView.setEnabled(false);
            ll_dots_container.addView(dotView,layoutParams);
        }

        ll_dots_container.getChildAt(0).setEnabled(true);
        loop_dec.setText(mDec[0]);
        previousSelectedPosition=0;
        //设置适配器
        viewPager.setAdapter(new LoopViewAdapter(mImgList));
        // 把ViewPager设置为默认选中Integer.MAX_VALUE / t2，从十几亿次开始轮播图片，达到无限循环目的;
        int m = (Integer.MAX_VALUE / 2) %mImgList.size();
        int currentPosition = Integer.MAX_VALUE / 2 - m;
        viewPager.setCurrentItem(currentPosition);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                int newPosition = i % mImgList.size();
                loop_dec.setText(mDec[newPosition]);
                ll_dots_container.getChildAt(previousSelectedPosition).setEnabled(false);
                ll_dots_container.getChildAt(newPosition).setEnabled(true);
                previousSelectedPosition = newPosition;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        // 开启轮询
        new Thread(){
            public void run(){
                isRunning = true;
                while(isRunning){
                    try{
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //下一条
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                        }
                    });
                }
            }
        }.start();

    }
}
class pagerOnClickListener implements View.OnClickListener{

    Context mContext;
    public pagerOnClickListener(Context mContext){
        this.mContext=mContext;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pager_img1:
                Toast.makeText(mContext, "关注啵啵小狗ww哞", Toast.LENGTH_SHORT).show();
                break;
            case R.id.pager_img2:
                Toast.makeText(mContext, "关注风妤_嘟嘟嘟哞", Toast.LENGTH_SHORT).show();
                break;
            case R.id.pager_img3:
                Toast.makeText(mContext, "关注小粒Q_Channel哞", Toast.LENGTH_SHORT).show();
                break;
            case R.id.pager_img4:
                Toast.makeText(mContext, "暂时无", Toast.LENGTH_SHORT).show();
                break;
            case R.id.pager_img5:
                Toast.makeText(mContext, "关注小K_KL哞", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
