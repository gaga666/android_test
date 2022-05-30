package com.example.charge.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.charge.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link homeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class homeFragment extends Fragment {
    private List<Map<String, Object>> lists =new ArrayList<Map<String,Object>>();
    private SimpleAdapter adapter;

    private String[] list_text1 = {"中考词汇","高考词汇","四级词汇","六级词汇","考研词汇","托福词汇","雅思词汇","GRE词汇"};
    private String[] list_text2 = {"这里是中考考纲要求的词汇","这里是高考考纲要求的词汇","这里是四级考纲要求的词汇","这里是六级考纲要求的词汇","这里是研究生入学考试考纲要求的词汇","这里是托福考纲要求的词汇","这里是雅思考纲要求的词汇","这里是GRE考纲要求的词汇"};
    private int[] list_image = new int[]{R.drawable.zk,R.drawable.gk,R.drawable.cet4,R.drawable.cet6,R.drawable.ky,R.drawable.tuofu,R.drawable.yasi,R.drawable.gre};
    //此处暂时使用一个图片，后面要拓宽为图片组
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView home_list_text1,home_list_text2;
    private ImageView home_list_image;
    public homeFragment() {
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
    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();
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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}