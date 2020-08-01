package com.ui.ks;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.ui.adapter.GetOpenOrderAdapter;
import com.ui.entity.GetOpenOrder;
import com.ui.entity.GetOpenOrder_info;
import com.ui.entity.Order;
import com.ui.entity.OrderGoods;
import com.ui.global.Global;
import com.ui.listview.PagingListView;
import com.ui.util.BluetoothService;
import com.ui.util.CustomRequest;
import com.ui.util.PreferencesService;
import com.ui.util.PrintUtil;
import com.ui.util.SetEditTextInput;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetOpenOrderActivity extends BaseActivity implements View.OnClickListener {
    private View layout_err, include_nowifi, include_noresult;
    private Button load_btn_refresh_net, load_btn_retry;
    private TextView load_tv_noresult;

    private PagingListView list_order;
    private Button btn_choose_ok;
    private TextView tv_price_sum;
    private ImageView iv_back,iv_cell,iv_search;
    private EditText et_inputgoodname;
    private ArrayList<GetOpenOrder> getOpenOrders;
    private ArrayList<GetOpenOrder> getOpenOrders_choose;
    private ArrayList<GetOpenOrder_info> getOpenOrder_infos;
    private RelativeLayout fragment;//顶部栏
    private GetOpenOrderAdapter adapter;
    private int position;//点击展开订单详情的位置
    private double total_price=0.00;//选中商品的总价

    private String order_id = "";
    private String sellername="";
    private String pay_status="0";
    private String payed_time;
    private String total_fee_double;
    private Order order;
    public ArrayList<OrderGoods> goodsList;
    private PreferencesService service;
    private String tel;
    private boolean is_search=false;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothService mService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_open_order);
        SysUtils.setupUI(GetOpenOrderActivity.this,findViewById(R.id.activity_get_open_order));
        initToolbar(this);
        initView();
    }

    private void initView() {
        layout_err = (View)findViewById(R.id.layout_err);
        include_noresult = layout_err.findViewById(R.id.include_noresult);
        load_btn_retry = (Button) layout_err.findViewById(R.id.load_btn_retry);
        load_btn_retry.setVisibility(View.GONE);
        load_tv_noresult = (TextView) layout_err.findViewById(R.id.load_tv_noresult);
        load_tv_noresult.setText(getString(R.string.str37));
        load_tv_noresult.setCompoundDrawablesWithIntrinsicBounds(
                0, //left
                R.drawable.icon_no_result_melt, //top
                0, //right
                0//bottom
        );
        include_nowifi = layout_err.findViewById(R.id.include_nowifi);
        load_btn_refresh_net = (Button) include_nowifi.findViewById(R.id.load_btn_refresh_net);
        load_btn_refresh_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //重新加载数据
                getopenorder_num();
            }
        });

        list_order= (PagingListView) findViewById(R.id.list_order);
        btn_choose_ok= (Button) findViewById(R.id.btn_choose_ok);
        tv_price_sum= (TextView ) findViewById(R.id.tv_price_sum);
        fragment= (RelativeLayout) findViewById(R.id. fragment);

        iv_back= (ImageView) findViewById(R.id. iv_back);
        iv_cell= (ImageView) findViewById(R.id. iv_cell);
        iv_search= (ImageView) findViewById(R.id.iv_search);
        et_inputgoodname= (EditText) findViewById(R.id.et_inputgoodname);

        service=new PreferencesService(GetOpenOrderActivity.this);
         Map<String,String> stringMap= service.getPerferences_seller_name();
        sellername=stringMap.get("seller_name");
        tel=stringMap.get("tel");

        iv_back.setOnClickListener(this);
        iv_cell.setOnClickListener(this);
        iv_search.setOnClickListener(this);

        et_inputgoodname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String goodsname=s.toString().trim();
                if(goodsname.length()>0){
                    iv_cell.setVisibility(View.VISIBLE);
                }else {
                    iv_cell.setVisibility(View.GONE);
                }
            }
        });
        et_inputgoodname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH){
                    String goodsname=et_inputgoodname.getText().toString().trim();
                    if(!TextUtils.isEmpty(goodsname)){
                        is_search=true;
                        getopenorder_num();
                    }else {
                        Toast.makeText(GetOpenOrderActivity.this,getString(R.string.str48),Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });

        initData();
    }
    private void initData(){
        getopenorder_num();
    }
    /**
     * 获取挂单订单列表及搜索
     */
    String mark_text;
    private void getopenorder_num(){
        getOpenOrders=new ArrayList<>();
        Map<String,String> map=new HashMap<>();
        if(is_search){
            String mark_text=et_inputgoodname.getText().toString().trim();
            map.put("mark_text",mark_text);
        }
        CustomRequest r=new CustomRequest(CustomRequest.Method.POST, SysUtils.getSellerServiceUrl("get_order_info"),map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("挂单ret="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    if(!status.equals("200")){
                        SysUtils.showError(message);
                    }else {
                        JSONArray data=ret.getJSONArray("data");
                        if(data!=null){
                            for(int i=0;i<data.length();i++){
                                JSONObject data_info=data.getJSONObject(i);
                                String order_id=data_info.getString("order_id");
                                String createtime=data_info.getString("createtime");
                                mark_text=data_info.getString("mark_text");
                                String name=data_info.getString("name");
                                double price=data_info.getDouble("price");
                                String obj_id=data_info.getString("obj_id");
                                JSONArray goods=data_info.getJSONArray("goods");
                                if(goods!=null) {
                                    getOpenOrder_infos=new ArrayList<>();
                                    for (int j = 0; j < goods.length(); j++) {
                                        JSONObject goods_info=goods.getJSONObject(j);
                                        String goods_name=goods_info.getString("goods_name");
                                        String goods_id=goods_info.getString("goods_id");
                                        String goods_price=goods_info.getString("goods_price");
                                        String goods_num=goods_info.getString("goods_num");
                                        String item_id=goods_info.getString("item_id");
                                        String tag_id=goods_info.getString("tag_id");

                                        GetOpenOrder_info getOpenOrder_info=new GetOpenOrder_info(goods_id,tag_id,goods_name,goods_num,goods_price,obj_id,item_id);
                                        getOpenOrder_infos.add(getOpenOrder_info);
                                    }
                                }
                                GetOpenOrder getOpenOrder=new GetOpenOrder(order_id,createtime,mark_text,name,price,getOpenOrder_infos,false,false);
                                getOpenOrders.add(getOpenOrder);


                            }
                        }
                        setView();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    hideLoading();
                    adapter=new GetOpenOrderAdapter(GetOpenOrderActivity.this,getOpenOrders);
                    list_order.setAdapter(adapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setNoNetwork();
                hideLoading();
                SysUtils.showNetworkError();
            }

        });
        executeRequest(r);
        showLoading(GetOpenOrderActivity.this);

    }
    /**
     * 作废订单
     */
    private void delete_order(String order_id, final int position){
        Map<String,String> map=new HashMap<>();
        map.put("order_id",order_id);
        CustomRequest r=new CustomRequest(CustomRequest.Method.POST, SysUtils.getSellerServiceUrl("delete_order"),map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("作废ret="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    if(!status.equals("200")){
                        SysUtils.showError(message);
                    }else {
                        getOpenOrders.remove(position);
                        adapter.notifyDataSetChanged();
                        setView();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    hideLoading();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setNoNetwork();
                hideLoading();
                SysUtils.showNetworkError();
            }

        });
        executeRequest(r);
        showLoading(GetOpenOrderActivity.this);

    }
    /**
     * 接收广播
     *
     */
    AlertDialog malertDialog=null;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            /**
             * type:1点击展开订单详情，type=2,选中商品，type=3,取消选中商品,type=4,作废订单，5订单编辑,6订单打印,7页面finish
             */
            int Type=intent.getIntExtra("type",0);
            if(Type==7){
                finish();
            }
            if(Type==1){
                position=intent.getIntExtra("position",0);
                list_order.smoothScrollToPositionFromTop(position,0);//滚动到顶部
            }else if(Type==2){
                    double price=intent.getDoubleExtra("price",0.00);
                BigDecimal bd1 = new BigDecimal(Double.toString(price));
                BigDecimal bd2 = new BigDecimal(Double.toString(total_price));
                total_price=bd2.add(bd1).doubleValue();
//                total_price+=price;
            }else if(Type==3){
                double price=intent.getDoubleExtra("price",0.00);
                BigDecimal bd1 = new BigDecimal(Double.toString(price));
                BigDecimal bd2 = new BigDecimal(Double.toString(total_price));
                total_price=bd2.subtract(bd1).doubleValue();
//                total_price-=price;
            }else if(Type==4){
                final String order_id=intent.getStringExtra("order_id");
                final int position=intent.getIntExtra("position",0);

                AlertDialog.Builder alertDialog=new AlertDialog.Builder(GetOpenOrderActivity.this,R.style.AlertDialog)
                        .setMessage(getString(R.string.str49))
                        .setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delete_order(order_id,position);
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                malertDialog.dismiss();
                            }
                        });
                malertDialog=alertDialog.show();
                malertDialog.show();
            }else if(Type==5){
                finish();
            }else if(Type==6){
                order_id=intent.getStringExtra("order_id");
                payed_time=intent.getStringExtra("creat_time");
                total_fee_double=intent.getStringExtra("price");
               final String order_mark=intent.getStringExtra("order_mark");
                goodsList=intent.getParcelableArrayListExtra("goodsList");


                new MaterialDialog.Builder(GetOpenOrderActivity.this)
                        .theme(SysUtils.getDialogTheme())
                        .content(getString(R.string.sure_small_ticket))
                        .positiveText(getString(R.string.sure))
                        .negativeText(getString(R.string.cancel))
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
//                                PrintUtil.startPrint(GetOpenOrderActivity.this,sellername,payed_time,pay_status,total_fee_double,
//                                        tel, order_id,goodsList,order,1);
                                new PrintUtil(GetOpenOrderActivity.this,sellername,payed_time,pay_status,total_fee_double,
                                        tel, order_id,goodsList,order,1,order_mark);
                            }
                        })
                        .show();
            }
            if(total_price>0){
                tv_price_sum.setText("￥"+ SetEditTextInput.stringpointtwo(total_price+""));
            }else {
                tv_price_sum.setText("￥"+0.00);
            }
