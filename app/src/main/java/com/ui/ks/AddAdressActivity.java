package com.ui.ks;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.google.gson.Gson;
import com.ui.entity.AddressList;
import com.ui.entity.City;
import com.ui.entity.County;
import com.ui.entity.Province;
import com.ui.util.CustomRequest;
import com.ui.util.SysUtils;
import com.ui.util.area_selectdialog.CityPickerDialog;
import com.ui.util.area_selectdialog.Util;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddAdressActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_name,et_phone,et_adress;
    private RelativeLayout layout_area;
    private TextView tv_area;
    private Button btn_addadress;
    private ArrayList<Province> provinces = new ArrayList<Province>();
    private  int type=0;//0表示新增地址，1表示地址编辑
    private AddressList mAddressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_adress);
        SysUtils.setupUI(AddAdressActivity.this,findViewById(R.id.activity_add_adress));
        initToolbar(this);

        initView();
    }

    private void initView() {
        et_name= (EditText) findViewById(R.id.et_name);
        et_name.setTextColor(Color.parseColor("#000000"));
        et_phone= (EditText) findViewById(R.id.et_phone);
        et_adress= (EditText) findViewById(R.id.et_adress);
        layout_area= (RelativeLayout) findViewById(R.id.layout_area);
        tv_area= (TextView) findViewById(R.id.tv_area);
        btn_addadress= (Button) findViewById(R.id.btn_addadress);

        layout_area.setOnClickListener(this);
        btn_addadress.setOnClickListener(this);

        Intent Intent=getIntent();
        if(Intent!=null){
            type=Intent.getIntExtra("type",0);
            if(type==1){
                setToolbarTitle("编辑收货地址");
                mAddressList=Intent.getParcelableExtra("mAddressList");
                et_name.setText(mAddressList.getName());
                et_phone.setText(mAddressList.getMobile());
                tv_area.setText(mAddressList.getArea());
                et_adress.setText(mAddressList.getAddr());
            }else {
                setToolbarTitle("新增收货地址");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_area:
                if (provinces.size() > 0) {
                    showAddressDialog();
                } else {
                    new InitAreaTask(AddAdressActivity.this).execute(0);
                }
                break; case R.id.btn_addadress:
              AddAdress();
                break;
        }
    }
 private void AddAdress(){
     String name=et_name.getText().toString().trim();
     String mobile=et_phone.getText().toString().trim();
     String addr=et_adress.getText().toString().trim();
     String area=tv_area.getText().toString().trim();
     if(TextUtils.isEmpty(name)){
         SysUtils.showError("联系人姓名不能为空！");
         return;
     } if(TextUtils.isEmpty(mobile)){
         SysUtils.showError("联系电话不能为空！");
         return;
     }  if(TextUtils.isEmpty(area)){
         SysUtils.showError("地址区域不能为空！");
         return;
     } if(TextUtils.isEmpty(addr)){
         SysUtils.showError("详细地址不能为空！");
         return;
     }
     Map<String,String>  map=new HashMap<>();
     map.put("name",name);
     map.put("mobile",mobile);
     map.put("addr",addr);
     map.put("area",area);
     if(type==1){
         map.put("id",mAddressList.getId());
     }
     System.out.print("map"+map);
     CustomRequest customRequest=new CustomRequest(Request.Method.POST, SysUtils.getSellerServiceUrl("address"), map, new Response.Listener<JSONObject>() {
         @Override
         public void onResponse(JSONObject jsonObject) {
             hideLoading();
             try {
                 JSONObject ret = SysUtils.didResponse(jsonObject);
                 System.out.println("添加地址ret="+ret);
                 String status = ret.getString("status");
                 String message = ret.getString("message");
                 JSONObject  data=null;
                 if(!status.equals("200")){
                     SysUtils.showError(message);
                 }else {
                    Intent intent=new Intent(AddAdressActivity.this,SelectAdressActivity.class);
                     startActivity(intent);
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
     showLoading(AddAdressActivity.this);
 }
    /**
     * 地址选择器的diog
     */
    private void showAddressDialog() {
        new CityPickerDialog(AddAdressActivity.this, provinces, null, null, null,
                new CityPickerDialog.onCityPickedListener() {

                    @Override
                    public void onPicked(Province selectProvince,
                                         City selectCity, County selectCounty) {
                        StringBuilder address = new StringBuilder();
                        address.append(
                                selectProvince != null ? selectProvince
                                        .getAreaName() : "")
                                .append(selectCity != null ? selectCity
                                        .getAreaName() : "")
                                .append(selectCounty != null ? selectCounty
                                        .getAreaName() : "");
                        String text = selectCounty != null ? selectCounty
                                .getAreaName() : "";
                        tv_area.setText(address);
                    }
                }).show();
    }

    /**
     * 初始化地址地区
     */
    private class InitAreaTask extends AsyncTask<Integer, Integer, Boolean> {

        Context mContext;

        Dialog progressDialog;

        public InitAreaTask(Context context) {
            mContext = context;
            progressDialog = Util.createLoadingDialog(mContext, getString(R.string.str92), true,
                    0);
        }

        @Override
        protected void onPreExecute() {

            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();
            if (provinces.size()>0) {
                showAddressDialog();
            } else {
                Toast.makeText(mContext, "数据初始化失败", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            String address = null;
            InputStream in = null;
            try {
                in = mContext.getResources().getAssets().open("address.txt");
                byte[] arrayOfByte = new byte[in.available()];
                in.read(arrayOfByte);
                address = EncodingUtils.getString(arrayOfByte, "UTF-8");
                JSONArray jsonList = new JSONArray(address);
                Gson gson = new Gson();
                for (int i = 0; i < jsonList.length(); i++) {
                    try {
                        provinces.add(gson.fromJson(jsonList.getString(i),
                                Province.class));
                    } catch (Exception e) {
                    }
                }
                return true;
            } catch (Exception e) {
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                    }
                }
            }
            return false;
        }

    }
}
