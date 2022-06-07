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
import com.example.charge.edit.write_blog;
import com.example.charge.search.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {
    private ListView add_listView;
    private SimpleAdapter simpleAdapter;
    private DragFloatActionButton search_user;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView bar_add_text;
    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
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
        View view= inflater.inflate(R.layout.fragment_add, container, false);

        search_user = view.findViewById(R.id.search_user);
        search_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), search.class);
                startActivity(intent);
            }
        });
        add_listView = view.findViewById(R.id.add_list);
        simpleAdapter = new SimpleAdapter(getActivity(),getData(),R.layout.add_list_view,new String[]{"title","image"},new int[]{R.id.add_list_text1,R.id.add_list_image});
        add_listView.setAdapter(simpleAdapter);

        add_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){

                }
            }
        });
        return view;
    }
    private List<Map<String ,Object>> getData(){
        String[] titles = {"关注1号","关注2号","关注3号"};
        int[] images = {R.drawable.gk,R.drawable.zk,R.drawable.exit};
        List<Map<String ,Object>> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Map map = new HashMap();
            map.put("image",images[i]);
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