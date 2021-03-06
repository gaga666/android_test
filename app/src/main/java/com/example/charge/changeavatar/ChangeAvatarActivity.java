package com.example.charge.changeavatar;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.example.charge.BaseActivity;
import com.example.charge.R;
import com.example.charge.UserInfoManager;
import com.example.charge.api.Api;
import com.example.charge.api.callback.ApiDataCallback;
import com.example.charge.api.exception.ApiException;
import com.example.charge.api.model.dto.ImageInfo;
import com.example.charge.api.model.dto.UserInfo;
import com.example.charge.utils.LogUtils;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ChangeAvatarActivity extends BaseActivity {

    private static final String TAG = ChangeAvatarActivity.class.getName();

    private ActivityResultLauncher<String> mReqPermissionLauncher;
    private ActivityResultLauncher<String> mGetContentLauncher;
    private ActivityResultLauncher<Uri> mTakePictureLauncher;
    private ActivityResultLauncher<Intent> mStartActivityLauncher;

    private File mCropCacheFile;
    private File mCameraCacheFile;

    private ImageView mAvatarIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_avatar);

        mAvatarIV = findViewById(R.id.avatar_preview);
        findViewById(R.id.select_avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseDialog();
            }
        });
        findViewById(R.id.upload_avatar).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View view) {
                if (mCropCacheFile != null && mCropCacheFile.exists()) {
                    uploadAvatar();
                } else {
                    Toast.makeText(ChangeAvatarActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mGetContentLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null) {
                            startUCrop(uri);
                        }
                    }
                });
        mReqPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean isGranted) {
                        if (isGranted) {
                            // ???????????????????????????
                            mCameraCacheFile = new File(getCacheDir(), "camera_cache_file.jpg");
                            String authority = "com.example.charge.fileprovider";
                            Uri uri = FileProvider.getUriForFile(ChangeAvatarActivity.this, authority, mCameraCacheFile);
                            mTakePictureLauncher.launch(uri);
                        }
                    }
                });
        mTakePictureLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean success) {
                        if (success) {
                            String authority = "com.example.charge.fileprovider";
                            Uri uri = FileProvider.getUriForFile(ChangeAvatarActivity.this, authority, mCameraCacheFile);
                            if (uri != null) {
                                startUCrop(uri);
                            }
                        }
                    }
                });
        mStartActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            final Uri resultUri = UCrop.getOutput(result.getData());
                            mAvatarIV.setImageURI(resultUri);
                        } else if (result.getResultCode() == UCrop.RESULT_ERROR) {
                            final Throwable cropError = UCrop.getError(result.getData());
                        }
                    }
                });
    }

    /**
     * ????????????: UCrop
     * @param sourceUri
     */
    private void startUCrop(Uri sourceUri) {
        ContentResolver contentResolver = getContentResolver();
        String ext = MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(sourceUri));
        // cache file
        String filename =
                System.currentTimeMillis() + "_" + Math.round((Math.random() + 1) * 1000)
                        + "." + ext;
        mCropCacheFile = new File(getCacheDir(), filename);
        String authority = "com.example.charge.fileprovider";
        Uri destUri = FileProvider.getUriForFile(ChangeAvatarActivity.this, authority, mCropCacheFile);
        Intent intent = UCrop.of(sourceUri, destUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(300, 300)
                .getIntent(ChangeAvatarActivity.this);
        mStartActivityLauncher.launch(intent);
    }

    private void chooseFromLocalContent() {
        mGetContentLauncher.launch("image/*");
    }

    private void chooseFromTakePicture() {
        mReqPermissionLauncher.launch(Manifest.permission.CAMERA);
    }

    /**
     * ??????????????????????????????
     */
    public void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("????????????");
        String[] items = { "??????????????????", "??????" };
        builder.setNegativeButton("??????", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // ??????????????????
                        chooseFromLocalContent();
                        break;
                    case 1: // ??????
                        chooseFromTakePicture();
                        break;
                }
            }
        });
        builder.create().show();
    }

    /**
     * ????????????
     * @param
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void uploadAvatar() {
        Api.changeAvatar(mCropCacheFile, new ApiDataCallback<ImageInfo>() {
            @Override
            public void onSuccess(@NonNull ImageInfo data) {
                runOnUiThread(() -> {
                    Toast.makeText(ChangeAvatarActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                    finish();
                });
                UserInfo newUserInfo = new UserInfo()
                        .setAvatar(data.getImageUrl())
                        .setSex(UserInfoManager.getInstance().getSex())
                        .setUsername(UserInfoManager.getInstance().getUsername())
                        .setUid(UserInfoManager.getInstance().getUid());
                UserInfoManager.getInstance().updateInfo(newUserInfo);
            }
            @Override
            public void onFailure(int errCode, @NonNull String errMsg) {
                String log = "errCode: " + errCode + ", errMsg: " + errMsg;
                LogUtils.e(TAG, "changeAvatar().onFailure: " + log);
                runOnUiThread(() -> {
                    Toast.makeText(ChangeAvatarActivity.this, log, Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
            @Override
            public void onException(@NonNull ApiException e) {
                LogUtils.e(TAG, "changeAvatar().onException: e -> " + e);
                runOnUiThread(() -> {
                    Toast.makeText(ChangeAvatarActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // ??????????????????
        if (mCropCacheFile != null) {
            mCropCacheFile.delete();
        }
        if (mCameraCacheFile != null) {
            mCameraCacheFile.delete();
        }
        LogUtils.d(TAG, "??????????????????");
    }

    /**
     * Android 10(Q)????????? Uri to File
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
            // ??????????????????????????????
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


//
//    private void takePicture() {
//        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
//        if (Build.VERSION.SDK_INT >= 23) {
//            // ????????????????????????
//            int check = ContextCompat.checkSelfPermission(this, permissions[0]);
//            // ?????????????????? ?????? GRANTED---??????  DINIED---??????
//            if (check != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//            }
//        }
//        Intent openCameraIntent = new Intent(
//                MediaStore.ACTION_IMAGE_CAPTURE);
//        File file = new File(Environment
//                .getExternalStorageDirectory(), "image.jpg");
//        //???????????????AndroidN?????????????????????
//        if (Build.VERSION.SDK_INT >= 24) {
//            openCameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            tempUri = FileProvider.getUriForFile(uses_head.this, "com.example.charge.fileProvider", file);
//        } else {
//            tempUri = Uri.fromFile(new File(Environment
//                    .getExternalStorageDirectory(), "image.jpg"));
//        }
//        // ???????????????????????????SD?????????image.jpg??????????????????????????????????????????????????????????????????
//        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
//        startActivityForResult(openCameraIntent, TAKE_PICTURE);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) { // ??????????????????????????????
//            switch (requestCode) {
//                case TAKE_PICTURE:
////                    startPhotoZoom(tempUri); // ?????????????????????????????????
//                    if (data != null){
//                        Uri uri = data.getData();
//                        use_image.setImageURI(uri);
//                    }
//                    break;
//                case CHOOSE_PICTURE:
////                    startPhotoZoom(data.getData()); // ?????????????????????????????????
//                    break;
//                case CROP_SMALL_PICTURE:
//                    if (data != null) {
////                        setImageToView(data); // ??????????????????????????????????????????????????????
//                    }
//                    break;
//            }
//        }
//    }
//
//    /**
//     * ????????????????????????
//     *
//     * @param uri
//     */
//    protected void startPhotoZoom(Uri uri) {
//        if (uri == null) {
//            Log.i("tag", "The uri is not exist.");
//        }
//        tempUri = uri;
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        // ????????????
//        intent.putExtra("crop", "true");
//        // aspectX aspectY ??????????????????
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        // outputX outputY ?????????????????????
//        intent.putExtra("outputX", 150);
//        intent.putExtra("outputY", 150);
//        intent.putExtra("return-data", true);
//        startActivityForResult(intent, CROP_SMALL_PICTURE);
//    }
//
//    /**
//     * ?????????????????????????????????
//     *
//     * @param
//     */
//    protected void setImageToView(Intent data) {
//        Bundle extras = data.getExtras();
//        if (extras != null) {
//            Bitmap photo = extras.getParcelable("data");
//            Log.d(TAG,"setImageToView:"+photo);
//            photo = ImageUtils.toRoundBitmap(photo); // ???????????????????????????????????????????????????
//            use_image.setImageBitmap(photo);
//            uploadPic(photo);
//        }
//    }
//
//    private void uploadPic(Bitmap bitmap) {
//        // ??????????????????
//        // ... ??????????????????Bitmap?????????file???????????????file???url????????????????????????
//        // ???????????????????????????????????????????????????
//        // bitmap??????????????????????????????????????????????????????
//        String imagePath = ImageUtils.savePhoto(bitmap, Environment
//                .getExternalStorageDirectory().getAbsolutePath(), String
//                .valueOf(System.currentTimeMillis()));
//        Log.e("imagePath", imagePath+"");
//        if(imagePath != null){
//            // ??????imagePath?????????
//            // ...
//            Log.d(TAG,"imagePath:"+imagePath);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//        } else {
//            // ???????????? ???????????????????????????????????????app
//            Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show();
//        }
//    }
//}
/**
 * G
 */
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
//        startActivityForResult(intent,2); //????????????
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode){
//            case 1:
//                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    openAlbum();
//                }else {
//                    Toast.makeText(this,"?????????????????????",Toast.LENGTH_SHORT).show();
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
//                    //???????????????????????????
//                    if(Build.VERSION.SDK_INT>=19){
//                        //4.4?????????????????????????????????????????????
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
//            //?????????document?????????Uri????????????document id??????
//            String docId = DocumentsContract.getDocumentId(uri);
//            if("com.android.providers.media.documents".equals(uri.getAuthority())){
//                String id = docId.split(":")[1];  //????????????????????????id
//                String selection = MediaStore.Images.Media._ID+"="+id;
//                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
//            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
//                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public downloads"),Long.valueOf(docId));
//                imagePath = getImagePath(contentUri,null);
//            }
//        }else if("content".equalsIgnoreCase(uri.getScheme())){
//            //?????????file?????????Uri?????????????????????????????????
//            imagePath = getImagePath(uri,null);
//        }else if("file".equalsIgnoreCase(uri.getScheme())){
//            //?????????file?????????Uri?????????????????????????????????
//            imagePath = uri.getPath();
//        }
//        displayImage(imagePath); //??????????????????????????????
//    }
//
//    //??????????????????Uri???????????????
//    @SuppressLint("Range")
//    private String getImagePath(Uri uri, String selection){
//        String path = null;
//        //??????Uri???selection??????????????????????????????
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
//    //????????????
//    private void displayImage(String imagePath){
//        if(imagePath!=null && !imagePath.equals("")){
//            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//            use_image.setImageBitmap(bitmap);
//            //??????????????????????????????????????????????????????app????????????
//            SharedPreferences sp = getSharedPreferences("sp_img",MODE_PRIVATE);  //??????xml?????????????????????name:?????????xml?????????
//            SharedPreferences.Editor editor = sp.edit(); //??????edit()
//            editor.putString("imgPath",imagePath);
//            editor.apply();
//        }else {
//            Toast.makeText(this,"??????????????????",Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        //????????????app??????????????????
//        SharedPreferences sp = getSharedPreferences("sp_img", MODE_PRIVATE);
//        //????????????????????????????????????????????????????????????
//        String beforeImagePath = sp.getString("imgPath", null);
//        displayImage(beforeImagePath);
//    }