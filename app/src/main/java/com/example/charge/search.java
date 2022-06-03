package com.example.charge;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class search extends AppCompatActivity {
    ImageView search_search;
    SearchView search_edit;
    ListView search_list;
    SimpleAdapter simpleAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }
    private void initView(){
        search_edit = findViewById(R.id.search_edit);
        search_list = findViewById(R.id.search_list);
        simpleAdapter = new SimpleAdapter(this,getData(),R.layout.search_list,new String[]{"text"},new int[]{R.id.search_list_text});
        search_list.setAdapter(simpleAdapter);
        search_list.setTextFilterEnabled(true);
        search_edit.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!TextUtils.isEmpty(s)){
                    search_list.setFilterText(s);
                }else {
                    search_list.clearTextFilter();
                }
                return false;
            }
        });
    }
    private List<Map<String,Object>> getData(){
        String[] text = {"历史记录1","历史记录2","历史记录3"};
        List<Map<String,Object>> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Map map = new HashMap();
            map.put("text",text[i]);
            list.add(map);
        }
        return list;
    }

}