package com.example.charge;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.charge.headImage.ImageUtils;

import java.io.File;

public class uses_head extends AppCompatActivity {
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    Button use_head;
    ImageView use_image;
    Uri imageUri;
    File file;
    String url = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uses_head);
        InitView();

    }
    protected void showChoosePicDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items = {"选择本地照片","拍照"};
        builder.setNegativeButton("取消",null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case CHOOSE_PICTURE:
                        Intent openAlumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        openAlumIntent.setType("image/*");
                        startActivityForResult(openAlumIntent,CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE:
                        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,tempUri);
                        startActivityForResult(openCameraIntent,TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.create().show();
    }
    private void InitView(){
        use_head = findViewById(R.id.use_change);
        use_image = findViewById(R.id.use_image);
        OnClick();
    }
    private void OnClick(){
        use_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChoosePicDialog();
            }
        });
    }
    @SuppressLint({"MissingSuperCall", "Range"})
    @Override
    protected void onActivityResult(int requestCode ,int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri);
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData());
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null){
                        setImageToView(data);
                    }
                    break;
            }
        }
    }

    /**
     * 保存裁剪后的图片
     * @param data
     */
    protected void setImageToView(Intent data){
        Bundle extras = data.getExtras();
        if (extras != null){
            Bitmap photo = extras.getParcelable("data");
            photo = ImageUtils.toRoundBitmap(photo,tempUri);
            use_image.setImageBitmap(photo);
            uploadPic(photo);
        }
    }

    /**
     * 上传图片
     * @param bitmap
     */
    private void uploadPic(Bitmap bitmap){
        String imagePath = ImageUtils.savePhoto(bitmap,Environment
                .getExternalStorageDirectory().getAbsolutePath(),String.valueOf(System.currentTimeMillis()));
        Log.e("imagePath",imagePath+"");
        if(imagePath != null){

        }
    }

    /**
     * 裁剪
     * @param uri
     */
    protected void startPhotoZoom(Uri uri){
        if (uri == null){
            Log.i("tag","The image uri is not exist");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri,"image/*");
        intent.putExtra("crop","true");
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        intent.putExtra("outputX",150);
        intent.putExtra("outputY",150);
        intent.putExtra("return-data",true);
        startActivityForResult(intent,CROP_SMALL_PICTURE);
    }
//    private void requestPermission(){
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//        }else {
//            openAlbum();
//        }
//    }
//    private void openAlbum(){
//        Intent intent = new Intent("android.intent.action.GET_CONTENT");
//        intent.setType("image/*");
//        startActivityForResult(intent,2); //打开相册
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode){
//            case 1:
//                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    openAlbum();
//                }else {
//                    Toast.makeText(this,"你拒绝了该权限",Toast.LENGTH_SHORT).show();
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode){
//            case 2:
//                if(resultCode == RESULT_OK){
//                    //判断手机系统版本号
//                    if(Build.VERSION.SDK_INT>=19){
//                        //4.4及以上系统使用这个方法处理图片
//                        handleImageOnKitKat(data);
//                    }
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private void handleImageOnKitKat(Intent data){
//        String imagePath = null;
//        Uri uri = data.getData();
//        if(DocumentsContract.isDocumentUri(this,uri)){
//            //如果是document类型的Uri，则通过document id处理
//            String docId = DocumentsContract.getDocumentId(uri);
//            if("com.android.providers.media.documents".equals(uri.getAuthority())){
//                String id = docId.split(":")[1];  //解析出数字格式的id
//                String selection = MediaStore.Images.Media._ID+"="+id;
//                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
//            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
//                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public downloads"),Long.valueOf(docId));
//                imagePath = getImagePath(contentUri,null);
//            }
//        }else if("content".equalsIgnoreCase(uri.getScheme())){
//            //如果是file类型的Uri，直接获取图片路径即可
//            imagePath = getImagePath(uri,null);
//        }else if("file".equalsIgnoreCase(uri.getScheme())){
//            //如果是file类型的Uri，直接获取图片路径即可
//            imagePath = uri.getPath();
//        }
//        displayImage(imagePath); //根据图片路径显示图片
//    }
//
//    //将选择的图片Uri转换为路径
//    @SuppressLint("Range")
//    private String getImagePath(Uri uri, String selection){
//        String path = null;
//        //通过Uri和selection来获取真实的图片路径
//        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
//        if(cursor!= null){
//            if(cursor.moveToFirst()){
//                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//            }
//            cursor.close();
//        }
//        return path;
//    }
//
//    //展示图片
//    private void displayImage(String imagePath){
//        if(imagePath!=null && !imagePath.equals("")){
//            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//            use_image.setImageBitmap(bitmap);
//            //存储上次选择的图片路径，用以再次打开app设置图片
//            SharedPreferences sp = getSharedPreferences("sp_img",MODE_PRIVATE);  //创建xml文件存储数据，name:创建的xml文件名
//            SharedPreferences.Editor editor = sp.edit(); //获取edit()
//            editor.putString("imgPath",imagePath);
//            editor.apply();
//        }else {
//            Toast.makeText(this,"获取图片失败",Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        //设置再次app时显示的图片
//        SharedPreferences sp = getSharedPreferences("sp_img", MODE_PRIVATE);
//        //取出上次存储的图片路径设置此次的图片展示
//        String beforeImagePath = sp.getString("imgPath", null);
//        displayImage(beforeImagePath);
//    }
}