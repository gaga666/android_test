package com.example.charge;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class maininterface extends AppCompatActivity {
    private List<Map<String, Object>> lists =new ArrayList<Map<String,Object>>();
    private SimpleAdapter adapter;

    private String[] list_text1 = {"中考词汇","高考词汇","四级词汇","六级词汇","考研词汇","托福词汇","雅思词汇","GRE词汇"};
    private String[] list_text2 = {"这里是中考考纲要求的词汇","这里是高考考纲要求的词汇","这里是四级考纲要求的词汇","这里是六级考纲要求的词汇","这里是研究生入学考试考纲要求的词汇","这里是托福考纲要求的词汇","这里是雅思考纲要求的词汇","这里是GRE考纲要求的词汇"};
    private int[] list_image = new int[]{R.drawable.zk,R.drawable.gk,R.drawable.cet4,R.drawable.cet6,R.drawable.ky,R.drawable.tuofu,R.drawable.yasi,R.drawable.gre};
    //此处暂时使用一个图片，后面要拓宽为图片组

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maininterface);
        SwipeRefreshLayout inter_refresh = findViewById(R.id.inter_refresh);
        inter_refresh.setColorSchemeResources(R.color.blue);
        inter_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                inter_refresh.setRefreshing(false);
            }
        });
        /**
         * 把图片和文字对应起来
         */
        for (int i = 0; i < list_image.length; i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("list_image",list_image[i]);
            map.put("list_text1",list_text1[i]);
//            map.put("list_text2",list_text2[i]);
            lists.add(map);
        }
        /**
         * 指定布局格式
         */
        adapter = new SimpleAdapter(maininterface.this
                ,lists
                ,R.layout.list
                ,new String[]{"list_image","list_text1","list_text2"}
                , new int[]{R.id.list_image,R.id.list_text1});
        ListView listView = (ListView) findViewById(R.id.inter_listview);
        listView.setAdapter(adapter);
    }
}