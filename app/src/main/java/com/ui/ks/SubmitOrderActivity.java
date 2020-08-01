package com.ui.ks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.ui.adapter.SubmitOrderAdapter;
import com.ui.entity.GetOpenOrder;
import com.ui.entity.GetOpenOrder_info;
import com.ui.entity.Order;
import com.ui.entity.OrderGoods;
import com.ui.global.Global;
import com.ui.listview.PagingListView;
import com.ui.util.CustomRequest;
import com.ui.util.DialogUtils;
import com.ui.util.NoDoubleClickUtils;
import com.ui.util.PreferencesService;
import com.ui.util.PrintUtil;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubmitOrderActivity extends BaseActivity implements View.OnClickListener {

    private int type;
    private double total_price;
    private ArrayList<GetOpenOrder> getOpenOrders_choose;
    private PagingListView list_order;
    private RelativeLayout scancode_layout,paycode_layout,cash_layout;
    private TextView tv_total_price;
    private String order_id;//订单id
    private String time;//订单时间
    private String getOpenOrders;//订单列表字符串
    private ArrayList<GetOpenOrder_info> getOpenOrder_infos;
    private PreferencesService service;
    private ImageView iv_orderfragment_back;
    private LinearLayout layout_openorder;
    private int isprint_type=1;//1表示手动打印，2表示自动打印
    private String pay_status="0";//0表示未付款，1表示付款
    private String sellername="";
    private String tel;
    private int num1;//挂单打印设置次数
    private int num2;//买单成功打印设置次数
    private int openorder=1;//0表示直接买单，1表示挂单
    public ArrayList<OrderGoods> goodsList;
    private String order_print_id;//订单打印id
    private String order_mark;//订单备注
    private String payed_time;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_order);
        SysUtils.setupUI(SubmitOrderActivity.this,findViewById(R.id.activity_submit_order));
        initToolbar(this);

        initadata();

        initView();

    }

    private void initadata() {
        service=new PreferencesService(SubmitOrderActivity.this);
        Map<String, String> params_isprint = service.getPerferences_isprint();
        isprint_type=Integer.valueOf(params_isprint.get("isprint"));
        Map<String, String> params_seller_info = service.getPerferences_seller_name();
        sellername=String.valueOf(params_seller_info.get("seller_name"));
        tel=String.valueOf(params_seller_info.get("tel"));
        //获取打印设置次数
        Map<String, String> print_num=service.getPerferences_print_num();
        num1=Integer.valueOf(print_num.get("num1"));
        num2=Integer.valueOf(print_num.get("num2"));
    }

    private void initView() {
        list_order= (PagingListView) findViewById(R.id.list_order);
        scancode_layout= (RelativeLayout) findViewById(R.id.scancode_layout);
        paycode_layout= (RelativeLayout) findViewById(R.id.paycode_layout);
        cash_layout= (RelativeLayout) findViewById(R.id.cash_layout);
        tv_total_price= (TextView) findViewById(R.id.tv_total_price);
        iv_orderfragment_back= (ImageView) findViewById(R.id.iv_orderfragment_back);
        layout_openorder= (LinearLayout) findViewById(R.id.layout_openorder);


        iv_orderfragment_back.setOnClickListener(this);
        layout_openorder.setOnClickListener(this);
        scancode_layout.setOnClickListener(this);
        paycode_layout.setOnClickListener(this);
        cash_layout.setOnClickListener(this);
        initData();
    }

    private void initData() {

        Intent intent=getIntent();
        getOpenOrders_choose=new ArrayList<>();
        if(intent!=null){
            type=intent.getIntExtra("type",0);
            if(type==1){
                total_price=Double.parseDouble(intent.getStringExtra("total_price"));
                getOpenOrders_choose=intent.getParcelableArrayListExtra("getOpenOrders");
                order_id="";
                for(int i=0;i<getOpenOrders_choose.size();i++){
                    order_id+=getOpenOrders_choose.get(i).getOrder_id()+",";
                }
                SubmitOrderAdapter submitOrderAdapter=new SubmitOrderAdapter(SubmitOrderActivity.this,getOpenOrders_choose);
                list_order.setAdapter(submitOrderAdapter);
                tv_total_price.setText(total_price+"");

            }else if(type==2){
                order_id="";
                total_price=Double.parseDouble(intent.getStringExtra("total_price"));
                order_id=intent.getStringExtra("order_id");
                time=intent.getStringExtra("time");
                getOpenOrders=intent.getStringExtra("getOpenOrders");
                System.out.println("getOpenOrders="+getOpenOrders.toString());
                try {
                    JSONArray jsonArray=new JSONArray(getOpenOrders.replace("/","|"));
                    getOpenOrder_infos=new ArrayList<>();
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String id=jsonObject.getString("goods_id");
                        String name=jsonObject.getString("name");
                        String num=jsonObject.getString("nums");
                        String price=jsonObject.getString("price");
                        GetOpenOrder_info getOpenOrder_info=new GetOpenOrder_info(id,"",name,num,price,"","");
                        getOpenOrder_infos.add(getOpenOrder_info);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("e="+e.toString());
                }
                getOpenOrders_choose.add(new GetOpenOrder(order_id,time,"","",total_price,getOpenOrder_infos,false,false));
                SubmitOrderAdapter submitOrderAdapter=new SubmitOrderAdapter(SubmitOrderActivity.this,getOpenOrders_choose);
                list_order.setAdapter(submitOrderAdapter);
                tv_total_price.setText(total_price+"");
            }
        }
    }

    /**
     * 添加挂单备注
     * @param mContext
     */
    InputMethodManager imm;
    AlertDialog mAlertDialog;
    EditText et_accountinfo_input;
    boolean isshow=false;
    String et_accountinfo_input_str;
    CheckBox is_print;
    public  void isaddmark(final Context mContext){
        AlertDialog.Builder dialog=new AlertDialog.Builder(mContext);
        View view =View.inflate(mContext, R.layout.openorder_dialog,null);
        //自动打印
        is_print= (CheckBox) view.findViewById(R.id.is_print);
        if(isprint_type==1){
            is_print.setChecked(false);
        }else if(isprint_type==2){
            is_print.setChecked(true);
        }
        is_print.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isprint_type=2;
                }else {
                    isprint_type=1;
                }
                service.save_isprint(isprint_type);

            }
        });
        et_accountinfo_input=(EditText)view.findViewById(R.id.et_input);
        et_accountinfo_input.requestFocus();
        et_accountinfo_input.setFocusable(true);
