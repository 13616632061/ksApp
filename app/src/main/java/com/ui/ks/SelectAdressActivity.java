package com.ui.ks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.ui.adapter.AddressListAdapter;
import com.ui.entity.AddressList;
import com.ui.global.Global;
import com.ui.util.CustomRequest;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectAdressActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_addadress;
    private ListView list_content;
    private ArrayList<AddressList> mAddressList;
    private AddressListAdapter mAddressListAdapter;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_adress);
        SysUtils.setupUI(SelectAdressActivity.this,findViewById(R.id.activity_select_adress));
        initToolbar(this);

        intiView();

    }

    private void intiView() {
        btn_addadress= (Button) findViewById(R.id.btn_addadress);
        list_content= (ListView) findViewById(R.id.list_content);

        btn_addadress.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_addadress:
                Intent intent=new Intent(SelectAdressActivity.this,AddAdressActivity.class);
                startActivity(intent);
                break;
        }
    }
    //获取收货地址列表
    private void getAdressList(){

        CustomRequest customRequest=new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("address_list"), null, new Response.Listener<JSONObject>() {
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
                        mAddressList=new ArrayList<>();
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
                                AddressList addressList=new AddressList(id,seller_id,name,mobile,area,addr,acquiesce);
                                mAddressList.add(addressList);
                            }
                            mAddressListAdapter=new AddressListAdapter(SelectAdressActivity.this,mAddressList);
                            list_content.setAdapter(mAddressListAdapter);
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
        showLoading(SelectAdressActivity.this);
    }
    //删除地址和设默认地址
    private void setAdress(final int position){
        String url = null;
        Map<String,String> map=new HashMap<>();

        if(type==2){
            System.out.println("position="+position+"   mAddressList="+mAddressList);
            map.put("id",mAddressList.get(position).getId());
            map.put("acquiesce",mAddressList.get(position).getAcquiesce());
            url=SysUtils.getSellerServiceUrl("set_default");
        }else if(type==1){
            map.put("id",mAddressList.get(position).getId());
            url=SysUtils.getSellerServiceUrl("address_delete");
        }
        System.out.print("map"+map);
        CustomRequest customRequest=new CustomRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONObject ret = SysUtils.didResponse(jsonObject);
                    System.out.println("ret="+ret);
                    String status = ret.getString("status");
                    String message = ret.getString("message");
                    JSONObject  data=null;
                    if(!status.equals("200")){
                        SysUtils.showError(message);
                    }else {
                        getAdressList();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SysUtils.showNetworkError();

            }
        });
        executeRequest(customRequest);
    }
private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        /**
         * type=1删除地址，2设置默认地址，3选择地址
         */
        type=intent.getIntExtra("type",0);
        int position=intent.getIntExtra("position",0);
        if(type==3){
            Intent Intent=new Intent();
            Intent.putExtra("mAddress",mAddressList.get(position));
            SelectAdressActivity.this.setResult(202,Intent);
            finish();
        }else if(type==1||type==2){
            setAdress(position);
        }

    }
};
    @Override
    protected void onResume() {
        super.onResume();
        getAdressList();
        SelectAdressActivity.this.registerReceiver(broadcastReceiver,new IntentFilter(Global.BROADCAST_SelectAdressActivity_ACTION));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SelectAdressActivity.this.unregisterReceiver(broadcastReceiver);
    }
}
