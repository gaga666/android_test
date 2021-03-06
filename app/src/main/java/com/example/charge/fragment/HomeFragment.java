package com.example.charge.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.barnettwong.dragfloatactionbuttonlibrary.view.DragFloatActionButton;
import com.example.charge.R;
import com.example.charge.Home1;
import com.example.charge.Home2;
import com.example.charge.edit.write_blog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private List<Map<String, Object>> lists =new ArrayList<Map<String,Object>>();
    private ListView home_list;
    private SimpleAdapter simpleAdapter;
    private DragFloatActionButton home_add;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView home_list_text1,home_list_text2;
    private ImageView home_list_image;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment homeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false);
        View view = inflater.inflate(R.layout.fragment_home,container,false);
//        home_add = view.findViewById(R.id.home_add);
//        home_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), write_blog.class);
//                startActivity(intent);
//            }
//        });

        home_list = view.findViewById(R.id.home_list);
        simpleAdapter = new SimpleAdapter(getActivity(),getData(),R.layout.home_listview,new String[]{"title","image1","image2","image3"},new int[]{R.id.home_list_text,R.id.home_list_image1,R.id.home_list_image2,R.id.home_list_image3});
        home_list.setAdapter(simpleAdapter);

        home_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent intent = new Intent(getActivity(), Home1.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(getActivity(), Home2.class);
                        startActivity(intent1);
                        break;
                }
            }
        });
        return view;
    }
    private List<Map<String ,Object>> getData(){
        String[] titles = {"???????????????????????????????????????","??????????????????","test1","test2"};
        int[] images = {R.drawable.username,R.drawable.password,R.drawable.e_mail,R.drawable.search};
        List<Map<String,Object>> list=new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Map map =new HashMap();
            map.put("image3",images[i]);
            map.put("image2",images[i]);
            map.put("image1",images[i]);
            map.put("title",titles[i]);
            list.add(map);
        }
        return list;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}