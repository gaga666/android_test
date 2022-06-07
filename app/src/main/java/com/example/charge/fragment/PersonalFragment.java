package com.example.charge.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.charge.ActivityCollector;
import com.example.charge.R;
import com.example.charge.TokenManager;
import com.example.charge.UserInfoManager;
import com.example.charge.api.Api;
import com.example.charge.api.callback.ApiCallback;
import com.example.charge.api.exception.ApiException;
import com.example.charge.changemail.ChangeMailActivity;
import com.example.charge.changepwd.ChangePwdActivity;
import com.example.charge.login.LoginActivity;
import com.example.charge.search.search;
import com.example.charge.signup.Register;
import com.example.charge.changeavatar.ChangeAvatarActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalFragment extends Fragment implements AdapterView.OnItemClickListener {
    ListView listView;
    SimpleAdapter simpleAdapter;
    ImageView personalAvatar;
    private TextView personalUname;
    private TextView personalUid;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public PersonalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment personalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalFragment newInstance(String param1, String param2) {
        PersonalFragment fragment = new PersonalFragment();
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
//         Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal, container,false);
        personalAvatar = view.findViewById(R.id.personal_avatar);
        Glide.with(getActivity()).load(UserInfoManager.getInstance().getAvatar()).into(personalAvatar);

        personalUname = view.findViewById(R.id.personal_uname);
        personalUname.setText(UserInfoManager.getInstance().getUsername());

        personalUid = view.findViewById(R.id.personal_uid);
        personalUid.setText("UID:" + UserInfoManager.getInstance().getUid());
        personalUid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText(null, ((TextView) v).getText());
                cm.setPrimaryClip(clipData);
                Toast.makeText(getContext(), "UID号已复制到剪切板", Toast.LENGTH_SHORT).show();
            }
        });

        listView = view.findViewById(R.id.personal_view);
        simpleAdapter = new SimpleAdapter(getActivity(),getData(),R.layout.list,new String[]{"title","image"},new int[]{R.id.list_text1,R.id.list_image});
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent intent = new Intent(getActivity(), ChangeAvatarActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(getActivity(), ChangePwdActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(getActivity(), ChangeMailActivity.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(getActivity(), search.class);
                        startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4 = new Intent(getActivity(), Register.class);
                        startActivity(intent4);
                        break;
                    case 5:
                        diaLog();
                        // 清理 TOKEN 信息
//                        TokenManager.getInstance().clearInfo();
//                        // 清理用户信息
//                        UserInfoManager.getInstance().clearInfo();
//                        // 关闭所有 Activity
//                        ActivityCollector.finishAll();
//                        Intent intent5 = new Intent(getActivity(), LoginActivity.class);
//                        startActivity(intent5);
                        break;
                }
            }
        });
        return view;
    }

    private void diaLog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确认退出登录？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Logout();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }
    private void Logout(){
        Api.logout(new ApiCallback() {
            @Override
            public void onSuccess() {
                getActivity().runOnUiThread(() -> {
                    TokenManager.getInstance().clearInfo();
                    // 清理用户信息
                    UserInfoManager.getInstance().clearInfo();
                    // 关闭所有 Activity
                    ActivityCollector.finishAll();
                    Intent intent5 = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent5);
                });
            }

            @Override
            public void onFailure(int errCode, @NonNull String errMsg) {
                /**
                 * TODO
                 */
//                Toast.makeText(getActivity(), errCode+errMsg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(@NonNull ApiException e) {
            }
        });
    }

    private List<Map<String,Object>> getData(){
        String[] titles = {"修改头像","修改密码","修改邮箱","搜索","注册新账号","退出登录"};
        int[] images = {R.drawable.username,R.drawable.password,R.drawable.e_mail,R.drawable.search,R.drawable.username,R.drawable.exit};
        List<Map<String,Object>> list=new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Map map =new HashMap();
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
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String text = listView.getAdapter().getItem(i).toString();
    }
}