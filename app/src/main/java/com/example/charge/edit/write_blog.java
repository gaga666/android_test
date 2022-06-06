package com.example.charge.edit;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.charge.R;
import com.example.charge.utils.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class write_blog extends AppCompatActivity {
    private EditText write_editText;
    private Button write_send;
    private GridView gridView;
    private final int IMAGE_OPEN = 1;
    private File imageFile;
    private SimpleAdapter simpleAdapter;
    private ArrayList<HashMap<String ,Object>> imageItem;
    private String imagePath;
    private Bitmap bmp;
    private ImageView imageView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_blog);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
    }
    private void initView(){
        gridView = findViewById(R.id.gridView1);
        imageView1 = findViewById(R.id.imageView1);
        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.add_image);
        imageItem = new ArrayList<HashMap<String,Object>>();
        HashMap<String,Object> map= new HashMap<String,Object>();
//        map.put("itemImage",bmp);
        map.put("itemImage",bmp);
        imageItem.add(map);
        simpleAdapter = new SimpleAdapter(this, imageItem, R.layout.image_shape,new String[]{"itemImage"},new int[]{R.id.imageView1});
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object o, String s) {
                if (view instanceof ImageView && o instanceof Bitmap){
                    ImageView i = (ImageView) view;
                    i.setImageBitmap((Bitmap) o);
                    return true;
                }
                return false;
            }
        });
        gridView.setAdapter(simpleAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (imageItem.size() == 10 && i == 0){
                    Toast.makeText(write_blog.this, "图片数9张已满", Toast.LENGTH_SHORT).show();
                }else if(i == 0){
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,IMAGE_OPEN);
                }else {
                    dialog(i);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && requestCode==IMAGE_OPEN){
            Uri uri = data.getData();
            imageFile = uriToFile(uri);
            if(!TextUtils.isEmpty(uri.getAuthority())){
                Cursor cursor = getContentResolver().query(uri,new String[]{MediaStore.Images.Media.DATA},null,null,null);
                if (cursor == null){
                    return;
                }
                cursor.moveToFirst();
                imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                LogUtils.log("imagePath-> "+imagePath);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(String.valueOf(imageFile))){
            Bitmap addBmp = BitmapFactory.decodeFile(String.valueOf(imageFile));
            HashMap<String, Object> map = new HashMap<String,Object>();
            map.put("itemImage",addBmp);
            imageItem.add(map);
            simpleAdapter = new SimpleAdapter(this, imageItem,R.layout.image_shape, new String[]{"itemImage"},new int[]{R.id.imageView1});
            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object o, String s) {
                    if (view instanceof ImageView && o instanceof Bitmap){
                        ImageView i = (ImageView) view;
                        i.setImageBitmap((Bitmap) o);
                        return true;
                    }
                    return false;
                }
            });
            gridView.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
            imagePath = null;
        }
    }

    protected void dialog(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(write_blog.this);
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener(){
           @Override
           public void onClick(DialogInterface dialog, int which){
               dialog.dismiss();
               imageItem.remove(position);
               simpleAdapter.notifyDataSetChanged();
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

    /**
     * Android 10(Q)及以上 Uri to File
     * @param uri
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private File uriToFile(final Uri uri) {
        File file = null;

        if (uri == null) {
            return null;
        }

        if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
            file = new File(uri.getPath());
        } else if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            // 把文件复制到沙盒目录
            ContentResolver contentResolver = getContentResolver();
            // tempName
            String tempName =
                    System.currentTimeMillis() + "_" + Math.round((Math.random() + 1) * 1000)
                            + "." + MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri));
            LogUtils.log("tempName -> " + tempName);
            try {
                InputStream is = contentResolver.openInputStream(uri);
                File cache = new File(getCacheDir().getAbsolutePath(), tempName);
                FileOutputStream fos = new FileOutputStream(cache);
                FileUtils.copy(is, fos);
                file = cache;
                fos.close();
                is.close();
            } catch (IOException e) {
                LogUtils.error(e.getLocalizedMessage());
            }
        }
        return file;
    }
}