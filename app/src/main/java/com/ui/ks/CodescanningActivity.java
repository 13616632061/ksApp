package com.ui.ks;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.ui.adapter.RotationchartAdapter;
import com.ui.entity.Carousel;
import com.ui.listview.PagingListView;
import com.ui.util.CustomRequest;
import com.ui.util.DialogUtils;
import com.ui.util.GetImagePath;
import com.ui.util.SelectPicPopupWindow;
import com.ui.util.SysUtils;
import com.ui.util.UploadUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2020/3/7.
 */

public class CodescanningActivity extends BaseActivity implements View.OnClickListener {


    PagingListView lv_content;
    Button btn_add;

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
    private String image_id;

    private List<Carousel> datas=new ArrayList<>();
    RotationchartAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codescanning);
        initToolbar(this);
        initView();

        LoadDatas();

    }

    //加载数据
    private void LoadDatas() {
        String url=SysUtils.getnewsellerUrl("Menu/Carousel");
        CustomRequest r=new CustomRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject ret=SysUtils.didResponse(response);
                Log.d("print", "onResponse: 打印数据的轮播图"+response);
                String status = null;
                try {
                    status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject data=null;
                    data=ret.getJSONObject("data");
                    JSONArray list=data.getJSONArray("list");
                    if(!status.equals("200")){
                        SysUtils.showError(message);
                    }else {
                        analysis(list);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
            }
        });
        executeRequest(r);
    }

    //解析
    public void analysis(JSONArray data){
        try {
            for (int i = 0; i < data.length(); i++) {
                JSONObject object = data.getJSONObject(i);
                Carousel carousel = new Carousel();
                carousel.setId(Integer.parseInt(object.getString("id")));
                carousel.setHref(object.getString("href"));
                carousel.setImage_id(object.getString("image_id"));
                carousel.setSort_num(Integer.parseInt(object.getString("sort_num")));
                carousel.setUrl(object.getString("url"));
                carousel.setTitle(object.getString("title"));
                datas.add(carousel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            adapter=new RotationchartAdapter(CodescanningActivity.this,datas);
            lv_content.setAdapter(adapter);
        }
    }

    //初始化控件
    private void initView() {
        lv_content= (PagingListView) findViewById(R.id.lv_content);
        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                DialogUtils.ShowAddRotationchart(CodescanningActivity.this,photoUri,datas.get(i));
                DialogUtils dialogUtils=new DialogUtils();
                dialogUtils.SetOnAddRotationchart(new DialogUtils.OnAddRotationchart() {
                    @Override
                    public void AddImagview() {
                    }
                    @Override
                    public void DeleteRotationchart(String id, String title, String href, String sort_num) {
                        updatas(id,title,href,sort_num,datas.get(i).getImage_id(),true);
                        datas.remove(i);
                        adapter =new RotationchartAdapter(CodescanningActivity.this,datas);
                        lv_content.setAdapter(adapter);
                    }
                    @Override
                    public void AddAddRotationchart(String id, String title, String href, String sort_num) {
                        updatas("0",title,href,"1",image_id,false);
                    }
                });
            }
        });
        btn_add= (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
    }

    //添加轮播图
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add:
//                Intent intent=new Intent(CodescanningActivity.this,AddImageViewDialog.class);
//                startActivityForResult(intent,202);
                if (datas.size()<=5){
                    getSelectPicPopupWindow();
                    mSelectPicPopupWindow.showAtLocation(findViewById(R.id.btn_add) , Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                }else {
                    SysUtils.showSuccess("轮播图不能超过5个");
                }
                break;
        }
    }

    /**
     * 弹出拍照，从相册获取添加图片的窗口
     */
    private void getSelectPicPopupWindow(){
        mSelectPicPopupWindow=new SelectPicPopupWindow(CodescanningActivity.this, new View.OnClickListener() {
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
                            int checkwritefile= ContextCompat.checkSelfPermission(CodescanningActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            if(checkwritefile != PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions(CodescanningActivity.this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},223);
                                return;
                            } else {
                                creatfile();
                            }
                            //动态添加拍照权限
                            int checkCallPhonePermission = ContextCompat.checkSelfPermission(CodescanningActivity.this, android.Manifest.permission.CAMERA);
                            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions(CodescanningActivity.this,new String[]{android.Manifest.permission.CAMERA},222);
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
            bit = UploadUtil.getBitmapFormUri(CodescanningActivity.this, uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path = GetImagePath.getPath(CodescanningActivity.this, uri);
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
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode==222){
                if (resultCode == PackageManager.PERMISSION_GRANTED) {
                    onOpenCamera();
                } else {
                    Toast.makeText(CodescanningActivity.this, "很遗憾你把相机权限禁用了。请务必开启相机权限享受我们提供的服务吧。", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            if(requestCode==223){
                if (resultCode == PackageManager.PERMISSION_GRANTED) {
                    creatfile();
                } else {
                    Toast.makeText(CodescanningActivity.this, "很遗憾你把读写权限禁用了。请务必开启相机权限享受我们提供的服务吧。", Toast.LENGTH_SHORT)
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
                    bit = UploadUtil.getBitmapFormUri(CodescanningActivity.this, pick_photo);
                    String path = GetImagePath.getPath(CodescanningActivity.this, pick_photo);
                    myCaptureFile = UploadUtil.saveFile(bit,path+".jpg");
                    photoUri = Uri.fromFile(myCaptureFile);
                    startPhotoZoom(photoUri,INTENT_BTN_PICK_PHOTO);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("ee="+e.toString());
                }
                upImageAsyTack upImageAsyTack=new upImageAsyTack();
                upImageAsyTack.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

//拍照获取图片并显示
            if (requestCode == INTENT_BTN_TAKE_PHOTO) {
                mSelectPicPopupWindow.dismiss();
                isTakePhoto=2;
                try {
                    bit=  UploadUtil.getBitmapFormUri(CodescanningActivity.this,uri);
                    myCaptureFile= UploadUtil.saveFile(bit,mFilePath+".jpg");
                    photoUri = Uri.fromFile(myCaptureFile);
                    startPhotoZoom(photoUri,INTENT_BTN_TAKE_PHOTO);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                upImageAsyTack upImageAsyTack=new upImageAsyTack();
                upImageAsyTack.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                //            if(requestCode ==PHOTO_CROP_CODE){
//                AddGoodsActivity.upImageAsyTack upImageAsyTack=new AddGoodsActivity.upImageAsyTack();
//                upImageAsyTack.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            }
            }
            DialogUtils.ShowAddRotationchart(CodescanningActivity.this,photoUri,null);
            DialogUtils dialogUtils=new DialogUtils();
            dialogUtils.SetOnAddRotationchart(new DialogUtils.OnAddRotationchart() {
                @Override
                public void AddImagview() {
                }
                @Override
                public void DeleteRotationchart(String id, String title, String href, String sort_num) {
//                    updatas(id,title,href,sort_num,true);
                }
                @Override
                public void AddAddRotationchart(String id, String title, String href, String sort_num) {
                    updatas("0",title,href,(datas.size()+1)+"",image_id,false);
                }
            });
        }
    }


    public void updatas(String id, String title, String href, String sort_num, String image_id, final boolean isdelete){
        Map<String,String> map=new HashMap<>();
        map.put("id",id);
        map.put("image_id",image_id);
        map.put("title",title);
        map.put("href",href);
        map.put("sort_num",sort_num);
        if (isdelete){
            map.put("type","1");
        }else {
            map.put("type","0");
        }
        Log.d("print","打印上传轮播图的数据"+map.toString());
        String url=SysUtils.getnewsellerUrl("Menu/updateCarousel");
        CustomRequest r=new CustomRequest(CustomRequest.Method.POST, url, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideLoading();
                Log.d("print","打印上传轮播图的数据"+response.toString());
                JSONObject ret=SysUtils.didResponse(response);
                String status = null;
                try {
                    status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject  data=null;
                    if(!status.equals("200")){
                        SysUtils.showError(message);
                    }else {
                        if (isdelete){
                            SysUtils.showSuccess("删除成功");
                        }else {
                            SysUtils.showSuccess("添加成功");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
            }
        });
        showLoading(CodescanningActivity.this,"正在上传数据...");
        executeRequest(r);
    }

    /**
     * 上传图片轮播图
     */
    class  upImageAsyTack extends AsyncTask<Void,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading(CodescanningActivity.this);
        }

        @Override
        protected String doInBackground(Void... params) {
            Log.d("print", "doInBackground: 打印上传图片的url"+requrl);
//            String str = UploadUtil.uploadFile( myCaptureFile, requrl, "photo");
            Map<String,String> map=new HashMap<>();
            map.put("type","carousel");
            String str = UploadUtil.uploadFile( myCaptureFile, requrl, "file",map);
            if (str != null) {
                try {
                    System.out.println("添加商品图片到七牛云" + str);
                    JSONObject jsonObject = new JSONObject(str);
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("添加商品图片ret=" + ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject data = ret.getJSONObject("data");
                    JSONObject object=data.getJSONObject("list");
                    image_id = object.getString("image_id");
                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return image_id;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            hideLoading();
//            if (s != null) {
//                if (isTakePhoto == 2) {
//                    showUriImage(photoUri);
//                }
//                if (isTakePhoto == 1) {
//                    if (pick_photo != null) {
//                        showUriImage(photoUri);
//                    }
//                }
//            }else {
//                SysUtils.showError("图片上传失败");
//            }
            if( myCaptureFile.exists()){
                myCaptureFile.delete();  //删除原图片
            }
        }
    }

}