//        if(!TextUtils.isEmpty(order_mark)){
//            et_accountinfo_input.setText(order_mark);
//            et_accountinfo_input.setSelection(order_mark.length());
//        }
//        if("null".equals(order_mark)){
//            et_accountinfo_input.setText("");
//        }
        //转换成数字键盘
//        et_accountinfo_input.setKeyListener(new BaseKeyListener() {
//            @Override
//            public int getInputType() {
//                return InputType.TYPE_CLASS_NUMBER;
//            }
//        });
        et_accountinfo_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String et=et_accountinfo_input.getText().toString().trim();
                if(et!=null){
                    if(et.length()>5){
                        String et_src=et.substring(0,5);
                        et_accountinfo_input.setText(et_src);
                        et_accountinfo_input.setSelection(et_src.length());
                    }
                }
            }
        });
        //软键盘显示
        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        ImageView iv_close= (ImageView) view.findViewById(R.id.iv_close);
        TextView tv_sure=(TextView)view.findViewById(R.id.tv_sure);
        TextView tv_nomark=(TextView)view.findViewById(R.id.tv_nomark);
        //关闭
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
                if(isSoftShowing()){
                    //再次调用软键盘消失
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
//                showBottomSheet();
            }
        });
        //确定按钮
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(et_accountinfo_input.getText().toString().trim())){
                    openorder=1;
                    if (!NoDoubleClickUtils.isDoubleClick()) {{
                        putOpenOrder();
                    }}
                }else {
                    Toast.makeText(SubmitOrderActivity.this,getString(R.string.str36),Toast.LENGTH_SHORT).show();
                }
            }
        });
        //不备注
        tv_nomark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openorder=1;
                if (!NoDoubleClickUtils.isDoubleClick()) {{
                    putOpenOrder();
                }}
            }
        });
        mAlertDialog= dialog.setView(view).show();
        mAlertDialog.setCancelable(false);
        mAlertDialog.show();
    }


    /**
     * 挂单
     */
