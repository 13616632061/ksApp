package com.ui.ks;

import android.Manifest;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.material.widget.PaperButton;
import com.ui.util.CustomRequest;
import com.ui.util.DeleteEditText;
import com.ui.util.DialogUtils;
import com.ui.util.GetImagePath;
import com.ui.util.SelectPicPopupWindow;
import com.ui.util.StringUtils;
import com.ui.util.SysUtils;
import com.ui.util.UploadUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends BaseActivity implements View.OnClickListener {
    //收件人
    TextView imageView1;
    EditText textView2;

    //手机号
    TextView textView3;
    EditText textView4;

    //邮编
    TextView textView5;
    EditText textView6;

    //省
    RelativeLayout relativeLayout1;
    TextView textView8;

    //市
//    RelativeLayout relativeLayout2;
//    TextView textView12;

    //县
//    RelativeLayout relativeLayout3;
//    TextView textView14;

    //详细地址
    TextView textView9;
    EditText textView10;

    private int provinceId = 0, cityId = 0, areaId=  0;
    private String area = "";

    private boolean hasPass = false;

    private TextView  yh, kh;
    private TextView khr,branchbank;
    private TextView phone;
    private ImageView logo, yyzz, wsxkz, qtzp;
    private DisplayImageOptions options;
    private RelativeLayout logo_rl, yyzz_rl, wsxkz_rl, qt_rl;

    private ImageView iv_bank_photo,iv_front_card,iv_side_card,iv_door_photo,iv_shop_photo,iv_cashier_photo;

    private String logoV = "";
    private String businessV = "";
    private String healthV = "";
    private String otherV = "";
    private String khr_str="";
    private String yh_str="";
    private String kh_str="";
    private String branchbank_str="";

    private String bank_card_img="";
    private String identity_face_img="";
    private String identity_back_img="";
    private String door_img="";
    private String store_img="";
    private String cashier_img="";

    private  int INTENT_BTN_PICK_PHOTO=200;
    private  int INTENT_BTN_TAKE_PHOTO=201;
    private SelectPicPopupWindow mSelectPicPopupWindow;
    final String requrl=SysUtils.getUploadImageServiceUrl("fileup");
    final String requrl1=SysUtils.getUploadImageServiceUrl("imageup");
    private String strname;
    private ImageView mShowImageView;
    // 获取SD卡路径
    private String mFilePath;
    private Uri uri;;
    private FileInputStream is = null;

    private LinearLayout layout_khr;
    private LinearLayout layout_yh;
    private LinearLayout layout_branchbank;
    private LinearLayout layout_bankcard;
    private LinearLayout layout_phone;
    private  boolean isImage=false;
    private RelativeLayout rl_photo1,rl_photo2,rl_photo3,rl_photo4,rl_photo5,rl_photo6;
    //判断是那个接口上传的
    private boolean isurl=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SysUtils.setupUI(this, findViewById(R.id.main));

        initToolbar(this);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("hasPass")) {
                hasPass = bundle.getBoolean("hasPass");
            }
        }

        if(!hasPass) {
            SysUtils.startAct(this, new ProfileEntryActivity());
            finish();
        }
        initView();
        imageView1 = (TextView) findViewById(R.id.imageView1);
        textView2 = (EditText) findViewById(R.id.textView2);    //姓名
        new DeleteEditText(textView2, imageView1);

        options = SysUtils.imageOption(false);

        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (EditText) findViewById(R.id.textView4);    //手机号
        new DeleteEditText(textView4, textView3);

        textView5 = (TextView) findViewById(R.id.textView5);
        textView6 = (EditText) findViewById(R.id.textView6);    //固定电话
        new DeleteEditText(textView6, textView5);

        khr = (TextView) findViewById(R.id.khr);
        yh = (TextView)findViewById(R.id.yh);
        kh = (TextView)findViewById(R.id.kh);
        branchbank=(TextView) findViewById(R.id.branchbank);
        phone=(TextView) findViewById(R.id.phone);

        layout_khr=(LinearLayout) findViewById(R.id.layout_khr);
        layout_yh=(LinearLayout) findViewById(R.id.layout_yh);
        layout_branchbank=(LinearLayout) findViewById(R.id.layout_branchbank);
        layout_bankcard=(LinearLayout) findViewById(R.id.layout_bankcard);
        layout_phone=(LinearLayout) findViewById(R.id.layout_phone);
        //点击开户人
        layout_khr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.editAccountDialog(ProfileActivity.this,getString(R.string.str159),khr);//添加开户人
            }
        });
        //点击开户银行
        layout_yh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.editAccountDialog(ProfileActivity.this,getString(R.string.str264),yh);//添加开户银行
            }
        });
        //点击开户支行
        layout_branchbank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.editAccountDialog(ProfileActivity.this,getString(R.string.str265),branchbank);//添加开户支行
            }
        });
        //点击卡号
        layout_bankcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.editAccountDialog(ProfileActivity.this,getString(R.string.str266),kh);//添加银行卡号
            }
        });
        layout_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtils.editAccountDialog(ProfileActivity.this,getString(R.string.str267),phone);//添加预留手机号
            }
        });
        logo = (ImageView)findViewById(R.id.logo);
        yyzz = (ImageView)findViewById(R.id.yyzz);
        wsxkz = (ImageView)findViewById(R.id.wsxkz);
        qtzp = (ImageView)findViewById(R.id.qtzp);
        iv_bank_photo = (ImageView)findViewById(R.id.iv_bank_photo);
        iv_front_card = (ImageView)findViewById(R.id.iv_front_card);
        iv_side_card = (ImageView)findViewById(R.id.iv_side_card);
        iv_door_photo = (ImageView)findViewById(R.id.iv_door_photo);
        iv_shop_photo = (ImageView)findViewById(R.id.iv_shop_photo);
        iv_cashier_photo = (ImageView)findViewById(R.id.iv_cashier_photo);


        logo_rl = (RelativeLayout) findViewById(R.id.logo_rl);
        //判断是否有图片，如没有图片这调用弹窗添加图片
        logo_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(logoV)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("pic_list", logoV);
                    bundle.putInt("offset", 0);

                    SysUtils.startAct(ProfileActivity.this, new PicViewActivity(), bundle);
                    ((BaseActivity) ProfileActivity.this).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else {
                    isurl=true;
                    strname="logo";
                    mShowImageView=logo;
                    getSelectPicPopupWindow();
                    mSelectPicPopupWindow.showAtLocation(findViewById(R.id.logo_rl) , Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            }
        });

        yyzz_rl = (RelativeLayout) findViewById(R.id.yyzz_rl);
        yyzz_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(businessV)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("pic_list", businessV);
                    bundle.putInt("offset", 0);

                    SysUtils.startAct(ProfileActivity.this, new PicViewActivity(), bundle);
                    ((BaseActivity) ProfileActivity.this).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else {
                    isurl=true;
                    strname="business";
                    mShowImageView=yyzz;
                    getSelectPicPopupWindow();
                    mSelectPicPopupWindow.showAtLocation(findViewById(R.id.yyzz_rl) , Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            }
        });

        wsxkz_rl = (RelativeLayout) findViewById(R.id.wsxkz_rl);
        wsxkz_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(healthV)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("pic_list", healthV);
                    bundle.putInt("offset", 0);

                    SysUtils.startAct(ProfileActivity.this, new PicViewActivity(), bundle);
                    ((BaseActivity) ProfileActivity.this).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else {
                    isurl=true;
                    strname="health";
                    mShowImageView=wsxkz;
                    getSelectPicPopupWindow();
                    mSelectPicPopupWindow.showAtLocation(findViewById(R.id.wsxkz_rl) , Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            }
        });

        qt_rl = (RelativeLayout) findViewById(R.id.qt_rl);
        qt_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(otherV)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("pic_list", otherV);
                    bundle.putInt("offset", 0);

                    SysUtils.startAct(ProfileActivity.this, new PicViewActivity(), bundle);
                    ((BaseActivity) ProfileActivity.this).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else {
                    isurl=true;
                     strname="other";
                    mShowImageView=qtzp;
                    getSelectPicPopupWindow();
                    mSelectPicPopupWindow.showAtLocation(findViewById(R.id.wsxkz_rl) , Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            }
        });


        //省
        relativeLayout1 = (RelativeLayout) findViewById(R.id.relativeLayout1);
        textView8 = (TextView) findViewById(R.id.textView8);
        relativeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", 0);
                bundle.putInt("pid", 0);

                SysUtils.startAct(ProfileActivity.this, new AddressLocationActivity(), bundle, true);
            }
        });

        textView9 = (TextView) findViewById(R.id.textView9);
        textView10 = (EditText) findViewById(R.id.textView10);    //详细地址
        new DeleteEditText(textView10, textView9);


        PaperButton button1 = (PaperButton) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = textView2.getText().toString();
                if( !isImage){
                    SysUtils.showError(getString(R.string.str268));//图片至少上传一张
                    return;
                }
                if(TextUtils.isEmpty(khr.getText().toString().trim())||khr.getText().toString().trim().equals("null")){
                    SysUtils.showError(getString(R.string.str269));//请填写开户人
                    return;
                }
                if(TextUtils.isEmpty(yh.getText().toString().trim())||yh.getText().toString().trim().equals("null")){
                    SysUtils.showError(getString(R.string.str270));//请填写银行
                    return;
                }
                if(TextUtils.isEmpty(branchbank.getText().toString().trim())||branchbank.getText().toString().trim().equals("null")){
                    SysUtils.showError(getString(R.string.str271));//请填写开户支行
                    return;
                }
                if(TextUtils.isEmpty(kh.getText().toString().trim())||kh.getText().toString().trim().equals("null")){
                    SysUtils.showError(getString(R.string.str272));//请填写卡号
                    return;
                }
                if(StringUtils.isEmpty(name)||name.equals("null")) {
                    SysUtils.showError(getString(R.string.str273));//请填写姓名
                } else {
                    String mobile = textView4.getText().toString();
                    String phone = textView6.getText().toString();
                    if(StringUtils.isEmpty(mobile) && StringUtils.isEmpty(phone)||mobile.equals("null")) {
                        SysUtils.showError(getString(R.string.str274));//手机号码和固定电话必填其一
                    } else {

                        if(provinceId <= 0) {
                            SysUtils.showError(getString(R.string.str275));//请选择所在地区
                        } else {
                            String address = textView10.getText().toString();
                            if(StringUtils.isEmpty(address)||address.equals("null")) {
                                SysUtils.showError(getString(R.string.str276));//请填写详细地址
                            } else {
                                Map<String,String> map = new HashMap<String,String>();
                                map.put("name", name);
                                map.put("mobile", mobile);
                                map.put("tel", phone);
                                map.put("area", getPostArea());
                                map.put("addr", address);
                                map.put("khr", khr.getText().toString().trim());
                                map.put("acbank", yh.getText().toString().trim());
                                map.put("bankcard", kh.getText().toString().trim());
                                map.put("branchbank", branchbank.getText().toString().trim());

                                CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("doAccount"), map, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject jsonObject) {
                                        hideLoading();

                                        try {
                                            JSONObject ret = SysUtils.didResponse(jsonObject);
                                            String status = ret.getString("status");
                                            String message = ret.getString("message");

                                            if (!status.equals("200")) {
                                                SysUtils.showError(message);
                                            } else {
                                                SysUtils.showSuccess(getString(R.string.str277));//修改已保存
                                                finish();
                                            }
                                        } catch(Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        hideLoading();
                                        SysUtils.showNetworkError();
                                    }
                                });

                                executeRequest(r);

                                showLoading(ProfileActivity.this);
                            }
                        }
                    }
                }
            }
        });
        rl_photo1=(RelativeLayout)findViewById(R.id.rl_photo1);
        rl_photo1.setOnClickListener(this);
        rl_photo2=(RelativeLayout)findViewById(R.id.rl_photo2);
        rl_photo2.setOnClickListener(this);
        rl_photo3=(RelativeLayout)findViewById(R.id.rl_photo3);
        rl_photo3.setOnClickListener(this);
        rl_photo4=(RelativeLayout)findViewById(R.id.rl_photo4);
        rl_photo4.setOnClickListener(this);
        rl_photo5=(RelativeLayout)findViewById(R.id.rl_photo5);
        rl_photo5.setOnClickListener(this);
        rl_photo6=(RelativeLayout)findViewById(R.id.rl_photo6);
        rl_photo6.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                if(requestCode==1){
                Bundle b = data.getExtras();
                if (b != null && b.containsKey("provinceId") && b.containsKey("cityId") && b.containsKey("townId") && b.containsKey("areaStr")) {
                    provinceId = b.getInt("provinceId");
                    cityId = b.getInt("cityId");
                    areaId = b.getInt("townId");
                    area = b.getString("areaStr");

                    textView8.setText(area);
                }

                }
                if(requestCode==222){
                    if (resultCode == PackageManager.PERMISSION_GRANTED) {
                        onOpenCamera();
                    } else {
                        //很遗憾你把权限禁用了。请务必开启相机权限享受我们提供的服务吧。
                        Toast.makeText(ProfileActivity.this, getString(R.string.str278), Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                if(requestCode==223){
                    if (resultCode == PackageManager.PERMISSION_GRANTED) {
                        creatfile();
                    } else {
                        //很遗憾你把权限禁用了。请务必开启相机权限享受我们提供的服务吧。
                        Toast.makeText(ProfileActivity.this, getString(R.string.str278), Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                //从相册获取图片并显示
                if (requestCode == INTENT_BTN_PICK_PHOTO) {
                    mSelectPicPopupWindow.dismiss();
                    Uri pick_photo = data.getData();
                    showUriImage(pick_photo);
                    Bitmap bit=null;
                    File myCaptureFile=null;
                    try {
                        bit = UploadUtil.getBitmapFormUri(ProfileActivity.this, pick_photo);
                        String path = GetImagePath.getPath(ProfileActivity.this, pick_photo);
                        myCaptureFile = UploadUtil.saveFile(bit,path);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    String path = GetImagePath.getPath(ProfileActivity.this, pick_photo);
//                    final File file = new File(path);

                    upImageThread(myCaptureFile);
                    isImage=true;

                }
                //拍照获取图片并显示
                if (requestCode == INTENT_BTN_TAKE_PHOTO) {
                    mSelectPicPopupWindow.dismiss();
                    Bitmap bit=null;
                    File myCaptureFile=null;
                    try {
                         bit=  UploadUtil.getBitmapFormUri(ProfileActivity.this,uri);
                        mShowImageView.setImageBitmap(bit);
                        myCaptureFile= UploadUtil.saveFile(bit,mFilePath);
                        isImage=true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("生成图片失败："+e.toString());
                    }
                    upImageThread(myCaptureFile);
            }
            }
    }

    /**
     *创建存储拍照图片的文件夹
     */
    private void creatfile(){
        String name = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        String str_path=getSDPath()+"/image";
        File file=new File(str_path.trim());
        //判断文件夹是否存在,如果不存在则创建文件夹
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 获取SD卡路径
        mFilePath= str_path+ name + ".jpg";
        uri=Uri.fromFile(new File(mFilePath));;
    }
    /**
     * 调用相机拍照
     */
    private void onOpenCamera() {
        // 加载路径
        // 指定存储路径，这样就可以保存原图了
        Intent intent_btn_take_photo=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent_btn_take_photo.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent_btn_take_photo,INTENT_BTN_TAKE_PHOTO);
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

    private void initView() {
        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("account"), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    Log.e("print","打印获取数据"+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject dataObject = ret.getJSONObject("data");

                    if (!status.equals("200")) {
                        SysUtils.showError(message);
                    } else {
                        JSONObject sellerObject = dataObject.getJSONObject("seller_info");

                        textView2.setText(sellerObject.getString("seller_name"));
                        textView4.setText(sellerObject.getString("mobile"));
                        textView6.setText(sellerObject.getString("tel"));

                        JSONObject areaObject = sellerObject.getJSONObject("area");

                        area = areaObject.getString("area");
                        textView8.setText(area);

                        getAreaId(areaObject.getString("area_id"));
                        textView10.setText(sellerObject.getString("addr"));
                        khr_str=sellerObject.optString("khr");
                        //开户人，银行，开户支行，卡号为空时可以编辑，有数据不可编辑
                        if(!TextUtils.isEmpty(khr_str)&&!khr_str.equals("null")) {
                            layout_khr.setOnClickListener(null);
                        }
                        khr.setText( khr_str);
                        yh_str=sellerObject.optString("acbank");
                        if(!TextUtils.isEmpty( yh_str)&&!yh_str.equals("null")) {
                            layout_yh.setOnClickListener(null);
                        }
                        yh.setText( yh_str);
                        kh_str=sellerObject.optString("bankcard");
                        if(!TextUtils.isEmpty( kh_str)&&!kh_str.equals("null")) {
                            layout_bankcard.setOnClickListener(null);
                        }
                        kh.setText(kh_str);
                        branchbank_str=sellerObject.optString("branchbank");
                        if(!TextUtils.isEmpty( branchbank_str)&&!branchbank_str.equals("null")) {
                            layout_branchbank.setOnClickListener(null);
                        }
                        branchbank.setText(branchbank_str);

                        logoV = sellerObject.optString("logo");
                        if(!TextUtils.isEmpty(logoV)) {
                            isImage=true;
                            imageLoader.displayImage(logoV, logo, options);
                        }
                        businessV = sellerObject.optString("business");
                        if(!TextUtils.isEmpty(businessV)) {
                            isImage=true;
                            imageLoader.displayImage(businessV, yyzz, options);
                        }
                        healthV = sellerObject.optString("health");
                        if(!TextUtils.isEmpty(healthV)) {
                            isImage=true;
                            imageLoader.displayImage(healthV, wsxkz, options);
                        }
                        otherV = sellerObject.optString("other");
                        if(!TextUtils.isEmpty(otherV)) {
                            isImage=true;
                            imageLoader.displayImage(otherV, qtzp, options);
                        }
                        bank_card_img=sellerObject.optString("bank_card_img");
                        if (!TextUtils.isEmpty(bank_card_img)&&!bank_card_img.equals("null")){
                            isImage=true;
                            imageLoader.displayImage(bank_card_img, iv_bank_photo, options);
                        }
                        identity_face_img=sellerObject.optString("identity_face_img");
                        if (!TextUtils.isEmpty(identity_face_img)&&!identity_face_img.equals("null")){
                            isImage=true;
                            imageLoader.displayImage(identity_face_img, iv_front_card, options);
                        }
                        identity_back_img=sellerObject.optString("identity_back_img");
                        if (!TextUtils.isEmpty(identity_back_img)&&!identity_back_img.equals("null")){
                            isImage=true;
                            imageLoader.displayImage(identity_back_img, iv_side_card, options);
                        }
                        door_img=sellerObject.optString("door_img");
                        if (!TextUtils.isEmpty(door_img)&&!door_img.equals("null")){
                            isImage=true;
                            imageLoader.displayImage(door_img, iv_door_photo, options);
                        }
                        store_img=sellerObject.optString("store_img");
                        if (!TextUtils.isEmpty(store_img)&&!store_img.equals("null")){
                            isImage=true;
                            imageLoader.displayImage(store_img, iv_shop_photo, options);
                        }
                        cashier_img=sellerObject.optString("cashier_img");
                        if (!TextUtils.isEmpty(cashier_img)&&!cashier_img.equals("null")){
                            isImage=true;
                            imageLoader.displayImage(cashier_img, iv_cashier_photo, options);
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                SysUtils.showNetworkError();
                hideLoading();
            }
        });
        executeRequest(r);
        showLoading(this);
    }


    private String getPostArea() {
        String ret = "mainland";
        ret += ":" + area;
        if(areaId > 0) {
            ret += ":" + areaId;
        } else if(cityId > 0) {
            ret += ":" + cityId;
        } else if(provinceId > 0) {
            ret += ":" + provinceId;
        }

//        SysUtils.showSuccess(ret);

        return ret;
    }

    private void getAreaId(String area_id) {
        String[] aa = area_id.split(",");

        int aIndex = 0;
        for (int  i = 0; i < aa.length; i++) {
            if (!StringUtils.isEmpty(aa[i])) {
                int aid = Integer.parseInt(aa[i]);

                if (aid > 0) {
                    if (aIndex == 0) {
                        provinceId = aid;
                    } else if(aIndex == 1) {
                        cityId = aid;
                    } else if(aIndex == 2) {
                        areaId= aid;
                    }
                    aIndex++;
                }

            }
        }
    }

    /**
     * 弹出拍照，从相册获取添加图片的窗口
     */
    private void getSelectPicPopupWindow(){
        mSelectPicPopupWindow=new SelectPicPopupWindow(ProfileActivity.this, new View.OnClickListener() {
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
                            int checkwritefile=ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            if(checkwritefile != PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions(ProfileActivity.this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},223);
                                return;
                            } else {
                                    creatfile();
                            }
                            //动态添加拍照权限
                            int checkCallPhonePermission = ContextCompat.checkSelfPermission(ProfileActivity.this, android.Manifest.permission.CAMERA);
                            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions(ProfileActivity.this,new String[]{android.Manifest.permission.CAMERA},222);
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
     * 上传图片
     * @param file
     */
    private void  upImageThread(final File file){
        new Thread(new Runnable(){
            @Override
            public void run() {
                String  str="";
                if (isurl){
                    str= UploadUtil.uploadFile(file,requrl,strname);
                }else {
                    str= UploadUtil.uploadFile(file,requrl1,strname);
                    Log.d("上传的数据","打印上传的数据"+str);
                }


            }
        }).start();
    }

    /**
     * uri格式的图片显示
     * @param uri
     */
    private void showUriImage(Uri uri){
        mShowImageView.setImageURI(uri);
    }

    /**
     * Bitmap格式的图片显示
     * @param b
     */
    private void showBitmapImage(Bitmap b){
        mShowImageView.setImageBitmap(b);
    }

    //选择图片
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_photo1:
                if(!TextUtils.isEmpty(bank_card_img)&&!bank_card_img.equals("null")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("pic_list", bank_card_img);
                    bundle.putInt("offset", 0);
                    SysUtils.startAct(ProfileActivity.this, new PicViewActivity(), bundle);
                    ((BaseActivity) ProfileActivity.this).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else {
                    isurl = false;
                    strname = "bank_card_img";
                    mShowImageView = iv_bank_photo;
                    getSelectPicPopupWindow();
                    mSelectPicPopupWindow.showAtLocation(findViewById(R.id.logo_rl), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
                break;
            case R.id.rl_photo2:
                if(!TextUtils.isEmpty(identity_face_img)&&!identity_face_img.equals("null")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("pic_list", identity_face_img);
                    bundle.putInt("offset", 0);
                    SysUtils.startAct(ProfileActivity.this, new PicViewActivity(), bundle);
                    ((BaseActivity) ProfileActivity.this).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else {
                    isurl = false;
                    strname = "identity_face_img";
                    mShowImageView = iv_front_card;
                    getSelectPicPopupWindow();
                    mSelectPicPopupWindow.showAtLocation(findViewById(R.id.logo_rl), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
                break;
            case R.id.rl_photo3:
                if(!TextUtils.isEmpty(identity_back_img)&&!identity_back_img.equals("null")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("pic_list", identity_back_img);
                    bundle.putInt("offset", 0);

                    SysUtils.startAct(ProfileActivity.this, new PicViewActivity(), bundle);
                    ((BaseActivity) ProfileActivity.this).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else {
                    isurl = false;
                    strname = "identity_back_img";
                    mShowImageView = iv_side_card;
                    getSelectPicPopupWindow();
                    mSelectPicPopupWindow.showAtLocation(findViewById(R.id.logo_rl), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
                break;
            case R.id.rl_photo4:
                if(!TextUtils.isEmpty(door_img)&&!door_img.equals("null")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("pic_list", door_img);
                    bundle.putInt("offset", 0);

                    SysUtils.startAct(ProfileActivity.this, new PicViewActivity(), bundle);
                    ((BaseActivity) ProfileActivity.this).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else {
                    isurl = false;
                    strname = "door_img";
                    mShowImageView = iv_door_photo;
                    getSelectPicPopupWindow();
                    mSelectPicPopupWindow.showAtLocation(findViewById(R.id.logo_rl), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
                break;
            case R.id.rl_photo5:
                if(!TextUtils.isEmpty(store_img)&&!store_img.equals("null")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("pic_list", store_img);
                    bundle.putInt("offset", 0);

                    SysUtils.startAct(ProfileActivity.this, new PicViewActivity(), bundle);
                    ((BaseActivity) ProfileActivity.this).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else {
                    isurl = false;
                    strname = "store_img";
                    mShowImageView = iv_shop_photo;
                    getSelectPicPopupWindow();
                    mSelectPicPopupWindow.showAtLocation(findViewById(R.id.logo_rl), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
                break;
            case R.id.rl_photo6:
                if(!TextUtils.isEmpty(cashier_img)&&!cashier_img.equals("null")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("pic_list", cashier_img);
                    bundle.putInt("offset", 0);

                    SysUtils.startAct(ProfileActivity.this, new PicViewActivity(), bundle);
                    ((BaseActivity) ProfileActivity.this).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else {
                    isurl = false;
                    strname = "cashier_img";
                    mShowImageView = iv_cashier_photo;
                    getSelectPicPopupWindow();
                    mSelectPicPopupWindow.showAtLocation(findViewById(R.id.logo_rl), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
                break;
        }
    }
}
