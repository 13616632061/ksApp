package com.ui.ks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.BaseActivity;
import com.ui.entity.Order;
import com.ui.entity.OrderGoods;
import com.ui.global.Global;
import com.ui.util.PreferencesService;
import com.ui.util.PrintUtil;
import com.ui.util.SysUtils;

import java.util.ArrayList;
import java.util.Map;

public class PayOpenOrderSuccessActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_backopenoder,btn_print;
    private RelativeLayout layout_print_set;
    private TextView tv_totalprice,tv_order_id,tv_creattime;
    private int type;
    private String  order_id;
    private String  pay_status;
    private String total_price;
    private Order order;
    private String payed_time;
    private String tel;
    private String sellername="";
    private int num1;//挂单打印设置次数
    private int num2;//买单成功打印设置次数
    private ArrayList<OrderGoods> goodsList;
    private PreferencesService service;//偏好设置
    private int isprint_type=1;//1表示手动打印，2表示自动打印

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_open_order_success);
        SysUtils.setupUI(PayOpenOrderSuccessActivity.this,findViewById(R.id.activity_pay_open_order_success));
        initToolbar(this);

        initView();
    }

    private void initView() {
        btn_backopenoder= (Button) findViewById(R.id.btn_backopenoder);
        btn_print= (Button) findViewById(R.id.btn_print);
        layout_print_set= (RelativeLayout) findViewById(R.id.layout_print_set);
        tv_totalprice= (TextView) findViewById(R.id.tv_totalprice);
        tv_order_id= (TextView) findViewById(R.id.tv_order_id);
        tv_creattime= (TextView) findViewById(R.id.tv_creattime);


        btn_backopenoder.setOnClickListener(this);
        layout_print_set.setOnClickListener(this);
        btn_print.setOnClickListener(this);
        initData();

    }

    private void initData() {

        service=new PreferencesService(PayOpenOrderSuccessActivity.this);
        Map<String, String> params_isprint = service.getPerferences_isprint_success();
        isprint_type=Integer.valueOf(params_isprint.get("isprint_success"));
        Map<String, String> params_seller_info = service.getPerferences_seller_name();
        sellername=String.valueOf(params_seller_info.get("seller_name"));
        tel=String.valueOf(params_seller_info.get("tel"));
        //获取打印设置次数
        Map<String, String> print_num=service.getPerferences_print_num();
        num2=Integer.valueOf(print_num.get("num2"));

        Intent intent=getIntent();
        goodsList=new ArrayList<>();
        if(intent!=null){
            type=intent.getIntExtra("type",0);
            if(type==3){
                order_id=intent.getStringExtra("order_id");
                pay_status=intent.getStringExtra("pay_status");
                payed_time=intent.getStringExtra("payed_time");
                total_price=intent.getStringExtra("total_price");
                goodsList=intent.getParcelableArrayListExtra("goodsList");
                tv_totalprice.setText("￥"+total_price);
                tv_order_id.setText(order_id);
                tv_creattime.setText(payed_time);
                if(isprint_type==2){
                    new PrintUtil(PayOpenOrderSuccessActivity.this,sellername,payed_time,pay_status,total_price,
                            tel, order_id,goodsList,order,num2,"");
                }
            }
        }
        PayOpenOrderSuccessActivity.this.sendBroadcast(new Intent(Global.BROADCAST_SubmitOrderActivity_ACTION).putExtra("type",1) );
        PayOpenOrderSuccessActivity.this.sendBroadcast(new Intent(Global.BROADCAST_GetOpenOrder_ACTION).putExtra("type",7) );
        PayOpenOrderSuccessActivity.this.sendBroadcast(new Intent(Global.BROADCAST_OpenOrderPayCode_ACTION).putExtra("type",2) );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_backopenoder:
                PayOpenOrderSuccessActivity.this.sendBroadcast(new Intent(Global.BROADCAST_OpenOrderActivity_ACTION).putExtra("type",7) );
                Intent intent=new Intent(PayOpenOrderSuccessActivity.this,OpenOrderActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.layout_print_set:
                Intent intent_print_set=new Intent(PayOpenOrderSuccessActivity.this,PrintSetActivity.class);
                startActivity(intent_print_set);
                break;
            case R.id.btn_print:
                new PrintUtil(PayOpenOrderSuccessActivity.this,sellername,payed_time,pay_status,total_price,
                        tel, order_id,goodsList,order,num2,"");
                break;
        }
    }

}
