package com.ui.ks;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.base.BaseActivity;
import com.ui.util.GetImagePath;
import com.ui.util.SelectPicPopupWindow;
import com.ui.util.SysUtils;
import com.ui.util.UploadUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2020/3/9.
 */

public class AddImageViewDialog extends BaseActivity implements View.OnClickListener {

    ImageView iv_goodpicture;
    EditText ed_describe;
    EditText ed_link;
    Button btn_delete;
    Button btn_preservation;

    private SelectPicPopupWindow mSelectPicPopupWindow;
    private  int INTENT_BTN_PICK_PHOTO=200;
    private  int INTENT_BTN_TAKE_PHOTO=201;
    private  int PHOTO_CROP_CODE=203;//图片裁剪
    private  int INTENT_GOODS_SORT=202;
    private String mFilePath;
    private Uri uri;
    private Uri photoUri;
    //    private final String requrl=SysUtils.getUploadImageServiceUrl("fileup");
    private final String requrl= SysUtils.getnewsellerUrl("Helper/uploadImg");
    private File myCaptureFile=null;
    private Uri pick_photo;
    private Bitmap bit=null;
    private int isTakePhoto=1;//1表示从相册获取图片，2表示拍照获取


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addrotationchart);
        initView();
    }

    private void initView() {
        iv_goodpicture= (ImageView) findViewById(R.id.iv_goodpicture);
        iv_goodpicture.setOnClickListener(this);
        ed_describe= (EditText) findViewById(R.id.ed_describe);
        ed_link= (EditText) findViewById(R.id.ed_link);
        btn_delete= (Button) findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(this);
        btn_preservation= (Button) findViewById(R.id.btn_preservation);
        btn_preservation.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_goodpicture:
                getSelectPicPopupWindow();
                mSelectPicPopupWindow.showAtLocation(findViewById(R.id.ll) , Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);

                break;
            case R.id.btn_delete:
                break;
            case R.id.btn_preservation:
                break;
        }
    }

    /**
     * 弹出拍照，从相册获取添加图片的窗口
     */
    private void getSelectPicPopupWindow(){
        mSelectPicPopupWindow=new SelectPicPopupWindow(AddImageViewDialog.this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_pick_photo:
                        //从相册获取图片
                        Intent intent_btn_pick_photo=new Intent();
                        intent_btn_pick_photo.setType("image/*");
                        intent_btn_pick_photo.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent_btn_pick_photo,INTENT_BTN_PICK_PHOTO);
                        break;
                    case R.id.btn_take_photo:
                        if (Build.VERSION.SDK_INT >= 23) {
                            //动态添加sdk写入权限，主要适配与android6.0以上的系统
                            int checkwritefile= ContextCompat.checkSelfPermission(AddImageViewDialog.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            if(checkwritefile != PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions(AddImageViewDialog.this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},223);
                                return;
                            } else {
                                creatfile();
                            }
                            //动态添加拍照权限
                            int checkCallPhonePermission = ContextCompat.checkSelfPermission(AddImageViewDialog.this, android.Manifest.permission.CAMERA);
                            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions(AddImageViewDialog.this,new String[]{android.Manifest.permission.CAMERA},222);
                                return;
                            }else{
                                onOpenCamera();
                            }
                        } else {
                            creatfile();
                            onOpenCamera();
                        }
                        break;

                }
            }
        });
    }


    /**
     *创建存储拍照图片的文件夹
     */
    private void creatfile(){
        String name = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        String str_path=getSDPath()+"/image";
//        File file=new File(str_path.trim());
//        //判断文件夹是否存在,如果不存在则创建文件夹
        // 获取SD卡路径
        mFilePath= str_path+ name + ".jpg";
        File file=new File(mFilePath);
//        if (!file.exists()) {
//            try {
//                file.mkdirs();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        uri= Uri.fromFile(new File(mFilePath));;
    }


    /**
     * 调用相机拍照
     */
    private void onOpenCamera() {
        // 加载路径
        // 指定存储路径，这样就可以保存原图了
//        Intent intent_btn_take_photo=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent_btn_take_photo.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        startActivityForResult(intent_btn_take_photo,INTENT_BTN_TAKE_PHOTO);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(
//                new File(Environment.getExternalStorageDirectory(), imgName)));
//        File file = new File(Environment.getExternalStorageDirectory(), imgName);
        if (file != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                uri = Uri.fromFile(file);
            } else {
                uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(cameraIntent, INTENT_BTN_TAKE_PHOTO);
        }
    }
    //    android获取sd卡路径方法：
    public String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.getPath();
    }


    /**
     * @param
     * @description 裁剪图片
     * @author ldm
     * @time 2016/11/30 15:19
     */
    // 定义拍照后存放图片的文件位置和名称，使用完毕后可以方便删除
    File file = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
    private void startPhotoZoom(Uri uri, int type) {
        Bitmap bit = null;
        try {
            bit = UploadUtil.getBitmapFormUri(AddImageViewDialog.this, uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path = GetImagePath.getPath(AddImageViewDialog.this, uri);
        myCaptureFile= UploadUtil.saveFile(bit,path+".jpg");
        photoUri = Uri.fromFile(myCaptureFile);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // 去黑边
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        // aspectX aspectY 是宽高的比例，根据自己情况修改
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高像素
        intent.putExtra("outputX", 800);
        intent.putExtra("outputY", 800);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        //取消人脸识别功能
        intent.putExtra("noFaceDetection", true);
        //设置返回的uri
//        if(type==INTENT_BTN_PICK_PHOTO)
//        {
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        }else {
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        //设置为不返回数据
        intent.putExtra("return-data",false);
        startActivityForResult(intent, PHOTO_CROP_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode==222){
                if (resultCode == PackageManager.PERMISSION_GRANTED) {
                    onOpenCamera();
                } else {
                    Toast.makeText(AddImageViewDialog.this, "很遗憾你把相机权限禁用了。请务必开启相机权限享受我们提供的服务吧。", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            if(requestCode==223){
                if (resultCode == PackageManager.PERMISSION_GRANTED) {
                    creatfile();
                } else {
                    Toast.makeText(AddImageViewDialog.this, "很遗憾你把读写权限禁用了。请务必开启相机权限享受我们提供的服务吧。", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            //从相册获取图片并显示
            if (requestCode == INTENT_BTN_PICK_PHOTO) {
                mSelectPicPopupWindow.dismiss();
                pick_photo = data.getData();
                isTakePhoto=1;
                Bitmap bit=null;
                try {
                    bit = UploadUtil.getBitmapFormUri(AddImageViewDialog.this, pick_photo);
                    String path = GetImagePath.getPath(AddImageViewDialog.this, pick_photo);
                    myCaptureFile = UploadUtil.saveFile(bit,path+".jpg");
                    photoUri = Uri.fromFile(myCaptureFile);
                    startPhotoZoom(photoUri,INTENT_BTN_PICK_PHOTO);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("ee="+e.toString());
                }
//                AddGoodsActivity.upImageAsyTack upImageAsyTack=new CodescanningActivity.upImageAsyTack();
//                upImageAsyTack.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

//拍照获取图片并显示
            if (requestCode == INTENT_BTN_TAKE_PHOTO) {
                mSelectPicPopupWindow.dismiss();
                isTakePhoto=2;
                try {
                    bit=  UploadUtil.getBitmapFormUri(AddImageViewDialog.this,uri);
                    myCaptureFile= UploadUtil.saveFile(bit,mFilePath+".jpg");
                    photoUri = Uri.fromFile(myCaptureFile);
                    startPhotoZoom(photoUri,INTENT_BTN_TAKE_PHOTO);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //            if(requestCode ==PHOTO_CROP_CODE){
//                AddGoodsActivity.upImageAsyTack upImageAsyTack=new AddGoodsActivity.upImageAsyTack();
//                upImageAsyTack.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            }
            }

        }
    }


}