//    private JSONArray jsonArray;
    private String  total_amount;
    int orders_status;
    private void putOpenOrder(){
        if(openorder==1){
            et_accountinfo_input_str=et_accountinfo_input.getText().toString();
        }
        if(TextUtils.isEmpty(et_accountinfo_input_str)){
            et_accountinfo_input_str="null";
        }
        String  total_amount_str=tv_total_price.getText().toString().trim();
        System.out.println("total_amount_str="+total_amount_str);
        total_amount=total_amount_str.replace("￥","");
        System.out.println("total_amount="+total_amount);

        final Map<String, String> Map=new HashMap<>();
        final ArrayList<Map<String,String>> mapArrayList=new ArrayList<>();
        goodsList=new ArrayList<>();
        for(int i=0;i<getOpenOrder_infos.size();i++){
            String goods_id=getOpenOrder_infos.get(i).getId();
            String name=getOpenOrder_infos.get(i).getName();
            double price=Double.parseDouble(getOpenOrder_infos.get(i).getPrice());
            int nums=Integer.parseInt(getOpenOrder_infos.get(i).getNum());
            if(openorder==1){
                orders_status=1;//(0代表不挂单，1代表挂单)
            }else if(openorder==0){
                if(TextUtils.isEmpty(order_id)){
                    orders_status=0;//(0代表不挂单，1代表挂单)
                }else {
                    orders_status=1;//(0代表不挂单，1代表挂单)
                }
            }
            int pay_status=0;//付款款状态：pay_status（0:未支付;1:已支付;2:已付款至到担保方;3:部分付款;4:部分退款;5:全额退款）
            Map<String,String> map=new HashMap<>();
            map.put("goods_id",goods_id);
            name = name.replace(" ","");//将空格装换
            map.put("name", name);
            map.put("price",price+"");
            map.put("nums",nums+"");
            map.put("mark_text",et_accountinfo_input_str);
            map.put("orders_status",orders_status+"");
            map.put("pay_status",pay_status+"");
            mapArrayList.add(map);
            OrderGoods orderGoods=new OrderGoods(nums,name,price);
            goodsList.add(orderGoods);
        }
        Map.put("map",mapArrayList.toString());
        JSONObject jsonObject=new JSONObject(Map);
        try {
            String map_str=jsonObject.getString("map");
            jsonArray=new JSONArray(map_str.replace("/","|"));
            Map.put("map",jsonArray+"");//转化为json数组的字符串
            Map.put("total_amount",total_amount+"");//总价
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("e:"+e.toString());
        }
        System.out.println("Map="+Map);
        CustomRequest r=new CustomRequest(CustomRequest.Method.POST, SysUtils.getSellerServiceUrl("shengcheng_order"),Map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("商品ret="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    if(!status.equals("200")){
                        SysUtils.showError(message);
                    }else {
                        if(openorder==1){
                            JSONObject data = ret.getJSONObject("data");
                            if(data!=null) {
                                order_print_id = data.getString("order_id");
                                payed_time = String.valueOf(data.getLong("time"));
                                JSONArray num_str = data.getJSONArray("num");
                                JSONObject num_obj = num_str.getJSONObject(0);
                                int num = Integer.parseInt(num_obj.getString("num"));
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    order_id="";
                    order_mark="";
//                    ordercreattime="";
//                    clearShoppingCar();
                    if(mAlertDialog!=null){
                        mAlertDialog.dismiss();
                    }
                    if(imm!=null&&isSoftShowing()){
                        //再次调用软键盘消失
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                    if(openorder==1){
                        creatDialogSuccess_Gtay();
                        handler.sendEmptyMessageDelayed(201,1000);
                    }
                    SubmitOrderActivity.this.finish();
//                    bottomSheetLayout.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SysUtils.showNetworkError();
                hideLoading();
            }

        });
        executeRequest(r);
        showLoading(SubmitOrderActivity.this);
    }


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==201){
                if(malertDialog!=null) {
                    malertDialog.dismiss();
                }
                if(openorder==1&&isprint_type==2){
//                    startPrint();
//                    PrintUtil.startPrint(OpenOrderActivity.this,sellername,payed_time,pay_status,total_amount,
//                            tel,order_print_id,goodsList,order,num1);
                    new PrintUtil(SubmitOrderActivity.this,sellername,payed_time,pay_status,total_amount,
                            tel,order_print_id,goodsList,order,num1,et_accountinfo_input_str);
                }
            }

        }
    };

    /**
     * 挂单成功弹窗
     */
    AlertDialog malertDialog = null;
    private void creatDialogSuccess_Gtay(){
        View view=View.inflate(SubmitOrderActivity.this,R.layout.alertdialog_success_gray,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(SubmitOrderActivity.this,R.style.AlertDialog_success_gray);
        malertDialog=builder.setView(view).show();
        malertDialog.show();
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.layout_openorder:
                    isaddmark(SubmitOrderActivity.this);
                    break;
                case R.id.iv_orderfragment_back:
                    SubmitOrderActivity.this.finish();
                    break;
                case R.id.scancode_layout:
                    if (Build.VERSION.SDK_INT >= 23) {
                        int checkCallPhonePermission = ContextCompat.checkSelfPermission(SubmitOrderActivity.this, android.Manifest.permission.CAMERA);
                        if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(SubmitOrderActivity.this,new String[]{android.Manifest.permission.CAMERA},222);
                            return;
                        }else{
                            Intent intent=new Intent(SubmitOrderActivity.this,MipcaActivityCapture.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("type",3);
                            intent.putExtra("getOpenOrders_choose",getOpenOrders_choose);
                            intent.putExtra("total_price",total_price+"");
                            startActivity(intent);
                        }
                    } else {
                        Intent intent=new Intent(SubmitOrderActivity.this,MipcaActivityCapture.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("type",3);
                        intent.putExtra("getOpenOrders_choose",getOpenOrders_choose);
                        intent.putExtra("total_price",total_price+"");
                        startActivity(intent);
                    }
                    break;
                case R.id.paycode_layout:
                    paycode();
                    break;
                case R.id.cash_layout:
                    Intent intent=new Intent(SubmitOrderActivity.this,CashChargeActivity.class);
                    intent.putExtra("type",2);
                    intent.putExtra("getOpenOrders_choose",getOpenOrders_choose);
                    intent.putExtra("total_price",total_price+"");
                    startActivity(intent);

                    break;
            }

    }

    /**
     * 选择二维码支付
     */
    private JSONArray jsonArray;
    private void paycode(){
        Map<String,String> map= new HashMap<String,String>();
            ArrayList<Map<String,String>>  mapArrayList=new ArrayList<>();
            for(int i=0;i<getOpenOrders_choose.size();i++){
                Map<String,String> map1=new HashMap<>();
                map1.put("order_id",getOpenOrders_choose.get(i).getOrder_id());
                map1.put("price",getOpenOrders_choose.get(i).getPrice()+"");
                mapArrayList.add(map1);
            map.put("map",mapArrayList.toString());
            JSONObject jsonObject=new JSONObject(map);
            try {
                String map_str=jsonObject.getString("map");
                jsonArray=new JSONArray(map_str);
                map.put("map",jsonArray+"");//转化为json数组的字符串
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        map.put("total_fee",(new Double(total_price*100)).intValue()+"");
        map.put("pay_type","wxpayjsapi");
        map.put("auth_code","code");
        CustomRequest r = new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("common_pay"), map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("ret="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject data=null;
                    if (!status.equals("200")) {
                        if(status.equals("E.404")){
                            DialogUtils.showbuilder(SubmitOrderActivity.this,message);
                        }else {
                            SysUtils.showError(message);
                        }
                    } else {
                        data=ret.getJSONObject("data");
                        String order_id=data.getString("order_id");
                        String code_url=data.getString("url");

                        service=new PreferencesService(SubmitOrderActivity.this);
                        service.save_order_id(order_id);

                        Intent intent=new Intent(SubmitOrderActivity.this,OpenOrderPayCodeActivity.class);
                        intent.putExtra("code_url",code_url);
                        intent.putExtra("order_id",order_id);
                        intent.putExtra("getOpenOrders_choose",getOpenOrders_choose);
                        intent.putExtra("total_price",total_price+"");
                        startActivity(intent);

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
        showLoading(SubmitOrderActivity.this);
    }
    private BroadcastReceiver broadcastAffirmReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int type=intent.getIntExtra("type",0);
            if(type==1){
                finish();
            }
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        SubmitOrderActivity.this.registerReceiver(broadcastAffirmReceiver, new IntentFilter(Global.BROADCAST_SubmitOrderActivity_ACTION));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SubmitOrderActivity.this.unregisterReceiver(broadcastAffirmReceiver);
    }

    //判断软键盘是否存在
    private boolean isSoftShowing() {
        //获取当前屏幕内容的高度
        int screenHeight = getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        return screenHeight - rect.bottom != 0;
    }
}