//只要有选中订单，则选好按钮高亮
            for(int i=0;i<getOpenOrders.size();i++){
                if(getOpenOrders.get(i).ischoose()==true){
                    btn_choose_ok.setBackgroundResource(R.drawable.btn_corner_orange);
                    btn_choose_ok.setOnClickListener(GetOpenOrderActivity.this);
                    break;
                }else {
                    btn_choose_ok.setBackgroundResource(R.drawable.btn_corner_gray);
                    btn_choose_ok.setOnClickListener(null);
                }
            }

        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        GetOpenOrderActivity.this.registerReceiver(broadcastReceiver, new IntentFilter(Global.BROADCAST_GetOpenOrder_ACTION));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_choose_ok:
                String str_price=tv_price_sum.getText().toString().trim();
                String  total_price=str_price.replace("￥","");
                getOpenOrders_choose=new ArrayList<>();
                    for (int i = 0; i < getOpenOrders.size(); i++) {
                        if (getOpenOrders.get(i).ischoose() == true) {
                            getOpenOrders_choose.add(getOpenOrders.get(i));
                        }
                    }
                    Intent intent = new Intent(GetOpenOrderActivity.this, SubmitOrderActivity.class);
                    intent.putExtra("type", 1);
                    intent.putExtra("total_price", total_price);
                    intent.putExtra("getOpenOrders", getOpenOrders_choose);
                    startActivity(intent);
                break;

            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_cell:
                et_inputgoodname.setText("");
                break;
            case R.id.iv_search:
                String str_inputgoodname=et_inputgoodname.getText().toString().trim();
                if(!TextUtils.isEmpty(str_inputgoodname)){
                    is_search=true;
                    getopenorder_num();
                }else {
                    Toast.makeText(GetOpenOrderActivity.this,getString(R.string.str48),Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void setView() {
        if(getOpenOrders.size() < 1) {
            //没有结果
            list_order.setVisibility(View.GONE);
            include_nowifi.setVisibility(View.GONE);
            include_noresult.setVisibility(View.VISIBLE);
            layout_err.setVisibility(View.VISIBLE);
        } else {
            //有结果
            list_order.setVisibility(View.VISIBLE);
            include_noresult.setVisibility(View.GONE);
            include_nowifi.setVisibility(View.GONE);
            layout_err.setVisibility(View.GONE);
        }
    }

    private void setNoNetwork() {
        //网络不通
        if(getOpenOrders.size() < 1) {
            if(!include_nowifi.isShown()) {
                list_order.setVisibility(View.GONE);
                include_noresult.setVisibility(View.GONE);
                include_nowifi.setVisibility(View.VISIBLE);
                layout_err.setVisibility(View.VISIBLE);
            }
        } else {
            SysUtils.showNetworkError();
        }
    }
}
