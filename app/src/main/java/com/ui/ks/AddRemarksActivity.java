package com.ui.ks;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.base.BaseActivity;
import com.ui.util.CustomRequest;
import com.ui.util.SysUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2020/3/20.
 */

public class AddRemarksActivity extends BaseActivity implements View.OnClickListener {


    Button btn_add_remarks;
    EditText ed_order_remarks;
    EditText ed_goods_remarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remarks);
        initToolbar(this);
        initView();
        LoadDatas();
    }

    public void LoadDatas(){
        String url=SysUtils.getnewsellerUrl("Menu/menu");
        CustomRequest r=new CustomRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideLoading();
                try {
                    JSONObject object=new JSONObject(response.toString());
                    JSONObject ret = null;
                    ret = object.getJSONObject("response");
                    String status=ret.getString("status");
                    if (status.equals("0")) {
                        JSONObject data=ret.getJSONObject("data");
                        JSONArray ja=data.getJSONArray("memo_order");
                        String memo_order="";
                        for (int i=0;i<ja.length();i++){
                            if (i==ja.length()-1){
                                memo_order+=ja.getString(i);
                            }else {
                                memo_order+=ja.getString(i)+" ";
                            }
                        }
                        JSONArray jg=data.getJSONArray("memo_goods");
                        String memo_goods="";
                        for (int i=0;i<jg.length();i++){
                            if (i==jg.length()-1){
                                memo_goods+=jg.getString(i);
                            }else {
                                memo_goods+=jg.getString(i)+" ";
                            }
                        }
                        ed_order_remarks.setText(memo_order);
                        ed_goods_remarks.setText(memo_goods);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
            }
        });
        executeRequest(r);
        showLoading(AddRemarksActivity.this);
    }



    private void initView() {
        btn_add_remarks= (Button) findViewById(R.id.btn_add_remarks);
        btn_add_remarks.setOnClickListener(this);

        ed_order_remarks= (EditText) findViewById(R.id.ed_order_remarks);
        ed_goods_remarks= (EditText) findViewById(R.id.ed_goods_remarks);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add_remarks:
                if (ed_order_remarks.getText().toString().equals("")){
                    //订单备注不能为空
                    Toast.makeText(AddRemarksActivity.this,getString(R.string.str364),Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ed_goods_remarks.getText().toString().equals("")){
                    //商品备注不能为空
                    Toast.makeText(AddRemarksActivity.this,getString(R.string.str365),Toast.LENGTH_SHORT).show();
                    return;
                }
                AddRemarks(ed_order_remarks.getText().toString(),ed_goods_remarks.getText().toString());
                break;
        }
    }




    public void AddRemarks(String memo_order,String memo_goods){
        String url = SysUtils.getnewsellerUrl("Menu/updateMenu");
        Map<String,String> map =new HashMap<>();
        map.put("memo_order",memo_order);
        map.put("memo_goods",memo_goods);
        CustomRequest r=new CustomRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideLoading();
                try {
                    JSONObject object=new JSONObject(response.toString());
                    JSONObject ret = null;
                    ret = object.getJSONObject("response");
                    String status=ret.getString("status");
                    if (status.equals("0")) {
                        Toast.makeText(AddRemarksActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
            }
        });
        executeRequest(r);
        showLoading(AddRemarksActivity.this);
    }


}
