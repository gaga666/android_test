package com.example.charge.search;

import android.app.ActionBar;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.charge.BaseActivity;
import com.example.charge.R;
import com.example.charge.api.Api;
import com.example.charge.api.callback.ApiDataCallback;
import com.example.charge.api.exception.ApiException;
import com.example.charge.api.model.dto.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class search extends BaseActivity {
    ImageView search_image;
    SearchView search_edit;
    ListView search_list;
    TextView search_text1,search_text2;
    SimpleAdapter simpleAdapter;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView(){
        search_edit = findViewById(R.id.search_edit);
        search_image = findViewById(R.id.search_image);
        search_list = findViewById(R.id.search_list);
        search_text1 = findViewById(R.id.search_text1);
        search_text2 = findViewById(R.id.search_text2);
        simpleAdapter = new SimpleAdapter(search.this,getData(),R.layout.search_list,new String[]{"text"},new int[]{R.id.search_list_text});
        search_list.setAdapter(simpleAdapter);
        search_list.setTextFilterEnabled(true);
        search_edit.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (!isUID(s)) {
                    searchUsername(s);
                }else {
                    String str = s.substring(4,5);
                    searchUID(Long.parseLong(str));
                }
                return false;
            }
            public void searchUID(Long uid){
                Api.getUserInfoByUid(uid, new ApiDataCallback<UserInfo>() {
                    @Override
                    public void onSuccess(@NonNull UserInfo data) {
                        String avatar = data.getAvatar();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(search.this).load(avatar).into(search_image);
                                search_text1.setText("username:  " + data.getUsername());
                                search_text2.setText("uid :   "+data.getUid().toString());
                            }
                        });
                    }

                    @Override
                    public void onFailure(int errCode, @NonNull String errMsg) {

                    }

                    @Override
                    public void onException(@NonNull ApiException e) {

                    }
                });
            }
            public boolean isUID(String s){
                if (s ==null){
                    return false;
                }
                Pattern pattern = Pattern.compile("^UID:\\S$");
                return pattern.matcher(s).matches();
            }
            public void searchUsername(String username){
                Api.getUserInfoByUsername(username,new ApiDataCallback<UserInfo>(){

                    @Override
                    public void onSuccess(@NonNull UserInfo data) {
                        String avatar = data.getAvatar();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(search.this).load(avatar).into(search_image);
                                search_text1.setText("username:  " + data.getUsername());
                                search_text2.setText("uid :   "+data.getUid().toString());
                            }
                        });
                    }

                    @Override
                    public void onFailure(int errCode, @NonNull String errMsg) {

                    }

                    @Override
                    public void onException(@NonNull ApiException e) {

                    }
                });
            }
            @Override
            public boolean onQueryTextChange(String s) {
                if(!TextUtils.isEmpty(s)){
                    search_list.setFilterText(s);
                    search_list.dispatchDisplayHint(View.INVISIBLE);
                }else {
                    search_list.clearTextFilter();
                }
                return true;
            }
        });
    }
    private List<Map<String,Object>> getData(){
        String[] text = {"历史记录1","历史记录2"};
        List<Map<String,Object>> list = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            Map map = new HashMap();
            map.put("text","");
            list.add(map);
        }
        return list;
    }

}