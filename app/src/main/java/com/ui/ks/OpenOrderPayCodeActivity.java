package com.ui.ks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.BaseActivity;
import com.ui.entity.GetOpenOrder;
import com.ui.entity.OrderGoods;
import com.ui.global.Global;
import com.ui.util.DateUtils;
import com.ui.util.PreferencesService;
import com.ui.util.QRCodeUtil;
import com.ui.util.SysUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class OpenOrderPayCodeActivity extends BaseActivity {

    private TextView tv_reality_price,tv_name;
    private ImageView iv_paycode;

    private String code_url;
    private String total_price;
    private String order_id;
    private ArrayList<OrderGoods> goodsList;
    private ArrayList<GetOpenOrder> getOpenOrders_choose;
    private String goodsname;
    private String goodsprice;
    private String goodsnum;
    private String sellername;
    private PreferencesService service;//偏好设置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_order_pay_code);
        SysUtils.setupUI(OpenOrderPayCodeActivity.this,findViewById(R.id.activity_open_order_pay_code));
        initToolbar(this);

        initView();
    }

    private void initView() {
        tv_reality_price= (TextView) findViewById(R.id.tv_reality_price);
        tv_name= (TextView) findViewById(R.id.tv_name);
        iv_paycode= (ImageView) findViewById(R.id.iv_paycode);

        service=new PreferencesService(OpenOrderPayCodeActivity.this);
        Map<String, String> params_seller_info = service.getPerferences_seller_name();
        sellername=String.valueOf(params_seller_info.get("seller_name"));

        tv_name.setText(getString(R.string.str55)+sellername+getString(R.string.str56));

        Intent intent=getIntent();
        if(intent!=null){
            code_url=intent.getStringExtra("code_url");
            order_id=intent.getStringExtra("order_id");
            total_price=intent.getStringExtra("total_price");
            getOpenOrders_choose=intent.getParcelableArrayListExtra("getOpenOrders_choose");
            tv_reality_price.setText("￥"+total_price);

            goodsList=new ArrayList<>();
            for(int i=0;i<getOpenOrders_choose.size();i++){
                boolean ishas=true;
                for(int j=0;j<getOpenOrders_choose.get(i).getGetOpenOrder_infos().size();j++){
                    goodsname= getOpenOrders_choose.get(i).getGetOpenOrder_infos().get(j).getName();
                    goodsnum= getOpenOrders_choose.get(i).getGetOpenOrder_infos().get(j).getNum();
                    goodsprice= getOpenOrders_choose.get(i).getGetOpenOrder_infos().get(j).getPrice();
                    for(int z=0;z<goodsList.size();z++){
                        if(goodsList.get(z).getName().equals(goodsname)){
                            goodsList.get(z).setQuantity(goodsList.get(z).getQuantity()+1);
                            ishas=false;
                        }
                    }
                    if(ishas){
                        goodsList.add(new OrderGoods(Integer.parseInt(goodsnum),goodsname,Double.parseDouble(goodsprice)));
                    }
                }
            }
        }

        initData();
    }

    private void initData() {
        final String filePath = QRCodeUtil.getFileRoot(this) + File.separator
                + "qr_" + System.currentTimeMillis() + ".jpg";
        //二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = QRCodeUtil.createQRImage(code_url, 800, 800, null, filePath);

                if (success) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Drawable drawable =new BitmapDrawable(BitmapFactory.decodeFile(filePath));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                iv_paycode.setBackground(drawable);
                            }else {
                                iv_paycode.setImageBitmap(BitmapFactory.decodeFile(filePath));
                            }
                        }
                    });
                }
            }
        }).start();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int type=intent.getIntExtra("type",0);
            if(type==2){
                finish();
            }
            if(type==1){
                long time= intent.getLongExtra("time",0);
                Intent intent1=new Intent(OpenOrderPayCodeActivity.this,PayOpenOrderSuccessActivity.class);
                intent1.putExtra("type",3);
                intent1.putExtra("order_id",order_id);
                intent1.putExtra("total_price",total_price);
                intent1.putExtra("payed_time", DateUtils.getDateTimeFromMillisecond(Long.parseLong(time+"")*1000)+"");
                intent1.putExtra("pay_status",1+"");
                intent1.putExtra("goodsList",goodsList);
                startActivity(intent1);
            }
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
//        clearShoppingCarDB();
        OpenOrderPayCodeActivity.this.registerReceiver(broadcastReceiver, new IntentFilter(Global.BROADCAST_OpenOrderPayCode_ACTION));
    }
}
