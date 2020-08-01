package com.ui.ks;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.MyApplication.KsApplication;
import com.base.BaseActivity;
import com.ui.util.BitmapUtils;
import com.ui.util.QRCodeUtil;
import com.ui.util.SetEditTextInput;
import com.ui.util.SysUtils;

import java.io.File;

/**
 * Created by Administrator on 2020/3/22.
 */

public class TablePicActivity extends BaseActivity implements View.OnClickListener {


    EditText ed_table_num;
    ImageView image_table;
    Button btn_add_code;
    Button btn_generate;


    String code="";
    String filePath="";
    RelativeLayout mypaycaode_layout;
    TextView tv_table;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_pic);
        initToolbar(this);
        initView();
    }

    private void initView() {
        ed_table_num= (EditText) findViewById(R.id.ed_table_num);
        image_table = (ImageView) findViewById(R.id.image_table);
        btn_add_code= (Button) findViewById(R.id.btn_add_remarks);
        btn_add_code.setOnClickListener(this);
        btn_generate= (Button) findViewById(R.id.btn_generate);
        btn_generate.setOnClickListener(this);
        tv_table= (TextView) findViewById(R.id.tv_table);
        mypaycaode_layout = (RelativeLayout) findViewById(R.id.mypaycaode_layout);
        SetEditTextInput.setStringFilter(ed_table_num);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_generate:
                if (ed_table_num.getText().toString().equals("")){
                    //请填写桌号
                    Toast.makeText(TablePicActivity.this,getString(R.string.str368),Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (StringUtils.isNumber(ed_table_num.getText().toString())){
                    code=SysUtils.getnewcode()+"index/"+ KsApplication.getInt("seller_id",0)+"/"+ed_table_num.getText().toString();
//                }else {
//                    code=StringUtils.encodeUrl(SysUtils.getnewcode()+"index/"+KsApplication.getInt("seller_id",0)+"/"+ed_table_num.getText().toString());
//                }
                Log.d("print","打印出桌号的链接"+code);
                tv_table.setText(ed_table_num.getText().toString());
//                http://bbs.czxshop.com/index/7006/8
                filePath=QRCodeUtil.getFileRoot(TablePicActivity.this) + File.separator
                        + "qr_" + System.currentTimeMillis() + ".jpg";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean success = QRCodeUtil.createQRImage(code, 800, 800, null, filePath);
                        if (success) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Drawable drawable =new BitmapDrawable(BitmapFactory.decodeFile(filePath));
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        image_table.setBackground(drawable);
                                    }else {
                                        image_table.setImageBitmap(BitmapFactory.decodeFile(filePath));
                                    }
//                            iv_mypaycaode.setImageBitmap(BitmapFactory.decodeFile(filePath));
                                }
                            });
                        }
                    }
                }).start();
                break;
            case R.id.btn_add_remarks:
                if (tv_table.getText().toString().equals("")){
                    //请输入桌号生成二维码
                   Toast.makeText(TablePicActivity.this,getString(R.string.str366),Toast.LENGTH_SHORT).show();
                   return;
                }
                //将制定图片区保存到相册
                try {
                    BitmapUtils.saveImage(TablePicActivity.this, mypaycaode_layout);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //二维码以保存到相册
                Toast.makeText(TablePicActivity.this, getString(R.string.str367), Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
