package com.ui.ks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.ui.adapter.ShoppingCartOrderSureAdapter;
import com.ui.entity.AddressList;
import com.ui.entity.ShopperCartInfo;
import com.ui.global.Global;
import com.library.utils.BigDecimalArith;
import com.ui.util.CustomRequest;
import com.ui.util.DateUtils;
import com.ui.util.SetEditTextInput;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShoppingCartOrderSureActivity extends BaseActivity implements View.OnClickListener {

    private View toolbar_layout;
    private TextView btn_set,tv_total_money,tv_name,tv_phone,tv_adress,tv_submitorders,tv_area;
    private ListView list_content;
    private ArrayList<ShopperCartInfo> shopperCartInfos_isSeclect;
    private ShoppingCartOrderSureAdapter shoppingCartOrderSureAdapter;
    private RelativeLayout layout_adress;
    private AddressList mAdress;
    private  double total_money = 0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart_order_sure);
        SysUtils.setupUI(ShoppingCartOrderSureActivity.this,findViewById(R.id.activity_shopping_cart_order_sure));
        initToolbar(this);

        initView();
        getAdressList();
    }

    private void initView() {
        toolbar_layout=findViewById(R.id.toolbar_layout);
        btn_set= (TextView) toolbar_layout.findViewById(R.id.btn_set);
        btn_set.setText("编辑");
        list_content= (ListView) findViewById(R.id.list_content);
        tv_total_money= (TextView) findViewById(R.id.tv_total_money);
        tv_name= (TextView) findViewById(R.id.tv_name);
        tv_phone= (TextView) findViewById(R.id.tv_phone);
        tv_adress= (TextView) findViewById(R.id.tv_adress);
        tv_submitorders= (TextView) findViewById(R.id.tv_submitorders);
        tv_area= (TextView) findViewById(R.id.tv_area);
        layout_adress= (RelativeLayout) findViewById(R.id.layout_adress);

        layout_adress.setOnClickListener(this);
        tv_submitorders.setOnClickListener(this);

        Intent intent=getIntent();
        if(intent!=null){
                shopperCartInfos_isSeclect = intent.getParcelableArrayListExtra("shopperCartInfos_isSeclect");
                if (shopperCartInfos_isSeclect.size() > 0) {
                    shoppingCartOrderSureAdapter = new ShoppingCartOrderSureAdapter(ShoppingCartOrderSureActivity.this, shopperCartInfos_isSeclect);
                    list_content.setAdapter(shoppingCartOrderSureAdapter);
                            total_money = 0.00;
                    for (int i = 0; i < shopperCartInfos_isSeclect.size(); i++) {
                        String money_str = shopperCartInfos_isSeclect.get(i).getTotal_amount();
                        double money = Double.parseDouble(money_str);
                        total_money = BigDecimalArith.add(money, total_money);
                        //配送费
                        if(total_money<Double.parseDouble(shopperCartInfos_isSeclect.get(i).getFreight())){
                            tv_total_money.setText("￥" + BigDecimalArith.add(total_money,Double.parseDouble(SetEditTextInput.stringpointtwo(shopperCartInfos_isSeclect.get(i).getReward()))));
                        }else {
                            tv_total_money.setText("￥" + total_money);
                        }
                    }
                }
            }
        }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_adress:
                Intent intent=new Intent(ShoppingCartOrderSureActivity.this,SelectAdressActivity.class);
                startActivityForResult(intent,201);
                break;
            case R.id.tv_submitorders:
                submintOrderInfos();

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==201&& resultCode==202){
            mAdress=data.getParcelableExtra("mAddress");
            tv_name.setText(mAdress.getName());
            tv_phone.setText(mAdress.getMobile());
            tv_area.setText(mAdress.getArea());
            tv_adress.setText(mAdress.getAddr());
        }
    }
    //获取收货地址列表
    private void getAdressList(){
        Map<String,String> map=new HashMap<>();
            map.put("acquiesce",1+"");
        CustomRequest customRequest=new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("address_list"),map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("获取地址ret="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject data=ret.getJSONObject("data");
                    if(!status.equals("200")){
                        SysUtils.showError(message);
                    }else {
                        JSONArray address=data.getJSONArray("address");
                        if(address!=null){
                            for(int i=0;i<address.length();i++){
                                JSONObject address_object=address.getJSONObject(i);
                                String id=address_object.getString("id");
                                String seller_id=address_object.getString("seller_id");
                                String name=address_object.getString("name");
                                String mobile=address_object.getString("mobile");
                                String addr=address_object.getString("addr");
                                String area=address_object.getString("area");
                                String acquiesce=address_object.getString("acquiesce");
                                tv_name.setText(name);
                                tv_phone.setText(mobile);
                                tv_area.setText(area);
                                tv_adress.setText(addr);
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
                SysUtils.showNetworkError();

            }
        });
        executeRequest(customRequest);
        showLoading(ShoppingCartOrderSureActivity.this);
    }
    //提交订单
    private void submintOrderInfos(){
        Map<String,String> map=new HashMap<>();
        String tv_name_str=tv_name.getText().toString().trim();
        String tv_phone_str=tv_phone.getText().toString().trim();
        String tv_adress_str=tv_adress.getText().toString().trim();
        String tv_total_money_str=tv_total_money.getText().toString().trim();
        if(TextUtils.isEmpty(tv_name_str)){
            SysUtils.showError("收货人姓名不能为空！");
            return;
        } if(TextUtils.isEmpty(tv_phone_str)){
            SysUtils.showError("收货人电话不能为空！");
            return;
        }if(TextUtils.isEmpty(tv_adress_str)){
            SysUtils.showError("收货人地址不能为空！");
            return;
        }
        ArrayList<Map<String,String>> commoditylist=new ArrayList<>();
        double total_cost_freight=0.00;
        for(int i=0;i<shopperCartInfos_isSeclect.size();i++){
            String cart_id=shopperCartInfos_isSeclect.get(i).getCart_id();//购物车id
            String supply_id=shopperCartInfos_isSeclect.get(i).getSeller_id();//供应商
            String total_amount=shopperCartInfos_isSeclect.get(i).getTotal_amount();//总费用
            String cost_freight=null;//配送费
            String amount=null;//商品金额
            if(Double.parseDouble(total_amount)>Double.parseDouble(shopperCartInfos_isSeclect.get(i).getFreight())){
                amount=total_amount;
                cost_freight=0.00+"";
            }else {
                cost_freight=shopperCartInfos_isSeclect.get(i).getReward();
                amount=total_amount;
            }
            String shipping_id=null;
            if(shopperCartInfos_isSeclect.get(i).isSend()){
                shipping_id="15";//配送
            }else {
                shipping_id="11";//自提
                cost_freight=0.00+"";
            }
            total_cost_freight=BigDecimalArith.add(Double.parseDouble(cost_freight),total_cost_freight);
            String ship_time=shopperCartInfos_isSeclect.get(i).getSendtime();//预约时间
            if(TextUtils.isEmpty(ship_time)){
                SysUtils.showError("预约时间不能为空！");
                return;
            }
            String mark_text=shopperCartInfos_isSeclect.get(i).getMessage();//留言
            if(TextUtils.isEmpty(mark_text)){
                mark_text="null";
            }
            ArrayList<Map<String,String>> goods_infolist=new ArrayList<>();
            for(int j=0;j<shopperCartInfos_isSeclect.get(i).getShopperCartInfo_items().size();j++){
                String goods_id=shopperCartInfos_isSeclect.get(i).getShopperCartInfo_items().get(j).getGoods_id();
                String goods_name=shopperCartInfos_isSeclect.get(i).getShopperCartInfo_items().get(j).getGoods_name();
                String goods_nums=shopperCartInfos_isSeclect.get(i).getShopperCartInfo_items().get(j).getGoods_nums();
                String goods_price=shopperCartInfos_isSeclect.get(i).getShopperCartInfo_items().get(j).getGoods_price();
                Map<String,String> map_goods_info=new HashMap<>();
                map_goods_info.put("goods_id",goods_id);
                map_goods_info.put("goods_name",goods_name);
                map_goods_info.put("goods_nums",goods_nums);
                map_goods_info.put("goods_price",goods_price);
                goods_infolist.add(map_goods_info);
            }
            Map<String,String> map_commodity=new HashMap<>();
            map_commodity.put("goods_info",goods_infolist.toString());
            JSONObject goods_info_object=new JSONObject(map_commodity);
            try {
                String goods_info_object_str=goods_info_object.getString("goods_info");
                JSONArray goods_info_array=new JSONArray(goods_info_object_str.replace("/","|"));
                map_commodity.put("goods_info",goods_info_array+"");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            map_commodity.put("cart_id",cart_id);
            map_commodity.put("supply_id",supply_id);
            map_commodity.put("cost_freight",cost_freight);
            map_commodity.put("amount",amount);
            map_commodity.put("shipping_id",shipping_id);
            map_commodity.put("mark_text",mark_text);
            map_commodity.put("ship_time", DateUtils.dataOne(ship_time));
            commoditylist.add(map_commodity);
        }
        map.put("commodity",commoditylist.toString());
        JSONObject commodity_object=new JSONObject(map);
        try {
            String commodity_object_str=commodity_object.getString("commodity");
            JSONArray commodity_array=new JSONArray(commodity_object_str.replace("/","|"));
            map.put("commodity",commodity_array+"");//商品信息
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("commodity_e="+e.toString());
        }
            map.put("nums_amount",total_money+"");//商品总金额
            map.put("ship_name",tv_name_str);//收货人
            map.put("ship_tel",tv_phone_str);//电话
            map.put("ship_area",tv_area.getText().toString().trim());//地区
            map.put("ship_addr",tv_adress_str);//详细地址
            map.put("nums_cost_freight",total_cost_freight+"");//总配送费

        System.out.println("map="+map);
        CustomRequest customRequest=new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("place_order"),map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("提交订单ret="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject data=ret.getJSONObject("data");
                    if(!status.equals("200")){
                        SysUtils.showError(message);
                    }else {
                        String order_id=data.getString("order_id");
                        String money=data.getString("money");
                        Intent intent_submit=new Intent(ShoppingCartOrderSureActivity.this,PaySureActivity.class);
                        intent_submit.putExtra("order_id",order_id);
                        intent_submit.putExtra("money",money);
                        startActivity(intent_submit);
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
                SysUtils.showNetworkError();

            }
        });
        executeRequest(customRequest);
        showLoading(ShoppingCartOrderSureActivity.this);
    }
private BroadcastReceiver mBroadcastReceiver=new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        int type=intent.getIntExtra("type",0);
        String Reward_str=intent.getStringExtra("Reward");
        if(Reward_str==null){
            Reward_str=0.00+"";
        }
        String total_money_str=tv_total_money.getText().toString().trim().replace("￥","");
        double Reward=Double.parseDouble(SetEditTextInput.stringpointtwo(Reward_str));
        double total_money=Double.parseDouble(total_money_str);
        if(type==1){
            tv_total_money.setText("￥"+BigDecimalArith.sub(total_money,Reward));
        }else if(type==2){
            tv_total_money.setText("￥"+BigDecimalArith.add(total_money,Reward));
        }

    }
};
    @Override
    protected void onResume() {
        super.onResume();
        ShoppingCartOrderSureActivity.this.registerReceiver(mBroadcastReceiver,new IntentFilter(Global.BROADCAST_ShoppingCartOrderSureActivity_ACTION));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ShoppingCartOrderSureActivity.this.unregisterReceiver(mBroadcastReceiver);
    }
}
