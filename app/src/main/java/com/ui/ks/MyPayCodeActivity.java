package com.ui.ks;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.base.BaseActivity;
import com.ui.util.BitmapUtils;
import com.ui.util.QRCodeUtil;
import com.ui.util.SysUtils;

import java.io.File;

/**
 * 我的二维码页面
 */

public class MyPayCodeActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout savephoto_layout;
    private RelativeLayout mypaycaode_layout;
    private ImageView iv_mypaycaode;
    private TextView tv_seller_name;
    private  String  seller_id;
    private String seller_name;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pay_code);

        SysUtils.setupUI(this, findViewById(R.id.activity_my_pay_code));

        initToolbar(this);
        initView();
    }

    private void initView() {

        savephoto_layout= (RelativeLayout) findViewById(R.id.savephoto_layout);
        mypaycaode_layout= (RelativeLayout) findViewById(R.id.mypaycaode_layout);
        iv_mypaycaode= (ImageView) findViewById(R.id.iv_mypaycaode);
        tv_seller_name= (TextView) findViewById(R.id.tv_seller_name);

        savephoto_layout.setOnClickListener(this);

        Intent intent=getIntent();
        seller_name=intent.getStringExtra("seller_name");
        seller_id=intent.getStringExtra("seller_id");

        //正式的
//      code="http://new.czxshop.com/wap/scanPayment-"+seller_id+".html";
      code="http://new.zjzccn.com/wap/scanPayment-"+seller_id+".html";
//        code="http://www.czxshop.net/index.php/wap/scanPayment-"+seller_id+".html";

        tv_seller_name.setText(seller_name);

        final String filePath = QRCodeUtil.getFileRoot(MyPayCodeActivity.this) + File.separator
                + "qr_" + System.currentTimeMillis() + ".jpg";
        //二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
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
                                iv_mypaycaode.setBackground(drawable);
                            }else {
                                iv_mypaycaode.setImageBitmap(BitmapFactory.decodeFile(filePath));
                            }
//                            iv_mypaycaode.setImageBitmap(BitmapFactory.decodeFile(filePath));
                        }
                    });
                }
            }
        }).start();


    }

    @Override
    public void onClick(View v) {
        if(SysUtils.isFastDoubleClick()){
            return;
        }
        switch (v.getId()){
            case R.id.savephoto_layout:
                //将制定图片区保存到相册
                try {
                    BitmapUtils.saveImage(MyPayCodeActivity.this, mypaycaode_layout);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(MyPayCodeActivity.this, getString(R.string.str26), Toast.LENGTH_SHORT).show();

                break;
        }
    }

}
